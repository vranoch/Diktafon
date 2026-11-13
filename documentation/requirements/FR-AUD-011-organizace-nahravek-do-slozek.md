# FR-AUD-011 — Organizace nahrávek do složek

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-12
**Zdroje:**
- Upřesnění uživatele v rámci revize, 2026-11-12 (tato konverzace).
- Upřesnění uživatele v rámci revize, 2026-11-12 (tato konverzace) — cílovou složku lze zvolit/změnit už při zahájení nahrávání, viz [UC-001](../use-cases/UC-001-poridit-a-ulozit-hlasovy-zaznam.md).
- Doplněno na popud uživatele v rámci revize, 2026-11-13 (tato konverzace) — doplněn use-case [UC-011](../use-cases/UC-011-organizovat-nahravky-do-slozek.md).
**Priorita:** MUST / MVP

## Popis

[POTVRZENO] Nahrávky lze organizovat do složek — vytvořit složku, přesunout nahrávku do zvolené složky nebo zpět mimo složky, procházet seznam nahrávek po složkách — nejen jako jeden plochý seznam. Cílovou složku lze zvolit/změnit už při zahájení nového nahrávání (viz [UC-001](../use-cases/UC-001-poridit-a-ulozit-hlasovy-zaznam.md)), nejen dodatečným přesunem po jeho uložení.

TODO: upřesnit hloubku hierarchie (jedna úroveň vs. vnořené složky) a chování při mazání neprázdné složky (smazat i obsah / přesunout obsah ven / zakázat). Zatím otevřená otázka k dořešení před implementací.

Pozn.: nejde o totéž co [FR-AUD-009](FR-AUD-009-spojovani-epizod-do-jednoho-zaznamu.md) (spojení více nahrávek do jedné) ani [FR-AUD-005](FR-AUD-005-seznam-nahravek-s-metadaty.md) (metadata, název, poznámka jedné nahrávky) — zde jde čistě o organizační strukturu seznamu.

## Akceptační kritéria

- [ ] Uživatel může vytvořit, přejmenovat a smazat složku.
- [ ] Uživatel může nahrávku přesunout do zvolené složky nebo ji ze složky vyjmout.
- [ ] Seznam nahrávek lze procházet po složkách.
- [ ] Při zahájení nového nahrávání lze zvolit/změnit cílovou složku, do které nahrávka patří.

## Související

- Use-case: [UC-001](../use-cases/UC-001-poridit-a-ulozit-hlasovy-zaznam.md), [UC-002](../use-cases/UC-002-importovat-externi-nahravku.md), [UC-011](../use-cases/UC-011-organizovat-nahravky-do-slozek.md)
- ADR: (žádný)
- Modul: (zatím žádný — moduly nejsou dosud definovány, viz poznámka v `documentation/modules/README.md`)
