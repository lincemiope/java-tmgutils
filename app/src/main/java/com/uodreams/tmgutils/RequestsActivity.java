package com.uodreams.tmgutils;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.uodreams.tmgutils.conf.Const;
import com.uodreams.tmgutils.conf.IIntend;
import com.uodreams.tmgutils.db.sqlHelper;
import com.uodreams.tmgutils.model.Login;
import com.uodreams.tmgutils.model.Member;
import com.uodreams.tmgutils.model.RequestViewModel;
import com.uodreams.tmgutils.model.requestUser;
import com.uodreams.tmgutils.adapter.RequestsModelAdapter;
import com.uodreams.tmgutils.service.RequestsService;
import com.uodreams.tmgutils.utils.Misc;

import java.util.ArrayList;

public class RequestsActivity extends AppCompatActivity implements IIntend {
    public static final String REQUESTS_ACTION = Const.PKG + ".action.REQUESTS_ACTION";

    private RequestViewModel mRequestViewModel;
    private ArrayList<requestUser> mRUsers;
    private Member mMember;
    //private Login mLogin;

    private TextView tvDisplayID;
    private ListView lvRequests;

    //private int startingUsers = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);
        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this.mRequestViewModel = bundle.getParcelable(REQUESTS_DATA_EXTRA);
        }

        final sqlHelper db = sqlHelper.getInstance(this);
        this.mMember = db.getMember();
        //this.mLogin = db.getLogin();

        if (mRequestViewModel != null) {
            tvDisplayID = (TextView) findViewById(R.id.tvDisplayID);
            tvDisplayID.setText(String.format("%d %s", mRequestViewModel.value, mRequestViewModel.skill));
            mRUsers = mRequestViewModel.characters;

            if (mRUsers != null && !mRUsers.isEmpty()) {
                final RequestsModelAdapter rAdapter = new RequestsModelAdapter(this,mRUsers);
                lvRequests = (ListView) findViewById(R.id.lvRequests);
                lvRequests.setAdapter(rAdapter);
                //this.startingUsers = this.mRUsers.size();
            }
        }
    }

    @Override
    public void onBackPressed() {
        /*if (this.mRUsers != null && this.mRUsers.size() != this.startingUsers) {
            final StringBuilder sb = new StringBuilder();

            for (int i = 0; i < this.mRUsers.size(); ++i) {
                final requestUser user = this.mRUsers.get(i);
                final String delimiter = (i < this.mRUsers.size() - 1) ? "," : "";
                sb.append(user.userId).append(delimiter);
            }

            new updateRequestsTask().execute("" + this.mMember.userid, this.mLogin.password, "" + this.mRequestViewModel.value, this.mRequestViewModel.skill, sb.toString());
        } else {*/
            goBackToSops();
        //}
    }

    private void goBackToSops() {
        final Intent sopsIntent = new Intent();
        setResult(RESULT_OK,sopsIntent);
        finish();
    }
    /*public void removeRequest(final int position) {
        if (this.mRUsers == null || this.mRUsers.isEmpty())
            return;

        this.mRUsers.remove(position);
    }*/

    private class updateRequestsTask extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            //TODO: Aggiungere progressdialog quandro implementerò la funzione rimuovi richieta per gm e cogm
        }

        protected String doInBackground(String... params) {
            final int myUserId = Integer.parseInt(params[0]);
            final String password = params[1];
            final int value = Integer.parseInt(params[2]);
            final String skill = params[3];
            final String theirUserIds = params[4];

            return RequestsService.get().AdminDelRequests(myUserId,password,value,skill,theirUserIds);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result.toUpperCase().contains("ERROR RANK")) {
                showRankToast();
            } else if (result.toUpperCase().contains("ERROR")) {
                showErrorToast();
            }

            goBackToSops();

        }
    }

    private void showRankToast() {
        final String errorString = getResources().getString(R.string.error_rank_too_low, Misc.getRankName(2)) + " (" + this.mMember.rank + ")";
        Toast.makeText(this,errorString,Toast.LENGTH_SHORT).show();
    }

    private void showErrorToast() {
        Toast.makeText(this, R.string.error_general_error, Toast.LENGTH_SHORT).show();
    }
}
