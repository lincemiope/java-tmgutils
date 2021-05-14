package com.uodreams.tmgutils;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.uodreams.tmgutils.conf.Const;

public class firstAccessActivity extends AppCompatActivity {
    //private static final String TAG_LOG = firstAccessActivity.class.getName();
    public static final String FIRST_ACTION = Const.PKG + ".action.FIRST_ACTION";
    private static final int LOGIN_REQUEST_ID = 1;
    private static final int REGISTER_REQUEST_ID = 2;
    private long mBackPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_access);
    }

    public void btnRegister_onClick(View view) {
        final Intent registerIntent = new Intent(RegisterActivity.REGISTER_ACTION);
        startActivityForResult(registerIntent,REGISTER_REQUEST_ID);
    }

    public void btnLogin_onClick(View view) {
        final Intent loginIntent = new Intent(LoginActivity.LOGIN_ACTION);
        startActivityForResult(loginIntent, LOGIN_REQUEST_ID);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LOGIN_REQUEST_ID:
                if (resultCode == RESULT_OK) {
                    final Intent mainIntent = new Intent(MainActivity.MENU_ACTIVITY_ACTION);
                    startActivity(mainIntent);
                    finish();
                }
                break;
            case REGISTER_REQUEST_ID:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this,R.string.notif_now_may_login, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        long t = System.currentTimeMillis();
        if (t - this.mBackPressedTime > 2000) {    // 2 secs
            this.mBackPressedTime = t;
            final String avviso_exit = getResources().getString(R.string.notif_double_back_exit);
            Toast.makeText(this, avviso_exit,
                    Toast.LENGTH_SHORT).show();
        } else {
            finishAffinity();
            System.exit(0);

        }
    }
}
