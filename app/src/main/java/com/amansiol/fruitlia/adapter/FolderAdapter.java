package com.amansiol.fruitlia.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amansiol.fruitlia.CameraActivity;
import com.amansiol.fruitlia.PhotoActivity;
import com.amansiol.fruitlia.R;
import com.amansiol.fruitlia.model.Folder;
import com.amansiol.fruitlia.viewholder.FolderHolder;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class FolderAdapter extends RecyclerView.Adapter<FolderHolder> {
    Context context;
    ArrayList<Folder> pathlist;

    public FolderAdapter(Context context, ArrayList<Folder> pathlist) {
        this.context = context;
        this.pathlist = pathlist;
    }

    @NonNull
    @Override
    public FolderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.folder_row, parent, false);
        return new FolderHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderHolder holder, int position) {
        File imgFile = new File(pathlist.get(position).getFilepaths().get(0));
        Log.d("ImageFile", imgFile.toString());
        if (position == 0) {
            holder.imageView.setImageResource(R.drawable.camera);
            holder.name.setText("Open Camera");
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, CameraActivity.class));
                }
            });
        } else {
            // on below line we are checking if tje file exists or not.
            if (imgFile.exists()) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(imgFile.getAbsolutePath(), options);
                int imageHeight = options.outHeight;
                int imageWidth = options.outWidth;
                Log.d("Space", imgFile.length() + "");
                float mb = 1024 * 1024;
                float ratio = imageWidth / imageHeight;
                if (imgFile.length() > (mb / 2)) {
                    imageHeight *= 0.10;
                    imageWidth *= (0.10 * ratio);
                    Picasso.get().load(imgFile).placeholder(R.drawable.ispinner).resize(imageWidth, imageHeight).into(holder.imageView);
                } else {
                    Picasso.get().load(imgFile).placeholder(R.drawable.ispinner).into(holder.imageView);
                }

                holder.name.setText(pathlist.get(position).getFolderName());
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PhotoActivity.class);
                    intent.putStringArrayListExtra("pathlist", pathlist.get(position).getFilepaths());
                    context.startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return pathlist.size();
    }

}
