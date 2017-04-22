package com.app.sportcity.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.sportcity.R;
import com.app.sportcity.objects.CartDetails;
import com.app.sportcity.objects.ItemDetail;
import com.app.sportcity.statics.StaticVariables;
import com.app.sportcity.utils.MySharedPreference;
import com.app.sportcity.utils.Opener;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CartList extends AppCompatActivity {

    RecyclerView recyclerView;
    Button btnProceed, btnShopMore;
    MySharedPreference prefs;
    private final int PERMISSION_REQUEST_CODE = 23;
//    TextView tvMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);
        recyclerView = (RecyclerView) findViewById(R.id.rv_cart_items);
        btnProceed = (Button) findViewById(R.id.btn_proceed);
        btnShopMore = (Button) findViewById(R.id.btn_shop_more);
        Intent intent = new Intent(CartList.this, PayPalService.class);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        startService(intent);
        prefs = new MySharedPreference(CartList.this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (StaticVariables.cartDetails.getTotalCount() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(new LinearLayoutManager(CartList.this));
            recyclerView.setAdapter(new CartListAdapter());

            btnShopMore.setVisibility(View.VISIBLE);
            btnShopMore.setOnClickListener(redirectToShop());
            btnProceed.setBackground(getResources().getDrawable(R.drawable.button_shape));
            btnProceed.setText(getString(R.string.proceed));
            btnProceed.setOnClickListener(
                    redirectToPaypal()
            );
        } else {
            recyclerView.setVisibility(View.GONE);
            btnShopMore.setVisibility(View.GONE);
            btnProceed.setText("ΑΔΕΙΟ ΚΑΛΑΘΙ ΕΠΙΣΤΡΟΦΗ ΣΤΗ ΓΚΑΛΕΡΙ");
            btnProceed.setBackground(getResources().getDrawable(R.drawable.button_shape_orange));
            btnProceed.setOnClickListener(redirectToShop());
        }
    }

    private static PayPalConfiguration config = new PayPalConfiguration()

            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
//            .environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION)
//            .clientId("Ac3E_yople9chtpuq5FzlngGCekOfaw-uw-kLIsUsKwepYmikTbnLoPHbyBXQCL1r-hgpg-gkYiI1L21"); // client original id
            .clientId("ATYLTKUa-cxPgNt_pHk8xL6P2oTKP1i-8ji8PA7TYuQ4E2mT8kgHc-KYN2rslWYYb_YfkTS5i_YITihr"); // dummy test id

    @NonNull
    private View.OnClickListener redirectToShop() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Opener.Shop(CartList.this);
            }
        };
    }

    @NonNull
    private View.OnClickListener redirectToPaypal() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayPalPayment payment = new PayPalPayment(new BigDecimal(StaticVariables.cartDetails.getTotalAmount()), "EUR", "Total Amount",
                        PayPalPayment.PAYMENT_INTENT_SALE);

                Intent intent = new Intent(CartList.this, PaymentActivity.class);
                // send the same configuration for restart resiliency
                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
                startActivityForResult(intent, 0);
            }
        };
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new DownloadImages().execute(StaticVariables.cartDetails.getItemDetail());
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            final PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    Log.i("paymentExample", confirm.toJSONObject().toString(4));


                    final ProgressDialog progress = new ProgressDialog(CartList.this);
                    progress.setMessage("Confirming purchase");
                    progress.show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject jsonObj = confirm.toJSONObject();
                            try {
                                jsonObj = jsonObj.getJSONObject("response");
                                if (jsonObj.getString("state").equalsIgnoreCase("approved")) {
                                    downloadImages();
                                } else{
                                    Toast.makeText(CartList.this, "Purchase failure please try again.", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            progress.dismiss();
                        }
                    }, 1500);


                    // TODO: send 'confirm' to your server for verification.
                    // see https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                    // for more details.

                } catch (JSONException e) {
                    Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("paymentExample", "The user canceled.");
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        }
    }

    private void downloadImages() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission()) {
                new DownloadImages().execute(StaticVariables.cartDetails.getItemDetail());
            } else {
                requestPermission(); // Code for permission
            }
        } else {
            new DownloadImages().execute(StaticVariables.cartDetails.getItemDetail());
        }


    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(CartList.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(CartList.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(CartList.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(CartList.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    public class DownloadImages extends AsyncTask<List<ItemDetail>, Void, Void> {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CartList.this);
            progressDialog.setIndeterminate(true);
            int count = StaticVariables.cartDetails.getTotalCount();
            String msg = count>1? "ΠΛΗΡΩΜΗ ΕΠΙΤΥΧΗΣ.\nDownloading your cart items":"ΠΛΗΡΩΜΗ ΕΠΙΤΥΧΗΣ.\nDownloading your cart item";
            progressDialog.setMessage(msg);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(List<ItemDetail>... params) {
            Looper.prepare();
            for (ItemDetail itemDetail : params[0]) {
                Bitmap theBitmap;
                try {
                    theBitmap = Glide.
                            with(CartList.this).
                            load(itemDetail.getItemOrgImgUrl()).
                            asBitmap().
                            into(-1, -1).
                            get();
                    saveToInternalStorage(theBitmap, itemDetail.getItemName());
                } catch (InterruptedException e) {
                    StaticVariables.Cart.addDownloadIssueItem(itemDetail);
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    StaticVariables.Cart.addDownloadIssueItem(itemDetail);
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            AlertDialog.Builder builder = new AlertDialog.Builder(CartList.this);
            builder.setMessage("Images downloaded successfully to your gallery.\nPlease check.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            prefs.setKeyValues(StaticVariables.CART_ITEM, new Gson().toJson(new CartDetails()).toString());
                            StaticVariables.Cart.reset();
                            CartList.this.finish();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();
        }
    }

    public void saveToInternalStorage(Bitmap bitmapImage, String name) {
        MediaStore.Images.Media.insertImage(getContentResolver(), bitmapImage, name, "");
//
//
//        ContextWrapper cw = new ContextWrapper(context);
//        // path to /data/data/yourapp/app_data/imageDir
//
//        String name_="foldername"; //Folder name in device android/data/
//        File directory = cw.getDir(name_, Context.MODE_PRIVATE);
//
//        // Create imageDir
//        File mypath=new File(directory,name);
//
//        FileOutputStream fos = null;
//        try {
//
//            fos = new FileOutputStream(mypath);
//
//            // Use the compress method on the BitMap object to write image to the OutputStream
//            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
//            fos.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Log.e("absolutepath ", directory.getAbsolutePath());
//        return directory.getAbsolutePath();
    }

    /**
     * Method to retrieve image from your device
     **/

    public Bitmap loadImageFromStorage(String path, String name) {
        Bitmap b;
        String name_ = "foldername";
        try {
            File f = new File(path, name_);
            b = BitmapFactory.decodeStream(new FileInputStream(f));
            return b;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    public class CartListAdapter extends RecyclerView.Adapter<CartListVH> {
        LayoutInflater inflater = LayoutInflater.from(CartList.this);

        @Override
        public CartListVH onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.item_cart_list, parent, false);
            return new CartListVH(view);
        }

        @Override
        public void onBindViewHolder(CartListVH holder, final int position) {
            holder.tvPrice.setText("€" + StaticVariables.cartDetails.getItemDetail().get(position).getItemPrice());
            holder.tvTitle.setText(StaticVariables.cartDetails.getItemDetail().get(position).getItemName());
            Glide.with(CartList.this)
                    .load(StaticVariables.cartDetails.getItemDetail().get(position).getImageUrl())
                    .centerCrop()
                    .into(holder.ivCartItemImg);
            holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ConfirmItemRemoval(position);
                }
            });
        }

        private void ConfirmItemRemoval(final int position) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CartList.this);
            builder.setMessage("ΝΑ ΔΙΑΓΡΑΦΤΕΙ;")
                    .setNegativeButton("ΝΑΙ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            StaticVariables.Cart.deleteItem(StaticVariables.cartDetails.getItemDetail().get(position));
                            notifyDataSetChanged();
                            updateJSON();
                            if (getItemCount() == 0) {
                                recyclerView.setVisibility(View.GONE);
                                btnShopMore.setVisibility(View.GONE);
                                btnProceed.setText("ΑΔΕΙΟ ΚΑΛΑΘΙ ΕΠΙΣΤΡΟΦΗ ΣΤΗ ΓΚΑΛΕΡΙ");
                                btnProceed.setOnClickListener(redirectToShop());
                                btnProceed.setBackground(getResources().getDrawable(R.drawable.button_shape_orange));
                            }
                        }
                    }).setPositiveButton("ΟΧΙ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.create().show();
        }

        private void updateJSON() {
            MySharedPreference prefs = new MySharedPreference(CartList.this);
            Gson gson = new Gson();
            prefs.setKeyValues(StaticVariables.CART_ITEM, gson.toJson(StaticVariables.cartDetails));
        }

        @Override
        public int getItemCount() {
            return StaticVariables.cartDetails.getItemDetail().size();
        }
    }

    public class CartListVH extends RecyclerView.ViewHolder {

        ImageView ivCartItemImg, ivDelete;
        TextView tvTitle, tvPrice;

        public CartListVH(View itemView) {
            super(itemView);
            ivDelete = (ImageView) itemView.findViewById(R.id.iv_delete);
            ivCartItemImg = (ImageView) itemView.findViewById(R.id.iv_cart_img);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
