package org.koitharu.kotatsu.parsers.site.madara.pt

import org.koitharu.kotatsu.parsers.MangaLoaderContext
import org.koitharu.kotatsu.parsers.MangaSourceParser
import org.koitharu.kotatsu.parsers.model.MangaSource
import org.koitharu.kotatsu.parsers.site.madara.MadaraParser

@MangaSourceParser("SINENSISSCANS", "Sinensis Scans", "pt")
internal class SinensisScans(context: MangaLoaderContext) :
	MadaraParser(context, MangaSource.SINENSISSCANS, "sinensisscans.com") {

	override val datePattern: String = "dd/MM/yyyy"
}
