package com.example.purrfecttask;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ColorPickerAdapter extends BaseAdapter {
    private int[] colors;
    private Context context;

    public ColorPickerAdapter(int[] colors) {
        this.colors = colors;
    }

    @Override
    public int getCount() {
        return colors.length;
    }

    @Override
    public Object getItem(int position) {
        return colors[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView colorImageView;

        if (convertView == null) {
            context = parent.getContext();
            colorImageView = new ImageView(context);
            colorImageView.setLayoutParams(new GridView.LayoutParams(120, 120));
            colorImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            colorImageView.setPadding(8, 8, 8, 8);
        } else {
            colorImageView = (ImageView) convertView;
        }

        int color = colors[position];
        colorImageView.setBackgroundColor(color);

        return colorImageView;
    }
}