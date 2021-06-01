package com.projectbelajar.yuukbelajar.ui.smartmeet

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.database.FirebaseDatabase
import com.projectbelajar.yuukbelajar.Preferences
import com.projectbelajar.yuukbelajar.data.network.NetworkConfig
import com.projectbelajar.yuukbelajar.databinding.ActivityMeetBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_meet.*
import org.jitsi.meet.sdk.*
import timber.log.Timber
import java.net.MalformedURLException
import java.net.URL
import kotlin.random.Random

class MeetActivity : AppCompatActivity() {

    private var binding : ActivityMeetBinding ?= null
    private var refrence = FirebaseDatabase.getInstance().getReference("Room")
    private var key = refrence.push().key
    private var preferences : Preferences?= null
    private var abjadKelas : String ?= null
    val randomInt : Int ?= Random.nextInt(1 , 9999999)
    private var codeSchool : String ?= null
    private var nipTeacher : String ?= null
    private var listDataKelas : MutableList<String> = ArrayList()

    private var codeRoom : String ?=null
    private var timeStamp : String ?= "" + System.currentTimeMillis()



    init{
        preferences = Preferences(this@MeetActivity)
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            onBroadcastReceived(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMeetBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        initVar()
        initView()
        initConf()
        attahBtn()

    }

    private fun initVar() {
        nipTeacher = preferences?.getValues("nip").toString()
        codeRoom = timeStamp + nipTeacher
    }

    private fun initView() {

        NetworkConfig.service().get_list_kelas(preferences?.getValues("kodesekolah").toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                   for (i in 0 until it?.result?.datanya?.size!!){
                       listDataKelas.add(it.result.datanya.get(i)?.kelas ?: "")
                   }

                },{

                })

        val arrayAdapter = ArrayAdapter(this@MeetActivity,
                android.R.layout.simple_list_item_1, listDataKelas)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        et_Meet_kelas?.setAdapter(arrayAdapter)

//        binding?.spinnerMeetKelas?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//
//            }
//
//            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                abjadKelas = adapterView?.getItemAtPosition(position).toString()
//            }
//
//        }
    }

    private fun initConf() {
        // Initialize default options for Jitsi Meet conferences.
        val serverURL: URL
        serverURL = try {
            // When using JaaS, replace "https://meet.jit.si" with the proper serverURL
            URL("https://meet.jit.si")
        } catch (e: MalformedURLException) {
            e.printStackTrace()
            throw RuntimeException("Invalid server URL!")
        }
        val defaultOptions = JitsiMeetConferenceOptions.Builder()
                .setServerURL(serverURL)
                // When using JaaS, set the obtained JWT here
                //.setToken("MyJWT")
                // Different features flags can be set
                //.setFeatureFlag("toolbox.enabled", false)
//                .setFeatureFlag("filmstrip.enabled", false)
                .setFeatureFlag("call-integration.enabled", false)
                .setFeatureFlag("pip.enabled",false)
                .setWelcomePageEnabled(false)
                .build()
        JitsiMeet.setDefaultConferenceOptions(defaultOptions)

        registerForBroadcastMessages()
    }

    private fun attahBtn() {
        binding?.btnMeetJoin?.setOnClickListener {
                val options = JitsiMeetConferenceOptions.Builder()
                        .setRoom(codeRoom)
                        // Settings for audio and video
                        //.setAudioMuted(true)
                        //.setVideoMuted(true)
                        .build()
                // Launch the new activity with the given options. The launch() method takes care
                // of creating the required Intent and passing the options.
                JitsiMeetActivity.launch(this, options)
        }
    }

    private fun onBroadcastReceived(intent: Intent?) {
        if (intent != null) {
            val event = BroadcastEvent(intent)
            when (event.type) {

                BroadcastEvent.Type.CONFERENCE_TERMINATED -> {
                    refrence?.child(key ?: "")?.removeValue()
                }

                BroadcastEvent.Type.CONFERENCE_JOINED -> {
                    val hashMap = HashMap<String, Any>()

                    hashMap["kode_ruangan"] = codeRoom.toString()
                    hashMap["kode_sekolah"] = preferences?.getValues("kodesekolah").toString()
                    hashMap["kelas"] = binding?.etMeetKelas?.text.toString()
                    hashMap["nama_guru"] = preferences?.getValues("nama").toString()
                    hashMap["foto_guru"] = preferences?.getValues("foto").toString()

                    refrence?.child(key ?: "")?.setValue(hashMap)
                    Timber.i("Conference Joined with url%s", event.data.get("url"))
                }
                BroadcastEvent.Type.PARTICIPANT_JOINED -> Timber.i("Participant joined%s", event.data.get("name"))
            }
        }
    }

    private fun registerForBroadcastMessages() {
        val intentFilter = IntentFilter()

        /* This registers for every possible event sent from JitsiMeetSDK
           If only some of the events are needed, the for loop can be replaced
           with individual statements:
           ex:  intentFilter.addAction(BroadcastEvent.Type.AUDIO_MUTED_CHANGED.action);
                intentFilter.addAction(BroadcastEvent.Type.CONFERENCE_TERMINATED.action);
                ... other events
         */
        for (type in BroadcastEvent.Type.values()) {
            intentFilter.addAction(type.action)
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter)
    }



    private fun hangUp() {
        val hangupBroadcastIntent: Intent = BroadcastIntentHelper.buildHangUpIntent()
        LocalBroadcastManager.getInstance(org.webrtc.ContextUtils.getApplicationContext()).sendBroadcast(hangupBroadcastIntent)
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
        binding = null
        refrence?.child(key ?: "")?.removeValue()
        super.onDestroy()
    }


}