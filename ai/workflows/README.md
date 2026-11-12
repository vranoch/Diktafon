# ai/workflows/

Opakované vícekrokové postupy zapsané jako checklisty v Markdownu (např. "přidání nové
feature", "release checklist", "code review před mergem"). Na rozdíl od `ai/skills/`
(jednotlivé návody) jde o sekvenci kroků, které se často kombinují s ADR/skill odkazy.

## Konvence pro workflow

- Soubor `kebab-case.md` s názvem workflow.
- Struktura: krátký popis účelu + číslovaný/checklist seznam kroků, u relevantních kroků
  odkaz na konkrétní skill nebo ADR.
- Workflow popisuje **co a v jakém pořadí**, ne detailní "jak" — to patří do skillu.

Viz [`_template.md`](_template.md) pro šablonu nového workflow.
