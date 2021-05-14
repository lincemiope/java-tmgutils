package com.uodreams.tmgutils.service;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public final class LoginService extends BaseService {
    private static final String TAG_LOG = LoginService.class.getName();
    private static LoginService instance;

    public synchronized static LoginService get() {
        if (instance == null)
            instance = new LoginService();
        return instance;
    }

    public String Login(final String user, final String pwd) {
        final String hash = (pwd.length() == 128) ? pwd : getHash(pwd);
        final String SOAP_ACTION = "https://tmgutils.azurewebsites.net/Login";
        final String OPERATION_NAME = "Login";

        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);
        PropertyInfo pi = new PropertyInfo();
        pi.setName("user");
        pi.setValue(user);
        pi.setType(String.class);
        request.addProperty(pi);
        pi = new PropertyInfo();
        pi.setName("password");
        pi.setValue(hash);
        pi.setType(String.class);
        request.addProperty(pi);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        Object response;

        try
        {
            httpTransport.call(SOAP_ACTION, envelope);
            response = envelope.getResponse();
            Log.i(TAG_LOG, response.toString());
        }
        catch (Exception exception)
        {
            Log.d(TAG_LOG, exception.toString());
            response = "ERRORCONN";
        }
        return response.toString();
    }

    public String Register(final String user, final String pwd, final String code, final String alias) {
        final String hash = (pwd.length() == 128) ? pwd : getHash(pwd);
        final String SOAP_ACTION = "https://tmgutils.azurewebsites.net/Register";
        final String OPERATION_NAME = "Register";

        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);
        PropertyInfo pi = new PropertyInfo();
        pi.setName("user");
        pi.setValue(user);
        pi.setType(String.class);
        request.addProperty(pi);
        pi = new PropertyInfo();
        pi.setName("password");
        pi.setValue(hash);
        pi.setType(String.class);
        request.addProperty(pi);
        pi = new PropertyInfo();
        pi.setName("code");
        pi.setValue(code);
        pi.setType(String.class);
        request.addProperty(pi);
        pi = new PropertyInfo();
        pi.setName("alias");
        pi.setValue(alias);
        pi.setType(String.class);
        request.addProperty(pi);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        Object response;

        try
        {
            httpTransport.call(SOAP_ACTION, envelope);
            response = envelope.getResponse();
            Log.i(TAG_LOG, response.toString());
        }
        catch (Exception exception)
        {
            Log.d(TAG_LOG, exception.toString());
            response = "ERRORCONN";
        }
        return response.toString();
    }

    public String SetRoles(final int userId, final String pwd, final String roles) {
        final String hash = (pwd.length() == 128) ? pwd : getHash(pwd);
        final String passwordCode = hash.substring(0,4) + hash.substring(hash.length() - 4);
        final String SOAP_ACTION = "https://tmgutils.azurewebsites.net/SetRoles";
        final String OPERATION_NAME = "SetRoles";

        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);
        PropertyInfo pi = new PropertyInfo();
        pi.setName("userId");
        pi.setValue(userId);
        pi.setType(Integer.class);
        request.addProperty(pi);
        pi = new PropertyInfo();
        pi.setName("passwordCode");
        pi.setValue(passwordCode);
        pi.setType(String.class);
        request.addProperty(pi);
        pi = new PropertyInfo();
        pi.setName("roles");
        pi.setValue(roles);
        pi.setType(String.class);
        request.addProperty(pi);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        Object response;

        try
        {
            httpTransport.call(SOAP_ACTION, envelope);
            response = envelope.getResponse();
            Log.i(TAG_LOG, response.toString());
        }
        catch (Exception exception)
        {
            Log.d(TAG_LOG, exception.toString());
            response = "ERRORCONN";
        }
        return response.toString();
    }
}
