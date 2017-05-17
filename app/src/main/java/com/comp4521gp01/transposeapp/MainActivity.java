package com.comp4521gp01.transposeapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "MESSAGETOCROP";
    private Boolean isFabOpen = false;
    private FloatingActionButton fab,camera_fab,web_fab,gallery_fab;
    private TextView fab_label,camera_fab_label,web_fab_label,gallery_fab_label,debug,fab_blur;
    private Animation fab_blur_open, fab_blur_close,fab_open,fab_close,rotate_forward,rotate_backward;
    private String imgDecodableString;
    private GridView gridview;
    private ImageAdapter imageAdapter;

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

        requestPermission();
        //loadGridview();

    }

    private void requestPermission() {
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP_MR1) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        200);
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch(requestCode){
            case 200:
                boolean writeAccepted = grantResults[0]== PackageManager.PERMISSION_GRANTED;
                Log.d(TAG,"writeAcceped--"+writeAccepted);
                prepareDirectory();
                break;

        }
    }

    private void prepareDirectory() {
        String IMGS_PATH = Environment.getExternalStorageDirectory().toString() + "/TransposeApp/imgs";
        File dir = new File(IMGS_PATH);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Log.e(TAG, "ERROR: Creation of directory " + IMGS_PATH + " failed, check does Android Manifest have permission to write to external storage.");
            }
        } else {
            Log.i(TAG, "Created directory " + IMGS_PATH);
        }

        String TESS_PATH = Environment.getExternalStorageDirectory().toString() + "/TransposeApp/tessdata";

        try {
            File dirt = new File(TESS_PATH);
            if (!dirt.exists()) {
                if (!dirt.mkdirs()) {
                    Log.e(TAG, "ERROR: Creation of directory " + TESS_PATH + " failed, check does Android Manifest have permission to write to external storage.");
                }
            } else {
                Log.i(TAG, "Created directory " + TESS_PATH);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadGridview() {
        gridview = (GridView) findViewById(R.id.gridview_main);
        //imageAdapter = new ImageAdapter(this);
        imageAdapter = new ImageAdapter();
        gridview.setAdapter(imageAdapter);

        String ExternalStorageDirectoryPath = Environment
                .getExternalStorageDirectory()
                .getAbsolutePath();

        String targetPath = ExternalStorageDirectoryPath + "/TransposeApp/imgs/";

        Toast.makeText(getApplicationContext(), targetPath, Toast.LENGTH_LONG).show();
        File targetDirector = new File(targetPath);

        File[] files = targetDirector.listFiles();
        for (File file : files){
            imageAdapter.add(file.getAbsolutePath());
        }
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
                    Intent cameraIntent = new Intent(MainActivity.this, CameraActivity.class);

                    //MainActivity.this.startActivity(cameraIntent);
                    startActivityForResult(cameraIntent,0);

                    break;
                case R.id.web_fab:

                    debug.setText("Web");

                    Intent webIntent = new Intent(MainActivity.this, WebActivity.class);
                    //MainActivity.this.startActivity(webIntent);
                    startActivityForResult(webIntent, 1);

                    break;
                case R.id.gallery_fab:

                    debug.setText("Gallery");

                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    // Start the Intent
                    startActivityForResult(galleryIntent, 2);

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

            }else if(requestCode == 1 && resultCode == RESULT_OK && null != data){

            }else if(requestCode == 2 && resultCode == RESULT_OK && null != data){

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

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeFile(imgDecodableString, options);


                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                float bitmapRatio = (float) width / (float) height;
                if (bitmapRatio > 1) {
                    width = 2000;//3000
                    height = (int) (width / bitmapRatio);
                } else {
                    height = 2000;
                    width = (int) (height * bitmapRatio);
                }
                Bitmap resizeBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);

                cursor.close();

                /*
                //Bitmap to String
                ByteArrayOutputStream baos=new  ByteArrayOutputStream();
                resizeBitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
                byte [] b=baos.toByteArray();
                String message= Base64.encodeToString(b, Base64.DEFAULT);

                Intent intent = new Intent(MainActivity.this, CropActivity.class);
                intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);
                */
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                resizeBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                Intent intent = new Intent(MainActivity.this, CropActivity.class);
                intent.putExtra("picture", byteArray);
                startActivity(intent);

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

    private class ImageAdapter extends BaseAdapter{
        //private Context mContext;
        ArrayList<String> itemlist = new ArrayList<String>();

        /*
        public ImageAdapter(Context c){
            mContext = c;
        }
        */

        void add(String path){
            itemlist.add(path);
        }

        @Override
        public int getCount() {
            return itemlist.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            if(convertView == null) {
                holder = new Holder();
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.grid_item_main, null);
                holder.imageView = (ImageView) findViewById(R.id.imageview_grid_main);
                holder.textView = (TextView) findViewById(R.id.textview_grid_main);
                convertView.setTag(holder);
            }else {
                holder = (Holder) convertView.getTag();
            }

            holder.textView.setText("Test");

            Bitmap bm = decodeSampledBitmapFromUri(itemlist.get(position), 220, 220);

            holder.imageView.setImageBitmap(bm);

            return convertView;

            /*
            Holder holder;
            if (convertView == null) {  // if it's not recycled, initialize some attributes
                //imageView = (ImageView) findViewById(R.id.imageview_grid_main);
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.grid_item_main,null);
                holder = new Holder();
                ImageView imageView = holder.imageView;
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(220, 220));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 10, 8, 10);

                TextView textView = holder.textView;
                textView = new TextView(mContext);
                textView.setText("Test");


                convertView.setTag(holder);
            } else {
                //imageView = (ImageView) convertView;
                holder = (Holder) convertView.getTag();
            }

            Bitmap bm = decodeSampledBitmapFromUri(itemlist.get(position), 220, 220);

            holder.imageView.setImageBitmap(bm);
            return convertView;
            */
        }

        private Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight){
            Bitmap bm = null;
            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            bm = BitmapFactory.decodeFile(path, options);

            return bm;
        }

        public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
            // Raw height and width of image
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {
                if (width > height) {
                    inSampleSize = Math.round((float)height / (float)reqHeight);
                } else {
                    inSampleSize = Math.round((float)width / (float)reqWidth);
                }
            }

            return inSampleSize;
        }

        class Holder{
            ImageView imageView;
            TextView textView;
        }
    }

}