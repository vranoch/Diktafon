# ADR-004 — Vyměnitelní externí AI poskytovatelé

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-12
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — kapitola 8, ADR-004

## Kontext

Trh STT/LLM modelů a jejich ceny/dostupnost pro češtinu se mění; nelze se vázat na jednoho providera natrvalo.

## Rozhodnutí

Použít provider-neutrální rozhraní (`SpeechToTextProvider`, `DocumentProcessor`) s verzovanou konfigurací modelu. Výchozí kandidáti k ověření: STT a menší model pro strukturování od jednoho z větších poskytovatelů, s alternativním providerem jako experiment.

## Důsledky

Konkrétní oficiální názvy modelů, API kompatibilita, ceny, limity a kvalita češtiny musí být ověřeny při implementaci — nejde o neměnné požadavky (otevřená otázka Q-012 v [zdrojovém dokumentu, kap. 12](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md)). TODO: ověřit konkrétní modely a jejich podmínky před ostrým nasazením.
