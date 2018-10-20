package org.woolrim.woolrim.Temp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
/*
    현재 임시로 만들어 둔거임.
    서버의 데이터 형식에 맞게 수정해야됨.
 */
public interface DataInterface {
    @GET("{user}")
    Call<Data> getGet(@Path("user") String userName);

    @GET("{user}")
    Call<Data> getPost(
            @Path("user") String userName,
            @Query("lat_l") double lat_l, @Query("lat_r") double lat_r,
            @Query("lng_d") double lng_d, @Query("lng_u") double lng_u
    );

}
