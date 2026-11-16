# FR-AUD-005 — Seznam nahrávek s metadaty, názvem a poznámkou

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-16
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — FR-AUD-005
- Upřesnění uživatele v rámci revize, 2026-11-12 (tato konverzace) — doplněno pojmenování a poznámka.
- Upřesnění uživatele v rámci revize, 2026-11-12 (tato konverzace) — při zahájení nahrávání se nabídne předgenerovaný název se změnou, případně rovnou i zápis poznámky.
- [Chat-extract — Revize názvu/poznámky před spuštěním nahrávání, 2026-11-16](../source-materials/chat-extracts/2026-11-16-revize-pred-spustenim-nahravani.md) — název/poznámku lze revidovat ještě před zahájením záznamu, ne až za běhu mikrofonu.
**Priorita:** MUST / MVP

## Popis

[POTVRZENO] Seznam nahrávek obsahuje datum, délku a stav zpracování. Každou nahrávku lze navíc pojmenovat (vlastní název, nejen odvozený z data/pořadí) a doplnit volnou textovou poznámkou; obojí lze kdykoliv změnit. Při zahájení nového nahrávání (viz [UC-001](../use-cases/UC-001-poridit-a-ulozit-hlasovy-zaznam.md)) aplikace nejdřív nabídne předgenerovaný název k revizi (mikrofon ještě neběží) — uživatel jej může změnit a volitelně rovnou zapsat poznámku — a teprve explicitním potvrzením zahájí samotné nahrávání; obojí (název i poznámku) lze stejně tak doplnit/upravit i kdykoliv později, i za běhu nahrávání.

Pozn.: organizace nahrávek do složek je samostatný požadavek, viz [FR-AUD-011](FR-AUD-011-organizace-nahravek-do-slozek.md).

## Akceptační kritéria

- [ ] Každá položka seznamu zobrazuje datum, délku a aktuální stav zpracování.
- [ ] Uživatel může nahrávce nastavit a kdykoliv změnit název.
- [ ] Uživatel může k nahrávce připsat a upravit volnou textovou poznámku.
- [ ] Při zahájení nového nahrávání se nejdřív zobrazí předgenerovaný název k revizi (editovatelný, s možností rovnou zapsat poznámku) a teprve explicitní potvrzení zahájí nahrávání mikrofonem.

## Související

- Use-case: [UC-002](../use-cases/UC-002-importovat-externi-nahravku.md)
- ADR: (žádný)
- Modul: (zatím žádný — moduly nejsou dosud definovány, viz poznámka v `documentation/modules/README.md`)
