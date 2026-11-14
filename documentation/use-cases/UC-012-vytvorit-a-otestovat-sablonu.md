# UC-012 — Vytvořit, upravit a otestovat šablonu

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-13
**Změněno:** 2026-11-14
**Zdroje:**
- Doplněno na popud uživatele v rámci revize, 2026-11-13 (tato konverzace) — chyběl use-case pokrývající [FR-TPL-004](../requirements/FR-TPL-004-vytvoreni-a-uprava-sablony-z-prikladu-s-testovanim.md)/[FR-TPL-005](../requirements/FR-TPL-005-export-sdileni-sablon-zalohovani.md) a [ADR-009](../decisions/ADR-009-sablony-vznikaji-z-prikladu-pres-llm-s-iterativnim-testovanim.md).
- [ADR-015](../decisions/ADR-015-principy-ui-ux-pro-e-ink-a-proces-navrhu-obrazovek.md) — proces návrhu obrazovek, krok 5: sekce Obrazovky a tok se doplňuje po schválení mockupu.

## Aktér

Uživatel (administrační použití — viz [ADR-010](../decisions/ADR-010-tri-urovne-pouziti-bezne-rozsirene-administracni.md)).

## Předpoklady

Je nakonfigurován LLM provider (viz [UC-007](UC-007-nastavit-externi-api.md)). Pro testování draftu je výhodou existující nahrávka/přepis (viz [UC-001](UC-001-poridit-a-ulozit-hlasovy-zaznam.md)/[UC-003](UC-003-prevest-nahravku-na-cesky-text.md)), není to ale tvrdá podmínka pro zahájení tvorby šablony.

## Hlavní tok

1. Uživatel otevře správu šablon (administrační použití, znatelně oddělené od běžného a rozšířeného použití).
2. Zadá nebo upraví vzorový text výsledného dokumentu, volitelně doplněný poznámkami k proměnným polím.
3. Aplikace pomocí LLM odvodí draft JSON Schema a instrukcí šablony a zobrazí je k náhledu.
4. Uživatel vyzkouší draft šablony na libovolné existující nahrávce/přepisu.
5. Zkontroluje výsledek testu; pokud není spokojen, upraví vzorový text/poznámky a zopakuje kroky 3–4.
6. Až je s výsledkem spokojen, explicitně potvrdí uložení — vznikne nová verze šablony (viz [FR-TPL-002](../requirements/FR-TPL-002-struktura-sablony-id-verze-schema-instrukce.md)).
7. Volitelně uloženou šablonu zkopíruje/sdílí (copy/paste, Android Share) za účelem zálohy.

## Alternativní toky

- **A1:** Uživatel ukončí tvorbu bez uložení → draft se zahodí, dříve uložená verze šablony (pokud existovala) zůstává beze změny.
- **A2:** Test draftu selže nebo vrátí neočekávaný výsledek → uživatel se vrátí ke kroku 2 bez nutnosti cokoliv ukládat.

## Výsledek

Existuje uložená, verzovaná šablona použitelná při strukturování (viz [UC-004](UC-004-strukturovat-text-podle-sablony.md)); odvozené schéma/instrukce jsou návrh ověřený uživatelem přes test na reálných datech, ne automaticky důvěryhodný výstup LLM.

## Obrazovky a tok

Realizováno mockupem [Diktafon – návrh obrazovek](https://claude.ai/artifact/1iuLje7CMomhtKSSiLFw4d) (viz [ADR-015](../decisions/ADR-015-principy-ui-ux-pro-e-ink-a-proces-navrhu-obrazovek.md)): Administrace → Správa šablon (katalog existujících šablon) a Šablona (zadání vzorového textu, odvození draftu, test na existujícím přepisu, uložení nové verze).

## Související

- Požadavek: [FR-TPL-002](../requirements/FR-TPL-002-struktura-sablony-id-verze-schema-instrukce.md), [FR-TPL-004](../requirements/FR-TPL-004-vytvoreni-a-uprava-sablony-z-prikladu-s-testovanim.md), [FR-TPL-005](../requirements/FR-TPL-005-export-sdileni-sablon-zalohovani.md)
- ADR: [ADR-004](../decisions/ADR-004-vymenitelni-externi-ai-poskytovatele.md), [ADR-006](../decisions/ADR-006-schema-sablony-je-kontrakt.md), [ADR-009](../decisions/ADR-009-sablony-vznikaji-z-prikladu-pres-llm-s-iterativnim-testovanim.md), [ADR-010](../decisions/ADR-010-tri-urovne-pouziti-bezne-rozsirene-administracni.md)
- Modul: (zatím žádný — moduly nejsou dosud definovány, viz poznámka v [`documentation/modules/README.md`](../modules/README.md))
