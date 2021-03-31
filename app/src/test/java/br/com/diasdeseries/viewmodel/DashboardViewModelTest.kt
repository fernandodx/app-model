package br.com.diasdeseries.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import br.com.diasdeseries.R
import br.com.diasdeseries.data.pojo.*
import br.com.diasdeseries.repository.SeriesTvRepository
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class DashboardViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    val dispatcher = TestCoroutineDispatcher()

    private lateinit var viewModel: DashboardViewModel

    @Mock
    private lateinit var listSeriesObserver: Observer<Result<List<SerieNowData>>>

    @Mock
    private lateinit var messageErrorObserver : Observer<Int>

    @Mock
    private lateinit var isShowLoadingObserver : Observer<Boolean>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when get series now with sucess`() {

        val listSerieNowData = getListSerieNowData()


        viewModel = DashboardViewModel(MockSerieTvRepository())
        viewModel.getSeriesNow()

        viewModel.listSeriesLiveData.observeForever(listSeriesObserver)
        viewModel.messageErrorLiveData.observeForever(messageErrorObserver)
        viewModel.isShowLoadingLiveData.observeForever(isShowLoadingObserver)

        //Assert
        verify(listSeriesObserver).onChanged(Result.success(listSerieNowData))
    }

    @Test
    fun `when get series now with list empty`() {

        viewModel = DashboardViewModel(MockSerieTvErrorRepository())
        viewModel.getSeriesNow()

        viewModel.listSeriesLiveData.observeForever(listSeriesObserver)
        viewModel.messageErrorLiveData.observeForever(messageErrorObserver)
        viewModel.isShowLoadingLiveData.observeForever(isShowLoadingObserver)

        //Assert
//        verify(listSeriesObserver).onChanged(Result.failure(Throwable()))
        verify(messageErrorObserver).onChanged(R.string.msg_error_none_serie)

    }


    class MockSerieTvRepository : SeriesTvRepository {
        override suspend fun findSeriesWithName(nm: String): List<SerieData> {
            return getListSerieData()
        }

        override suspend fun getSeriesNow(): List<SerieNowData> {
           return getListSerieNowData()
        }

        override suspend fun getDetailSerieWithId(id: Int): DetailSerieData {
            return getDetailSerie(id)
        }

        override suspend fun getImagesWithId(id: Int): List<ImageSerieData> {
            return getListImages()
        }

        override suspend fun getEpisodesWithId(id: Int): List<EpisodesSerieData> {
            TODO("Not yet implemented")
        }

        override suspend fun getSeasonsWithId(id: Int): List<SeasonSerieData> {
            TODO("Not yet implemented")
        }

        override suspend fun findAllSeriesWithPagination(page: Int): List<SerieData.Show> {
            TODO("Not yet implemented")
        }
    }

    class MockSerieTvErrorRepository : SeriesTvRepository {
        override suspend fun findSeriesWithName(nm: String): List<SerieData> {
            return listOf()
        }

        override suspend fun getSeriesNow(): List<SerieNowData> {
            return mutableListOf()
        }

        override suspend fun getDetailSerieWithId(id: Int): DetailSerieData {
           return DetailSerieData()
        }

        override suspend fun getImagesWithId(id: Int): List<ImageSerieData> {
            return getListImages()
        }

        override suspend fun getEpisodesWithId(id: Int): List<EpisodesSerieData> {
            TODO("Not yet implemented")
        }

        override suspend fun getSeasonsWithId(id: Int): List<SeasonSerieData> {
            TODO("Not yet implemented")
        }

        override suspend fun findAllSeriesWithPagination(page: Int): List<SerieData.Show> {
            TODO("Not yet implemented")
        }
    }

    companion object {

        fun getListImages(): List<ImageSerieData> {
            val listImages = mutableListOf<ImageSerieData>()
            for (index in 1..3) {
                val image = ImageSerieData().apply {
                    id = index
                    type = "jpg"
                    resolutions = ImageSerieData.Resolutions().apply {
                        original = ImageSerieData.Original().apply {
                            url =
                                "https://lagesgardenshopping.com.br/wp-content/uploads/2019/08/capa-filme-velozes-e-furiosos-hobbs-e-shaw-e2cc4-large-189x300.jpg"
                        }
                    }
                }
                listImages.add(image)
            }
            return listImages
        }

        fun getListSerieData(): MutableList<SerieData> {
            val listSerieData = mutableListOf<SerieData>()
            for (index in 1..10) {
                val serieData = SerieData().apply {
                    this.show = createSerieDataShow(index)
                }
                listSerieData.add(serieData)
            }
            return listSerieData
        }

        fun getListSerieNowData(): MutableList<SerieNowData> {
            val listSerieData = mutableListOf<SerieNowData>()
            for (index in 1..10) {
                val serieData = createSerieNowDataShow(index)
                listSerieData.add(serieData)
            }
            return listSerieData
        }

        fun createSerieDataShow(index: Int): SerieData.Show {
            return SerieData.Show().apply {
                id = index
                name = "Name - $index"
                rating = SerieData.Rating().apply {
                    average = (2 * index).toDouble()
                }
                schedule = SerieData.Schedule().apply {
                    time = "$index:$index"
                    days = listOf("Moday", "Thuestday")
                }
                network = SerieData.Network().apply {
                    id = index
                    name = "Canal $index"
                    country = SerieData.Country().apply {
                        name = "País $index"
                    }
                }
                image = SerieData.Image().apply {
                    original =
                        "http://salacritica.com.br/wp-content/uploads/2020/12/soul-poster-sala-critica-215x300.jpg"
                    medium =
                        "http://salacritica.com.br/wp-content/uploads/2020/06/soul-pixar-sala-cr%C3%ADtica.jpg"
                }
            }
        }

        fun createSerieNowDataShow(index: Int): SerieNowData {
            return SerieNowData().apply {
                id = index
                name = "Name - $index"
                image = SerieData.Image().apply {
                    original =
                        "http://salacritica.com.br/wp-content/uploads/2020/12/soul-poster-sala-critica-215x300.jpg"
                    medium =
                        "http://salacritica.com.br/wp-content/uploads/2020/06/soul-pixar-sala-cr%C3%ADtica.jpg"
                }
                show = createSerieDataShow(index)
            }
        }

        fun getDetailSerie(index: Int): DetailSerieData {
            return DetailSerieData().apply {
                id = index
                url = "http://www.google.com.br"
                name = "Name - $index"
                genres = listOf("terror", "adventure")
                officialSite = "http://facebook.com.br"
                network = SerieData.Network().apply {
                    id = index
                    name = "Canal $index"
                    country = SerieData.Country().apply {
                        name = "País $index"
                    }
                }
                image = SerieData.Image().apply {
                    original =
                        "http://salacritica.com.br/wp-content/uploads/2020/12/soul-poster-sala-critica-215x300.jpg"
                    medium =
                        "http://salacritica.com.br/wp-content/uploads/2020/06/soul-pixar-sala-cr%C3%ADtica.jpg"
                }
                rating = SerieData.Rating().apply {
                    average = (2 * index).toDouble()
                }
            }
        }
    }


}