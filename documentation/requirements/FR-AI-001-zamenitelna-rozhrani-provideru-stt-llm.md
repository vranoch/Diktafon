# FR-AI-001 — Zaměnitelná rozhraní providerů STT/LLM

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-12
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — FR-AI-001
**Priorita:** SHOULD / MVP

## Popis

[NAVRŽENO] Integrace STT a LLM používají zaměnitelná rozhraní providerů (adaptéry), nezávislá na UI a perzistenci.

## Akceptační kritéria

- [ ] STT i LLM providery jsou zapojeny přes rozhraní (`SpeechToTextProvider`, `DocumentProcessor`), ne napevno v doménové logice.

## Související

- Use-case: (žádný)
- ADR: [ADR-004](../decisions/ADR-004-vymenitelni-externi-ai-poskytovatele.md)
- Modul: (zatím žádný — moduly nejsou dosud definovány, viz poznámka v `documentation/modules/README.md`)
