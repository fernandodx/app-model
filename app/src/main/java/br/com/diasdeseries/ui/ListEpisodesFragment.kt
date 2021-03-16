package br.com.diasdeseries.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.diasdeseries.R
import br.com.diasdeseries.databinding.DetailListEpisodesSeasonBinding
import br.com.diasdeseries.repository.SeriesTvRepositoryImpl
import br.com.diasdeseries.ui.adapter.EpisodeAdapter
import br.com.diasdeseries.viewmodel.DetailSerieViewModel
import com.google.android.material.snackbar.Snackbar

class ListEpisodesFragment : Fragment() {

    private lateinit var dataBinding : DetailListEpisodesSeasonBinding

    private val viewModel : DetailSerieViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val seriesTvRepository = SeriesTvRepositoryImpl()
                return DetailSerieViewModel(seriesTvRepository) as T
            }
        }
    }

    val episodeAdapter by lazy {
        EpisodeAdapter()
    }

    val args : ListEpisodesFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dataBinding = DataBindingUtil.inflate(LayoutInflater.from(requireContext()), R.layout.detail_list_episodes_season, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listeners()
        observers()
        init()
    }

    private fun init() {
        viewModel.getDetailEpisodesWithIdAndSeason(args.idSerieTv, args.numberSeason)
    }

    private fun observers() {

        viewModel.episodeSerieLiveData.observe(viewLifecycleOwner) { result ->
            if(result.isSuccess){
                val listEpisodes = result.getOrDefault(emptyList())
                episodeAdapter.updateListEpisodes(listEpisodes)
            }
        }

        viewModel.messageErrorLiveData.observe(viewLifecycleOwner) { codeMessage ->
            Snackbar.make(requireView(), getString(codeMessage), Snackbar.LENGTH_LONG).show()
        }

        viewModel.isShowLoadingLiveData.observe(viewLifecycleOwner){ isShowLoading ->
            showHideLoading(isShowLoading)
        }

    }

    private fun listeners() {

        dataBinding.listEpisodesRecyclerview.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = episodeAdapter
        }

    }

    fun showHideLoading(isShowLoading : Boolean){
        dataBinding.viewLoading.visibility = if(isShowLoading) View.VISIBLE else View.GONE
    }


}