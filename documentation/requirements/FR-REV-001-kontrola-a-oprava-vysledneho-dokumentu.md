# FR-REV-001 — Kontrola a oprava výsledného dokumentu

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-12
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — FR-REV-001
- Upřesnění uživatele v rámci revize, 2026-11-12 (tato konverzace) — editace musí jít provést perem nebo jakýmkoliv jiným vstupem, který zařízení umožní.
**Priorita:** MUST / MVP

## Popis

[POTVRZENO] Uživatel může zkontrolovat a opravit výsledný strukturovaný dokument před jeho předáním/exportem. Stejně jako u [FR-STT-003](FR-STT-003-editovatelnost-prepisu.md) nesmí být oprava jednotlivých polí omezena jen na jeden vstupní způsob — aplikace musí umožnit opravu libovolným textovým vstupem, který dané zařízení nabízí, včetně pera/stylusu (OVĚŘIT konkrétní možnosti na cílovém BOOX zařízení, viz [NFR-UX-002](../requirements/NFR-UX-002-e-ink-optimalizace-ui.md)).

## Akceptační kritéria

- [ ] Z náhledu strukturovaného dokumentu lze přejít do editace jednotlivých polí.
- [ ] Editace pole je možná i perem/stylusem, pokud to platforma/zařízení podporuje (ne jen virtuální klávesnicí).

## Související

- Use-case: [UC-005](../use-cases/UC-005-opravit-a-schvalit-navrh-dokumentu.md)
- ADR: (žádný)
- Modul: (zatím žádný — moduly nejsou dosud definovány, viz poznámka v `documentation/modules/README.md`)
