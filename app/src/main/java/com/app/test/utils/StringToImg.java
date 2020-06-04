package com.app.test.utils;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.app.test.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.TextUtils;


public class StringToImg {
	public static String[][] object = { { "\\[织\\]", "<img src='bq1' />" },
			{ "\\[神马\\]", "<img src='bq2' />" },
			{ "\\[浮云\\]", "<img src='bq3' />" },
			{ "\\[给力\\]", "<img src='bq4' />" },
			{ "\\[围观\\]", "<img src='bq5' />" },
			{ "\\[威武\\]", "<img src='bq6' />" },
			{ "\\[熊猫\\]", "<img src='bq7' />" },
			{ "\\[兔子\\]", "<img src='bq8' />" },
			{ "\\[奥特曼\\]", "<img src='bq9' />" },
			{ "\\[囧\\]", "<img src='bq10' />" },
			{ "\\[互粉\\]", "<img src='bq11' />" },
			{ "\\[礼物\\]", "<img src='bq12' />" },
			{ "\\[呵呵\\]", "<img src='bq13' />" },
			{ "\\[嘻嘻\\]", "<img src='bq14' />" },
			{ "\\[哈哈\\]", "<img src='bq15' />" },

			{ "\\[可爱\\]", "<img src='bq16' />" },
			{ "\\[可怜\\]", "<img src='bq17' />" },
			{ "\\[挖鼻屎\\]", "<img src='bq18' />" },
			{ "\\[吃惊\\]", "<img src='bq19' />" },
			{ "\\[害羞\\]", "<img src='bq20' />" },
			{ "\\[挤眼\\]", "<img src='bq21' />" },
			{ "\\[闭嘴\\]", "<img src='bq22' />" },
			{ "\\[鄙视\\]", "<img src='bq23' />" },
			{ "\\[爱你\\]", "<img src='bq24' />" },
			{ "\\[泪\\]", "<img src='bq25' />" },
			{ "\\[偷笑\\]", "<img src='bq26' />" },
			{ "\\[亲亲\\]", "<img src='bq27' />" },
			{ "\\[生病\\]", "<img src='bq28' />" },

			{ "\\[太开心\\]", "<img src='bq29' />" },
			{ "\\[懒得理你\\]", "<img src='bq30' />" },
			{ "\\[右哼哼\\]", "<img src='bq31' />" },
			{ "\\[左哼哼\\]", "<img src='bq32' />" },
			{ "\\[嘘\\]", "<img src='bq33' />" },
			{ "\\[衰\\]", "<img src='bq34' />" },

			{ "\\[委屈\\]", "<img src='bq35' />" },
			{ "\\[吐\\]", "<img src='bq36' />" },
			{ "\\[打哈气\\]", "<img src='bq37' />" },
			{ "\\[抱抱\\]", "<img src='bq38' />" },
			{ "\\[怒\\]", "<img src='bq39' />" },
			{ "\\[疑问\\]", "<img src='bq40' />" },
			{ "\\[馋嘴\\]", "<img src='bq41' />" },
			{ "\\[拜拜\\]", "<img src='bq42' />" },
			{ "\\[思考\\]", "<img src='bq43' />" },
			{ "\\[汗\\]", "<img src='bq44' />" },
			{ "\\[困\\]", "<img src='bq45' />" },

			{ "\\[睡觉\\]", "<img src='bq46' />" },
			{ "\\[钱\\]", "<img src='bq47' />" },
			{ "\\[失望\\]", "<img src='bq48' />" },

			{ "\\[酷\\]", "<img src='bq49' />" },
			{ "\\[花心\\]", "<img src='bq50' />" },
			{ "\\[鼓掌\\]", "<img src='bq51' />" },
			{ "\\[哼\\]", "<img src='bq52' />" },
			{ "\\[心\\]", "<img src='bq53' />" },
			{ "\\[伤心\\]", "<img src='bq54' />" },
			{ "\\[猪头\\]", "<img src='bq55' />" },
			{ "\\[ok\\]", "<img src='bq56' />" },
			{ "\\[耶\\]", "<img src='bq57' />" },
			{ "\\[good\\]", "<img src='bq58' />" },

			{ "\\[不要\\]", "<img src='bq59' />" },
			{ "\\[赞\\]", "<img src='bq60' />" },
			{ "\\[来\\]", "<img src='bq61' />" },
			{ "\\[蜡烛\\]", "<img src='bq62' />" },
			{ "\\[钟\\]", "<img src='bq63' />" },
			{ "\\[蛋糕\\]", "<img src='bq64' />" },
			{ "\\[话筒\\]", "<img src='bq65' />" },
			{ "\\[手套\\]", "<img src='bq66' />" },
			{ "\\[坏笑\\]", "<img src='bq26' />" }, };

	public static String replace(String sourceString) {
		String temp = sourceString;
		for (int i = 0; i < object.length; i++) {
			String[] result = object[i];
			if (result != null && result[0] != null) {
				Pattern pattern = Pattern.compile(result[0]);
				Matcher matcher = pattern.matcher(temp);
				temp = matcher.replaceAll(result[1]);
			}
		}
		return temp;
	}

	public static CharSequence formatString(String str, final Context context) {
		if (TextUtils.isEmpty(str)) {
			return "";
		}
		ImageGetter get = new ImageGetter() {
			@Override
			public Drawable getDrawable(String source) {
				if (source.startsWith("bq")) {
					Drawable d = context.getResources().getDrawable(
							getResourceId(source));
					d.setBounds(0, 0, d.getIntrinsicWidth(),
							d.getIntrinsicHeight());
					return d;
				} else {
					return null;
				}
			}
		};
		CharSequence ch = Html.fromHtml(replace(str), get, null);
		return ch;
	}

	// 利用反射机制，通过资源名字得到资源的ID
	public static int getResourceId(String name) {
		try {
			Field field = R.drawable.class.getField(name);
			return Integer.parseInt(field.get(null).toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}