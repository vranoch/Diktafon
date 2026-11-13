# FR-AUD-005 — Seznam nahrávek s metadaty, názvem a poznámkou

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-12
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — FR-AUD-005
- Upřesnění uživatele v rámci revize, 2026-11-12 (tato konverzace) — doplněno pojmenování a poznámka.
- Upřesnění uživatele v rámci revize, 2026-11-12 (tato konverzace) — při zahájení nahrávání se nabídne předgenerovaný název se změnou, případně rovnou i zápis poznámky.
**Priorita:** MUST / MVP

## Popis

[POTVRZENO] Seznam nahrávek obsahuje datum, délku a stav zpracování. Každou nahrávku lze navíc pojmenovat (vlastní název, nejen odvozený z data/pořadí) a doplnit volnou textovou poznámkou; obojí lze kdykoliv změnit. Při zahájení nového nahrávání (viz [UC-001](../use-cases/UC-001-poridit-a-ulozit-hlasovy-zaznam.md)) aplikace rovnou nabídne předgenerovaný název (např. odvozený z data/času), který uživatel může ihned změnit, a umožní volitelně rovnou zapsat i poznámku — obojí lze ale stejně tak doplnit/upravit i kdykoliv později.

Pozn.: organizace nahrávek do složek je samostatný požadavek, viz [FR-AUD-011](FR-AUD-011-organizace-nahravek-do-slozek.md).

## Akceptační kritéria

- [ ] Každá položka seznamu zobrazuje datum, délku a aktuální stav zpracování.
- [ ] Uživatel může nahrávce nastavit a kdykoliv změnit název.
- [ ] Uživatel může k nahrávce připsat a upravit volnou textovou poznámku.
- [ ] Při zahájení nového nahrávání je nabídnutý předgenerovaný název ihned editovatelný a je nabídnuta i možnost zapsat poznámku.

## Související

- Use-case: [UC-002](../use-cases/UC-002-importovat-externi-nahravku.md)
- ADR: (žádný)
- Modul: (zatím žádný — moduly nejsou dosud definovány, viz poznámka v `documentation/modules/README.md`)
