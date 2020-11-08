package com.avorobyev174.mec_winet.classes.meter;

import com.avorobyev174.mec_winet.classes.apartment.ApartmentInfoResponse;
import com.avorobyev174.mec_winet.classes.apartment.ApartmentResponseWithParams;
import com.avorobyev174.mec_winet.classes.api.SimpleResponse;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MeterApi {
    @GET("meter/?485kf9056kjwj3=04jf75h@hjfks")
    Call<MeterInfoResponse> getMeters(@Query("apartment_id") int apartmentId);

    @POST("meter/?485kf9056kjwj3=04jf75h@hjfks")
    Call<MeterResponseWithParams> createMeter(@Query("meter_type") String meterType,
                                                      @Query("serial_number") String serNumber,
                                                      @Query("password") String password,
                                                      @Query("apartment_id") int apartmentId);

    @DELETE("mobile_redirect.php?485kf9056kjwj3=04jf75h@hjfks")
    Call<SimpleResponse> deleteMeter(@Query("_type") String entity, @Query("_id") int meterId);

    @GET("mobile_redirect.php?485kf9056kjwj3=04jf75h@hjfks")
    Call<MeterInfoResponse> getMeter(@Query("_type") String entity, @Query("_id") int meterId);
}
