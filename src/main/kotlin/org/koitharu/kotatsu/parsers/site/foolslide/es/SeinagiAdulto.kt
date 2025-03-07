package org.koitharu.kotatsu.parsers.site.foolslide.es


import kotlinx.coroutines.coroutineScope
import org.koitharu.kotatsu.parsers.MangaLoaderContext
import org.koitharu.kotatsu.parsers.MangaSourceParser
import org.koitharu.kotatsu.parsers.model.ContentType
import org.koitharu.kotatsu.parsers.model.Manga
import org.koitharu.kotatsu.parsers.model.MangaSource
import org.koitharu.kotatsu.parsers.site.foolslide.FoolSlideParser
import org.koitharu.kotatsu.parsers.util.domain
import org.koitharu.kotatsu.parsers.util.parseHtml
import org.koitharu.kotatsu.parsers.util.selectFirstOrThrow
import org.koitharu.kotatsu.parsers.util.src
import org.koitharu.kotatsu.parsers.util.toAbsoluteUrl


@MangaSourceParser("SEINAGIADULTO", "Seinagi Adulto", "es", ContentType.HENTAI)
internal class SeinagiAdulto(context: MangaLoaderContext) :
	FoolSlideParser(context, MangaSource.SEINAGIADULTO, "adulto.seinagi.org.es") {


	override suspend fun getDetails(manga: Manga): Manga = coroutineScope {
		val fullUrl = manga.url.toAbsoluteUrl(domain)
		val testAdultPage = webClient.httpGet(fullUrl).parseHtml()

		val doc = if (testAdultPage.selectFirst("div.info form") != null) {
			webClient.httpPost(fullUrl, "adult=true").parseHtml()
		} else {
			testAdultPage
		}
		val chapters = getChapters(manga, doc)

		val desc = if (doc.selectFirstOrThrow(selectInfo).html().contains("Descripción")) {
			doc.selectFirstOrThrow(selectInfo).text().substringAfter("Descripción: ").substringBefore("Lecturas")
		} else {
			doc.selectFirstOrThrow(selectInfo).text()
		}

		val author = if (doc.selectFirstOrThrow(selectInfo).html().contains("Author")) {
			doc.selectFirstOrThrow(selectInfo).text().substringAfter("Author: ").substringBefore("Art")
		} else {
			null
		}

		manga.copy(
			coverUrl = doc.selectFirst(".thumbnail img")?.src().orEmpty(),// for manga result on search
			description = desc,
			altTitle = null,
			author = author,
			state = null,
			chapters = chapters,
		)
	}
}
