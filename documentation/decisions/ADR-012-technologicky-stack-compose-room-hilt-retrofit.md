# ADR-012 — Technologický stack Android implementace (Compose, Room, Hilt, Retrofit)

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-13
**Změněno:** 2026-11-13
**Zdroje:**
- Upřesnění uživatele v rámci revize, 2026-11-13 (tato konverzace) — volba konkrétních knihoven pro zahájení implementace.

## Kontext

[ADR-001](ADR-001-nativni-android-aplikace.md) stanovil nativní Kotlin Android aplikaci, ale konkrétní knihovny pro UI, lokální perzistenci, dependency injection a síťovou komunikaci s AI providery (viz [ADR-004](ADR-004-vymenitelni-externi-ai-poskytovatele.md), [NFR-MAINT-001](../requirements/NFR-MAINT-001-oddeleni-vrstev-architektury.md)) dosud nebyly zvoleny. Jde o nové závislosti projektu, které podle [AGENTS.md §7](../../AGENTS.md) vyžadují výslovné schválení uživatele.

## Rozhodnutí

Zvolen běžný, dobře zdokumentovaný mainstream Android stack:

- **UI:** Jetpack Compose (deklarativní UI, dobře testovatelné přes Compose testing API, lépe se hodí pro Robolectric-based testování dle [ADR-011](ADR-011-vyvojove-prostredi-v-kontejnerech-a-testovaci-strategie.md)).
- **Lokální perzistence:** Room (SQLite) pro strukturovaná metadata (nahrávky, přepisy, šablony, složky — viz [ADR-005](ADR-005-lokalni-perzistence-vsech-mezivysledku.md)); samotná audio/text data zůstávají jako soubory ve vlastním úložišti aplikace.
- **Dependency injection:** Hilt.
- **Síťová komunikace s AI providery:** Retrofit + OkHttp + kotlinx.serialization, za rozhraním vyměnitelných provider adaptérů (viz [ADR-004](ADR-004-vymenitelni-externi-ai-poskytovatele.md), [FR-AI-001](../requirements/FR-AI-001-zamenitelna-rozhrani-provideru-stt-llm.md)).

## Důsledky

- Oddělení vrstev (UI/Compose, doména, Room, provider adaptéry) odpovídá požadavku [NFR-MAINT-001](../requirements/NFR-MAINT-001-oddeleni-vrstev-architektury.md) — provider adaptéry lze nahradit bez zásahu do UI/domény/perzistence.
- Hilt přidává generovaný kód a build-time komplexitu navíc oproti ruční DI — akceptováno výměnou za menší množství boilerplate kódu.
- Volba je mainstream a dobře zdokumentovaná, což usnadňuje práci v omezeném kontejnerovém prostředí bez lokálního IDE (viz [ADR-011](ADR-011-vyvojove-prostredi-v-kontejnerech-a-testovaci-strategie.md)).
- Konkrétní verze knihoven, minSdk/targetSdk a struktura Gradle modulů se zavedou při scaffoldingu projektu; nejsou předmětem tohoto ADR.

## Související

- Požadavek: [NFR-MAINT-001](../requirements/NFR-MAINT-001-oddeleni-vrstev-architektury.md), [FR-AI-001](../requirements/FR-AI-001-zamenitelna-rozhrani-provideru-stt-llm.md)
- ADR: [ADR-001](ADR-001-nativni-android-aplikace.md), [ADR-004](ADR-004-vymenitelni-externi-ai-poskytovatele.md), [ADR-005](ADR-005-lokalni-perzistence-vsech-mezivysledku.md), [ADR-011](ADR-011-vyvojove-prostredi-v-kontejnerech-a-testovaci-strategie.md)
