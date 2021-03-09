package com.projectbelajar.yuukbelajar.fragment

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.projectbelajar.yuukbelajar.*
import com.projectbelajar.yuukbelajar.adapter.ViewPagerAdapter
import com.projectbelajar.yuukbelajar.chat.activity.ChatActivity
import com.projectbelajar.yuukbelajar.maps.MapsActivity
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import kotlinx.android.synthetic.main.fragment_course.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class CourseFragment : Fragment(), View.OnClickListener {
    private var editTextName: TextView? = null
    private var editTextSkl: TextView? = null

    //private EditText eml;
    private var eml: String? = null
    private var nmx: String? = null
    private var jumlah: Int? = null
    private var jumlah2: Int? = null
    private var jumlah3: Int? = null
    private var jumlah4: Int? = null
    private var jumlah5: Int? = null
    private var jumlah6: Int? = null
    private var Image1: ImageView? = null
    private var Image2: ImageView? = null
    private var Image3: ImageView? = null
    private var Image4: ImageView? = null
    private var Image6: ImageView? = null
    private var Image7: ImageView? = null
    private var Image8: ImageView? = null
    private var Image9: ImageView? = null
    private var Image10: ImageView? = null
    private var Image11: ImageView? = null
    private var Image12: ImageView? = null

    //===================== info inbox
    private var ly_notif: LinearLayout? = null
    private var ly_notif2: LinearLayout? = null
    private var ly_notif3: LinearLayout? = null
    private var ly_notif4: LinearLayout? = null
    private var ly_notif6: LinearLayout? = null
    private var tv_notif: TextView? = null
    private var tv_notif2: TextView? = null
    private var tv_notif3: TextView? = null
    private var tv_notif4: TextView? = null
    private var tv_notif6: TextView? = null
    private var adapter: ViewPagerAdapter? = null
    private var carouselBanners = ArrayList<String>()
    private var preferences: Preferences? = null
    var sessionManager: SessionManager? = null
    private var currentPos = 0
    private var numpages = 0
    private var firebaseUser: FirebaseUser? = null

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CourseFragment.
     */
    // TODO: Rename and change types and number of parameters
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_course, container, false)


        //sessionManager.logout();
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferences = Preferences(activity)

        when(preferences?.getValues("level")){
            "ORANG TUA", "WALI MURID"-> btn_Course_track.visibility = VISIBLE
            else -> btn_Course_track.visibility = INVISIBLE

        }

        btn_Course_track.setOnClickListener{
            startActivity(Intent(context, MapsActivity::class.java))
        }
        carouselBanners = ArrayList()
        carouselBanners.add("https://www.yuukbelajar.com/gbr/iklan.jpg")
        carouselBanners.add("https://www.yuukbelajar.com/gbr/image1.jpg")
        carouselBanners.add("https://www.yuukbelajar.com/gbr/image2.jpg")
        carouselBanners.add("https://yuukbelajar.com/gbr/image3.png")
        val Iklan = view.findViewById<ImageView>(R.id.Iklan)
        Glide.with(activity).load("https://www.yuukbelajar.com/gbr/iklane1.jpg")
                .fitCenter() //menyesuaikan ukuran imageview
                .crossFade() //animasi
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(Iklan)
        val Iklan2 = view.findViewById<ImageView>(R.id.Iklan2)
        Glide.with(activity).load("https://www.yuukbelajar.com/gbr/iklane2.jpg")
                .fitCenter() //menyesuaikan ukuran imageview
                .crossFade() //animasi
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(Iklan2)
        val Iklan3 = view.findViewById<ImageView>(R.id.Iklan3)
        Glide.with(activity).load("https://image.freepik.com/free-vector/back-chool-template-wooden-board_1308-33554.jpg")
                .fitCenter() //menyesuaikan ukuran imageview
                .crossFade() //animasi
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(Iklan3)
        viewPager = view.findViewById(R.id.sliderpager)
        val dotsIndicator: DotsIndicator = view.findViewById(R.id.dots_indicator)
        adapter = ViewPagerAdapter(carouselBanners, activity)
        adapter?.notifyDataSetChanged()
        viewPager?.adapter = adapter
        dotsIndicator.setViewPager(viewPager!!)
        //        sliderView = view.findViewById(R.id.sliderCourse);
