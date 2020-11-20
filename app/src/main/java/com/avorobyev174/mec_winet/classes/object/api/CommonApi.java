package com.avorobyev174.mec_winet.classes.object.api;

import com.avorobyev174.mec_winet.classes.api.SimpleResponse;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CommonApi {
    @GET("common_object/?485kf9056kjwj3=04jf75h@hjfks")
    Call<CommonInfoResponse> getCommonObjects();

    @POST("common_object/?485kf9056kjwj3=04jf75h@hjfks")
    Call<CommonResponseWithParams> createCommonObject(@Query("parent_id") int parentId,
                                                    @Query("object_name") String name
                                             );

    @DELETE("mobile_redirect.php?485kf9056kjwj3=04jf75h@hjfks")
    Call<SimpleResponse> deleteCommonObject(@Query("_type") String entity, @Query("_id") int objId);

    @GET("common_object/?485kf9056kjwj3=04jf75h@hjfks")
    Call<CommonInfoResponse> getCommonObject(@Query("id") int objId);
}
