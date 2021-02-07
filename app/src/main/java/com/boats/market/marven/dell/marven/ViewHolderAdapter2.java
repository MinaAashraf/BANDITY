package com.boats.market.marven.dell.marven;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dell on 7/25/2019.
 */

public class ViewHolderAdapter2 extends RecyclerView.Adapter<ViewHolderAdapter2.ViewHolder> {
    Context context;
    ArrayList<String> colors = new ArrayList<>();
    int index = 0;
    Typeface typeface;

    public ViewHolderAdapter2(Context context, ArrayList<String> colors, int index) {
        this.context = context;
        this.colors = colors;
        this.index = index;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.text_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        typeface = Typeface.createFromAsset(context.getAssets(), "gotham.ttf");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.colorTxt.setText(colors.get(position));
        holder.colorTxt.setTypeface(typeface);
        holder.colorTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = position;
                notifyDataSetChanged();
                if (index == position) {
                    holder.colorTxt.setBackgroundResource(R.drawable.btn);
                    holder.colorTxt.setTextColor(Color.parseColor("#ffffff"));
                }

            }
        });

        if (index == position) {
            holder.colorTxt.setBackgroundResource(R.drawable.btn);
            holder.colorTxt.setTextColor(Color.parseColor("#ffffff"));
        } else {
            holder.colorTxt.setBackgroundResource(R.drawable.startshoping_style);
            holder.colorTxt.setTextColor(Color.parseColor("#292561"));
        }


    }


    public int getIndex() {
        return index;
    }


    @Override
    public int getItemCount() {
        return colors.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView colorTxt;

        public ViewHolder(View itemView) {
            super(itemView);
            colorTxt = itemView.findViewById(R.id.colText);
        }
    }

}
