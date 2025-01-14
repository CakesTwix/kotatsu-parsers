package org.koitharu.kotatsu.parsers.site.madara.pt

import org.koitharu.kotatsu.parsers.MangaLoaderContext
import org.koitharu.kotatsu.parsers.MangaSourceParser
import org.koitharu.kotatsu.parsers.model.MangaSource
import org.koitharu.kotatsu.parsers.site.madara.MadaraParser

@MangaSourceParser("IMPERIODABRITANNIA", "ImperiodaBritannia", "pt")
internal class ImperiodaBritannia(context: MangaLoaderContext) :
	MadaraParser(context, MangaSource.IMPERIODABRITANNIA, "imperiodabritannia.com", 10) {

	override val datePattern: String = "dd 'de' MMMMM 'de' yyyy"
	override val withoutAjax = true
}
