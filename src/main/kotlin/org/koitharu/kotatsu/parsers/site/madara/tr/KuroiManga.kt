package org.koitharu.kotatsu.parsers.site.madara.tr


import org.koitharu.kotatsu.parsers.MangaLoaderContext
import org.koitharu.kotatsu.parsers.MangaSourceParser
import org.koitharu.kotatsu.parsers.model.ContentType
import org.koitharu.kotatsu.parsers.model.MangaSource
import org.koitharu.kotatsu.parsers.site.madara.MadaraParser


@MangaSourceParser("KUROIMANGA", "Kuroi Manga", "tr", ContentType.HENTAI)
internal class KuroiManga(context: MangaLoaderContext) :
	MadaraParser(context, MangaSource.KUROIMANGA, "www.kuroimanga.com") {

	override val datePattern = "d MMMM yyyy"
}
