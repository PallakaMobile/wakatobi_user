package ps.salam.wakatobi.model

import android.os.Parcel
import android.os.Parcelable

class DataProfile {
    lateinit var response: Response
    lateinit var diagnostic: Diagnostic

    class Response : Parcelable {

        var id: String? = null
        var nama: String? = null
        var uname: String? = null
        var mail: String? = null
        var ktp: String? = null
        var foto: String? = null
        var telp: String? = null
        var jk: String? = null
        var pekerjaan: String? = null
        var alamat: String? = null
        var provinsi: String? = null
        var kabupaten: String? = null
        var kecamatan: String? = null
        var desa_kelurahan: String? = null
        var kode_provinsi: String? = null
        var kode_kabupaten: String? = null
        var kode_kecamatan: String? = null
        var kode_desa_kelurahan: String? = null
        var rt: String? = null
        var rw: String? = null
        var poin: String? = null
        var aktif: Int = 0
        var verifikasi: Int = 0
        var tipe_user: Int = 0
        var tot_like: Int? = null
        var tot_lapor: Int? = null
        var tot_komentar: Int? = null
        var status_notif: Int? = null


        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeString(this.id)
            dest.writeString(this.nama)
            dest.writeString(this.uname)
            dest.writeString(this.mail)
            dest.writeString(this.ktp)
            dest.writeString(this.foto)
            dest.writeString(this.telp)
            dest.writeString(this.jk)
            dest.writeString(this.pekerjaan)
            dest.writeString(this.alamat)
            dest.writeString(this.provinsi)
            dest.writeString(this.kabupaten)
            dest.writeString(this.kecamatan)
            dest.writeString(this.desa_kelurahan)
            dest.writeString(this.kode_provinsi)
            dest.writeString(this.kode_kabupaten)
            dest.writeString(this.kode_kecamatan)
            dest.writeString(this.kode_desa_kelurahan)
            dest.writeString(this.rt)
            dest.writeString(this.rw)
            dest.writeString(this.poin)
            dest.writeInt(this.aktif)
            dest.writeInt(this.verifikasi)
            dest.writeInt(this.tipe_user)
            dest.writeValue(this.status_notif)
            dest.writeValue(this.tot_like)
            dest.writeValue(this.tot_lapor)
            dest.writeValue(this.tot_komentar)
        }

        constructor()

        private constructor(`in`: Parcel) {
            this.id = `in`.readString()
            this.nama = `in`.readString()
            this.uname = `in`.readString()
            this.mail = `in`.readString()
            this.ktp = `in`.readString()
            this.foto = `in`.readString()
            this.telp = `in`.readString()
            this.jk = `in`.readString()
            this.pekerjaan = `in`.readString()
            this.alamat = `in`.readString()
            this.provinsi = `in`.readString()
            this.kabupaten = `in`.readString()
            this.kecamatan = `in`.readString()
            this.kode_desa_kelurahan = `in`.readString()
            this.kode_provinsi = `in`.readString()
            this.kode_kabupaten = `in`.readString()
            this.kode_kecamatan = `in`.readString()
            this.kode_desa_kelurahan = `in`.readString()
            this.rt = `in`.readString()
            this.rw = `in`.readString()
            this.poin = `in`.readString()
            this.aktif = `in`.readInt()
            this.verifikasi = `in`.readInt()
            this.tipe_user = `in`.readInt()
            this.tot_like = `in`.readValue(Int::class.java.classLoader) as Int
            this.tot_lapor = `in`.readValue(Int::class.java.classLoader) as Int
            this.tot_komentar = `in`.readValue(Int::class.java.classLoader) as Int
            this.status_notif = `in`.readValue(Int::class.java.classLoader) as Int
        }

        companion object {
            @JvmField
            var CREATOR: Parcelable.Creator<Response> = object : Parcelable.Creator<Response> {
                override fun createFromParcel(source: Parcel): Response {
                    return Response(source)
                }

                override fun newArray(size: Int): Array<Response> {
                    return newArray(size)
                }
            }
        }
    }
}
