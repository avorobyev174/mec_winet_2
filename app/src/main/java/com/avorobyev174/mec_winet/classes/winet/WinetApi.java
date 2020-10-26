package com.avorobyev174.mec_winet.classes.winet;

import com.avorobyev174.mec_winet.classes.api.SimpleResponse;
import com.avorobyev174.mec_winet.classes.vestibule.VestibuleInfoResponse;
import com.avorobyev174.mec_winet.classes.vestibule.VestibuleResponseWithParams;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface WinetApi {
    @GET("winet/?485kf9056kjwj3=04jf75h@hjfks")
    Call<WinetInfoResponse> getWinets(@Query("vestibule_id") int vestibuleId);

    @POST("winet/?485kf9056kjwj3=04jf75h@hjfks")
    Call<WinetResponseWithParams> createWinet(@Query("winet_type") String type, @Query("ser_number") String serNumber, @Query("vestibule_id") int vestibuleId);

    @DELETE("mobile_redirect.php?485kf9056kjwj3=04jf75h@hjfks")
    Call<SimpleResponse> deleteWinet(@Query("_type") String entity, @Query("_id") int winetId);

    @GET("floor/?485kf9056kjwj3=04jf75h@hjfks")
    Call<WinetInfoResponse> getWinet(@Query("winet_id") int winetId);
}