//        mLinearLayout = view.findViewById(R.id.pagesContainer);
        editTextName = view.findViewById(R.id.namasiswa)
        editTextSkl = view.findViewById(R.id.namasekolah)
        Image1 = view.findViewById(R.id.image1)
        Image1?.setOnClickListener(this)
        Image2 = view.findViewById(R.id.image2)
        Image2?.setOnClickListener(this)
        Image3 = view.findViewById(R.id.image3)
        Image3?.setOnClickListener(this)
        Image4 = view.findViewById(R.id.image4)
        Image4?.setOnClickListener(this)
        Image6 = view.findViewById(R.id.image6)
        Image6?.setOnClickListener(this)
        Image7 = view.findViewById(R.id.image7)
        Image7?.setOnClickListener(this)
        Image8 = view.findViewById(R.id.image8)
        Image8?.setOnClickListener(this)
        Image9 = view.findViewById(R.id.image9)
        Image9?.setOnClickListener(this)
        Image10 = view.findViewById(R.id.image10)
        Image10?.setOnClickListener(this)
        Image11 = view.findViewById(R.id.image11)
        Image11?.setOnClickListener(this)
        Image12 = view.findViewById(R.id.image12)
        Image12?.setOnClickListener(this)
        editTextName?.text = "Halo, " + preferences!!.getValues("nama")
        editTextSkl?.text = "Selamat Datang di " + preferences!!.getValues("namaSekolah")
        sessionManager = SessionManager(context)
        val prefs = this.activity!!.getSharedPreferences(pref_name, Context.MODE_PRIVATE)
        eml = prefs.getString(SessionManager.password, null)

        class GetEmployee : AsyncTask<Void?, Void?, String?>() {

            override fun onPostExecute(s: String?) {
                super.onPostExecute(s)
                try {
                    val jsonObject = JSONObject(s ?: "")
                    val result = jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY)
                    val c = result.getJSONObject(0)
                    //String id = c.getString(konfigurasi.TAG_ID);
                    val name = c.getString(konfigurasi.TAG_NAMA)
                    val desg = c.getString(konfigurasi.TAG_POSISI)
                    val tingkat = c.getString(konfigurasi.TAG_TINGKAT)
                    val sal = c.getString(konfigurasi.TAG_GAJIH)
                    val uts = c.getString(konfigurasi.TAG_UTS)
                    val uas = c.getString(konfigurasi.TAG_UAS)
                    val quiz = c.getString(konfigurasi.TAG_QUIZ)
                    val tugas = c.getString(konfigurasi.TAG_TUGAS)
                    val jmlchat = c.getString(konfigurasi.TAG_JMLCHAT)
                    val pesan = c.getString(konfigurasi.TAG_PESAN)

                    //Toast.makeText(getApplicationContext(), "hallooo.."+pesan, Toast.LENGTH_LONG).show();
                    nmx = tingkat
                    jumlah = sal.toInt()
                    jumlah2 = uts.toInt()
                    jumlah3 = uas.toInt()
                    jumlah4 = quiz.toInt()
                    jumlah5 = tugas.toInt()
                    jumlah6 = jmlchat.toInt()
                    //        tmpilkan jumlah kedalam notifikasi
                    ly_notif = view.findViewById(R.id.ly_notif)
                    tv_notif = view.findViewById(R.id.tv_notif)
                    if (jumlah!! > 0) {
                        ly_notif?.visibility = View.VISIBLE
                        tv_notif?.text = "" + jumlah
                    } else {
                        ly_notif?.visibility = View.GONE
                        tv_notif?.text = "" + jumlah
                    }
                    ly_notif2 = view.findViewById(R.id.ly_notif2)
                    tv_notif2 = view.findViewById(R.id.tv_notif2)
                    if (jumlah2!! > 0) {
                        ly_notif2?.visibility = View.VISIBLE
                        tv_notif2?.text = "" + jumlah2
                    } else {
                        ly_notif2?.visibility = View.GONE
                        tv_notif2?.text = "" + jumlah2
                    }
                    ly_notif3 = view.findViewById(R.id.ly_notif3)
                    tv_notif3 = view.findViewById(R.id.tv_notif3)
                    if (jumlah3!! > 0) {
                        ly_notif3?.visibility = View.VISIBLE
                        tv_notif3?.text = "" + jumlah3
                    } else {
                        ly_notif3?.visibility = View.GONE
                        tv_notif3?.text = "" + jumlah3
                    }
                    ly_notif4 = view.findViewById(R.id.ly_notif4)
                    tv_notif4 = view.findViewById(R.id.tv_notif4)
                    if (jumlah4!! > 0) {
                        ly_notif4?.visibility = View.VISIBLE
                        tv_notif4?.text = "" + jumlah4
                    } else {
                        ly_notif4?.visibility = View.GONE
                        tv_notif4?.text = "" + jumlah4
                    }
                    ly_notif6 = view.findViewById(R.id.ly_notif6)
                    tv_notif6 = view.findViewById(R.id.tv_notif6)
                    if (jumlah6!! > 0) {
                        ly_notif6?.visibility = View.VISIBLE
                        tv_notif6?.text = "" + jumlah6
                    } else {
                        ly_notif6?.visibility = View.GONE
                        tv_notif6?.text = "" + jumlah6
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun doInBackground(vararg params: Void?): String? {
                val rh = RequestHandler()
                return rh.sendGetRequestParam(konfigurasi.URL_GET_EMP, eml)
            }
        }

        val ge = GetEmployee()
        ge.execute()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        autoRunCarousel()
        //        setupSlider();
    }

    //
    //    private void setupSlider() {
    //        sliderView.setDurationScroll(1000);
    //        List<Fragment> fragments = new ArrayList<>();
    //        fragments.add(FragmentSlider.newInstance("https://www.yuukbelajar.com/gbr/iklan.jpg"));
    //        fragments.add(FragmentSlider.newInstance("https://www.yuukbelajar.com/gbr/image1.jpg"));
    //        fragments.add(FragmentSlider.newInstance("https://www.yuukbelajar.com/gbr/image2.jpg"));
    //        fragments.add(FragmentSlider.newInstance("https://yuukbelajar.com/gbr/image3.png"));
    //
    //        mAdapter = new SliderPagerAdapter(getFragmentManager(), fragments);
    //        sliderView.setAdapter(mAdapter);
    //        mIndicator = new SliderIndicator(getActivity(), mLinearLayout, sliderView, R.drawable.indicator_circle);
    //        mIndicator.setPageCount(fragments.size());
    //        mIndicator.show();
    //
    //    }
    override fun onClick(view: View) {
        when (view.id) {
            R.id.image1 -> {
                Image1!!.setOnClickListener {
                    val i = Intent(activity, InfoSekolah::class.java)
                    startActivity(i)
                }
                Image2!!.setOnClickListener {
                    val i = Intent(activity, InfoUts::class.java)
                    startActivity(i)
                }
                Image3!!.setOnClickListener {
                    val i = Intent(activity, InfoUas::class.java)
                    startActivity(i)
                }
                Image4!!.setOnClickListener {
                    val i = Intent(activity, Harian::class.java)
                    startActivity(i)
                }
                Image6!!.setOnClickListener {
                    val i = Intent(activity, WaliKls::class.java)
                    startActivity(i)
                }
                Image7!!.setOnClickListener {
                    firebaseUser = FirebaseAuth.getInstance().currentUser
                    Log.d("firebaseUser ", " $firebaseUser")
                    if (firebaseUser == null) {
                        Toast.makeText(activity, "Kamu belum login!", Toast.LENGTH_LONG).show()
                    } else {
//                            Toast.makeText(getActivity(), "Kamu sudah login!", Toast.LENGTH_LONG).show();
                        if (preferences!!.getValues("level") == "GURU") {
                            Toast.makeText(activity, "Anda bukan walikelas!", Toast.LENGTH_LONG).show()
                        } else {
                            preferences!!.setValues("chatType", "walikelas")
                            val i = Intent(activity, ChatActivity::class.java)
                            startActivity(i)
                        }
                        //                            Toast.makeText(getActivity(), "chatType " + preferences.getValues("chatType"), Toast.LENGTH_SHORT).show();

//                            getActivity().finish();
                    }
                }
                Image8!!.setOnClickListener {
                    val i = Intent(activity, QuizOnline::class.java)
                    startActivity(i)
                }
                Image9!!.setOnClickListener { //Toast.makeText(getApplicationContext(), "hallooo.."+nmx, Toast.LENGTH_LONG).show();
                    if (nmx == "SD") {
                        val i = Intent(activity, MenuElearningSd::class.java)
                        startActivity(i)
                    } else {
                        val i = Intent(activity, MenuElearning::class.java)
                        startActivity(i)
                    }
                }
                Image10!!.setOnClickListener { Toast.makeText(activity, "Sekolah Belum Registrasi", Toast.LENGTH_SHORT).show() }
                Image11!!.setOnClickListener { Toast.makeText(activity, "Sekolah Belum Registrasi", Toast.LENGTH_SHORT).show() }
                Image12!!.setOnClickListener { Toast.makeText(activity, "Sekolah Belum Registrasi", Toast.LENGTH_SHORT).show() }
            }
            R.id.image2 -> {
                Image2!!.setOnClickListener {
                    val i = Intent(activity, InfoUts::class.java)
                    startActivity(i)
                }
                Image3!!.setOnClickListener {
                    val i = Intent(activity, InfoUas::class.java)
                    startActivity(i)
                }
                Image4!!.setOnClickListener {
                    val i = Intent(activity, Harian::class.java)
                    startActivity(i)
                }
                Image6!!.setOnClickListener {
                    val i = Intent(activity, WaliKls::class.java)
                    startActivity(i)
                }
                Image7!!.setOnClickListener {
                    firebaseUser = FirebaseAuth.getInstance().currentUser
                    Log.d("firebaseUser ", " $firebaseUser")
                    if (firebaseUser == null) {
                        Toast.makeText(activity, "Kamu belum login!", Toast.LENGTH_LONG).show()
                    } else {
                        if (preferences!!.getValues("level") == "GURU") {
                            Toast.makeText(activity, "Anda bukan walikelas!", Toast.LENGTH_LONG).show()
                        } else {
                            preferences!!.setValues("chatType", "walikelas")
                            val i = Intent(activity, ChatActivity::class.java)
                            startActivity(i)
                        }
                    }
                }
                Image8!!.setOnClickListener {
                    val i = Intent(activity, QuizOnline::class.java)
                    startActivity(i)
                }
                Image9!!.setOnClickListener {
                    if (nmx == "SD") {
                        val i = Intent(activity, MenuElearningSd::class.java)
                        startActivity(i)
                    } else {
                        val i = Intent(activity, MenuElearning::class.java)
                        startActivity(i)
                    }
                }
                Image10!!.setOnClickListener { Toast.makeText(activity, "Sekolah Belum Registrasi", Toast.LENGTH_SHORT).show() }
                Image11!!.setOnClickListener { Toast.makeText(activity, "Sekolah Belum Registrasi", Toast.LENGTH_SHORT).show() }
                Image12!!.setOnClickListener { Toast.makeText(activity, "Sekolah Belum Registrasi", Toast.LENGTH_SHORT).show() }
            }
            R.id.image3 -> {
                Image3!!.setOnClickListener {
                    val i = Intent(activity, InfoUas::class.java)
                    startActivity(i)
                }
                Image4!!.setOnClickListener {
                    val i = Intent(activity, Harian::class.java)
                    startActivity(i)
                }
                Image6!!.setOnClickListener {
                    val i = Intent(activity, WaliKls::class.java)
                    startActivity(i)
                }
                Image7!!.setOnClickListener {
                    firebaseUser = FirebaseAuth.getInstance().currentUser
                    Log.d("firebaseUser ", " $firebaseUser")
                    if (firebaseUser == null) {
                        Toast.makeText(activity, "Kamu belum login!", Toast.LENGTH_LONG).show()
                    } else {
                        if (preferences!!.getValues("level") == "GURU") {
                            Toast.makeText(activity, "Anda bukan walikelas!", Toast.LENGTH_LONG).show()
                        } else {
                            preferences!!.setValues("chatType", "walikelas")
                            val i = Intent(activity, ChatActivity::class.java)
                            startActivity(i)
                        }
                    }
                }
                Image8!!.setOnClickListener {
                    val i = Intent(activity, QuizOnline::class.java)
                    startActivity(i)
                }
                Image9!!.setOnClickListener {
                    if (nmx == "SD") {
                        val i = Intent(activity, MenuElearningSd::class.java)
                        startActivity(i)
                    } else {
                        val i = Intent(activity, MenuElearning::class.java)
                        startActivity(i)
                    }
                }
                Image10!!.setOnClickListener { Toast.makeText(activity, "Sekolah Belum Registrasi", Toast.LENGTH_SHORT).show() }
                Image11!!.setOnClickListener { Toast.makeText(activity, "Sekolah Belum Registrasi", Toast.LENGTH_SHORT).show() }
                Image12!!.setOnClickListener { Toast.makeText(activity, "Sekolah Belum Registrasi", Toast.LENGTH_SHORT).show() }
            }
            R.id.image4 -> {
                Image4!!.setOnClickListener {
                    val i = Intent(activity, Harian::class.java)
                    startActivity(i)
                }
                Image6!!.setOnClickListener {
                    val i = Intent(activity, WaliKls::class.java)
                    startActivity(i)
                }
                Image7!!.setOnClickListener {
                    firebaseUser = FirebaseAuth.getInstance().currentUser
                    Log.d("firebaseUser ", " $firebaseUser")
                    if (firebaseUser == null) {
                        Toast.makeText(activity, "Kamu belum login!", Toast.LENGTH_LONG).show()
                    } else {
                        if (preferences!!.getValues("level") == "GURU") {
                            Toast.makeText(activity, "Anda bukan walikelas!", Toast.LENGTH_LONG).show()
                        } else {
                            preferences!!.setValues("chatType", "walikelas")
                            val i = Intent(activity, ChatActivity::class.java)
                            startActivity(i)
                        }
                    }
                }
                Image8!!.setOnClickListener {
                    val i = Intent(activity, QuizOnline::class.java)
                    startActivity(i)
                }
                Image9!!.setOnClickListener {
                    if (nmx == "SD") {
                        val i = Intent(activity, MenuElearningSd::class.java)
                        startActivity(i)
                    } else {
                        val i = Intent(activity, MenuElearning::class.java)
                        startActivity(i)
                    }
                }
                Image10!!.setOnClickListener { Toast.makeText(activity, "Sekolah Belum Registrasi", Toast.LENGTH_SHORT).show() }
                Image11!!.setOnClickListener { Toast.makeText(activity, "Sekolah Belum Registrasi", Toast.LENGTH_SHORT).show() }
                Image12!!.setOnClickListener { Toast.makeText(activity, "Sekolah Belum Registrasi", Toast.LENGTH_SHORT).show() }
            }
            R.id.image6 -> {
                Image6!!.setOnClickListener {
                    val i = Intent(activity, WaliKls::class.java)
                    startActivity(i)
                }
                Image7!!.setOnClickListener {
                    firebaseUser = FirebaseAuth.getInstance().currentUser
                    Log.d("firebaseUser ", " $firebaseUser")
                    if (firebaseUser == null) {
                        Toast.makeText(activity, "Kamu belum login!", Toast.LENGTH_LONG).show()
                    } else {
                        if (preferences!!.getValues("level") == "GURU") {
                            Toast.makeText(activity, "Anda bukan walikelas!", Toast.LENGTH_LONG).show()
                        } else {
                            preferences!!.setValues("chatType", "walikelas")
                            val i = Intent(activity, ChatActivity::class.java)
                            startActivity(i)
                        }
                    }
                }
                Image8!!.setOnClickListener {
                    val i = Intent(activity, QuizOnline::class.java)
                    startActivity(i)
                }
                Image9!!.setOnClickListener {
                    if (nmx == "SD") {
                        val i = Intent(activity, MenuElearningSd::class.java)
                        startActivity(i)
                    } else {
                        val i = Intent(activity, MenuElearning::class.java)
                        startActivity(i)
                    }
                }
                Image10!!.setOnClickListener { Toast.makeText(activity, "Sekolah Belum Registrasi", Toast.LENGTH_SHORT).show() }
                Image11!!.setOnClickListener { Toast.makeText(activity, "Sekolah Belum Registrasi", Toast.LENGTH_SHORT).show() }
                Image12!!.setOnClickListener { Toast.makeText(activity, "Sekolah Belum Registrasi", Toast.LENGTH_SHORT).show() }
            }
            R.id.image7 -> {
                Image7!!.setOnClickListener {
                    firebaseUser = FirebaseAuth.getInstance().currentUser
                    Log.d("firebaseUser ", " $firebaseUser")
                    if (firebaseUser == null) {
                        Toast.makeText(activity, "Kamu belum login!", Toast.LENGTH_LONG).show()
                    } else {
                        if (preferences!!.getValues("level") == "GURU") {
                            Toast.makeText(activity, "Anda bukan walikelas!", Toast.LENGTH_LONG).show()
                        } else {
                            preferences!!.setValues("chatType", "walikelas")
                            val i = Intent(activity, ChatActivity::class.java)
                            startActivity(i)
                        }
                    }
                }
                Image8!!.setOnClickListener {
                    val i = Intent(activity, QuizOnline::class.java)
                    startActivity(i)
                }
                Image9!!.setOnClickListener {
                    if (nmx == "SD") {
                        val i = Intent(activity, MenuElearningSd::class.java)
                        startActivity(i)
                    } else {
                        val i = Intent(activity, MenuElearning::class.java)
                        startActivity(i)
                    }
                }
                Image10!!.setOnClickListener { Toast.makeText(activity, "Sekolah Belum Registrasi", Toast.LENGTH_SHORT).show() }
                Image11!!.setOnClickListener { Toast.makeText(activity, "Sekolah Belum Registrasi", Toast.LENGTH_SHORT).show() }
                Image12!!.setOnClickListener { Toast.makeText(activity, "Sekolah Belum Registrasi", Toast.LENGTH_SHORT).show() }
            }
            R.id.image8 -> {
                Image8!!.setOnClickListener {
                    val i = Intent(activity, QuizOnline::class.java)
                    startActivity(i)
                }
                Image9!!.setOnClickListener {
                    if (nmx == "SD") {
                        val i = Intent(activity, MenuElearningSd::class.java)
                        startActivity(i)
                    } else {
                        val i = Intent(activity, MenuElearning::class.java)
                        startActivity(i)
                    }
                }
                Image10!!.setOnClickListener { Toast.makeText(activity, "Sekolah Belum Registrasi", Toast.LENGTH_SHORT).show() }
                Image11!!.setOnClickListener { Toast.makeText(activity, "Sekolah Belum Registrasi", Toast.LENGTH_SHORT).show() }
                Image12!!.setOnClickListener { Toast.makeText(activity, "Sekolah Belum Registrasi", Toast.LENGTH_SHORT).show() }
            }
            R.id.image9 -> {
                Image9!!.setOnClickListener {
                    if (nmx == "SD") {
                        val i = Intent(activity, MenuElearningSd::class.java)
                        startActivity(i)
                    } else {
                        val i = Intent(activity, MenuElearning::class.java)
                        startActivity(i)
                    }
                }
                Image10!!.setOnClickListener { Toast.makeText(activity, "Sekolah Belum Registrasi", Toast.LENGTH_SHORT).show() }
                Image11!!.setOnClickListener { Toast.makeText(activity, "Sekolah Belum Registrasi", Toast.LENGTH_SHORT).show() }
                Image12!!.setOnClickListener { Toast.makeText(activity, "Sekolah Belum Registrasi", Toast.LENGTH_SHORT).show() }
            }
            R.id.image10 -> {
                Image10!!.setOnClickListener { Toast.makeText(activity, "Sekolah Belum Registrasi", Toast.LENGTH_SHORT).show() }
                Image11!!.setOnClickListener { Toast.makeText(activity, "Sekolah Belum Registrasi", Toast.LENGTH_SHORT).show() }
                Image12!!.setOnClickListener { Toast.makeText(activity, "Sekolah Belum Registrasi", Toast.LENGTH_SHORT).show() }
            }
            R.id.image11 -> {
                Image11!!.setOnClickListener { Toast.makeText(activity, "Sekolah Belum Registrasi", Toast.LENGTH_SHORT).show() }
                Image12!!.setOnClickListener { Toast.makeText(activity, "Sekolah Belum Registrasi", Toast.LENGTH_SHORT).show() }
            }
            R.id.image12 -> Image12!!.setOnClickListener { Toast.makeText(activity, "Sekolah Belum Registrasi", Toast.LENGTH_SHORT).show() }
        }
    }

    private fun autoRunCarousel() {
        NUM_PAGES = carouselBanners.size
        numpages = carouselBanners.size
        val handler = Handler()
        val Update = Runnable {
            if (currentPage == NUM_PAGES) {
                currentPage = 0
            }
            if (currentPos == numpages) {
                currentPos = 0
            }
            viewPager!!.setCurrentItem(currentPos++, true)
        }
        val timer = Timer() // This will create a new Thread
        //delay in milliseconds before task is to be executed
        val DELAY_MS: Long = 500
        // time in milliseconds between successive task executions
        val PERIOD_MS: Long = 5000
        timer.schedule(object : TimerTask() {
            // task to be scheduled
            override fun run() {
                handler.post(Update)
            }
        }, DELAY_MS, PERIOD_MS)
    }

    companion object {
        private var viewPager: ViewPager? = null
        private const val pref_name = "crudpref"
        private var currentPage = 0
        private var NUM_PAGES = 0
    }
}