# Chat-extract — Reálné STT/LLM API (OpenAI) s přepínačem mock/reálné

**Datum:** 2026-11-15

## Kontext diskuze

Po dotazu, zda jsou URL pro STT/LLM endpointy napevno, bylo potvrzeno, že obě integrace
(`MockSpeechToTextProvider`, `MockDocumentProcessor`) jsou zatím jen mocky bez jakéhokoliv
síťového volání — v souladu s [ADR-004](../../decisions/ADR-004-vymenitelni-externi-ai-poskytovatele.md),
které volbu konkrétního poskytovatele nechávalo jako otevřenou otázku (Q-012).

Doporučení: OpenAI Whisper (`/v1/audio/transcriptions`) pro přepis a OpenAI Chat Completions
s JSON module pro strukturování — obojí s dobrou češtinou, jednoduchým REST API a již
přítomnými závislostmi (`retrofit`/`okhttp`/`kotlinx-serialization-json` jsou v projektu, ale
dosud nezapojené).

## Rozhodnutí uživatele

1. Implementovat reálné volání OpenAI API pro STT i strukturování.
2. Zachovat možnost přepnout mezi simulací (mock) a reálnou implementací — **mock zůstává
   výchozí volbou**, aby šlo aplikaci bezpečně zkoušet bez nákladů/klíče.
3. Přepínání probíhá přes existující pole "Provider" v Administraci (dosud netknutelný volný
   text), které se mění na výběr Simulace/OpenAI.

## Navazující artefakty

- [ADR-004 — Vyměnitelní externí AI poskytovatelé (aktualizace)](../../decisions/ADR-004-vymenitelni-externi-ai-poskytovatele.md)
