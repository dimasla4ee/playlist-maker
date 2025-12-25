package com.dimasla4ee.playlistmaker.core.domain.model

sealed class ResultCode(val code: Int) {

    data object NoInternet : ResultCode(-1)
    data object Ok : ResultCode(200)
    data object BadRequest : ResultCode(400)
    data object InternalServerError : ResultCode(500)
    data class Unknown(val raw: Int) : ResultCode(raw)
}