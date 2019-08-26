package ps.salam.wakatobi.utils

import android.support.v7.widget.SearchView
import com.orhanobut.logger.Logger

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Created by ukieTux on 10/13/16 with ♥ .
 * @email : ukie.tux@gmail.com
 * @github : https://www.github.com/tuxkids
 * © 2016 | All Rights Reserved
 */

object RxHelperSearchView {
    fun getTextWatcherObservable(searchView: SearchView): Observable<String> {
        val subject = PublishSubject.create<String>()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                subject.onNext(newText)
                Logger.d(newText)
                return false
            }
        })
        return subject
    }
}
