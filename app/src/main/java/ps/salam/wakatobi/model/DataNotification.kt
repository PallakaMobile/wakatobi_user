package ps.salam.wakatobi.model

/**
 **********************************************
 * Created by ukie on 4/4/17 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2017 | All Right Reserved
 */
class DataNotification {
    lateinit var diagnostic: Diagnostic
    lateinit var response: Response

    class Response {
        val status_notif: Int = 0
    }
}