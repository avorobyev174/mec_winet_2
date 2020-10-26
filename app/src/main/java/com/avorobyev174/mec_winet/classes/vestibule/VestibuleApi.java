package com.avorobyev174.mec_winet.classes.vestibule;

import com.avorobyev174.mec_winet.classes.api.SimpleResponse;
import com.avorobyev174.mec_winet.classes.floor.FloorInfoResponse;
import com.avorobyev174.mec_winet.classes.floor.FloorResponseWithParams;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface VestibuleApi {
    @GET("vestibule/?485kf9056kjwj3=04jf75h@hjfks")
    Call<VestibuleInfoResponse> getVestibules(@Query("section_id") int sectionId);

    @POST("vestibule/?485kf9056kjwj3=04jf75h@hjfks")
    Call<VestibuleResponseWithParams> createVestibule(@Query("vestibule_number") String vestibuleNumber, @Query("floor_id") int floorId);

    @DELETE("mobile_redirect.php?485kf9056kjwj3=04jf75h@hjfks")
    Call<SimpleResponse> deleteVestibule(@Query("_type") String entity, @Query("_id") int vestibuleId);

    @GET("floor/?485kf9056kjwj3=04jf75h@hjfks")
    Call<VestibuleInfoResponse> getVestibule(@Query("vestibule_id") int vestibuleId);
}
