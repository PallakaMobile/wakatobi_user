package ps.salam.wakatobi.network

import okhttp3.Cache
import okhttp3.Interceptor
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import ps.salam.wakatobi.services.ConnectionReceiver
import ps.salam.wakatobi.utils.AppController
import java.io.File

/**
 * ----------------------------------------------
 * Created by ukieTux on 5/24/16 with ♥ .

 * @email : ukie.tux@gmail.com
 * *
 * @github : https://www.github.com/tuxkids
 * * ----------------------------------------------
 * * © 2017 | All Rights Reserved
 */
internal class OkhttpClient
//    public void setHeaders(Map<String, String> headers) {
//        this.headers = headers;
//    }

private constructor() {
    var okHttpClient: OkHttpClient? = null
        private set

    init {

        System.setProperty("http.keepAlive", "false");
        val builder = OkHttpClient.Builder()

        //Logging -> hapus saat release
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(logging)

        builder.connectTimeout(60, TimeUnit.SECONDS)
        builder.readTimeout(60, TimeUnit.SECONDS)
        builder.writeTimeout(60, TimeUnit.SECONDS)
        builder.addInterceptor(logging)
        builder.retryOnConnectionFailure(true)
        builder.networkInterceptors().add(REWRITE_CACHE_CONTROL_INTERCEPTOR)

        //setup cache
        val httpCacheDirectory = File(AppController.context?.cacheDir, "responses")
        val cacheSize = 10 * 1024 * 1024 // 10 MiB
        val cache = Cache(httpCacheDirectory, cacheSize.toLong())

        builder.cache(cache)

        okHttpClient = builder.build()
        okHttpClient = okHttpClient
    }


    companion object {
        private var ourInstance: OkhttpClient? = null


        val instance: OkhttpClient
            get() {

                if (ourInstance == null) {
                    ourInstance = OkhttpClient()
                }

                return ourInstance!!
            }


        private val REWRITE_CACHE_CONTROL_INTERCEPTOR = Interceptor { chain ->
            val originalResponse = chain.proceed(chain?.request())
            if (ConnectionReceiver.isConnected) {
                val maxAge = 1 // read from cache for 1 second
                originalResponse.newBuilder()
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build()
            } else {
                val maxStale = 60 * 60 * 24 * 28 // tolerate 4-weeks stale
                originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build()
            }
        }

    }
}
