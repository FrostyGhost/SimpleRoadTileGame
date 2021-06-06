package com.fg.blablabla.game_tools

import android.R
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView



class GameTile : AppCompatTextView {

    var tileBg = 9;
    var rotatePos = 0;
    var topPos = 0;
    var bottomPos = 0;
    var leftPos =0;
    var rightPos = 0;
    var arrayPos = -1;
    var edgePos = -1

    constructor(context: Context?) : super(context!!) {}

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {}

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context!!, attrs, defStyle) {}

    fun rotateTile(){
        if (edgePos != -1){
            val newTopPos = leftPos
            val newLeftPos = bottomPos
            val newRightPos = topPos
            val newBottomPos = rightPos

            Log.d("QQ old", "top $topPos right $rightPos bottom $bottomPos left $leftPos")

            leftPos = newLeftPos
            topPos = newTopPos
            rightPos = newRightPos
            bottomPos = newBottomPos

            Log.d("QQ new", "top $topPos right $rightPos bottom $bottomPos left $leftPos")
        }

        rotatePos++
        this.rotation = this.rotation + 90

        if (rotatePos == 3){
            rotatePos = 0
        }
    }

}