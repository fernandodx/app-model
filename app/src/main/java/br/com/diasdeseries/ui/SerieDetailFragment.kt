package br.com.diasdeseries.ui

import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.diasdeseries.R
import br.com.diasdeseries.databinding.DetailSerieFragmentBinding
import br.com.diasdeseries.repository.SeriesTvRepository
import br.com.diasdeseries.repository.SeriesTvRepositoryImpl
import br.com.diasdeseries.ui.adapter.SeasonAdapter
import br.com.diasdeseries.viewmodel.DetailSerieViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.resources.TextAppearance
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso

class SerieDetailFragment : Fragment() {

    private lateinit var dataBinding : DetailSerieFragmentBinding

    private val viewModel : DetailSerieViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val seriesTvRepository = SeriesTvRepositoryImpl()
                return DetailSerieViewModel(seriesTvRepository) as T
            }
        }
    }

    private val seasonAdapter by lazy {
        SeasonAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dataBinding = DataBindingUtil.inflate(LayoutInflater.from(requireContext()), R.layout.detail_serie_fragment, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observers()

        viewModel.getDetailSerieWithId(5881)

        dataBinding.listSeasonsRecyclerview.run {
            this.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
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

                        //Buscar banner do enpoint das imagens
                        Picasso.get()
                            .load(Uri.parse(detail.banner))
                            .error(R.drawable.ic_launcher_foreground)
                            .into(this.bannerImageview)

                        this.nameSerieTextview.text = detail.name

                        detail.genres?.forEach { genre ->
                            val chip = Chip(requireContext())
                            chip.text = genre
                            chip.setTextAppearanceResource(R.style.ChipTextStyle)
                            chip.chipBackgroundColor = ContextCompat.getColorStateList(requireContext(), R.color.design_default_color_primary)

                            this.chipGroupGenre.addView(chip)
                        }

                        this.sumaryTextview.text = HtmlCompat.fromHtml(detail.summary?:"", HtmlCompat.FROM_HTML_MODE_COMPACT)
                        this.siteImageview.setOnClickListener {
                            val site = if(detail.officialSite != null) detail.officialSite else detail.url
                            Toast.makeText(requireContext(), site, Toast.LENGTH_LONG).show()
                        }
                        this.avarageTextview.text = detail.rating?.average.toString()

                        Picasso.get()
                            .load(Uri.parse("https://www.countryflags.io/${detail.network?.country?.code}/flat/64.png"))
                            .into(this.flagLocationImageview)

                        seasonAdapter.updateListEpisodesSeason(detail.listaCoverEpisodes?: emptyList())

                    }
                }

            }
        }

        viewModel.messageErrorLiveData.observe(viewLifecycleOwner) { codeMessage ->
            Snackbar.make(requireView(), getString(codeMessage), Snackbar.LENGTH_LONG).show()
        }
    }


}