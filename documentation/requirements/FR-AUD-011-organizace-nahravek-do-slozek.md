# FR-AUD-011 — Organizace nahrávek do složek

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-13
**Zdroje:**
- Upřesnění uživatele v rámci revize, 2026-11-12 (tato konverzace).
- Upřesnění uživatele v rámci revize, 2026-11-12 (tato konverzace) — cílovou složku lze zvolit/změnit už při zahájení nahrávání, viz [UC-001](../use-cases/UC-001-poridit-a-ulozit-hlasovy-zaznam.md).
- Doplněno na popud uživatele v rámci revize, 2026-11-13 (tato konverzace) — doplněn use-case [UC-011](../use-cases/UC-011-organizovat-nahravky-do-slozek.md).
- Upřesnění uživatele v rámci revize, 2026-11-13 (tato konverzace) — otevřená otázka hloubky hierarchie a mazání neprázdné složky vyřešena, viz [ADR-013](../decisions/ADR-013-hierarchicka-struktura-slozek.md).
**Priorita:** MUST / MVP

## Popis

[POTVRZENO] Nahrávky lze organizovat do složek — vytvořit složku, přesunout nahrávku do zvolené složky nebo zpět mimo složky, procházet nahrávky po složkách — nejen jako jeden plochý seznam. Cílovou složku lze zvolit/změnit už při zahájení nového nahrávání (viz [UC-001](../use-cases/UC-001-poridit-a-ulozit-hlasovy-zaznam.md)), nejen dodatečným přesunem po jeho uložení; pokud uživatel složku explicitně nezvolí, použije se naposledy použitá složka (viz [ADR-013](../decisions/ADR-013-hierarchicka-struktura-slozek.md)).

Složky tvoří strom libovolné hloubky (podsložky uvnitř podsložek), ne jen jednu plochou úroveň — viz [ADR-013](../decisions/ADR-013-hierarchicka-struktura-slozek.md). Složku lze kromě vytvoření/přejmenování/smazání i přesunout jinam ve stromu. Složku nelze smazat, dokud obsahuje podsložku nebo nahrávku — uživatel musí obsah nejdřív přesunout jinam (nejbezpečnější varianta, viz [ADR-013](../decisions/ADR-013-hierarchicka-struktura-slozek.md)).

Pozn.: nejde o totéž co [FR-AUD-009](FR-AUD-009-spojovani-epizod-do-jednoho-zaznamu.md) (spojení více nahrávek do jedné) ani [FR-AUD-005](FR-AUD-005-seznam-nahravek-s-metadaty.md) (metadata, název, poznámka jedné nahrávky) — zde jde čistě o organizační strukturu seznamu.

## Akceptační kritéria

- [ ] Uživatel může vytvořit, přejmenovat, přesunout a smazat složku.
- [ ] Složky mohou být vnořené do libovolné hloubky.
- [ ] Smazání složky je odmítnuto, dokud obsahuje podsložku nebo nahrávku.
- [ ] Uživatel může nahrávku přesunout do zvolené složky nebo ji ze složky vyjmout.
- [ ] Nahrávky lze procházet po jednotlivých složkách (včetně podsložek).
- [ ] Při zahájení nového nahrávání/importu lze zvolit/změnit cílovou složku; není-li zvolena, použije se naposledy použitá složka.

## Související

- Use-case: [UC-001](../use-cases/UC-001-poridit-a-ulozit-hlasovy-zaznam.md), [UC-002](../use-cases/UC-002-importovat-externi-nahravku.md), [UC-011](../use-cases/UC-011-organizovat-nahravky-do-slozek.md)
- ADR: [ADR-013](../decisions/ADR-013-hierarchicka-struktura-slozek.md)
- Modul: (zatím žádný — moduly nejsou dosud definovány, viz poznámka v `documentation/modules/README.md`)
