package org.koitharu.kotatsu.parsers.site.madara.pt

import org.koitharu.kotatsu.parsers.MangaLoaderContext
import org.koitharu.kotatsu.parsers.MangaSourceParser
import org.koitharu.kotatsu.parsers.model.MangaSource
import org.koitharu.kotatsu.parsers.site.madara.MadaraParser

@MangaSourceParser("CAFECOMYAOI", "Cafecom Yaoi", "pt")
internal class CafecomYaoi(context: MangaLoaderContext) :
	MadaraParser(context, MangaSource.CAFECOMYAOI, "cafecomyaoi.com.br") {

	override val datePattern = "dd/MM/yyyy"
	override val postreq = true
}
