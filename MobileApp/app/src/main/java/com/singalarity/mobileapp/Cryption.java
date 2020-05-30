package com.singalarity.mobileapp;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import cz.muni.fi.xklinec.whiteboxAES.AES;
import cz.muni.fi.xklinec.whiteboxAES.WBCStringCryption;
import cz.muni.fi.xklinec.whiteboxAES.generator.ExternalBijections;
import cz.muni.fi.xklinec.whiteboxAES.generator.Generator;

public class Cryption {

    private WBCStringCryption wbcStringEncryption;
    private WBCStringCryption wbcStringDecryption;
    private Context context;
    public Cryption(Context context){
        this.context = context;
    }
    public void WBCInit(){

        Generator gEnc = new Generator();
        Generator gDec = new Generator();

        ExternalBijections extc = new ExternalBijections();
        gEnc.generateExtEncoding(extc, Generator.WBAESGEN_EXTGEN_ID);
        gEnc.setUseIO04x04Identity(true);
        gEnc.setUseIO08x08Identity(true);
        gEnc.setUseMB08x08Identity(true);
        gEnc.setUseMB32x32Identity(true);
        gDec.setUseIO04x04Identity(true);
        gDec.setUseIO08x08Identity(true);
        gDec.setUseMB08x08Identity(true);
        gDec.setUseMB32x32Identity(true);

        byte[] AESKey =  new byte[]{(byte)0x4a, (byte)0x2d, (byte)0x1d, (byte)0x65, (byte)0xb5,
                (byte)0xb1, (byte)0xe2, (byte)0x2d, (byte)0xfc, (byte)0xea,
                (byte)0xa0, (byte)0x65, (byte)0xd7, (byte)0x63, (byte)0x21, (byte)0x67};
        gEnc.generate(true, AESKey, 16, extc);
        gDec.generate(false, AESKey, 16, extc);
        AES AESenc = gEnc.getAESi();
        AES AESdec = gDec.getAESi();
        this.wbcStringEncryption = new WBCStringCryption(AESenc, true);
        this.wbcStringDecryption= new WBCStringCryption(AESdec, false);
        ///////
        try{
            FileOutputStream file = context.openFileOutput("WBCEncryptString", Context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(this.wbcStringEncryption);
            out.close();
            file.close();
            System.out.println("Object has been serialized");
        } catch(IOException e){
            System.out.println("Message: " + e.getMessage());
            System.out.println("2. IOException is caught");
        }
        WBCStringCryption deseiralizeWBC;
        try{
            FileInputStream fi = context.openFileInput("WBCEncryptString");
            ObjectInputStream in = new ObjectInputStream(fi);
            deseiralizeWBC = (WBCStringCryption) in.readObject();
            in.close();
            fi.close();
            System.out.println("Real cryption: "+ wbcStringEncryption.getStringCryptionResult("testing crypt") );
            System.out.println("deserialize cryption: "+ deseiralizeWBC.getStringCryptionResult("testing crypt") );
        } catch(IOException | ClassNotFoundException e){
            System.out.println(e.getMessage());
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
}
