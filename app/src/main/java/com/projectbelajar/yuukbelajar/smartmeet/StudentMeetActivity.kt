package com.projectbelajar.yuukbelajar.smartmeet

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.projectbelajar.yuukbelajar.Preferences
import com.projectbelajar.yuukbelajar.databinding.ActivityStudentMeetBinding
import com.projectbelajar.yuukbelajar.smartmeet.adapter.StudentMeetAdapter
import com.projectbelajar.yuukbelajar.smartmeet.model.RoomInfo
import org.jitsi.meet.sdk.BroadcastEvent
import org.jitsi.meet.sdk.JitsiMeet
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import timber.log.Timber
import java.net.MalformedURLException
import java.net.URL

class StudentMeetActivity : AppCompatActivity() {

    private var binding : ActivityStudentMeetBinding ?= null
    private var refrence = FirebaseDatabase.getInstance().getReference("Room")
    private var preferences : Preferences?= null
    private var nis : String ?= null
    private var nama : String ?= null
    private var check_in : String ?= null
    private var check_out : String ?= null


    private var listRoom =  ArrayList<RoomInfo>()

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            onBroadcastReceived(intent)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentMeetBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        preferences = Preferences(this@StudentMeetActivity)

        initVar()
        initConf()
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

    private fun initVar() {
        nis = preferences?.getValues("nis")
        nama = preferences?.getValues("nama")
        refrence.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@StudentMeetActivity, "Something Error...", Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                listRoom.clear()
                for (data in snapshot.children){
                    if (data.child("kelas").value.toString() == preferences?.getValues("kelas")
                            && data.child("kode_sekolah").value.toString() == preferences?.getValues("kodesekolah")
                    ){
                        listRoom.add(data.getValue(RoomInfo::class.java)!!)
                    }
                }

                binding?.rvMeetRoom?.adapter = StudentMeetAdapter(this@StudentMeetActivity, listRoom,
                        object : StudentMeetAdapter.onItemClick {
                            override fun join(item: RoomInfo) {
                                AlertDialog.Builder(this@StudentMeetActivity).apply {
                                    setTitle("Konfirmasi Join")
                                    setMessage("Apakah anda ingin bergabung dengan kelas ${item.nama_guru} ?")
                                            .setNegativeButton("Ya"){dialog, which ->
                                                startMeet(item?.kode_ruangan)
                                                dialog.dismiss()
                                            }
                                            .setPositiveButton("Tidak"){dialog, which ->
                                                dialog.dismiss()
                                            }
                                }.show()
                            }
                })

            }
        })
    }

    private fun startMeet(kodeRuangan: String?) {
        val options = JitsiMeetConferenceOptions.Builder()
                .setRoom(kodeRuangan)
                // Settings for audio and video
                //.setAudioMuted(true)
                //.setVideoMuted(true)
                .build()
        // Launch the new activity with the given options. The launch() method takes care
        // of creating the required Intent and passing the options.
        JitsiMeetActivity.launch(this, options)
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

    private fun onBroadcastReceived(intent: Intent?) {
        if (intent != null) {
            val event = BroadcastEvent(intent)
            when (event.type) {

                BroadcastEvent.Type.CONFERENCE_TERMINATED ->Timber.i("Conference Joined with url%s", event.data.get("url"))
                BroadcastEvent.Type.PARTICIPANT_JOINED -> Timber.i("Participant joined%s", event.data.get("name"))
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}