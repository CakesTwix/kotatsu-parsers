package org.koitharu.kotatsu.parsers.site.madara.ko

import org.koitharu.kotatsu.parsers.MangaLoaderContext
import org.koitharu.kotatsu.parsers.MangaSourceParser
import org.koitharu.kotatsu.parsers.model.ContentType
import org.koitharu.kotatsu.parsers.model.MangaSource
import org.koitharu.kotatsu.parsers.site.madara.MadaraParser
import java.util.*

@MangaSourceParser("RAWDEX", "Raw Dex", "ko", ContentType.HENTAI)
internal class RawDex(context: MangaLoaderContext) :
	MadaraParser(context, MangaSource.RAWDEX, "rawdex.net", 40) {

	override val sourceLocale: Locale = Locale.ENGLISH
}
