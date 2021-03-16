package br.com.diasdeseries.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.diasdeseries.data.pojo.DetailSerieData
import br.com.diasdeseries.repository.SeriesTvRepository
import kotlinx.coroutines.launch
import br.com.diasdeseries.R
import br.com.diasdeseries.data.db.entity.FavoriteSeriesEntity
import br.com.diasdeseries.data.pojo.EpisodesSerieData
import br.com.diasdeseries.repository.FavoriteSeriesRepository

class DetailSerieViewModel(
    private val seriesTvRepository: SeriesTvRepository,
) : ViewModel() {

    companion object {
        private val TAG = FavoriteSeriesViewModel.javaClass.simpleName
    }

    private val detailSerieMutableLiveData = MutableLiveData<Result<DetailSerieData>>()
    val detailSerieLiveData: LiveData<Result<DetailSerieData>>
        get() = detailSerieMutableLiveData

    private val episodesSerieMutableLiveData = MutableLiveData<Result<List<EpisodesSerieData>>>()
    val episodeSerieLiveData: LiveData<Result<List<EpisodesSerieData>>>
        get() = episodesSerieMutableLiveData

    private val messageErrorMutableLiveData = MutableLiveData<Int>()
    val messageErrorLiveData: LiveData<Int>
        get() = messageErrorMutableLiveData

    private val isShowLoadingMutableLiveData = MutableLiveData<Boolean>()
    val isShowLoadingLiveData : LiveData<Boolean>
        get() = isShowLoadingMutableLiveData

    fun getDetailSerieWithId(id: Int) = viewModelScope.launch {

        try {

            isShowLoadingMutableLiveData.value = true

            val detail = seriesTvRepository.getDetailSerieWithId(id)
            val images = seriesTvRepository.getImagesWithId(id)
            val seasons = seriesTvRepository.getSeasonsWithId(id)
            val episodes = seriesTvRepository.getEpisodesWithId(id)


            images.forEach { imageData ->
                if ("banner".equals(imageData.type) || "background".equals(imageData.type)) {
                    detail.banner = imageData.resolutions?.original?.url ?: ""
                }
            }

            val listCoverEpisodes = mutableListOf<EpisodesSerieData>()
            seasons.forEach { season ->
                val episodesSeason = episodes.filter { it.season == season.number }
                val firstEpisode = episodesSeason[0]
                firstEpisode.countEpisodes = episodesSeason.size
                listCoverEpisodes.add(firstEpisode)
            }

            detail.listaCoverEpisodes = listCoverEpisodes
            detailSerieMutableLiveData.value = Result.success(detail)

        } catch (e: Exception) {
            Log.e(TAG, e.localizedMessage)
            messageErrorMutableLiveData.value = R.string.msg_error_detail_serie
        }finally {
            isShowLoadingMutableLiveData.value = false
        }
    }

    fun getDetailEpisodesWithIdAndSeason(id: Int, numberSeason: Int) = viewModelScope.launch {
        try {
            isShowLoadingMutableLiveData.value = true
            val episodes = seriesTvRepository.getEpisodesWithId(id)
            val filterEpisodes = episodes.filter { it.season == numberSeason }
            episodesSerieMutableLiveData.value = Result.success(filterEpisodes)
        }catch (e : Exception){
            Log.e(TAG, e.localizedMessage)
            messageErrorMutableLiveData.value = R.string.msg_error_list_episodes
        }finally {
            isShowLoadingMutableLiveData.value = false
        }
    }




}