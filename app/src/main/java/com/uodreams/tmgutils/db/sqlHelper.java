package com.uodreams.tmgutils.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.uodreams.tmgutils.model.Login;
import com.uodreams.tmgutils.model.Member;
import com.uodreams.tmgutils.model.Request;
import com.uodreams.tmgutils.model.Sop;
import com.uodreams.tmgutils.utils.Misc;

import java.util.ArrayList;

public class sqlHelper extends SQLiteOpenHelper {
    private static sqlHelper sInstance;
    private static final String TAG_LOG = sqlHelper.class.getName();
    // Database
    private static final String DATABASE_NAME = "TMGUtilsDB";
    private static final int DATABASE_VERSION = 2;
    // Tables
    private static final String TABLE_SOPS = "sops";
    private static final String TABLE_REQUESTS = "requests";
    private static final String TABLE_LOGIN = "login";
    private static final String TABLE_MEMBER = "member";
    private static final String TABLE_MYREQUESTS = "myrequests";
    // Columns
    public static final String KEY_SOPS_ID = "sop_id";
    public static final String KEY_SOPS_VALUE = "sop_value";
    public static final String KEY_SOPS_SKILL = "sop_skill";
    public static final String KEY_SOPS_DAYS = "sop_days";
    public static final String KEY_SOPS_CURRENT = "sop_current";
    public static final String KEY_SOPS_SERIAL = "sop_serial";
    public static final String KEY_SOPS_COOLDOWN = "sop_cooldown"; // CD prima di poter fare una nuova query al server

    public static final String KEY_REQUESTS_ID = "req_id";
    public static final String KEY_REQUESTS_SOP_VALUE = "req_value";
    public static final String KEY_REQUESTS_SOP_SKILL = "req_skill";
    public static final String KEY_REQUESTS_PLAYERS = "req_players";

    public static final String KEY_LOGIN_ID = "log_id";
    public static final String KEY_LOGIN_USER = "log_user";
    public static final String KEY_LOGIN_PASSWORD = "log_password";

    public static final String KEY_MEMBER_ID = "mem_id";
    public static final String KEY_MEMBER_USER = "mem_user";
    //public static final String KEY_MEMBER_PASSWORD = "mem_password";
    public static final String KEY_MEMBER_CHARACTER = "mem_character";
    public static final String KEY_MEMBER_RANK = "mem_rank";
    public static final String KEY_MEMBER_USERID = "mem_userid";
    public static final String KEY_MEMBER_ROLES = "mem_roles";

    public static final String KEY_MYREQUESTS_ID = "my_id";
    public static final String KEY_MYREQUESTS_SOP_VALUE = "my_value";
    public static final String KEY_MYREQUESTS_SOP_SKILL = "my_skill";
    public static final String KEY_MYREQUESTS_USERIDS = "my_userids";

    // Create Strings
    private static final String CREATE_TABLE_SOPS = "CREATE TABLE IF NOT EXISTS " +
            TABLE_SOPS +
            "(" +
            KEY_SOPS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            KEY_SOPS_VALUE + " INTEGER," +
            KEY_SOPS_SKILL + " TEXT," +
            KEY_SOPS_DAYS + " INTEGER," +
            KEY_SOPS_CURRENT + " INTEGER NOT NULL DEFAULT 0," +
            KEY_SOPS_SERIAL + " INTEGER UNIQUE," +
            KEY_SOPS_COOLDOWN + " LONG NOT NULL DEFAULT 0" +
            ")";
    private static final String CREATE_TABLE_REQUESTS = "CREATE TABLE IF NOT EXISTS " +
            TABLE_REQUESTS +
            "(" +
            KEY_REQUESTS_ID + " INTEGER PRIMARY KEY NOT NULL DEFAULT 1," +
            KEY_REQUESTS_SOP_VALUE + " INTEGER," +
            KEY_REQUESTS_SOP_SKILL + " TEXT," +
            KEY_REQUESTS_PLAYERS + " TEXT" +
            ")";

