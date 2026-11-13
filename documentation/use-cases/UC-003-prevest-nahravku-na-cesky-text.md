# UC-003 — Převést nahrávku na český text

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-12
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — kapitola 5, UC-003

## Aktér

Uživatel; sekundární aktér: STT provider.

## Předpoklady

Existuje audio, je dostupná konektivita a platná konfigurace API.

## Hlavní tok

1. Uživatel vybere nahrávku.
2. Volitelně zvolí jazyk přepisu (výchozí čeština, viz [FR-STT-001](../requirements/FR-STT-001-odeslani-audia-do-stt-a-ulozeni-prepisu.md)).
3. Spustí přepis.
4. Aplikace zkontroluje limity a stav sítě.
5. Aplikace odešle audio STT službě včetně zvoleného jazyka.
6. Aplikace uloží odpověď a metadata modelu (provider, model, jazyk).
7. Zobrazí se editovatelný přepis.

## Alternativní toky

- **A1:** Chyba autorizace 401/403.
- **A2:** Překročení limitu 429.
- **A3:** Limit velikosti souboru.
- **A4:** Timeout.
- **A5:** Serverová chyba.
- **A6:** Prázdný nebo chybný přepis — lze opakovat bez ztráty audia.

## Výsledek

Existuje verzovaný přepis, nebo jasná chyba.

## Související

- Požadavek: [FR-STT-001](../requirements/FR-STT-001-odeslani-audia-do-stt-a-ulozeni-prepisu.md), [FR-STT-002](../requirements/FR-STT-002-jazykove-a-terminologicke-napovedy-pro-stt.md), [FR-STT-003](../requirements/FR-STT-003-editovatelnost-prepisu.md), [FR-CFG-001](../requirements/FR-CFG-001-sprava-api-klice.md), [FR-CFG-003](../requirements/FR-CFG-003-informovani-o-chybach-limitech-a-offline-stavu.md), [NFR-REL-002](../requirements/NFR-REL-002-perzistence-stavu-workflow.md)
- Modul: (zatím žádný — moduly nejsou dosud definovány)
