# NFR-QUAL-001 — Věcná kvalita STT a extrakce polí

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-12
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — NFR-QUAL-001
**Priorita:** MUST

## Popis

[NAVRŽENO] Kvalita STT i extrakce polí musí projít věcným testem; JSON-validita sama o sobě nezaručuje správnost.

TODO: ověřit na reprezentativní sadě 10–20 českých diktátů (AT-011, ASM-007) — přesnost přepisu, chyby terminologie, záměny negací/čísel, extrakce do polí. Riziko R-002 — STT přehlédne negaci nebo odborný výraz.

## Akceptační kritéria

- [ ] Referenční test na 10–20 reprezentativních českých nahrávkách vyhodnotí věcnou přesnost STT i extrakce do polí (AT-011).

## Související

- Use-case: (žádný)
- ADR: (žádný)
- Modul: (zatím žádný — moduly nejsou dosud definovány, viz poznámka v `documentation/modules/README.md`)
