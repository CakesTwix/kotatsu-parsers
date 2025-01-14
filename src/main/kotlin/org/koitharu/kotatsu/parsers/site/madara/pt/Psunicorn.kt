package org.koitharu.kotatsu.parsers.site.madara.pt

import org.koitharu.kotatsu.parsers.MangaLoaderContext
import org.koitharu.kotatsu.parsers.MangaSourceParser
import org.koitharu.kotatsu.parsers.model.ContentType
import org.koitharu.kotatsu.parsers.model.MangaSource
import org.koitharu.kotatsu.parsers.site.madara.MadaraParser

@MangaSourceParser("PSUNICORN", "Psunicorn", "pt", ContentType.HENTAI)
internal class Psunicorn(context: MangaLoaderContext) :
	MadaraParser(context, MangaSource.PSUNICORN, "psunicorn.com")
