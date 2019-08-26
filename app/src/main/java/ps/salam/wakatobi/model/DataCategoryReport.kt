package ps.salam.wakatobi.model

/**
 *----------------------------------------------
 * Created by ukieTux on 3/5/17 with ♥ .
 * @email  : ukie.tux@gmail.com
 * @github : https://www.github.com/tuxkids
 * ----------------------------------------------
 *          © 2017 | All Rights Reserved
 */
class DataCategoryReport {
    lateinit var diagnostic: Diagnostic
    lateinit var response: MutableList<Response>

    class Response {
        var id: String? = null
        var nama: String = ""
        var keterangan: String? = null

        constructor(id: String, nama: String, keterangan: String) {
            this.id = id
            this.nama = nama
            this.keterangan = keterangan
        }

        override fun toString(): String {
            return nama
        }
    }
}