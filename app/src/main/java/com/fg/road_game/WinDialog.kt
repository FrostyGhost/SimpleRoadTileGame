package com.fg.road_game
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import androidx.fragment.app.DialogFragment
import java.util.*
import android.os.Bundle
import android.view.*

import kotlinx.android.synthetic.main.win_dialog_fragment.*
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size


private const val DIALOG_HEIGHT = 1.0
private const val DIALOG_WIDTH = 1.0

open class WinDialog(timeAfterStart: Long, nextLvl: Int) : DialogFragment(){

    private var lvlTime = timeAfterStart
    private val nextLvl = nextLvl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.Dialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.win_dialog_fragment, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTimeView()
        setupNextBtn()
        setupDialogClickListener()
        callKonfetti()

    }

    override fun onStart() {
        super.onStart()
        setDialogSize(DIALOG_WIDTH, DIALOG_HEIGHT)
    }

    private fun setupTimeView(){
        val seconds = (lvlTime % 60)
        val minutes = (lvlTime / 60)

        txtTime.text = String.format("%sm : %ss", minutes, seconds)
    }

    private fun setupDialogClickListener(){
        container.setOnClickListener { dismiss() }
    }

    private fun setupNextBtn(){
        txtOpenNextLvl.setOnClickListener {
            openNextLvl()
        }
    }

    private fun openNextLvl(){
        if (nextLvl < Constant.BASE_LVL_ELEMENT.lvls.size){
            val intent = Intent(requireActivity(), MainActivity::class.java)
            intent.putExtra(Constant.BUNDLE_ARGS.selectedNum, nextLvl)
            startActivity(intent)
        }else{
            val intent = Intent(requireActivity(), MenuActivity::class.java)
            startActivity(intent)
        }
    }

    private fun callKonfetti(){
        viewKonfetti.build()
            .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
            .setDirection(0.0, 359.0)
            .setSpeed(1f, 5f)
            .setFadeOutEnabled(true)
            .setTimeToLive(2000L)
            .addShapes(Shape.Square, Shape.Circle)
            .addSizes(Size(12))
            .setPosition(-50f, viewKonfetti.width + 50f, -50f, -50f)
            .streamFor(300, 5000L)
    }

   private fun setDialogSize(newWidth: Double, newHeight: Double) {
        if (dialog != null && dialog!!.window != null) {
            val window = dialog!!.window
            val size = Point()
            val display = window!!.windowManager.defaultDisplay
            display.getSize(size)
            val width = size.x
            val height = size.y
            window.setLayout(
                ((width * newWidth).toInt()),
                ((height * newHeight).toInt())
            )
            Objects.requireNonNull(dialog!!.window)?.setGravity(Gravity.CENTER)

        }
    }

    override fun dismiss() {
        super.dismiss()

        val intent = Intent(requireActivity(), MenuActivity::class.java)
        startActivity(intent)
    }
}


