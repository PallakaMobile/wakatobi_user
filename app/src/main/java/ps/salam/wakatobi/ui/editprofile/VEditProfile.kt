package ps.salam.wakatobi.ui.editprofile

import android.app.ProgressDialog
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function6
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.custom_spinner.view.*
import kotlinx.android.synthetic.main.edit_profile_screen.*
import ps.salam.wakatobi.R
import ps.salam.wakatobi.base.BaseActivity
import ps.salam.wakatobi.model.*
import ps.salam.wakatobi.utils.RxHelperEditText
import ps.salam.wakatobi.utils.SharedKey
import ps.salam.wakatobi.utils.SharedPref
import ps.salam.wakatobi.widget.CustomToastView
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

class VEditProfile : BaseActivity(), IEditProfile.View {
    private val mPresenter = PEditProfile()
    private val composite = CompositeDisposable()

    private lateinit var dialog: ProgressDialog

    private var provinceCode = "74"
    private var districtCode = "7407"
    private var subDistrictCode = ""
    private var villageCode = ""

    private var isValid = false
    private var jk: String = "L"
    private lateinit var userType: String

    override fun getLayoutResource(): Int {
        return R.layout.edit_profile_screen
    }

    override fun myCodeHere() {
        title = getString(R.string.edit_profile)
        val response: DataProfile.Response = intent.extras.getParcelable("response")

        //form validation
        formValidation()

        setupView(response)
    }

