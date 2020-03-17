package com.alatheer.abnaa.Service;
import android.content.Context;

import com.alatheer.abnaa.Models.Charities_Model;
import com.alatheer.abnaa.Preference.MySharedPreference;
import com.alatheer.abnaa.Preference.MySharedPreferences2;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {

    private static Retrofit retrofit = null;


    public static Retrofit getRetrofit(Context context)
    {
        if (retrofit==null)
        {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .writeTimeout(1,TimeUnit.MINUTES)
                    .readTimeout(1,TimeUnit.MINUTES)
                    .retryOnConnectionFailure(true)
                    .build();

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            MySharedPreferences2 mySharedPreferences2 = MySharedPreferences2.getInstance();
            Charities_Model charities_model = mySharedPreferences2.Get_UserData(context);
            String base_url = charities_model.getBaseUrl();
            retrofit = new Retrofit.Builder()
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl(base_url)
                    .build();

        }
        return retrofit;
    }

    public static Services getService(Context context)
    {
        retrofit = getRetrofit(context);
        return retrofit.create(Services.class);

    }
}

