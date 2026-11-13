# FR-AUD-010 — Rozpoznání nadiktované šablony

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-12
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — FR-AUD-010
- Upřesnění uživatele v rámci revize, 2026-11-12 (tato konverzace) — zařazeno do MVP jako jedna z cest výběru šablony (viz [FR-TPL-001](FR-TPL-001-vyber-cilove-sablony.md)), s povinnou možností změny před zpracováním.
**Priorita:** MUST / MVP

## Popis

[POTVRZENO] Rozpoznání nadiktovaného názvu šablony z obsahu nahrávky; uživatel vždy může výsledek před spuštěním strukturování opravit/změnit (viz [FR-TPL-001](FR-TPL-001-vyber-cilove-sablony.md)).

Riziko: volně formulovaná instrukce z audia není důvěryhodná systémová instrukce — výběr šablony musí být omezen na schválený katalog (viz [UC-010](../use-cases/UC-010-zpracovat-audio-s-nadiktovanou-sablonou.md)); rozpoznaný název se použije jen jako návrh, nikdy jako přímý systémový příkaz.

## Akceptační kritéria

- [ ] Po přepisu nahrávky je z jejího obsahu rozpoznán případný nadiktovaný název šablony a nabídnut jako návrh.
- [ ] Nerozpozná-li se žádná známá šablona ze schváleného katalogu, aplikace nenabídne žádný neplatný/vymyšlený název a necháva na uživateli ruční výběr.
- [ ] Navrženou šablonu lze před spuštěním strukturování změnit na jinou ze schváleného katalogu (viz [UC-010](../use-cases/UC-010-zpracovat-audio-s-nadiktovanou-sablonou.md)).

## Související

- Use-case: [UC-010](../use-cases/UC-010-zpracovat-audio-s-nadiktovanou-sablonou.md)
- ADR: (žádný)
- Modul: (zatím žádný — moduly nejsou dosud definovány, viz poznámka v `documentation/modules/README.md`)
