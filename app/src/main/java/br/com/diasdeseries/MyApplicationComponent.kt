package br.com.diasdeseries

import android.content.Context
import br.com.diasdeseries.data.RoomModule
import br.com.diasdeseries.repository.DataModule
import br.com.diasdeseries.ui.MainComponent
import br.com.diasdeseries.viewmodel.DiasdeSeriesViewModelFactory
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class, DiasdeSeriesViewModelFactory.ViewModelBuilderModule::class, SubcomponentsModule::class, RoomModule::class])
interface MyApplicationComponent {


    @Component.Factory
    interface Factory {
        fun create(@BindsInstance myContext : Context) : MyApplicationComponent
    }

    fun mainComponent(): MainComponent.Factory

}

@Module(subcomponents = [MainComponent::class])
object SubcomponentsModule