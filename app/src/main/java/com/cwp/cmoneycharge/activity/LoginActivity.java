package com.cwp.cmoneycharge.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.cwp.cmoneycharge.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 登陆的界面
 */
public class LoginActivity extends Activity {

    @Bind(R.id.username)
    EditText mUsername;
    @Bind(R.id.password)
    EditText mPassword;
    @Bind(R.id.btnCancle)
    Button mBtnCancle;
    @Bind(R.id.btnLogin)
    Button mBtnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ButterKnife.bind(this);
    }
}
