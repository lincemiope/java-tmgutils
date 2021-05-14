package com.uodreams.tmgutils;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.uodreams.tmgutils.conf.Const;
import com.uodreams.tmgutils.conf.IIntend;
import com.uodreams.tmgutils.db.sqlHelper;
import com.uodreams.tmgutils.model.Login;
import com.uodreams.tmgutils.model.Member;
import com.uodreams.tmgutils.model.REST.RequestVMResponse;
import com.uodreams.tmgutils.model.RequestViewModel;
import com.uodreams.tmgutils.model.Sop;
import com.uodreams.tmgutils.service.RequestsService;
import com.uodreams.tmgutils.adapter.SopsModelAdapter;
import com.uodreams.tmgutils.json.jsonHandler;

import java.util.ArrayList;
import java.util.HashMap;

public class SopListActivity extends AppCompatActivity implements IIntend {
    private static final String TAG_LOG = SopListActivity.class.getName();
    public static final String SOPLIST_ACTION = Const.PKG + ".action.SOPLIST_ACTION";
    private static final int REQUESTS_REQUEST_ID = 1;

    private ArrayList<Sop> mSops;
    private Member mMember;
    private Login mLogin;
    private HashMap<String, Boolean> mAscDesc;

    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sop_list);

        final sqlHelper db = sqlHelper.getInstance(SopListActivity.this);
        this.mMember = db.getMember();
        this.mSops = db.selectSops();
        this.mLogin = db.getLogin();

        this.mAscDesc = new HashMap<>();
        this.mAscDesc.put(sqlHelper.KEY_SOPS_VALUE, false);
        this.mAscDesc.put(sqlHelper.KEY_SOPS_SKILL, false);
        this.mAscDesc.put(sqlHelper.KEY_SOPS_DAYS, false);

        //final SopsAdapter adapter = new SopsAdapter(this, this.mSopData);
        final SopsModelAdapter adapter = new SopsModelAdapter(this, this.mSops);

        final ListView lvSopList = (ListView) findViewById(R.id.lvSopList);

        lvSopList.setAdapter(adapter);
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUESTS_REQUEST_ID) {
            if (resultCode == RESULT_OK) {

            }
        }
    }*/

    public void goToRequests(final RequestViewModel requestViewModel) {
        final Intent requestIntent = new Intent(RequestsActivity.REQUESTS_ACTION); // this, class
        final Bundle bundle = new Bundle();
        bundle.putParcelable(REQUESTS_DATA_EXTRA, requestViewModel);
        requestIntent.putExtras(bundle);
        startActivityForResult(requestIntent, REQUESTS_REQUEST_ID);
    }

    public void queryForRequests(final Sop sop) {
        sqlHelper.getInstance(this).setSopAsRequested(sop);
        new requestsTask().execute("" + this.mMember.userid, this.mLogin.password, "" + sop.value, sop.skill);
    }

    private class requestsTask extends AsyncTask<String, String, String> {
        private int mValue;
        private String mSkill;
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(SopListActivity.this);
            pd.setMessage(getApplicationContext().getResources().getString(R.string.prog_collecting_requests));
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {
            final int userId = Integer.parseInt(params[0]);
            final String password = params[1];
            this.mValue = Integer.parseInt(params[2]);
            this.mSkill = params[3];

            return RequestsService.get().ShowRequestsForSop(userId, password, this.mValue, this.mSkill);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (pd.isShowing())
                pd.dismiss();

            final RequestVMResponse requestVMResponse = jsonHandler.getRequest(result);

            if (requestVMResponse == null) {
                showNoRequestsToask();
                return;
            }

            if (requestVMResponse.Response.Code == 100) {
                final RequestViewModel requestViewModel = requestVMResponse.Request;
                goToRequests(requestViewModel);
            } else {
                showNoRequestsToask();
            }
        }

    }

    private void showNoRequestsToask() {
        Toast.makeText(this,R.string.notif_no_requests,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        final Intent mainIntent = new Intent(MainActivity.MENU_ACTIVITY_ACTION);
        startActivity(mainIntent);
        finish();
    }

    public void btnValue_Click(View v) {
        buttonAction(sqlHelper.KEY_SOPS_VALUE);
    }

    public void btnSkill_Click(View v) {
        buttonAction(sqlHelper.KEY_SOPS_SKILL);
    }

    public void btnDays_Click(View v) {
        buttonAction(sqlHelper.KEY_SOPS_DAYS);
    }

    private void buttonAction(final String key) {
        try {
            final String ascdesc = (this.mAscDesc.get(key)) ? "ASC" : "DESC";
            resetAscDesc(key);
            final sqlHelper sql = sqlHelper.getInstance(getApplicationContext());
            this.mSops = sql.selectSops(ascdesc, key);
            final SopsModelAdapter adapter = new SopsModelAdapter(this, this.mSops);
            final ListView lvSopList = (ListView) findViewById(R.id.lvSopList);
            lvSopList.setAdapter(adapter);
        } catch (Exception e) {
            Log.d(TAG_LOG, e.toString());
        }
    }
    private void resetAscDesc(final String key) {
        try {
            final boolean value = this.mAscDesc.get(key);
            this.mAscDesc.put(key, !value);
        } catch (Exception e) {
            Log.d(TAG_LOG, e.toString());
        }
    }
}
