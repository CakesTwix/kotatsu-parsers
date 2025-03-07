package org.koitharu.kotatsu.parsers.site.madara.tr

import org.koitharu.kotatsu.parsers.MangaLoaderContext
import org.koitharu.kotatsu.parsers.MangaSourceParser
import org.koitharu.kotatsu.parsers.model.MangaSource
import org.koitharu.kotatsu.parsers.site.madara.MadaraParser

@MangaSourceParser("GUNCEL_MANGA", "Guncel Manga", "tr")
internal class GuncelManga(context: MangaLoaderContext) :
	MadaraParser(context, MangaSource.GUNCEL_MANGA, "guncelmanga.net") {

	override val datePattern = "d MMMM yyyy"
}
