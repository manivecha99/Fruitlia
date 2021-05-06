package com.amansiol.fruitlia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.amansiol.fruitlia.adapter.FruitAdapter;
import com.amansiol.fruitlia.model.Fruit;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String[] fruitsNames={"Mango","Apple","Papaya","Kiwi"};
    int[] calories={66,52,119,61};
    String[] nutrients={"3 g fiber","24 g of sugar","1 g protein","257 mg potassium"};
    int[] images={R.drawable.mango,R.drawable.apple,R.drawable.papaya,R.drawable.kiwi};
    RecyclerView recentSearchRecycler;
    FruitAdapter fruitAdapter;
    ArrayList<Fruit> fruits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recentSearchRecycler=findViewById(R.id.recent_search_recycler);
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
    }
}