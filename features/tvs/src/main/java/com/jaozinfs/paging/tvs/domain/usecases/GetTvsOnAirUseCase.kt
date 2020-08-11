package com.jaozinfs.paging.tvs.domain.usecases

import com.jaozinfs.paging.network.BaseUseCase
import com.jaozinfs.paging.tvs.data.network.repository.TvsNetworkRepository
import com.jaozinfs.paging.tvs.domain.model.TvUI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetTvsOnAirUseCase(private val tvsNetworkRepository: TvsNetworkRepository) :
    BaseUseCase<Unit?, List<TvUI>> {
    override fun execute(params: Unit?): Flow<List<TvUI>> = flow {
        emit(tvsNetworkRepository.getTvsOnAir())
    }
}