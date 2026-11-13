package app.diktafon.structuring.domain

interface TemplateNameRecognizer {
    // Rozpoznava jen presny nazev sablony ze schvaleneho katalogu uvnitr textu (FR-AUD-010) -
    // nikdy volne odvozeny/vymysleny nazev, protoze obsah audia neni duveryhodna instrukce.
    fun recognize(transcriptText: String, approvedCatalog: List<Template>): Template?
}
