# NFR-SEC-005 — Rozpočtové limity API

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-12
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — NFR-SEC-005
- Upřesnění uživatele v rámci revize, 2026-11-12 (tato konverzace) — aplikace si vede vlastní evidenci čerpání, zobrazí ji na vyžádání a upozorní na překročení nastaveného limitu.
- Upřesnění uživatele v rámci revize, 2026-11-12 (tato konverzace) — priorita MUST; limit je nastavitelný na měsíc.
- Upřesnění uživatele v rámci revize, 2026-11-12 (tato konverzace) — jde o kalendářní měsíc.
**Priorita:** MUST

## Popis

[POTVRZENO] Aplikace si vede vlastní lokální evidenci čerpání zdrojů (volání STT/LLM, odhadovaná cena/spotřeba) nezávisle na nastavení u providera. Na vyžádání tuto evidenci uživateli zobrazí (přehled spotřeby) a upozorní, pokud je překročen uživatelem nastavený měsíční limit. Limit se nastavuje jako částka za kalendářní měsíc a evidence se vždy prvním dnem nového kalendářního měsíce resetuje. Doplňkově lze (kde to provider umožňuje) nastavit i měsíční rozpočtový limit přímo u providera jako další pojistku (viz [FR-CFG-001](FR-CFG-001-sprava-api-klice.md), [UC-007](../use-cases/UC-007-nastavit-externi-api.md)).

## Akceptační kritéria

- [ ] Aplikace zaznamenává každé volání STT/LLM s odhadovanou cenou/spotřebou do lokální evidence.
- [ ] Uživatel si může evidenci čerpání kdykoliv zobrazit (přehled spotřeby za aktuální měsíc).
- [ ] Uživatel může nastavit vlastní měsíční limit čerpání; při jeho překročení aplikace zřetelně upozorní.
- [ ] Evidence čerpání se vztahuje vždy k jednomu kalendářnímu měsíci a prvním dnem dalšího kalendářního měsíce se počítá znovu od nuly.
- [ ] Nastavení rozpočtového limitu přímo u providera (pokud to API umožňuje) zůstává možné jako doplňková pojistka.

## Související

- Use-case: [UC-007](../use-cases/UC-007-nastavit-externi-api.md)
- ADR: (žádný)
- Modul: (zatím žádný — moduly nejsou dosud definovány, viz poznámka v `documentation/modules/README.md`)
