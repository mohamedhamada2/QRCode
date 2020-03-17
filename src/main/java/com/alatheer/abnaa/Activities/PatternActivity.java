package com.alatheer.abnaa.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.alatheer.abnaa.Models.Charities_Model;
import com.alatheer.abnaa.Models.UserModel;
import com.alatheer.abnaa.Preference.MySharedPreference;
import com.alatheer.abnaa.Preference.MySharedPreferences2;
import com.alatheer.abnaa.R;
import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;

import java.util.List;

public class PatternActivity extends AppCompatActivity implements PatternLockViewListener {
    PatternLockView patternLockView;
    MySharedPreference mprefs;
    MySharedPreferences2 mprefs2;
    UserModel userModel;
    Charities_Model charities_model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern);
        mprefs = MySharedPreference.getInstance();
        mprefs2 = MySharedPreferences2.getInstance();
        userModel = mprefs.Get_UserData(PatternActivity.this);
        charities_model = mprefs2.Get_UserData(PatternActivity.this);
        patternLockView = findViewById(R.id.pattern_lock_view);
        patternLockView.addPatternLockListener(this);
    }


    @Override
    public void onStarted() {

    }

    @Override
    public void onProgress(List<PatternLockView.Dot> progressPattern) {

    }

    @Override
    public void onComplete(List<PatternLockView.Dot> pattern) {
      if(PatternLockUtils.patternToString(patternLockView,pattern).equalsIgnoreCase("0125")){
          patternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
          if(userModel != null && charities_model != null){
              startActivity(new Intent(PatternActivity.this,  MainActivity.class));
              finish();
          }else if (userModel == null && charities_model!= null ){
              startActivity(new Intent(PatternActivity.this,LoginActivity.class));
              finish();
          }else {
              startActivity(new Intent(PatternActivity.this, All_Charities.class));
             finish();
          }
      }else {
          patternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
          Toast.makeText(this, "pattern error", Toast.LENGTH_SHORT).show();
      }
    }

    @Override
    public void onCleared() {

    }
}
