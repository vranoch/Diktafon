# ADR-011 — Vývojové prostředí v kontejnerech a testovací strategie

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-13
**Změněno:** 2026-11-13
**Zdroje:**
- Upřesnění uživatele v rámci revize, 2026-11-13 (tato konverzace) — vývoj/kompilace/testování výhradně ve vývojových kontejnerech, bez lokálně instalovaného Android/Kotlin SDK.

## Kontext

Uživatel nemá a nechce mít lokálně nainstalované vývojové prostředí pro Kotlin/Android. Vývoj, kompilace i testování proto musí běžet ve vývojových kontejnerech (Docker). Pro otestování use-cases (viz [`documentation/use-cases/`](../use-cases/)) by se hodil Android emulátor.

Ověřeno v tomto prostředí (2026-11-13): Docker Desktop (WSL2 backend) je dostupný, CPU podporuje virtualizační rozšíření (VT-x), ale **`/dev/kvm` není dostupné** ani uvnitř Docker kontejneru, ani přímo ve WSL2 distribuci — a to ani po zapnutí `nestedVirtualization=true` v `.wslconfig` a restartu WSL. Ruční `modprobe kvm_intel` skončil na `Operation not permitted` (Microsoftí WSL2 kernel blokuje načítání virtualizačních modulů). Bez KVM běží Android emulátor jen čistě softwarově (QEMU TCG) — bootování v řádu minut, každá interakce pomalá — pro opakované testování všech use-cases prakticky nepoužitelné.

## Rozhodnutí

1. Veškerý build, lint a testy běží ve vývojovém kontejneru (`.devcontainer/`), nikdy na hostiteli přímo.
2. Hlavní automatizované pokrytí use-cases zajišťuje **Robolectric** (běží na JVM v kontejneru, bez nutnosti emulátoru/KVM) — simuluje Android framework natolik, aby šlo otestovat navigaci, workflow a byznys logiku popsanou v [`documentation/use-cases/`](../use-cases/).
3. Reálný Android emulátor (bez HW akcelerace, tedy pomalý) se používá jen pro orientační smoke-test (instalace a spuštění aplikace), ne jako hlavní testovací smyčka.
4. Funkčnosti závislé na konkrétním hardwaru/firmwaru (mikrofon, nahrávání při zhasnutém displeji, chování E Ink displeje, BOOX-specifické chování — viz např. [FR-AUD-007](../requirements/FR-AUD-007-nahravani-pri-zhasnutem-displeji-na-pozadi.md)) nelze v tomto prostředí ověřit vůbec; zůstávají označené `OVĚŘIT` a vyžadují ruční test na reálném zařízení.
5. Volání STT/LLM API se při automatizovaných testech nahrazuje lokálním mockem (žádné reálné volání, žádné náklady, deterministické odpovědi) — viz [FR-AI-001](../requirements/FR-AI-001-zamenitelna-rozhrani-provideru-stt-llm.md). Reálné API se ověřuje ručně, mimo automatizovanou testovací smyčku.

## Důsledky

- Rychlá zpětná vazba (Robolectric běží v řádu sekund) za cenu nižší věrnosti simulace oproti reálnému zařízení/emulátoru.
- Pokrytí use-cases bude v této fázi vždy jen částečné — hardwarově závislé kroky zůstanou neověřené automatizovaně (konzistentní s existujícími `OVĚŘIT` značkami, např. [FR-AUD-007](../requirements/FR-AUD-007-nahravani-pri-zhasnutem-displeji-na-pozadi.md), [NFR-UX-002](../requirements/NFR-UX-002-e-ink-optimalizace-ui.md)).
- Pokud se v budoucnu objeví potřeba věrnějšího ověření (CI s reálně HW-akcelerovaným emulátorem, např. GitHub Actions runner s KVM), je to revisit trigger pro doplnění tohoto ADR — nejde o řešení v tomto prostředí.
- Změna `.wslconfig` (přidáno `nestedVirtualization=true`) zůstává na hostitelském stroji; sama o sobě bez efektu, ale neškodí.

## Související

- Požadavek: [FR-AI-001](../requirements/FR-AI-001-zamenitelna-rozhrani-provideru-stt-llm.md)
- Use-case: (týká se způsobu ověření všech UC, viz [`documentation/use-cases/README.md`](../use-cases/README.md))
- ADR: [ADR-001](ADR-001-nativni-android-aplikace.md), [ADR-002](ADR-002-bez-vlastni-serverove-casti-v-mvp.md)
