package com.comp4521gp01.transposeapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by waimancheung on 6/4/2017.
 */

public class TransposeActivity extends Activity {
    private TextView textView;
    private ImageView imageView;
    private Button transpose_up, transpose_down, confirm_trans;
    private TessBaseAPI tessBaseAPI;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transpose);
        Intent intent = getIntent();

        transpose_up = (Button) findViewById(R.id.transpose_up);
        transpose_down = (Button) findViewById(R.id.transpose_down);
        confirm_trans = (Button) findViewById(R.id.confirm_trans);
        imageView = (ImageView) findViewById(R.id.show_image);

        readImage();
    }

    private void readImage() {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(),R.drawable.edelweiss);
        imageView.setImageBitmap(bmp);

        TessBaseAPI baseApi = new TessBaseAPI();
        baseApi.init(Environment.getExternalStorageDirectory().toString() + "/TransposeApp", "eng");
        baseApi.setPageSegMode(TessBaseAPI.PageSegMode.PSM_SPARSE_TEXT_OSD);
        baseApi.setImage(bmp);
        String outputText = baseApi.getUTF8Text();
        textView.setText(outputText);
    }
    /*
    private List<String> list, chordsList;
    private TextView textView;
    private ImageView imageView;
    private Button transpose_up,transpose_down,confirm_trans;
    private TessBaseAPI tessBaseApi;
    String result = "empty";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transpose);
        Intent intent = getIntent();

        list  = Arrays.asList("C","C#","D","D#","E","F","F#","G","G#","A","A#","B");
        chordsList = new ArrayList<String>();
        textView = (TextView) findViewById(R.id.textview);
        transpose_up = (Button) findViewById(R.id.transpose_up);
        transpose_down = (Button) findViewById(R.id.transpose_down);
        confirm_trans = (Button) findViewById(R.id.confirm_trans);
        imageView = (ImageView) findViewById(R.id.imageView2);

        transpose_up.setOnClickListener(clickListener);
        transpose_down.setOnClickListener(clickListener);
        confirm_trans.setOnClickListener(clickListener);

        //scanImage();
        //startOCR();
        readIm();
    }

    private void readIm() {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(),R.drawable.kite);
        imageView.setImageBitmap(bmp);

        TessBaseAPI baseApi = new TessBaseAPI();
        baseApi.init(Environment.getExternalStorageDirectory().toString() + "/TransposeApp", "eng");
        baseApi.setPageSegMode(TessBaseAPI.PageSegMode.PSM_SINGLE_LINE);
        baseApi.setImage(bmp);
        String outputText = baseApi.getUTF8Text();
        textView.setText(outputText);
    }

    private void scanImage() {
        String chordsText = textView.getText().toString();
        for(int i = 0; i < textView.getText().length(); i++){
            chordsList.add("" + chordsText.charAt(i));
        }
    }

    private void startOCR() {
        String TESS_PATH = Environment.getExternalStorageDirectory().toString() + "/TransposeApp/tessdata/";

        try {
            String fileList[] = getAssets().list("tessdata");

            for (String fileName : fileList) {

                // open file within the assets folder
                // if it is not already there copy it to the sdcard
                String pathToDataFile = TESS_PATH + fileName;
                if (!(new File(pathToDataFile)).exists()) {

                    InputStream in = getAssets().open("tessdata" + "/" + fileName);

                    OutputStream out = new FileOutputStream(pathToDataFile);

                    // Transfer bytes from in to out
                    byte[] buf = new byte[1024];
                    int len;

                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    in.close();
                    out.close();

                    Log.d(TAG, "Copied " + fileName + "to tessdata");
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Unable to copy files to tessdata " + e.toString());
        }


        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4; // 1 - means max size. 4 - means maxsize/4 size. Don't use value <4, because you need more memory in the heap to store your data.
            Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath()+"/TransposeApp/imgs/temp.jpg",options);

            result = extractText(bitmap);

            textView.setText(result);

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }


    private String extractText(Bitmap bitmap) {
        try {
            tessBaseApi = new TessBaseAPI();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            if (tessBaseApi == null) {
                Log.e(TAG, "TessBaseAPI is null. TessFactory not returning tess object.");
            }
        }

        tessBaseApi.init(Environment.getExternalStorageDirectory().toString(), "eng");
        tessBaseApi.setPageSegMode(TessBaseAPI.PageSegMode.PSM_SINGLE_LINE);

        tessBaseApi.setImage(bitmap);
        String extractedText = "empty result";
        try {
            extractedText = tessBaseApi.getUTF8Text();
        } catch (Exception e) {
            Log.e(TAG, "Error in recognizing text.");
        }
        tessBaseApi.end();
        return extractedText;
    }

    private View.OnClickListener clickListener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.transpose_up:
                    transpose("U", chordsList.size());
                    changeText();
                    break;

                case R.id.transpose_down:
                    transpose("D", chordsList.size());
                    changeText();
                    break;

                case R.id.confirm_trans:
                    break;
            }
        }
    };

    public void transpose(String k, int i) {
        if(k == "U") {
            if (i > 0) {transpose("U", i--);}

            if (chordsList.get(0) != " ") {
                int chordNum = list.indexOf(chordsList.get(0)) + 1;
                if (chordNum == 12) chordNum = 0;
                chordsList.remove(0);
                chordsList.add(list.get(chordNum));
            } else {
                chordsList.remove(0);
                chordsList.add(" ");
            }
        }else {
            if (i > 0) {transpose("D", i--);}

            if (chordsList.get(0) != " ") {
                int chordNum = list.indexOf(chordsList.get(0)) - 1;
                if (chordNum == -1) chordNum = 11;
                chordsList.remove(0);
                chordsList.add(list.get(chordNum));
            } else {
                chordsList.remove(0);
                chordsList.add(" ");
            }
        }
    }

    private void changeText() {
        String chordstring = null;
        for(int i = 0; i < chordsList.size(); i++){
            chordstring += chordsList.get(i);
        }
        textView.setText(chordstring);
    }

*/
}
