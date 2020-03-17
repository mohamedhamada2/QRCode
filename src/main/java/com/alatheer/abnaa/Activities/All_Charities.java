package com.alatheer.abnaa.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

//import com.alatheer.abnaa.Adapters.Charities_Adapter;
import com.alatheer.abnaa.Models.Charities_Model;
import com.alatheer.abnaa.Models.UserModel;
import com.alatheer.abnaa.Preference.MySharedPreference;
import com.alatheer.abnaa.Preference.MySharedPreferences2;
import com.alatheer.abnaa.R;
import com.alatheer.abnaa.Service.Api1;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class All_Charities extends AppCompatActivity {
    RecyclerView recyclerView ;
    TextInputEditText txt_code;
//    Charities_Adapter charities_adapter;
    RecyclerView.LayoutManager layoutManager;
    List<Charities_Model>charities_modelList;
    MySharedPreferences2 mySharedPreference;
    Charities_Model charities_model;
    String code ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all__charities);
        initview();
    }

    private void initview() {
        //recyclerView = findViewById(R.id.recycler_all);
        //charities_modelList = new ArrayList<>();
        txt_code = findViewById(R.id.code);
        //initrecycler();
        mySharedPreference = MySharedPreferences2.getInstance();


        String session = mySharedPreference.getSession(this);
        if (!TextUtils.isEmpty(session) || session != null) {
            if (session.equals("login1")) {
                charities_model = mySharedPreference.Get_UserData(this);

                if (charities_model != null) {
                    Intent intent = new Intent(All_Charities.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

        }
    }

    /*private void initrecycler() {
        Charities_Model charities_model1 = new Charities_Model();
        charities_model1.setImage(R.drawable.icon22);
        charities_model1.setName("Abna");
        Charities_Model charities_model2 = new Charities_Model();
        charities_model2.setName("Raoom");
        charities_model2.setImage(R.drawable.icon33);
        Charities_Model charities_model3 = new Charities_Model();
        charities_model3.setName("Albaha");
        charities_model3.setImage(R.drawable.icon44);
        charities_modelList.add(charities_model1);
        charities_modelList.add(charities_model2);
        charities_modelList.add(charities_model3);
        recyclerView.setHasFixedSize(true);
        charities_adapter = new Charities_Adapter(charities_modelList,this);
        recyclerView.setAdapter(charities_adapter);
        layoutManager =new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);

    }*/

    public void show(View view) {
        Validation();

    }

    private void Validation() {
        code = txt_code.getText().toString();
        if(!TextUtils.isEmpty(code) && code.length()==4){
            txt_code.setError(null);
            Code_Login(code);
        }else {
            if(TextUtils.isEmpty(code)){
                txt_code.setError("مطلوب تسجيل رقم الهوية");
            }else{
                txt_code.setError("رقم الهوية غير صحيح");
            }
        }
    }
    private void Code_Login(String code) {
        Api1.getService().Code_Login(code).enqueue(new Callback<Charities_Model>() {
            @Override
            public void onResponse(Call<Charities_Model> call, Response<Charities_Model> response) {
                if(response.isSuccessful()){
                    if(response.body().getSuccess() == 1){
                        charities_model = response.body();
                        mySharedPreference = MySharedPreferences2.getInstance();
                        mySharedPreference.Create_Update_UserData(All_Charities.this,charities_model);
                        Toast.makeText(All_Charities.this,"تم تسجيل دخولك بنجاح في "+ response.body().getName(),Toast.LENGTH_LONG).show();
                        Log.d("model",mySharedPreference.Get_UserData(All_Charities.this).getBaseUrl());
                        startActivity(new Intent(All_Charities.this,LoginActivity.class));
                        finish();

                    }else if(response.body().getSuccess()==2){
                        Toast.makeText(All_Charities.this,"كود الجمعية غير صحيح",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Charities_Model> call, Throwable t) {

            }
        });
    }
}
