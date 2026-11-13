# UC-008 — Obnovit přerušené zpracování

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-12
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — kapitola 5, UC-008

## Aktér

Uživatel.

## Předpoklady

Dříve rozpracovaná úloha existuje v perzistentním stavu.

## Hlavní tok

1. Aplikace se vrátí po chybě, restartu nebo ztrátě konektivity.
2. Načte perzistentní stav.
3. Zobrazí poslední úspěšný krok.
4. Umožní pokračovat z uložených dat.
5. Před opakovaným placeným voláním upozorní na možnost dvojího účtování.

## Alternativní toky

- (žádné zaznamenané)

## Výsledek

Uživatel nepřijde o dosud uložená data.

## Související

- Požadavek: [FR-LLM-005](../requirements/FR-LLM-005-opakovani-jen-stt-nebo-jen-strukturovani.md), [NFR-REL-001](../requirements/NFR-REL-001-zachovani-dat-pri-selhani-ai-volani.md), [NFR-REL-002](../requirements/NFR-REL-002-perzistence-stavu-workflow.md), [NFR-REL-003](../requirements/NFR-REL-003-omezene-opakovani-sitovych-chyb.md)
- Modul: (zatím žádný — moduly nejsou dosud definovány)
