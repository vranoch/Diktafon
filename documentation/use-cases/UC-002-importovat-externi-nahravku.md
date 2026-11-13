# UC-002 — Importovat externí nahrávku

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-12
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — kapitola 5, UC-002
- Upřesnění uživatele v rámci revize, 2026-11-12 (tato konverzace) — vznik nahrávky zrcadlí [UC-001](UC-001-poridit-a-ulozit-hlasovy-zaznam.md) (volba složky, předgenerovaný název, poznámka), místo spuštění záznamu se ale vyhledá a vloží existující soubor.

## Aktér

Uživatel.

## Předpoklady

Existuje kompatibilní audio soubor dostupný přes systémový file picker.

## Hlavní tok

1. Uživatel založí novou nahrávku stejně jako v [UC-001](UC-001-poridit-a-ulozit-hlasovy-zaznam.md) — volitelně zvolí/změní cílovou složku (viz [FR-AUD-011](../requirements/FR-AUD-011-organizace-nahravek-do-slozek.md)) a aplikace nabídne předgenerovaný název k případné změně i volitelný zápis poznámky (viz [FR-AUD-005](../requirements/FR-AUD-005-seznam-nahravek-s-metadaty.md)).
2. Místo spuštění záznamu zvolí „Importovat“.
3. Otevře se systémový file picker.
4. Uživatel vybere kompatibilní audio soubor (např. z externího diktafonu).
5. Aplikace ověří čitelnost a formát souboru.
6. Aplikace bezpečně zkopíruje nebo zpřístupní soubor a vloží jej do takto založené nahrávky.
7. Nahrávka se zobrazí v seznamu (ve zvolené složce, s názvem a poznámkou).

## Alternativní toky

- **A1:** Nepodporovaný kodek.
- **A2:** Příliš velký soubor.
- **A3:** Zrušení výběru souboru.
- **A4:** Ztráta oprávnění k URI souboru.

TODO: upřesnit, zda lze při jednom importu vybrat a vložit více souborů najednou (např. více epizod z externího diktafonu) — vztah k [FR-AUD-009](../requirements/FR-AUD-009-spojovani-epizod-do-jednoho-zaznamu.md) je zatím otevřená otázka.

## Výsledek

Import lze zpracovat bez trvalé závislosti na externím médiu.

## Související

- Požadavek: [FR-AUD-002](../requirements/FR-AUD-002-import-existujici-nahravky.md), [FR-AUD-005](../requirements/FR-AUD-005-seznam-nahravek-s-metadaty.md), [FR-AUD-011](../requirements/FR-AUD-011-organizace-nahravek-do-slozek.md), [NFR-PERF-001](../requirements/NFR-PERF-001-limity-delky-velikosti-nahravky.md)
- Modul: (zatím žádný — moduly nejsou dosud definovány)
