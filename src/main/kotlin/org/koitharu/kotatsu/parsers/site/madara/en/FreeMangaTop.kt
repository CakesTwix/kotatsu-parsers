package org.koitharu.kotatsu.parsers.site.madara.en

import org.koitharu.kotatsu.parsers.MangaLoaderContext
import org.koitharu.kotatsu.parsers.MangaSourceParser
import org.koitharu.kotatsu.parsers.model.MangaSource
import org.koitharu.kotatsu.parsers.site.madara.MadaraParser

@MangaSourceParser("FREEMANGATOP", "Free Manga Top", "en")
internal class FreeMangaTop(context: MangaLoaderContext) :
	MadaraParser(context, MangaSource.FREEMANGATOP, "freemangatop.com") {

	override val datePattern = "MM/dd/yyyy"
}
