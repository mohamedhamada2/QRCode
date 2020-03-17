package com.alatheer.abnaa;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.CancellationSignal;

import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.alatheer.abnaa.Activities.All_Charities;
import com.alatheer.abnaa.Activities.LoginActivity;
import com.alatheer.abnaa.Activities.MainActivity;
import com.alatheer.abnaa.Activities.Splash;
import com.alatheer.abnaa.Models.Charities_Model;
import com.alatheer.abnaa.Models.UserModel;
import com.alatheer.abnaa.Preference.MySharedPreference;
import com.alatheer.abnaa.Preference.MySharedPreferences2;

import java.text.SimpleDateFormat;

import androidx.core.content.ContextCompat;

@TargetApi(Build.VERSION_CODES.M)
public class FingerprintHandle extends FingerprintManager.AuthenticationCallback {

    private Context context;
    MySharedPreference mprefs;
    MySharedPreferences2 mprefs2;
    UserModel userModel;
    Charities_Model charities_model;
    int secondsDelayed;
    public FingerprintHandle(Context context){

        this.context = context;

    }

    public void startAuth(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject){

        CancellationSignal cancellationSignal = new CancellationSignal();
        fingerprintManager.authenticate(cryptoObject, cancellationSignal, 0, this, null);

    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {

        this.update("There was an Auth Error. " + errString, false);

    }

    @Override
    public void onAuthenticationFailed() {

        this.update("Auth Failed. ", false);

    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {

        this.update("Error: " + helpString, false);

    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        mprefs = MySharedPreference.getInstance();
        mprefs2 = MySharedPreferences2.getInstance();
        userModel = mprefs.Get_UserData(context);
        charities_model = mprefs2.Get_UserData(context);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if(userModel != null && charities_model != null){
                    context.startActivity(new Intent(context,  MainActivity.class));
                    ((Activity)context).finish();
                }else if (userModel == null && charities_model!= null ){
                    context.startActivity(new Intent(context,LoginActivity.class));
                    ((Activity)context).finish();
                }else {
                    context.startActivity(new Intent(context, All_Charities.class));
                    ((Activity)context).finish();
                }

            }
        }, secondsDelayed * 3000);
        this.update("You can now access the app.", true);

    }

    private void update(String s, boolean b) {

        ImageView imageView = (ImageView) ((Activity)context).findViewById(R.id.image_splash);

        //paraLabel.setText(s);

        if(b == false){

            //paraLabel.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));

        } else {

           // paraLabel.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
           // context.startActivity(new Intent(context,Main2Activity.class));

        }

    }
}
