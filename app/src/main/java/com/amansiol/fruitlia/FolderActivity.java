package com.amansiol.fruitlia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import com.amansiol.fruitlia.adapter.FolderAdapter;
import com.amansiol.fruitlia.model.Folder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class FolderActivity extends AppCompatActivity {

    ArrayList<String> pathlist=new ArrayList<>();
    ArrayList<Folder> FolderList=new ArrayList<>();
    RecyclerView folderrecycler;
    FolderAdapter folderAdapter;
    HashMap<String, ArrayList<String>> folder = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);
        folderrecycler = findViewById(R.id.folder);
        folderrecycler.setLayoutManager(new GridLayoutManager(this, 2));
        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + getResources().getResourcePackageName(R.drawable.camera)
                + '/' + getResources().getResourceTypeName(R.drawable.camera)
                + '/' + getResources().getResourceEntryName(R.drawable.camera));
        getImagePath();
        for (String x : pathlist) {
            File dir = new File(x);
            folder.put(dir.getParentFile().getName(), getAllImage(dir.getParent()));
        }
        for (HashMap.Entry<String, ArrayList<String>> set : folder.entrySet()) {
            FolderList.add(new Folder(set.getKey(),set.getValue()));
//            Log.d("Folder Name: ",set.getKey());
//            for(Photo x: set.getValue())
//                Log.d("Folder: ",x.getPath());
        }
        ArrayList<String> cameraimage = new ArrayList<>();
        cameraimage.add(imageUri.toString());
        FolderList.add(0,new Folder("Open Camera",cameraimage));
        folderAdapter=new FolderAdapter(FolderActivity.this,FolderList);
        folderrecycler.setAdapter(folderAdapter);
        folderAdapter.notifyDataSetChanged();

    }
    ArrayList<String> getAllImage(String path) {
        ArrayList<String> temp = new ArrayList<>();
        for (String x : pathlist) {
            if (new File(x).getParent().equals(path))
                temp.add(x);
        }
        return temp;
    }
    private void getImagePath() {
        // in this method we are adding all our image paths
        // in our arraylist which we have created.
        // on below line we are checking if the device is having an sd card or not.
        boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);

        if (isSDPresent) {

            // if the sd card is present we are creating a new list in
            // which we are getting our images data with their ids.
            final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};

            // on below line we are creating a new
            // string to order our images by string.
            final String orderBy = MediaStore.Images.Media._ID;

            // this method will stores all the images
            // from the gallery in Cursor
            Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);

            // below line is to get total number of images
            int count = cursor.getCount();

            // on below line we are running a loop to add
            // the image file path in our array list.
            for (int i = 0; i < count; i++) {

                // on below line we are moving our cursor position
                cursor.moveToPosition(i);

                // on below line we are getting image file path
                int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);

                // after that we are getting the image file path
                // and adding that path in our array list.
                pathlist.add(cursor.getString(dataColumnIndex));
            }
//            photoAdapter.notifyDataSetChanged();
            // after adding the data to our
            // array list we are closing our cursor.
            cursor.close();
        }
    }
}