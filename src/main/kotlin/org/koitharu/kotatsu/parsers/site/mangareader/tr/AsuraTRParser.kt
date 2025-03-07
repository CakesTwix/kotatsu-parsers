package org.koitharu.kotatsu.parsers.site.mangareader.tr

import org.koitharu.kotatsu.parsers.MangaLoaderContext
import org.koitharu.kotatsu.parsers.MangaSourceParser
import org.koitharu.kotatsu.parsers.model.MangaSource
import org.koitharu.kotatsu.parsers.site.mangareader.MangaReaderParser

@MangaSourceParser("ASURATR", "Asura Scans (tr)", "tr")
internal class AsuraTRParser(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaSource.ASURATR, "asurascanstr.com", pageSize = 30, searchPageSize = 10) {

	override val datePattern = "MMM d, yyyy"
}
