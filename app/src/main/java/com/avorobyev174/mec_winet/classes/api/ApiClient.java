package com.avorobyev174.mec_winet.classes.api;

import com.avorobyev174.mec_winet.classes.floor.FloorApi;
import com.avorobyev174.mec_winet.classes.house.HousesApi;
import com.avorobyev174.mec_winet.classes.section.SectionApi;
import com.avorobyev174.mec_winet.classes.vestibule.VestibuleApi;
import com.avorobyev174.mec_winet.classes.winet.WinetApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit getRetrofit(){

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.m-e-c.ru/services/mobile/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        return retrofit;
    }

    public static HousesApi getHouseApi() {
        HousesApi housesApi = getRetrofit().create(HousesApi.class);
        return housesApi;
    }

    public static SectionApi getSectionApi() {
        SectionApi sectionApi = getRetrofit().create(SectionApi.class);
        return sectionApi;
    }

    public static FloorApi getFloorApi() {
        FloorApi floorApi = getRetrofit().create(FloorApi.class);
        return floorApi;
    }

    public static VestibuleApi getVestibuleApi() {
        VestibuleApi vestibuleApi = getRetrofit().create(VestibuleApi.class);
        return vestibuleApi;
    }

    public static WinetApi getWinetApi() {
        WinetApi winetApi = getRetrofit().create(WinetApi.class);
        return winetApi;
    }
}
