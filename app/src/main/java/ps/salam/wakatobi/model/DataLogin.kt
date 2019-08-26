package ps.salam.wakatobi.model

class DataLogin {
    lateinit var response: Response
    lateinit var diagnostic: Diagnostic


    class Response {
        var jk: String? = null
        var rt: Int = 0
        var telp: String? = null
        var verifikasi: Int = 0
        var uname: String? = null
        var rw: Int = 0
        var kec: String? = null
        var id_user: String? = null
        var aktif: Int = 0
        var alamat: String? = null
        var poin: Int = 0
        var kel: String? = null
        var nama: String? = null
        var foto: String? = null
        var pekerjaan: String? = null
        var tipe_user: Int = 0
        var pass_encrypt:String?=null
    }
}
