package br.com.diasdeseries.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.diasdeseries.MainActivity
import br.com.diasdeseries.R
import br.com.diasdeseries.data.db.dao.FavoriteSeriesDAO
import br.com.diasdeseries.data.db.entity.AppDatabase
import br.com.diasdeseries.databinding.DetailSerieFragmentBinding
import br.com.diasdeseries.extensions.navigateWithAnimations
import br.com.diasdeseries.repository.FavoriteSeriesRepository
import br.com.diasdeseries.repository.FavoriteSeriesRoomDataSource
import br.com.diasdeseries.repository.SeriesTvRepositoryImpl
import br.com.diasdeseries.ui.adapter.SeasonAdapter
import br.com.diasdeseries.viewmodel.DetailSerieViewModel
import br.com.diasdeseries.viewmodel.FavoriteSeriesViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import javax.inject.Inject


class DetailSerieFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var dataBinding : DetailSerieFragmentBinding

//    private val viewModel : DetailSerieViewModel by viewModels {
//        object : ViewModelProvider.Factory {
//            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//                val seriesTvRepository = SeriesTvRepositoryImpl()
//                return DetailSerieViewModel(seriesTvRepository) as T
//            }
//        }
//    }

    private val viewModel : DetailSerieViewModel by viewModels<DetailSerieViewModel> { viewModelFactory }
    private val favoriteSeriesViewModel: FavoriteSeriesViewModel by viewModels<FavoriteSeriesViewModel> { viewModelFactory }

//    private val favoriteSeriesViewModel : FavoriteSeriesViewModel by viewModels {
//        object : ViewModelProvider.Factory {
//            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//                val favoriteSeriesDAO : FavoriteSeriesDAO = AppDatabase.getInstance(requireContext()).favoriteSeriesDAO
//                val favoriteSeriesRepository : FavoriteSeriesRepository = FavoriteSeriesRoomDataSource(
//                    favoriteSeriesDAO
//                )
//                return FavoriteSeriesViewModel(favoriteSeriesRepository) as T
//            }
//        }
//    }

    private val args: DetailSerieFragmentArgs by navArgs()

    private val seasonAdapter by lazy {
        SeasonAdapter { episodeClick ->

            val detail = viewModel.detailSerieLiveData.value?.getOrNull()

            detail?.id?.let {
                val direct = DetailSerieFragmentDirections.actionSerieDetailFragmentToListEpisodesFragment(
                    it,
                    episodeClick.season ?: 1
                )
                findNavController().navigateWithAnimations(direct)
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

        dataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.detail_serie_fragment,
            container,
            false
        )
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observers()
        listeners()
        init()
    }

    private fun init() {
        viewModel.getDetailSerieWithId(args.idSerieTv)
        favoriteSeriesViewModel.checkIsFavoriteSerie(args.idSerieTv)
    }

    override fun onResume() {
        super.onResume()

    }

    private fun listeners() {

        dataBinding.favoriteButtonChip.setOnClickListener {

            val detail = viewModel.detailSerieLiveData.value?.getOrNull()

            detail?.let {
                favoriteSeriesViewModel.addOrRemoveFavoriteSerie(
                    idSerieTv = detail.id,
                    nameSerie = detail.name,
                    banner = detail.banner,
                    thumb = detail.image?.original,
                    rating = detail.rating?.average,
                    countSeason = detail.listaCoverEpisodes?.size
                )
            }
        }


        dataBinding.listSeasonsRecyclerview.run {
            this.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            this.setHasFixedSize(true)
            this.adapter = seasonAdapter
        }
    }

    private fun observers() {
        viewModel.detailSerieLiveData.observe(viewLifecycleOwner){ result ->
            if(result.isSuccess){
                val detailSerie  = result.getOrNull()
                detailSerie?.let { detail ->
                    dataBinding.run {

                        Picasso.get()
                            .load(Uri.parse(detail.banner))
                            .error(R.drawable.ic_launcher_foreground)
                            .into(this.bannerImageview)

                        this.nameSerieTextview.text = detail.name

                        detail.genres?.forEach { genre ->
                            val chip = Chip(requireContext())
                            chip.text = genre
                            chip.setTextAppearanceResource(R.style.ChipTextStyle)
                            chip.chipBackgroundColor = ContextCompat.getColorStateList(
                                requireContext(),
                                R.color.design_default_color_primary
                            )
                            this.chipGroupGenre.addView(chip)
                        }

                        this.sumaryTextview.text = HtmlCompat.fromHtml(
                            detail.summary ?: "",
                            HtmlCompat.FROM_HTML_MODE_COMPACT
                        )
                        this.siteImageview.setOnClickListener {
                            val site = if(detail.officialSite != null) detail.officialSite else detail.url
                            val browserIntent =
                                Intent(Intent.ACTION_VIEW, Uri.parse(site))
                            startActivity(browserIntent)
                        }
                        this.avarageTextview.text = "${detail.rating?.average?: "-"}"

                        Picasso.get()
                            .load(
                                Uri.parse(
                                    getString(
                                        R.string.url_get_flag,
                                        detail.network?.country?.code
                                    )
                                )
                            )
                            .into(this.flagLocationImageview)

                        seasonAdapter.updateListEpisodesSeason(
                            detail.listaCoverEpisodes ?: emptyList()
                        )

                    }
                }

            }
        }

        viewModel.messageErrorLiveData.observe(viewLifecycleOwner) { codeMessage ->
            Snackbar.make(requireView(), getString(codeMessage), Snackbar.LENGTH_LONG).show()
        }

       favoriteSeriesViewModel.isfavoriteSerieLiveData.observe(viewLifecycleOwner) { isFavorite ->
           if(isFavorite){
               dataBinding.favoriteButtonChip.text = getString(R.string.label_remove_favorite)
               dataBinding.favoriteButtonChip.chipIcon = ContextCompat.getDrawable(
                   requireContext(),
                   R.drawable.ic_remove
               )
           }else{
               dataBinding.favoriteButtonChip.text = getString(R.string.label_add_favorite)
               dataBinding.favoriteButtonChip.chipIcon = ContextCompat.getDrawable(
                   requireContext(),
                   R.drawable.ic_favorite
               )
           }
       }

        favoriteSeriesViewModel.messageErrorLiveData.observe(viewLifecycleOwner) { codeMessage ->
            Snackbar.make(requireView(), getString(codeMessage), Snackbar.LENGTH_LONG).show()
        }

        viewModel.isShowLoadingLiveData.observe(viewLifecycleOwner) { isShowLoading ->
            showHideLoading(isShowLoading)
        }
    }

    fun showHideLoading(isShowLoading: Boolean){
        dataBinding.viewLoading.visibility = if(isShowLoading) View.VISIBLE else View.GONE
    }


}