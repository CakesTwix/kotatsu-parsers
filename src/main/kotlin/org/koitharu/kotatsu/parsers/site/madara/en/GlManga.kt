package org.koitharu.kotatsu.parsers.site.madara.en

import org.koitharu.kotatsu.parsers.MangaLoaderContext
import org.koitharu.kotatsu.parsers.MangaSourceParser
import org.koitharu.kotatsu.parsers.model.MangaSource
import org.koitharu.kotatsu.parsers.site.madara.MadaraParser

@MangaSourceParser("GLMANGA", "Gl Manga", "en")
internal class GlManga(context: MangaLoaderContext) :
	MadaraParser(context, MangaSource.GLMANGA, "glmanga.com")
