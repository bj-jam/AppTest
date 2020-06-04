package com.app.test.game.api

interface CheckIdiomListener {
    fun currentCharacterNeedFill(relativeX: Int, relativeY: Int): Boolean
}