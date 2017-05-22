package com.comp4521gp01.transposeapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.design.internal.BottomNavigationItemView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        //textView.append("\n\n\n Key Flag: " + key_flag +"\n");
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
                    saveDialog();
                    break;
            }
        }
    };

    private void saveDialog() {
        AlertDialog.Builder editDialog = new AlertDialog.Builder(TransposeActivity.this);
        editDialog.setTitle("Save Document");

        final EditText editText = new EditText(TransposeActivity.this);
        editDialog.setView(editText);

        editDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                dialog.cancel();
            }
        })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //EditText editText_trans = (EditText) findViewById(R.id.editText_trans);
                        Bitmap bitmap;
                        bitmap = Bitmap.createBitmap(textView.getWidth(), textView.getHeight(), Bitmap.Config.ARGB_8888);

                        Canvas c = new Canvas(bitmap);
                        textView.draw(c);

                        try {
                            String path = Environment.getExternalStorageDirectory().getPath()+"/TransposeApp/imgs";
                            File dir = new File(path);
                            if(!dir.exists())
                                dir.mkdirs();
                            File file = new File(dir, editText.getText().toString() + ".jpg");
                            FileOutputStream fOut = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, fOut);
                            fOut.flush();
                            fOut.close();

                            Toast.makeText(textView.getContext(), "Saved", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage());
                        }finally {
                            if(bitmap!=null) {
                                bitmap.recycle();
                            }
                        }
                        finish();
                    }
                })
                .show();
/*
        final AlertDialog builder = new AlertDialog.Builder(TransposeActivity.this)
                .setTitle("Save Document")
                .setView(R.layout.dialog_transpose)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editText_trans = (EditText) findViewById(R.id.editText_trans);
                        Bitmap bitmap;
                        bitmap = Bitmap.createBitmap(textView.getWidth(), textView.getHeight(), Bitmap.Config.ARGB_8888);


                        Canvas c = new Canvas(bitmap);
                        textView.draw(c);

                        try {
                            String path = Environment.getExternalStorageDirectory().getPath()+"/TransposeApp/imgs";
                            File dir = new File(path);
                            if(!dir.exists())
                                dir.mkdirs();
                            File file = new File(dir, editText_trans.getText().toString() + "1.jpg");
                            FileOutputStream fOut = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, fOut);
                            fOut.flush();
                            fOut.close();

                            Toast.makeText(textView.getContext(), "Saved", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage());
                        }finally {
                            if(bitmap!=null) {
                                bitmap.recycle();
                            }
                        }
                    }
                })
                .show();
*/
    }

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
}
