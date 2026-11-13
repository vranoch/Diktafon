# UC-010 — Zpracovat audio s nadiktovanou šablonou

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-12
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — kapitola 5, UC-010
- Upřesnění uživatele v rámci revize, 2026-11-12 (tato konverzace) — zařazeno do MVP, doplněn krok explicitní změny návrhu.

## Aktér

Uživatel.

## Předpoklady

Existuje přepis nahrávky (viz [UC-003](UC-003-prevest-nahravku-na-cesky-text.md)); schválený katalog šablon je definován.

## Hlavní tok

1. Uživatel nahraje/importuje audio a nechá jej přepsat.
2. Aplikace z obsahu přepisu rozpozná případnou formulaci typu dokumentu.
3. Aplikace navrhne šablonu ze schváleného katalogu (pokud nic nerozpozná, nechá volbu na uživateli).
4. Uživatel navrženou šablonu buď potvrdí, nebo ji před pokračováním změní na jinou ze schváleného katalogu.
5. Pokračuje se podle [UC-004](UC-004-strukturovat-text-podle-sablony.md).

## Alternativní toky

- **A1:** Volně formulovaná instrukce z audia není důvěryhodná systémová instrukce — výběr je omezen na schválený katalog šablon.
- **A2:** Nic není rozpoznáno — uživatel zvolí šablonu ručně stejně jako v [UC-004](UC-004-strukturovat-text-podle-sablony.md).

## Výsledek

Zvolena je bezpečně potvrzená šablona z katalogu, nikoliv volně odvozená z obsahu audia.

## Související

- Požadavek: [FR-AUD-010](../requirements/FR-AUD-010-rozpoznani-nadiktovane-sablony.md), [FR-TPL-001](../requirements/FR-TPL-001-vyber-cilove-sablony.md)
- Modul: (zatím žádný — moduly nejsou dosud definovány)
