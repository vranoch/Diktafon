# FR-STT-003 — Editovatelnost přepisu

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-12
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — FR-STT-003
- Upřesnění uživatele v rámci revize, 2026-11-12 (tato konverzace) — editace musí jít provést perem nebo jakýmkoliv jiným vstupem, který zařízení umožní.
**Priorita:** MUST / MVP

## Popis

[POTVRZENO] Přepis je uživateli k dispozici samostatně a lze jej upravit jak před strukturováním, tak i po něm. Editace nesmí být omezena jen na jeden vstupní způsob (např. jen virtuální klávesnici) — aplikace musí umožnit opravu přepisu libovolným textovým vstupem, který dané zařízení nabízí, včetně pera/stylusu (OVĚŘIT konkrétní možnosti na cílovém BOOX zařízení, viz [NFR-UX-002](../requirements/NFR-UX-002-e-ink-optimalizace-ui.md)).

Pozn.: nejde o totéž co [FR-NOTE-001](FR-NOTE-001-rucni-poznamky-perem-prilozene-k-zaznamu.md) — tam jde o přiložení samostatné ruční poznámky perem k záznamu (NEXT, vyžaduje rozpoznávání rukopisu); zde jde o to, aby samotná oprava textu přepisu nebyla vstupně omezena jen na klávesnici.

## Akceptační kritéria

- [ ] Uživatel může upravit text přepisu kdykoliv před i po spuštění strukturování (AT-007).
- [ ] Úprava přepisu vytváří novou revizi, aniž by byla ztracena ta předchozí bez rozhodnutí uživatele.
- [ ] Editace přepisu je možná i perem/stylusem, pokud to platforma/zařízení podporuje (ne jen virtuální klávesnicí).

## Související

- Use-case: [UC-003](../use-cases/UC-003-prevest-nahravku-na-cesky-text.md)
- ADR: (žádný)
- Modul: (zatím žádný — moduly nejsou dosud definovány, viz poznámka v `documentation/modules/README.md`)
