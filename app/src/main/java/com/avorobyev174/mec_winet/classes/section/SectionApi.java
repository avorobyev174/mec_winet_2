package com.avorobyev174.mec_winet.classes.section;

import com.avorobyev174.mec_winet.classes.api.SimpleResponse;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface SectionApi {
    @GET("section/?485kf9056kjwj3=04jf75h@hjfks")
    Call<SectonInfoResponse> getSections(@Query("house_id") int houseId);

    @POST("section/?485kf9056kjwj3=04jf75h@hjfks")
    Call<SectionResponseWithParams> createSection(@Query("section_number") String sectionNumber, @Query("house_id") int houseId);

    @DELETE("mobile_redirect.php?485kf9056kjwj3=04jf75h@hjfks")
    Call<SimpleResponse> deleteSection(@Query("_type") String entity, @Query("_id") int sectionId);

    @GET("section/?485kf9056kjwj3=04jf75h@hjfks")
    Call<SectonInfoResponse> getSection(@Query("section_id") int sectionId);
}
