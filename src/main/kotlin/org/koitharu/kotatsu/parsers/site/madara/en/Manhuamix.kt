package org.koitharu.kotatsu.parsers.site.madara.en

import org.koitharu.kotatsu.parsers.MangaLoaderContext
import org.koitharu.kotatsu.parsers.MangaSourceParser
import org.koitharu.kotatsu.parsers.model.MangaSource
import org.koitharu.kotatsu.parsers.site.madara.MadaraParser

@MangaSourceParser("MANHUAMIX", "ManhuaMix", "en")
internal class Manhuamix(context: MangaLoaderContext) :
	MadaraParser(context, MangaSource.MANHUAMIX, "manhuamix.com", 20) {

	override val tagPrefix = "manhua-genre/"
	override val listUrl = "manhua/"
}
