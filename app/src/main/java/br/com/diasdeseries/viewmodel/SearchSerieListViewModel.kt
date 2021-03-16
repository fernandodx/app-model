package br.com.diasdeseries.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.diasdeseries.R
import br.com.diasdeseries.data.pojo.SerieData
import br.com.diasdeseries.repository.SeriesTvRepository
import kotlinx.coroutines.launch

class SearchSerieListViewModel(
    private val repository : SeriesTvRepository
) : ViewModel() {

    companion object {
        private val TAG = FavoriteSeriesViewModel.javaClass.simpleName
    }

    private val messageErrorMutableLiveData = MutableLiveData<Int>()
    val messageErrorLiveData : LiveData<Int>
        get() = messageErrorMutableLiveData

    private val seriesMutableLiveData = MutableLiveData<Result<List<SerieData>>>()
    val seriesLiveData : LiveData<Result<List<SerieData>>>
        get() = seriesMutableLiveData

    private val isShowLoadingMutableLiveData = MutableLiveData<Boolean>()
    val isShowLoadingLiveData : LiveData<Boolean>
        get() = isShowLoadingMutableLiveData

    var isSearchWithPagination = false
    var currentPage = 0
    val listSerie = mutableListOf<SerieData>()


    fun findSerieWithName(name: String) = viewModelScope.launch {
        try {
            isSearchWithPagination = false
            listSerie.clear()
            isShowLoadingMutableLiveData.value = true
            val listSerie = repository.findSeriesWithName(name)
            if(listSerie.isNullOrEmpty()){
                messageErrorMutableLiveData.value = R.string.msg_error_find_series_not_found
            }
            seriesMutableLiveData.value = Result.success(listSerie)
        }catch (e: Exception){
            Log.e(TAG, e.localizedMessage)
            messageErrorMutableLiveData.value = R.string.msg_error_find_series
        }finally {
            isShowLoadingMutableLiveData.value = false
        }
    }

    fun findNextPage(){
        currentPage = currentPage.plus(1)
        findAllSeriesWithPage(currentPage)
    }

    fun findAllSeriesWithPage(page : Int = 0) = viewModelScope.launch {
        try {
            isSearchWithPagination = true
            isShowLoadingMutableLiveData.value = true
            val listShow = repository.findAllSeriesWithPagination(page)
            if(listShow.isNullOrEmpty()){
                messageErrorMutableLiveData.value = R.string.msg_error_find_series_not_found
            }

            //Caso queira testar a paginação
//            listShow.subList(0, 30)
            listShow.forEach {
                val serieData = SerieData()
                serieData.show = it
                listSerie.add(serieData)
            }

            seriesMutableLiveData.value = Result.success(listSerie)
        }catch (e: Exception){
            Log.e(TAG, e.localizedMessage)
            messageErrorMutableLiveData.value = R.string.msg_error_find_series
        } finally {
            isShowLoadingMutableLiveData.value = false
        }
    }




}