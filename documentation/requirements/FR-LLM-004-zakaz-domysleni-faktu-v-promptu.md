# FR-LLM-004 — Zákaz domýšlení faktů v promptu

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-12
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — FR-LLM-004
- Upřesnění uživatele v rámci revize, 2026-11-12 (tato konverzace) — nejisté hodnoty musí být v UI jasně a nepřehlédnutelně označené.
**Priorita:** MUST / MVP

## Popis

[POTVRZENO] Prompt výslovně zakazuje domýšlení faktů; neznámé nebo nejisté hodnoty mají být vynechány nebo označeny podle schématu (pole nejistot), nikoliv vyplněny vymyšlenou hodnotou. Takto označené nejisté/chybějící hodnoty musí být v UI při revizi dokumentu jasně a nepřehlédnutelně vizuálně odlišené od jistých/potvrzených hodnot — viz [FR-LLM-006](FR-LLM-006-upozorneni-na-nejasnosti-a-chybejici-udaje.md) (zobrazení nejasností) a [FR-REV-002](FR-REV-002-odliseni-prepisu-od-strukturovaneho-vysledku-v-ui.md) (odlišení v UI obecně).

Riziko R-003 — LLM doplní neexistující fakt; mitigace: striktní instrukce, pole pro nejistoty, viditelné označení a povinná lidská revize.

## Akceptační kritéria

- [ ] Diktát bez povinného pole vede k označení chybějícího údaje, nikoliv k vymyšlené hodnotě (AT-006).
- [ ] Pole označené jako nejisté/chybějící je v obrazovce revize vizuálně zřetelně odlišeno (např. barvou/ikonou), tak aby je uživatel při kontrole nemohl snadno přehlédnout.

## Související

- Use-case: [UC-004](../use-cases/UC-004-strukturovat-text-podle-sablony.md)
- ADR: [ADR-006](../decisions/ADR-006-schema-sablony-je-kontrakt.md)
- Modul: (zatím žádný — moduly nejsou dosud definovány, viz poznámka v `documentation/modules/README.md`)
