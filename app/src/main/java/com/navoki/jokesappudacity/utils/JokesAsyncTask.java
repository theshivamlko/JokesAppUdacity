package com.navoki.jokesappudacity.utils;

import android.os.AsyncTask;

import com.example.migration.endpoints.backend.myApi.MyApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.navoki.jokesappudacity.interfaces.OnAsynResponse;

import java.io.IOException;

public class JokesAsyncTask extends AsyncTask<String, String, String> {
    private static MyApi myApiService = null;

    private final OnAsynResponse onAsynResponse;

    public JokesAsyncTask(OnAsynResponse onAsynResponse) {
        this.onAsynResponse = onAsynResponse;
    }

    @Override
    protected String doInBackground(String... strings) {
        if (myApiService == null) {  // Only do this once
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local devappserver
                    .setRootUrl("http://10.0.2.2:8080/_ah/api/");

            myApiService = builder.build();
        }

        try {
            return myApiService.sayHi().execute().getData();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(final String s) {
        super.onPostExecute(s);
        onAsynResponse.onResponse(s);
    }
}