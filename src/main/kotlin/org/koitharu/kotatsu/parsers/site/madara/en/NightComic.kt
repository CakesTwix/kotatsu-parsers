package org.koitharu.kotatsu.parsers.site.madara.en

import org.koitharu.kotatsu.parsers.MangaLoaderContext
import org.koitharu.kotatsu.parsers.MangaSourceParser
import org.koitharu.kotatsu.parsers.model.MangaSource
import org.koitharu.kotatsu.parsers.site.madara.MadaraParser

@MangaSourceParser("NIGHTCOMIC", "NightComic", "en")
internal class NightComic(context: MangaLoaderContext) :
	MadaraParser(context, MangaSource.NIGHTCOMIC, "www.nightcomic.com") {

	override val postreq = true
}
