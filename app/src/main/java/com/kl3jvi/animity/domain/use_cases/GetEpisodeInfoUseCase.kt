package com.kl3jvi.animity.domain.use_cases

import android.util.Log
import com.kl3jvi.animity.domain.repositories.activity_repositories.PlayerRepository
import com.kl3jvi.animity.utils.Constants.Companion.REFERER
import com.kl3jvi.animity.utils.Constants.Companion.getNetworkHeader
import com.kl3jvi.animity.utils.Resource
import com.kl3jvi.animity.utils.logError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetEpisodeInfoUseCase @Inject constructor(
    private val playerRepository: PlayerRepository,
    private val ioDispatcher: CoroutineDispatcher
) {
    /**
     * It emits a loading state, then fetches the episode media url from the repository, and emits the
     * response as a success state
     *
     * @param url The url of the episode to be played
     */
    operator fun invoke(url: String) = flow {
        emit(Resource.Loading())
        try {
            val response = playerRepository.fetchEpisodeMediaUrl(getNetworkHeader(), url)
            emit(Resource.Success(data = response))
        } catch (e: Exception) {
            emit(Resource.Error("Oops an error occurred, try again!"))
        }
    }.flowOn(ioDispatcher)


    /**
     * It fetches the m3u8 url from the server and emits the result as a Resource object
     *
     * @param url The url of the anime you want to fetch the m3u8 url from.
     */
    fun fetchM3U8(url: String?) = flow {
        emit(Resource.Loading())
        try {
            val response = playerRepository.fetchM3u8Url(getNetworkHeader(), url ?: "")
            Log.e("response", response.toString())
            emit(Resource.Success(data = response.last()))
        } catch (e: Exception) {
            logError(e)
            emit(Resource.Error("Couldn't find a Stream for this Anime"))
        }
    }.flowOn(ioDispatcher)


    /**
     * It fetches the encrypted ajax url from the server and returns a stream url
     *
     * @param url The url of the episode you want to fetch the stream for.
     * @param id The id of the anime you want to fetch the stream for.
     */
    fun fetchEncryptedAjaxUrl(url: String?, id: String) = flow {
        emit(Resource.Loading())
        try {
            val response = playerRepository.fetchEncryptedAjaxUrl(getNetworkHeader(), url ?: "", id)
            val streamUrl = "${REFERER}encrypt-ajax.php?${response}"
            emit(Resource.Success(data = streamUrl))
        } catch (e: Exception) {
            logError(e)
            emit(Resource.Error("Couldn't find a Stream for this Anime"))
        }
    }.flowOn(ioDispatcher)
}
