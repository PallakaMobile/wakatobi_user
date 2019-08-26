package ps.salam.wakatobi.ui.editprofile

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ps.salam.wakatobi.base.BasePresenter
import ps.salam.wakatobi.network.api

/**
 **********************************************
 * Created by ukie on 3/30/17 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2017 | All Right Reserved
 */

class PEditProfile : BasePresenter<IEditProfile.View>(), IEditProfile.Presenter {
    private val composite = CompositeDisposable()
    override fun attachView(mvpView: IEditProfile.View) {
        super.attachView(mvpView)
    }

    override fun detachView() {
        super.detachView()
        composite.clear()
    }

    override fun getProvince() {

        composite.add(api.API_CLIENT.getProvince()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { mView()?.onShowDialog(true) }
                .doOnComplete { mView()?.onShowDialog(false) }
                .subscribe({ province ->
                    if (province.code() == 200) {
                        if (province.body().diagnostic.status == 200) {
                            mView()?.onGetProvince(province.body().response)
                        } else {
                            mView()?.onShowMessage(province.body().diagnostic.description)
                        }
                    } else {
                        mView()?.onShowMessage(province.errorBody().toString())
                    }
                }))
    }

    override fun getDistrict(provinceCode: String) {
        composite.add(api.API_CLIENT.getDistrict(provinceCode)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { mView()?.onShowDialog(true) }
                .doOnComplete { mView()?.onShowDialog(false) }
                .subscribe({ district ->
                    if (district.code() == 200) {
                        if (district.body().diagnostic.status == 200) {
                            mView()?.onGetDistrict(district.body().response)
                        } else {
                            mView()?.onShowMessage(district.body().diagnostic.description)
                        }
                    } else {
                        mView()?.onShowMessage(district.errorBody().string())
                    }
                }))
    }

    override fun getSubDistrict(districtCode: String) {
        composite.add(api.API_CLIENT.getSubDistrict(districtCode)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { mView()?.onShowDialog(true) }
                .doOnComplete { mView()?.onShowDialog(false) }
                .subscribe({ subDistrict ->
                    if (subDistrict.code() == 200) {
                        if (subDistrict.body().diagnostic.status == 200) {
                            mView()?.onGetSubDistrict(subDistrict.body().response)
                        } else {
                            mView()?.onShowMessage(subDistrict.body().diagnostic.description)
                        }
                    } else {
                        mView()?.onShowMessage(subDistrict.errorBody().string())
                    }
                }))
    }

    override fun getVillage(subDistrictCode: String) {
        composite.add(api.API_CLIENT.getVillage(subDistrictCode)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { mView()?.onShowDialog(true) }
                .doOnComplete { mView()?.onShowDialog(false) }
                .subscribe({ village ->
                    if (village.code() == 200) {
                        if (village.body().diagnostic.status == 200) {
                            mView()?.onGetVillage(village.body().response)
                        } else {
                            mView()?.onShowMessage(village.body().diagnostic.description)
                        }
                    } else {
                        mView()?.onShowMessage(village.errorBody().string())
                    }
                }))
    }

    override fun updateProfile(items: HashMap<String, String>) {
        composite.add(api.API_CLIENT.updateProfileInfo(items)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { mView()?.onShowDialog(true) }
                .doOnComplete { mView()?.onShowDialog(false) }
                .subscribe({ updateProfileInfo ->
                    if (updateProfileInfo.code() == 200) {
                        if (updateProfileInfo.body().diagnostic.status == 200) {
                            mView()?.onSuccess()
                        }
                        mView()?.onShowMessage(updateProfileInfo.body().diagnostic.description)
                    } else {
                        mView()?.onShowMessage(updateProfileInfo.errorBody().string())
                    }
                }))
    }
}