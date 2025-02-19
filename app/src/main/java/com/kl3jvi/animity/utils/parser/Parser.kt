package com.kl3jvi.animity.utils.parser

import com.kl3jvi.animity.data.model.ui_models.*
import org.jsoup.select.Elements

interface Parser {
    fun parseRecentSubOrDub(response: String, typeValue: Int): ArrayList<AnimeMetaModel>
    fun parsePopular(response: String, typeValue: Int): ArrayList<AnimeMetaModel>
    fun parseMovie(response: String, typeValue: Int): ArrayList<AnimeMetaModel>
    fun parseAnimeInfo(response: String): AnimeInfoModel
    fun getGenreList(genreHtmlList: Elements): ArrayList<GenreModel>
    fun fetchEpisodeList(response: String): ArrayList<EpisodeModel>
    fun fetchEpisodeReleaseTime(response: String): EpisodeReleaseModel
    fun decryptAES(encrypted: String, key: String, iv: String): String
    fun encryptAes(text: String, key: String, iv: String): String
    fun parseEncryptAjax(response: String, id: String): String
    fun parseMediaUrl(response: String): EpisodeInfo
    fun parseEncryptedUrls(response: String): ArrayList<String>
}
