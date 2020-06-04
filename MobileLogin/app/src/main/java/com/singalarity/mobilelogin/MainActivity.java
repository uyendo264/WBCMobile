package com.singalarity.mobilelogin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public String sendData(String url,  String username, String password){
        Log.d("TEST", "url: " + url);
        Log.d("TEST","username: "+ username);
        final int[] check = new int[1];
        final String[] result = new String[1];
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
            jsonObject.put("password", password);
            jsonObject.put("hashPassword", "qr534t0");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("TEST", "Exception raised while attempting to create JSON payload for upload.");
        }
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                final String error = e.getMessage();
                check[0]= -1;
                result[0] =e.getMessage();
                Log.d("Test","Error"+ e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    // TextView responseText = findViewById(R.id.response_textView);
                    //responseText.setText(myResponse);
                    check[0] = 1;
                    result[0]= myResponse;
                    Log.d("Test", "Response:"+myResponse);
                }
            }
        });
        return result[0];
    }
}
