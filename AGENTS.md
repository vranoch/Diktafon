# AGENTS.md — Diktafon

Toto je **jediný primární zdroj pravdy** o tom, jak se v tomto repozitáři pracuje — platí pro
všechny AI nástroje (Claude Code, Codex, Copilot, Cursor, …) i pro lidi. Nástroj-specifické
soubory (`CLAUDE.md`, `.codex/...`, atd.) smí obsahovat jen odkaz sem + příkazy specifické pro
daný nástroj. Žádná provozní znalost (konvence, workflow, pravidla) se nesmí duplikovat jinde —
pokud se najde jen v nástroj-specifickém souboru, je to chyba a patří přesunout sem.

> **Stav projektu:** založení repozitáře, technologie a architektura zatím nejsou zvoleny.
> Sekce níže jsou kostra k doplnění — neobsahují ještě žádné rozhodnutí o stacku.

## 1. O projektu

- **Diktafon** — TBD: stručný popis účelu aplikace, až bude znám.
- **Tech stack:** TBD.
- **Jazyk kódu / dokumentace:** TBD (doporučení: kód anglicky, dokumentace v jazyce týmu).

## 2. Princip agnosticity nástrojů

- `AGENTS.md` (tento soubor) = jediné místo s provozní znalostí o projektu a pravidly práce.
- `CLAUDE.md`, `.codex/`, případně další nástroj-specifické soubory smí obsahovat pouze:
  odkaz na tento soubor, build/run/test příkazy specifické pro daný nástroj, a odkazy do
  `ai/skills/`.
- Nové "jak na to" postupy patří do [`ai/skills/`](ai/skills/), ne do nástroj-specifických
  souborů.

## 3. Primární zdroje pravdy (v pořadí priority)

1. Tento soubor (`AGENTS.md`)
2. [`documentation/decisions/`](documentation/decisions/) — architektonická rozhodnutí (ADR)
3. [`ai/skills/`](ai/skills/) a [`ai/workflows/`](ai/workflows/) — postupy a workflow
4. Kód a jeho testy

## 4. Mapa projektu

| Složka / soubor | Co tam je |
|---|---|
| `AGENTS.md` | Tento soubor — pravidla a konvence pro AI i lidi |
| `CLAUDE.md` | Tenký wrapper pro Claude Code (odkaz + build příkazy) |
| `README.md` | Lidsky čitelný přehled projektu a quickstart |
| `ai/skills/` | Krátké, nástroj-agnostické návody "jak na to" |
| `ai/workflows/` | Opakované vícekrokové postupy (checklisty) |
| `documentation/decisions/` | Architektonická rozhodnutí (ADR) |
| `_workspace/` | Dočasný scratch prostor (negitovaný, viz README uvnitř) |

Tabulka se doplňuje s tím, jak přibývají nové složky (kód, testy, infrastruktura, ...).

## 5. Workflow pro agenty

- Před větší změnou zkontroluj, jestli existuje relevantní ADR v `documentation/decisions/`
  nebo skill v `ai/skills/`.
- Pokud během práce vznikne nové rozhodnutí o architektuře/designu, zapiš ho jako ADR
  (viz šablona a index v `documentation/decisions/`).
- Nejisté věci (předpoklady, otevřené otázky) označuj přímo v textu tagem `ASSUMPTION:` nebo
  `TODO:`, ať jsou dohledatelné a není nutné je domýšlet.
- Dočasné/rozpracované soubory patří do `_workspace/`, ne do trvalé struktury repozitáře.

## 6. Guardrails

Bez výslovného schválení uživatele agent **nesmí**:

- force-push, `git reset --hard`, mazat větve
- přidávat nové závislosti/balíčky
- měnit již schválené ADR dokumenty (vytvoří se nový ADR, který ten starý nahrazuje)
- vymýšlet architekturu nebo požadavky, které nejsou nikde podložené — takové kroky je třeba
  označit jako `ASSUMPTION` a nechat potvrdit

## 7. Definition of Done

- [ ] Kód odpovídá konvencím zvoleným pro daný stack (viz budoucí sekce "Konvence kódu")
- [ ] Relevantní ADR je založen/aktualizován, pokud změna ovlivňuje architekturu
- [ ] Dočasné soubory z `_workspace/` jsou uklizeny nebo přesunuty na trvalé místo
- [ ] Nic citlivého (klíče, tokeny, osobní údaje) není commitnuto
