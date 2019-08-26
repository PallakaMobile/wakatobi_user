package ps.salam.wakatobi.utils

/**
 * *********************************************
 * Created by ukie on 4/5/17 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 * *********************************************
 * © 2017 | All Right Reserved
 */

import java.lang.reflect.Type

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit

class NullOnEmptyConverterFactory : Converter.Factory() {

    override fun responseBodyConverter(type: Type?, annotations: Array<Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *> {
        val delegate = retrofit!!.nextResponseBodyConverter<ResponseBody>(this, type!!, annotations!!)
        return Converter<ResponseBody, Any> { body ->
            if (body.contentLength().toInt() == 0) return@Converter null
            delegate.convert(body)
        }
    }
}
