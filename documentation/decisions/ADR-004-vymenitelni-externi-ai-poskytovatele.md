# ADR-004 — Vyměnitelní externí AI poskytovatelé

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-19
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — kapitola 8, ADR-004
- [Chat-extract — Reálné STT/LLM API (OpenAI) s přepínačem mock/reálné, 2026-11-15](../source-materials/chat-extracts/2026-11-15-realne-stt-llm-openai.md)

## Kontext

Trh STT/LLM modelů a jejich ceny/dostupnost pro češtinu se mění; nelze se vázat na jednoho providera natrvalo.

## Rozhodnutí

Použít provider-neutrální rozhraní (`SpeechToTextProvider`, `DocumentProcessor`) s verzovanou konfigurací modelu.

**Aktualizace 2026-11-15:** jako první konkrétní implementace zvolen **OpenAI** — Whisper
(`/v1/audio/transcriptions`) pro přepis, Chat Completions s JSON módem (`response_format:
json_object`) pro strukturování. Mock zůstává výchozí volbou (bezpečné zkoušení bez nákladů a
bez nutnosti API klíče); mezi simulací a OpenAI se přepíná polem "Provider" v Administraci
(dříve netknutelný volný text, nyní výběr Simulace/OpenAI). Implementace nepřidává novou
závislost — `retrofit`/`okhttp`/`kotlinx-serialization-json` už byly v projektu přítomné, jen
nezapojené; reálná volání jdou přímo přes `OkHttpClient` (ne přes Retrofit rozhraní — pro dva
jednoduché endpointy to nebylo potřeba).

**Aktualizace 2026-11-19:** stejný selector/mock-OpenAI mechanismus (sdílející LLM pole
Provider/Model/API klíč z Administrace) rozšířen na další dvě místa, kde se dřív používala jen
naivní neinteligentní simulace: odvození draftu šablony z ukázkového textu (dřív jen useknutí
textu před dvojtečkou na každém řádku, viz [FR-TPL-004](../requirements/FR-TPL-004-vytvoreni-a-uprava-sablony-z-prikladu-s-testovanim.md))
a rozpoznání zamýšlené šablony z přepisu (dřív jen doslovné hledání přesného názvu šablony v
textu, viz [FR-AUD-010](../requirements/FR-AUD-010-rozpoznani-nadiktovane-sablony.md)). U
rozpoznání šablony navíc platí, že selhání (chybějící klíč, síť, nevalidní odpověď) se tiše
vyhodnotí jako "nerozpoznáno" bez chybové hlášky — jde jen o pomocný návrh, ne kritický krok,
na rozdíl od přepisu/strukturování, kde se chyba uživateli vždy zobrazí.

## Důsledky

- Konkrétní oficiální názvy modelů, API kompatibilita, ceny, limity a kvalita češtiny u OpenAI
  implementace musí být průběžně ověřovány v provozu — výchozí modely (`whisper-1`,
  `gpt-4o-mini`) jsou jen startovní volba, uživatel je může v Administraci přepsat.
- Rozpočtové limity API ([NFR-SEC-005](../requirements/NFR-SEC-005-rozpoctove-limity-api.md))
  nejsou touto implementací pokryty — zůstává otevřený následný úkol.
- Alternativní poskytovatel (experiment) zůstává otevřenou otázkou Q-012 v
  [zdrojovém dokumentu, kap. 12](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md)
  — architektura (zaměnitelné rozhraní + selector dle konfigurace) jej umožňuje přidat bez
  zásahu do zbytku kódu.
