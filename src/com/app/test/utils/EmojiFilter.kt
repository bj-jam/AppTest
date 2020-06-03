package com.app.test.utils

import java.util.regex.Pattern

object EmojiFilter {
    var emoji = Pattern
            .compile(
                    "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                    Pattern.UNICODE_CASE or Pattern.CASE_INSENSITIVE)

    /**
     * 检测是否有emoji字符
     *
     * @return 一旦含有就抛出
     */
    fun containsEmoji(cs: CharSequence?): Boolean {
        val emojiMatcher = emoji.matcher(cs)
        return emojiMatcher.find()
    }
}