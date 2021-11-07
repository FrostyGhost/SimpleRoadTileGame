package com.fg.road_game.game_tools

import org.json.JSONObject




data class TileModel (
        var tileBg :Int? = 9,
        var rotatePos :Int?= 0,
        var topPos :Int? = 0,
        var bottomPos :Int?= 0,
        var leftPos :Int?=0,
        var rightPos :Int?= 0,
        var arrayPos :Int?= -1,
        var edgePos :Int?= -1
) {
    fun getJson(): JSONObject {
        val obj = JSONObject()

        obj.put("tileBg", tileBg)
        obj.put("rotatePos", rotatePos)
        obj.put("topPos", topPos)
        obj.put("bottomPos", bottomPos)
        obj.put("leftPos", leftPos)
        obj.put("rightPos", rightPos)
        obj.put("arrayPos", arrayPos)
        obj.put("edgePos", edgePos)

        return obj
    }
}