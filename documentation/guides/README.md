# documentation/guides/

**Guides** jsou živé, lidsky i AI čitelné referenční dokumenty — vysvětlují, jak něco v projektu
funguje nebo jak něco udělat end-to-end (např. "jak nastavit vývojové prostředí", "jak nasadit
novou verzi", "jak funguje autentizace"). Na rozdíl od ostatních kategorií v `documentation/`
(viz [AGENTS.md §5](../../AGENTS.md)) guide **nemá** stavový cyklus `WORKING/APPROVED` — je to
živý dokument, který se průběžně aktualizuje tak, jak se mění realita, kterou popisuje.

## Kdy psát guide (a kdy ne)

- **Guide** — vysvětluje širší kontext/postup, který se bude opakovaně číst a bude se měnit
  s vývojem projektu (onboarding, provoz, troubleshooting, "jak na X v tomto projektu").
- **[`ai/skills/`](../../ai/skills/)** — krátký, úzce zaměřený AI-konzumovatelný návod
  přikládaný k promptu. Pokud postup slouží hlavně AI agentovi při konkrétním úkolu, patří tam.
- **[ADR](../decisions/)** — jednorázové architektonické rozhodnutí s odůvodněním, ne návod.

## Konvence

- Soubor `kebab-case.md` v `documentation/guides/`.
- Povinná metadata na začátku (bez pole `Stav`, protože guide nemá lifecycle):
  ```
  **Vytvořeno:** YYYY-MM-DD
  **Změněno:** YYYY-MM-DD
  **Zdroje:** (odkazy na chat-extract, ADR, apod. — pokud relevantní)
  ```
- Struktura: **Účel** (pro koho a proč) → **Předpoklady** → **Postup** → **Související odkazy**
  (klikatelné markdown odkazy, viz [AGENTS.md §5.4](../../AGENTS.md)).
- Index se aktualizuje ve stejném commitu jako guide (viz [AGENTS.md §5.3](../../AGENTS.md)).

Viz [`_template.md`](_template.md) pro šablonu nového guide.

## Index

| Název | Změněno | Shrnutí |
|---|---|---|
| — | — | zatím žádný guide |
