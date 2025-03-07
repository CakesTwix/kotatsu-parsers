package org.koitharu.kotatsu.parsers.site.madara.pt

import org.koitharu.kotatsu.parsers.MangaLoaderContext
import org.koitharu.kotatsu.parsers.MangaSourceParser
import org.koitharu.kotatsu.parsers.model.ContentType
import org.koitharu.kotatsu.parsers.model.MangaSource
import org.koitharu.kotatsu.parsers.site.madara.MadaraParser

@MangaSourceParser("MOONLOVERSSCAN", "Moon Lovers Scan", "pt", ContentType.HENTAI)
internal class MoonLoversScan(context: MangaLoaderContext) :
	MadaraParser(context, MangaSource.MOONLOVERSSCAN, "moonloversscan.com.br", 10)
