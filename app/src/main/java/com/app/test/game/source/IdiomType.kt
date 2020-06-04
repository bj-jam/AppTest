package com.app.test.game.source

interface IdiomType {
    companion object {
        const val HIDE = 0
        const val SHOW = 1

        //从成语棋盘取下备选词
        const val TAKE_DOWN_WORD = 3

        //全部打完且答对
        const val ALL_FILL_RIGHT = 4

        //成语填词每次答对的数量
        const val EACH_PROVERB_RIGHT_COUNT = 5

        //自动填充某个字，通知备选面板
        const val AUTO_FILL_CHAR = 6

        //完成成语之后，把每个成语最后一个字所在view传过去
        const val ANSWER_RIGHT_VIEWS_POSITION = 7

        //成语填写完成且填错
        const val PROVERB_FILL_ERROR = 8

        //toast提示
        const val SHOW_TOAST = 9

        //成语填词每次答对收集答对间隔
        const val EACH_PROVERB_RIGHT_COLLECT = 10

        //完成成语之后，每个成语的view执行放大动画
        const val ANSWER_RIGHT_IDIOM_VIEWS_POSITION = 11

        //填完且没有填对成语之后，每个成语的view执行位移动画
        const val ANSWER_ERROR_IDIOM_VIEWS_POSITION = 12
    }
}