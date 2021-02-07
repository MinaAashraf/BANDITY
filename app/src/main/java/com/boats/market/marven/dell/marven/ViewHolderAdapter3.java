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

public class ViewHolderAdapter3 extends RecyclerView.Adapter<ViewHolderAdapter3.ViewHolder> {
    Context context;
    ArrayList<String> colors = new ArrayList<>();
    int index = 0;
    Typeface typeface;

    public ViewHolderAdapter3(Context context, ArrayList<String> colors,int index) {
        this.context = context;
        this.colors = colors;
        this.index = index;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.size_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        typeface = Typeface.createFromAsset(context.getAssets(), "gotham.ttf");

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.sizeTxt.setText(colors.get(position));
        holder.sizeTxt.setTypeface(typeface);
        holder.sizeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = position;
                notifyDataSetChanged();
                if (index == position) {
                    holder.sizeTxt.setBackgroundResource(R.drawable.btn);
                    holder.sizeTxt.setTextColor(Color.parseColor("#ffffff"));
                }

            }
        });
        if (index == position) {
            holder.sizeTxt.setBackgroundResource(R.drawable.btn);
            holder.sizeTxt.setTextColor(Color.parseColor("#ffffff"));
        } else {
            holder.sizeTxt.setBackgroundResource(R.drawable.startshoping_style);
            holder.sizeTxt.setTextColor(Color.parseColor("#292561"));
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
        TextView sizeTxt;

        public ViewHolder(View itemView) {
            super(itemView);
            sizeTxt = itemView.findViewById(R.id.sizeText);
        }
    }

}
