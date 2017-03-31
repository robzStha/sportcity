package com.app.sportcity.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
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

import java.math.BigDecimal;

public class CartList extends AppCompatActivity {

    RecyclerView recyclerView;
    Button btnProceed;
    MySharedPreference prefs;
//    TextView tvMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);
        recyclerView = (RecyclerView) findViewById(R.id.rv_cart_items);
        btnProceed = (Button) findViewById(R.id.btn_proceed);
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
        if (StaticVariables.Cart.cartDetails.getTotalCount() > 0) {
            recyclerView.setLayoutManager(new LinearLayoutManager(CartList.this));
            recyclerView.setAdapter(new CartListAdapter());

            btnProceed.setOnClickListener(
                    redirectToPaypal()
            );
        } else {
            recyclerView.setVisibility(View.GONE);
            btnProceed.setText("Cart Empty !! Shop some");

//            tvMsg = (TextView) findViewById(R.id.tv_msg);
//            tvMsg.setText("No any items on the cart. Please buy some :)");

            btnProceed.setOnClickListener(redirectToShop());
        }
    }

    private static PayPalConfiguration config = new PayPalConfiguration()

            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)

            .clientId("ATYLTKUa-cxPgNt_pHk8xL6P2oTKP1i-8ji8PA7TYuQ4E2mT8kgHc-KYN2rslWYYb_YfkTS5i_YITihr");

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
                PayPalPayment payment = new PayPalPayment(new BigDecimal(StaticVariables.Cart.cartDetails.getTotalAmount()), "USD", "Total Amount",
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
                                    prefs.setKeyValues(StaticVariables.CART_ITEM, new Gson().toJson(new CartDetails()).toString());
                                    StaticVariables.Cart.reset();
                                    CartList.this.finish();
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
            holder.tvPrice.setText("$" + StaticVariables.Cart.cartDetails.getItemsDetail().get(position).getItemPrice());
            holder.tvTitle.setText("$" + StaticVariables.Cart.cartDetails.getItemsDetail().get(position).getItemName());
            Glide.with(CartList.this)
                    .load(StaticVariables.Cart.cartDetails.getItemsDetail().get(position).getImageUrl())
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
            builder.setMessage("Are you sure you want to remove this item?")
                    .setNegativeButton("Remove", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            StaticVariables.Cart.deleteItem(StaticVariables.Cart.cartDetails.getItemsDetail().get(position));
                            notifyDataSetChanged();
                            updateJSON();
                            if (getItemCount() == 0) {
                                recyclerView.setVisibility(View.GONE);
                                btnProceed.setText("Cart Empty !! Shop some");
                                btnProceed.setOnClickListener(redirectToShop());
                            }
                        }
                    }).setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
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
            prefs.setKeyValues(StaticVariables.CART_ITEM, gson.toJson(StaticVariables.Cart.cartDetails));
        }

        @Override
        public int getItemCount() {
            return StaticVariables.Cart.cartDetails.getItemsDetail().size();
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
