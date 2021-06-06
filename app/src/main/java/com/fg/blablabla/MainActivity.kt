package com.fg.blablabla

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fg.blablabla.game_tools.GameTile
import com.fg.blablabla.game_tools.TileModel
import com.fg.blablabla.grapf.DepthFirstSearch
import com.fg.blablabla.grapf.Dijkstra
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception


class MainActivity : AppCompatActivity() , DepthFirstSearch.OnFinish{

    private var rowsList : ArrayList<LinearLayout> = ArrayList<LinearLayout>()
    private val tiles : ArrayList<GameTile> = ArrayList();
    private lateinit var depthFirstSearchExampleNeighbourList : DepthFirstSearch
    private val vertex = ArrayList<DepthFirstSearch.Vertex>()
    var tileVertex:ArrayList<Int> = ArrayList()
    var start = -1
    var end = -1

    private var startId = 0
    var x:Int = 720
    var y:Int = 1080
    var h: Int = 100
    var matrixY : Int = 5;
    var matrixX : Int = 5;

    var selectedLvl : Int = 0;

    var testLvLString = false

    private lateinit var sp: SharedPreferences

    private lateinit var tileModelList : List<TileModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sp = getSharedPreferences(Constant.BUNDLE_ARGS.SharedPreferences, Context.MODE_PRIVATE)
        depthFirstSearchExampleNeighbourList =  DepthFirstSearch(this)

