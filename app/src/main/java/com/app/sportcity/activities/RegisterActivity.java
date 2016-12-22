package com.app.sportcity.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.app.sportcity.R;
import com.app.sportcity.utils.CircleTransform;
import com.app.sportcity.utils.CommonMethods;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.app.sportcity.utils.CommonMethods.getCameraPhotoOrientation;
import static com.app.sportcity.utils.CommonMethods.getDisplaySize;
import static com.app.sportcity.utils.CommonMethods.hasPermissions;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton ibtnCapturePhoto;
    final int PERMISSION_ALL = 1;
    private CropImageView civ;
    private FrameLayout flImageCropper, flRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_white_48dp);

        ibtnCapturePhoto = (ImageButton) findViewById(R.id.ibtnCapturePhoto);
        ibtnCapturePhoto.setOnClickListener(this);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtnCapturePhoto:
                String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

                if (!hasPermissions(this, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
                } else {
                    startActivityForResult(CommonMethods.getPickImageChooserIntent(RegisterActivity.this), 23);
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_ALL:
                startActivityForResult(CommonMethods.getPickImageChooserIntent(RegisterActivity.this), 23);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 23:
                    flImageCropper = (FrameLayout) findViewById(R.id.fl_img_cropper);
                    flRegister = (FrameLayout) findViewById(R.id.fl_registration);
                    flRegister.setVisibility(View.GONE);
                    flImageCropper.setVisibility(View.VISIBLE);

                    civ = (CropImageView) findViewById(R.id.iv_img_cropper);
                    Uri imageUri = CommonMethods.getPickImageResultUri(data, this);

                    String filePath = imageUri.getPath();
//                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
//
//                    Cursor cursor = getContentResolver().query(data.getData(), filePathColumn, null, null, null);
//                    cursor.moveToFirst();
//
//                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                    String filePath = cursor.getString(columnIndex);
//                    cursor.close();
//
                    int rotateImage = 0;
                    if (data != null)
                        rotateImage = getCameraPhotoOrientation(RegisterActivity.this, data.getData(), filePath);
//
//
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        Bitmap bMapRotate;
                        if (bitmap.getWidth() > bitmap.getHeight()) {
                            Matrix mat = new Matrix();
                            mat.postRotate(rotateImage);
                            Point point = getDisplaySize(RegisterActivity.this);
//                            Toast.makeText(RegisterActivity.this, "W:H==" + bitmap.getWidth() + ":" + bitmap.getHeight(), Toast.LENGTH_SHORT).show();
                            int height = bitmap.getHeight();
                            int width = bitmap.getWidth();
                            int scaledHeight = getScaledHeight(point.x, width, height);
                            bMapRotate = Bitmap.createScaledBitmap(bitmap, point.x, scaledHeight, false);
                            bitmap.recycle();

                        } else {
                            bMapRotate = bitmap;
                        }
                        civ.setAspectRatio(1, 1);
                        civ.setFixedAspectRatio(true);
//                    civ.setImageBitmap(bitmap);
                        civ.setImageBitmap(bMapRotate);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
        Toast.makeText(RegisterActivity.this, "Result code: " + resultCode, Toast.LENGTH_SHORT).show();

    }

    public int getScaledHeight(int width, int orgWidht, int orgHeight) {
        int height;

        height = (int) (width * ((float) orgHeight / orgWidht));

        return height;
    }

    public void onCropImageClick(View view) {
        Bitmap cropped = civ.getCroppedImage();
        if (cropped != null) {
            flRegister.setVisibility(View.VISIBLE);
            flImageCropper.setVisibility(View.GONE);
            new BitmapWorkerTask().execute(cropped);
        }
    }

    class BitmapWorkerTask extends AsyncTask<Bitmap, Void, String> {

        private Bitmap data = null;

        @Override
        protected void onPreExecute() {
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(String fileLoc) {
            File f = new File(fileLoc);
            System.out.println("File Loc: " + fileLoc + "\n\n" + f.getAbsolutePath());
            Glide.with(RegisterActivity.this)
                    .load(f).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .transform(new CircleTransform(RegisterActivity.this))
                    .override(125, 125)
                    .into(ibtnCapturePhoto);
        }

        @Override
        protected String doInBackground(Bitmap... params) {
            data = params[0];
            return saveBitmap(data);
        }
    }

    public String saveBitmap(Bitmap bmp) {

        FileOutputStream out = null;
        String filePath = getExternalCacheDir().getPath();
        File file = new File(filePath);
        file.mkdirs();
        filePath += "/profile_pic.png";
        try {
            out = new FileOutputStream(filePath);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return filePath;
    }
}
