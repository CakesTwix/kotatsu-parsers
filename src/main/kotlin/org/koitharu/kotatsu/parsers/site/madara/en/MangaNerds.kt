package org.koitharu.kotatsu.parsers.site.madara.en

import org.koitharu.kotatsu.parsers.MangaLoaderContext
import org.koitharu.kotatsu.parsers.MangaSourceParser
import org.koitharu.kotatsu.parsers.model.MangaSource
import org.koitharu.kotatsu.parsers.site.madara.MadaraParser

@MangaSourceParser("MANGA_NERDS", "Manga Nerds", "en")
internal class MangaNerds(context: MangaLoaderContext) :
	MadaraParser(context, MangaSource.MANGA_NERDS, "manganerds.com", 20)
