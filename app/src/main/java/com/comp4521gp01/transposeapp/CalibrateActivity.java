package com.comp4521gp01.transposeapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.util.ArrayList;

/**
 * Created by waimancheung on 25/4/2017.
 */

public class CalibrateActivity extends Activity{
    public static final String EXTRA_MESSAGE = "calibrate.MESSAGE";
    private EditText editText_cali;
    private ImageView imageView;
    private Button refresh_cali, confirm_cali;
    private String chordText;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibrate);
        Intent intent = getIntent();

        editText_cali = (EditText) findViewById(R.id.editText_cali);
        imageView = (ImageView) findViewById(R.id.imageView_cali);
        refresh_cali = (Button) findViewById(R.id.refresh_cali);
        confirm_cali = (Button) findViewById(R.id.confirm_cali);

        refresh_cali.setOnClickListener(clickListener);
        confirm_cali.setOnClickListener(clickListener);

        readImage();
    }

    private View.OnClickListener clickListener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.refresh_cali:
                    renewSpacing();
                    break;
                case R.id.confirm_cali:
                    Intent intent = new Intent(CalibrateActivity.this, TransposeActivity.class);
                    String message = chordText;
                    intent.putExtra(EXTRA_MESSAGE, message);
                    startActivity(intent);
                    break;
            }
        }
    };

    private void renewSpacing() {
        chordText = editText_cali.getText().toString();
        ArrayList<Integer> chordspace = new ArrayList<Integer>();
        for(int i = 0; i < chordText.length(); i++){
            if(isChord(i) != -99){
                    chordspace.add(isChord(i));
            }
        }

        editText_cali.append("\n\n\n");

        //editText_cali.append(chordspace.size() + "\n");
        //editText_cali.append(chordspace.get(chordspace.size()-1) + "\n");
        //editText_cali.append(chordText.charAt(chordspace.get(chordspace.size()-1)) + "\n");

        int s = chordspace.size();
        for(int i = 0; i < s; i++){
            if(chordText.charAt(chordspace.get(chordspace.size()-1)+1) != ' ') {
                String front = chordText.substring(0, chordspace.get(chordspace.size() - 1) + 1);
                String back = " " + chordText.substring(chordspace.get(chordspace.size() - 1) + 1);
                chordText = front + back;
            }
            chordspace.remove(chordspace.size() - 1);
        }
        editText_cali.setText(chordText);
    }

    //B, Eb, C7, C/Bb, C#, C#m, Cm, Csus4, C#sus2, Dadd2

    private int isChord(int i){
        if(chordText.charAt(i) == 'A' || chordText.charAt(i) == 'B' || chordText.charAt(i) == 'C' || chordText.charAt(i) == 'D' || chordText.charAt(i) == 'E' || chordText.charAt(i) == 'F' || chordText.charAt(i) == 'G'){
            if(chordText.length() > i+1){
                if(chordText.charAt(i+1) == 'b'){
                    if(chordText.length() > i+2){
                        if(chordText.charAt(i+2) == 'm'){
                            if(chordText.length() > i+3){
                                if(chordText.charAt(i+3) == '7'){
                                    return i+3;
                                }else{
                                    return i+2;
                                }
                            }else{
                                return i+2;
                            }
                        }else if(chordText.charAt(i+2) == '7'){
                            return i+2;
                        }else{
                            return i+1;
                        }
                    }else{
                        return i+1;
                    }
                }else if(chordText.charAt(i+1) == '#'){
                    if(chordText.length() > i+2){
                        if(chordText.charAt(i+2) == 'm'){
                            if(chordText.length() > i+3){
                                if(chordText.charAt(i+3) == '7'){
                                    return i+3;
                                }else{
                                    return i+2;
                                }
                            }else{
                                return i+2;
                            }
                        }else if(chordText.charAt(i+2) == '7'){
                            return i+2;
                        }else{
                            return i+1;
                        }
                    }else{
                        return i+1;
                    }
                }else if(chordText.charAt(i+1) == 'm'){
                    if(chordText.length() > i+2){
                        if(chordText.charAt(i+2) == '7'){
                            return i+2;
                        }else{
                            return i+1;
                        }
                    }else{
                        return i+1;
                    }
                }else if(chordText.charAt(i+1) == '7'){
                    return i+1;
                }else if(chordText.charAt(i+1) == '|' || chordText.charAt(i+1) == 'A' || chordText.charAt(i+1) == 'B' || chordText.charAt(i+1) == 'C' || chordText.charAt(i+1) == 'D' || chordText.charAt(i+1) == 'E' || chordText.charAt(i+1) == 'F' || chordText.charAt(i+1) == 'G') {
                    return i;
                }else{
                    return -99;
                }
            }else{
                return i;
            }
        }else{
            return -99;
        }
    }

    private void readImage() {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(),R.drawable.edelweiss);
        imageView.setImageBitmap(bmp);

        TessBaseAPI baseApi = new TessBaseAPI();
        baseApi.init(Environment.getExternalStorageDirectory().toString() + "/TransposeApp", "eng");
        baseApi.setPageSegMode(TessBaseAPI.PageSegMode.PSM_SPARSE_TEXT_OSD);
        baseApi.setImage(bmp);
        String outputText = baseApi.getUTF8Text();
        chordText = outputText.replace("\n", "").replace("\r", "");

        editText_cali.setText(chordText);
    }
}
