package ps.salam.wakatobi.ui.mainscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.design.widget.AppBarLayout

import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.NavigationView
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.bumptech.glide.Glide
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.main_screen.*
import kotlinx.android.synthetic.main.main_screen_layer_app_bar.*
import kotlinx.android.synthetic.main.main_screen_layer_nav_header.*
import kotlinx.android.synthetic.main.main_screen_layer_toolbar.*


import ps.salam.wakatobi.R
import ps.salam.wakatobi.model.DataProfile
import ps.salam.wakatobi.model.FABItem
import ps.salam.wakatobi.model.DataSlider
import ps.salam.wakatobi.support.fab.RapidFloatingActionHelper
import ps.salam.wakatobi.support.imageslider.Animations.DescriptionAnimation
import ps.salam.wakatobi.support.imageslider.SliderLayout
import ps.salam.wakatobi.support.imageslider.SliderTypes.TextSliderView
import ps.salam.wakatobi.ui.OnBoardingScreen
import ps.salam.wakatobi.ui.addreport.VAddReport
import ps.salam.wakatobi.ui.detailreport.VDetailReport
import ps.salam.wakatobi.ui.fragment.report.VReport
import ps.salam.wakatobi.ui.fragment.info.VInfo
import ps.salam.wakatobi.ui.fragment.category.VCategory
import ps.salam.wakatobi.ui.fragment.statistik.VStatistic
import ps.salam.wakatobi.ui.infoapp.InfoApp
import ps.salam.wakatobi.ui.login.VLogin
import ps.salam.wakatobi.ui.navstatistic.VNavStatistic
import ps.salam.wakatobi.ui.profile.VProfile
import ps.salam.wakatobi.ui.reportstatus.VReportStatus
import ps.salam.wakatobi.ui.searchreport.VSearchReport
import ps.salam.wakatobi.ui.settings.VSettings
import ps.salam.wakatobi.utils.RapidFloatingActionContentCardListView
import ps.salam.wakatobi.utils.ScreenSizeGetter
import ps.salam.wakatobi.utils.SharedKey
import ps.salam.wakatobi.utils.SharedPref
import kotlin.collections.ArrayList

