package com.fg.blablabla

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import com.fg.blablabla.editor.CreateLvlActivity
import kotlinx.android.synthetic.main.activity_menu2.*

class MenuActivity : AppCompatActivity() {

    private lateinit var llId : LinearLayout
    private var currLvl = 1
    private val maxLvl=50
    private var lvl = 0
    private var h: Int = 300

    private lateinit var sp: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu2)

        sp = getSharedPreferences(Constant.BUNDLE_ARGS.SharedPreferences, Context.MODE_PRIVATE)
        currLvl = sp.getInt(Constant.BUNDLE_ARGS.currentLvl, 0)
        setupDisplay()
        setupOthersBtnListener()
    }

    private fun setupDisplay(){
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val x = size.x
        val y = size.y
        h = x / (3)

        createLvl()
    }

    private fun createLvl(){
        for ( y in 0 until 10){
            for (x in 0 until 3){
                if (x == 0){
                    val linearLayout = LinearLayout(this)
                    linearLayout.orientation = LinearLayout.HORIZONTAL
                    linearLayout.id = View.generateViewId()
                    llId = linearLayout
                    lvl_container.addView(linearLayout)
                }
                val btn = lvlBtn()
                llId.addView(btn)
            }
        }
    }

    private fun lvlBtn(): Button {
        val btn = Button(this)
        btn.layoutParams = LinearLayout.LayoutParams(h,h)
        btn.id = View.generateViewId()
        btn.text = lvl.toString()
        when {
            lvl < currLvl -> {
                btn.setBackgroundResource(R.drawable.rounded_lvl_btn)
            }
            lvl == currLvl ->{
                btn.setBackgroundResource(R.drawable.rounded_current_btn)
            }
            else -> {
                btn.setTextColor(Color.WHITE)
                btn.setBackgroundResource(R.drawable.rounded_disable_btn)
//                btn.isClickable = false
            }
        }
        lvl++
        btn.setOnClickListener { click(it as Button) }
        return btn
    }

    private fun click (view: Button): View.OnClickListener? {
        if (view.text.toString().toInt() <= currLvl){
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(Constant.BUNDLE_ARGS.selectedNum, view.text.toString().toInt())
            startActivity(intent)
        }
        return null
    }

    private fun setupOthersBtnListener(){
        btn_createLvL.setOnClickListener {
            val intent = Intent(this, CreateLvlActivity::class.java)
            startActivity(intent)
        }

        btn_testLvL.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val jsonParser = com.fg.blablabla.game_tools.JsonParser()
            val  lvl = jsonParser.getLvl(clipboard.text.toString())
            if (lvl == null){
                Toast.makeText(this, "СКОПІЮЙТЕ КОРЕКТНИЙ РІВЕНЬ", Toast.LENGTH_SHORT).show()
            }else{
                sp.edit().putString(Constant.BUNDLE_ARGS.testLvl, clipboard.text.toString()).apply()
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra(Constant.BUNDLE_ARGS.testLvl, true)
                startActivity(intent)
            }
        }
    }
}