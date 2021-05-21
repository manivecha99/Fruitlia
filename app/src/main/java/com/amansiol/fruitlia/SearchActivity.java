package com.amansiol.fruitlia;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.custom.FirebaseCustomLocalModel;
import com.google.firebase.ml.custom.FirebaseModelDataType;
import com.google.firebase.ml.custom.FirebaseModelInputOutputOptions;
import com.google.firebase.ml.custom.FirebaseModelInputs;
import com.google.firebase.ml.custom.FirebaseModelInterpreter;
import com.google.firebase.ml.custom.FirebaseModelInterpreterOptions;
import com.google.firebase.ml.custom.FirebaseModelOutputs;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.CAMERA;

public class SearchActivity extends AppCompatActivity {

    FirebaseCustomLocalModel localModel;
    FirebaseModelInterpreter interpreter;
    FirebaseModelInterpreterOptions options;
    FirebaseModelInputOutputOptions inputOutputOptions;
    FirebaseModelInputs inputs;
    ImageView previewImage;
    HashMap<String, Float> labelMap;
    final int width=224;
    final int height=224;
    final int labellen=23;
    TextView answer;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat
                .getColor(getApplicationContext(), R.color.black));
        setContentView(R.layout.activity_search);
        FirebaseApp.initializeApp(this);
        previewImage=findViewById(R.id.preview);
        answer=findViewById(R.id.result);
        progressBar=findViewById(R.id.progressbar);
        labelMap=new HashMap<>();
        localModel = new FirebaseCustomLocalModel.Builder()
                .setAssetFilePath("model_unquant.tflite")
                .build();
        try {
            options = new FirebaseModelInterpreterOptions.Builder(localModel).build();
            interpreter = FirebaseModelInterpreter.getInstance(options);
        } catch (FirebaseMLException e) {
            Log.d("TAG",""+e.getMessage());
        }
        Intent i = getIntent();
        if(i!=null)
        {
            Bitmap bmap = null;
            try {
                bmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(i.getStringExtra("path")));
                previewImage.setImageURI(Uri.parse(i.getStringExtra("path")));
            } catch (IOException e) {
                e.printStackTrace();
            }
            AnalysePic(bmap);
        }
    }
    private void AnalysePic(Bitmap bitmap) {
        if(bitmap!=null)
        {
            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
            int batchNum = 0;
            float[][][][] input = new float[1][width][height][3];
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int pixel = bitmap.getPixel(x, y);
                    // Normalize channel values to [-1.0, 1.0]. This requirement varies by
                    // model. For example, some models might require values to be normalized
                    // to the range [0.0, 1.0] instead.
                    input[batchNum][x][y][0] = (Color.red(pixel) - 127) / 128.0f;
                    input[batchNum][x][y][1] = (Color.green(pixel) - 127) / 128.0f;
                    input[batchNum][x][y][2] = (Color.blue(pixel) - 127) / 128.0f;
                }
            }
            try {
                inputOutputOptions = new FirebaseModelInputOutputOptions.Builder()
                        .setInputFormat(0, FirebaseModelDataType.FLOAT32, new int[]{1, width, height, 3})
                        .setOutputFormat(0, FirebaseModelDataType.FLOAT32, new int[]{1, labellen})
                        .build();
            } catch (FirebaseMLException e) {
                e.printStackTrace();
                Log.i("MLKit", "1"+e.getMessage());
            }

            try {
                inputs = new FirebaseModelInputs.Builder()
                        .add(input)  // add() as many input arrays as your model requires
                        .build();
            } catch (FirebaseMLException e) {
                e.printStackTrace();
                Log.i("MLKit", "2"+e.getMessage());
            }

            interpreter.run(inputs, inputOutputOptions)
                    .addOnSuccessListener(
                            new OnSuccessListener<FirebaseModelOutputs>() {
                                @Override
                                public void onSuccess(FirebaseModelOutputs result) {
                                    // ...
                                    float[][] output = result.getOutput(0);
                                    float[] probabilities = output[0];
                                    BufferedReader reader = null;
                                    try {
                                        reader = new BufferedReader(
                                                new InputStreamReader(getAssets().open("labels.txt")));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        Log.i("MLKit", "3"+e.getMessage());
                                    }
                                    for (int i = 0; i < probabilities.length; i++) {
                                        String label = null;
                                        try {
                                            label = reader.readLine();
                                            String[]  labels=label.split(" ",2);
                                            label=labels[1].trim();
                                            labelMap.put(label,probabilities[i]*100);

                                        } catch (IOException e) {
                                            e.printStackTrace();
                                            Log.i("MLKit", "4"+e.getMessage());
                                        }
                                        Log.i("MLKit", String.format("%s: %1.4f", label, probabilities[i]*100));
                                    }
                                    if(labelMap.size()>0)
                                    {
                                        labelMap=sortHashMapByValues(labelMap);
                                    }
                                    Map.Entry<String, Float>[] temp = new Map.Entry[labelMap.size()];
                                    labelMap.entrySet().toArray(temp);
                                    Log.d("Map",temp[labelMap.size()-1].getKey()+" "+temp[labelMap.size()-1].getValue());
                                    Log.d("Map",temp[labelMap.size()-2].getKey()+" "+temp[labelMap.size()-2].getValue());
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressBar.setVisibility(View.GONE);
                                            answer.setText(temp[labelMap.size()-1].getKey());
                                        }
                                    },2000);


                                }

                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Task failed with an exception
                                    // ...
                                    Log.i("MLKit", "5"+e.getMessage());
                                }
                            });
        }
    }



    public LinkedHashMap<String, Float> sortHashMapByValues(
            HashMap<String, Float> passedMap) {
        List<String> mapKeys = new ArrayList<String>(passedMap.keySet());
        List<Float> mapValues = new ArrayList<Float>(passedMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);

        LinkedHashMap<String, Float> sortedMap =
                new LinkedHashMap<>();

        Iterator<Float> valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            Float val = valueIt.next();
            Iterator<String> keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                String key = keyIt.next();
                Float comp1 = passedMap.get(key);
                Float comp2 = val;

                if (comp1.equals(comp2)) {
                    keyIt.remove();
                    sortedMap.put(key, val);
                    break;
                }
            }
        }
        return sortedMap;
    }
}