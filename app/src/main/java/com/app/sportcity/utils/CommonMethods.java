package com.app.sportcity.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.graphics.Point;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Interpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rabin on 9/7/2015.
 */
public class CommonMethods {

    public static Intent getPickImageChooserIntent(Activity activity) {

// Determine Uri of camera image to  save.
        Uri outputFileUri = getCaptureImageOutputUri(activity);

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = activity.getPackageManager();

// collect all camera intents
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

// collect all gallery intents
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

// the main intent is the last in the  list (fucking android) so pickup the useless one
        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        // Create a chooser from the main  intent
        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }

    /**
     * Get URI to image received from capture  by camera.
     */
    private static Uri getCaptureImageOutputUri(Activity activity) {
        Uri outputFileUri = null;
        File getImage = activity.getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "pickImageResult.jpg"));
        }
        return outputFileUri;
    }

    /**
     * Get the URI of the selected image from  {@link #//getPickImageChooserIntent()}.<br/>
     * Will return the correct URI for camera  and gallery image.
     *
     * @param data the returned data of the  activity result
     */
    public static Uri getPickImageResultUri(Intent data, Activity activity) {
        boolean isCamera = true;
        if (data != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }
        return isCamera ? getCaptureImageOutputUri(activity) : data.getData();
    }

    /**
     * Method for removing the keyboard if touched outside the editview.
     *
     * @param view
     * @param baseActivity
     */
    public static void setupUI(View view, final Activity baseActivity) {

        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
//                    baseActivity.closeKeyboard();
                    hideSoftKeyboard(baseActivity);
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(innerView, baseActivity);
            }
        }
    }

    public static void hideSoftKeyboard(Activity activity) {

        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }

    /**
     * @return : file name with jpt extension
     */
    public static String randomFileName(String ext) {
        String fileName;
        fileName = String.format("%s.%s", System.currentTimeMillis(), ext);
        return fileName;
    }

    public static Point getDisplaySize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static void setImageviewRatio(Context ctx, ImageView iv, int padding) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) ctx).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenWidth = displaymetrics.widthPixels - 2 * padding;
        int screenHeight = screenWidth * 3 / 4;
        System.out.println("Width=>" + screenWidth);
        System.out.println("Height=>" + screenHeight);
        iv.getLayoutParams().width = screenWidth;
        iv.getLayoutParams().height = screenWidth * 3 / 4;
        iv.requestLayout();
    }

    public static float dpFromPx(final Activity activity, final int px) {
        return px / activity.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final Activity activity, final int dp) {
        return dp * activity.getResources().getDisplayMetrics().density;
    }
    public static float pxFromDp(final Context activity, final int dp) {
        return dp * activity.getResources().getDisplayMetrics().density;
    }

    public static void makeCall(Context context, String mobileNumber) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);  //gets the current TelephonyManager
        if ((tm.getSimState() == TelephonyManager.SIM_STATE_ABSENT)) {
            Toast.makeText(context, "Please insert SIM", Toast.LENGTH_SHORT).show();
            return;
        }
        if ((tm.getSimState() != TelephonyManager.SIM_STATE_READY)) {
            Toast.makeText(context, "Sim is not ready.", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobileNumber));
        try {
            context.startActivity(callIntent);

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "yourActivity is not founded", Toast.LENGTH_SHORT).show();
        }
    }

    public static void visitUrl(Context context, String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        context.startActivity(i);

    }

    public static void sendEmail(Context context, String EmailId) {
        /* Create the Intent */
        final Intent emailIntent = new Intent(Intent.ACTION_SEND);

/* Fill it with Data */
        emailIntent.setType("plain/text");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{EmailId});
//        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");

/* Send it off to the Activity-Chooser */
        context.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }

    public static void getDeviceDensity(Context context){
        switch (context.getResources().getDisplayMetrics().densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                System.out.println("Device density: DENSITY_LOW");
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                System.out.println("Device density: DENSITY_MEDIUM");
                break;
            case DisplayMetrics.DENSITY_HIGH:
                System.out.println("Device density: DENSITY_HIGH");
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                System.out.println("Device density: DENSITY_XHIGH");
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                System.out.println("Device density: DENSITY_XXHIGH");
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                System.out.println("Device density: DENSITY_XXXHIGH");
                break;
        }
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static class PasswordObj{
        boolean isValid;
        String msg;
    }

    public static PasswordObj isValidPassword(String pwd, EditText etPwd) {
        PasswordObj pwdObj = new PasswordObj();
        return pwdObj;
    }

    public static void setFadeOut(Interpolator interpolator, int duration, View view) {
        AlphaAnimation fadeOut = new AlphaAnimation(1,0);
        fadeOut.setInterpolator(interpolator); // add this
        fadeOut.setDuration(duration);
        view.setAnimation(fadeOut);
    }

    public static void setFadeIn(Interpolator interpolator, int duration, View view) {
        AlphaAnimation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(interpolator); // add this
        fadeIn.setDuration(duration);
        view.setAnimation(fadeIn);
    }

    public static ArrayList<View> getViewsByTag(ViewGroup root, String tag){
        ArrayList<View> views = new ArrayList<View>();
        final int childCount = root.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = root.getChildAt(i);
            if (child instanceof ViewGroup) {
                views.addAll(getViewsByTag((ViewGroup) child, tag));
            }

            final Object tagObj = child.getTag();
            if (tagObj != null && tagObj.equals(tag)) {
                views.add(child);
            }

        }
        return views;
    }

    public class UrlHelper {


        public static final String BASE_URL = "http://imaginationcpl.com/developer/sportscity/public/api/v1/";
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static int getCameraPhotoOrientation(Context context, Uri imageUri, String imagePath){
        int rotate = 0;
        try {
            context.getContentResolver().notifyChange(imageUri, null);
            File imageFile = new File(imagePath);

            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }

            Log.i("RotateImage", "Exif orientation: " + orientation);
            Log.i("RotateImage", "Rotate value: " + rotate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }
}