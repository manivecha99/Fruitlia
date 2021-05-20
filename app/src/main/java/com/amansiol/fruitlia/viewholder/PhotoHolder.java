package com.amansiol.fruitlia.viewholder;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.amansiol.fruitlia.R;

public class PhotoHolder extends RecyclerView.ViewHolder {
    public ImageView imageView;
    public PhotoHolder(@NonNull View itemView) {
        super(itemView);
        imageView=itemView.findViewById(R.id.photo);
    }
}
