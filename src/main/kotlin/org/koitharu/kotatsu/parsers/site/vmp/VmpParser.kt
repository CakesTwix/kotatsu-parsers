package org.koitharu.kotatsu.parsers.site.vmp

import kotlinx.coroutines.coroutineScope
import org.koitharu.kotatsu.parsers.MangaLoaderContext
import org.koitharu.kotatsu.parsers.PagedMangaParser
import org.koitharu.kotatsu.parsers.config.ConfigKey
import org.koitharu.kotatsu.parsers.model.*
import org.koitharu.kotatsu.parsers.util.*
import java.util.*

internal abstract class VmpParser(
	context: MangaLoaderContext,
	source: MangaSource,
	domain: String,
	pageSize: Int = 24,
) : PagedMangaParser(context, source, pageSize) {

	override val configKeyDomain = ConfigKey.Domain(domain)
	override val sortOrders: Set<SortOrder> = EnumSet.of(SortOrder.UPDATED)

	protected open val listUrl = "xxx/"
	protected open val geneUrl = "genero/"

	init {
		paginator.firstPage = 1
		searchPaginator.firstPage = 1
	}

	override suspend fun getListPage(
		page: Int,
		query: String?,
		tags: Set<MangaTag>?,
		sortOrder: SortOrder,
	): List<Manga> {
		val tag = tags.oneOrThrowIfMany()
		val url = buildString {
			append("https://$domain/")
			if (!tags.isNullOrEmpty()) {
				append(geneUrl)
				append(tag?.key.orEmpty())
				append("/page/")
				append(page.toString())
			} else {
				append(listUrl)
				append("/page/")
				append(page.toString())
				if (!query.isNullOrEmpty()) {
					append("?s=")
					append(query.urlEncoded())
				}
			}
		}

		val doc = webClient.httpGet(url).parseHtml()

		return doc.select("div.blog-list-items div.entry").map { div ->
			val href = div.selectFirstOrThrow("a").attrAsRelativeUrl("href")
			Manga(
				id = generateUid(href),
				url = href,
				publicUrl = href.toAbsoluteUrl(div.host ?: domain),
				coverUrl = div.selectFirst("img")?.src().orEmpty(),
				title = div.selectFirstOrThrow("h2").text().orEmpty(),
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

	override suspend fun getTags(): Set<MangaTag> {
		val doc = webClient.httpGet("https://$domain/$listUrl").parseHtml()
		return doc.select("div.tagcloud a").mapNotNullToSet { a ->
			MangaTag(
				key = a.attr("href").removeSuffix("/").substringAfterLast(geneUrl, ""),
				title = a.text().toTitleCase(),
				source = source,
			)
		}
	}

	override suspend fun getDetails(manga: Manga): Manga = coroutineScope {
		val fullUrl = manga.url.toAbsoluteUrl(domain)
		val doc = webClient.httpGet(fullUrl).parseHtml()

		manga.copy(
			tags = doc.select("div.tax_box div.links ul:not(.post-categories) li a").mapNotNullToSet { a ->
				MangaTag(
					key = a.attr("href").removeSuffix("/").substringAfterLast(geneUrl, ""),
					title = a.text().toTitleCase(),
					source = source,
				)
			},
			description = null,
			altTitle = null,
			author = null,
			state = null,
			chapters = listOf(
				MangaChapter(
					id = manga.id,
					name = manga.title,
					number = 1,
					url = manga.url,
					scanlator = null,
					uploadDate = 0,
					branch = null,
					source = source,
				),
			),
		)
	}

	override suspend fun getPages(chapter: MangaChapter): List<MangaPage> {
		val fullUrl = chapter.url.toAbsoluteUrl(domain)
		val doc = webClient.httpGet(fullUrl).parseHtml()
		return doc.select("div.wp-content img").map { div ->
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
