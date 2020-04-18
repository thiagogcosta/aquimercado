package com.aquimercado.aquimercado;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;

import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by FabioNote on 27/02/2017.
 */

public class VolleyProvider{
    private static VolleyProvider instance;
    private RequestQueue queue;
    private HurlStack hurlStack;

    private VolleyProvider(Context ctx){
        try {
            InputStream instream = ctx.getApplicationContext().getResources()
                    .openRawResource(R.raw.keystore);
            KeyStore trustStore = KeyStore.getInstance("BKS");
            try {
                trustStore.load(instream, "aquisenha".toCharArray());

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {instream.close();} catch (Exception ignore) {}
            }

            String algorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory
                    .getInstance(algorithm);
            tmf.init(trustStore);
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);

            SSLSocketFactory sslFactory = context.getSocketFactory();
            hurlStack = new HurlStack(null, sslFactory);
            queue = Volley.newRequestQueue(ctx, hurlStack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static VolleyProvider getInstance(Context ctx){
        if(instance == null){
            instance = new VolleyProvider(ctx);
        }
        return instance;
    }

    public RequestQueue getQueue(){
        return this.queue;
    }

    public <T> Request<T> addRequest(Request<T> req) {
        return getQueue().add(req);
    }

    public <T> Request<T> addRequest(Request<T> req, String tag) {
        req.setTag(tag);
        return getQueue().add(req);
    }

}