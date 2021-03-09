/*
 * Copyright (c) 2015 mariotaku
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.app.test.pickcrop;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.Spanned;
import android.text.style.ImageSpan;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 */
public class PNCUtils {

    @Nullable
    public static String getImageUrl(final Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) return null;
        return UtilsAPI16.getImageUrl(context);
    }

    public static boolean closeSilently(final Closeable c) {
        if (c == null) return false;
        try {
            c.close();
        } catch (final IOException e) {
            return false;
        }
        return true;
    }

    public static void copyStream(@NonNull final InputStream is, @NonNull final OutputStream os)
            throws IOException {
        final int bufferSize = 8192;
        final byte[] bytes = new byte[bufferSize];
        int count = is.read(bytes, 0, bufferSize);
        while (count != -1) {
            os.write(bytes, 0, count);
            count = is.read(bytes, 0, bufferSize);
        }
    }

    public static boolean deleteMedia(@NonNull Context context, @NonNull Uri uri) {
        final String scheme = uri.getScheme();
        if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            final ContentResolver cr = context.getContentResolver();
            if (getFileAuthority(context).equals(uri.getAuthority())) {
                return cr.delete(uri, null, null) > 0;
            }
            String[] projection = {MediaStore.MediaColumns.DATA};
            Cursor cursor = cr.query(uri, projection, null, null, null);
            if (cursor == null) return false;
            try {
                final boolean result;
                int idx = cursor.getColumnIndex(MediaStore.MediaColumns.DATA);
                if (idx >= 0 && cursor.moveToFirst()) {
                    String path = cursor.getString(idx);
                    result = new File(path).delete();
                } else {
                    result = false;
                }
                if (!result) return false;
                cr.delete(uri, null, null);
                return true;
            } finally {
                cursor.close();
            }
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            return new File(uri.getPath()).delete();
        }
        return false;
    }

    @NonNull
    public static String getFileAuthority(Context context) {
        return context.getPackageName() + ".fileProvider";
    }

    public static void takePersistableUriPermission(ContentResolver resolver, Uri uri, int flags) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        UtilsAPI19.takePersistableUriPermission(resolver, uri, flags);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private static class UtilsAPI16 {

        public static String getImageUrl(final Context context) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) return null;
            final ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            if (cm == null || !cm.hasPrimaryClip()) return null;
            final ClipData primaryClip = cm.getPrimaryClip();
            if (primaryClip != null && primaryClip.getItemCount() > 0) {
                final ClipData.Item item = primaryClip.getItemAt(0);
                final CharSequence styledText = item.coerceToStyledText(context);
                if (styledText instanceof Spanned) {
                    final Spanned spanned = (Spanned) styledText;
                    final ImageSpan[] imageSpans = spanned.getSpans(0, spanned.length(), ImageSpan.class);
                    if (imageSpans.length == 1) return imageSpans[0].getSource();
                }
            }
            return null;
        }
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static class UtilsAPI19 {
        public static void takePersistableUriPermission(ContentResolver resolver, Uri uri, int flags) {
            resolver.takePersistableUriPermission(uri, flags);
        }

    }
}
