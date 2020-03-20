/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.app.test.view.image;

import java.lang.reflect.Method;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION;
import android.view.View;

@TargetApi(16)
public class SDK16 {

	public static void postOnAnimation(Context context, View view, Runnable r) {
		if (VERSION.SDK_INT >= 16) {
			try {
				// view.postOnAnimation(r); // 4.1 API

				// Class<?> viewClass = Class.forName("android.view.View");
				Class<?> viewClass = View.class;
				Object obj = new View(context);
				Method method = viewClass.getDeclaredMethod("postOnAnimation",
						new Class[] { Runnable.class });
				method.invoke(obj, new Object[] { r });
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
