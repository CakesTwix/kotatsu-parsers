package org.koitharu.kotatsu.parsers.site.madara.pt

import org.koitharu.kotatsu.parsers.MangaLoaderContext
import org.koitharu.kotatsu.parsers.MangaSourceParser
import org.koitharu.kotatsu.parsers.model.MangaSource
import org.koitharu.kotatsu.parsers.site.madara.MadaraParser

@MangaSourceParser("CERISE_SCANS", "Cerise Scans", "pt")
internal class CeriseScans(context: MangaLoaderContext) :
	MadaraParser(context, MangaSource.CERISE_SCANS, "cerisescan.com/") {

	override val datePattern: String = "dd 'de' MMMMM 'de' yyyy"
}
