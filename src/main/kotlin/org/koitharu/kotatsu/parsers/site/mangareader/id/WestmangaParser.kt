package org.koitharu.kotatsu.parsers.site.mangareader.id

import org.koitharu.kotatsu.parsers.MangaLoaderContext
import org.koitharu.kotatsu.parsers.MangaSourceParser
import org.koitharu.kotatsu.parsers.model.MangaSource
import org.koitharu.kotatsu.parsers.site.mangareader.MangaReaderParser
import java.util.*

@MangaSourceParser("WESTMANGA", "Westmanga", "id")
internal class WestmangaParser(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaSource.WESTMANGA, "westmanga.info", pageSize = 20, searchPageSize = 10) {

	override val sourceLocale: Locale = Locale.ENGLISH
}
