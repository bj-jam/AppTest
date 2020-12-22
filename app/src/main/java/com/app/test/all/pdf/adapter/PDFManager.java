package com.app.test.all.pdf.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import java.io.File;
import java.io.IOException;
import java.net.URI;

/**
 * @author lcx
 * Created at 2020/12/22
 * Describe:
 */
public class PDFManager {

    protected PdfRenderer renderer;
    protected BitmapContainer bitmapContainer;
    private final Context context;


    public PDFManager(Context context, String pdfPath) throws IOException {
        this.context = context;
        renderer = new PdfRenderer(getSeekableFileDescriptor(pdfPath));
        PdfRenderer.Page samplePage = renderer.openPage(0);
        //params.setWidth((int) (samplePage.getWidth() * renderQuality));
        // params.setHeight((int) (samplePage.getHeight() * renderQuality));
        bitmapContainer = new SimpleBitmapPool((int) (samplePage.getWidth() * 2.0), (int) (samplePage.getHeight() * 2.0), Bitmap.Config.ARGB_8888, 3);
        samplePage.close();
    }

    protected ParcelFileDescriptor getSeekableFileDescriptor(String path) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor;

        File pdfCopy = new File(path);
        if (pdfCopy.exists()) {
            parcelFileDescriptor = ParcelFileDescriptor.open(pdfCopy, ParcelFileDescriptor.MODE_READ_ONLY);
            return parcelFileDescriptor;
        }
        if (isAnAsset(path)) {
            pdfCopy = new File(context.getCacheDir(), path);
            parcelFileDescriptor = ParcelFileDescriptor.open(pdfCopy, ParcelFileDescriptor.MODE_READ_ONLY);
        } else {
            URI uri = URI.create(String.format("file://%s", path));
            parcelFileDescriptor = context.getContentResolver().openFileDescriptor(Uri.parse(uri.toString()), "rw");
        }
        return parcelFileDescriptor;
    }


    protected boolean isAnAsset(String path) {
        return !path.startsWith("/");
    }

    /**
     * 返回需要显示的bitmap
     *
     * @param position
     * @return
     */
    public Bitmap getBitmap(int position) {
        if (bitmapContainer == null)
            return null;
        Bitmap bitmap = bitmapContainer.get(position);
        PdfRenderer.Page page = renderer.openPage(position);
        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
        page.close();
        return bitmap;
    }

    public int getCount() {
        return renderer != null ? renderer.getPageCount() : 0;
    }

    public void close() {
        if (bitmapContainer != null) {
            bitmapContainer.clear();
        }
        if (renderer != null) {
            renderer.close();
        }
    }
}
