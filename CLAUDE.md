@AGENTS.md

## Claude Code — specifické příkazy

Build/test běží jen v kontejneru (viz [ADR-011](documentation/decisions/ADR-011-vyvojove-prostredi-v-kontejnerech-a-testovaci-strategie.md)), nikdy na hostiteli přímo:

```bash
docker build -t diktafon-dev -f .devcontainer/Dockerfile .devcontainer
```

**Doporučený způsob spuštění (rychlý, bez bind-mountu):** Docker Desktop na Windows mountuje
`-v "$(pwd)":/workspace` přes pomalý most mezi NTFS diskem a WSL2 (naměřeno ~150× pomalejší na
operacích s malým souborem než nativní úložiště kontejneru — 13 s vs. 87 ms na 1500 souborech).
Místo bind-mountu se zdrojové soubory pošlou do kontejneru jednorázově přes `tar` rouru, build
běží na rychlém internímu úložišti kontejneru a výsledek (APK, test reporty) se pošle zpátky ven
stejnou cestou:

```bash
tar -cf - --exclude='.git' --exclude='app/build' --exclude='_workspace' . | \
  docker run --rm -i \
    -v diktafon-gradle-cache:/root/.gradle \
    -v diktafon-android-home:/root/.android \
    diktafon-dev sh -c '
      mkdir -p /workspace && tar -xf - -C /workspace
      cd /workspace && ./gradlew test assembleDebug > /tmp/build.log 2>&1
      GRADLE_EXIT=$?
      cat /tmp/build.log >&2
      tar -cf - app/build/outputs 2>/dev/null
      exit $GRADLE_EXIT
    ' > /tmp/diktafon-output.tar
BUILD_EXIT=$?
tar -xf /tmp/diktafon-output.tar -C .
echo "BUILD_EXIT=$BUILD_EXIT"
```

Důležité: Gradle log jde uvnitř kontejneru přes `> /tmp/build.log 2>&1` a zpátky ven přes
`cat ... >&2` (stderr) — stdout kontejneru musí zůstat čistě binární (jen `tar` výstup), jinak
se log smíchá s archivem a `tar -xf` na hostiteli ho nepůjde rozbalit spolehlivě. Reálně
naměřeno na plném `test assembleDebug`: **~8 minut touto cestou vs. 12–20 minut přes
bind-mount.** Výstupní `app/build/outputs/...` se extrahuje zpět na stejné místo jako při
bind-mountu, takže na čtení APK/testovacích výsledků se nic nemění.

**Jednodušší (pomalejší) alternativa přes bind-mount**, užitečná např. když potřebuješ, aby
kontejner viděl soubory živě během jejich úpravy (ne jen jednorázový snímek):

```bash
docker run --rm -v "$(pwd)":/workspace -v diktafon-gradle-cache:/root/.gradle -v diktafon-android-home:/root/.android diktafon-dev sh -c "cd /workspace && ./gradlew test"
```

**Poznámka pro Git Bash na Windows:** MSYS automaticky převádí cesty v `-v "$(pwd)":/workspace`,
což může mount umlčet na prázdný adresář (v kontejneru pak `./gradlew: not found`). Pokud se to
stane, spusť příkaz s `MSYS_NO_PATHCONV=1` před `docker run`, nebo použij cestu ve tvaru
`//d/data/vyvoj/Diktafon` místo `$(pwd)`.

Druhý `-v` mount (`diktafon-gradle-cache`) je pojmenovaný Docker volume, který mezi jednotlivými
`docker run` běhy drží `$GRADLE_USER_HOME` (stažené závislosti, cache buildu) — bez něj by se
likvidovaly při každém `--rm` běhu kontejneru a Gradle Wrapper by si navíc zbytečně znovu
stahoval vlastní distribuci Gradle (přestože je v image předinstalovaná v `/opt/gradle`, ale
`./gradlew` ji nepoužije).

Třetí `-v` mount (`diktafon-android-home`) je nutný pro `assembleDebug` — bez něj si AGP při
každém `--rm` běhu kontejneru vygeneruje nový náhodný debug keystore (`~/.android/debug.keystore`),
takže by byl každý sestavený debug APK podepsaný jiným klíčem a instalace novější verze přes
starší by na zařízení skončila chybou "balíček je v konfliktu se stávajícím balíčkem" — Android
odmítá přepsat nainstalovanou aplikaci jinak podepsanou verzí stejného `applicationId`. S tímto
mountem zůstává debug keystore stejný napříč všemi buildy.

**Nikdy nespouštěj dva `docker run` build/test příkazy současně** — oba sdílejí stejný
`diktafon-gradle-cache` volume a druhý běh skončí na `Timeout waiting to lock journal cache`.
Pokud se to stane (typicky po přerušeném/zapomenutém běhu na pozadí), zkontroluj
`docker ps` a počkej, až doběhne/skončí, než spustíš další.

## Klíčové soubory pro orientaci

- [AGENTS.md](AGENTS.md) — pravidla a konvence (jediný zdroj pravdy)
- [README.md](README.md) — přehled projektu
- [ai/skills/](ai/skills/) — návody "jak na to"
- [documentation/decisions/](documentation/decisions/) — architektonická rozhodnutí

## Konvence

- Commit zprávy: TBD (doporučení: anglicky, konvenční formát)
- Kód: TBD jazyk/styl podle zvoleného stacku
