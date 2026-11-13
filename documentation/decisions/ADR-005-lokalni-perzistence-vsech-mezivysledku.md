# ADR-005 — Lokální perzistence všech mezivýsledků

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-12
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — kapitola 8, ADR-005
- Upřesnění uživatele v rámci revize, 2026-11-12 (tato konverzace) — artefakty jedné nahrávky tvoří pro uživatele jednu skupinu/jednotku manipulace; konkrétní fyzické uložení je implementační detail.

## Kontext

Zpracování prochází více kroky (audio → přepis → návrh → schválený výstup) přes nespolehlivou síť; je potřeba zajistit dohledatelnost a možnost opakování jednotlivých kroků bez ztráty dat.

## Rozhodnutí

Původní audio (případně více epizod/částí, viz [FR-AUD-003](../requirements/FR-AUD-003-ui-stavu-nahravani-a-ochrana-pred-zahozenim.md)), přepis, návrh strukturovaného dokumentu a schválený výstup existují jako oddělené, samostatně perzistované entity (`Recording`, `Transcript`, `StructuredDocument`). Pro uživatele ale všechny artefakty patřící k jedné nahrávce tvoří jednu skupinu/jednotku, se kterou lze společně manipulovat (zobrazit pohromadě, přesunout, smazat — viz [FR-DATA-001](../requirements/FR-DATA-001-mazani-nahravek-prepisu-a-vysledku.md)). Konkrétní fyzické uložení (společná složka, oddělené soubory propojené identifikátorem apod.) je implementační detail a není tímto rozhodnutím předepsáno.

Toto automatické seskupení "jedna nahrávka = jedna jednotka" je odlišná vrstva od [FR-AUD-011](../requirements/FR-AUD-011-organizace-nahravek-do-slozek.md) — tam jde o uživatelem vytvářené složky, do kterých uživatel řadí více celých nahrávek kvůli procházení seznamu; zde jde o vždy existující, automatické seskupení artefaktů *uvnitř* jedné nahrávky, nezávislé na tom, do jaké uživatelské složky daná nahrávka případně patří.

## Důsledky

Je nutné řešit retenční politiku, šifrování citlivých dat v klidu a správu úložné kapacity zařízení (viz [NFR-SEC-004](../requirements/NFR-SEC-004-soukrome-uloziste-a-ochrana-dat-v-klidu.md), [FR-DATA-002](../requirements/FR-DATA-002-konfigurovatelna-retencni-politika.md)). Seskupení usnadňuje hromadné operace nad celým záznamem (mazání, přesun) jako jednotkou — logické oddělení entit `Recording`/`Transcript`/`StructuredDocument` tím není dotčeno.
