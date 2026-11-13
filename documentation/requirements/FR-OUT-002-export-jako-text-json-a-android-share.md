# FR-OUT-002 — Export jako formátovaný text a Android Share (JSON doplňkově)

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-12
**Změněno:** 2026-11-13
**Zdroje:**
- [PRODUCT_REQUIREMENTS_ARCHITECTURE (zdrojový dokument, 2026-11-12)](../source-materials/source-documents/20261112_PRODUCT_REQUIREMENTS_ARCHITECTURE.md) — FR-OUT-002
- Upřesnění uživatele v rámci revize, 2026-11-12 (tato konverzace) — copy/paste a sdílení do libovolného dostupného kanálu jsou závazné (MUST), ne jen volitelné.
- Upřesnění uživatele v rámci revize, 2026-11-12 (tato konverzace) — pokud je výsledkem formátovaný text (např. Markdown), musí jít přenést i ten; export technického JSON je podstatně méně důležitý.
- Doplněno na popud uživatele v rámci revize, 2026-11-13 (tato konverzace) — doplněn odkaz na [FR-OUT-004](FR-OUT-004-vyber-prijemce-ze-seznamu-pri-odeslani-emailem.md) (výběr příjemce ze seznamu adres při odeslání e-mailem).
**Priorita:** MUST / MVP

## Popis

[POTVRZENO] MVP nabízí export výsledku v podobě, v jaké je uživateli zobrazen — tedy jako čitelný formátovaný text (např. Markdown, pokud je výstup takto formátován), k dispozici přes zkopírování do schránky (copy/paste) a přes systémové Android Share (sdílení do libovolné aplikace dostupné na zařízení — e-mail, OneNote, Google Drive apod., bez nutnosti vlastní integrace s každou z nich). Konkrétní cílové aplikace nejsou pevně dané — závisí na tom, co uživatel na svém zařízení má nainstalované.

Export syrového strukturovaného JSON (technická reprezentace dat podle schématu šablony) je oproti tomu podstatně méně důležitý — má smysl spíše jako doplňková/technická možnost (ladění, strojové zpracování), ne jako hlavní způsob předání výsledku uživatelem.

## Akceptační kritéria

- [ ] Export ve formátu, v jakém je výsledek zobrazen uživateli (prostý nebo formátovaný text, např. Markdown), je dostupný z detailu dokumentu.
- [ ] Obsah dokumentu v tomto formátu lze zkopírovat do schránky (copy/paste).
- [ ] Export přes Android Share Intent je dostupný a nabízí všechny aplikace na zařízení, které umí přijmout text/soubor (e-mail, OneNote, Google Drive apod.).
- [ ] Export syrového JSON může být nabídnut jako doplňková možnost, ale není podmínkou pro splnění tohoto požadavku.

## Související

- Use-case: [UC-006](../use-cases/UC-006-exportovat-predat-dokument.md)
- Požadavek: [FR-OUT-004](FR-OUT-004-vyber-prijemce-ze-seznamu-pri-odeslani-emailem.md)
- ADR: (žádný)
- Modul: (zatím žádný — moduly nejsou dosud definovány, viz poznámka v `documentation/modules/README.md`)
