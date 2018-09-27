package com.navoki.javalib;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class JokesAPI {

    private String response;

    public JokesAPI() {
    }

    public String getResponse() {
        try {
            URL url = new URL("http://www.mocky.io/v2/5b2fac2a30000001080660ac");
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            if (huc.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Scanner scanner = new Scanner(huc.getInputStream());
                scanner.useDelimiter("\\A");
                response = scanner.next();
                return response;
            }
            huc.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getRandomJoke() {
        Gson converter = new Gson();
        Type type = new TypeToken<List<String>>() {
        }.getType();
        List<String> list = converter.fromJson(response, type);

        int random = new Random().nextInt(list.size() - 1);
        return String.format(list.get(random));

    }
}
