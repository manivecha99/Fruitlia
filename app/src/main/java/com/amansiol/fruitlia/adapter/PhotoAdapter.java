package com.amansiol.fruitlia.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amansiol.fruitlia.R;
import com.amansiol.fruitlia.viewholder.PhotoHolder;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {

    ArrayList<String> pathlist;
    Context context;

    public PhotoAdapter(ArrayList<String> pathlist, Context context) {
        this.pathlist = pathlist;
        this.context = context;
    }

    @NonNull
    @Override
    public PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.photo_row,parent,false);
        return new PhotoHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoHolder holder, int position) {
        // on below line we are getting th file from the
        // path which we have stored in our list.
        File imgFile = new File(pathlist.get(position));
//        Log.d("ImageFile",imgFile.toString());
//        if(position==0){
//            holder.imageView.setImageResource(R.drawable.camera);
//            holder.imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    context.startActivity(new Intent(context, CameraActivity.class));
//                }
//            });
//        }
        // on below line we are checking if tje file exists or not.
        if (imgFile.exists()) {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(imgFile.getAbsolutePath(), options);
            int imageHeight = options.outHeight;
            int imageWidth = options.outWidth;
            Log.d("Space",imgFile.length()+"");
            float mb=1024*1024;
            float ratio = imageWidth/imageHeight;
           if(imgFile.length()>(mb/2)){
               imageHeight*=0.25;
               imageWidth*=(0.25* ratio);
               Picasso.get().load(imgFile).placeholder(R.drawable.ispinner).resize(imageWidth,imageHeight).into(holder.imageView);
           }else {
               Picasso.get().load(imgFile).placeholder(R.drawable.ispinner).into(holder.imageView);
           }

        }
    }

    @Override
    public int getItemCount() {
        return pathlist.size();
    }
}
