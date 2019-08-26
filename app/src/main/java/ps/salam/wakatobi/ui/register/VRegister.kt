package ps.salam.wakatobi.ui.register


import android.app.ProgressDialog
import android.content.Intent
import android.text.TextUtils.isEmpty
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function3
import io.reactivex.functions.Function9
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.custom_spinner.view.*
import kotlinx.android.synthetic.main.register_screen.*
import ps.salam.wakatobi.R
import ps.salam.wakatobi.base.BaseActivity
import ps.salam.wakatobi.model.DataDistrict
import ps.salam.wakatobi.model.DataProvince
import ps.salam.wakatobi.model.DataSubDistrict
import ps.salam.wakatobi.model.DataVillage
import ps.salam.wakatobi.ui.account.verification.VAccountVerification
import ps.salam.wakatobi.utils.RxHelperEditText
import ps.salam.wakatobi.widget.CustomToastView
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

class VRegister : BaseActivity(), IRegister.View {

    private val composite = CompositeDisposable()
    private val mPresenter = PRegister()

    private var provinceCode = "74"
    private var districtCode = "7407"
    private var subDistrictCode = ""
    private var villageCode = ""

    private var isValid = false
    private var jk: String = "L"
    private lateinit var userType: String

    private lateinit var dialog: ProgressDialog

    override fun getLayoutResource(): Int {
        return R.layout.register_screen
    }

