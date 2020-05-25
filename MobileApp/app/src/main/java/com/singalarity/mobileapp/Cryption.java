package com.singalarity.mobileapp;

import android.util.Log;

import cz.muni.fi.xklinec.whiteboxAES.AES;
import cz.muni.fi.xklinec.whiteboxAES.WBCStringCryption;
import cz.muni.fi.xklinec.whiteboxAES.generator.ExternalBijections;
import cz.muni.fi.xklinec.whiteboxAES.generator.Generator;

public class Cryption {

    private WBCStringCryption wbcStringEncryption;
    private WBCStringCryption wbcStringDecryption;

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
