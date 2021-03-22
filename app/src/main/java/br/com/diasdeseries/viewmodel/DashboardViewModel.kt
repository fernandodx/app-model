package br.com.diasdeseries.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.diasdeseries.R
import br.com.diasdeseries.data.pojo.SerieData
import br.com.diasdeseries.data.pojo.SerieNowData
import br.com.diasdeseries.repository.SeriesTvRepository
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class DashboardViewModel @Inject constructor(
    private val seriesTvRepository: SeriesTvRepository
) : ViewModel() {

    companion object {
        private val TAG = FavoriteSeriesViewModel.javaClass.simpleName
    }

    private val listSeriesMutableLiveData = MutableLiveData<Result<List<SerieNowData>>>()
    val listSeriesLiveData : LiveData<Result<List<SerieNowData>>>
        get() = listSeriesMutableLiveData

    private val messageErrorMutableLiveData = MutableLiveData<Int>()
    val messageErrorLiveData : LiveData<Int>
        get() = messageErrorMutableLiveData

    private val isShowLoadingMutableLiveData = MutableLiveData<Boolean>()
    val isShowLoadingLiveData : LiveData<Boolean>
        get() = isShowLoadingMutableLiveData


    fun getSeriesNow() = viewModelScope.launch {

        try {
            isShowLoadingMutableLiveData.value = true
            val series = seriesTvRepository.getSeriesNow()

            if(series.isNullOrEmpty()){
                listSeriesMutableLiveData.value = Result.failure(Throwable())
                messageErrorMutableLiveData.value = R.string.msg_error_none_serie
            }else{
                listSeriesMutableLiveData.value = Result.success(series)
            }

        }catch (e : Exception){
            Log.e(TAG, e.localizedMessage)
            messageErrorMutableLiveData.value = R.string.msg_error_find_series
        }finally {
            isShowLoadingMutableLiveData.value = false
        }
    }


}