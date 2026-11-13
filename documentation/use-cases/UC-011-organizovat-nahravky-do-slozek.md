# UC-011 — Organizovat nahrávky do složek

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-13
**Změněno:** 2026-11-13
**Zdroje:**
- Doplněno na popud uživatele v rámci revize, 2026-11-13 (tato konverzace) — chyběl use-case pokrývající [FR-AUD-011](../requirements/FR-AUD-011-organizace-nahravek-do-slozek.md).

## Aktér

Uživatel (rozšířené použití — viz [ADR-010](../decisions/ADR-010-tri-urovne-pouziti-bezne-rozsirene-administracni.md)).

## Předpoklady

Existuje alespoň jedna nahrávka (viz [UC-001](UC-001-poridit-a-ulozit-hlasovy-zaznam.md), [UC-002](UC-002-importovat-externi-nahravku.md)).

## Hlavní tok

1. Uživatel otevře správu složek (rozšířené použití, odděleno od běžného použití).
2. Vytvoří novou složku, případně přejmenuje nebo smaže existující.
3. Vybranou nahrávku (nebo více nahrávek) přesune do zvolené složky nebo ji ze složky vyjme zpět mimo složky.
4. Prochází seznam nahrávek po jednotlivých složkách namísto jednoho plochého seznamu.

## Alternativní toky

- **A1:** Uživatel maže neprázdnou složku → aplikace vyřeší obsah podle zvoleného chování (TODO — viz otevřená otázka v [FR-AUD-011](../requirements/FR-AUD-011-organizace-nahravek-do-slozek.md)).
- **A2:** Uživatel zvolí/změní cílovou složku už při zahájení nového nahrávání nebo importu, místo dodatečného přesunu — viz [UC-001](UC-001-poridit-a-ulozit-hlasovy-zaznam.md), [UC-002](UC-002-importovat-externi-nahravku.md).

## Výsledek

Nahrávky jsou organizované do uživatelem definované struktury složek; organizace do složek je nezávislá na logickém seskupení artefaktů jedné nahrávky (viz [ADR-005](../decisions/ADR-005-lokalni-perzistence-vsech-mezivysledku.md)).

## Související

- Požadavek: [FR-AUD-011](../requirements/FR-AUD-011-organizace-nahravek-do-slozek.md), [FR-AUD-005](../requirements/FR-AUD-005-seznam-nahravek-s-metadaty.md)
- ADR: [ADR-005](../decisions/ADR-005-lokalni-perzistence-vsech-mezivysledku.md), [ADR-010](../decisions/ADR-010-tri-urovne-pouziti-bezne-rozsirene-administracni.md)
- Modul: (zatím žádný — moduly nejsou dosud definovány, viz poznámka v [`documentation/modules/README.md`](../modules/README.md))