    override fun setupView(response: DataProfile.Response) {
        mPresenter.attachView(this)

        dialog = ProgressDialog(this)
        dialog.setMessage(getString(R.string.please_wait))
        dialog.setCancelable(false)

        tet_full_name.setText(response.nama)
        if (response.jk == "L")
            rb_man.isChecked = true
        else if (response.jk == "P")
            rb_woman.isChecked = true

        rg_sex.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_man -> jk = "L"
                R.id.rb_woman -> jk = "P"
            }
        }

        tet_phone.setText(response.telp)
        tet_job_name.setText(response.pekerjaan)
        tet_address.setText(response.alamat)

        tet_rt.setText(response.rt)
        tet_rw.setText(response.rw)

        //setup area
        provinceCode = response.kode_provinsi!!
        districtCode = response.kode_kabupaten!!
        subDistrictCode = response.kode_kecamatan!!
        villageCode = response.kode_desa_kelurahan!!

        if (intent.extras.getString("type") == getString(R.string.visitor)) {
            cs_province.visibility = View.VISIBLE
            cs_district.visibility = View.VISIBLE
            mPresenter.getProvince()
            userType = "0"
        } else {
            mPresenter.getSubDistrict(districtCode)
            userType = "1"
        }

        btn_update_profile.setOnClickListener {
            if (isValid) {
                val items: HashMap<String, String> = HashMap()
                items.put("id", SharedPref.getString(SharedKey.id_user))
                items.put("action", "update-profile-info")
                items.put("nama", tet_full_name.text.toString())
                items.put("telp", tet_phone.text.toString())
                items.put("jk", jk)
                items.put("pekerjaan", tet_job_name.text.toString())
                items.put("alamat", tet_address.text.toString())
                items.put("provinsi", provinceCode)
                items.put("kabupaten", districtCode)
                items.put("kecamatan", subDistrictCode)
                items.put("desa_kelurahan", villageCode)
                items.put("rt", tet_rt.text.toString())
                items.put("rw", tet_rw.text.toString())
                mPresenter.updateProfile(items)
            } else {
                CustomToastView.makeText(this, getString(R.string.error_empty), Toast.LENGTH_SHORT)
            }
        }

    }

    override fun onShowDialog(isShow: Boolean) {
        if (isShow)
            dialog.show()
        else dialog.dismiss()
    }

    override fun onSuccess() {
        finish()
    }

    override fun onShowMessage(message: String) {
        CustomToastView.makeText(this, message, Toast.LENGTH_SHORT)
    }

    override fun onGetProvince(response: List<DataProvince.Response>) {
        val adapter = ArrayAdapter<DataProvince.Response>(this, android.R.layout.simple_spinner_item, response)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        cs_province.sp_content.adapter = adapter

        response.indices
                .filter { response[it].kode_provinsi == provinceCode }
                .map { adapter.getPosition(response[it]) }
                .forEach { cs_province.sp_content.setSelection(it) }

        cs_province.sp_content.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                setSpinner(false)
                provinceCode = response[position].kode_provinsi
                mPresenter.getDistrict(provinceCode)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
    }

    override fun onGetDistrict(response: List<DataDistrict.Response>) {
        val adapter = ArrayAdapter<DataDistrict.Response>(this, android.R.layout.simple_spinner_item, response)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        cs_district.sp_content.adapter = adapter

        response.indices
                .filter { response[it].kode_kabupaten == districtCode }
                .map { adapter.getPosition(response[it]) }
                .forEach { cs_district.sp_content.setSelection(it) }

        cs_district.sp_content.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                setSpinner(false)
                districtCode = response[position].kode_kabupaten
                mPresenter.getSubDistrict(districtCode)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
    }

    override fun onGetSubDistrict(response: List<DataSubDistrict.Response>) {
        val adapter = ArrayAdapter<DataSubDistrict.Response>(this, android.R.layout.simple_spinner_item, response)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        cs_sub_district.sp_content.adapter = adapter

        response.indices
                .filter { response[it].kode_kecamatan == subDistrictCode }
                .map { adapter.getPosition(response[it]) }
                .forEach { cs_sub_district.sp_content.setSelection(it) }

        cs_sub_district.sp_content.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                setSpinner(false)
                subDistrictCode = response[position].kode_kecamatan
                mPresenter.getVillage(subDistrictCode)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
    }

    override fun onGetVillage(response: List<DataVillage.Response>) {
        val adapter = ArrayAdapter<DataVillage.Response>(this, android.R.layout.simple_spinner_item, response)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        cs_village.sp_content.adapter = adapter
        response.indices
                .filter { response[it].kode_desa == villageCode }
                .map { adapter.getPosition(response[it]) }
                .forEach { cs_village.sp_content.setSelection(it) }

        cs_village.sp_content.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                villageCode = response[position].kode_desa
                setSpinner(true)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    private fun formValidation() {
        composite.add(checkName().subscribe({ isValid ->
            if (!isValid) {
                tet_full_name.error = getString(R.string.name_not_valid)
                tet_full_name.requestFocus()
            } else
                tet_full_name.error = null

        }))

        composite.add(checkPhone().subscribe({ isValid ->
            if (!isValid) {
                tet_phone.error = getString(R.string.phone_not_valid)
                tet_phone.requestFocus()
            } else tet_phone.error = null
        }))

        composite.add(checkWork().subscribe({ isValid ->
            if (!isValid) {
                tet_job_name.error = getString(R.string.work_not_valid)
                tet_job_name.requestFocus()
            } else tet_job_name.error = null
        }))

        composite.add(checkAddress().subscribe({ isValid ->
            if (!isValid) {
                tet_address.error = getString(R.string.address_not_valid)
                tet_address.requestFocus()
            } else tet_address.error = null
        }))

        composite.add(checkAddress().subscribe({ isValid ->
            if (!isValid) {
                tet_address.error = getString(R.string.address_not_valid)
                tet_address.requestFocus()
            } else tet_address.error = null
        }))

        composite.add(checkRT().subscribe({ isValid ->
            if (!isValid) {
                tet_rt.error = getString(R.string.rt_rw_not_valid)
                tet_rt.requestFocus()
            } else tet_rt.error = null
        }))

        composite.add(checkRW().subscribe({ isValid ->
            if (!isValid) {
                tet_rw.error = getString(R.string.rt_rw_not_valid)
                tet_rw.requestFocus()
            } else tet_rw.error = null
        }))
        composite.add(checkForm().subscribe({ isValid -> this.isValid = isValid }))
    }

    private fun checkName(): Observable<Boolean> {
        return RxHelperEditText.getTextWatcherObservable(tet_full_name)
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .map({ name: String -> !TextUtils.isEmpty(name) && authName(name) })
    }


    private fun checkPhone(): Observable<Boolean> {
        return RxHelperEditText.getTextWatcherObservable(tet_phone)
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .map({ phone: String -> !TextUtils.isEmpty(phone) && tet_phone.length() > 10 })
    }

    private fun checkWork(): Observable<Boolean> {
        return RxHelperEditText.getTextWatcherObservable(tet_job_name)
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .map({ job: String -> !TextUtils.isEmpty(job) && tet_job_name.length() > 1 })
    }

    private fun checkAddress(): Observable<Boolean> {
        return RxHelperEditText.getTextWatcherObservable(tet_address)
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .map({ address: String -> !TextUtils.isEmpty(address) && tet_address.length() > 5 })
    }

    private fun checkRT(): Observable<Boolean> {
        return RxHelperEditText.getTextWatcherObservable(tet_rt)
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .map({ rt: String -> !TextUtils.isEmpty(rt) && tet_rt.length() == 3 })
    }

    private fun checkRW(): Observable<Boolean> {
        return RxHelperEditText.getTextWatcherObservable(tet_rw)
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .map({ rw: String -> !TextUtils.isEmpty(rw) && tet_rw.length() == 3 })
    }

    private fun checkForm(): Observable<Boolean> {
        return Observable.combineLatest<Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean>((checkName()),
                checkAddress(), checkWork(), checkRT(), checkRW(), checkPhone(),
                (Function6 { t1, t2, t3, t4, t5, t6 -> t1 && t2 && t3 && t4 && t5 && t6 }))

    }

    private fun authName(nama: String): Boolean {
        val p = Pattern.compile("^[A-Za-z\\s]+[.]?[A-Za-z\\s]+$")
        val m = p.matcher(nama)
        return m.matches()
    }

    private fun setSpinner(activated: Boolean) {
        cs_province.isEnabled = activated
        cs_district.isEnabled = activated
        cs_sub_district.isEnabled = activated
        cs_village.isEnabled = activated
    }

    override fun onDetachScreen() {
        mPresenter.detachView()
        composite.clear()
    }

}
