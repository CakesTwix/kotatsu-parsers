package org.koitharu.kotatsu.parsers.site.fmreader.ja


import org.jsoup.nodes.Document
import org.koitharu.kotatsu.parsers.MangaLoaderContext
import org.koitharu.kotatsu.parsers.MangaSourceParser
import org.koitharu.kotatsu.parsers.model.Manga
import org.koitharu.kotatsu.parsers.model.MangaChapter
import org.koitharu.kotatsu.parsers.model.MangaPage
import org.koitharu.kotatsu.parsers.model.MangaSource
import org.koitharu.kotatsu.parsers.model.MangaTag
import org.koitharu.kotatsu.parsers.model.RATING_UNKNOWN
import org.koitharu.kotatsu.parsers.model.SortOrder
import org.koitharu.kotatsu.parsers.site.fmreader.FmreaderParser
import org.koitharu.kotatsu.parsers.util.*
import java.text.SimpleDateFormat


@MangaSourceParser("KLZ9", "Klz9", "ja")
internal class Klz9(context: MangaLoaderContext) :
	FmreaderParser(context, MangaSource.KLZ9, "klz9.com") {

	override val selectDesc = "div.row:contains(Description)"
	override val selectState = "ul.manga-info li:contains(Status) a"
	override val selectAlt = "ul.manga-info li:contains(Other name (s))"
	override val selectTag = "ul.manga-info li:contains(Genre(s)) a"
	override val selectChapter = "tr"
	override val selectDate = "td i"
	override val selectPage = "img"
	override val selectBodyTag = "div.panel-body a"

	override suspend fun getListPage(
		page: Int,
		query: String?,
		tags: Set<MangaTag>?,
		sortOrder: SortOrder,
	): List<Manga> {
		val tag = tags.oneOrThrowIfMany()
		val url = buildString {
			append("https://")
			append(domain)
			append("/$listeurl")
			append("?page=")
			append(page.toString())
			when {
				!query.isNullOrEmpty() -> {

					append("&name=")
					append(query.urlEncoded())
				}

				!tags.isNullOrEmpty() -> {
					append("&genre=")
					append(tag?.key.orEmpty())
				}
			}

			append("&sort=")
			when (sortOrder) {
				SortOrder.POPULARITY -> append("views")
				SortOrder.UPDATED -> append("last_update")
				SortOrder.ALPHABETICAL -> append("name")
				else -> append("last_update")
			}
		}
		val doc = webClient.httpGet(url).parseHtml()

		return doc.select("div.thumb-item-flow").map { div ->
			val href = "/" + div.selectFirstOrThrow("a").attrAsRelativeUrl("href")
			Manga(
				id = generateUid(href),
				url = href,
				publicUrl = href.toAbsoluteUrl(div.host ?: domain),
				coverUrl = div.selectFirstOrThrow("div.img-in-ratio").attr("style").substringAfter("('")
					.substringBeforeLast("')"),
				title = div.selectFirstOrThrow("div.series-title").text().orEmpty(),
				altTitle = null,
				rating = RATING_UNKNOWN,
				tags = emptySet(),
				author = null,
				state = null,
				source = source,
				isNsfw = isNsfwSource,
			)
		}
	}

	override suspend fun getChapters(manga: Manga, doc: Document): List<MangaChapter> {
		val slug = doc.selectFirstOrThrow("div.h0rating").attr("slug")
		val docload =
			webClient.httpGet("https://$domain/app/manga/controllers/cont.listChapter.php?slug=$slug").parseHtml()
		val dateFormat = SimpleDateFormat(datePattern, sourceLocale)
		return docload.body().select(selectChapter).mapChapters(reversed = true) { i, a ->
			val href = "/" + a.selectFirstOrThrow("a.chapter").attrAsRelativeUrl("href")
			val dateText = a.selectFirst(selectDate)?.text()
			MangaChapter(
				id = generateUid(href),
				name = a.selectFirstOrThrow("a").text(),
				number = i + 1,
				url = href,
				uploadDate = parseChapterDate(
					dateFormat,
					dateText,
				),
				source = source,
				scanlator = null,
				branch = null,
			)
		}
	}

	override suspend fun getPages(chapter: MangaChapter): List<MangaPage> {
		val fullUrl = chapter.url.toAbsoluteUrl(domain)
		val doc = webClient.httpGet(fullUrl).parseHtml()

		val cid = doc.selectFirstOrThrow("#chapter").attr("value")
		val docload = webClient.httpGet("https://$domain/app/manga/controllers/cont.listImg.php?cid=$cid").parseHtml()

		return docload.select(selectPage).map { img ->
			val url = img.src()?.toRelativeUrl(domain) ?: img.parseFailed("Image src not found")

			MangaPage(
				id = generateUid(url),
				url = url,
				preview = null,
				source = source,
			)
		}
	}

}
