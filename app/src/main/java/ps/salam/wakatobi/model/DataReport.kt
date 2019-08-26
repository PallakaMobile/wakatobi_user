package ps.salam.wakatobi.model

/**
 *----------------------------------------------
 * Created by ukieTux on 3/8/17 with ♥ .
 * @email  : ukie.tux@gmail.com
 * @github : https://www.github.com/tuxkids
 * ----------------------------------------------
 *          © 2017 | All Rights Reserved
 */
class DataReport {
    lateinit var diagnostic: Diagnostic
    lateinit var pagination: Pagination
    lateinit var response: List<DataDetailReport.Response>

}