class VMainScreen : AppCompatActivity(),
        NavigationView.OnNavigationItemSelectedListener,
        RapidFloatingActionContentCardListView.OnRapidFloatingActionContentCardListViewListener,
        IMainScreen.View {

    private var isCollapse = false

    private var rfaContent: RapidFloatingActionContentCardListView? = null
    private val mPresenter = PMainScreen()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_screen)

        // Check for first time use
        if (!SharedPref.getBol(SharedKey.on_boarding)) {
            finish()
            startActivity(Intent(applicationContext, OnBoardingScreen::class.java))
        } else {
            if (!SharedPref.getBol(SharedKey.is_login)) {
                finish()
                startActivity(Intent(applicationContext, VLogin::class.java))
                Logger.d("belum login")
            } else
                setupView()
        }

        //setup nav drawer menu
        if (SharedPref.getInt(SharedKey.type_user) == 0) {
            Logger.d("visitor")
            nav_view.menu.clear()
            nav_view.inflateMenu(R.menu.menu_navigation_visitor)
        } else if (SharedPref.getInt(SharedKey.type_user) == 1) {
            Logger.d("resident")
            nav_view.menu.clear()
            nav_view.inflateMenu(R.menu.menu_navigation)
        }
    }


    override fun setupView() {
        mPresenter.attachView(this)
        Logger.d("id_user " + SharedPref.getString(SharedKey.id_user))

        if (toolbar != null) {
            setSupportActionBar(toolbar)
            supportActionBar?.title = null
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }

        val toggle = object : ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            override fun onDrawerOpened(drawerView: View?) {
                onTransparent(true)
                super.onDrawerOpened(drawerView)
            }

            override fun onDrawerClosed(drawerView: View?) {
                onTransparent(false)
                super.onDrawerClosed(drawerView)
            }
        }

        iv_nav_drawer.setOnClickListener {
            if (drawer_layout.isDrawerOpen(GravityCompat.END))
                drawer_layout.closeDrawer(GravityCompat.END)
            else
                drawer_layout.openDrawer(GravityCompat.END)
        }

        @Suppress("DEPRECATION")
        drawer_layout.setDrawerListener(toggle)
        toggle.syncState()
        toggle.isDrawerIndicatorEnabled = false

        nav_view.setNavigationItemSelectedListener(this)

        //add tab
        setupTab()

        //check if appbar layout is collapse
        app_bar_layout.addOnOffsetChangedListener { _, i ->
            isCollapse = i == 0
        }

        //setup fab
        rfaContent = RapidFloatingActionContentCardListView(applicationContext)
        rfaContent!!.setOnRapidFloatingActionContentCardListViewListener(this)

        val fabItem: ArrayList<FABItem> = ArrayList()
        fabItem.add(FABItem(getString(R.string.take_picture), R.drawable.ic_take_report))
        fabItem.add(FABItem(getString(R.string.create_report), R.drawable.ic_create_report))
        rfaContent!!.setList(fabItem)

        rfal.setIsContentAboveLayout(true)
        rfal.setDisableContentDefaultAnimation(true)


        RapidFloatingActionHelper(applicationContext, rfal, rfab_add_post, rfaContent).build()

        //if accept report from notif
        try {
            if (intent.extras.getBoolean("notif")) {
                val detailReport = Intent(applicationContext, VDetailReport::class.java)
                detailReport.putExtra("id_report", intent.extras.getString("id_report"))
//                detailReport.putExtra("notif", true)
                startActivity(detailReport)
            }
        } catch (ignore: Exception) {
        }
    }

    override fun onRFACCardListItemClick(position: Int) {
        val intent = Intent(applicationContext, VAddReport::class.java)
        when (position) {
            0 -> {
                intent.putExtra("open_camera", true)
                startActivity(intent)
            }
            1 -> {
                intent.putExtra("open_camera", false)
                startActivity(intent)
            }
        }
        rfal.collapseContent()
    }

    override fun onGetSlider(response: List<DataSlider.Response>) {
        sl_slider.removeAllSliders()
        for (i in response.indices) {
            val tsv = TextSliderView(this)
            tsv.titleDescription(response[i].judul)
                    .contentDescription(response[i].isi)
                    .image(response[i].thumbnail)
                    .setOnSliderClickListener {
                        val detailReport = Intent(applicationContext, VDetailReport::class.java)
                        detailReport.putExtra("id_report", response[i].id)
                        startActivity(detailReport)
                    }

            sl_slider.addSlider(tsv)
            sl_slider.isEnabled = false
        }
        sl_slider.setPresetTransformer(SliderLayout.Transformer.Default)
        sl_slider.setCustomAnimation(DescriptionAnimation())
        sl_slider.setDuration(4000)
        sl_slider.layoutParams.height = (ScreenSizeGetter.getHeight(windowManager) * 0.20).toInt()
    }

    private fun onTransparent(isTransparent: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            if (isTransparent) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = ContextCompat.getColor(this@VMainScreen, R.color.transparent)
            } else {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = ContextCompat.getColor(this@VMainScreen, R.color.colorPrimary)
            }
        }
    }

    private fun setupTab() {

        //add to pager
        val adapter: ViewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(VReport(), getString(R.string.home))
        adapter.addFragment(VCategory(), getString(R.string.category))
        adapter.addFragment(VInfo(), getString(R.string.info))
        adapter.addFragment(VStatistic(), getString(R.string.statistic))
        vp_container.adapter = adapter
        tabs.setupWithViewPager(vp_container)

        //setup tab item
        @SuppressLint("InflateParams")
        val tabHome = LayoutInflater.from(this).inflate(R.layout.custom_tab_layout, null) as TextView
        tabHome.text = getString(R.string.home)
        tabHome.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_home, 0, 0)
        tabs.getTabAt(0)?.customView = tabHome

        @SuppressLint("InflateParams")
        val tabCategory = LayoutInflater.from(this).inflate(R.layout.custom_tab_layout, null) as TextView
        tabCategory.text = getString(R.string.category)
        tabCategory.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_category, 0, 0)
        tabs.getTabAt(1)?.customView = tabCategory

        @SuppressLint("InflateParams")
        val tabInfo = LayoutInflater.from(this).inflate(R.layout.custom_tab_layout, null) as TextView
        tabInfo.text = getString(R.string.info)
        tabInfo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_info, 0, 0)
        tabs.getTabAt(2)?.customView = tabInfo

        @SuppressLint("InflateParams")
        val tabStatistic = LayoutInflater.from(this).inflate(R.layout.custom_tab_layout, null) as TextView
        tabStatistic.text = getString(R.string.statistic)
        tabStatistic.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_statistic, 0, 0)
        tabs.getTabAt(3)?.customView = tabStatistic


        //get current position
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 0) {
                    Logger.d(tab.position)
                    if (!isCollapse)
                        app_bar_layout.setExpanded(false, false)
                    else app_bar_layout.setExpanded(true, true)

                    collapse_toolbar_layout.isClickable = true
                    setAppBarDragging(true)

                } else {
                    Logger.d(tab?.position.toString() + "test")

                    collapse_toolbar_layout.isClickable = false
                    app_bar_layout.setExpanded(false, false)

                    setAppBarDragging(false)
                }
            }

        })

    }

    private fun setAppBarDragging(isDragging: Boolean) {
        if (isDragging) {
//            val params: AppBarLayout.LayoutParams = collapse_toolbar_layout.layoutParams as AppBarLayout.LayoutParams
//            params.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
//            params.height = sl_slider.height + toolbar.height
            sl_slider.visibility = View.VISIBLE
//            collapse_toolbar_layout.layoutParams = params
        } else {
//            val params: AppBarLayout.LayoutParams = collapse_toolbar_layout.layoutParams as AppBarLayout.LayoutParams
//            params.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS or AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
//            params.height = toolbar.height + (toolbar.height / 4) - resources.getDimension(R.dimen.dimen2).toInt()
//            params.height = toolbar.height
            sl_slider.visibility = View.GONE
//            collapse_toolbar_layout.layoutParams = params
        }
    }

    inner class ViewPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {
        val fragmentList: ArrayList<Fragment> = ArrayList()
        val titleList: ArrayList<String> = ArrayList()

        fun addFragment(fragment: Fragment, title: String) {
            fragmentList.add(fragment)
            titleList.add(title)
        }

        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }

        override fun getCount(): Int {
            return titleList.size
        }

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.END)) {
            drawer_layout.closeDrawer(GravityCompat.END)
        } else {
            super.onBackPressed()
        }
    }

    override fun onPause() {
        super.onPause()
        sl_slider.stopAutoCycle()
    }

    override fun onResume() {
        super.onResume()
        sl_slider.startAutoCycle()
        mPresenter.getProfile(SharedPref.getString(SharedKey.id_user))
        mPresenter.getSlider()
        Logger.d("onResume")
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

    override fun onSuccess() {
    }

    override fun onError(errorMessage: String) {
    }

    override fun onGetProfile(response: DataProfile.Response) {
        Logger.d("onResume : " + response.foto)
        val typeUser: String
        tv_nav_username.text = response.nama
        if (response.tipe_user == 0) {
            typeUser = getString(R.string.visitor)
            rl_point.visibility = View.GONE
        } else {
            typeUser = getString(R.string.resident)
            rl_point.visibility = View.VISIBLE
        }
        tv_nav_user_status.text = typeUser
        Glide.with(this)
                .load(response.foto)
                .crossFade()
                .error(R.drawable.ic_default_profile)
                .into(iv_nav_profile)
        SharedPref.saveInt(SharedKey.status_notif, response.status_notif!!)
        tv_point.text = response.poin
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_search -> startActivity(Intent(applicationContext, VSearchReport::class.java))
            R.id.nav_report -> startActivity(Intent(applicationContext, VReportStatus::class.java))
            R.id.nav_profile -> startActivity(Intent(applicationContext, VProfile::class.java))
            R.id.nav_statistic -> startActivity(Intent(applicationContext, VNavStatistic::class.java))
            R.id.nav_settings -> startActivity(Intent(applicationContext, VSettings::class.java))
            R.id.nav_about -> startActivity(Intent(applicationContext, InfoApp::class.java))
        }
        drawer_layout.closeDrawer(GravityCompat.END)
        return true
    }
}
