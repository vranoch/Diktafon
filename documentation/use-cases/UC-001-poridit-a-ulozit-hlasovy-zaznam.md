# UC-001 — Pořídit a uložit hlasový záznam

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-12
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — kapitola 5, UC-001
- Upřesnění uživatele v rámci revize, 2026-11-12 (tato konverzace) — volba cílové složky a předgenerovaný název/poznámka při zahájení nahrávání.

## Aktér

Uživatel.

## Předpoklady

Aplikace má oprávnění mikrofonu a dostatek úložiště.

## Hlavní tok

1. Uživatel spustí nahrávání („Nová nahrávka“) — volitelně zvolí/změní cílovou složku, do které nahrávka patří (viz [FR-AUD-011](../requirements/FR-AUD-011-organizace-nahravek-do-slozek.md)).
2. Aplikace nabídne předgenerovaný název nahrávky; uživatel jej může ihned změnit a volitelně rovnou zapsat poznámku (viz [FR-AUD-005](../requirements/FR-AUD-005-seznam-nahravek-s-metadaty.md)).
3. Vidí indikaci běhu nahrávání a uběhlý čas.
4. Volitelně nahrávání pozastaví (Pauza) a později v téže nahrávce pokračuje (append) — kroky 3–4 se mohou opakovat.
5. Zastaví nahrávání (Stop).
6. Aplikace atomicky uloží soubor a metadata (včetně zvolené složky, názvu a poznámky).
7. Aplikace nabídne přehrání či zpracování nahrávky.

## Alternativní toky

- **A1:** Odmítnuté oprávnění k mikrofonu.
- **A2:** Nedostatek úložiště.
- **A3:** Přerušení telefonním hovorem (vhodné řešit jako automatickou Pauzu).
- **A4:** Aplikace zabita operačním systémem (i během Pauzy).
- **A5:** Záznam při uspání zařízení není podporován.

## Výsledek

Korektní nahrávka je lokálně uložena, nebo je explicitně indikováno selhání bez falešného úspěchu.

## Související

- Požadavek: [FR-AUD-001](../requirements/FR-AUD-001-novy-zaznam-vestavenym-mikrofonem.md), [FR-AUD-003](../requirements/FR-AUD-003-ui-stavu-nahravani-a-ochrana-pred-zahozenim.md), [FR-AUD-004](../requirements/FR-AUD-004-lokalni-ulozeni-audia-pred-odeslanim-do-stt.md), [FR-AUD-005](../requirements/FR-AUD-005-seznam-nahravek-s-metadaty.md), [FR-AUD-006](../requirements/FR-AUD-006-prehrani-nahravky-pred-odeslanim.md), [FR-AUD-007](../requirements/FR-AUD-007-nahravani-pri-zhasnutem-displeji-na-pozadi.md), [FR-AUD-011](../requirements/FR-AUD-011-organizace-nahravek-do-slozek.md), [NFR-REL-001](../requirements/NFR-REL-001-zachovani-dat-pri-selhani-ai-volani.md)
- Modul: (zatím žádný — moduly nejsou dosud definovány)