    private static final String CREATE_TABLE_LOGIN = "CREATE TABLE IF NOT EXISTS " +
            TABLE_LOGIN +
            "(" +
            KEY_LOGIN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            KEY_LOGIN_USER + " TEXT UNIQUE," +
            KEY_LOGIN_PASSWORD + " TEXT" +
            ")";

    private static final String CREATE_TABLE_MEMBER = "CREATE TABLE IF NOT EXISTS " +
            TABLE_MEMBER +
            "(" +
            KEY_MEMBER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            KEY_MEMBER_USER + " TEXT UNIQUE," +
            //KEY_MEMBER_PASSWORD + " TEXT," +
            KEY_MEMBER_CHARACTER + " TEXT UNIQUE," +
            KEY_MEMBER_RANK + " INTEGER," +
            KEY_MEMBER_USERID + " INTEGER," +
            KEY_MEMBER_ROLES + " TEXT" +
            ")";

    private static final String CREATE_TABLE_MYREQUESTS = "CREATE TABLE IF NOT EXISTS " +
            TABLE_MYREQUESTS +
            "(" +
            KEY_MYREQUESTS_ID + " INTEGER PRIMARY KEY NOT NULL DEFAULT 1," +
            KEY_MYREQUESTS_SOP_VALUE + " INTEGER," +
            KEY_MYREQUESTS_SOP_SKILL + " TEXT," +
            KEY_MYREQUESTS_USERIDS + " TEXT" +
            ")";

    // Drop Strings
    private static final String DROP_TABLE_SOPS = "DROP TABLE IF EXISTS " + TABLE_SOPS;
    private static final String DROP_TABLE_REQUESTS = "DROP TABLE IF EXISTS " + TABLE_REQUESTS;
    private static final String DROP_TABLE_LOGIN = "DROP TABLE IF EXISTS " + TABLE_LOGIN;
    private static final String DROP_TABLE_MEMBER = "DROP TABLE IF EXISTS " + TABLE_MEMBER;
    private static final String DROP_TABLE_MYREQUESTS = "DROP TABLE IF EXISTS " + TABLE_MYREQUESTS;

