package com.fg.road_game.game_tools

import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception


class JsonParser {

    @Throws(JSONException::class)
    fun getLvl(response: String): LvlStructures? {
        try {
            val gson = Gson()
            val lvlJson = JSONObject(response)
            val id = lvlJson.getInt("gameId")
            val matrixX = lvlJson.getInt("matrixX")
            val matrixY = lvlJson.getInt("matrixY")
            val tilesList: Array<TileModel> =
                    gson.fromJson(lvlJson.getString("modelList").toString(), Array<TileModel>::class.java)

            return LvlStructures(
                    id,
                    matrixX,
                    matrixY,
                    tilesList
            )
        }catch (e:Exception){
            return null
        }
    }

}