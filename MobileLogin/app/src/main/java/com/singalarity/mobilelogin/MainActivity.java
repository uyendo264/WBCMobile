package com.singalarity.mobilelogin;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Cryption wbcCryption;
    private SharedViewModel viewModel;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wbcCryption = new Cryption(getApplicationContext());
        wbcCryption.WBCInit("1234567890");
        String myFileName = "";
        TelephonyManager telephonyManager;
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Log.d("Check device ID", "get deviceid");
        String deviceID = telephonyManager.getDeviceId();
        Log.d("Check device ID", "Device ID = "+ deviceID);
        File file = new File(myFileName);
        if (file.exists()){//file exits
            Log.d("Check WBC file", "exits WBC file");
            file.delete();
            Log.d("Check WBC file", "deleted file");
        }
        else{
            try {
                Log.d("Check WBC file", "create new WBC file");

                FileOutputStream fileout=openFileOutput(myFileName, MODE_PRIVATE);
                OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
                outputWriter.write("abcdeffff");
                outputWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
                    check[0] = 1;
                    result[0]= myResponse;
                    Log.d("Test", "Response:"+myResponse);
                }
            }
        });
        return result[0];
    }
}