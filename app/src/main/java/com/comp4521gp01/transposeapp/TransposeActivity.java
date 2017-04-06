package com.comp4521gp01.transposeapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by waimancheung on 6/4/2017.
 */

public class TransposeActivity extends Activity {
    private List<String> list, chordsList;
    private TextView textView;
    private Button transpose_up,transpose_down,confirm_trans;

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

        scanImage();
    }

    private void scanImage() {
        String chordsText = textView.getText().toString();
        for(int i = 0; i < textView.getText().length(); i++){
            chordsList.add("" + chordsText.charAt(i));
        }
    }

    private View.OnClickListener clickListener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.transpose_up:
                    for(int i = 0; i < chordsList.size(); i++){
                        int chordNum = list.indexOf(chordsList.get(i)) + 1;
                        if(chordNum == 12){chordNum = 0;}
                        chordsList.remove(chordNum);
                        chordsList.add(i, list.get(chordNum));
                    }
                    break;
                case R.id.transpose_down:
                    for(int i = 0; i < chordsList.size(); i++){
                        int chordNum = list.indexOf(chordsList.get(i)) - 1;
                        if(chordNum == -1){chordNum = 11;}
                        chordsList.remove(chordNum);
                        chordsList.add(i, list.get(chordNum));
                    }
                    break;
                case R.id.confirm_trans:
                    break;
            }
        }
    };
}
