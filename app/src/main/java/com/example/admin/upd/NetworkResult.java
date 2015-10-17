package com.example.admin.upd;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

/**
 * Created by root on 10/17/15.
 */
public class NetworkResult {

    public Date getNetworkResult() {
        Network net = new Network();
        String s = null;
        try {
            s = net.execute(Constants.Http).get();
        } catch (Exception e) {
            return null;
        }

        return headerToDate(s);
    }

    protected Date headerToDate(String value) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.httpDateFormat, Locale.US);
        try {
            return sdf.parse(value);
        } catch (ParseException e) {
            return null;
        }
    }
}
