# FR-TPL-007 — Automaticky doplňovaná pole šablony

**Stav:** PŘIJATO
**Vytvořeno:** 2026-11-16
**Změněno:** 2026-11-16
**Zdroje:**
- [Chat-extract — Zpětná vazba při odvozování, editovatelný draft, automaticky doplňovaná pole, 2026-11-16](../source-materials/chat-extracts/2026-11-16-zpetna-vazba-editace-draftu-auto-doplneni.md)
**Priorita:** SHOULD / NEXT

## Popis

U pole šablony lze při jejím vytváření/úpravě (viz [FR-TPL-004](FR-TPL-004-vytvoreni-a-uprava-sablony-z-prikladu-s-testovanim.md))
zapnout automatické doplnění hodnoty z metadat nahrávky místo vyplnění z přepisu přes LLM —
zatím jediný podporovaný druh je **datum a čas vzniku nahrávky**. Takto označené pole se při
zpracování nahrávky podle šablony (viz [FR-LLM-001](FR-LLM-001-predani-prepisu-a-sablony-llm.md))
dosadí z metadat nahrávky bez ohledu na to, co (pokud něco) k němu vrátí LLM, a nepočítá se mezi
nejasnosti/chybějící údaje k doplnění (viz [FR-LLM-006](FR-LLM-006-upozorneni-na-nejasnosti-a-chybejici-udaje.md)),
protože jde o údaj, který aplikace zná jistě. I takto doplněnou hodnotu lze na Kontrole (viz
[FR-REV-001](FR-REV-001-kontrola-a-oprava-vysledneho-dokumentu.md)) jako kterékoliv jiné pole
ručně opravit.

Důvod: datum/čas vzniku záznamu bývá v šabloně očekáváno, ale při mluveném záznamu se typicky
nahlas nediktuje, takže LLM nemá z čeho takové pole vyplnit.

## Akceptační kritéria

- [ ] Při vytváření/úpravě šablony lze u libovolného pole zapnout automatické doplnění datem a
      časem vzniku nahrávky.
- [ ] Takto označené pole se při zpracování nahrávky podle šablony vyplní datem/časem nahrávky
      bez ohledu na výstup LLM.
- [ ] Automaticky doplněné pole se nepočítá mezi chybějící povinné údaje/nejasnosti.
- [ ] Automaticky doplněnou hodnotu lze na Kontrole ručně přepsat jako kterékoliv jiné pole.

## Související

- Use-case: [UC-012](../use-cases/UC-012-vytvorit-a-otestovat-sablonu.md), [UC-004](../use-cases/UC-004-strukturovat-text-podle-sablony.md)
- FR: [FR-TPL-002](FR-TPL-002-struktura-sablony-id-verze-schema-instrukce.md), [FR-TPL-004](FR-TPL-004-vytvoreni-a-uprava-sablony-z-prikladu-s-testovanim.md), [FR-LLM-006](FR-LLM-006-upozorneni-na-nejasnosti-a-chybejici-udaje.md)
- ADR: [ADR-006](../decisions/ADR-006-schema-sablony-je-kontrakt.md)
- Modul: (zatím žádný — moduly nejsou dosud definovány, viz poznámka v [documentation/modules/README.md](../modules/README.md))
