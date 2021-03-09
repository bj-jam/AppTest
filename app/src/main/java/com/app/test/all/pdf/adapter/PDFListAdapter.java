package com.app.test.all.pdf.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.test.R;

import java.io.IOException;


/**
 * @author lcx
 * Created at 2020/12/22
 * Describe:
 */
public class PDFListAdapter extends RecyclerView.Adapter<PDFListAdapter.ItemViewHolder> {
    private final Context context;
    private PDFManager pdfManager;

    public PDFListAdapter(Context context, String fileUrl) {
        this.context = context;
        try {
            pdfManager = new PDFManager(context, fileUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(View.inflate(context, R.layout.item_pdf_view, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        if (pdfManager == null || getItemCount() < position) {
            return;
        }
        Bitmap bitmap = pdfManager.getBitmap(position);
        holder.imageView.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return pdfManager != null ? pdfManager.getCount() : 0;
    }

    public void close() {
        if (pdfManager != null) {
            pdfManager.close();
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        ItemViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
