package ps.salam.wakatobi.model

/**
 **********************************************
 * Created by ukie on 4/4/17 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2017 | All Right Reserved
 */
class DataCategory {
    lateinit var diagnostic: Diagnostic
    lateinit var response: List<Response>

    class Response{
        val id: String? = null
        val nama: String? = null
        val keterangan: String? = null

    }
}