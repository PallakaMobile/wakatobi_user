package ps.salam.wakatobi.model

/**
 **********************************************
 * Created by ukie on 4/5/17 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2017 | All Right Reserved
 */
class DataReportStatus {
    lateinit var diagnostic: Diagnostic
    lateinit var pagination: Pagination
    lateinit var response: List<Response>

    class Response {
        val id: String? = null
        val gambar: String? = null
        val gambar_thumbnail: String? = null
        val gambar_selesai: String? = null
        val judul: String? = null
        val pesan: String? = null
        val pesan_selesai: String? = null
        val status_laporan: Int = 0
        val waktu_submit: String? = null
        val waktu_belum: String? = null
        val waktu_proses: String? = null
        val waktu_selesai: String? = null
    }
}