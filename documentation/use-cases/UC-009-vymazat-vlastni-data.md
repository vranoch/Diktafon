# UC-009 — Vymazat vlastní data

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-14
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — kapitola 5, UC-009
- [ADR-015](../decisions/ADR-015-principy-ui-ux-pro-e-ink-a-proces-navrhu-obrazovek.md) — proces návrhu obrazovek, krok 5: sekce Obrazovky a tok se doplňuje po schválení mockupu.

## Aktér

Uživatel.

## Předpoklady

Existuje nahrávka nebo odvozený artefakt určený ke smazání.

## Hlavní tok

1. Uživatel vybere záznam.
2. Prohlédne si závislé artefakty.
3. Potvrdí odstranění.
4. Aplikace odstraní lokální data podle zvolené retence.

## Alternativní toky

- **A1:** Neúspěšné odstranění některého souboru — uživatel je informován.

## Výsledek

Lokální smazání je provedeno a diagnostikováno; **nelze tím automaticky zaručit odstranění dat u externího AI poskytovatele**.

## Obrazovky a tok

Realizováno mockupem [Diktafon – návrh obrazovek](https://claude.ai/artifact/1iuLje7CMomhtKSSiLFw4d) (viz [ADR-015](../decisions/ADR-015-principy-ui-ux-pro-e-ink-a-proces-navrhu-obrazovek.md)): mazání je kontextová ikona přímo u artefaktu (epizoda/přepis/celý záznam v Detailu nahrávky, prázdná složka ve Složkách) místo samostatné obrazovky potvrzení.

## Související

- Požadavek: [FR-DATA-001](../requirements/FR-DATA-001-mazani-nahravek-prepisu-a-vysledku.md), [FR-DATA-002](../requirements/FR-DATA-002-konfigurovatelna-retencni-politika.md), [NFR-SEC-003](../requirements/NFR-SEC-003-dpia-pred-zpracovanim-osobnich-zdravotnich-udaju.md)
- Modul: (zatím žádný — moduly nejsou dosud definovány)
