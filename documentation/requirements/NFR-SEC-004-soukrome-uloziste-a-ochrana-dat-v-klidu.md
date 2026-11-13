# NFR-SEC-004 — Soukromé úložiště a ochrana dat v klidu

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-12
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — NFR-SEC-004
**Priorita:** SHOULD

## Popis

[NAVRŽENO] Lokální databáze a soubory jsou v soukromém úložišti aplikace; pro citlivá data se volí ochrana v klidu a omezení systémových záloh.

## Akceptační kritéria

- [ ] Soubory a databáze aplikace nejsou přístupné jiným aplikacím a jsou vyloučeny ze standardních systémových záloh pro citlivá data.

## Související

- Use-case: (žádný)
- ADR: [ADR-005](../decisions/ADR-005-lokalni-perzistence-vsech-mezivysledku.md)
- Modul: (zatím žádný — moduly nejsou dosud definovány, viz poznámka v `documentation/modules/README.md`)
