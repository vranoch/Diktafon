# FR-DATA-001 — Mazání nahrávek, přepisů a výsledků

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-12
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — FR-DATA-001
- Upřesnění uživatele v rámci revize, 2026-11-12 (tato konverzace) — smazání záznamu maže celou jeho skupinu artefaktů (audio/epizody, přepis, výstup) jako jednotku.
**Priorita:** MUST / MVP

## Popis

[POTVRZENO] Uživatel může mazat jednotlivé nahrávky, přepisy i výsledky; mazání vyžaduje potvrzení a respektuje závislosti mezi artefakty. Díky tomu, že artefakty jedné nahrávky tvoří jednu skupinu/jednotku manipulace (viz [ADR-005](../decisions/ADR-005-lokalni-perzistence-vsech-mezivysledku.md)), smazání celého záznamu odstraní najednou originální audio/epizody, přepis i formátovaný výstup; dílčí smazání jen některé části (např. jen výsledku, se zachováním přepisu) musí zůstat možné samostatně. Konkrétní fyzické uložení není podstatné.

## Akceptační kritéria

- [ ] Smazání nahrávky/přepisu/výsledku vyžaduje explicitní potvrzení a zobrazí závislé artefakty, kterých se týká.
- [ ] Smazání celého záznamu odstraní celou jeho skupinu artefaktů (audio/epizody, přepis, výstup) v jednom kroku.
- [ ] Smazání jen přepisu nebo jen výsledku (bez smazání celé nahrávky) zůstává samostatně možné.

## Související

- Use-case: [UC-009](../use-cases/UC-009-vymazat-vlastni-data.md)
- ADR: [ADR-005](../decisions/ADR-005-lokalni-perzistence-vsech-mezivysledku.md)
- Modul: (zatím žádný — moduly nejsou dosud definovány, viz poznámka v `documentation/modules/README.md`)
