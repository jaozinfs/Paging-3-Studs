package com.jaozinfs.paging.tvs.domain.model

data class TvDetailsUI(
    val backdrop_path: String?,
//    val created_by: List<Any>,
    val episode_run_time: List<Int>,
    val first_air_date: String,
    val genres: List<GenreUI>,
    val homepage: String,
    val id: Int,
    val in_production: Boolean,
    val languages: List<String>,
    val last_air_date: String,
    val last_episode_to_air: LastEpisodeToAirUI,
    val name: String,
    val networks: List<NetworkUI>,
//    val next_episode_to_air: Any,
    val number_of_episodes: Int,
    val number_of_seasons: Int,
    val origin_country: List<String>,
    val original_language: String,
    val original_name: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String?,
//    val production_companies: List<Any>,
    val seasons: List<SeasonUI>,
    val status: String,
    val type: String,
    val vote_average: Double,
    val vote_count: Int
)