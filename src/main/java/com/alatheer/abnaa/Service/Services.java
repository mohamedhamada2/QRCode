package com.alatheer.abnaa.Service;

import com.alatheer.abnaa.Models.QRCodeModel;
import com.alatheer.abnaa.Models.UserModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Services {
    @FormUrlEncoded
    @POST("qr_c/Api/login_Qrcode")
    Call<UserModel>Login(@Field("emp_nationl_num")String emp_national_num);
    @FormUrlEncoded
    @POST("qr_c/Api/register_Qrcode")
    Call<QRCodeModel>Register(@Field("emp_nationl_num")String emp_natonal_num,@Field("Qrcode")String qrcode
    ,@Field("lat")double lat,@Field("long")double lon);

}