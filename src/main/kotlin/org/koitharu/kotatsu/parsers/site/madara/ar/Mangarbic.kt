package org.koitharu.kotatsu.parsers.site.madara.ar

import org.koitharu.kotatsu.parsers.MangaLoaderContext
import org.koitharu.kotatsu.parsers.MangaSourceParser
import org.koitharu.kotatsu.parsers.model.MangaSource
import org.koitharu.kotatsu.parsers.site.madara.MadaraParser

@MangaSourceParser("MANGARBIC", "Mangarbic", "ar")
internal class Mangarbic(context: MangaLoaderContext) :
	MadaraParser(context, MangaSource.MANGARBIC, "mangarabic.com") {
	override val postreq = true
	override val datePattern = "yyyy/MM/dd"
}
