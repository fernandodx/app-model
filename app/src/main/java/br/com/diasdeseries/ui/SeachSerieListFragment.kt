package br.com.diasdeseries.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnScrollChangeListener
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.diasdeseries.MainActivity
import br.com.diasdeseries.R
import br.com.diasdeseries.databinding.SearchListSerieBinding
import br.com.diasdeseries.extensions.hideKeyboard
import br.com.diasdeseries.extensions.navigateWithAnimations
import br.com.diasdeseries.repository.SeriesTvRepository
import br.com.diasdeseries.repository.SeriesTvRepositoryImpl
import br.com.diasdeseries.ui.adapter.SearchSerieAdapter
import br.com.diasdeseries.viewmodel.SearchSerieListViewModel
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class SeachSerieListFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var dataBinding : SearchListSerieBinding

    private val  viewModel : SearchSerieListViewModel by viewModels<SearchSerieListViewModel> { viewModelFactory }

    val seachSerieAdapter : SearchSerieAdapter by lazy {
        SearchSerieAdapter { serieClick ->
            serieClick.show?.id?.let {
                val direct = SeachSerieListFragmentDirections.actionSeachSerieListFragmentToSerieDetailFragment(it)
                findNavController().navigateWithAnimations(direct)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (requireActivity() as MainActivity).mainComponent.inject(fragment = this)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(LayoutInflater.from(requireContext()), R.layout.search_list_serie, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listener()
        observers()
        init()

    }

    private fun init() {
        viewModel.findAllSeriesWithPage()
    }

    private fun observers() {
        viewModel.seriesLiveData.observe(viewLifecycleOwner) { result ->
            if(result.isSuccess){
                seachSerieAdapter.updateListSerieSearch(result.getOrDefault(emptyList()))
            }
        }

        viewModel.messageErrorLiveData.observe(viewLifecycleOwner) { codeMessage ->
            Snackbar.make(requireView(), getString(codeMessage), Snackbar.LENGTH_LONG).show()
        }

        viewModel.isShowLoadingLiveData.observe(viewLifecycleOwner) { isShowLoading ->
            showHideLoading(isShowLoading)
        }
    }

    private fun listener() {
        val gridLayout = GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
        dataBinding.listSerieRecyclerview.run {
            setHasFixedSize(true)
            layoutManager = gridLayout
            adapter = seachSerieAdapter
            addOnScrollListener(object : PaginationScrollListener(gridLayout){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                }

                override fun loadMoreItens() {
                  viewModel.findNextPage()
                }

                override fun isLoading(): Boolean {
                    val isShowLoading = viewModel.isShowLoadingLiveData.value
                    return isShowLoading?:false
                }

                override fun isSearchWithPagination(): Boolean {
                    return viewModel.isSearchWithPagination
                }

                override fun curretPage(): Int {
                    return viewModel.currentPage
                }
            })
        }

        dataBinding.searchSerieTextInput.setOnEditorActionListener(object : TextView.OnEditorActionListener{
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){

                    requireActivity().hideKeyboard()
                    viewModel.findSerieWithName(dataBinding.textInputLayout.editText?.text.toString())

                    return true
                }
                return false
            }
        })
    }

    abstract class PaginationScrollListener(
        private val gridLayoutManager: GridLayoutManager
    ) : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val visibleItemCount = gridLayoutManager.childCount
            val totalItemCount = gridLayoutManager.itemCount
            val firstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPosition()

            if(!isLoading() && isSearchWithPagination()){
                if((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                    loadMoreItens()
                }
            }

        }

        abstract fun loadMoreItens()
        abstract fun isLoading() : Boolean
        abstract fun isSearchWithPagination() : Boolean
        abstract fun curretPage() : Int

    }

    fun showHideLoading(isShowLoading : Boolean){
        dataBinding.viewLoading.visibility = if(isShowLoading) View.VISIBLE else View.GONE
    }

}


