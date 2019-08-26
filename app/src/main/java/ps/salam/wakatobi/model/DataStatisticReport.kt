package ps.salam.wakatobi.model

/**
 **********************************************
 * Created by ukie on 4/5/17 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2017 | All Right Reserved
 */
class DataStatisticReport {
    lateinit var diagnostic: Diagnostic
    lateinit var response: List<Response>

    class Response {
        val judul: String? = null
        val jumlah_laporan: String? = null
    }
}