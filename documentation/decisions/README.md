# Architektonická rozhodnutí (ADR)

Každé netriviální architektonické nebo designové rozhodnutí se zapisuje jako samostatný
ADR soubor `ADR-NNN-kratky-nazev.md` podle šablony [`_template.md`](_template.md).

## Pravidla

- ADR se po schválení **neupravuje** — pokud se rozhodnutí změní, založí se nový ADR, který
  starý nahrazuje (`Stav: SUPERSEDED by ADR-NNN`), původní zůstává jako historický záznam.
- Každý ADR musí mít v [indexu](#index) řádek.
- Status: `NAVRŽENO` → `PŘIJATO` → `SUPERSEDED` / `ZAMÍTNUTO`.

## Index

| ID | Název | Stav | Datum | Shrnutí |
|---|---|---|---|---|
| [ADR-001](ADR-001-nativni-android-aplikace.md) | Nativní Android aplikace | PŘIJATO | 2026-11-12 | Kotlin Android APK, prioritně BOOX, bez povinného BOOX SDK. |
| [ADR-002](ADR-002-bez-vlastni-serverove-casti-v-mvp.md) | Bez vlastní serverové části v MVP | PŘIJATO | 2026-11-12 | Přímé volání externích STT/LLM API z klienta, bez backendu. |
| [ADR-003](ADR-003-oddelene-kroky-stt-a-llm.md) | Oddělené kroky STT a LLM | PŘIJATO | 2026-11-12 | Výchozí workflow audio→přepis→strukturování jako dva kroky. |
| [ADR-004](ADR-004-vymenitelni-externi-ai-poskytovatele.md) | Vyměnitelní externí AI poskytovatelé | PŘIJATO | 2026-11-19 | Provider-neutrální rozhraní; OpenAI jako reálná implementace (STT, strukturování, draft šablony, rozpoznání šablony), mock výchozí. |
| [ADR-005](ADR-005-lokalni-perzistence-vsech-mezivysledku.md) | Lokální perzistence všech mezivýsledků | PŘIJATO | 2026-11-12 | Oddělené entity; pro uživatele tvoří artefakty jedné nahrávky jednu jednotku manipulace. |
| [ADR-006](ADR-006-schema-sablony-je-kontrakt.md) | Schéma šablony je kontrakt | PŘIJATO | 2026-11-19 | Verzované JSON Schema + instrukce + lokální validace; pole má oddělený technický klíč a zobrazovaný popisek. |
| [ADR-007](ADR-007-serverless-proxy-az-pri-zmene-okolnosti.md) | Serverless proxy až při změně okolností | PŘIJATO | 2026-11-12 | Žádná serverová vrstva v MVP; odloženo do naplnění triggeru. |
| [ADR-008](ADR-008-synchronizace-neni-soucasti-mvp-aplikacnich-dat.md) | Synchronizace není součástí MVP aplikačních dat | PŘIJATO | 2026-11-12 | Data žijí lokálně, export/předání je explicitní akce uživatele. |
| [ADR-009](ADR-009-sablony-vznikaji-z-prikladu-pres-llm-s-iterativnim-testovanim.md) | Šablony vznikají z příkladu přes LLM, s iterativním testováním | PŘIJATO | 2026-11-12 | Šablona se odvodí z příkladového textu přes LLM, draft lze opakovaně testovat. |
| [ADR-010](ADR-010-tri-urovne-pouziti-bezne-rozsirene-administracni.md) | Tři úrovně použití: běžné, rozšířené a administrační | PŘIJATO | 2026-11-12 | UI odděluje běžné, rozšířené a administrační použití; administrace znatelně oddělena. |
| [ADR-011](ADR-011-vyvojove-prostredi-v-kontejnerech-a-testovaci-strategie.md) | Vývojové prostředí v kontejnerech a testovací strategie | PŘIJATO | 2026-11-13 | Build/test jen v kontejnerech; bez HW-akcelerovaného emulátoru hlavní pokrytí přes Robolectric. |
| [ADR-012](ADR-012-technologicky-stack-compose-room-hilt-retrofit.md) | Technologický stack Android implementace (Compose, Room, Hilt, Retrofit) | PŘIJATO | 2026-11-13 | Compose + Room + Hilt + Retrofit/OkHttp/kotlinx.serialization. |
| [ADR-013](ADR-013-hierarchicka-struktura-slozek.md) | Hierarchická struktura složek | PŘIJATO | 2026-11-13 | Složky jako strom libovolné hloubky; mazání jen prázdné složky; naposledy použitá složka. |
| [ADR-014](ADR-014-navigace-strom-slozek-kontextove-akce.md) | Navigace: strom složek jako hlavní cesta, kontextové akce na nahrávce | PŘIJATO | 2026-11-22 | Minimalistické Home, procházení jako prohlížeč, administrace znatelně oddělená; operace nad nahrávkou se vrací do původní složky. |
| [ADR-015](ADR-015-principy-ui-ux-pro-e-ink-a-proces-navrhu-obrazovek.md) | Principy UI/UX pro E Ink a proces návrhu obrazovek | PŘIJATO | 2026-11-14 | Konkrétní čísla pro kontrast/dotykovou plochu/text/animace; proces screen-flow mockupu a odkazu z UC před implementací. |
