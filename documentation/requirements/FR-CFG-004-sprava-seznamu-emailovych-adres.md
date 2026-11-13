# FR-CFG-004 — Správa seznamu e-mailových adres příjemců

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-13
**Změněno:** 2026-11-13
**Zdroje:**
- Nový požadavek uživatele v rámci revize, 2026-11-13 (tato konverzace).
**Priorita:** MUST / MVP

## Popis

[POTVRZENO] V administraci (viz [ADR-010](../decisions/ADR-010-tri-urovne-pouziti-bezne-rozsirene-administracni.md)) lze spravovat seznam e-mailových adres, na které uživatel opakovaně posílá nahrávky, přepisy nebo výsledné dokumenty — přidat novou adresu, upravit nebo smazat existující. Tento seznam slouží jako zdroj pro výběr příjemce při odesílání e-mailem (viz [FR-OUT-004](FR-OUT-004-vyber-prijemce-ze-seznamu-pri-odeslani-emailem.md)).

## Akceptační kritéria

- [ ] V administraci lze přidat, upravit a smazat e-mailovou adresu v seznamu příjemců.
- [ ] Seznam je trvale uložen lokálně a je dostupný při odesílání e-mailem (viz [FR-OUT-004](FR-OUT-004-vyber-prijemce-ze-seznamu-pri-odeslani-emailem.md)).

## Související

- Use-case: (žádný — jednoduchá administrace seznamu; využití popisuje [FR-OUT-004](FR-OUT-004-vyber-prijemce-ze-seznamu-pri-odeslani-emailem.md))
- ADR: [ADR-010](../decisions/ADR-010-tri-urovne-pouziti-bezne-rozsirene-administracni.md)
- Modul: (zatím žádný — moduly nejsou dosud definovány, viz poznámka v [`documentation/modules/README.md`](../modules/README.md))