    public static synchronized sqlHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new sqlHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private sqlHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SOPS);
        db.execSQL(CREATE_TABLE_REQUESTS);
        db.execSQL(CREATE_TABLE_LOGIN);
        db.execSQL(CREATE_TABLE_MEMBER);
        db.execSQL(CREATE_TABLE_MYREQUESTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(DROP_TABLE_SOPS);
        db.execSQL(DROP_TABLE_REQUESTS);
        db.execSQL(DROP_TABLE_LOGIN);
        db.execSQL(DROP_TABLE_MEMBER);
        db.execSQL(DROP_TABLE_MYREQUESTS);
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    /* QUERIES */
    public boolean sopExists(final int serial) {
        boolean result = false;
        SQLiteDatabase db = getReadableDatabase();

        db.beginTransaction();
        final Cursor c = db.query(TABLE_SOPS,null,KEY_SOPS_SERIAL + "=?",new String[] { "" + serial},null,null,null);

        try {
            if (c.moveToFirst()) {
                result = true;
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG_LOG, e.toString());
        } finally {
            if (c != null && !c.isClosed())
                c.close();
            db.endTransaction();
        }
        return result;
    }

    public ArrayList<Sop> selectSops() {
        return selectSops("","");
    }

    public ArrayList<Sop> selectSops(final String order, final String tableKey) {
        final String orderBy = (!order.isEmpty() && !tableKey.isEmpty()) ? tableKey + " " + order : null;
        final ArrayList<Sop> result = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        db.beginTransaction();
        Cursor c = db.query(TABLE_SOPS,null,KEY_SOPS_DAYS + ">?",new String[] {"0"},null,null,orderBy);
        try {
            if (c.moveToFirst()) {
                final int cnt = c.getCount();
                for (int i = 0; i < cnt; ++i) {
                    final Sop sop = new Sop();
                    sop.id = c.getInt(c.getColumnIndex(KEY_SOPS_ID));
                    sop.value = c.getInt(c.getColumnIndex(KEY_SOPS_VALUE));
                    sop.skill = c.getString(c.getColumnIndex(KEY_SOPS_SKILL));
                    sop.days = c.getInt(c.getColumnIndex(KEY_SOPS_DAYS));
                    sop.serial = c.getInt(c.getColumnIndex(KEY_SOPS_SERIAL));
                    //sop.current = c.getInt(c.getColumnIndex(KEY_SOPS_CURRENT));

                    result.add(sop);
                    c.moveToNext();
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG_LOG, e.toString());
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
            db.endTransaction();
        }

        return result;
    }

    public boolean areTheyOnCD() {
        boolean result = false;
        SQLiteDatabase db = getReadableDatabase();
        db.beginTransaction();

        try {
            final long now = Misc.getTimestamp();
            final Cursor c = db.query(TABLE_SOPS, new String[]{KEY_SOPS_SERIAL}, KEY_SOPS_COOLDOWN + ">?", new String[]{"" + now}, null, null, null);
            result = c.getCount() > 0;
            if (c != null && !c.isClosed())
                c.close();
            db.setTransactionSuccessful();
        } catch (Exception ex) {
            Log.d(TAG_LOG, ex.toString());
        } finally {
            db.endTransaction();
        }
        return result;
    }

    public void deleteSops() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_SOPS, null, null);
            db.setTransactionSuccessful();
        } catch (Exception ex) {
            Log.d(TAG_LOG, ex.toString());
        } finally {
            db.endTransaction();
        }
    }

    public void insertSop(final Sop sop, long newCD) {
        if (sop.days < 0)
            return;

        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            final ContentValues cv = new ContentValues();
            cv.put(KEY_SOPS_COOLDOWN, newCD);
            if (sopExists(sop.serial)) {
                db.update(TABLE_SOPS,cv,KEY_SOPS_SERIAL + "=?",new String[] {"" + sop.serial});
            } else {
                cv.put(KEY_SOPS_VALUE, sop.value);
                cv.put(KEY_SOPS_SKILL, sop.skill);
                cv.put(KEY_SOPS_DAYS, sop.days);
                cv.put(KEY_SOPS_SERIAL, sop.serial);
                db.insert(TABLE_SOPS, null, cv);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG_LOG, e.toString());
        } finally {
            db.endTransaction();
        }
    }

    public void setSopAsRequested(final Sop sop) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            final ContentValues cv = new ContentValues();
            cv.put(KEY_SOPS_CURRENT, 0);
            db.update(TABLE_SOPS,cv,null,null);
            cv.clear();
            cv.put(KEY_SOPS_CURRENT, 1);
            db.update(TABLE_SOPS,cv,KEY_SOPS_ID + "=?",new String[] { "" + sop.id });
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG_LOG, e.toString());
        } finally {
            db.endTransaction();
        }
    }

    public void userLogout() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_LOGIN, null, null);
            db.delete(TABLE_MEMBER, null, null);
            db.setTransactionSuccessful();
        } catch (Exception ex) {
            Log.d(TAG_LOG, ex.toString());
        } finally {
            db.endTransaction();
        }
    }

    public void userLogin(final String user, final String password) {
        if (getLogin() != null)
            userLogout();
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            final ContentValues cv = new ContentValues();
            cv.put(KEY_LOGIN_USER, user);
            cv.put(KEY_LOGIN_PASSWORD, password);
            db.insert(TABLE_LOGIN, null, cv);
            db.setTransactionSuccessful();
        } catch (Exception ex) {
            Log.d(TAG_LOG, ex.toString());
        } finally {
            db.endTransaction();
        }
    }

    public Login getLogin() {
        Login result = null;
        SQLiteDatabase db = getReadableDatabase();
        db.beginTransaction();
        final Cursor c = db.query(TABLE_LOGIN,null,null,null,null,null,KEY_LOGIN_ID + " DESC");
        try {
            if (c.moveToFirst()) {
                result = new Login();
                result.id = c.getInt(c.getColumnIndex(KEY_LOGIN_ID));
                result.user = c.getString(c.getColumnIndex(KEY_LOGIN_USER));
                result.password = c.getString(c.getColumnIndex(KEY_LOGIN_PASSWORD));
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG_LOG, e.toString());
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
            db.endTransaction();
        }
        return result;
    }

    public void addMember(final Member member) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_MEMBER,null,null);
            final ContentValues cv = new ContentValues();
            cv.put(KEY_MEMBER_USER, member.user);
            cv.put(KEY_MEMBER_CHARACTER, member.alias);
            cv.put(KEY_MEMBER_RANK, member.rank);
            cv.put(KEY_MEMBER_USERID, member.userid);
            cv.put(KEY_MEMBER_ROLES, member.roles);

            final long rows = db.insert(TABLE_MEMBER,null,cv);
            Log.i(TAG_LOG, "addMember Rows: " + rows);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG_LOG, e.toString());
        } finally {
            db.endTransaction();
        }
    }

    public Member getMember() {
        Member result = null;

        SQLiteDatabase db = getReadableDatabase();
        db.beginTransaction();
        final Cursor c = db.query(TABLE_MEMBER,null,null,null,null,null,KEY_MEMBER_ID + " DESC");
        try {
            if (c.moveToFirst()) {
                result = new Member();
                result.id = c.getInt(c.getColumnIndex(KEY_MEMBER_ID));
                result.user = c.getString(c.getColumnIndex(KEY_MEMBER_USER));
                result.alias = c.getString(c.getColumnIndex(KEY_MEMBER_CHARACTER));
                result.rank = c.getInt(c.getColumnIndex(KEY_MEMBER_RANK));
                result.userid = c.getInt(c.getColumnIndex(KEY_MEMBER_USERID));
                result.roles = c.getString(c.getColumnIndex(KEY_MEMBER_ROLES));
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG_LOG, e.toString());
        }finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
            db.endTransaction();
        }

        return result;
    }

    public void editRoles(final String newRoles) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            final ContentValues cv = new ContentValues();
            cv.put(KEY_MEMBER_ROLES, newRoles);
            db.update(TABLE_MEMBER,cv,null,null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG_LOG, e.toString());
        } finally {
            db.endTransaction();
        }
    }

    public void truncateMyRequests() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_MYREQUESTS,null,null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG_LOG, e.toString());
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<Request> getMyRequests() {
        SQLiteDatabase db = getReadableDatabase();
        db.beginTransaction();
        ArrayList<Request> result = new ArrayList<>();
        final Cursor c = db.query(TABLE_MYREQUESTS,null,null,null,null,null,null);
        try {
            if (c.moveToFirst()) {
                final int cnt = c.getCount();
                for (int i = 0; i < cnt; ++i) {
                    final Request myRequest = new Request();
                    myRequest.id = c.getInt(c.getColumnIndex(KEY_MYREQUESTS_ID));
                    myRequest.value = c.getInt(c.getColumnIndex(KEY_MYREQUESTS_SOP_VALUE));
                    myRequest.skill = c.getString(c.getColumnIndex(KEY_MYREQUESTS_SOP_SKILL));
                    myRequest.userIds = c.getString(c.getColumnIndex(KEY_MYREQUESTS_USERIDS));

                    result.add(myRequest);
                    c.moveToNext();
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG_LOG, e.toString());
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
            db.endTransaction();
        }
        return result;
    }

    public void seedMyRequests(final ArrayList<Request> myRequests) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.beginTransaction();

            for (int i = 0; i < myRequests.size(); ++i) {
                final Request myRequest = myRequests.get(i);
                final ContentValues cv = new ContentValues();
                cv.put(KEY_MYREQUESTS_ID, myRequest.id);
                cv.put(KEY_MYREQUESTS_SOP_VALUE, myRequest.value);
                cv.put(KEY_MYREQUESTS_SOP_SKILL, myRequest.skill);
                cv.put(KEY_MYREQUESTS_USERIDS, myRequest.userIds);
                db.insert(TABLE_MYREQUESTS,null,cv);
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG_LOG, e.toString());
        } finally {
            db.endTransaction();
        }
    }
}
