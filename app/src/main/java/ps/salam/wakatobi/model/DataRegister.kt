package ps.salam.wakatobi.model

/**
 *----------------------------------------------
 * Created by ukieTux on 3/5/17 with ♥ .
 * @email  : ukie.tux@gmail.com
 * @github : https://www.github.com/tuxkids
 * ----------------------------------------------
 *          © 2017 | All Rights Reserved
 */
class DataRegister {
    lateinit var diagnostic:Diagnostic
    lateinit var response: Response

    class Response {
        val id: String? = null
        val message: String? = null
    }
}