package com.avorobyev174.mec_winet.classes.floor;

import com.avorobyev174.mec_winet.classes.api.SimpleResponse;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface FloorApi {
    @GET("floor/?485kf9056kjwj3=04jf75h@hjfks")
    Call<FloorInfoResponse> getFloors(@Query("section_id") int sectionId);

    @POST("floor/?485kf9056kjwj3=04jf75h@hjfks")
    Call<FloorResponseWithParams> createFloor(@Query("floor_number") String floorNumber, @Query("section_id") int sectionId);

    @DELETE("mobile_redirect.php?485kf9056kjwj3=04jf75h@hjfks")
    Call<SimpleResponse> deleteFloor(@Query("_type") String entity, @Query("_id") int floorId);

    @GET("floor/?485kf9056kjwj3=04jf75h@hjfks")
    Call<FloorInfoResponse> getFloor(@Query("floor_id") int floorId);
}
