package ps.salam.wakatobi.ui.addreport


import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.provider.Settings
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.orhanobut.logger.Logger
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.add_report_screen.*
import kotlinx.android.synthetic.main.custom_spinner.view.*
import okhttp3.MediaType
import okhttp3.RequestBody
import ps.salam.wakatobi.R
import ps.salam.wakatobi.base.BaseActivity
import ps.salam.wakatobi.model.DataCategory
import ps.salam.wakatobi.model.DataCategoryReport
import ps.salam.wakatobi.ui.DialogImageZoom
import ps.salam.wakatobi.utils.AppController.Companion.context
import ps.salam.wakatobi.utils.ProgressRequestBody
import ps.salam.wakatobi.utils.SharedKey
import ps.salam.wakatobi.utils.SharedPref
import ps.salam.wakatobi.widget.CustomToastView
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class VAddReport : BaseActivity(),
        IAddReport.View,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        ProgressRequestBody.UploadCallbacks {

    private val mPresenter = PAddReport()

    private lateinit var rxPermissions: RxPermissions

    private lateinit var dialog: ProgressDialog
    private lateinit var progress: ProgressDialog

    private lateinit var mGoogleApiClient: GoogleApiClient
    private var mLastLocation: Location? = null
    private var mMap: GoogleMap? = null
    private var mLatitude: Double = 0.0
    private var mLongitude: Double = 0.0

    private lateinit var alamat: String
    private lateinit var kelurahan: String
    private lateinit var kecamatan: String
    private lateinit var kabupaten: String

    private var imageFile: File? = null
    private lateinit var filePath: String

    private val CAMERA = 1
    private val GALLERY = 2

    private var reportCategory = ""

    override fun getLayoutResource(): Int {
        return R.layout.add_report_screen
    }

    override fun myCodeHere() {
        setupView()
        // button click listener here
        btn_reset_location.setOnClickListener {
            tet_location.setText(getString(R.string.search_location))
            mGoogleApiClient.reconnect()
        }

        btn_camera.setOnClickListener {
            openCamera()
        }
        btn_gallery.setOnClickListener {
            openGallery()
        }

        btn_send_report.setOnClickListener {
            if (tet_title_report.text.isEmpty() || tet_value_report.text.isEmpty() || imageFile == null || tet_location.text.isEmpty() || reportCategory == "") {
                CustomToastView.makeText(this, getString(R.string.error_empty), Toast.LENGTH_SHORT)
            } else {
                dialog = ProgressDialog(this)
                dialog.setMessage(getString(R.string.send_report))
                dialog.show()

                val items: HashMap<String, RequestBody> = HashMap()
                items.put("action", toRequestBody("send-report"))
                items.put("user", toRequestBody(SharedPref.getString(SharedKey.id_user)))
                items.put("kategori", toRequestBody(reportCategory))
                items.put("judul", toRequestBody(tet_title_report.text.toString()))
                items.put("pesan", toRequestBody(tet_value_report.text.toString()))
                items.put("latitude", toRequestBody(mLatitude.toString()))
                items.put("longitude", toRequestBody(mLongitude.toString()))
                mPresenter.sendReport(items, imageFile!!, this)

                Logger.d(SharedPref.getString(SharedKey.id_user) + ": " + reportCategory)
            }
        }
    }

    private fun toRequestBody(value: String): RequestBody {
        val body = RequestBody.create(MediaType.parse("text/plain"), value)
        return body
    }

    override fun setupView() {
        title = getString(R.string.create_report)

        //setup dialog
        dialog = ProgressDialog(this)
        dialog.setMessage(getString(R.string.please_wait))
        dialog.setCancelable(false)
        dialog.show()

        //setup progress bar
        progress = ProgressDialog(this)
        progress.setMessage(getString(R.string.send_report))
        progress.setCancelable(false)
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        progress.isIndeterminate = false

        //setup rxPermission
        rxPermissions = RxPermissions(this)

        if (intent.extras.getBoolean("open_camera"))
            openCamera()

        //connect to map
        buildGoogleApiClient()
        mGoogleApiClient.connect()

        mPresenter.attachView(this)
        mPresenter.getCategory()

    }

    override fun onProgressUpdate(percentage: Int) {
        progress.progress = percentage
    }

    override fun onError() {
        progress.dismiss()
    }

    override fun onFinish() {
        progress.progress = 100
    }

    override fun onShowDialog(isShow: Boolean) {
        if (isShow)
            dialog.show()
        else dialog.dismiss()
    }

    override fun onShowProgress(isShow: Boolean) {
        if (isShow)
            progress.show()
        else progress.dismiss()
    }

    override fun onGetCategory(response: MutableList<DataCategoryReport.Response>) {
        response.add(0, DataCategoryReport.Response("", getString(R.string.choose_category), ""))
        val adapter = ArrayAdapter<DataCategoryReport.Response>(this, android.R.layout.simple_spinner_item, response)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        cs_report_category.sp_content.adapter = adapter
        cs_report_category.sp_content.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 0) {
                    reportCategory = ""
                } else {
                    reportCategory = response[position].id!!
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
    }

    override fun onSuccess(type: Int, message: String) {
        if (type == 1) {
            finish()
            CustomToastView.makeText(this, message, Toast.LENGTH_LONG) // 5 detik
            Handler().postDelayed({
                CustomToastView.toast.cancel()
            }, 6000)
        }
    }

    override fun onError(errorMessage: String) {
        CustomToastView.makeText(this, errorMessage, Toast.LENGTH_SHORT)
    }

    private fun openCamera() {
        rxPermissions
                .request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .doOnError { e -> CustomToastView.makeText(this, e.toString(), Toast.LENGTH_SHORT) }
                .subscribe({ permission ->
                    if (permission) {
                        try {
                            imageFile = createImageFile()
                        } catch (ex: IOException) {
                            ex.printStackTrace()
                        }

                        if (imageFile != null) {
                            val photoURI: Uri = FileProvider.getUriForFile(this, "ps.salam.wakatobi.provider", imageFile)
                            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            val resolvedIntentActivities: List<ResolveInfo> = context?.packageManager!!.queryIntentActivities(cameraIntent, PackageManager.MATCH_DEFAULT_ONLY)
                            for (item in resolvedIntentActivities) {
                                val packageName = item.activityInfo.packageName
                                context?.grantUriPermission(packageName, photoURI, Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                            if (cameraIntent.resolveActivity(packageManager) != null)
                                startActivityForResult(cameraIntent, CAMERA)
                        }
                    } else {
                        val builder = AlertDialog.Builder(this)
                        builder.setMessage(getString(R.string.camera_not_allowed))
                        builder.setPositiveButton(getString(R.string.retry)) { dialog, _ -> dialog.dismiss() }
                        builder.setCancelable(false)
                        builder.show()
                    }
                })
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        filePath = image.absolutePath
        return image
    }

    private fun setPic(imageView: ImageView, imageFile: File) {
        Glide.with(this)
                .load(imageFile)
                .asBitmap()
                .centerCrop()
                .into(imageView)

        imageView.visibility = View.VISIBLE

        iv_report.setOnClickListener {
            val args = Bundle()
            args.putString("image_url", imageFile.absolutePath)
            args.putString("image_title", tet_title_report.text.toString())
            val dialogZoom = DialogImageZoom()
            dialogZoom.arguments = args
            dialogZoom.show(supportFragmentManager, "dialog image")
        }
    }

    private fun openGallery() {
        rxPermissions
                .request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe({ permission ->
                    if (permission) {
                        Logger.d("lacak _camera gallery")
                        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                        galleryIntent.type = "image/*"
                        startActivityForResult(galleryIntent, GALLERY)
                    } else {
                        val builder = AlertDialog.Builder(this)
                        builder.setMessage(getString(R.string.camera_not_allowed))
                        builder.setPositiveButton(getString(R.string.retry)) { dialog, _ -> dialog.dismiss() }
                        builder.setCancelable(false)
                        builder.show()

                    }
                })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA && resultCode == Activity.RESULT_OK) {
            imageFile = File(filePath)
            if (imageFile?.exists()!!) {
                Logger.d("image exist")
                Logger.d("size before compress" + imageFile?.length()!! / 1024)
                //reduce size if file image more than 2048
//                if (imageFile.length() / 1024 > 2048) {
                try {
                    val bitmap = BitmapFactory.decodeFile(imageFile?.absolutePath)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, FileOutputStream(imageFile))
                    val options = BitmapFactory.Options()
                    options.inJustDecodeBounds = true
                    options.inSampleSize = 5
                    BitmapFactory.decodeFile(imageFile?.absolutePath!!, options)

                    setPic(iv_report, imageFile!!)
                    Logger.d("size after compress" + imageFile?.length()!! / 1024)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
//                }
            }


        } else if (requestCode == GALLERY && resultCode == Activity.RESULT_OK) {
            try {
                val selectedImage = data?.data
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val cursorImage = this.contentResolver.query(selectedImage,
                        filePathColumn, null, null, null)
                if (cursorImage != null) {
                    cursorImage.moveToFirst()
                    val columnIndex = cursorImage.getColumnIndex(filePathColumn[0])
                    val picturePath = cursorImage.getString(columnIndex)
                    imageFile = File(picturePath)
                    cursorImage.close()
                }

                Logger.d("size before compress" + imageFile?.length()!! / 1024)

                //reduce size if file image more than 2048
//                if (imageFile.length() / 1024 > 2048) {
                val bitmap = BitmapFactory.decodeFile(imageFile?.absolutePath!!)
                try {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, FileOutputStream(imageFile))
                    val options = BitmapFactory.Options()
                    options.inJustDecodeBounds = true
                    options.inSampleSize = 5
                    BitmapFactory.decodeFile(imageFile?.absolutePath!!, options)
                    setPic(iv_report, imageFile!!)
                    Logger.d("size after compress" + imageFile?.length()!! / 1024)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
//                }
//                val bmp = BitmapFactory.decodeFile(imageFile.absolutePath)
//                iv_report.visibility = View.VISIBLE
//                iv_report.setImageBitmap(bmp)

            } catch (e: Exception) {        //for xiaomi device
                val uri = data?.data
                imageFile = File(uri?.path)
                val selectedImage = getImageContentUri(this, imageFile!!)
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

                val cursorImage = this.contentResolver.query(selectedImage,
                        filePathColumn, null, null, null)
                if (cursorImage != null) {
                    cursorImage.moveToFirst()
                    val columnIndex = cursorImage.getColumnIndex(filePathColumn[0])
                    val picturePath = cursorImage.getString(columnIndex)
                    imageFile = File(picturePath)
                    cursorImage.close()
                }

                Logger.d("size before compress" + imageFile?.length()!! / 1024)

                //reduce size if file image more than 2048
//                if (imageFile.length() / 1024 > 2048) {
                val bitmap = BitmapFactory.decodeFile(imageFile?.absolutePath!!)
                try {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, FileOutputStream(imageFile))
                    val options = BitmapFactory.Options()
                    options.inJustDecodeBounds = true
                    options.inSampleSize = 5
                    BitmapFactory.decodeFile(imageFile?.absolutePath!!, options)
                    setPic(iv_report, imageFile!!)
                    Logger.d("size after compress" + imageFile!!.length() / 1024)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
//                }
            }
            filePath = imageFile?.absolutePath!!
            imageFile = File(imageFile?.absolutePath!!)
        }
    }

    private fun getImageContentUri(context: Context, imageFile: File): Uri? {
        val filePath = imageFile.absolutePath
        val cursor = context.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                arrayOf(MediaStore.Images.Media._ID),
                MediaStore.Images.Media.DATA + "=? ",
                arrayOf(filePath), null)

        if (cursor != null && cursor.moveToFirst()) {
            val id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID))
            val baseUri = Uri.parse("content://media/external/images/media")
            cursor.close()
            return Uri.withAppendedPath(baseUri, "" + id)
        } else {
            if (imageFile.exists()) {
                val values = ContentValues()
                values.put(MediaStore.Images.Media.DATA, filePath)
                return context.contentResolver.insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            } else {
                return null
            }
        }
    }


    @Synchronized private fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
    }


    override fun onConnected(p0: Bundle?) {
        if (dialog.isShowing)
            dialog.dismiss()
        rxPermissions
                .requestEach(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe({ permission ->
                    if (permission.granted) {
                        try {
                            mMap?.isMyLocationEnabled = true
                            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)
                            updateLocation()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    } else {
                        if (permission.shouldShowRequestPermissionRationale) {
                            val builder = AlertDialog.Builder(this)
                            builder.setMessage(getString(R.string.access_location_not_allowed))
                            builder.setPositiveButton(getString(R.string.retry)) { dialog, _ -> dialog.dismiss() }
                            builder.setCancelable(false)
                            builder.show()
                        } else {
                            promptSettings()
                        }
                    }
                })
    }

    private fun updateLocation() {
        if (mLastLocation != null) {
            mLatitude = mLastLocation?.latitude!!
            mLongitude = mLastLocation?.longitude!!
            Logger.d(mLatitude.toString() + " : " + mLongitude.toString())

            val gcd = Geocoder(this, Locale("id"))
            val list: List<Address> = gcd.getFromLocation(mLatitude, mLongitude, 1)

            alamat = list[0].getAddressLine(0)
            kelurahan = list[0].subLocality
            kecamatan = list[0].locality
            kabupaten = list[0].subAdminArea

            if (dialog.isShowing)
                dialog.dismiss()
            tet_location.setText(String.format(getString(R.string.format_location_report), alamat, kelurahan, kecamatan))
        } else {
            val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                gpsPermission()
            }
            CustomToastView.makeText(this, getString(R.string.cant_find_location), Toast.LENGTH_SHORT)

        }
    }

    override fun onLocationChanged(location: Location) {
        mLastLocation = location
        updateLocation()
    }


    private fun promptSettings() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.access_location_not_allowed)
        builder.setPositiveButton(getString(R.string.settings)) { dialog, _ ->
            dialog.dismiss()
            goToSettings()
        }
        builder.setCancelable(false)
        builder.show()
    }

    private fun gpsPermission() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.gps_disable)
        builder.setMessage(R.string.ask_enable_gps)
        builder.setPositiveButton(getString(R.string.settings)) { dialog, _ ->
            val intent = Intent(
                    Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
            dialog.dismiss()
        }
        builder.setNegativeButton(getString(R.string.cancel), null)
        builder.setCancelable(false)
        builder.show()
    }

    private fun goToSettings() {
        val myAppSettings = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + this.packageName))
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT)
        myAppSettings.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        this.startActivity(myAppSettings)
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Logger.d(p0.errorMessage)
    }

    override fun onDetachScreen() {
        mGoogleApiClient.disconnect()
    }


}
