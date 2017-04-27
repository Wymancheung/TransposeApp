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
import java.lang.reflect.Array;
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
    private String chordText;
    private List<String> sharpList, flatList;
    private int key_flag = 0; // 1: sharp; 2: flat
    private  ArrayList<Integer> chordspace = new ArrayList<Integer>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transpose);
        Intent intent = getIntent();
        chordText = intent.getStringExtra(CalibrateActivity.EXTRA_MESSAGE);

        transpose_up = (Button) findViewById(R.id.transpose_up);
        transpose_down = (Button) findViewById(R.id.transpose_down);
        confirm_trans = (Button) findViewById(R.id.confirm_trans);
        imageView = (ImageView) findViewById(R.id.imageView_trans);
        textView = (TextView) findViewById(R.id.textView_trans);
        textView.setText(chordText);


        sharpList  = Arrays.asList("C","C#","D","D#","E","F","F#","G","G#","A","A#","B");
        flatList = Arrays.asList("C","Db","D","Eb","E","F","Gb","G","Ab","A","Bb","B");

        transpose_up.setOnClickListener(clickListener);
        transpose_down.setOnClickListener(clickListener);
        confirm_trans.setOnClickListener(clickListener);

        for(int i = 0; i < chordText.length(); i++){
            if(chordText.charAt(i) == '#'){
                key_flag = 1;
                break;
            }else if(chordText.charAt(i) == 'b'){
                key_flag = 2;
                break;
            }else{
                key_flag = 1;
            }
        }
        textView.append("\n\n\n Key Flag: " + key_flag +"\n");
    }

    private View.OnClickListener clickListener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.transpose_up:
                    transposeUp();
                    break;
                case R.id.transpose_down:
                    transposeDown();
                    break;
                case R.id.confirm_trans:
                    break;
            }
        }
    };

    private void transposeUp() {
        chordText = textView.getText().toString();
        for(int i = 0; i < chordText.length(); i++){
            if(isChord(i) != -99){
                textView.append("\n" + getNote(i)+" ");//////////
                if(key_flag == 1){
                    for(int j = 0; j < sharpList.size(); j++){
                        if(sharpList.get(j).equals(getNote(i))){
                            chordspace.add(i);
                            chordspace.add(j);
                            //textView.append("\n" + getNote(i)+" ");
                        }
                    }
                }else if(key_flag == 2){
                    for(int j = 0; j < flatList.size(); j++){
                        if(flatList.get(j).equals(getNote(i))){
                            chordspace.add(i);
                            chordspace.add(j);
                            //textView.append("\n" + getNote(i)+" ");
                        }
                    }
                }
            }
        }
        int s = chordspace.size();
        textView.append("\n chordspace.size = " + chordspace.size()+" ");
        for(int i = 0; i < s; i=i+2) {
            int location = chordspace.get(chordspace.size() - 2);
            int note = chordspace.get(chordspace.size() - 1);
            String front = "";
            String back = "";

            if (note == 1 || note == 3 || note == 6 || note == 8 || note == 10) {
                if(location != 0) {
                    front = chordText.substring(0, location);
                }
                back = chordText.substring(location + 2);
            } else {
                if(location != 0) {
                    front = chordText.substring(0, location);
                }
                back = chordText.substring(location + 1);
            }

            if (key_flag == 1) {
                if (note == 11) {
                    back = sharpList.get(0) + back;
                } else {
                    back = sharpList.get(note + 1) + back;
                }
                chordText = front + back;
            } else {
                if (note == 11) {
                    back = flatList.get(0) + back;
                } else {
                    back = flatList.get(note + 1) + back;
                }
                chordText = front + back;
            }
            chordspace.remove(chordspace.size() - 1);
            chordspace.remove(chordspace.size() - 1);
        }

        textView.setText(chordText);
    }

    private void transposeDown() {
        chordText = textView.getText().toString();
        for(int i = 0; i < chordText.length(); i++){
            if(isChord(i) != -99){
                if(key_flag == 1){
                    for(int j = 0; j < sharpList.size(); j++){
                        if(sharpList.get(j).equals(getNote(i))){
                        //if(sharpList.get(j) == getNote(i)){
                            chordspace.add(i);
                            chordspace.add(j);
                            //textView.append("\n" + getNote(i)+" ");
                        }
                    }
                }else if(key_flag == 2){
                    for(int j = 0; j < flatList.size(); j++){
                        if(flatList.get(j).equals(getNote(i))){
                        //if(flatList.get(j) == getNote(i)){
                            chordspace.add(i);
                            chordspace.add(j);
                            //textView.append("\n" + getNote(i) +" ");
                        }
                    }
                }
            }
        }
        int s = chordspace.size();
        textView.append("\n chordspace.size = " + chordspace.size()+" ");
        for(int i = 0; i < s; i=i+2) {
            int location = chordspace.get(chordspace.size() - 2);
            int note = chordspace.get(chordspace.size() - 1);
            String front = "";
            String back = "";

            if (note == 1 || note == 3 || note == 6 || note == 8 || note == 10) {
                if(location != 0) {
                    front = chordText.substring(0, location);
                }
                back = chordText.substring(location + 2);
            } else {
                if(location != 0) {
                    front = chordText.substring(0, location);
                }
                back = chordText.substring(location + 1);
            }

            if (key_flag == 1) {
                if (note == 0) {
                    back = sharpList.get(11) + back;
                } else {
                    back = sharpList.get(note - 1) + back;
                }
                chordText = front + back;
            } else {
                if (note == 0) {
                    back = flatList.get(11) + back;
                } else {
                    back = flatList.get(note - 1) + back;
                }
                chordText = front + back;
            }
            chordspace.remove(chordspace.size() - 1);
            chordspace.remove(chordspace.size() - 1);
        }

        textView.setText(chordText);
    }

    private String getNote(int i) {
        String note;
        if(chordText.length() > i+1) {
            if (chordText.charAt(i + 1) == 'b' || chordText.charAt(i + 1) == '#') {
                note = chordText.charAt(i) + "" + chordText.charAt(i + 1);
                return note;
            } else {
                note = chordText.charAt(i) + "";
                return note;
            }
        }else{
            note = chordText.charAt(i) + "";
            return note;
        }
    }


    private int isChord(int i){
        if(chordText.charAt(i) == 'A' || chordText.charAt(i) == 'B' || chordText.charAt(i) == 'C' || chordText.charAt(i) == 'D' || chordText.charAt(i) == 'E' || chordText.charAt(i) == 'F' || chordText.charAt(i) == 'G'){
            if(chordText.length() > i+1){
                if(chordText.charAt(i+1) == 'b'){
                    if(chordText.length() > i+2){
                        if(chordText.charAt(i+2) == 'm'){
                            if(chordText.length() > i+3){
                                if(chordText.charAt(i+3) == '7'){
                                    return i;
                                }else{
                                    return i;
                                }
                            }else{
                                return i;
                            }
                        }else if(chordText.charAt(i+2) == '7'){
                            return i;
                        }else{
                            return i;
                        }
                    }else{
                        return i;
                    }
                }else if(chordText.charAt(i+1) == '#'){
                    if(chordText.length() > i+2){
                        if(chordText.charAt(i+2) == 'm'){
                            if(chordText.length() > i+3){
                                if(chordText.charAt(i+3) == '7'){
                                    return i;
                                }else{
                                    return i;
                                }
                            }else{
                                return i;
                            }
                        }else if(chordText.charAt(i+2) == '7'){
                            return i;
                        }else{
                            return i;
                        }
                    }else{
                        return i;
                    }
                }else if(chordText.charAt(i+1) == 'm'){
                    if(chordText.length() > i+2){
                        if(chordText.charAt(i+2) == '7'){
                            return i;
                        }else{
                            return i;
                        }
                    }else{
                        return i;
                    }
                }else if(chordText.charAt(i+1) == '7') {
                    return i + 1;
                }else if(chordText.charAt(i+1) == ' '){
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
