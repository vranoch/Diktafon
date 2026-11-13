# FR-TPL-004 — Vytvoření a úprava šablony z příkladu s testováním na nahrávce

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-12
**Zdroje:**
- Upřesnění uživatele v rámci revize, 2026-11-12 (tato konverzace).
- Doplněno na popud uživatele v rámci revize, 2026-11-13 (tato konverzace) — doplněn use-case [UC-012](../use-cases/UC-012-vytvorit-a-otestovat-sablonu.md).
**Priorita:** MUST / MVP

## Popis

[POTVRZENO] Uživatel vytvoří nebo upraví šablonu tak, že navrhne vzorový text výsledného dokumentu — případně doplněný klíčovými slovy/poznámkami k tomu, co je proměnné pole. Aplikace z tohoto vzoru pomocí LLM odvodí JSON Schema a instrukce šablony (stejný mechanismus jako u samotného strukturování, viz [ADR-004](../decisions/ADR-004-vymenitelni-externi-ai-poskytovatele.md)). Před uložením může uživatel draft šablony opakovaně vyzkoušet na libovolné existující nahrávce/přepisu, zkontrolovat výsledek, vzor upravit a zkoušku zopakovat, dokud není s výsledkem spokojen. Teprve na explicitní potvrzení se šablona uloží jako nová verze (viz [FR-TPL-002](FR-TPL-002-struktura-sablony-id-verze-schema-instrukce.md)).

Odvozené schéma/instrukce jsou návrh k ověření uživatelem, ne automaticky důvěryhodný výsledek — stejný princip jako u strukturování samotného dokumentu (viz [FR-LLM-003](FR-LLM-003-lokalni-validace-vystupu-llm.md), [FR-LLM-004](FR-LLM-004-zakaz-domysleni-faktu-v-promptu.md)).

Viz také [ADR-009](../decisions/ADR-009-sablony-vznikaji-z-prikladu-pres-llm-s-iterativnim-testovanim.md).

## Akceptační kritéria

- [ ] Uživatel může zadat/upravit vzorový text šablony a volitelné poznámky k proměnným polím.
- [ ] Aplikace z tohoto vstupu odvodí JSON Schema a instrukce šablony a zobrazí je k náhledu.
- [ ] Draft šablonu lze vyzkoušet na existující nahrávce/přepisu bez nutnosti ji nejprve uložit.
- [ ] Po zkoušce lze vzor upravit a zkoušku opakovat libovolněkrát.
- [ ] Šablona se jako nová verze uloží teprve na explicitní potvrzení uživatele.

## Související

- Use-case: [UC-004](../use-cases/UC-004-strukturovat-text-podle-sablony.md), [UC-012](../use-cases/UC-012-vytvorit-a-otestovat-sablonu.md)
- ADR: [ADR-004](../decisions/ADR-004-vymenitelni-externi-ai-poskytovatele.md), [ADR-006](../decisions/ADR-006-schema-sablony-je-kontrakt.md), [ADR-009](../decisions/ADR-009-sablony-vznikaji-z-prikladu-pres-llm-s-iterativnim-testovanim.md)
- Modul: (zatím žádný — moduly nejsou dosud definovány, viz poznámka v `documentation/modules/README.md`)
