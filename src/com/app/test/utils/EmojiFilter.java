package com.app.test.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmojiFilter {
    public static Pattern emoji = Pattern
            .compile(
                    "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                    Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

    /**
     * 检测是否有emoji字符
     *
     * @return 一旦含有就抛出
     */
    public static boolean containsEmoji(CharSequence cs) {
        Matcher emojiMatcher = emoji.matcher(cs);
        return emojiMatcher.find();
    }
}