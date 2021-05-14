package com.uodreams.tmgutils;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.uodreams.tmgutils.adapter.MyRequestsListAdapter;
import com.uodreams.tmgutils.conf.Const;
import com.uodreams.tmgutils.model.Login;
import com.uodreams.tmgutils.model.Member;
import com.uodreams.tmgutils.model.Request;
import com.uodreams.tmgutils.db.sqlHelper;
import com.uodreams.tmgutils.service.RequestsService;

import java.util.ArrayList;
import android.util.SparseArray;

public class myRequestsActivity extends AppCompatActivity {
    public static final String MY_REQUESTS_ACTION = Const.PKG + ".action.MY_REQUESTS_ACTION";
    private ListView lvMyRequests;
    private MyRequestsListAdapter lAdapter;
    private ArrayList<Request> myRequests;
    private ArrayList<Request> Unchangeables;
    private SparseArray<String> myCurrentUpdates;
    private Member mMember;
    private Login mLogin;
    private ProgressDialog pd;

    //private SparseArray<String> mSparseUpdatedRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_requests);
        lvMyRequests = (ListView) findViewById(R.id.lvMyRequests);
        final sqlHelper db = sqlHelper.getInstance(this);
        this.mMember = db.getMember();
        this.mLogin = db.getLogin();
        this.myRequests = db.getMyRequests();
        this.Unchangeables = db.getMyRequests();
        this.myCurrentUpdates = new SparseArray<>();
        lAdapter = new MyRequestsListAdapter(this, this.myRequests);
        lvMyRequests.setAdapter(lAdapter);
    }

    @Override
    public void onBackPressed() {
        this.setUpdateList();

        if (this.myCurrentUpdates.size() == 0)
            goBackToMain();
        else
            new updateRequestsTask().execute("" + this.mMember.userid, this.mLogin.password);
    }

    public void updateRequest(final Request request) {
        final int cnt = lAdapter.getCount();
        for (int i = 0; i < cnt; ++i) {
            final Request myrequest = lAdapter.getItem(i);
            if (myrequest != null && myrequest.skill.equals(request.skill) && myrequest.value == request.value) {
                lAdapter.getItem(i).userIds = reformatRequest(myrequest.userIds);
                break;
            }
        }
        lAdapter.notifyDataSetChanged();
    }

    public boolean isThereMyID(final String userIds) {
        final String myID = "" + this.mMember.userid;
        return (userIds.contains(myID));
    }

    private String reformatRequest(final String userIds) {
        final StringBuilder sbIds = new StringBuilder();
        final String myID = "" + this.mMember.userid;
        if (isThereMyID(userIds)) { // c'è e devo toglierlo
            if (userIds.contains(",")) {// è una lista
                String[] arrIds = userIds.split(",");
                for (int i = 0; i < arrIds.length; ++i) {
                    String strId = arrIds[i];
                    if (!strId.equals(myID)) {
                        sbIds.append(strId).append((i < arrIds.length - 1) ? "," : "");
                    }
                }
            }
        } else if (userIds.contains(",")) {// se non c'è e devo inserirlo && è una lista
            String[] arrIds = userIds.split(",");
            for (String strId : arrIds) {
                sbIds.append(strId).append(",");
            }
            sbIds.append(myID);
        } else if (userIds.length() > 0) {// se non c'è && ce n'è uno solo e non è il mio
            sbIds.append(userIds).append(",").append(myID);
        } else { // se non c'è && è una stringa vuota
            sbIds.append(myID);
        }

        return sbIds.toString();
    }
    private void setUpdateList() {
        for (int i = 0; i < this.myRequests.size(); ++i) {
            final Request request = this.myRequests.get(i);
            final Request unchangeable = this.Unchangeables.get(i);
            if (!request.userIds.equals(unchangeable.userIds)) { // non si equivalgono
                this.myCurrentUpdates.put(request.id, request.userIds);
            }
        }
    }

    public void shortPressToast(final boolean addOrRemove) {
        if (addOrRemove) {
            Toast.makeText(this, R.string.notif_long_press_add, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.notif_long_press_remove, Toast.LENGTH_SHORT).show();
        }
    }

    private class updateRequestsTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(myRequestsActivity.this);
            pd.setMessage(getApplicationContext().getResources().getString(R.string.prog_myrequests_update));
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {
            final int userId = Integer.parseInt(params[0]);
            final String password = params[1];

            return RequestsService.get().UpdateRequests(userId,password,myRequestsActivity.this.myCurrentUpdates);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()) {
                pd.dismiss();
            }
            goBackToMain();
        }
    }

    private void goBackToMain() {
        final Intent mainIntent = new Intent();
        setResult(RESULT_OK, mainIntent);
        finish();

    }
}
