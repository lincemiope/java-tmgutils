package com.uodreams.tmgutils.service;

import android.util.Log;
import android.util.SparseArray;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public final class RequestsService extends BaseService {
    private static final String TAG_LOG = RequestsService.class.getName();
    private static RequestsService instance;

    public synchronized static RequestsService get() {
        if (instance == null)
            instance = new RequestsService();
        return instance;
    }

    public String ShowMine(final int userId, final String pwd) {
        final String hash = (pwd.length() == 128) ? pwd : getHash(pwd);
        final String passwordCode = hash.substring(0,4) + hash.substring(hash.length() - 4);
        final String SOAP_ACTION = "https://tmgutils.azurewebsites.net/ShowMine";
        final String OPERATION_NAME = "ShowMine";

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

    public String UpdateRequests(final int userId, final String pwd, final SparseArray<String> myUpdates) {
        final String hash = (pwd.length() == 128) ? pwd : getHash(pwd);
        final String passwordCode = hash.substring(0,4) + hash.substring(hash.length() - 4);
        final String SOAP_ACTION = "https://tmgutils.azurewebsites.net/UpdateRequests";
        final String OPERATION_NAME = "UpdateRequests";
        final String REQUEST_UPDATES = "updates";
        final String REQUEST_UPDATE = "RequestUpdate";

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

        SoapObject requestUpdates = new SoapObject(WSDL_TARGET_NAMESPACE,REQUEST_UPDATES);

        final int cnt = myUpdates.size();

        for(int i = 0; i < cnt; ++i) {
            final int requestId = myUpdates.keyAt(i);

            if (requestId == -1) { // per sicurezza
                continue;
            }

            final String userIds = myUpdates.get(requestId);

            SoapObject requestUpdate = new SoapObject(WSDL_TARGET_NAMESPACE,REQUEST_UPDATE);

            /* requestID */
            pi = new PropertyInfo();
            pi.setName("requestId");
            pi.setValue(requestId);
            pi.setType(Integer.class);
            requestUpdate.addProperty(pi);
            /* userIds */
            pi = new PropertyInfo();
            pi.setName("userIds");
            pi.setValue(userIds);
            pi.setType(String.class);
            requestUpdate.addProperty(pi);

            requestUpdates.addSoapObject(requestUpdate);
        }

        request.addSoapObject(requestUpdates);

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

    /*public String AddRequests(final int userId, final String pwd, final String sopIds) {
        final String hash = (pwd.length() == 128) ? pwd : getHash(pwd);
        final String passwordCode = hash.substring(0,4) + hash.substring(hash.length() - 4);
        final String SOAP_ACTION = "https://tmgutils.azurewebsites.net/AddRequests";
        final String OPERATION_NAME = "AddRequests";

        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);
        PropertyInfo pi = new PropertyInfo();
        pi.setName("userid");
        pi.setValue(userId);
        pi.setType(Integer.class);
        request.addProperty(pi);
        pi = new PropertyInfo();
        pi.setName("passwordCode");
        pi.setValue(passwordCode);
        pi.setType(String.class);
        request.addProperty(pi);
        pi = new PropertyInfo();
        pi.setName("ids");
        pi.setValue(sopIds);
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

    public String DelRequests(final int userId, final String pwd, final String sopIds) {
        final String hash = (pwd.length() == 128) ? pwd : getHash(pwd);
        final String passwordCode = hash.substring(0,4) + hash.substring(hash.length() - 4);
        final String SOAP_ACTION = "https://tmgutils.azurewebsites.net/DelRequests";
        final String OPERATION_NAME = "DelRequests";

        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);
        PropertyInfo pi = new PropertyInfo();
        pi.setName("userid");
        pi.setValue(userId);
        pi.setType(Integer.class);
        request.addProperty(pi);
        pi = new PropertyInfo();
        pi.setName("passwordCode");
        pi.setValue(passwordCode);
        pi.setType(String.class);
        request.addProperty(pi);
        pi = new PropertyInfo();
        pi.setName("ids");
        pi.setValue(sopIds);
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
    }*/

    public String AdminDelRequests(final int myUserId, final String pwd, final int value, final String skill, final String theirUserIds) {
        final String hash = (pwd.length() == 128) ? pwd : getHash(pwd);
        final String passwordCode = hash.substring(0,4) + hash.substring(hash.length() - 4);
        final String SOAP_ACTION = "https://tmgutils.azurewebsites.net/AdminDelRequests";
        final String OPERATION_NAME = "AdminDelRequests";

        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);
        PropertyInfo pi = new PropertyInfo();
        pi.setName("myUserId");
        pi.setValue(myUserId);
        pi.setType(Integer.class);
        request.addProperty(pi);
        pi = new PropertyInfo();
        pi.setName("passwordCode");
        pi.setValue(passwordCode);
        pi.setType(String.class);
        request.addProperty(pi);
        pi = new PropertyInfo();
        pi.setName("value");
        pi.setValue(value);
        pi.setType(Integer.class);
        request.addProperty(pi);
        pi = new PropertyInfo();
        pi.setName("skill");
        pi.setValue(skill);
        pi.setType(String.class);
        request.addProperty(pi);
        pi = new PropertyInfo();
        pi.setName("theirUserIds");
        pi.setValue(theirUserIds);
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

    public String ShowRequestsForSop(final int userId, final String pwd, final int value, final String skill) {
        final String hash = (pwd.length() == 128) ? pwd : getHash(pwd);
        final String passwordCode = hash.substring(0,4) + hash.substring(hash.length() - 4);
        final String SOAP_ACTION = "https://tmgutils.azurewebsites.net/ShowRequestsForSop";
        final String OPERATION_NAME = "ShowRequestsForSop";

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
        pi.setName("value");
        pi.setValue(value);
        pi.setType(Integer.class);
        request.addProperty(pi);
        pi = new PropertyInfo();
        pi.setName("skill");
        pi.setValue(skill);
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
