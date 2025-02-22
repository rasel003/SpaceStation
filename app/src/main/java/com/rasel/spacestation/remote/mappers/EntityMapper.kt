package com.rasel.spacestation.remote.mappers

interface EntityMapper<M, E> {

    fun mapFromModel(model: M): E
}
