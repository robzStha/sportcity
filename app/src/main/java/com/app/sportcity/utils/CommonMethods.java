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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Rabin on 9/7/2015.
 */
public class CommonMethods {

    public static final String TEMP_MAIL = "temp_mail";
    public static String FIREBASE_TOKEN="firebase_token";

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

    public static void getDeviceDensity(Context context) {
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

    public static class PasswordObj {
        boolean isValid;
        String msg;
    }

    public static PasswordObj isValidPassword(String pwd, EditText etPwd) {
        PasswordObj pwdObj = new PasswordObj();
        return pwdObj;
    }

    public static void setFadeOut(Interpolator interpolator, int duration, View view) {
        AlphaAnimation fadeOut = new AlphaAnimation(1, 0);
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

    public static ArrayList<View> getViewsByTag(ViewGroup root, String tag) {
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


//        public static final String BASE_URL = "http://imaginationcpl.com/developer/icpl_newsportal/wp-json/wp/v2/";
        public static final String BASE_URL = "https://sportcity.gr/wp-json/";
        public static final String DATA = "wp/v2/";
        public static final String MENU = "wp-api-menus/v2/";
        public static final String NOTIFICATION = "apnwp/";
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

    public static int getCameraPhotoOrientation(Context context, Uri imageUri, String imagePath) {
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

    public static int getRatioHeight(int newWidth, int oldWidth, int oldHeight) {

        return (int) (((float) oldHeight / oldWidth) * newWidth);

    }

    public static boolean hasConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            if (isInternetAvailable()) {
                return true;
            } else return false;
        } else return false;

//        return cm.getActiveNetworkInfo() != null && cm.isConnected()? isInternetAvailable() : false;
//        return cm.getActiveNetworkInfo() != null;
    }

    public static boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com"); //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }

    }

    public static String printDifference(Date startDate, Date endDate){

        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        String temp;
        if(elapsedDays>0){
            if(elapsedDays>1){
                temp = elapsedDays+" days ago";
//                tv.setText(elapsedDays+" days ago");
            }else temp = elapsedDays+" day ago";
        }else if(elapsedHours>1){
            temp = elapsedHours+" hrs ago";
        }else if(elapsedHours==1){
            temp = elapsedHours+" hr ago";
        }else{
            temp = elapsedHours+" min ago";
        }

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays,
                elapsedHours, elapsedMinutes, elapsedSeconds);
        return temp;

    }

    public static String timeElapsed(String sDate) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd-MM-yyyy HH:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = "";

        try {
            date = inputFormat.parse(sDate);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (str != "") {
            Date d1 = new Date();
            Date d2;
            String currentDateTimeString = outputFormat.format(d1);
            try {
                d1 = outputFormat.parse(currentDateTimeString);
                d2 = outputFormat.parse(str);
                str = CommonMethods.printDifference(d2, d1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            System.out.println(currentDateTimeString + " Current date time");
        }
        return str;
    }

    public static String random(int max_len) {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(max_len);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    public static String getRandomMail(){
        return CommonMethods.random(5)+System.currentTimeMillis()+"@sportcitytemp.com";
    }

}