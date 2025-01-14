package org.koitharu.kotatsu.parsers.site.madara.en

import org.koitharu.kotatsu.parsers.MangaLoaderContext
import org.koitharu.kotatsu.parsers.MangaSourceParser
import org.koitharu.kotatsu.parsers.model.ContentType
import org.koitharu.kotatsu.parsers.model.MangaSource
import org.koitharu.kotatsu.parsers.site.madara.MadaraParser

@MangaSourceParser("MANYCOMIC", "Many Comic", "en", ContentType.HENTAI)
internal class ManyComic(context: MangaLoaderContext) :
	MadaraParser(context, MangaSource.MANYCOMIC, "manycomic.com") {

	override val postreq = true
	override val tagPrefix = "comic-genre/"

}
