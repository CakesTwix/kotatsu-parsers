package org.koitharu.kotatsu.parsers.site

import org.koitharu.kotatsu.parsers.MangaLoaderContext
import org.koitharu.kotatsu.parsers.MangaParser
import org.koitharu.kotatsu.parsers.exception.ParseException
import org.koitharu.kotatsu.parsers.model.*
import org.koitharu.kotatsu.parsers.util.*
import java.text.SimpleDateFormat
import java.util.*

internal class MangaOwlParser(override val context: MangaLoaderContext) : MangaParser() {

	override val source = MangaSource.MANGAOWL

	override val defaultDomain = "mangaowls.com"

	override val sortOrders: Set<SortOrder> = EnumSet.of(
		SortOrder.POPULARITY,
		SortOrder.NEWEST,
		SortOrder.UPDATED,
	)

	override suspend fun getList(
		offset: Int,
		query: String?,
		tags: Set<MangaTag>?,
		sortOrder: SortOrder?,
	): List<Manga> {
		val page = (offset / 36f).toIntUp().inc()
		val link = buildString {
			append("https://")
			append(getDomain())
			when {
				!query.isNullOrEmpty() -> {
					append("/search/$page?search=")
					append(query.urlEncoded())
				}
				!tags.isNullOrEmpty() -> {
					for (tag in tags) {
						append(tag.key)
					}
					append("/$page?type=${getAlternativeSortKey(sortOrder)}")
				}
				else -> {
					append("/${getSortKey(sortOrder)}/$page")
				}
			}
		}
		val doc = context.httpGet(link).parseHtml()
		val slides = doc.body().select("ul.slides") ?: parseFailed("An error occurred while parsing")
		val items = slides.select("div.col-md-2")
		return items.mapNotNull { item ->
			val href = item.selectFirst("h6 a")?.relUrl("href") ?: return@mapNotNull null
			Manga(
				id = generateUid(href),
				title = item.selectFirst("h6 a")?.text() ?: return@mapNotNull null,
				coverUrl = item.select("div.img-responsive").attr("abs:data-background-image"),
				altTitle = null,
				author = null,
				rating = runCatching {
					item.selectFirst("div.block-stars")
						?.text()
						?.toFloatOrNull()
						?.div(10f)
				}.getOrNull() ?: Manga.NO_RATING,
				url = href,
				publicUrl = href.withDomain(),
				source = source,
			)
		}
	}

	override suspend fun getDetails(manga: Manga): Manga {
		val doc = context.httpGet(manga.publicUrl).parseHtml()
		val info = doc.body().selectFirst("div.single_detail") ?: parseFailed("An error occurred while parsing")
		val table = doc.body().selectFirst("div.single-grid-right") ?: parseFailed("An error occurred while parsing")
		val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)
		val trRegex = "window\\['tr'] = '([^']*)';".toRegex(RegexOption.IGNORE_CASE)
		val trElement =
			doc.getElementsByTag("script").find { trRegex.find(it.data()) != null } ?: parseFailed("Oops, tr not found")
		val tr = trRegex.find(trElement.data())!!.groups[1]!!.value
		val s = context.encodeBase64(defaultDomain.toByteArray())
		return manga.copy(
			description = info.selectFirst(".description")?.html(),
			largeCoverUrl = info.select("img").first()?.let { img ->
				if (img.hasAttr("data-src")) img.attr("abs:data-src") else img.attr("abs:src")
			},
			author = info.selectFirst("p.fexi_header_para a.author_link")?.text(),
			state = parseStatus(info.select("p.fexi_header_para:contains(status)").first()?.ownText()),
			tags = manga.tags + info.select("div.col-xs-12.col-md-8.single-right-grid-right > p > a[href*=genres]")
				.mapNotNull {
					val a = it.selectFirst("a") ?: return@mapNotNull null
					MangaTag(
						title = a.text().toTitleCase(),
						key = a.attr("href"),
						source = source,
					)
				},
			chapters = table.select("div.table.table-chapter-list").select("li.list-group-item.chapter_list")
				.asReversed().mapIndexed { i, li ->
					val a = li.select("a")
					val href = a.attr("data-href").ifEmpty {
						parseFailed("Link is missing")
					}
					MangaChapter(
						id = generateUid(href),
						name = a.select("label").text(),
						number = i + 1,
						url = "$href?tr=$tr&s=$s",
						scanlator = null,
						branch = null,
						uploadDate = dateFormat.tryParse(li.selectFirst("small:last-of-type")?.text()),
						source = MangaSource.MANGAOWL,
					)
				},
		)
	}

	override suspend fun getPages(chapter: MangaChapter): List<MangaPage> {
		val fullUrl = chapter.url.withDomain()
		val doc = context.httpGet(fullUrl).parseHtml()
		val root = doc.body().select("div.item img.owl-lazy") ?: throw ParseException("Root not found")
		return root.map { div ->
			val url = div?.relUrl("data-src") ?: parseFailed("Page image not found")
			MangaPage(
				id = generateUid(url),
				url = url,
				preview = null,
				referer = url,
				source = MangaSource.MANGAOWL,
			)
		}
	}

	private fun parseStatus(status: String?) = when {
		status == null -> null
		status.contains("Ongoing") -> MangaState.ONGOING
		status.contains("Completed") -> MangaState.FINISHED
		else -> null
	}

	override suspend fun getTags(): Set<MangaTag> {
		val doc = context.httpGet("https://${getDomain()}/").parseHtml()
		val root = doc.body().select("ul.dropdown-menu.multi-column.columns-3").select("li")
		return root.mapToSet { p ->
			val a = p.selectFirst("a") ?: parseFailed("a is null")
			MangaTag(
				title = a.text().toTitleCase(),
				key = a.attr("href"),
				source = source,
			)
		}
	}

	private fun getSortKey(sortOrder: SortOrder?) =
		when (sortOrder ?: sortOrders.minByOrNull { it.ordinal }) {
			SortOrder.POPULARITY -> "popular"
			SortOrder.NEWEST -> "new_release"
			SortOrder.UPDATED -> "lastest"
			else -> "lastest"
		}

	private fun getAlternativeSortKey(sortOrder: SortOrder?) =
		when (sortOrder ?: sortOrders.minByOrNull { it.ordinal }) {
			SortOrder.POPULARITY -> "0"
			SortOrder.NEWEST -> "2"
			SortOrder.UPDATED -> "3"
			else -> "3"
		}
}