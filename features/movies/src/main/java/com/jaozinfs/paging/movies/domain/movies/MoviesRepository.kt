package com.jaozinfs.paging.movies.domain.movies

import com.jaozinfs.paging.database.local.entities.MovieEntity

interface MoviesRepository {
    suspend fun getMovies(page: Int): List<MovieEntity>
    suspend fun getMovieDetails(movieId: Int): MovieUi
    suspend fun getMovieImages(movieId: Int): MovieImagesUI
    suspend fun getGenres(): List<GenreUI>
}