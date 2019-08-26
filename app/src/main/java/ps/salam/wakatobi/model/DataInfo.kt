package ps.salam.wakatobi.model

/**
 *----------------------------------------------
 * Created by ukieTux on 3/9/17 with ♥ .
 * @email  : ukie.tux@gmail.com
 * @github : https://www.github.com/tuxkids
 * ----------------------------------------------
 *          © 2017 | All Rights Reserved
 */
class DataInfo {
    lateinit var diagnostic: Diagnostic
    lateinit var pagination: Pagination
    lateinit var response: List<Response>

    class Response {
        var id: String? = null
        var judul: String? = null
        var gambar: String? = null
        var isi: String? = null
        var tanggal_post: String? = null
        var hit = 0
        var instansi: String? = null
    }

}