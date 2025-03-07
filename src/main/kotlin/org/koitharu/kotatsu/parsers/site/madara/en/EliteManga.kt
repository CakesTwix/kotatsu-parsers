package org.koitharu.kotatsu.parsers.site.madara.en

import org.koitharu.kotatsu.parsers.MangaLoaderContext
import org.koitharu.kotatsu.parsers.MangaSourceParser
import org.koitharu.kotatsu.parsers.model.MangaSource
import org.koitharu.kotatsu.parsers.site.madara.MadaraParser

@MangaSourceParser("ELITEMANGA", "Elite Manga", "en")
internal class EliteManga(context: MangaLoaderContext) :
	MadaraParser(context, MangaSource.ELITEMANGA, "www.elitemanga.org")
