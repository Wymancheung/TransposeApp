package com.comp4521gp01.transposeapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
    private Animation fab_blur_open, fab_blur_close,fab_open,fab_close,rotate_forward,rotate_backward;
    private String imgDecodableString;

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
        fab_blur_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_blur_open);
        fab_blur_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_blur_close);
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

                    Intent webIntent = new Intent(MainActivity.this, WebActivity.class);
                    //MainActivity.this.startActivity(webIntent);
                    startActivityForResult(webIntent, 0);

                    break;
                case R.id.gallery_fab:

                    debug.setText("Gallery");

                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    // Start the Intent
                    startActivityForResult(galleryIntent, 1);

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

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to exit?");
        alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
                System.exit(0);
            }
        });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == 0 && resultCode == RESULT_OK && null != data) {

            } else if(requestCode == 1 && resultCode == RESULT_OK && null != data){

                // Get the Image from data
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                debug.setText(imgDecodableString);
                //ImageView imageView = (ImageView) findViewById(R.id.imageView);

                //imageView.setImageBitmap(BitmapFactory
                //        .decodeFile(imgDecodableString));

            } else {
                debug.setText(requestCode + ", But wrong");
            }
        } catch (Exception e) {
            debug.setText("Something went wrong");
        }
        animateFAB();
    }

    public void animateFAB(){

        if(isFabOpen){

            fab_blur.startAnimation(fab_blur_close);
            fab.startAnimation(rotate_backward);
            camera_fab.startAnimation(fab_close);
            web_fab.startAnimation(fab_close);
            gallery_fab.startAnimation(fab_close);

            fab_blur.setVisibility(View.INVISIBLE);
            fab_label.setVisibility(View.INVISIBLE);
            camera_fab_label.setVisibility(View.INVISIBLE);
            web_fab_label.setVisibility(View.INVISIBLE);
            gallery_fab_label.setVisibility(View.INVISIBLE);

            camera_fab.setClickable(false);
            web_fab.setClickable(false);
            gallery_fab.setClickable(false);
            fab_blur.setClickable(false);
            isFabOpen = false;
        } else {
            fab_blur.startAnimation(fab_blur_open);
            fab.startAnimation(rotate_forward);
            camera_fab.startAnimation(fab_open);
            web_fab.startAnimation(fab_open);
            gallery_fab.startAnimation(fab_open);

            fab_blur.setVisibility(View.VISIBLE);
            fab_label.setVisibility(View.VISIBLE);
            camera_fab_label.setVisibility(View.VISIBLE);
            web_fab_label.setVisibility(View.VISIBLE);
            gallery_fab_label.setVisibility(View.VISIBLE);

            camera_fab.setClickable(true);
            web_fab.setClickable(true);
            gallery_fab.setClickable(true);
            fab_blur.setClickable(true);
            isFabOpen = true;
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
        }else if (id == R.id.action_faq){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
