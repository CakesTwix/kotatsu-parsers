package org.koitharu.kotatsu.parsers.site.madara.pt

import org.koitharu.kotatsu.parsers.MangaLoaderContext
import org.koitharu.kotatsu.parsers.MangaSourceParser
import org.koitharu.kotatsu.parsers.model.MangaSource
import org.koitharu.kotatsu.parsers.site.madara.MadaraParser

@MangaSourceParser("PEACHSCAN", "Peach Scan", "pt")
internal class PeachScan(context: MangaLoaderContext) :
	MadaraParser(context, MangaSource.PEACHSCAN, "www.peachscan.com", 10) {

	override val datePattern: String = "dd 'de' MMMMM 'de' yyyy"
}
