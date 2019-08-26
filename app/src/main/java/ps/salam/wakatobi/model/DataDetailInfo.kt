package ps.salam.wakatobi.model

/**
 **********************************************
 * Created by ukie on 3/28/17 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2017 | All Right Reserved
 */
class DataDetailInfo {
    lateinit var diagnostic: Diagnostic
    lateinit var response: Response

    class Response {
        val id: String? = null
        val judul: String? = null
        val gambar: String? = null
        val isi: String? = null
        val tgl_post: String? = null
        val viewer: String? = null
        val foto_instansi: String? = null
        val nama_instansi: String? = null
    }
}