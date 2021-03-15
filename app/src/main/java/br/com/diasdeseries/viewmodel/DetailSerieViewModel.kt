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
    private val favoriteSeriesRepository: FavoriteSeriesRepository
) : ViewModel() {

    companion object {
        private val TAG = FavoriteSeriesViewModel.javaClass.simpleName
    }

    private val detailSerieMutableLiveData = MutableLiveData<Result<DetailSerieData>>()
    val detailSerieLiveData: LiveData<Result<DetailSerieData>>
        get() = detailSerieMutableLiveData

    private val favoriteSerieMutableLiveData = MutableLiveData<Result<List<FavoriteSeriesEntity>>>()
    val favoriteSerieLiveData: LiveData<Result<List<FavoriteSeriesEntity>>>
        get() = favoriteSerieMutableLiveData

    private val messageErrorMutableLiveData = MutableLiveData<Int>()
    val messageErrorLiveData: LiveData<Int>
        get() = messageErrorMutableLiveData

    fun getDetailSerieWithId(id: Int) = viewModelScope.launch {

        try {

            val detail = seriesTvRepository.getDetailSerieWithId(id)
            val images = seriesTvRepository.getImagesWithId(id)
            val seasons = seriesTvRepository.getSeasonsWithId(id)
            val episodes = seriesTvRepository.getEpisodesWithId(id)

            //MELHORAR
            images.forEach { imageData ->
                if ("banner".equals(imageData.type) || "background".equals(imageData.type)) {
                    detail.banner = imageData.resolutions?.original?.url ?: ""
                }
            }

            val listCoverEpisodes = mutableListOf<EpisodesSerieData>()
            seasons.forEach { season ->
                episodes.forEach { episode ->
                    if (episode.season == season.number && episode.number == 1) {
                        episode.countEpisodes = season.episodeOrder
                        listCoverEpisodes.add(episode)
                    }
                }
            }

            detail.listaCoverEpisodes = listCoverEpisodes

            detailSerieMutableLiveData.value = Result.success(detail)

        } catch (e: Exception) {
            Log.e(TAG, e.localizedMessage)
            messageErrorMutableLiveData.value = R.string.msg_error_detail_serie

        }
    }

    fun addFavoriteSerie(
        idSerieTv: Int,
        nameSerie: String,
        banner: String,
        thumb: String,
        rating: Double,
        countSeason: Int
    ) = viewModelScope.launch {

        try {
            val id = favoriteSeriesRepository.insertFavoriteSeries(
                idSerieTv,
                nameSerie,
                banner,
                thumb,
                rating,
                countSeason
            )

            if(id > 0){
                val listFavoriteSeries = favoriteSeriesRepository.getAllFavoriteSeries()
                favoriteSerieMutableLiveData.value = Result.success(listFavoriteSeries)
            }

        } catch (e: Exception) {
            Log.e(TAG, e.localizedMessage)
            messageErrorMutableLiveData.value = R.string.msg_error_favorite_save_serie
        }

    }


}