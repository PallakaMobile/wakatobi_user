package ps.salam.wakatobi.services

import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import com.orhanobut.logger.Logger
import ps.salam.wakatobi.utils.SharedKey
import ps.salam.wakatobi.utils.SharedPref


/**
 * ----------------------------------------------
 * Created by ukieTux on 12/31/16 with ♥ .

 * @email : ukie.tux@gmail.com
 * *
 * @github : https://www.github.com/tuxkids
 * * ----------------------------------------------
 * * © 2016 | All Rights Reserved
 */

class MyFirebaseIDService : FirebaseInstanceIdService() {

    override fun onTokenRefresh() {
        super.onTokenRefresh()
        val refreshToken = FirebaseInstanceId.getInstance().token
        Logger.d("firebaseID : " + refreshToken)
        if (refreshToken != null)
            SharedPref.saveString(SharedKey.firebase_ID, refreshToken)
    }
}
