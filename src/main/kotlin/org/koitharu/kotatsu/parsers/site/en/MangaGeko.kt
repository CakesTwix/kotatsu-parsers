package org.koitharu.kotatsu.parsers.site.en

import okhttp3.Headers
import org.koitharu.kotatsu.parsers.MangaLoaderContext
import org.koitharu.kotatsu.parsers.MangaSourceParser
import org.koitharu.kotatsu.parsers.PagedMangaParser
import org.koitharu.kotatsu.parsers.config.ConfigKey
import org.koitharu.kotatsu.parsers.model.*
import org.koitharu.kotatsu.parsers.network.UserAgents
import org.koitharu.kotatsu.parsers.util.*
import java.text.SimpleDateFormat
import java.util.*

@MangaSourceParser("MANGAGEKO", "MangaGeko", "en")
internal class MangaGeko(context: MangaLoaderContext) : PagedMangaParser(context, MangaSource.MANGAGEKO, 30) {

	override val sortOrders: Set<SortOrder> = EnumSet.of(SortOrder.POPULARITY, SortOrder.UPDATED, SortOrder.NEWEST)

	override val configKeyDomain = ConfigKey.Domain("www.mangageko.com")

	override val headers: Headers = Headers.Builder()
		.add("User-Agent", UserAgents.CHROME_DESKTOP)
		.build()

	override suspend fun getListPage(
		page: Int,
		query: String?,
		tags: Set<MangaTag>?,
		sortOrder: SortOrder,
	): List<Manga> {
		val tag = tags.oneOrThrowIfMany()

		val url = if (!query.isNullOrEmpty()) {
			if (page > 1) {
				return emptyList()
			}
			buildString {
				append("https://$domain/search/?search=")
				append(query.urlEncoded())
			}
		} else {
			buildString {
				append("https://$domain/browse-comics/?results=")
				append(page)
				append("&filter=")
				when (sortOrder) {
					SortOrder.POPULARITY -> append("views")
					SortOrder.UPDATED -> append("Updated")
					SortOrder.NEWEST -> append("New")
					else -> append("Updated")
				}
				if (!tags.isNullOrEmpty()) {
					append("&genre=")
					append(tag?.key.orEmpty())
				}
			}
		}

		val doc = webClient.httpGet(url).parseHtml()

		return doc.select("li.novel-item").map { div ->
			val href = div.selectFirstOrThrow("a").attrAsAbsoluteUrl("href")
			Manga(
				id = generateUid(href),
				title = div.selectFirstOrThrow("h4").text(),
				altTitle = null,
				url = href,
				publicUrl = href.toAbsoluteUrl(domain),
				rating = RATING_UNKNOWN,
				isNsfw = false,
				coverUrl = div.selectFirstOrThrow("img").src().orEmpty(),
				tags = emptySet(),
				state = null,
				author = div.selectFirstOrThrow("h6").text().removePrefix("Author(S): "),
				source = source,
			)
		}
	}


	override suspend fun getTags(): Set<MangaTag> {
		val doc = webClient.httpGet("https://$domain/browse-comics/").parseHtml()
		return doc.select("label.checkbox-inline").mapNotNullToSet { label ->
			MangaTag(
				key = label.selectFirstOrThrow("input").attr("value"),
				title = label.text(),
				source = source,
			)
		}
	}

	override suspend fun getDetails(manga: Manga): Manga {
		val doc = webClient.httpGet(manga.url.toAbsoluteUrl(domain)).parseHtml()
		val dateFormat = SimpleDateFormat("MMM dd, yyyy", sourceLocale)
		return manga.copy(
			altTitle = doc.selectFirstOrThrow(".alternative-title").text(),
			state = when (doc.selectFirstOrThrow(".header-stats span:contains(Status) strong").text()) {
				"Ongoing" -> MangaState.ONGOING
				"Completed" -> MangaState.FINISHED
				else -> null
			},
			tags = doc.select(".categories ul li a").mapNotNullToSet { a ->
				MangaTag(
					key = a.attr("href").substringAfterLast("="),
					title = a.text(),
					source = source,
				)
			},
			author = doc.selectFirstOrThrow(".author").text(),
			description = doc.selectFirstOrThrow(".description").html(),
			chapters = doc.requireElementById("chapters").select("ul.chapter-list li")
				.mapChapters(reversed = true) { i, li ->
					val a = li.selectFirstOrThrow("a")
					val url = a.attrAsRelativeUrl("href")
					val name = li.selectFirstOrThrow(".chapter-title").text()
					val dateText = li.select(".chapter-update").attr("datetime").substringBeforeLast(',')
						.replace(".", "").replace("Sept", "Sep")
					MangaChapter(
						id = generateUid(url),
						name = name,
						number = i + 1,
						url = url,
						scanlator = null,
						uploadDate = dateFormat.tryParse(dateText),
						branch = null,
						source = source,
					)
				},
		)
	}

	override suspend fun getPages(chapter: MangaChapter): List<MangaPage> {
		val fullUrl = chapter.url.toAbsoluteUrl(domain)
		val doc = webClient.httpGet(fullUrl).parseHtml()

		return doc.requireElementById("chapter-reader").select("img").map { img ->
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
