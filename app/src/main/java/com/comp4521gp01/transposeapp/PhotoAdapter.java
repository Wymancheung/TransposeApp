//# COMP 4521    #  CHEUNG, Wai Man Raymond   20199778   wmcheungaa@connect.ust.hk
//# COMP 4521    #  LAW, Chiu Kwan  20212087   cklawad@connect.ust.hk
//# COMP 4521    #  WONG, Ho Yin Calvin  20196726  hycwong@connect.ust.hk
package com.comp4521gp01.transposeapp;

/**
 * Created by waimancheung on 22/5/2017.
 */

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
        if(filesNames == null){
            return 0;
        }else {
            return filesNames.length;
        }
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
            int width = bmp.getWidth();
            int height = bmp.getHeight();

            float bitmapRatio = (float) width / (float) height;
            if (bitmapRatio > 1) {
                width = 500;
                height = (int) (width / bitmapRatio);
            } else {
                height = 500;
                width = (int) (height * bitmapRatio);
            }

            bmp = Bitmap.createScaledBitmap(bmp, width, height, true);

            //textView.setWidth(Resources.getSystem().getDisplayMetrics().widthPixels /2 - 10);
            //imageView.setMaxWidth(Resources.getSystem().getDisplayMetrics().widthPixels/2 - 10);
            imageView.setImageBitmap(bmp);
        } else {
            grid = (View) convertView;
        }



        return grid;
    }


}