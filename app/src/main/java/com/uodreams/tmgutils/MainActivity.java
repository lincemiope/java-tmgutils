package com.uodreams.tmgutils;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.uodreams.tmgutils.conf.Const;
import com.uodreams.tmgutils.json.jsonHandler;
import com.uodreams.tmgutils.model.Login;
import com.uodreams.tmgutils.model.Member;
import com.uodreams.tmgutils.model.Request;
import com.uodreams.tmgutils.model.REST.MyRequestsResponse;
import com.uodreams.tmgutils.model.REST.SopsResponse;
import com.uodreams.tmgutils.model.Sop;
import com.uodreams.tmgutils.service.RequestsService;
import com.uodreams.tmgutils.service.SopsService;
import com.uodreams.tmgutils.utils.Misc;
import com.uodreams.tmgutils.db.sqlHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{
    public static final String MENU_ACTIVITY_ACTION = Const.PKG + ".action.MENU_ACTIVITY_ACTION";
    private static final int MYREQUESTS_REQUEST_ID = 1;
    private static final int MYACCOUNT_REQUEST_ID = 3;
    private static final int CHAAAMP_REQUEST_ID = 4;
    //private static final String TAG_LOG = MainActivity.class.getName();
    //private static final long DELAY_BETWEEN_QUERIES = 60 * 5; // 5 minutes
    //private boolean mIsFromRemote = false;
    //private ArrayList<Sop> mSops;
    private Button btnShowSops;
    private Button btnMyRequests;
    private Button btnAccount;
    private Button btnChamp;
    private TextView tvWelcome;
    private ProgressDialog pd;
    private long mBackPressedTime = 0L;
    private Member mMember;
    private Login mLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mMember = sqlHelper.getInstance(this).getMember();
        this.mLogin = sqlHelper.getInstance(this).getLogin();

        this.btnShowSops = (Button) findViewById(R.id.btnShowSops);

        this.btnShowSops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final sqlHelper db = sqlHelper.getInstance(MainActivity.this);
                if (!db.areTheyOnCD()) {
                    db.deleteSops();
                    new getSopsListTask().execute("" + MainActivity.this.mMember.userid, MainActivity.this.mLogin.password);
                } else {
                    handleListViewModel("OK");
                }
            }
        });

        this.btnAccount = (Button) findViewById(R.id.btnAccount);

        this.btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoMyAccount();
            }
        });

        this.btnChamp = (Button) findViewById(R.id.btnChamp);

        this.btnChamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoChaaamp();
            }
        });

        if (this.mMember.rank < 1)
            this.btnShowSops.setVisibility(View.GONE);

        this.btnMyRequests = (Button) findViewById(R.id.btnMyRequests);

        this.btnMyRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new getMyRequestsTask().execute("" + MainActivity.this.mMember.userid, MainActivity.this.mLogin.password);
            }
        });
        tvWelcome = (TextView) findViewById(R.id.tvWelcome);
        tvWelcome.setText(Misc.getWelcomeText(this.mMember));
    }

    private void gotoMyAccount(){
        final Intent myAccountIntent = new Intent(MyAccountActivity.MYACCOUNT_ACTION);
        startActivityForResult(myAccountIntent, MYACCOUNT_REQUEST_ID);

    }

    private void gotoChaaamp() {
        final Intent chaaampIntent = new Intent(ChampAlertActivity.CHAAAMP_ACTION);
        startActivityForResult(chaaampIntent, CHAAAMP_REQUEST_ID);
    }
    private void gotoMyRequests() {
        final Intent myRequestsIntent = new Intent(myRequestsActivity.MY_REQUESTS_ACTION);
        startActivityForResult(myRequestsIntent, MYREQUESTS_REQUEST_ID);
    }

    private class getSopsListTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage(getApplicationContext().getResources().getString(R.string.prog_collecting_sops));
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {
            final int userId = Integer.parseInt(params[0]);
            final String password = params[1];

            final String json = SopsService.get().GetSopsData(userId, password);

            final SopsResponse sopsResponse = jsonHandler.getSOPsData(json);

            if (sopsResponse == null)
                return "ERROR";

            if (sopsResponse.Response.Code == 100) {
                final ArrayList<Sop> sops = sopsResponse.Sops;
                final sqlHelper db = sqlHelper.getInstance(MainActivity.this);
                final long newCD = Misc.getTimestamp() + Const.SOP_REQUEST_DELAY;
                for (int i = 0; i < sops.size(); ++i) {
                    final Sop sop = sops.get(i);
                    db.insertSop(sop, newCD);
                }
                return "OK";
            } else {
                Misc.restToast(MainActivity.this, sopsResponse.Response.Message);
                return "KO";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()) {
                pd.dismiss();
            }
            handleListViewModel(result);
        }
    }

    private void handleListViewModel(final String result) {
        if (result.toUpperCase().contains("OK")) {
            goAhead();
        } else {
            Toast.makeText(MainActivity.this, R.string.error_collectin_data, Toast.LENGTH_SHORT).show();
        }
    }

    private void goAhead() {
        final Intent listIntent = new Intent(SopListActivity.SOPLIST_ACTION);
        startActivity(listIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        long t = System.currentTimeMillis();
        if (t - this.mBackPressedTime > 2000) {    // 2 secs
            this.mBackPressedTime = t;
            final String avviso_exit = getResources().getString(R.string.notif_double_back_logout);
            Toast.makeText(this, avviso_exit,
                    Toast.LENGTH_SHORT).show();
        } else {
            sqlHelper.getInstance(this).userLogout();
            final Intent firstIntent = new Intent(firstAccessActivity.FIRST_ACTION);
            startActivity(firstIntent);
            finish();
        }
    }

    /* MY REQUESTS */
    private class getMyRequestsTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {
            final int userId = Integer.parseInt(params[0]);
            final String password = params[1];

            final String json = RequestsService.get().ShowMine(userId, password);
            final MyRequestsResponse requestsResponse = jsonHandler.getMyRequests(json);

            if (requestsResponse == null)
                return "ERROR";

            if (requestsResponse.Response.Code == 100) {
                final sqlHelper db = sqlHelper.getInstance(MainActivity.this);
                final ArrayList<Request> myRequests = requestsResponse.Requests;
                db.truncateMyRequests();
                db.seedMyRequests(myRequests);

                return "OK";
            }

            Misc.restToast(MainActivity.this, requestsResponse.Response.Message);
            return "KO";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()) {
                pd.dismiss();
            }
            gotoMyRequests();
        }
    }
}
