# NFR-UX-001 — Jednoduché UX pro netechnického uživatele

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-12
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — NFR-UX-001
- Upřesnění uživatele v rámci revize, 2026-11-12 (tato konverzace) — tři úrovně použití (běžné/rozšířené/administrační), hlavní průchody musí být maximálně jednoduché.
**Priorita:** MUST

## Popis

[POTVRZENO] Jednoduché UX pro méně technického uživatele; standardní workflow je veden krokově bez nutnosti znát pojmy STT/LLM. Aplikace rozlišuje tři úrovně použití — **běžné** (vytváření a práce s nahrávkami a přepisy), **rozšířené** (podrobnější práce se složkami/nahrávkami — přesouvání, mazání) a **administrační** (systémová nastavení, správa šablon) — viz [ADR-010](../decisions/ADR-010-tri-urovne-pouziti-bezne-rozsirene-administracni.md). Hlavní, nejčastější průchody (běžné použití) musí být maximálně jednoduché a intuitivní; méně časté úkony (rozšířené použití) mohou mít nižší dostupnost.

## Akceptační kritéria

- [ ] Standardní cesta „nahrát/importovat → vybrat šablonu → zpracovat → zkontrolovat → exportovat“ je zvládnutelná bez technické asistence.
- [ ] Běžné použití je z hlavní obrazovky dostupné přímo, bez nutnosti procházet rozšířené nebo administrační funkce.
- [ ] Rozšířené úkony (organizace do složek, mazání) jsou dostupné, ale nejsou vizuálně na stejné úrovni důležitosti jako běžné použití.

## Související

- Use-case: (žádný)
- ADR: [ADR-010](../decisions/ADR-010-tri-urovne-pouziti-bezne-rozsirene-administracni.md)
- Modul: (zatím žádný — moduly nejsou dosud definovány, viz poznámka v `documentation/modules/README.md`)
