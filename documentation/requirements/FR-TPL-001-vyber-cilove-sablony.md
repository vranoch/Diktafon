# FR-TPL-001 — Výběr cílové šablony

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-12
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — FR-TPL-001
- Upřesnění uživatele v rámci revize, 2026-11-12 (tato konverzace) — šablonu lze zvolit i nadiktováním v nahrávce; rozpoznaný návrh musí jít před zpracováním změnit.
**Priorita:** MUST / MVP

## Popis

[POTVRZENO] Uživatel vybere cílovou šablonu strukturovaného dokumentu, podle které bude přepis zpracován. Výběr může proběhnout dvěma způsoby: (a) přímým výběrem v UI, nebo (b) nadiktováním názvu šablony přímo v nahrávce — aplikace jej po přepisu rozpozná a nabídne jako navrženou šablonu (viz [FR-AUD-010](../requirements/FR-AUD-010-rozpoznani-nadiktovane-sablony.md), [UC-010](../use-cases/UC-010-zpracovat-audio-s-nadiktovanou-sablonou.md)). V obou případech musí mít uživatel před spuštěním samotného strukturování možnost navrženou/rozpoznanou šablonu změnit — nadiktovaná volba se nikdy nepoužije bez možnosti opravy.

## Akceptační kritéria

- [ ] Výběr šablony je vyžadován před spuštěním strukturování (viz [UC-004](../use-cases/UC-004-strukturovat-text-podle-sablony.md)).
- [ ] Je-li šablona rozpoznána z diktátu v nahrávce, je před zpracováním zobrazena jako navržená a uživatel ji může před potvrzením změnit na jinou ze schváleného katalogu.

## Související

- Use-case: [UC-004](../use-cases/UC-004-strukturovat-text-podle-sablony.md)
- ADR: [ADR-006](../decisions/ADR-006-schema-sablony-je-kontrakt.md)
- Modul: (zatím žádný — moduly nejsou dosud definovány, viz poznámka v `documentation/modules/README.md`)
