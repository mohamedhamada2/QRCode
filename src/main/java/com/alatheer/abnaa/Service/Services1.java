package com.alatheer.abnaa.Service;

import com.alatheer.abnaa.Models.Charities_Model;
import com.alatheer.abnaa.Models.QRCodeModel;
import com.alatheer.abnaa.Models.UserModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Services1 {
    @FormUrlEncoded
    @POST("Api/get_site_by_code")
    Call<Charities_Model> Code_Login(@Field("code")String code);

}
