package com.uodreams.tmgutils;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.uodreams.tmgutils.conf.Const;
import com.uodreams.tmgutils.model.Response;
import com.uodreams.tmgutils.service.LoginService;
import com.uodreams.tmgutils.json.jsonHandler;
import com.uodreams.tmgutils.utils.Misc;

public class RegisterActivity extends AppCompatActivity {
    public static final String REGISTER_ACTION = Const.PKG + ".action.REGISTER_ACTION";
    private static final String TAG_LOG = RegisterActivity.class.getName();
    private TextView tvFieldError;
    private EditText etPassword;
    private EditText etRepeatPassword;
    private EditText etCode;
    private EditText etUsername;
    private EditText etAlias;

    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tvFieldError = (TextView) findViewById(R.id.tvFieldError);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etRepeatPassword = (EditText) findViewById(R.id.etRepeatPassword);
        etCode = (EditText) findViewById(R.id.etCode);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etAlias = (EditText) findViewById(R.id.etAlias);
    }

    private boolean validateRegistration() {
        final String user = etUsername.getText().toString();
        final String pwd = etPassword.getText().toString();
        final String rpwd = etRepeatPassword.getText().toString();
        final String alias = etAlias.getText().toString();
        final String code = etCode.getText().toString();

        final String password = getResources().getString(R.string.login_password_label).replace(":","");
        final String username = getResources().getString(R.string.login_username_label).replace(":","");
        final String codefield = getResources().getString(R.string.register_code_label).replace(":","");
        final String rpassword = getResources().getString(R.string.register_repeat_password_label).replace(":","");
        final String aliasfield = getResources().getString(R.string.register_alias_label).replace(":","");

        if (user.isEmpty()) {
            final String error = getResources().getString(R.string.error_field_empty, username);
            tvFieldError.setText(error);
            tvFieldError.setVisibility(View.VISIBLE);
            return false;
        }

        if (pwd.isEmpty()) {
            final String error = getResources().getString(R.string.error_field_empty, password);
            tvFieldError.setText(error);
            tvFieldError.setVisibility(View.VISIBLE);
            return false;
        }

        if (rpwd.isEmpty()) {
            final String error = getResources().getString(R.string.error_field_empty, rpassword);
            tvFieldError.setText(error);
            tvFieldError.setVisibility(View.VISIBLE);
            return false;
        }

        if (!pwd.equals(rpwd)) {
            final String error = getResources().getString(R.string.error_password_not_match);
            tvFieldError.setText(error);
            tvFieldError.setVisibility(View.VISIBLE);
            return false;
        }

        if (code.isEmpty()) {
            final String error = getResources().getString(R.string.error_field_empty, codefield);
            tvFieldError.setText(error);
            tvFieldError.setVisibility(View.VISIBLE);
            return false;
        }

        if (alias.isEmpty()) {
            final String error = getResources().getString(R.string.error_field_empty, aliasfield);
            tvFieldError.setText(error);
            tvFieldError.setVisibility(View.VISIBLE);
            return false;
        }

        if (alias.length() > Const.ALIAS_MAX_LENGTH) {
            final String error = getResources().getString(R.string.error_field_too_long, aliasfield, Const.ALIAS_MAX_LENGTH);
            tvFieldError.setText(error);
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
            final String text = getResources().getString(R.string.error_field_wrong_char,username);
            tvFieldError.setText(text);
            tvFieldError.setVisibility(View.VISIBLE);
            return false;
        }

        if (!pwd.matches("[a-zA-Z0-9]*")) {
            final String text = getResources().getString(R.string.error_field_wrong_char,password);
            tvFieldError.setText(text);
            tvFieldError.setVisibility(View.VISIBLE);
            return false;
        }

        if (!alias.matches("[a-zA-Z0-9]*")) {
            final String text = getResources().getString(R.string.error_field_wrong_char,aliasfield);
            tvFieldError.setText(text);
            tvFieldError.setVisibility(View.VISIBLE);
            return false;
        }

        if (!code.matches("[0-9]*")) {
            final String text = getResources().getString(R.string.error_field_wrong_char,codefield);
            tvFieldError.setText(text);
            tvFieldError.setVisibility(View.VISIBLE);
            return false;
        }

        if (code.length() != Const.CODE_LENGTH) {
            final String text = getResources().getString(R.string.error_wrong_code_length, Const.CODE_LENGTH);
            tvFieldError.setText(text);
            tvFieldError.setVisibility(View.VISIBLE);
            return false;
        }

        tvFieldError.setText("");
        tvFieldError.setVisibility(View.INVISIBLE);
        return true;
    }

    public void btnRegister_onClick(View view) {
        if (validateRegistration()) {
            final String user = etUsername.getText().toString();
            final String password = etPassword.getText().toString();
            final String code = etCode.getText().toString();
            final String alias = etAlias.getText().toString();
            new registerTask().execute(user,password,code,alias);
        }

    }

    private class registerTask extends AsyncTask<String, String, String> {
        private String mUser;
        private String mPassword;
        private String mCode;
        private String mAlias;
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(RegisterActivity.this);
            pd.setMessage(getApplicationContext().getResources().getString(R.string.prog_registrating));
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {
            mUser = params[0];
            mPassword = params[1];
            mCode = params[2];
            mAlias = params[3];

            return LoginService.get().Register(mUser, mPassword, mCode, mAlias);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing())
                pd.dismiss();

            final Response response = jsonHandler.getResponse(result);

            if (response == null) {
                reportResult(false);
                return;
            }

            if (response.Code == 100)
                reportResult(true);
            else {
                Misc.restToast(RegisterActivity.this, response.Message);
                reportResult(false);
            }
        }
    }

    private void reportResult(boolean success) {
        final Intent firstIntent = new Intent();
        if (success) {
            setResult(RESULT_OK, firstIntent);
            finish();
        } else {
            Toast.makeText(this,R.string.error_registration_faulty,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        final Intent firstIntent = new Intent();
        setResult(RESULT_CANCELED, firstIntent);
        finish();
    }
}
