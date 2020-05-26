package com.singalarity.mobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
    private Cryption wbcCryption;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wbcCryption = new Cryption();
        wbcCryption.WBCInit();

        Button sendDataButton = findViewById(R.id.EncryptAndSend_button);
        sendDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView responseText = findViewById(R.id.Respone_textView);
                responseText.setText("");
                EncryptandSend();
            }
        });
    }
    public void EncryptandSend() {
        EditText encryptDataText= findViewById(R.id.password_editText);
        System.out.println("----------------------------");
        String passwordPlaintext=encryptDataText.getText().toString();

        String encryptedData=wbcCryption.EncryptWBC(passwordPlaintext);
        EditText usernameText=findViewById(R.id.username_editText);
        String username=usernameText.getText().toString();
        sendData(username,passwordPlaintext,encryptedData);
    }
    public void sendData(String username, String passwordPlaintext,String encryptedPassword) {
        EditText urlText= (EditText) findViewById(R.id.SeverAddress_editText);
        String url=  urlText.getText().toString();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
            jsonObject.put("password",passwordPlaintext);
            jsonObject.put("encryptedPassword", encryptedPassword);
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
                Log.d("Test","Error"+ e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    TextView responseText = findViewById(R.id.Respone_textView);
                    responseText.setText(myResponse);
                    Log.d("Test", "Response:"+myResponse);
                }
            }
        });

    }
}
