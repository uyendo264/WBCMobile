package com.singalarity.mobileapp;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import cz.muni.fi.xklinec.whiteboxAES.WBCStringCryption;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<String> userName = new MutableLiveData<>();
    private MutableLiveData<Boolean> loginStatus = new MutableLiveData<>();
    private MutableLiveData<WBCStringCryption> WBC = new MutableLiveData<>();

    public LiveData<String> getUserName() {
        return userName;
    }

    public void setUserName(String name){
        userName.setValue(name);
    }

    public LiveData<Boolean> getLoginStatus (){
        return loginStatus;
    }

    public void setLoginStatus(Boolean status){
        loginStatus.setValue(status);
    }

    public LiveData<WBCStringCryption> getWBC (){
        return WBC;
    }
    public void setWBC(WBCStringCryption wbc){
        WBC.setValue(wbc);
    }
}
