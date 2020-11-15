package com.avorobyev174.mec_winet.classes.apartment.api;

import com.avorobyev174.mec_winet.classes.api.SimpleResponse;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApartmentApi {
    @GET("apartment/?485kf9056kjwj3=04jf75h@hjfks")
    Call<ApartmentInfoResponse> getApartments(@Query("winet_id") int winetId);

    @POST("apartment/?485kf9056kjwj3=04jf75h@hjfks")
    Call<ApartmentResponseWithParams> createApartment(@Query("apartment_type") String apartmentType,
                                                      @Query("apartment_description") String apartmentDesc,
                                                      @Query("apartment_comment") String apartmentComment,
                                                      @Query("winet_id") int winetId);

    @DELETE("mobile_redirect.php?485kf9056kjwj3=04jf75h@hjfks")
    Call<SimpleResponse> deleteApartment(@Query("_type") String entity, @Query("_id") int apartmentId);

    @GET("mobile_redirect.php?485kf9056kjwj3=04jf75h@hjfks")
    Call<ApartmentInfoResponse> getApartment(@Query("_type") String entity, @Query("_id") int winetId);
}
