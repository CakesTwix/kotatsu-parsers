package org.koitharu.kotatsu.parsers.site.madara.es

import org.koitharu.kotatsu.parsers.MangaLoaderContext
import org.koitharu.kotatsu.parsers.MangaSourceParser
import org.koitharu.kotatsu.parsers.model.ContentType
import org.koitharu.kotatsu.parsers.model.MangaChapter
import org.koitharu.kotatsu.parsers.model.MangaPage
import org.koitharu.kotatsu.parsers.model.MangaSource
import org.koitharu.kotatsu.parsers.site.madara.MadaraParser
import org.koitharu.kotatsu.parsers.util.*
import java.util.Locale

@MangaSourceParser("DOUJIN_HENTAI_NET", "Doujin Hentai Net", "es", ContentType.HENTAI)
internal class DoujinHentaiNet(context: MangaLoaderContext) :
	MadaraParser(context, MangaSource.DOUJIN_HENTAI_NET, "doujinhentai.net", 18) {
	override val datePattern = "dd MMM. yyyy"
	override val sourceLocale: Locale = Locale.ENGLISH
	override val listUrl = "lista-manga-hentai/"
	override val tagPrefix = "lista-manga-hentai/category/"
	override val selectTestAsync = "div.listing-chapters_wrap"
	override val selectChapter = "li.wp-manga-chapter:contains(Capitulo)"
	override val selectPage = "div#all img"


	override suspend fun getPages(chapter: MangaChapter): List<MangaPage> {
		val fullUrl = chapter.url.toAbsoluteUrl(domain)
		val doc = webClient.httpGet(fullUrl).parseHtml()
		return doc.select(selectPage).map { div ->
			val img = div.selectFirstOrThrow("img")
			val url = img.src()?.toRelativeUrl(domain) ?: div.parseFailed("Image src not found")
			MangaPage(
				id = generateUid(url),
				url = url,
				preview = null,
				source = source,
			)
		}
	}

}
