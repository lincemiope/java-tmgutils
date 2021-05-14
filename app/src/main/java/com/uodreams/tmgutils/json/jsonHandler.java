package com.uodreams.tmgutils.json;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.uodreams.tmgutils.model.REST.MemberResponse;
import com.uodreams.tmgutils.model.REST.MyRequestsResponse;
import com.uodreams.tmgutils.model.REST.RequestVMResponse;
import com.uodreams.tmgutils.model.REST.SopsResponse;
import com.uodreams.tmgutils.model.Response;

import java.lang.reflect.Type;

public final class jsonHandler {
    private static final String TAG_LOG = jsonHandler.class.getName();

    public static SopsResponse getSOPsData(final String json) {
        final Gson g = new GsonBuilder().serializeNulls().create();
        final Type type = new TypeToken<SopsResponse>(){}.getType();
        try {
            return g.fromJson(json, type);
        } catch (Exception e) {
            Log.d(TAG_LOG, e.toString());
            return null;
        }
    }

    public static MemberResponse getUserData(final String json) {
        final Gson g = new GsonBuilder().serializeNulls().create();
        final Type type = new TypeToken<MemberResponse>(){}.getType();
        try {
            return g.fromJson(json, type);
        } catch (Exception e) {
            Log.d(TAG_LOG, e.toString());
            return null;
        }
    }

    public static RequestVMResponse getRequest(final String json) {
        final Gson g = new GsonBuilder().serializeNulls().create();
        final Type type = new TypeToken<RequestVMResponse>(){}.getType();
        try {
            return g.fromJson(json, type);
        } catch (Exception e) {
            Log.d(TAG_LOG, e.toString());
            return null;
        }
    }

    public static MyRequestsResponse getMyRequests(final String json) {
        final Gson g = new GsonBuilder().serializeNulls().create();
        final Type type = new TypeToken<MyRequestsResponse>(){}.getType();
        try {
            return g.fromJson(json,type);
        } catch (Exception e) {
            Log.d(TAG_LOG, e.toString());
            return null;
        }
    }

    public static Response getResponse(final String json) {
        final Gson g = new GsonBuilder().serializeNulls().create();
        final Type type = new TypeToken<Response>(){}.getType();
        try {
            return g.fromJson(json,type);
        } catch (Exception e) {
            Log.d(TAG_LOG, e.toString());
            return null;
        }
    }
}