package com.amansiol.fruitlia.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amansiol.fruitlia.R;

public class FruitHolder extends RecyclerView.ViewHolder {
    public TextView name;
    public TextView cal;
    public TextView nutrients;
    public ImageView fruit_image;
    public FruitHolder(View itemView) {
        super(itemView);
        name=itemView.findViewById(R.id.fruit_name);
        cal=itemView.findViewById(R.id.fruit_calorie);
        nutrients=itemView.findViewById(R.id.fruit_nutrients);
        fruit_image=itemView.findViewById(R.id.fruit_image);
    }
}
