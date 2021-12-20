package com.test.nearbyplaces.domain.mappers

interface ThreeWayMapper<D, L, R> {
    fun remoteToLocal(type: R): L
    fun localToDomain(type: L): D
    fun domainToLocal(type: D): L
    fun remoteToDomain(type: R): D
}

interface Mapper<DTO, LOCAL> {
    fun fromDto(type: DTO): LOCAL
    fun toDto(type: LOCAL): DTO

}