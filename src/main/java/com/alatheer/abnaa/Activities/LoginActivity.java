package com.alatheer.abnaa.Activities;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.alatheer.abnaa.Models.Charities_Model;
import com.alatheer.abnaa.Models.UserModel;
import com.alatheer.abnaa.Preference.MySharedPreference;
import com.alatheer.abnaa.Preference.MySharedPreferences2;
import com.alatheer.abnaa.R;
import com.alatheer.abnaa.Service.Api;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText txt_emp_national_num;
    String emp_national_num;
    MySharedPreference mySharedPreference;
    MySharedPreferences2 mySharedPreferences2;
    UserModel userModel1;
    Charities_Model charities_model;
    ImageView logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init_view();
    }

    private void init_view() {
        txt_emp_national_num = findViewById(R.id.email);
        logo = findViewById(R.id.logo_image);
        mySharedPreference =  MySharedPreference.getInstance();
        mySharedPreferences2 = MySharedPreferences2.getInstance();
        charities_model = mySharedPreferences2.Get_UserData(this);
        String logo_image1 = charities_model.getLogo();
        Picasso.with(this).load(logo_image1).into(logo);
        String session = mySharedPreference.getSession(this);
        String session2 = mySharedPreferences2.getSession(this);
        if ((!TextUtils.isEmpty(session) || session != null)&&(!TextUtils.isEmpty(session2) || session2 != null)) {
            if (session.equals("login")&& session2.equals("login1")) {
                userModel1 = mySharedPreference.Get_UserData(this);

                if (userModel1 != null&&charities_model!=null) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

        }
    }

    public void show(View view) {
        Validation();
        //startActivity(new Intent(LoginActivity.this,MainActivity.class));
    }

    private void Validation() {
         emp_national_num = txt_emp_national_num.getText().toString();
        if(!TextUtils.isEmpty(emp_national_num) && emp_national_num.length()==10){
            txt_emp_national_num.setError(null);
            Login_QRCode(emp_national_num);
        }else {
            if(TextUtils.isEmpty(emp_national_num)){
                txt_emp_national_num.setError("مطلوب تسجيل رقم الهوية");
            }else{
                txt_emp_national_num.setError("رقم الهوية غير صحيح");
            }
        }
    }

    private void Login_QRCode(String emp_national_num) {
        Api.getService(LoginActivity.this).Login(emp_national_num).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if(response.isSuccessful()){
                    if(response.body().getSuccess() == 1){
                        mySharedPreference=MySharedPreference.getInstance();
                        userModel1 = response.body();
                        mySharedPreference.Create_Update_UserData(LoginActivity.this,userModel1);
                        Toast.makeText(LoginActivity.this,"تم تسجيل دخولك بنجاح",Toast.LENGTH_LONG).show();
                        Log.d("model",mySharedPreference.Get_UserData(LoginActivity.this).getEmp_nationl_num());
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        finish();
                    }else if(response.body().getSuccess() == 2){
                        Toast.makeText(LoginActivity.this,"لقد تم تسجيلك من موبايل اخر",Toast.LENGTH_LONG).show();
                    }else if(response.body().getSuccess()== 0){
                        Toast.makeText(LoginActivity.this,"رقم الهوية غير صحيح",Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {

            }
        });
    }
}
