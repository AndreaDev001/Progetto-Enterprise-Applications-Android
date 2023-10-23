package com.enterpriseapplications.config

import android.app.Application
import com.enterpriseapplications.config.authentication.AuthenticationManager
import com.enterpriseapplications.config.authentication.AuthorizationInterceptor
import com.enterpriseapplications.controllers.BanRetrofitApi
import com.enterpriseapplications.controllers.CategoryRetrofitApi
import com.enterpriseapplications.controllers.ConversationRetrofitApi
import com.enterpriseapplications.controllers.FollowRetrofitApi
import com.enterpriseapplications.controllers.LikeRetrofitApi
import com.enterpriseapplications.controllers.MessageRetrofitApi
import com.enterpriseapplications.controllers.OfferRetrofitApi
import com.enterpriseapplications.controllers.OrderRetrofitApi
import com.enterpriseapplications.controllers.PaymentMethodRetrofitApi
import com.enterpriseapplications.controllers.ProductRetrofitApi
import com.enterpriseapplications.controllers.ReplyRetrofitApi
import com.enterpriseapplications.controllers.ReviewRetrofitApi
import com.enterpriseapplications.controllers.reports.ReportRetrofitApi
import com.enterpriseapplications.controllers.UserRetrofitApi
import com.enterpriseapplications.controllers.images.ImageRetrofitApi
import com.enterpriseapplications.controllers.images.ProductImageRetrofitApi
import com.enterpriseapplications.controllers.images.UserImageRetrofitApi
import com.enterpriseapplications.controllers.reports.MessageReportRetrofitApi
import com.enterpriseapplications.controllers.reports.ProductReportRetrofitApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class RetrofitConfig(val application: Application,authenticationManager: AuthenticationManager)
{
    companion object
    {
        var resourceServerIpAddress: String = "192.168.1.74:8080";
        var baseURL: String = "http://$resourceServerIpAddress/api/v1/"
    }
    private val httpClient: OkHttpClient = OkHttpClient.Builder().addInterceptor(AuthorizationInterceptor(authenticationManager)).build()
    private val retrofit: Retrofit = Retrofit.
            Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create()).
            client(httpClient)
            .build()

    val productController: ProductRetrofitApi = retrofit.create(ProductRetrofitApi::class.java)
    val categoryController: CategoryRetrofitApi = retrofit.create(CategoryRetrofitApi::class.java)
    val userController: UserRetrofitApi = retrofit.create(UserRetrofitApi::class.java)
    val banController: BanRetrofitApi = retrofit.create(BanRetrofitApi::class.java)
    val reportController: ReportRetrofitApi = retrofit.create(ReportRetrofitApi::class.java)
    val productReportController: ProductReportRetrofitApi = retrofit.create(ProductReportRetrofitApi::class.java)
    val messageReportController: MessageReportRetrofitApi = retrofit.create(MessageReportRetrofitApi::class.java)
    val followController: FollowRetrofitApi = retrofit.create(FollowRetrofitApi::class.java)
    val messageController: MessageRetrofitApi = retrofit.create(MessageRetrofitApi::class.java)
    val orderController: OrderRetrofitApi = retrofit.create(OrderRetrofitApi::class.java)
    val offerController: OfferRetrofitApi = retrofit.create(OfferRetrofitApi::class.java)
    val paymentMethodController: PaymentMethodRetrofitApi = retrofit.create(PaymentMethodRetrofitApi::class.java)
    val likeController: LikeRetrofitApi  = retrofit.create(LikeRetrofitApi::class.java)
    val reviewController: ReviewRetrofitApi = retrofit.create(ReviewRetrofitApi::class.java)
    val conversationController: ConversationRetrofitApi = retrofit.create(ConversationRetrofitApi::class.java)
    val replyController: ReplyRetrofitApi = retrofit.create(ReplyRetrofitApi::class.java)
    val imageController: ImageRetrofitApi = retrofit.create(ImageRetrofitApi::class.java)
    val productImageController: ProductImageRetrofitApi = retrofit.create(ProductImageRetrofitApi::class.java)
    val userImageController: UserImageRetrofitApi = retrofit.create(UserImageRetrofitApi::class.java)
}