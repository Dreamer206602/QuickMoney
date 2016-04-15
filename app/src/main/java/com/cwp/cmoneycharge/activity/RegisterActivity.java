package com.cwp.cmoneycharge.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;

import com.cwp.cmoneycharge.R;

import butterknife.Bind;
import butterknife.ButterKnife;

//注册的界面
public class RegisterActivity extends Activity {

    @Bind(R.id.rusername)
    EditText mRusername;
    @Bind(R.id.rpassword)
    EditText mRpassword;
    @Bind(R.id.rrpassword)
    EditText mRrpassword;
    @Bind(R.id.tablerow4)
    TableRow mTablerow4;
    @Bind(R.id.btnrCancle)
    Button mBtnrCancle;
    @Bind(R.id.btnrRegister)
    Button mBtnrRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        ButterKnife.bind(this);
    }
}
