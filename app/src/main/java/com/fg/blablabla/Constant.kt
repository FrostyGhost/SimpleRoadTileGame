package com.fg.blablabla

class Constant {

    object BASE_LVL_ELEMENT {
        val bg  = arrayOf(
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

        var lvls = arrayOf(
                R.string.lvl_1,
                R.string.lvl_2,
                R.string.lvl_3,
                R.string.lvl_4,
                R.string.lvl_5,
                R.string.lvl_6,
                R.string.lvl_7,
                R.string.lvl_8,
                R.string.lvl_9,
                R.string.lvl_10
        )
    }

    object BUNDLE_ARGS{
        const val SharedPreferences = "BlaBlaBla"
        const val selectedNum = "selectedNum"
        const val testLvl = "textLVL"
        const val currentLvl = "currentLvl"
    }

}