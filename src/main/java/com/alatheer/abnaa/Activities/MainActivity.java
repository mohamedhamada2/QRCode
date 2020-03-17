package com.alatheer.abnaa.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alatheer.abnaa.Models.Charities_Model;
import com.alatheer.abnaa.Models.QRCodeModel;
import com.alatheer.abnaa.Models.UserModel;
import com.alatheer.abnaa.Preference.MySharedPreference;
import com.alatheer.abnaa.Preference.MySharedPreferences2;
import com.alatheer.abnaa.R;
import com.alatheer.abnaa.Service.Api;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler, LocationListener {
    ZXingScannerView zxscan;
    TextView txt_result;
    Button do_it;
    MySharedPreference mySharedPreference;
    UserModel userModel;
    MySharedPreferences2 mySharedPreferences2;
    Charities_Model charities_model;
    String emp_national_num;
    MediaPlayer mediaPlayer;
    LocationManager locationManager;
    LinearLayout layout;
    double tvLongitude,tvLatitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initview();

    }

    private void initview() {
        zxscan = findViewById(R.id.zxscan);
        mySharedPreference = MySharedPreference.getInstance();
        mySharedPreferences2 = MySharedPreferences2.getInstance();
        userModel = mySharedPreference.Get_UserData(MainActivity.this);
        charities_model = mySharedPreferences2.Get_UserData(MainActivity.this);
        emp_national_num = userModel.getEmp_nationl_num();
        layout = findViewById(R.id.layout_result);
        //Log.v("national",emp_national_num);
        layout.setBackgroundColor(Color.parseColor(charities_model.getColor()));
        txt_result = findViewById(R.id.txt_result);
        CheckPermission();
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                zxscan.setResultHandler(MainActivity.this);
                zxscan.startCamera();


            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

            }
        }).check();


    }
    @Override
    public void onResume() {
        super.onResume();
        getLocation();
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    public void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void CheckPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        tvLongitude = location.getLongitude();
        tvLatitude =location.getLatitude();


    }


    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(MainActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider!" + provider,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {



        //zxscan.stopCamera();
        super.onDestroy();
    }


    @Override
    public void handleResult(com.google.zxing.Result rawResult) {
        String result=rawResult.toString();
        Log.v("KKKK",result);
        Log.v("mmmm",tvLatitude+"");
        Log.v("nnnn",tvLongitude+"");
        Api.getService(MainActivity.this).Register(emp_national_num,result,tvLatitude,tvLongitude).enqueue(new Callback<QRCodeModel>() {
            @Override
            public void onResponse(Call<QRCodeModel> call, Response<QRCodeModel> response) {
                if (response.isSuccessful()){
                    if(response.body().getSuccess()==1){
                        txt_result.setText("تم تسجيل حضورك اليومي بنجاح");
                        zxscan.stopCamera();
                        mediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.voice7);
                        mediaPlayer.start();

                    }else if(response.body().getSuccess()==0){
                        txt_result.setText("لا يمكن تسجيل دخولك");
                        zxscan.stopCamera();
                        mediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.voice8);
                        mediaPlayer.start();

                    }
                }
            }

            @Override
            public void onFailure(Call<QRCodeModel> call, Throwable t) {

            }
        });
    }


    public void show(View view) {
        finish();
        System.exit(0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        System.exit(0);
    }
}