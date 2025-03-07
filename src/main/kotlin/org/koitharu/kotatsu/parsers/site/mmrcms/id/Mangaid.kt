package org.koitharu.kotatsu.parsers.site.mmrcms.id


import org.koitharu.kotatsu.parsers.MangaLoaderContext
import org.koitharu.kotatsu.parsers.MangaSourceParser
import org.koitharu.kotatsu.parsers.model.MangaSource
import org.koitharu.kotatsu.parsers.site.mmrcms.MmrcmsParser
import java.util.Locale


@MangaSourceParser("MANGAID", "Mangaid", "id")
internal class Mangaid(context: MangaLoaderContext) :
	MmrcmsParser(context, MangaSource.MANGAID, "mangaid.click") {


	override val selectState = "dt:contains(Status)"
	override val selectAlt = "dt:contains(Other names)"
	override val selectAut = "dt:contains(Author(s))"
	override val selectTag = "dt:contains(Categories)"
	override val sourceLocale: Locale = Locale.ENGLISH
}
