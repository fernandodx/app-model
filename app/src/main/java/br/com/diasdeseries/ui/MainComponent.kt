package br.com.diasdeseries.ui

import br.com.diasdeseries.MainActivity
import dagger.Subcomponent

@Subcomponent(modules = [MainModule::class])
interface MainComponent {

    @Subcomponent.Factory
    interface  Factory {
        fun create(): MainComponent
    }

    fun inject(activity: MainActivity)
    fun inject(fragment: DashboardFragment)
    fun inject(fragment: DetailSerieFragment)
    fun inject(fragment: ListEpisodesFragment)
    fun inject(fragment: SeachSerieListFragment)

}