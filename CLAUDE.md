@AGENTS.md

## Claude Code — specifické příkazy

Build/test běží jen v kontejneru (viz [ADR-011](documentation/decisions/ADR-011-vyvojove-prostredi-v-kontejnerech-a-testovaci-strategie.md)), nikdy na hostiteli přímo:

```bash
docker build -t diktafon-dev -f .devcontainer/Dockerfile .devcontainer
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

## Klíčové soubory pro orientaci

- [AGENTS.md](AGENTS.md) — pravidla a konvence (jediný zdroj pravdy)
- [README.md](README.md) — přehled projektu
- [ai/skills/](ai/skills/) — návody "jak na to"
- [documentation/decisions/](documentation/decisions/) — architektonická rozhodnutí

## Konvence

- Commit zprávy: TBD (doporučení: anglicky, konvenční formát)
- Kód: TBD jazyk/styl podle zvoleného stacku
