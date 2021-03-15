package br.com.diasdeseries.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.diasdeseries.R
import br.com.diasdeseries.repository.FavoriteSeriesRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class FavoriteSeriesViewModel(
    private val repository: FavoriteSeriesRepository
) : ViewModel() {

    companion object {
        private val TAG = FavoriteSeriesViewModel.javaClass.simpleName
    }

    private val favoriteSeriesMutableLiveData = MutableLiveData<Result<Long?>>()
    val favoriteSeriesLiveData : LiveData<Result<Long?>>
        get() = favoriteSeriesMutableLiveData

    private  val messageErrorMutableLiveData = MutableLiveData<Int>()
    val messageErrorLiveData: LiveData<Int>
        get() = messageErrorMutableLiveData


    fun addFavoriteSerie(nameSerie : String) = viewModelScope.launch {
        try {

            val id = repository.insertFavoriteSeries(nameSerie)
            favoriteSeriesMutableLiveData.value = Result.success(id)
        }catch (e: Exception){
            Log.e(TAG, e.toString())
            favoriteSeriesMutableLiveData.value = Result.failure(e)
            messageErrorMutableLiveData.value = R.string.msg_error_insert_favorite_serie
        }
    }


}