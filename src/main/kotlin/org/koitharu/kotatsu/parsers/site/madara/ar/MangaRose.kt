package org.koitharu.kotatsu.parsers.site.madara.ar

import org.koitharu.kotatsu.parsers.MangaLoaderContext
import org.koitharu.kotatsu.parsers.MangaSourceParser
import org.koitharu.kotatsu.parsers.model.MangaSource
import org.koitharu.kotatsu.parsers.site.madara.MadaraParser

@MangaSourceParser("MANGAROSE", "Manga Rose", "ar")
internal class MangaRose(context: MangaLoaderContext) :
	MadaraParser(context, MangaSource.MANGAROSE, "mangarose.net", pageSize = 20)
