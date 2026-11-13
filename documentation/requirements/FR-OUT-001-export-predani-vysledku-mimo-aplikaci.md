# FR-OUT-001 — Export/předání výsledku mimo aplikaci

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-13
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — FR-OUT-001
- Upřesnění uživatele v rámci revize, 2026-11-12 (tato konverzace) — mechanismus předání potvrzen: copy/paste a odeslání do libovolného kanálu dostupného na zařízení.
- Doplněno na popud uživatele v rámci revize, 2026-11-13 (tato konverzace) — doplněn odkaz na [FR-OUT-004](FR-OUT-004-vyber-prijemce-ze-seznamu-pri-odeslani-emailem.md) (výběr příjemce ze seznamu adres při odeslání e-mailem).
**Priorita:** MUST / MVP

## Popis

[POTVRZENO] Výsledek lze exportovat/předat mimo aplikaci, a to minimálně dvěma způsoby: (a) zkopírováním obsahu do schránky (copy/paste) a (b) odesláním přímo do zvoleného cílového kanálu (e-mail, OneNote, Google Drive apod.) — konkrétně kteréhokoliv kanálu, který je na daném zařízení dostupný, bez nutnosti pevné integrace s každým jednotlivě (viz [FR-OUT-002](FR-OUT-002-export-jako-text-json-a-android-share.md)). Konkrétní dodatečné přímé integrace nad rámec tohoto obecného mechanismu zůstávají otevřené (Q-002, Q-007 — riziko R-007).

## Akceptační kritéria

- [ ] Exportovaný/sdílený obsah odpovídá zobrazené schválené revizi dokumentu (AT-009).
- [ ] Selhání exportu ponechává dokument zachovaný lokálně.
- [ ] Obsah lze zkopírovat do schránky (copy/paste).
- [ ] Obsah lze odeslat do libovolného kanálu dostupného na zařízení přes systémové sdílení (e-mail, OneNote, Google Drive apod.).

## Související

- Use-case: [UC-006](../use-cases/UC-006-exportovat-predat-dokument.md)
- Požadavek: [FR-OUT-004](FR-OUT-004-vyber-prijemce-ze-seznamu-pri-odeslani-emailem.md)
- ADR: (žádný)
- Modul: (zatím žádný — moduly nejsou dosud definovány, viz poznámka v `documentation/modules/README.md`)
