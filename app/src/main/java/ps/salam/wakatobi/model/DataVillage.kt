package ps.salam.wakatobi.model

/**
 *----------------------------------------------
 * Created by ukieTux on 3/4/17 with ♥ .
 * @email  : ukie.tux@gmail.com
 * @github : https://www.github.com/tuxkids
 * ----------------------------------------------
 *          © 2017 | All Rights Reserved
 */
class DataVillage {
    lateinit var diagnostic: Diagnostic
    lateinit var response: List<Response>

    class Response {
        var kode_desa: String = ""
        var desa_kelurahan: String = ""
        override fun toString(): String {
            return desa_kelurahan
        }
    }
}