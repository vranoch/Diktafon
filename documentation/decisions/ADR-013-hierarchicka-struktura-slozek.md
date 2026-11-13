# ADR-013 — Hierarchická struktura složek

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-13
**Změněno:** 2026-11-13
**Zdroje:**
- Upřesnění uživatele v rámci revize, 2026-11-13 (tato konverzace) — explicitní popis stromové navigace ("na základní úrovni ikona složky pro procházení, v koncové složce s nahrávkou kontextové operace, na úrovni ostatních složek vytvořit/přesunout/smazat složku a novou nahrávku").

## Kontext

[FR-AUD-011](../requirements/FR-AUD-011-organizace-nahravek-do-slozek.md) ponechávalo jako otevřenou otázku, zda jsou složky jedna plochá úroveň, nebo zda jde o vnořenou hierarchii, a jaké má být chování při mazání neprázdné složky. Při revizi uživatel explicitně popsal procházení jako souborový prohlížeč — složky obsahují jak podsložky, tak nahrávky, a procházení jde do libovolné hloubky.

## Rozhodnutí

- Složky tvoří strom libovolné hloubky (`parentFolderId` odkazující na rodičovskou složku; `null` = kořenová úroveň). Žádné umělé omezení hloubky vnoření.
- Složku lze přejmenovat a přesunout (změnit rodiče) kamkoliv jinam ve stromu.
- Složku **nelze smazat**, dokud obsahuje byť jen jednu podsložku nebo nahrávku — uživatel musí obsah nejdřív přesunout jinam. Jde o nejbezpečnější variantu (žádná hromadná/kaskádová ztráta dat), ne o potvrzenou preferenci nad rámec této opatrnosti.
- Aplikace si pamatuje naposledy použitou složku (při založení nahrávky/importu) jako výchozí cíl pro rychlé zkratky na hlavní obrazovce.

## Důsledky

- Datový model potřebuje `parentFolderId` a dotazy na "děti dané složky" místo jediného plochého seznamu; řeší [FR-AUD-011](../requirements/FR-AUD-011-organizace-nahravek-do-slozek.md) a [UC-011](../use-cases/UC-011-organizovat-nahravky-do-slozek.md).
- UI procházení (viz [ADR-014](ADR-014-navigace-strom-slozek-kontextove-akce.md)) musí umět rekurzivně zobrazit libovolnou úroveň stromu, ne jen jeden výběr složky.
- Zákaz mazání neprázdné složky je bezpečný výchozí stav; pokud se v budoucnu ukáže jako příliš omezující (např. požadavek na „smazat se vším uvnitř“), je to podnět k nové verzi tohoto ADR, ne k tiché změně chování.

## Související

- Požadavek: [FR-AUD-011](../requirements/FR-AUD-011-organizace-nahravek-do-slozek.md)
- Use-case: [UC-011](../use-cases/UC-011-organizovat-nahravky-do-slozek.md)
- ADR: [ADR-014](ADR-014-navigace-strom-slozek-kontextove-akce.md)
