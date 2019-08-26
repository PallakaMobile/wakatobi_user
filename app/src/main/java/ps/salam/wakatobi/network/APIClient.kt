package ps.salam.wakatobi.network

import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import ps.salam.wakatobi.model.*
import retrofit2.Response
import retrofit2.http.*
import kotlin.collections.HashMap

/**
 * ----------------------------------------------
 * Created by ukieTux on 2/28/17 with ♥ .

 * @email : ukie.tux@gmail.com
 * *
 * @github : https://www.github.com/tuxkids
 * * ----------------------------------------------
 * * © 2017 | All Rights Reserved
 */

interface APIClient {

    //Login
    @FormUrlEncoded
    @POST("api.php?token=de047cf89fb0c244f3379316f67ffc7241805e7f")
    fun setLogin(@FieldMap items: HashMap<String, String>): Observable<Response<DataLogin>>

    @FormUrlEncoded
    @POST("api.php?token=de047cf89fb0c244f3379316f67ffc7241805e7f")
    fun resendVerification(@FieldMap items: HashMap<String, String>): Observable<Response<DiagnosticOnly>>

    @FormUrlEncoded
    @POST("api.php?token=de047cf89fb0c244f3379316f67ffc7241805e7f")
    fun verification(@FieldMap items: HashMap<String, String>): Observable<Response<DiagnosticOnly>>

    //forgot password
    @FormUrlEncoded
    @POST("api.php?token=de047cf89fb0c244f3379316f67ffc7241805e7f")
    fun forgotPassword(@FieldMap items: HashMap<String, String>): Observable<Response<DiagnosticOnly>>

    //register
    @GET("api.php?token=de047cf89fb0c244f3379316f67ffc7241805e7f&get=provinsi")
    fun getProvince(): Observable<Response<DataProvince>>

    @GET("api.php?token=de047cf89fb0c244f3379316f67ffc7241805e7f&get=kabupaten")
    fun getDistrict(@Query("kode_wilayah") provinceCode: String): Observable<Response<DataDistrict>>

    @GET("api.php?token=de047cf89fb0c244f3379316f67ffc7241805e7f&get=kecamatan")
    fun getSubDistrict(@Query("kode_wilayah") districtCode: String): Observable<Response<DataSubDistrict>>

    @GET("api.php?token=de047cf89fb0c244f3379316f67ffc7241805e7f&get=desa")
    fun getVillage(@Query("kode_wilayah") subDistrictCode: String): Observable<Response<DataVillage>>

    @FormUrlEncoded
    @POST("api.php?token=de047cf89fb0c244f3379316f67ffc7241805e7f")
    fun sendRegister(@FieldMap items: HashMap<String, String>): Observable<Response<DataRegister>>

    //profile
    @GET("api.php?token=de047cf89fb0c244f3379316f67ffc7241805e7f&get=user")
    fun getProfile(@Query("id") id_user: String): Observable<Response<DataProfile>>

    //slider
    @GET("api.php?token=de047cf89fb0c244f3379316f67ffc7241805e7f&get=slider&limit=3")
    fun getSlider(): Observable<Response<DataSlider>>

    //create report
    @GET("api.php?token=de047cf89fb0c244f3379316f67ffc7241805e7f&get=kategori")
    fun getCategoryReport(): Observable<Response<DataCategoryReport>>

    @Multipart
    @POST("api.php?token=de047cf89fb0c244f3379316f67ffc7241805e7f")
    fun sendReport(@PartMap items: HashMap<String, RequestBody>, @Part image: MultipartBody.Part): Observable<Response<DiagnosticOnly>>

    @GET("api.php?token=de047cf89fb0c244f3379316f67ffc7241805e7f&get=laporan")
    fun getReport(@Query("page") page: Int, @Query("filter") filter: Int, @Query("user") user: String): Observable<Response<DataReport>>

    @GET("api.php?token=de047cf89fb0c244f3379316f67ffc7241805e7f&get=berita")
    fun getInfo(@Query("page") page: Int): Observable<Response<DataInfo>>

    @FormUrlEncoded
    @POST("api.php?token=de047cf89fb0c244f3379316f67ffc7241805e7f")
    fun sendLiked(@FieldMap items: HashMap<String, String>): Observable<Response<DataLiked>>

    @GET("api.php?token=de047cf89fb0c244f3379316f67ffc7241805e7f&get=detail-laporan")
    fun getDetailReport(@Query("id") id_report: String, @Query("user") user: String): Observable<Response<DataDetailReport>>

