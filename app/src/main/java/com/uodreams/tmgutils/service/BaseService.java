package com.uodreams.tmgutils.service;

import android.util.Log;

import com.uodreams.tmgutils.conf.Const;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class BaseService {
    static final String WSDL_TARGET_NAMESPACE = Const.WSDL_TARGET_NAMESPACE;
    static final String SOAP_ADDRESS = Const.SOAP_ADDRESS;

    String getHash(final String pwd) {
        try {
            final MessageDigest md = MessageDigest.getInstance("SHA-512");
            final byte[] digest = md.digest(pwd.getBytes());
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < digest.length; ++i) {
                sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ns) {
            Log.d("Hash Exception", ns.getMessage());
            return ""; // così riceverà l'errore da php
        }
    }
}
