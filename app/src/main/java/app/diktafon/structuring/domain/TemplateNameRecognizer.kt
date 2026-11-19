package app.diktafon.structuring.domain

interface TemplateNameRecognizer {
    // Vraci jen sablonu, ktera uz je soucasti schvaleneho katalogu (FR-AUD-010) - nikdy volne
    // odvozeny/vymysleny nazev, protoze obsah audia neni duveryhodna instrukce. Realna (OpenAI)
    // implementace proto dostava jen seznam id+nazvu katalogu k vyberu, ne volnost vymyslet.
    suspend fun recognize(transcriptText: String, approvedCatalog: List<Template>): Template?
}
