package ps.salam.wakatobi.model

/**
 **********************************************
 * Created by ukie on 3/16/17 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2017 | All Right Reserved
 */
class DataComment {
    lateinit var diagnostic: Diagnostic
    lateinit var pagination: Pagination
    lateinit var response: List<Response>


    class Response {
        var id: String? = null
        var user: String? = null
        var nama_user: String? = null
        var komentar: String? = null
        var waktu_komentar: String? = null
        var foto: String? = null
        var status_komentar: Int = 0
    }
}