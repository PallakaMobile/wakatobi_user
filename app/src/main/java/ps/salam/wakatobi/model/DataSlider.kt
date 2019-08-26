package ps.salam.wakatobi.model

/**
 *----------------------------------------------
 * Created by ukieTux on 2/24/17 with ♥ .
 * @email  : ukie.tux@gmail.com
 * @github : https://www.github.com/tuxkids
 * ----------------------------------------------
 *          © 2017 | All Rights Reserved
 */

class DataSlider {
    lateinit var diagnostic: Diagnostic
    lateinit var response: List<Response>

    class Response {
        var id: String? = null
        var judul: String? = null
        var isi: String? = null
        var gambar: String? = null
        var thumbnail: String? = null
    }

}