package com.alialmohaya.anghamifydemo.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPClient {

    public static String getResponseString(String requestUrl, Shared.Method method, JSONObject body) throws IOException {

        HttpURLConnection httpConnection = (HttpURLConnection) (new URL(requestUrl)).openConnection();
        switch (method) {
            case GET:
                httpConnection.setRequestMethod("GET");
                break;
            case POST:
                httpConnection.setRequestMethod("POST");
                break;
        }

        httpConnection.setDoOutput(true);
        httpConnection.setDoInput(true);
        httpConnection.setRequestProperty("Content-Type", "application/json");
        OutputStream outputStream = httpConnection.getOutputStream();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
        outputStreamWriter.write(body.toString());
        outputStreamWriter.flush();
        outputStreamWriter.close();
        outputStream.close();
        httpConnection.connect();

        if (httpConnection.getResponseCode() >= 400) {
            StringBuilder stringBuilder = new StringBuilder();
            InputStream inputStream = httpConnection.getErrorStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            Log.i("anghamix", stringBuilder.toString());
        }

        StringBuilder stringBuilder = new StringBuilder();
        InputStream inputStream = httpConnection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }

        inputStream.close();
        httpConnection.disconnect();

        return stringBuilder.toString();
    }

    public static String getResponseString(String requestUrl, Shared.Method method) throws IOException {

        Log.i("anghamix", "OK TO NOW");
        HttpURLConnection httpConnection = (HttpURLConnection) (new URL(requestUrl)).openConnection();
        switch (method) {
            case GET:
                httpConnection.setRequestMethod("GET");
                break;
            case POST:
                httpConnection.setRequestMethod("POST");
                break;
        }
        httpConnection.setDoInput(true);
        httpConnection.connect();

        if (httpConnection.getResponseCode() >= 400) {
            StringBuilder stringBuilder = new StringBuilder();
            InputStream inputStream = httpConnection.getErrorStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            Log.i("anghamix", stringBuilder.toString() + httpConnection.getResponseCode());
        }

        StringBuilder stringBuilder = new StringBuilder();
        InputStream inputStream = httpConnection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        Log.i("anghamix", stringBuilder.toString() + httpConnection.getResponseCode());

        inputStream.close();
        httpConnection.disconnect();

        return stringBuilder.toString();
    }

    public static @Nullable
    Bitmap getImageBitmapFromUrl(String imageUrl) throws IOException {
        Bitmap bitmap = null;

        URL url = new URL(imageUrl);
        InputStream inputStream = url.openStream();
        bitmap = BitmapFactory.decodeStream(inputStream);
        return bitmap;
    }
}
