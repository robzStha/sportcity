package com.app.sportcity.activities;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.sportcity.objects.Category;
import com.app.sportcity.server_protocols.ApiCalls;
import com.app.sportcity.server_protocols.RetrofitSingleton;
import com.app.sportcity.utils.Opener;
import com.app.sportcity.R;
import com.app.sportcity.utils.CommonMethods;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnSignIn;
    TextView tvForgotPwd;
    FloatingActionButton fabSignUp;
    RelativeLayout rlParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setElevation(0);
        setContentView(R.layout.activity_login);
        rlParent = (RelativeLayout) findViewById(R.id.activity_login);
        btnSignIn = (Button) findViewById(R.id.btn_signIn);
        btnSignIn.setOnClickListener(this);
        fabSignUp = (FloatingActionButton)findViewById(R.id.fab_signUp);
        fabSignUp.setOnClickListener(this);
        tvForgotPwd = (TextView) findViewById(R.id.tv_forgotPwd);
        tvForgotPwd.setOnClickListener(this);
        CommonMethods.setupUI(rlParent, LoginActivity.this);

    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_signIn:
                Opener.BaseActivity(LoginActivity.this);
                break;
            case R.id.fab_signUp:
                Opener.RegisterActivity(LoginActivity.this);
                break;
            case R.id.tv_forgotPwd:
                Opener.ForgotPwdActivity(LoginActivity.this);
                break;
        }
    }
}
