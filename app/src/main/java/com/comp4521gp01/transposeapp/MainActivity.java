package com.comp4521gp01.transposeapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class MainActivity extends AppCompatActivity {

    private Boolean isFabOpen = false;
    private FloatingActionButton fab,camera_fab,web_fab,gallery_fab;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;

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
        fab.setOnClickListener(clickListener);
        camera_fab.setOnClickListener(clickListener);
        web_fab.setOnClickListener(clickListener);
        gallery_fab.setOnClickListener(clickListener);
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

                    Log.d("Raj", "Fab 1");
                    break;
                case R.id.web_fab:

                    Log.d("Raj", "Fab 2");
                    break;
                case R.id.gallery_fab:

                    Log.d("Raj", "Fab 3");
                    break;
            }
        }
    };

    public void animateFAB(){

        if(isFabOpen){

            fab.startAnimation(rotate_backward);
            camera_fab.startAnimation(fab_close);
            web_fab.startAnimation(fab_close);
            gallery_fab.startAnimation(fab_close);
            camera_fab.setClickable(false);
            web_fab.setClickable(false);
            gallery_fab.setClickable(false);
            isFabOpen = false;
            Log.d("Raj", "close");

        } else {

            fab.startAnimation(rotate_forward);
            camera_fab.startAnimation(fab_open);
            web_fab.startAnimation(fab_open);
            gallery_fab.startAnimation(fab_open);
            camera_fab.setClickable(true);
            web_fab.setClickable(true);
            gallery_fab.setClickable(true);
            isFabOpen = true;
            Log.d("Raj","open");

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
