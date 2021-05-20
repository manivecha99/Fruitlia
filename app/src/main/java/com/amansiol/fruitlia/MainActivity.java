package com.amansiol.fruitlia;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.amansiol.fruitlia.adapter.FruitAdapter;
import com.amansiol.fruitlia.model.Fruit;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static android.Manifest.permission.CAMERA;

public class MainActivity extends AppCompatActivity {

    String[] fruitsNames={"Mango","Apple","Papaya","Kiwi"};
    int[] calories={66,52,119,61};
    String[] nutrients={"3 g fiber","24 g of sugar","1 g protein","257 mg potassium"};
    int[] images={R.drawable.mango,R.drawable.apple,R.drawable.papaya,R.drawable.kiwi};
    RecyclerView recentSearchRecycler;
    FruitAdapter fruitAdapter;
    ArrayList<Fruit> fruits;
    FloatingActionButton openCamera;
    private final int requestPermissionCode=779;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recentSearchRecycler=findViewById(R.id.recent_search_recycler);
        openCamera=findViewById(R.id.camera);
        recentSearchRecycler.setLayoutManager(new LinearLayoutManager(this));
        fruits=new ArrayList<>();
        for(int i=0;i<fruitsNames.length;i++)
        {
            Fruit temp=new Fruit(fruitsNames[i],calories[i],nutrients,images[i]);
            fruits.add(temp);
        }
        fruitAdapter=new FruitAdapter(this,fruits);
        recentSearchRecycler.setAdapter(fruitAdapter);
        fruitAdapter.notifyDataSetChanged();

        // for taking photo from gallery
        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,FolderActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if(CheckingPermissionIsEnabledOrNot())
            {
                Toast.makeText(this,"All Permission Granted",Toast.LENGTH_SHORT).show();

            }
            // If, If permission is not enabled then else condition will execute.
            else {
                //Calling method to enable permission.
                RequestMultiplePermission();
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void RequestMultiplePermission() {

        // Creating String Array with Permissions.
        ActivityCompat.requestPermissions(MainActivity.this, new String[]
                {
                        CAMERA
                        , Manifest.permission.READ_EXTERNAL_STORAGE
                        ,Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ,Manifest.permission.ACCESS_MEDIA_LOCATION

                }, requestPermissionCode);

    }
    // Calling override method.
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == requestPermissionCode) {
            if (grantResults.length > 0) {

                boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean ReadStoragePermisssion = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                boolean WriteStoragePermisssion = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                boolean AccessMediaLocation = grantResults[3]==PackageManager.PERMISSION_GRANTED;

                if (AccessMediaLocation && CameraPermission && ReadStoragePermisssion || WriteStoragePermisssion) {
//                    Toast.makeText(this,"All Permission Granted",Toast.LENGTH_SHORT).show();
                    Log.d("Permission","All Permission Granted");
                } else {
                    showSettingsDialog();

                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public boolean CheckingPermissionIsEnabledOrNot() {

        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int FourthPermissionResult=ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_MEDIA_LOCATION);

        return  FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED &&
                FourthPermissionResult==PackageManager.PERMISSION_GRANTED;
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Grant Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings. ");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                MainActivity.this.openSettings();
            }
        });
        builder.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();

    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }
}