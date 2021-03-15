package br.com.diasdeseries.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.diasdeseries.R
import br.com.diasdeseries.data.db.dao.FavoriteSeriesDAO
import br.com.diasdeseries.data.db.entity.AppDatabase
import br.com.diasdeseries.databinding.DashboardFragmentBinding
import br.com.diasdeseries.viewmodel.DashboardViewModel
import br.com.diasdeseries.viewmodel.FavoriteSeriesViewModel
import br.com.diasdeseries.repository.FavoriteSeriesRepository
import br.com.diasdeseries.repository.FavoriteSeriesRoomDataSource
import br.com.diasdeseries.repository.SeriesTvRepository
import br.com.diasdeseries.repository.SeriesTvRepositoryImpl
import br.com.diasdeseries.ui.adapter.SeriesAdapter
import com.google.android.material.snackbar.Snackbar

class DashboardFragment : Fragment() {

    private lateinit var dataBinding : DashboardFragmentBinding

    private val favoriteSeriesViewModel : FavoriteSeriesViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val favoriteSeriesDAO : FavoriteSeriesDAO = AppDatabase.getInstance(requireContext()).favoriteSeriesDAO
                val favoriteSeriesRepository : FavoriteSeriesRepository = FavoriteSeriesRoomDataSource(favoriteSeriesDAO)
                return FavoriteSeriesViewModel(favoriteSeriesRepository) as T
            }
        }
    }

    private val viewModel : DashboardViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val seriesTvRepository : SeriesTvRepository = SeriesTvRepositoryImpl()
                return DashboardViewModel(seriesTvRepository) as T
            }
        }
    }

    val seriesAdapter by lazy {
        SeriesAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dataBinding = DataBindingUtil.inflate(LayoutInflater.from(requireContext()), R.layout.dashboard_fragment, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        observers()
        listeners()

        viewModel.getSeriesNow()

    }



    private fun observers() {
        favoriteSeriesViewModel.favoriteSeriesLiveData.observe(viewLifecycleOwner) { result ->

            if(result.isSuccess){

            }

        }

        favoriteSeriesViewModel.messageErrorLiveData.observe(viewLifecycleOwner) { codeMessage ->
            Snackbar.make(requireView(), getString(codeMessage), Snackbar.LENGTH_LONG).show()
        }

        viewModel.listSeriesLiveData.observe(viewLifecycleOwner) { result ->
            if(result.isSuccess){
                val listSeries = result.getOrNull()
                listSeries?.let {
                    seriesAdapter.updateSeries(listSeries)
                }
            }
        }

        viewModel.messageErrorLiveData.observe(viewLifecycleOwner) { codeMessage ->
            Snackbar.make(requireView(), getString(codeMessage), Snackbar.LENGTH_LONG).show()
        }

    }

    private fun listeners() {

        dataBinding.listSeriesNowRecyclerview.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = seriesAdapter
        }

    }




}