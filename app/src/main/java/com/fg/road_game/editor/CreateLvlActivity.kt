package com.fg.road_game.editor

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.fg.road_game.R
import com.fg.road_game.game_tools.GameTile
import com.fg.road_game.game_tools.TileModel
import kotlinx.android.synthetic.main.create_lvl.*
import org.json.JSONArray
import org.json.JSONObject

class CreateLvlActivity : Activity() {

    private var rowsList : ArrayList<LinearLayout> = ArrayList<LinearLayout>()
    private var selectedTile = 9
    private var btnIdArray:ArrayList<Int> = ArrayList()
    private  var tileArray : ArrayList<GameTile> = ArrayList<GameTile>()
    private var isRotate = false

    private var startId = 0
    var x:Int = 720
    var y:Int = 1080
    var h: Int = 100

    private var bg  = arrayOf(
            R.drawable.ic_top,
            R.drawable.ic_bottom_road,
            R.drawable.ic_road3,
            R.drawable.ic_road4,
            R.drawable.ic_round,
            R.drawable.ic_tonel_1,
            R.drawable.ic_tonel_2,
            R.drawable.ic_tonel_3,
            R.drawable.ic_tonel_4,
            R.drawable.ic_border)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_lvl)

        initRowList()
        setupListeners()
    }

    fun selectTileClickListeners(v: View?){
        val text = v!! as TextView
        selectedTile = if (text.text.toString() == "Empty Block"){
            10
        }else {
            text.text.toString().toInt()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupListeners(){
        tileRotate.setOnClickListener {
            isRotate = !isRotate
            if (isRotate){
                tileRotate.text = "Rotate: ON"
            }else{
                tileRotate.text = "Rotate: OFF"
            }
        }
        tileSave.setOnClickListener {
            saveLvl()
        }
        defultValues.setOnClickListener {
            et_x.setText("8")
            et_y.setText("10")
            et_id.setText("0")
            createGameBoard()
        }
        start.setOnClickListener {
            if (et_id.text.isNotEmpty() && et_id.text.isNotBlank()
                    && et_x.text.isNotEmpty() && et_x.text.isNotBlank()
                    && et_y.text.isNotEmpty() && et_y.text.isNotBlank()){
                createGameBoard()
            }else {
                Toast.makeText(this, "Заповніть всі поля", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createGameBoard(){
        create_lvl_step2.visibility = View.VISIBLE
        create_lvl_step_1.visibility = View.GONE
        setScreenSize()
        initMatrix(et_x.text.toString().toInt(), et_y.text.toString().toInt())
    }

    fun onClick(v: View?) {
        when (v!!.id) {
            in 0..121 -> {
                val tile = findViewById<GameTile>(v.id)

                if (isRotate){
                    tile.rotateTile()
                }else{
                    setBaseTileParam(tile)
                }
            }
        }
    }

    private fun setBaseTileParam(tile: GameTile){
        when (selectedTile){
            1 -> {
                baseTopTileParam(tile, true)
            }
            2 -> {
                baseTopTileParam(tile, false)
            }
            3 -> {
                baseRoundParam(tile)
            }
            4 -> {
                baseTreeParam(tile)
            }
            5 -> {
                baseFourParam(tile)
            }
            6 -> {
                baseTunnel1Param(tile)
            }
            7 -> {
                baseTunnel2Param(tile)
            }
            8 -> {
                baseTunnel3Param(tile)
            }
            9 ->{
                baseTunnel4Param(tile)
            }
            10->{
                baseBorderParam(tile)
            }
        }
    }

    private fun initMatrix(sizeH: Int, sizeW: Int){
        for (x in 0 until sizeW){
            for (y in 0 until sizeH){
                val btn = createBtn(y)
                rowsList[x].addView(btn)
            }
        }
    }

    private fun createBtn(int: Int): GameTile {
        val tile  = GameTile(this)
        tile.layoutParams = LinearLayout.LayoutParams(h,h)
        tile.id = startId
        tile.setBackgroundResource(bg[9])
        tile.setOnClickListener(View.OnClickListener { onClick(it as GameTile) })
        tile.arrayPos = startId;
        btnIdArray.add(tile.id)
        startId++
        tileArray.add(tile)

        return tile
    }

    private fun setScreenSize(){
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        x = size.x
        y = size.y
        h = x / (et_x.text.toString().toInt()+1)
    }

    private fun baseTopTileParam(tile: GameTile, isTop : Boolean){
        if (isTop){
            tile.topPos = 1
            tile.bottomPos = 1
            tile.leftPos = 0
            tile.rightPos = 0
            tile.tileBg = 0
            tile.setBackgroundResource(bg[0])
        }else{
            tile.topPos = 0
            tile.bottomPos = 0
            tile.leftPos = 1
            tile.rightPos = 1
            tile.tileBg = 1
            tile.setBackgroundResource(bg[1])
        }
        tile.rotatePos = 0
        tile.rotation = 0f
        tile.edgePos = 1
    }

    private fun baseTreeParam (tile: GameTile){
        tile.topPos = 0
        tile.bottomPos = 1
        tile.leftPos = 1
        tile.rightPos = 1
        tile.tileBg = 2
        tile.rotatePos = 0
        tile.rotation = 0f
        tile.edgePos = 1
        tile.setBackgroundResource(bg[2])
    }

    private fun baseFourParam (tile: GameTile){
        tile.topPos = 1
        tile.bottomPos = 1
        tile.leftPos = 1
        tile.rightPos = 1
        tile.tileBg = 3
        tile.rotatePos = 0
        tile.rotation = 0f
        tile.edgePos = 1
        tile.setBackgroundResource(bg[3])
    }

    private fun baseRoundParam (tile: GameTile){
        tile.topPos = 1
        tile.bottomPos = 0
        tile.leftPos = 0
        tile.rightPos = 1
        tile.tileBg = 4
        tile.rotatePos = 0
        tile.rotation = 0f
        tile.edgePos = 1
        tile.setBackgroundResource(bg[4])
    }

    private fun baseTunnel1Param (tile: GameTile){
        tile.topPos = 0
        tile.bottomPos = 1
        tile.leftPos = 0
        tile.rightPos = 0
        tile.tileBg = 5
        tile.rotatePos = 0
        tile.rotation = 0f
        tile.edgePos = 1
        tile.setBackgroundResource(bg[5])
    }
    private fun baseTunnel2Param (tile: GameTile){
        tile.topPos = 0
        tile.bottomPos = 0
        tile.leftPos = 0
        tile.rightPos = 1
        tile.tileBg = 6
        tile.rotatePos = 0
        tile.rotation = 0f
        tile.edgePos = 1
        tile.setBackgroundResource(bg[6])
    }

    private fun baseTunnel3Param (tile: GameTile){
        tile.topPos = 0
        tile.bottomPos = 0
        tile.leftPos = 1
        tile.rightPos = 0
        tile.tileBg = 7
        tile.rotatePos = 0
        tile.rotation = 0f
        tile.edgePos = 1
        tile.setBackgroundResource(bg[7])
    }

    private fun baseTunnel4Param (tile: GameTile){
        tile.topPos = 1
        tile.bottomPos = 0
        tile.leftPos = 0
        tile.rightPos = 0
        tile.tileBg = 8
        tile.rotatePos = 0
        tile.rotation = 0f
        tile.edgePos = 1
        tile.setBackgroundResource(bg[8])
    }

    private fun baseBorderParam (tile: GameTile){
        tile.topPos = 0
        tile.bottomPos = 0
        tile.leftPos = 0
        tile.rightPos = 0
        tile.tileBg = 9
        tile.rotatePos = 0
        tile.rotation = 0f
        tile.edgePos = 1
        tile.setBackgroundResource(bg[9])
    }

    private fun initRowList(){
        rowsList.add(row_1)
        rowsList.add(row_2)
        rowsList.add(row_3)
        rowsList.add(row_4)
        rowsList.add(row_5)
        rowsList.add(row_6)
        rowsList.add(row_7)
        rowsList.add(row_8)
        rowsList.add(row_9)
        rowsList.add(row_10)
        rowsList.add(row_11)
    }

    private fun saveLvl(){
        val modelList : ArrayList<TileModel> = ArrayList()

        for (tile in tileArray){
            modelList.add(TileModel(
                    tileBg = tile.tileBg, rotatePos = tile.rotatePos, arrayPos = tile.arrayPos,
                    topPos = tile.topPos, bottomPos = tile.bottomPos, leftPos = tile.leftPos,
                    rightPos = tile.rightPos, edgePos = tile.edgePos
            ))
        }

        val myJSONArray = JSONArray()
        val lvl = JSONObject()
        val array = JSONArray()
        for (model in modelList) {
            array.put(model.getJson())
        }

        lvl.put("gameId", et_id.text.toString())
        lvl.put("modelList", array)
        lvl.put("matrixY", et_y.text.toString())
        lvl.put("matrixX", et_x.text.toString())
        myJSONArray.put(lvl)

        val clipboard: ClipboardManager =
                applicationContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", lvl.toString())
        clipboard.setPrimaryClip(clip)

        Toast.makeText(this, "LVL saved", Toast.LENGTH_SHORT).show()
    }

}