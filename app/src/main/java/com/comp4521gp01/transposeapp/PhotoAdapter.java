package com.comp4521gp01.transposeapp;

/**
 * Created by waimancheung on 22/5/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PhotoAdapter extends BaseAdapter {
    private Context ctx;
    private final String[] filesNames;
    private final String[] filesPaths;


    public PhotoAdapter(Context ctx, String[] filesNames, String[] filesPaths) {
        this.ctx = ctx;
        this.filesNames = filesNames;
        this.filesPaths = filesPaths;
    }

    @Override
    public int getCount() {
        return filesNames.length;
    }

    @Override
    public Object getItem(int pos) {
        return pos;
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int p, View convertView, ViewGroup parent) {
        View grid;
        ImageView imageView;
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            grid = inflater.inflate(R.layout.grid_item_main, null);

            TextView textView = (TextView) grid.findViewById(R.id.gridview_text);
            imageView = (ImageView)grid.findViewById(R.id.gridview_image);
            String fileName = filesNames[p];
            fileName = fileName.replace(".jpg", "");

            textView.setText(fileName);


            Bitmap bmp = BitmapFactory.decodeFile(filesPaths[p]);
            imageView.setImageBitmap(bmp);
        } else {
            grid = (View) convertView;
        }



        return grid;
    }
}