package com.singalarity.mobileapp;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;
import java.util.concurrent.CountDownLatch;

import cz.muni.fi.xklinec.whiteboxAES.AES;
import cz.muni.fi.xklinec.whiteboxAES.WBCStringCryption;
import cz.muni.fi.xklinec.whiteboxAES.generator.ExternalBijections;
import cz.muni.fi.xklinec.whiteboxAES.generator.Generator;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static java.util.Base64.getDecoder;

public class Cryption {

    private WBCStringCryption wbcStringEncryption;
    private WBCStringCryption wbcStringDecryption;
    private Context context;
    public Cryption(Context context){
        this.context = context;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void WBCInit(){
        String encoded = sendRequest("1234567890");
        Log.d("CheckEncoded","encoded: "+encoded);
        byte[] decodebytes = Base64.getDecoder().decode(encoded);
                //android.util.Base64.decode(encoded, android.util.Base64.DEFAULT);
                //getDecoder().decode(encoded);
        // Write To File
        File someFile = new File("MobileFile");
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(String.valueOf(someFile),context.MODE_PRIVATE);
            fos.write(decodebytes);
            fos.flush();
            fos.close();

            FileInputStream mobileFile = context.openFileInput("MobileFile");
            ObjectInputStream mobileIn = new ObjectInputStream(mobileFile);

            WBCStringCryption deserializedWBCEnc = (WBCStringCryption) mobileIn.readObject();
            mobileFile.close();
            mobileIn.close();
            Log.d("TEST1","SerializedWBC: "+ deserializedWBCEnc.getStringCryptionResult("123123213213213"));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public String EncryptWBC(String plaintext){
        WBCStringCryption WBCStringEncryption = this.wbcStringEncryption;
        String encrypted;

        System.out.println("plaintext String: "+ plaintext);
        encrypted= WBCStringEncryption.getStringCryptionResult(plaintext);
        System.out.println("encrypted String: "+ encrypted);
        return (encrypted);
    }
    public void DecryptWBC(String ciphertext){
        String decryptedString;
        WBCStringCryption WBCStringDecryption=this.wbcStringDecryption;

        decryptedString= WBCStringDecryption.getStringCryptionResult(ciphertext);
        Log.d("Test","decrypted: "+decryptedString);
    }
    public String sendRequest(String deviceID){
        final String[] result = new String[1];
        String url=  "http://192.168.43.229:8081/wbcRequest";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("deviceID", deviceID);
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
        final CountDownLatch countDownLatch = new CountDownLatch(1);
         client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                String error = e.getMessage();
                result[0] = error;
                Log.d("Test","Error"+ e.getMessage());
                countDownLatch.countDown();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String myResponse = response.body().string();
                    try {
                        JSONObject jsonObject =new JSONObject(myResponse);
                        String wbcFileString = jsonObject.getString("wbcFileString");
                        result[0]= wbcFileString;
                        Log.d("TEST", "result: "+result[0]);
                        countDownLatch.countDown();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        result[0]=e.getMessage();
                        countDownLatch.countDown();
                    }

                    //Log.d("Test", "Response:"+myResponse);
                }
            }
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("Return result", "result = "+result[0]);
        return result[0];
    }
}
