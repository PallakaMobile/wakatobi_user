package ps.salam.wakatobi.ui.addreport

import com.orhanobut.logger.Logger
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import ps.salam.wakatobi.base.BasePresenter
import ps.salam.wakatobi.network.api
import ps.salam.wakatobi.utils.ProgressRequestBody
import java.io.File

/**
 *----------------------------------------------
 * Created by ukieTux on 3/5/17 with ♥ .
 * @email  : ukie.tux@gmail.com
 * @github : https://www.github.com/tuxkids
 * ----------------------------------------------
 *          © 2017 | All Rights Reserved
 */
class PAddReport : BasePresenter<IAddReport.View>(), IAddReport.Presenter {


    private val composite = CompositeDisposable()
    override fun attachView(mvpView: IAddReport.View) {
        super.attachView(mvpView)
    }

    override fun detachView() {
        super.detachView()
        composite.clear()
    }

    override fun sendReport(items: HashMap<String, RequestBody>, imageFile: File, uploadCallbacks: ProgressRequestBody.UploadCallbacks) {
        val progressRequestBody: ProgressRequestBody = ProgressRequestBody(imageFile, uploadCallbacks)

        val body = MultipartBody.Part.createFormData("gambar", imageFile.name, progressRequestBody)
        composite.add(api.API_CLIENT.sendReport(items, body)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { mView()?.onShowProgress(true) }
                .doOnComplete { mView()?.onShowProgress(false) }
                .subscribe({ sendReport ->
                    if (sendReport.code() == 200) {
                        if (sendReport.body().diagnostic.status == 200) {
                            imageFile.deleteOnExit()
                            mView()?.onSuccess(1, sendReport.body().diagnostic.description)

                        } else {
                            mView()?.onError(sendReport.body().diagnostic.description)
                            Logger.d(sendReport.body().diagnostic.description)
                        }
                    } else {
                        mView()?.onError(sendReport.errorBody().string())
                        Logger.d(sendReport.errorBody().string())
                    }
                }, { error -> Logger.d(error.toString()) }))
    }

    override fun getCategory() {
        composite.add(api.API_CLIENT.getCategoryReport()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { mView()?.onShowDialog(true) }
                .doOnComplete { mView()?.onShowDialog(false) }
                .subscribe({ category ->
                    if (category.code() == 200) {
                        if (category.body().diagnostic.status == 200) {
                            mView()?.onGetCategory(category.body().response)
                            mView()?.onSuccess(0, category.body().diagnostic.description)
                        } else {
                            mView()?.onError(category.body().diagnostic.description)
                        }
                    } else {
                        mView()?.onError(category.errorBody().string())
                    }
                }))
    }
}