package br.com.diasdeseries.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.diasdeseries.R
import br.com.diasdeseries.data.db.entity.FavoriteSeriesEntity
import br.com.diasdeseries.repository.FavoriteSeriesRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class FavoriteSeriesViewModel(
    private val repository: FavoriteSeriesRepository
) : ViewModel() {

    companion object {
        private val TAG = FavoriteSeriesViewModel.javaClass.simpleName
    }

    private  val messageErrorMutableLiveData = MutableLiveData<Int>()
    val messageErrorLiveData: LiveData<Int>
        get() = messageErrorMutableLiveData

    private val favoriteSerieMutableLiveData = MutableLiveData<Result<List<FavoriteSeriesEntity>>>()
    val favoriteSerieLiveData: LiveData<Result<List<FavoriteSeriesEntity>>>
        get() = favoriteSerieMutableLiveData

    private val isfavoriteSerieMutableLiveData = MutableLiveData<Boolean>()
    val isfavoriteSerieLiveData: LiveData<Boolean>
        get() = isfavoriteSerieMutableLiveData


    fun addOrRemoveFavoriteSerie(
        idSerieTv: Int?,
        nameSerie: String?,
        banner: String?,
        thumb: String?,
        rating: Double?,
        countSeason: Int?
    ) = viewModelScope.launch {
        if(isfavoriteSerieLiveData.value?:false){
            removeFavoriteSerie(idSerieTv)
        }else{
            addFavoriteSerie(idSerieTv, nameSerie, banner, thumb, rating, countSeason)
        }
    }

    fun getAllFavoriteSeries() = viewModelScope.launch {
        try {
            val listSeriesFavorite = repository.getAllFavoriteSeries()
            favoriteSerieMutableLiveData.value = Result.success(listSeriesFavorite.orEmpty())
        }catch (e: Exception){
            Log.e(TAG, e.localizedMessage)
            messageErrorMutableLiveData.value = R.string.msg_error_find_series_favorite
        }
    }

    fun checkIsFavoriteSerie(idSerieTv: Int?) = viewModelScope.launch {
        idSerieTv?.let {
            val favoriteSerie = repository.getFavoriteSerieWithIdSerieTv(it)
            isfavoriteSerieMutableLiveData.value = favoriteSerie != null
        }
    }


    private fun addFavoriteSerie(
        idSerieTv: Int?,
        nameSerie: String?,
        banner: String?,
        thumb: String?,
        rating: Double?,
        countSeason: Int?
    ) = viewModelScope.launch {

        try {
            val id = repository.insertFavoriteSeries(
                idSerieTv,
                nameSerie,
                banner,
                thumb,
                rating,
                countSeason
            )

            if(id > 0){
                val listFavoriteSeries = repository.getAllFavoriteSeries().orEmpty()
                favoriteSerieMutableLiveData.value = Result.success(listFavoriteSeries)
                isfavoriteSerieMutableLiveData.value = true
            }

        } catch (e: Exception) {
            Log.e(TAG, e.localizedMessage)
            messageErrorMutableLiveData.value = R.string.msg_error_favorite_save_serie
        }
    }

    private fun removeFavoriteSerie(idSerieTv: Int?) = viewModelScope.launch {

        try {

            idSerieTv?.let {
                repository.deleteFavoriteSeriesWithIdSerieTv(it)
                isfavoriteSerieMutableLiveData.value = false
            }

        } catch (e: Exception) {
            Log.e(TAG, e.localizedMessage)
            messageErrorMutableLiveData.value = R.string.msg_error_favorite_remove_serie
        }
    }


}