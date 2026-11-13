# FR-STT-001 — Odeslání audia do STT a uložení přepisu

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-12
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — FR-STT-001
- Upřesnění uživatele v rámci revize, 2026-11-12 (tato konverzace) — jazyk nahrávky/přepisu je volitelný, výchozí čeština.
**Priorita:** MUST / MVP

## Popis

[POTVRZENO] Aplikace odešle audio externí STT službě a uloží doslovný přepis. Jazyk nahrávky/přepisu si uživatel může zvolit; výchozím jazykem je čeština. Zvolený jazyk se předá STT službě jako parametr volání (viz [FR-STT-002](FR-STT-002-jazykove-a-terminologicke-napovedy-pro-stt.md) pro doplňkové terminologické nápovědy) a zaznamená se jako metadata přepisu.

## Akceptační kritéria

- [ ] Po úspěšném volání STT je k dispozici doslovný přepis v uživatelem zvoleném jazyce (bez explicitní volby v češtině) vázaný na danou nahrávku.
- [ ] Uživatel může před spuštěním přepisu zvolit jiný jazyk než výchozí češtinu.
- [ ] Jazyk, ve kterém přepis vznikl, je u přepisu uložen jako metadata.
- [ ] Selhání volání (401/403/429/timeout/serverová chyba) je srozumitelně signalizováno a nevede ke ztrátě audia (AT-004).

## Související

- Use-case: [UC-003](../use-cases/UC-003-prevest-nahravku-na-cesky-text.md)
- ADR: [ADR-003](../decisions/ADR-003-oddelene-kroky-stt-a-llm.md), [ADR-004](../decisions/ADR-004-vymenitelni-externi-ai-poskytovatele.md)
- Modul: (zatím žádný — moduly nejsou dosud definovány, viz poznámka v `documentation/modules/README.md`)
