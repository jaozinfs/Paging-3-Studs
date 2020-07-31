package com.jaozinfs.paging.movies.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import com.jaozinfs.paging.movies.R
import com.jaozinfs.paging.movies.di.moviesModules
import kotlinx.android.synthetic.main.activity_movies.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.dsl.koinApplication

class MoviesActivity : AppCompatActivity() {
    private val viewmodel: MoviesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)
        setSupportActionBar(toolbar)

        koinApplication {
            loadKoinModules(moviesModules)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        koinApplication {
            unloadKoinModules(moviesModules)
        }
    }

}