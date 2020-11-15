package com.avorobyev174.mec_winet.classes.api;

import com.avorobyev174.mec_winet.classes.floor.api.FloorApi;
import com.avorobyev174.mec_winet.classes.house.api.HousesApi;
import com.avorobyev174.mec_winet.classes.meter.MeterApi;
import com.avorobyev174.mec_winet.classes.section.api.SectionApi;
import com.avorobyev174.mec_winet.classes.vestibule.api.VestibuleApi;
import com.avorobyev174.mec_winet.classes.winet.api.WinetApi;
import com.avorobyev174.mec_winet.classes.apartment.api.ApartmentApi;

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

    public static ApartmentApi getApartmentApi() {
        ApartmentApi apartmentApi = getRetrofit().create(ApartmentApi.class);
        return apartmentApi;
    }

    public static MeterApi getMeterApi() {
        MeterApi meterApi = getRetrofit().create(MeterApi.class);
        return meterApi;
    }
}
