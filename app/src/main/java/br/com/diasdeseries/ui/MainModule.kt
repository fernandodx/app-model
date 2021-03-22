package br.com.diasdeseries.ui

import androidx.lifecycle.ViewModel
import br.com.diasdeseries.viewmodel.*
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface MainModule {

    @Binds
    @IntoMap
    @DiasdeSeriesViewModelFactory.ViewModelKey(DashboardViewModel::class)
    fun bindDashboardViewModel(viewModel: DashboardViewModel) : ViewModel

    @Binds
    @IntoMap
    @DiasdeSeriesViewModelFactory.ViewModelKey(DetailSerieViewModel::class)
    fun bindDetailSerieViewModel(viewModel: DetailSerieViewModel) : ViewModel

    @Binds
    @IntoMap
    @DiasdeSeriesViewModelFactory.ViewModelKey(FavoriteSeriesViewModel::class)
    fun bindFavoriteSerieViewModel(viewModel: FavoriteSeriesViewModel) : ViewModel

    @Binds
    @IntoMap
    @DiasdeSeriesViewModelFactory.ViewModelKey(SearchSerieListViewModel::class)
    fun bindSearchSerieListViewModel(viewModel: SearchSerieListViewModel) : ViewModel


}