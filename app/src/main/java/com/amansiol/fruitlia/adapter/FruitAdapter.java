package com.amansiol.fruitlia.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amansiol.fruitlia.R;
import com.amansiol.fruitlia.model.Fruit;
import com.amansiol.fruitlia.viewholder.FruitHolder;

import java.util.ArrayList;

public class FruitAdapter extends RecyclerView.Adapter<FruitHolder> {

    ArrayList<Fruit> fruits;
    Context context;

    public FruitAdapter(Context context, ArrayList<Fruit> fruits) {
        this.fruits = fruits;
        this.context = context;
    }

    @NonNull
    @Override
    public FruitHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recent_search_row, parent, false);
        return new FruitHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(FruitHolder holder, int position) {
        holder.name.setText(fruits.get(position).getName());
        holder.cal.setText(String.format("%d Cal per 100 g",fruits.get(position).getCal()));
        holder.fruit_image.setImageResource(fruits.get(position).getImageid());
        StringBuffer stringBuffer=new StringBuffer();
        for(String s : fruits.get(position).getNutrients())
        {
            stringBuffer.append(String.format("~ %s.\n",s));
        }
        holder.nutrients.setText(stringBuffer);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,fruits.get(position).getName(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return fruits.size();
    }
}
