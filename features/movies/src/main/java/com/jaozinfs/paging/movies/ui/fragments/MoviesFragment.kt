package com.jaozinfs.paging.movies.ui.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.airbnb.lottie.LottieDrawable
import com.google.android.material.transition.platform.MaterialElevationScale
import com.jaozinfs.paging.movies.R
import com.jaozinfs.paging.movies.di.moviesModules
import com.jaozinfs.paging.movies.ui.MoviesViewModel
import com.jaozinfs.paging.movies.ui.adapter.MoviesAdapter
import com.jaozinfs.paging.movies.ui.adapter.ReposLoadStateAdapter
import kotlinx.android.synthetic.main.fragment_movies.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.dsl.koinApplication


class MoviesFragment : Fragment(R.layout.fragment_movies) {

    private lateinit var searchView: SearchView
    private val viewModel: MoviesViewModel by sharedViewModel()
    private var searchJob: Job? = null
    private val adapter = MoviesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        koinApplication {
            unloadKoinModules(moviesModules)
            loadKoinModules(moviesModules)
        }

        setHasOptionsMenu(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            exitTransition = MaterialElevationScale(/* growing= */ false)
            reenterTransition = MaterialElevationScale(/* growing= */ true)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapterClickListener()
        setSwipeRefreshListener()
        initList()
        observeFilter()
    }

    //Set listener of swipe refresh layout to refresh list of movies
    private fun setSwipeRefreshListener() {
        swipe_refresh_layout.setOnRefreshListener {
            resetFilter()
            getMovies()
        }
    }

    private fun initList() {
        movies_rv.layoutManager = GridLayoutManager(context, 2)
        //Add Loading state on middle of view
        adapter.addLoadStateListener { loadState ->
            swipe_refresh_layout?.isRefreshing = loadState.source.refresh is LoadState.Loading
            if (loadState.source.refresh is LoadState.Error)
                setNoConnectionNetworkError()
        }
        //empty state
        adapter.addDataRefreshListener {
            Log.d("Teste", it.toString())
            movies_empty_animation.isVisible = it
        }
        //set adapter on recycler view and set adapter in header and footer
        movies_rv.adapter = adapter.withLoadStateHeaderAndFooter(
            header = ReposLoadStateAdapter { adapter.retry() },
            footer = ReposLoadStateAdapter { adapter.retry() }
        )
    }

    private fun setNoConnectionNetworkError() {
        with(movies_empty_animation) {
            setAnimation("not-network.json")
            isVisible = true
            repeatCount = LottieDrawable.INFINITE
        }
    }

    //Click lister of adapter movies
    private fun setAdapterClickListener() {
        adapter.setMovieClickListener { _, movieEntity, bannerImageView, ratingView ->
            val extras = FragmentNavigatorExtras(
                bannerImageView to MovieDetailFragment.BANNER_ENTER_TRANSITION_NAME,
                ratingView to MovieDetailFragment.RATING_ENTER_TRANSITION_NAME
            )
            val direction =
                MoviesFragmentDirections.actionNavMoviesToNavMoviesDetail(movieEntity.id)

            findNavController().navigate(
                direction,
                extras
            )
        }
    }

    //persist list of movies on viewmodel
    private fun getMovies(
        voteAvarage: String? = null,
        nameFilter: String? = null,
        isAdult: Boolean? = null,
        genres: List<Int>? = null
    ) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.getMovies(voteAvarage, nameFilter, isAdult, genres).collectLatest {
                adapter.submitData(it)
            }
        }
    }

    //inflate menu on fragment
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // TODO Add your menu entries here
        inflater.inflate(R.menu.movies_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        (searchItem.actionView as? SearchView)?.let {
            searchView = it
            searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
                override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                    return true
                }

                override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                    getMovies()
                    return true
                }
            })
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    getMovies(nameFilter = query)
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
//                    newText?.let {
//                        if (it.isEmpty())
//                            getMovies()
//                    }
                    return false
                }
            })
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    //listener menu item click
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.filter ->
                findNavController().navigate(
                    R.id.nav_movies_filter
                )
        }
        return true
    }

    //Observe when fragment backstack returns of FilterFragment
    //if have value, persiste list with filter object
    private fun observeFilter() {
        if (findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<MoviesFilterFragment.FilterObject>(
                MoviesFilterFragment.FILTER_OBJECT
            )?.value == null
        ) {
            getMovies()
            return
        }
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<MoviesFilterFragment.FilterObject>(
            MoviesFilterFragment.FILTER_OBJECT
        )?.observe(viewLifecycleOwner, Observer {
            it?.let {
                getMovies(it.voteAvarege, isAdult = it.isAdult, genres = it.genres)
            }
        })

    }


    private fun resetFilter() {
        findNavController().currentBackStackEntry?.savedStateHandle?.set(
            MoviesFilterFragment.FILTER_OBJECT,
            null
        )
    }

}
