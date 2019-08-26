package ps.salam.wakatobi.model

/**
 **********************************************
 * Created by ukie on 4/5/17 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2017 | All Right Reserved
 */
class DataCountStatistic {
    lateinit var diagnostic: Diagnostic
    lateinit var response: List<Response>

    class Response {
        val type: Int = 0
        val jumlah: Int = 0
    }
}