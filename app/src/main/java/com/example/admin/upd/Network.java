package com.example.admin.upd;

import android.os.AsyncTask;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class Network extends AsyncTask<String, Integer, String> {
    @Override
    protected String doInBackground(String... urls) {
        Header[] header = null;

        for (String url : urls) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet get = new HttpGet();
            get.addHeader("Range", "bytes=0-0");
            URI website;
            HttpResponse response = null;
            try {
                website = new URI(url);
                get.setURI(website);
                response = httpClient.execute(get);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            header = response != null ? response.getHeaders("Last-Modified") : null;
        }

        return header != null ? header[0].getValue() : null;
    }
}
