package ps.salam.wakatobi.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.*
import android.widget.ImageView
import kotlinx.android.synthetic.main.on_boarding_screen.*
import ps.salam.wakatobi.R
import ps.salam.wakatobi.ui.mainscreen.VMainScreen
import ps.salam.wakatobi.utils.SharedKey
import ps.salam.wakatobi.utils.SharedPref

class OnBoardingScreen : AppCompatActivity() {

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private var indicators: Array<ImageView>? = null
    private var page = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.on_boarding_screen)

        btn_finish.setOnClickListener {
            SharedPref.saveBol(SharedKey.on_boarding, true)
            startActivity(Intent(this@OnBoardingScreen, VMainScreen::class.java))
            finish()
        }

        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        indicators = arrayOf<ImageView>(iv_indicator_0, iv_indicator_1, iv_indicator_2, iv_indicator_3)

        vp_container.adapter = mSectionsPagerAdapter
        vp_container.currentItem = page
        updateIndicator(page)

        vp_container.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                page = position
                updateIndicator(page)
                btn_finish.visibility = (if (page == mSectionsPagerAdapter!!.count - 1) View.VISIBLE else View.GONE)
                ll_indicator_layout.visibility = if (page == mSectionsPagerAdapter!!.count - 1) View.GONE else View.VISIBLE
            }

        })
    }

    fun updateIndicator(position: Int) {
        for (i in indicators?.indices!!) {
            indicators?.get(i)?.setBackgroundResource(if (i == position) R.drawable.indicator_selected else R.drawable.indicator_unselected)
        }
    }


    class PlaceholderFragment : Fragment() {
        private var img: ImageView? = null

        internal var bgs = intArrayOf(R.drawable.onboarding_content1, R.drawable.onboarding_content2,
                R.drawable.onboarding_content3, R.drawable.onboarding_content4)

        override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val rootView = inflater!!.inflate(R.layout.onboarding_item_pager, container, false)

            img = rootView.findViewById(R.id.iv_item_onboarding) as ImageView
            img!!.setImageResource(bgs[arguments.getInt(ARG_SECTION_NUMBER) - 1])
            return rootView
        }

        companion object {
            private val ARG_SECTION_NUMBER = "section_number"

            fun newInstance(sectionNumber: Int): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }
    }

    private inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // OrderDetailReturn a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1)
        }

        override fun getCount(): Int {
            // Show 4 total pages.
            return 4
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        SharedPref.saveBol(SharedKey.on_boarding, true)
        startActivity(Intent(applicationContext, VMainScreen::class.java))
    }
}