    @GET("api.php?token=de047cf89fb0c244f3379316f67ffc7241805e7f&get=komentar")
    fun getComment(@Query("id_laporan") id_laporan: String, @Query("page") page: Int, @Query("user") user: String): Observable<Response<DataComment>>

    @FormUrlEncoded
    @POST("api.php?token=de047cf89fb0c244f3379316f67ffc7241805e7f")
    fun sendComment(@FieldMap items: HashMap<String, String>): Observable<Response<DiagnosticOnly>>

    @FormUrlEncoded
    @POST("api.php?token=de047cf89fb0c244f3379316f67ffc7241805e7f")
    fun sendShare(@FieldMap items: HashMap<String, String>): Observable<Response<DataShare>>

    @GET("api.php?token=de047cf89fb0c244f3379316f67ffc7241805e7f&get=detail-berita")
    fun getDetailInfo(@Query("id") id_info: String): Observable<Response<DataDetailInfo>>

    @FormUrlEncoded
    @POST("api.php?token=de047cf89fb0c244f3379316f67ffc7241805e7f")
    fun shareInfo(@FieldMap items: HashMap<String, String>): Observable<Response<DataShare>>

    @Multipart
    @POST("api.php?token=de047cf89fb0c244f3379316f67ffc7241805e7f")
    fun updateProfilePicture(@PartMap items: HashMap<String, RequestBody>, @Part image: MultipartBody.Part): Observable<Response<DataProfilePicture>>

    @FormUrlEncoded
    @POST("api.php?token=de047cf89fb0c244f3379316f67ffc7241805e7f")
    fun updateProfileInfo(@FieldMap items: HashMap<String, String>): Observable<Response<DiagnosticOnly>>

    @FormUrlEncoded
    @POST("api.php?token=de047cf89fb0c244f3379316f67ffc7241805e7f")
    fun updatePassword(@FieldMap items: HashMap<String, String>): Observable<Response<DiagnosticOnly>>

    @FormUrlEncoded
    @POST("api.php?token=de047cf89fb0c244f3379316f67ffc7241805e7f")
    fun updateEmail(@FieldMap items: HashMap<String, String>): Observable<Response<DiagnosticOnly>>

    @GET("api.php?token=de047cf89fb0c244f3379316f67ffc7241805e7f&get=cari-laporan")
    fun searchReport(@Query("page") page: Int, @Query("user") user: String, @Query("keyword") keyword: String): Observable<Response<DataReport>>

    @FormUrlEncoded
    @POST("api.php?token=de047cf89fb0c244f3379316f67ffc7241805e7f")
    fun sendFirebaseID(@FieldMap items: HashMap<String, String>): Observable<Response<DiagnosticOnly>>

    @GET("api.php?token=de047cf89fb0c244f3379316f67ffc7241805e7f&get=kategori")
    fun getCategory(): Observable<Response<DataCategory>>

    @GET("api.php?token=de047cf89fb0c244f3379316f67ffc7241805e7f&get=laporan-kategori")
    fun getCategoryReportDetail(@Query("page") page: Int, @Query("kategori") category: String, @Query("user") user: String): Observable<Response<DataReport>>

    @FormUrlEncoded
    @POST("api.php?token=de047cf89fb0c244f3379316f67ffc7241805e7f")
    fun setNotificationStatus(@FieldMap items: HashMap<String, String>): Observable<Response<DataNotification>>

    @GET("api.php?token=de047cf89fb0c244f3379316f67ffc7241805e7f&get=laporan-saya")
    fun getReportStatus(@Query("page") page: Int, @Query("filter") filter: Int, @Query("user") user: String): Observable<Response<DataReportStatus>>

    @GET("api.php?token=de047cf89fb0c244f3379316f67ffc7241805e7f&get=fetch-statistik-user")
    fun getStatisticUser(@Query("by") by: String, @Query("user") user: String): Observable<Response<DataStatisticReport>>

    @GET("api.php?token=de047cf89fb0c244f3379316f67ffc7241805e7f&get=fetch-statistik")
    fun getStatisticAll(@Query("by") by: String): Observable<Response<DataStatisticReport>>

    @GET("api.php?token=de047cf89fb0c244f3379316f67ffc7241805e7f&get=stat-count-lapor")
    fun getCountStatistic(@Query("user") user: String): Observable<Response<DataCountStatistic>>

    @FormUrlEncoded
    @POST("api.php?token=de047cf89fb0c244f3379316f67ffc7241805e7f")
    fun removeComment(@FieldMap items: HashMap<String, String>): Observable<Response<DiagnosticOnly>>
}
