package com.enterpriseapplications.controllers

import com.enterpriseapplications.model.Follow
import com.enterpriseapplications.model.PagedModel
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.UUID

interface FollowRetrofitApi {
    @GET("follows/public/{userID}/followers")
    fun getFollowers(@Path("userID") userID: UUID,@Query("page") page: Int = 0,@Query("pageSize") pageSize: Int = 20): Call<PagedModel<Follow>>;
    @GET("follows/public/{userID}/followed")
    fun getFollowed(@Path("userID") userID: UUID,@Query("page") page: Int = 0,@Query("pageSize") pageSize: Int = 20): Call<PagedModel<Follow>>;
    @GET("follows/public/{followerID}/{followedID}")
    fun getFollow(@Path("followerID") followerID: UUID,@Path("followedID") followedID: UUID): Call<Follow>;
    @POST("follows/private/{followedID}")
    fun createFollows(@Path("followedID") followedID: UUID): Call<Follow>;
    @DELETE("follows/private/{followID}")
    fun deleteFollows(@Path("followID") followedID: UUID): Call<Follow>;
    @DELETE("follows/private/followed/{followedID}")
    fun deleteFollowsByFollowed(@Path("followID") followedID: UUID): Call<Follow>;

}