        initRowList()
        getArgs()
    }

    private fun getArgs(){
        testLvLString = intent.getBooleanExtra(Constant.BUNDLE_ARGS.testLvl, false)
        if (testLvLString){
            val jsonParser = com.fg.blablabla.game_tools.JsonParser()
            val  lvl = jsonParser.getLvl(sp.getString(Constant.BUNDLE_ARGS.testLvl, "")!!)
            tileModelList = lvl!!.tilesList.toList()
            matrixY = lvl.matrixX!!
            matrixX = lvl.matrixY!!
            createGameBoard()
        }

        selectedLvl = intent.getIntExtra(Constant.BUNDLE_ARGS.selectedNum, -1)
        if (selectedLvl != -1){
            try {
                val jsonParser = com.fg.blablabla.game_tools.JsonParser()
                val  lvl = jsonParser.getLvl(getString(Constant.BASE_LVL_ELEMENT.lvls[selectedLvl]))
                tileModelList = lvl!!.tilesList.toList()
                matrixY = lvl.matrixX!!
                matrixX = lvl.matrixY!!
                createGameBoard()
            }catch (e:Exception){
                Toast.makeText(this, "Вийшло", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createGameBoard(){
        setScreenSize()
        initMatrix(matrixY, matrixX)
    }

    private fun initMatrix(matrixY: Int, matrixX: Int){
        for (x in 0 until matrixY){
            for (y in 0 until matrixX){
                if (tileModelList.size > tiles.size){
                    val btn = createBtn()
                    rowsList[x].addView(btn)
                }
            }
        }
        //це дял дейкстри, якщо відкину цей варік то видалити
        setupTileVertexList()
    }

    private fun createBtn(): GameTile {
        val tile  = GameTile(this)
        val tileModel = tileModelList[startId]
        tile.setBackgroundResource(Constant.BASE_LVL_ELEMENT.bg[tileModel.tileBg!!])
        tile.setOnClickListener(View.OnClickListener { onClick(it as GameTile) })
        tile.layoutParams = LinearLayout.LayoutParams(h,h)

        tile.id = startId
        tile.topPos = tileModel.topPos!!
        tile.bottomPos = tileModel.bottomPos!!
        tile.rightPos = tileModel.rightPos!!
        tile.leftPos = tileModel.leftPos!!
        tile.tileBg = tileModel.tileBg!!
        tile.edgePos = tileModel.edgePos!!
        tile.arrayPos = tileModel.arrayPos!!
        tile.rotatePos = tileModel.rotatePos!!
        tile.rotation = 90f * tileModel.rotatePos!!
        tiles.add(tile)
        startId++

        return tile
    }

    private fun setScreenSize(){
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        x = size.x
        y = size.y
        h = x / (matrixX+1)
    }

    fun onClick(v: View?) {
        when (v!!.id) {
            in 0..121 -> {
                val tile = findViewById<GameTile>(v.id)

                //перевіряємо який тип блоку, якщо це тунель чи пуста клітинка, рухати не потрібно
                if (!(tiles[v.id].tileBg == 9
                        || tiles[v.id].tileBg == 5
                        || tiles[v.id].tileBg == 6
                        || tiles[v.id].tileBg == 7
                        || tiles[v.id].tileBg == 8
                        || tiles[v.id].tileBg == 3)){

                    Log.e("QQ", "fdsfadsfdsa");
                    //тут розвертаємо блок
                    tile.rotateTile()

                    //глибина
                    //createGraph()
                    //дикстра
                    Log.e("QQ", "tileVertex Size " + tileVertex.size)
                    createDijkstraStartData(tileVertex.size)
                }

            }
        }
    }

    private fun setupTileVertexList(){
        //додаємо вершини в масив
        for (tile in tiles){
            if (tile.tileBg != 9){
                tileVertex.add(tile.arrayPos)
            }
        }
        // отримуємо початок та кінець дерева
        for (tile in tiles) {
            if (tile.tileBg == 5
                    || tile.tileBg == 6
                    || tile.tileBg == 7
                    || tile.tileBg == 8) {
                if (start == -1) {
                    start = tile.arrayPos
                } else {
                    end = tile.arrayPos
                }
            }
        }
    }

    private fun createDijkstraStartData(n: Int){
        val graph = Array(n) { IntArray(n) }

        //заповнюємо матрицю
        for (i in 0 until n) {
            for (j in 0 until n) {
                graph[i][j] = -1
            }
        }

        //перевіряємо, чи тайл має зв'язок з сусідніми тайлами. 9 тайл це пуста клітинка
        for (tile in tiles){
            if (tile.tileBg != 9){
                isConnected(tile, graph)
            }
        }

        //вивід матриці суміжності, чисто для наочності
        for (i in graph.indices) {
            for (j in graph[i].indices) {
                print(" " + graph[i][j].toString() + " ")
            }
            println()
        }

        // тут власне проходимо по дереву
        val dijkstra = Dijkstra(this)
        dijkstra.checkMatrix(tileVertex.indexOf(start), graph, tileVertex.size, tileVertex.indexOf(end))
    }

    //перевіряємо чи блок з'єднаний з іншим
    @SuppressLint("SetTextI18n")
    private fun isConnected(tile: GameTile, graph: Array<IntArray>) {
        if (tile.edgePos != -1){
            if (tile.rightPos == 1
                    && (inBounds(tile.arrayPos +1,tiles.size)
                            && tiles[tile.arrayPos +1].leftPos == 1)){
                graph[tileVertex.indexOf(tile.arrayPos)][tileVertex.indexOf(tiles[tile.arrayPos + 1].arrayPos)] = 2;
                graph[tileVertex.indexOf(tiles[tile.arrayPos + 1].arrayPos)][tileVertex.indexOf(tile.arrayPos)] = 2;
            }
            if (tile.leftPos == 1
                    && (inBounds(tile.arrayPos -1,tiles.size)
                            && tiles[tile.arrayPos -1].rightPos == 1)){
                graph[tileVertex.indexOf(tile.arrayPos)][tileVertex.indexOf(tiles[tile.arrayPos - 1].arrayPos)] = 2;
                graph[tileVertex.indexOf(tiles[tile.arrayPos - 1].arrayPos)][tileVertex.indexOf(tile.arrayPos)] = 2;
            }
            if (tile.topPos == 1
                    && (inBounds(tile.arrayPos - matrixX,tiles.size)
                            && tiles[tile.arrayPos - matrixX].bottomPos == 1)){
                graph[tileVertex.indexOf(tile.arrayPos)][tileVertex.indexOf(tiles[tile.arrayPos - matrixX].arrayPos)] = 2;
                graph[tileVertex.indexOf(tiles[tile.arrayPos - matrixX].arrayPos)][tileVertex.indexOf(tile.arrayPos)] = 2;
            }
            if (tile.bottomPos == 1
                    && inBounds(tile.arrayPos +matrixX,tiles.size)
                        && tiles[tile.arrayPos +matrixX].topPos == 1){
                    graph[tileVertex.indexOf(tile.arrayPos)][tileVertex.indexOf(tiles[tile.arrayPos + matrixX].arrayPos)] = 2;
                    graph[tileVertex.indexOf(tiles[tile.arrayPos + matrixX].arrayPos)][tileVertex.indexOf(tile.arrayPos)] = 2;
                }
            }
    }

// TODO дивись тут знизу метод пошуку в глибину, я його чуть пізніше видалю, вибач за засраний код

//    private fun createGraph() {
//        vertex.clear()
//
//        var start = -1
//        var end = -1
//        var edgePos = 0;
//        for (tile in tiles){
//            if (tile.tileBg == 5
//                    || tile.tileBg == 6
//                    || tile.tileBg == 7
//                    || tile.tileBg == 8){
//                if (start == -1){
//                    start = tile.arrayPos
//                }else{
//                    end = tile.arrayPos
//                }
//            }
//            if(tile.edgePos >= 0){
//                vertex.add(DepthFirstSearch.Vertex(tile.arrayPos))
//                tile.edgePos = edgePos
//                edgePos++
//            }
//        }
//
//        for (tile in tiles){
//            if (tile.tileBg != 9){
//                isConnected(tile)
//            }
//        }
//
//        depthFirstSearchExampleNeighbourList.directions.clear()
//        depthFirstSearchExampleNeighbourList.endPos = end
//        if (!vertex.isNullOrEmpty()){
//            depthFirstSearchExampleNeighbourList.dfs(vertex[0])
//        }
//
//    }
//
//    //тут є питання
//    @SuppressLint("SetTextI18n")
//    private fun isConnected(tile: GameTile) {
//        if (tile.edgePos != -1){
//            if (tile.rightPos == 1
//                && (inBounds(tile.arrayPos +1,tiles.size)
//                        && tiles[tile.arrayPos +1].leftPos == 1)){
//                vertex[tile.edgePos].addEdge(vertex[tiles[tile.arrayPos + 1].edgePos])
//            }
//            if (tile.leftPos == 1
//                && (inBounds(tile.arrayPos -1,tiles.size)
//                        && tiles[tile.arrayPos -1].rightPos == 1)){
//                vertex[tile.edgePos].addEdge(vertex[tiles[tile.arrayPos - 1].edgePos])
//            }
//            if (tile.topPos == 1
//                && (inBounds(tile.arrayPos - matrixX,tiles.size)
//                        && tiles[tile.arrayPos - matrixX].bottomPos == 1)){
//                vertex[tile.edgePos].addEdge(vertex[tiles[tile.arrayPos - matrixX].edgePos])
//            }
//            if (tile.bottomPos == 1){
//                Log.e("QQ","bottom true")
//                if (inBounds(tile.arrayPos +matrixX,tiles.size)
//                                && tiles[tile.arrayPos +matrixX].topPos == 1){
//                    vertex[tile.edgePos].addEdge(vertex[tiles[tile.arrayPos +matrixX].edgePos])
//                    Log.e("QQ","bottom true true")
//
//                }
//            }
//
//        }
//    }

    private fun inBounds(index: Int, length: Int) : Boolean{
        return index in 0 until length
    }

    override fun isFinish(isFinish: Boolean) {
        if (isFinish){
            Toast.makeText(this, "ПЕРЕМОГА",Toast.LENGTH_SHORT).show()
            Log.e("QQ", "Вийшло")

            if (!testLvLString && selectedLvl != -1 && sp.getInt(Constant.BUNDLE_ARGS.currentLvl, 0) == selectedLvl){
                sp.edit().putInt(Constant.BUNDLE_ARGS.currentLvl, sp.getInt(Constant.BUNDLE_ARGS.currentLvl, -1) +1 ).apply();
                Log.e("QQ", " зберегло ")
            }
        }else{
            Log.e("QQ", "Не Вийшло")
        }
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

}