package br.com.diasdeseries.ui

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.diasdeseries.MainActivity
import br.com.diasdeseries.R
import br.com.diasdeseries.databinding.DashboardFragmentBinding
import br.com.diasdeseries.extensions.navigateWithAnimations
import br.com.diasdeseries.ui.adapter.FavoriteSerieAdapter
import br.com.diasdeseries.ui.adapter.SeriesAdapter
import br.com.diasdeseries.viewmodel.DashboardViewModel
import br.com.diasdeseries.viewmodel.FavoriteSeriesViewModel
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class DashboardFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var dataBinding : DashboardFragmentBinding

    private val viewModel: DashboardViewModel by viewModels<DashboardViewModel> { viewModelFactory }
    private val favoriteSeriesViewModel: FavoriteSeriesViewModel by viewModels<FavoriteSeriesViewModel> { viewModelFactory }

    val seriesAdapter by lazy {
        SeriesAdapter { serieNowClick ->
            serieNowClick.show?.id?.let {
                detailSerie(it)
            }
        }
    }

    val favoriteSerieAdapter by lazy {
        FavoriteSerieAdapter { favoriteSeriesEntity ->
            favoriteSeriesEntity.idSerieTv?.let {
                detailSerie(it)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (requireActivity() as MainActivity).mainComponent.inject(fragment = this)
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
        init()
    }

    private fun init() {
        setHasOptionsMenu(true)
        viewModel.getSeriesNow()
        favoriteSeriesViewModel.getAllFavoriteSeries()
    }


    private fun observers() {

        viewModel.listSeriesLiveData.observe(viewLifecycleOwner) { result ->
            if(result.isSuccess){
                val listSeries = result.getOrNull()
                listSeries?.let {
                    seriesAdapter.updateSeries(listSeries)
                }
            }
        }

        viewModel.messageErrorLiveData.observe(viewLifecycleOwner) { codeMessage ->
            showMessage(codeMessage)
        }

        favoriteSeriesViewModel.favoriteSerieLiveData.observe(viewLifecycleOwner) { result ->
            if(result.isSuccess){
                val listFavorite = result.getOrNull()
                dataBinding.viewNotFoundMySerie.visibility = if(listFavorite.isNullOrEmpty())  View.VISIBLE else View.GONE
                favoriteSerieAdapter.updateListFavorite(result.getOrNull()?: emptyList())
            }else{
                dataBinding.viewNotFoundMySerie.visibility = View.VISIBLE
            }
        }

        favoriteSeriesViewModel.messageErrorLiveData.observe(viewLifecycleOwner) { codeMessage ->
           showMessage(codeMessage)
        }

        viewModel.isShowLoadingLiveData.observe(viewLifecycleOwner) { isShowLoading ->
            showHideLoading(isShowLoading)
        }

    }

    private fun listeners() {

        dataBinding.listSeriesNowRecyclerview.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = seriesAdapter
        }

        dataBinding.listFavoriteSeriesRecyclerview.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = favoriteSerieAdapter
        }

    }

    private fun showMessage(codeMessage : Int) {
        Snackbar.make(requireView(), getString(codeMessage), Snackbar.LENGTH_LONG).show()
    }

    private fun detailSerie(id : Int) {
        val direct = DashboardFragmentDirections.actionDashboardFragmentToSerieDetailFragment(id)
        findNavController().navigateWithAnimations(direct)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_dashboard_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.search -> {
               val direct = DashboardFragmentDirections.actionDashboardFragmentToSeachSerieListFragment()
                findNavController().navigateWithAnimations(direct)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun showHideLoading(isShowLoading : Boolean){
        dataBinding.viewLoading.visibility = if(isShowLoading) View.VISIBLE else View.GONE
    }




}