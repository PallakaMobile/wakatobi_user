package ps.salam.wakatobi.model

/**
 **********************************************
 * Created by ukie on 3/18/17 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2017 | All Right Reserved
 */

class DataShare {
    lateinit var diagnostic: Diagnostic
    lateinit var response: Response

    class Response {
        val link_share: String? = null
    }
}