package com.avorobyev174.mec_winet.classes.winet.api;

import com.avorobyev174.mec_winet.classes.api.SimpleResponse;
import com.avorobyev174.mec_winet.classes.winetData.api.WinetDataInfoResponse;
import com.avorobyev174.mec_winet.classes.winetData.api.WinetDataResponseWithParams;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface WinetApi {
    @GET("winet/?485kf9056kjwj3=04jf75h@hjfks")
    Call<WinetInfoResponse> getWinets(@Query("vestibule_id") int vestibuleId);

    @POST("winet/?485kf9056kjwj3=04jf75h@hjfks")
    Call<WinetResponseWithParams> createWinet(@Query("winet_type") String type, @Query("ser_number") String serNumber, @Query("vestibule_id") int vestibuleId);

    @DELETE("mobile_redirect.php?485kf9056kjwj3=04jf75h@hjfks")
    Call<SimpleResponse> deleteWinet(@Query("_type") String entity, @Query("_id") int winetId);

    @GET("mobile_redirect.php?485kf9056kjwj3=04jf75h@hjfks")
    Call<WinetDataInfoResponse> getWinet(@Query("_type") String entity, @Query("_id") int winetId);

    @PUT("mobile_redirect.php?485kf9056kjwj3=04jf75h@hjfks")
    Call<WinetDataResponseWithParams> saveWinet(@Query("_type") String entity,
                                                @Query("_id") int winetId,
                                                @Query("winet_type") String type,
                                                @Query("ser_number") String serNumber,
                                                @Query("winet_comment") String comment,
                                                @Query("winet_x") String winetX,
                                                @Query("winet_y") String winetY);

}
