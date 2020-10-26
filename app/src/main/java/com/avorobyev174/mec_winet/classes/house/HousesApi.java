package com.avorobyev174.mec_winet.classes.house;

import com.avorobyev174.mec_winet.classes.api.SimpleResponse;

import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.DELETE;
import retrofit2.Call;
import retrofit2.http.Query;

public interface HousesApi {
    @GET("house/?485kf9056kjwj3=04jf75h@hjfks")
    Call<HousesInfoResponse> getHouses();

    @POST("house/?485kf9056kjwj3=04jf75h@hjfks")
    Call<HouseResponseWithParams> createHouse(@Query("street") String street, @Query("house_number") String houseNumber);

    @DELETE("mobile_redirect.php?485kf9056kjwj3=04jf75h@hjfks")
    Call<SimpleResponse> deleteHouse(@Query("_type") String entity, @Query("_id") int houseId);

    @GET("house/?485kf9056kjwj3=04jf75h@hjfks")
    Call<HousesInfoResponse> getHouse(@Query("house_id") int houseId);
}
