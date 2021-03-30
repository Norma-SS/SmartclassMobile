package com.projectbelajar.yuukbelajar.smartmeet

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.database.FirebaseDatabase
import com.projectbelajar.yuukbelajar.Preferences
import com.projectbelajar.yuukbelajar.databinding.ActivityMeetBinding
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

    private var codeRoom : String ?= null


    init{
        preferences = Preferences(this@MeetActivity)

        nipTeacher = preferences?.getValues("nip").toString()
        codeSchool = preferences?.getValues("kodesekolah").toString()
        codeRoom = randomInt.toString() + codeSchool + nipTeacher
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



        initView()
        initConf()
        attahBtn()

    }

    private fun initView() {
        binding?.etMeetRoom?.setText(codeRoom)

        binding?.spinnerMeetKelas?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                abjadKelas = adapterView?.getItemAtPosition(position).toString()
            }

        }
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
                .setFeatureFlag("pip.enabled",false)
                .setWelcomePageEnabled(false)
                .build()
        JitsiMeet.setDefaultConferenceOptions(defaultOptions)

        registerForBroadcastMessages()
    }

    private fun attahBtn() {
        binding?.btnMeetJoin?.setOnClickListener {
            if (binding?.etMeetRoom?.text.isNullOrEmpty() || binding?.etMeetRoom?.text?.trim() ==""){
                binding?.etMeetRoom?.error = "Silahkan Masukan Code Room"
            }else{
                val options = JitsiMeetConferenceOptions.Builder()
                        .setRoom(binding?.etMeetRoom?.text.toString())
                        // Settings for audio and video
                        //.setAudioMuted(true)
                        //.setVideoMuted(true)
                        .build()
                // Launch the new activity with the given options. The launch() method takes care
                // of creating the required Intent and passing the options.
                JitsiMeetActivity.launch(this, options)
            }
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

                    hashMap ["id"] = key ?: ""
                    hashMap["kode_ruangan"] = codeRoom.toString()
                    hashMap["kode_sekolah"] = preferences?.getValues("kodesekolah").toString()
                    hashMap["kelas"] = binding?.etMeetKelas?.text.toString() + abjadKelas
                    hashMap["nama_guru"] = preferences?.getValues("nama").toString()
                    hashMap["photo_guru"] = preferences?.getValues("foto").toString()

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
        JitsiMeetActivityDelegate.onHostDestroy(this@MeetActivity)
        super.onDestroy()
    }


}