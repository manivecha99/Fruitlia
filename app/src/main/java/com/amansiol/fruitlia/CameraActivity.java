package com.amansiol.fruitlia;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.util.concurrent.ListenableFuture;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageActivity;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CameraActivity extends AppCompatActivity {

    private PreviewView previewView;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private  String TAG = "CameraXBasic";
    private  String FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS";
    ExecutorService cameraExecutor;
    File outputDirectory;
    ImageCapture imageCapture;
    ImageView showimage;
    FloatingActionButton capture;
    FloatingActionButton retake;
    FloatingActionButton go;
    Uri savedUri=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        showimage=findViewById(R.id.showimage);
        retake=findViewById(R.id.retake);
        go = findViewById(R.id.go);
        previewView = findViewById(R.id.viewFinder);
        capture=findViewById(R.id.camera_capture_button);
        outputDirectory=getOutputDirectory();
        cameraExecutor= Executors.newSingleThreadExecutor();
        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });
        retake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCamera();
                showimage.setVisibility(View.GONE);
                capture.setVisibility(View.VISIBLE);
                previewView.setVisibility(View.VISIBLE);
                retake.setVisibility(View.GONE);
                go.setVisibility(View.GONE);
            }
        });
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(savedUri!=null){
                    CropImage.activity(savedUri)
                            .setAspectRatio(1,1)
                            .setFixAspectRatio(true)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAllowCounterRotation(true)
                            .setOutputCompressQuality(50)
                            .start(CameraActivity.this);
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        startCamera();
    }

    private void takePhoto() {
        File photoFile= new File(outputDirectory, new SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis()) +".jpg");
        ImageCapture.OutputFileOptions outputOptions= new ImageCapture.OutputFileOptions.Builder(photoFile).build();
        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(this), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {

                savedUri = Uri.fromFile(photoFile);
                cameraExecutor.shutdown();
                showimage.setVisibility(View.VISIBLE);
                showimage.setImageURI(savedUri);
                capture.setVisibility(View.GONE);
                previewView.setVisibility(View.GONE);
                retake.setVisibility(View.VISIBLE);
                go.setVisibility(View.VISIBLE);


            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                    exception.printStackTrace();
            }
        });
    }

    private void startCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                ProcessCameraProvider cameraProvider = null;
                try {
                    cameraProvider = cameraProviderFuture.get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Preview preview= new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());
                CameraSelector cameraSelector =CameraSelector.DEFAULT_BACK_CAMERA;
                imageCapture= new ImageCapture.Builder().build();
                try{
                    cameraProvider.unbindAll();
                    cameraProvider.bindToLifecycle(CameraActivity.this, cameraSelector, preview,imageCapture);
                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        }, ContextCompat.getMainExecutor(this));
    }


    private File getOutputDirectory() {

        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return storageDir;

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                savedUri= result.getUri();
                Intent intent = new Intent(CameraActivity.this,SearchActivity.class);
                intent.putExtra("path",savedUri.toString());
                Log.d("path",savedUri.toString());
                startActivity(intent);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraExecutor.shutdown();
    }
}