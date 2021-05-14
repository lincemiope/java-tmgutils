package com.uodreams.tmgutils;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.uodreams.tmgutils.conf.Const;
import com.uodreams.tmgutils.db.sqlHelper;
import com.uodreams.tmgutils.json.jsonHandler;
import com.uodreams.tmgutils.model.Member;
import com.uodreams.tmgutils.model.REST.MemberResponse;
import com.uodreams.tmgutils.service.LoginService;
import com.uodreams.tmgutils.utils.Misc;

public class LoginActivity extends AppCompatActivity {
    public static final String LOGIN_ACTION = Const.PKG + ".action.LOGIN_ACTION";

    private TextView tvFieldError;
    private EditText etPassword;
    private EditText etUsername;
    private ProgressDialog pd;
    //private CheckBox cbRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        //cbRememberMe = (CheckBox) findViewById(R.userId.cbRememberMe);
        tvFieldError = (TextView) findViewById(R.id.tvFieldError);
    }

    public void loginOnClick(View v) {
        if (TextUtils.isEmpty(etUsername.getText()) || TextUtils.isEmpty(etPassword.getText())) {
            tvFieldError.setText(getResources().getString(R.string.error_empty_fields));
            tvFieldError.setVisibility(View.VISIBLE);
            return;
        }

        final String user = etUsername.getText().toString();
        final String password = etPassword.getText().toString();
        if (validateLogin(user,password)) {
            new loginTask().execute(user, password);
        }
    }

    private boolean validateLogin(final String user, final String pwd) {
        final String username = getResources().getString(R.string.login_username_label).replace(":","");
        final String password = getResources().getString(R.string.login_password_label).replace(":","");
        if (user.isEmpty()) {
            final String text = getResources().getString(R.string.error_field_empty, username);
            tvFieldError.setText(text);
            tvFieldError.setVisibility(View.VISIBLE);
            return false;
        }

        if (pwd.isEmpty()) {
            final String text = getResources().getString(R.string.error_field_empty, password);
            tvFieldError.setText(text);
            tvFieldError.setVisibility(View.VISIBLE);
            return false;
        }

        if (user.length() < Const.USERNAME_MIN_LENGTH) {
            final String text = getResources().getString(R.string.error_field_too_short, username, Const.USERNAME_MIN_LENGTH);
            tvFieldError.setText(text);
            tvFieldError.setVisibility(View.VISIBLE);
            return false;
        }

        if (pwd.length() < Const.PASSWORD_MIN_LENGTH) {
            final String text = getResources().getString(R.string.error_field_too_short, password, Const.PASSWORD_MIN_LENGTH);
            tvFieldError.setText(text);
            tvFieldError.setVisibility(View.VISIBLE);
            return false;
        }

        if (!user.matches("[a-zA-Z0-9]*")) {
            final String text = getResources().getString(R.string.error_field_wrong_char,user);
            tvFieldError.setText(text);
            tvFieldError.setVisibility(View.VISIBLE);
            return false;
        }

        if (!pwd.matches("[a-zA-Z0-9]*")) {
            final String text = getResources().getString(R.string.error_field_wrong_char,pwd);
            tvFieldError.setText(text);
            tvFieldError.setVisibility(View.VISIBLE);
            return false;
        }

        tvFieldError.setVisibility(View.INVISIBLE);
        return true;
    }
    private class loginTask extends AsyncTask<String, String, String> {
        private String mUser;
        private String mPassword;
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(LoginActivity.this);
            pd.setMessage(getApplicationContext().getResources().getString(R.string.prog_logging_in));
            pd.setCancelable(false);
            pd.show();

        }

        protected String doInBackground(String... params) {
            mUser = params[0];
            mPassword = params[1];

            return LoginService.get().Login(mUser, mPassword);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (pd.isShowing())
                pd.dismiss();

            if (result.equals("{}") || result.toUpperCase().contains("ERROR")) {
                unsuccessfullLogin();
                return;
            }

            final MemberResponse memberResponse = jsonHandler.getUserData(result);

            if (memberResponse != null) {
                if (memberResponse.Response.Code == 100) {
                    final sqlHelper sql = sqlHelper.getInstance(getApplicationContext());
                    final Member member = memberResponse.Member;
                    sql.userLogin(mUser, mPassword);
                    sql.addMember(member);
                    gotoFirst();
                } else {
                    Misc.restToast(LoginActivity.this, memberResponse.Response.Message);
                }
            }
        }
    }

    private void unsuccessfullLogin() {
        Toast.makeText(getApplicationContext(), R.string.error_login_unsuccessful, Toast.LENGTH_SHORT).show();
    }

    private void gotoFirst() {
        final Intent firstIntent = new Intent();
        setResult(RESULT_OK, firstIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        final Intent firstIntent = new Intent();
        setResult(RESULT_CANCELED, firstIntent);
        finish();
    }
}
