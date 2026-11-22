# UC-011 — Organizovat nahrávky do složek

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-13
**Změněno:** 2026-11-22
**Zdroje:**
- Doplněno na popud uživatele v rámci revize, 2026-11-13 (tato konverzace) — chyběl use-case pokrývající [FR-AUD-011](../requirements/FR-AUD-011-organizace-nahravek-do-slozek.md).
- Upřesnění uživatele v rámci revize, 2026-11-13 (tato konverzace) — procházení jako strom (souborový prohlížeč), smazání neprázdné složky zakázáno, přesun složky doplněn; viz [ADR-013](../decisions/ADR-013-hierarchicka-struktura-slozek.md)/[ADR-014](../decisions/ADR-014-navigace-strom-slozek-kontextove-akce.md).
- [ADR-015](../decisions/ADR-015-principy-ui-ux-pro-e-ink-a-proces-navrhu-obrazovek.md) — proces návrhu obrazovek, krok 5: sekce Obrazovky a tok se doplňuje po schválení mockupu.
- [Chat-extract — Navigace po smazání nahrávky zpět do složky, ověření procházení stromu složek, 2026-11-22](../source-materials/chat-extracts/2026-11-22-navigace-po-smazani-nahravky-zpet-do-slozky.md) — doplněn breadcrumb po ověření, že obyčejné tlačítko zpět samo o sobě spolehlivou navigaci nahoru stromem nezaručuje.

## Aktér

Uživatel (rozšířené použití — viz [ADR-010](../decisions/ADR-010-tri-urovne-pouziti-bezne-rozsirene-administracni.md)).

## Předpoklady

Existuje alespoň jedna nahrávka (viz [UC-001](UC-001-poridit-a-ulozit-hlasovy-zaznam.md), [UC-002](UC-002-importovat-externi-nahravku.md)).

## Hlavní tok

1. Uživatel otevře procházení složek z hlavní obrazovky (ikona složky).
2. Vidí podsložky a nahrávky aktuální úrovně (kořen = nic nezvoleno); může sestoupit do kterékoliv podsložky do libovolné hloubky. Nad obsahem je breadcrumb (cesta od kořene po aktuální složku) — kterákoliv úroveň kromě té aktuální je klikací a skočí tam přímo, nezávisle na tom, jakou cestou uživatel do aktuální složky došel.
3. Na dané úrovni může vytvořit novou složku, přejmenovat nebo přesunout existující složku jinam ve stromu, nebo založit novou nahrávku/import přímo do procházené složky.
4. Vybranou nahrávku přesune do zvolené složky nebo ji ze složky vyjme zpět mimo složky.
5. Dojde-li až k samotné nahrávce, pokračuje kontextovými akcemi nad ní (přehrát, přepsat, konvertovat, odeslat, smazat) — viz [ADR-014](../decisions/ADR-014-navigace-strom-slozek-kontextove-akce.md).

## Alternativní toky

- **A1:** Uživatel maže neprázdnou složku → aplikace to odmítne, dokud uživatel nejdřív nepřesune/nevyjme její obsah (viz [ADR-013](../decisions/ADR-013-hierarchicka-struktura-slozek.md)).
- **A2:** Uživatel zvolí/změní cílovou složku už při zahájení nového nahrávání nebo importu, místo dodatečného přesunu — viz [UC-001](UC-001-poridit-a-ulozit-hlasovy-zaznam.md), [UC-002](UC-002-importovat-externi-nahravku.md).
- **A3:** Uživatel nezvolí žádnou složku při zahájení nahrávání/importu → použije se naposledy použitá složka.

## Výsledek

Nahrávky jsou organizované do uživatelem definované stromové struktury složek; organizace do složek je nezávislá na logickém seskupení artefaktů jedné nahrávky (viz [ADR-005](../decisions/ADR-005-lokalni-perzistence-vsech-mezivysledku.md)).

## Obrazovky a tok

Realizováno mockupem [Diktafon – návrh obrazovek](https://claude.ai/artifact/1iuLje7CMomhtKSSiLFw4d) (viz [ADR-015](../decisions/ADR-015-principy-ui-ux-pro-e-ink-a-proces-navrhu-obrazovek.md)): obrazovka Složky (strom složek, nová/přejmenovat/přesunout, založení nahrávky přímo ve složce) dostupná z Domů.

## Související

- Požadavek: [FR-AUD-011](../requirements/FR-AUD-011-organizace-nahravek-do-slozek.md), [FR-AUD-005](../requirements/FR-AUD-005-seznam-nahravek-s-metadaty.md)
- ADR: [ADR-005](../decisions/ADR-005-lokalni-perzistence-vsech-mezivysledku.md), [ADR-010](../decisions/ADR-010-tri-urovne-pouziti-bezne-rozsirene-administracni.md), [ADR-013](../decisions/ADR-013-hierarchicka-struktura-slozek.md), [ADR-014](../decisions/ADR-014-navigace-strom-slozek-kontextove-akce.md)
- Modul: (zatím žádný — moduly nejsou dosud definovány, viz poznámka v [`documentation/modules/README.md`](../modules/README.md))
