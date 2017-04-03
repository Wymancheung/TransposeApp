package com.comp4521gp01.transposeapp;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Boolean isFabOpen = false;
    private FloatingActionButton fab,camera_fab,web_fab,gallery_fab;
    private TextView fab_label,camera_fab_label,web_fab_label,gallery_fab_label,debug,fab_blur;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        camera_fab = (FloatingActionButton)findViewById(R.id.camera_fab);
        web_fab = (FloatingActionButton)findViewById(R.id.web_fab);
        gallery_fab = (FloatingActionButton)findViewById(R.id.gallery_fab);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);
        fab_label = (TextView) findViewById(R.id.fab_label);
        camera_fab_label = (TextView) findViewById(R.id.camera_fab_label);
        web_fab_label = (TextView) findViewById(R.id.web_fab_label);
        gallery_fab_label = (TextView) findViewById(R.id.gallery_fab_label);
        debug = (TextView) findViewById(R.id.debug);
        fab_blur = (TextView) findViewById(R.id.fab_blur);

        fab.setOnClickListener(clickListener);
        camera_fab.setOnClickListener(clickListener);
        web_fab.setOnClickListener(clickListener);
        gallery_fab.setOnClickListener(clickListener);
        fab_blur.setOnClickListener(clickListener);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(clickListener);


    }

    private View.OnClickListener clickListener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.fab:
                    animateFAB();
                    break;
                case R.id.camera_fab:

                    debug.setText("Camera");
                    break;
                case R.id.web_fab:

                    debug.setText("Web");
                    break;
                case R.id.gallery_fab:

                    debug.setText("Gallery");
                    break;
                case R.id.fab_blur:
                    animateFAB();
                    break;
                case R.id.button:
                    debug.setText("button");
                    break;
            }
        }
    };

    public void animateFAB(){

        if(isFabOpen){
            fab_blur.setVisibility(View.INVISIBLE);

            fab.startAnimation(rotate_backward);
            camera_fab.startAnimation(fab_close);
            web_fab.startAnimation(fab_close);
            gallery_fab.startAnimation(fab_close);

            fab_label.setVisibility(View.INVISIBLE);
            camera_fab_label.setVisibility(View.INVISIBLE);
            web_fab_label.setVisibility(View.INVISIBLE);
            gallery_fab_label.setVisibility(View.INVISIBLE);

            camera_fab.setClickable(false);
            web_fab.setClickable(false);
            gallery_fab.setClickable(false);
            fab_blur.setClickable(false);
            isFabOpen = false;
            Log.d("Raj", "close");
            //dialog.dismiss();

        } else {
            fab_blur.setVisibility(View.VISIBLE);
            fab.startAnimation(rotate_forward);
            camera_fab.startAnimation(fab_open);
            web_fab.startAnimation(fab_open);
            gallery_fab.startAnimation(fab_open);

            fab_label.setVisibility(View.VISIBLE);
            camera_fab_label.setVisibility(View.VISIBLE);
            web_fab_label.setVisibility(View.VISIBLE);
            gallery_fab_label.setVisibility(View.VISIBLE);

            camera_fab.setClickable(true);
            web_fab.setClickable(true);
            gallery_fab.setClickable(true);
            fab_blur.setClickable(true);
            isFabOpen = true;
            Log.d("Raj","open");
            //dialog.show();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
