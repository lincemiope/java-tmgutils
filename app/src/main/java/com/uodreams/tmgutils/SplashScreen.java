package com.uodreams.tmgutils;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.uodreams.tmgutils.db.sqlHelper;
import com.uodreams.tmgutils.model.Login;
import com.uodreams.tmgutils.model.Member;
import com.uodreams.tmgutils.model.REST.MemberResponse;
import com.uodreams.tmgutils.service.LoginService;
import com.uodreams.tmgutils.json.jsonHandler;

public class SplashScreen extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        goAhead();
    }

    private void goAhead() {

        final Login authUser = sqlHelper.getInstance(this).getLogin();
        if (authUser == null) {
            gotoFirst(); // LoginActivity
        } else {
            new loginTask().execute(authUser.user, authUser.password); // Query a MySQL remoto
        }
    }

    private class loginTask extends AsyncTask<String, String, String> {
        private String mUser;
        private String mPassword;

        protected void onPreExecute() {
            super.onPreExecute();
            //TODO: non ne ho idea, un progressdialog è fuori discussione, magari qualche check
        }

        protected String doInBackground(String... params) {
            mUser = params[0];
            mPassword = params[1];

            return LoginService.get().Login(mUser,mPassword);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            final MemberResponse memberResponse = jsonHandler.getUserData(result);

            if (memberResponse != null) {
                if (memberResponse.Response.Code == 100) {
                    final Member member = memberResponse.Member;
                    sqlHelper.getInstance(getApplicationContext()).addMember(member);
                    gotoMain();
                } else { // nel caso avessimo salvato un utente nella memoria persistente che sul web non esiste o non esiste più.
                    gotoFirst();
                }
            }
        }

    }

    private void gotoMain() {
        final Intent mainIntent = new Intent(MainActivity.MENU_ACTIVITY_ACTION);
        startActivity(mainIntent);
        finish();
    }

    private void gotoFirst() {
        final Intent intent = new Intent(firstAccessActivity.FIRST_ACTION);
        startActivity(intent);
        finish();
    }
}
