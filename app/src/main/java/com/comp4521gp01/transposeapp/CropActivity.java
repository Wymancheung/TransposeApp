package com.comp4521gp01.transposeapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;

/**
 * Created by waimancheung on 17/5/2017.
 */

public class CropActivity extends Activity {
    public static final String EXTRA_MESSAGE = "crop.MESSAGE";
    private Button rotate_crop, confirm_crop;
    private String bittext;
    private CropImageView cropImageView;
    private Bitmap bitmap = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        Intent intent = getIntent();
        /*
        bittext = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        //String to Bitmap
        try {
            byte [] encodeByte= Base64.decode(bittext,Base64.DEFAULT);
            bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch(Exception e) {
            e.getMessage();
        }
        */

        Bundle extras = getIntent().getExtras();
        byte[] byteArray = extras.getByteArray("picture");

        bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        cropImageView = (CropImageView) findViewById(R.id.cropImageView);
        rotate_crop = (Button) findViewById(R.id.rotate_crop);
        confirm_crop = (Button) findViewById(R.id.confirm_crop);

        rotate_crop.setOnClickListener(clickListener);
        confirm_crop.setOnClickListener(clickListener);

        cropImageView.setImageBitmap(bitmap);

    }

    private View.OnClickListener clickListener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.rotate_crop:
                    cropImageView.rotateImage(90);
                    break;
                case R.id.confirm_crop:
                    Bitmap cropped = cropImageView.getCroppedImage();

                    //Bitmap to String
                    ByteArrayOutputStream baos=new  ByteArrayOutputStream();
                    cropped.compress(Bitmap.CompressFormat.PNG,100, baos);
                    byte [] b=baos.toByteArray();
                    String message= Base64.encodeToString(b, Base64.DEFAULT);

                    Intent intent = new Intent(CropActivity.this, CalibrateActivity.class);
                    intent.putExtra(EXTRA_MESSAGE, message);
                    startActivity(intent);
                    finish();

                    break;
            }
        }
    };

}
