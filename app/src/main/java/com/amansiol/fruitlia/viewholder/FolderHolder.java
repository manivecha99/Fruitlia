package com.amansiol.fruitlia.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amansiol.fruitlia.R;

public class FolderHolder extends RecyclerView.ViewHolder {
    public ImageView imageView;
    public TextView name;
    public FolderHolder(@NonNull View itemView) {
        super(itemView);
        imageView=itemView.findViewById(R.id.thumbnail);
        name=itemView.findViewById(R.id.folder_name);
    }
}
