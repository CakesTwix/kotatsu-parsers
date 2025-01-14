package org.koitharu.kotatsu.parsers.site.madara.tr


import org.koitharu.kotatsu.parsers.MangaLoaderContext
import org.koitharu.kotatsu.parsers.MangaSourceParser
import org.koitharu.kotatsu.parsers.model.MangaSource
import org.koitharu.kotatsu.parsers.site.madara.MadaraParser


@MangaSourceParser("PIEDPIPERFANSUB", "Piedpiper Fansub", "tr")
internal class PiedpiperFansub(context: MangaLoaderContext) :
	MadaraParser(context, MangaSource.PIEDPIPERFANSUB, "piedpiperfansub.me", 18) {

	override val datePattern = "d MMMM yyyy"
}