    override fun myCodeHere() {
        title = getString(R.string.register_account)

        dialog = ProgressDialog(this)
        dialog.setMessage(getString(R.string.please_wait))
        dialog.setCancelable(false)

        formValidation()

        rg_sex.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_man -> jk = "L"
                R.id.rb_woman -> jk = "P"
            }
        }

        btn_register.setOnClickListener {
            if (isValid) {
                val items: HashMap<String, String> = HashMap()
                items.put("action", "register")
                items.put("username", tet_username.text.toString())
                items.put("password", tet_retype_password.text.toString())
                items.put("ktp", tet_no_ktp.text.toString())
                items.put("mail", tet_email.text.toString())
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
                items.put("tipe_user", userType)
                mPresenter.sendForm(items)
            } else {
                CustomToastView.makeText(this, getString(R.string.error_empty), Toast.LENGTH_SHORT)
            }
        }

        setupView()
    }


    override fun setupView() {
        mPresenter.attachView(this)

        if (intent.extras.getString("type") == getString(R.string.visitor)) {
            cs_province.visibility = View.VISIBLE
            cs_district.visibility = View.VISIBLE
            mPresenter.getProvince()
            userType = "0"
        } else {
            mPresenter.getSubDistrict(districtCode)
            userType = "1"
        }


    }

    override fun onGetProvince(response: List<DataProvince.Response>) {
        val adapter = ArrayAdapter<DataProvince.Response>(this, android.R.layout.simple_spinner_item, response)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        cs_province.sp_content.adapter = adapter
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
        cs_village.sp_content.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                villageCode = response[position].kode_desa
                setSpinner(true)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
    }

    override fun onSuccess(idUser: String) {
        val validation = Intent(applicationContext, VAccountVerification::class.java)
        validation.putExtra("id_user", idUser)
        startActivity(validation)
        finish()
    }

    override fun onError(errorMessage: String) {
        CustomToastView.makeText(this, errorMessage, Toast.LENGTH_SHORT)
    }

    override fun formValidation() {
        composite.add(checkName().subscribe({ isValid ->
            if (!isValid) {
                tet_full_name.error = getString(R.string.name_not_valid)
                tet_full_name.requestFocus()
            } else
                tet_full_name.error = null

        }))
        composite.add(checkKTP().subscribe({ isValid ->
            if (!isValid) {
                tet_no_ktp.error = getString(R.string.ktp_not_valid)
                tet_no_ktp.requestFocus()
            } else
                tet_no_ktp.error = null
        }))
        composite.add(checkPassword().subscribe({ isValid ->
            if (!isValid) {
                tet_password.error = getString(R.string.password_not_valid)
                tet_password.requestFocus()
            } else tet_password.error = null
        }))

        composite.add(checkRetypePassword().subscribe({ isValid ->
            if (!isValid) {
                tet_retype_password.error = getString(R.string.password_not_match)
                tet_retype_password.requestFocus()
            } else tet_retype_password.error = null
        }))

        composite.add(checkEmail().subscribe({ isValid ->
            if (!isValid) {
                tet_email.error = getString(R.string.email_not_valid)
                tet_email.requestFocus()
            } else tet_email.error = null
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
        composite.add(checkAllForm().subscribe({ isValid -> this.isValid = isValid }))
    }

    private fun checkName(): Observable<Boolean> {
        return RxHelperEditText.getTextWatcherObservable(tet_full_name)
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .map({ name: String -> !isEmpty(name) && authName(name) })
    }

    private fun checkKTP(): Observable<Boolean> {
        return RxHelperEditText.getTextWatcherObservable(tet_no_ktp)
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .map({ ktp: String -> !isEmpty(ktp) && tet_no_ktp.length() > 5 })
    }

    private fun checkPassword(): Observable<Boolean> {
        return RxHelperEditText.getTextWatcherObservable(tet_password)
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .map({ password: String -> !isEmpty(password) && tet_password.length() > 5 })
    }

    private fun checkRetypePassword(): Observable<Boolean> {
        return RxHelperEditText.getTextWatcherObservable(tet_retype_password)
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .map({ retypePassword: String -> !isEmpty(retypePassword) && retypePassword == tet_password.text.toString() })
    }


    private fun checkEmail(): Observable<Boolean> {
        return RxHelperEditText.getTextWatcherObservable(tet_email)
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .map({ email: String -> !isEmpty(email) && authEmail(email) })
    }

    private fun checkPhone(): Observable<Boolean> {
        return RxHelperEditText.getTextWatcherObservable(tet_phone)
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .map({ phone: String -> !isEmpty(phone) && tet_phone.length() > 10 })
    }

    private fun checkWork(): Observable<Boolean> {
        return RxHelperEditText.getTextWatcherObservable(tet_job_name)
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .map({ job: String -> !isEmpty(job) && tet_job_name.length() > 1 })
    }

    private fun checkAddress(): Observable<Boolean> {
        return RxHelperEditText.getTextWatcherObservable(tet_address)
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .map({ address: String -> !isEmpty(address) && tet_address.length() > 5 })
    }

    private fun checkRT(): Observable<Boolean> {
        return RxHelperEditText.getTextWatcherObservable(tet_rt)
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .map({ rt: String -> !isEmpty(rt) && tet_rt.length() == 3 })
    }

    private fun checkRW(): Observable<Boolean> {
        return RxHelperEditText.getTextWatcherObservable(tet_rw)
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .map({ rw: String -> !isEmpty(rw) && tet_rw.length() == 3 })
    }

    private fun checkUsername(): Observable<Boolean> {
        return RxHelperEditText.getTextWatcherObservable(tet_username)
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .map({ username: String -> !isEmpty(username) })
    }

    private fun checkForm(): Observable<Boolean> {
        return Observable.combineLatest<Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean>((checkName()),
                checkKTP(), checkPassword(), checkRetypePassword(), checkEmail(), checkAddress(), checkWork(), checkRT(), checkRW(),
                (Function9 { t1, t2, t3, t4, t5, t6, t7, t8, t9 -> t1 && t2 && t3 && t4 && t5 && t6 && t7 && t8 && t9 }))
    }

    private fun checkAllForm(): Observable<Boolean> {
        return Observable.combineLatest(checkForm(), checkPhone(), checkUsername(), (Function3 { t1, t2, t3 -> t1 && t2 && t3 }))
    }

    private fun authName(nama: String): Boolean {
        val p = Pattern.compile("^[A-Za-z\\s]+[.]?[A-Za-z\\s]+$")
        val m = p.matcher(nama)
        return m.matches()
    }

    private fun authEmail(email: String): Boolean {
        val p = Pattern.compile("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}")
        val m = p.matcher(email)
        return m.matches()
    }

    override fun onShowDialog(isShow: Boolean) {
        if (isShow) dialog.show()
        else dialog.dismiss()
    }

    private fun setSpinner(activated: Boolean) {
        cs_province.isEnabled = activated
        cs_district.isEnabled = activated
        cs_sub_district.isEnabled = activated
        cs_village.isEnabled = activated
    }

    override fun onDetachScreen() {
        composite.clear()
        mPresenter.detachView()
    }
}
