package com.uodreams.tmgutils;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.uodreams.tmgutils.conf.Const;
import com.uodreams.tmgutils.model.Login;
import com.uodreams.tmgutils.model.Member;
import com.uodreams.tmgutils.db.sqlHelper;
import com.uodreams.tmgutils.model.Response;
import com.uodreams.tmgutils.service.LoginService;
import com.uodreams.tmgutils.utils.Misc;
import com.uodreams.tmgutils.json.jsonHandler;

public class MyAccountActivity extends AppCompatActivity {
    public static final String MYACCOUNT_ACTION = Const.PKG + ".action.MYACCOUNT_ACTION";
    private Member mMember;
    private Login mLogin;
    private ProgressDialog pd;
    //private MyAccountListAdapter lAdapter;

    private ImageView ivMyRolePVM;
    private ImageView ivMyRolePVP;
    private ImageView ivMyRoleHidder;

    private String mRoles = "000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        this.mMember = sqlHelper.getInstance(this).getMember();
        this.mLogin = sqlHelper.getInstance(this).getLogin();

        if (this.mMember != null) {
            final TextView tvDataUser = (TextView) findViewById(R.id.tvDataUser);
            final TextView tvDataAlias = (TextView) findViewById(R.id.tvDataAlias);
            final TextView tvDataRank = (TextView) findViewById(R.id.tvDataRank);

            tvDataUser.setText(this.mMember.user);
            tvDataAlias.setText(this.mMember.alias);
            tvDataRank.setText(Misc.getRankName(this.mMember.rank));

            ivMyRolePVM = (ImageView) findViewById(R.id.ivMyRolePVM);
            ivMyRolePVP = (ImageView) findViewById(R.id.ivMyRolePVP);
            ivMyRoleHidder = (ImageView) findViewById(R.id.ivMyRoleHidder);

            this.mRoles = this.mMember.roles;
            if (this.mMember.roles.charAt(0) == '1') {
                ivMyRolePVM.setTag(R.drawable.role_my_pvm);
                ivMyRolePVM.setImageResource(R.drawable.role_my_pvm);
            } else {
                ivMyRolePVM.setTag(R.drawable.role_my_non_pvm);
                ivMyRolePVM.setImageResource(R.drawable.role_my_non_pvm);
            }

            if (this.mMember.roles.charAt(1) == '1') {
                ivMyRolePVP.setTag(R.drawable.role_my_pvp);
                ivMyRolePVP.setImageResource(R.drawable.role_my_pvp);
            } else {
                ivMyRolePVP.setTag(R.drawable.role_my_non_pvp);
                ivMyRolePVP.setImageResource(R.drawable.role_my_non_pvp);
            }

            if (this.mMember.roles.charAt(2) == '1') {
                ivMyRoleHidder.setTag(R.drawable.role_my_hidder);
                ivMyRoleHidder.setImageResource(R.drawable.role_my_hidder);
            } else {
                ivMyRoleHidder.setTag(R.drawable.role_my_non_hidder);
                ivMyRoleHidder.setImageResource(R.drawable.role_my_non_hidder);
            }

            ivMyRolePVM.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if ((Integer) ivMyRolePVM.getTag() == R.drawable.role_my_pvm) {
                        ivMyRolePVM.setTag(R.drawable.role_my_non_pvm);
                        ivMyRolePVM.setImageResource(R.drawable.role_my_non_pvm);
                    } else {
                        ivMyRolePVM.setTag(R.drawable.role_my_pvm);
                        ivMyRolePVM.setImageResource(R.drawable.role_my_pvm);
                    }
                    return true;
                }
            });

            ivMyRolePVP.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if ((Integer) ivMyRolePVP.getTag() == R.drawable.role_my_pvp) {
                        ivMyRolePVP.setTag(R.drawable.role_my_non_pvp);
                        ivMyRolePVP.setImageResource(R.drawable.role_my_non_pvp);
                    } else {
                        ivMyRolePVP.setTag(R.drawable.role_my_pvp);
                        ivMyRolePVP.setImageResource(R.drawable.role_my_pvp);
                    }
                    return true;
                }
            });

            ivMyRoleHidder.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if ((Integer) ivMyRoleHidder.getTag() == R.drawable.role_my_hidder) {
                        ivMyRoleHidder.setTag(R.drawable.role_my_non_hidder);
                        ivMyRoleHidder.setImageResource(R.drawable.role_my_non_hidder);
                    } else {
                        ivMyRoleHidder.setTag(R.drawable.role_my_hidder);
                        ivMyRoleHidder.setImageResource(R.drawable.role_my_hidder);
                    }
                    return true;
                }
            });
        }
    }

    /*private void seedList() {
        final ListView lvMyData = (ListView) findViewById(R.userId.lvMyData);
        final ArrayList<HashMap<String,String>> arrList = new ArrayList<>();

        HashMap<String,String> data = new HashMap<>();
        data.put("key","Username:");
        data.put("value",this.mMember.user);
        arrList.add(data);

        data = new HashMap<>();
        data.put("key","Alias:");
        data.put("value", this.mMember.alias);
        arrList.add(data);

        data = new HashMap<>();
        data.put("key","Rank:");
        data.put("value", Misc.getRankName(this.mMember.rank));
        arrList.add(data);

        this.lAdapter = new MyAccountListAdapter(this,arrList);
        lvMyData.setAdapter(lAdapter);
    }*/

    @Override
    public void onBackPressed() {
        if (this.hasChanged()) {
            new setRolesTask().execute("" + this.mMember.userid, this.mLogin.password, this.mRoles);
        } else {
            goBackToMain();
        }
    }

    private void goBackToMain() {
        final Intent mainIntent = new Intent();
        setResult(RESULT_OK, mainIntent);
        finish();
    }

    public void ivOnClick(View view) {
        final ImageView img = (ImageView) view;
        final String txt = img.getContentDescription().toString();

        Toast.makeText(this,txt,Toast.LENGTH_SHORT).show();
    }

    private boolean hasChanged() {
        final String pvm = ((Integer) this.ivMyRolePVM.getTag() == R.drawable.role_my_pvm) ? "1" : "0";
        final String pvp = ((Integer) this.ivMyRolePVP.getTag() == R.drawable.role_my_pvp) ? "1" : "0";
        final String hidder = ((Integer) this.ivMyRoleHidder.getTag() == R.drawable.role_my_hidder) ? "1" : "0";

        final String roles = pvm + pvp + hidder;

        this.mRoles = roles;

        return !roles.equals(this.mMember.roles);
    }

    private class setRolesTask extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(MyAccountActivity.this);
            pd.setMessage(getApplicationContext().getResources().getString(R.string.prog_roles_update));
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {
            final int userId = Integer.parseInt(params[0]);
            final String password = params[1];
            final String roles = params[2];

            return LoginService.get().SetRoles(userId,password,roles);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (pd.isShowing())
                pd.dismiss();

            final Response response = jsonHandler.getResponse(result);
            if (response != null && response.Code == 100) {
                final sqlHelper sql = sqlHelper.getInstance(getApplicationContext());
                sql.editRoles(mRoles);
                goBackToMain();
            } else {
                unsuccessfulUpdate();
            }
        }
    }

    private void unsuccessfulUpdate() {
        Toast.makeText(this,R.string.error_collectin_data,Toast.LENGTH_SHORT).show();
        goBackToMain();
    }
}
