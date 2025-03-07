package org.koitharu.kotatsu.parsers.site.madara.es

import org.koitharu.kotatsu.parsers.MangaLoaderContext
import org.koitharu.kotatsu.parsers.MangaSourceParser
import org.koitharu.kotatsu.parsers.model.MangaSource
import org.koitharu.kotatsu.parsers.site.madara.MadaraParser


@MangaSourceParser("UNITOONOFICIAL", "UniToonOficial", "es")
internal class UniToonOficial(context: MangaLoaderContext) :
	MadaraParser(context, MangaSource.UNITOONOFICIAL, "unitoonoficial.com") {

	override val datePattern = "dd/MM/yyyy"
	override val tagPrefix = "generos/"
}
