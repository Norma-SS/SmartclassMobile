package com.projectbelajar.yuukbelajar.ui.smartmeet

import android.annotation.SuppressLint
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
import com.projectbelajar.yuukbelajar.data.network.NetworkConfig
import com.projectbelajar.yuukbelajar.data.network.model.request.smartmeet.student.RequestCheckIn
import com.projectbelajar.yuukbelajar.data.network.model.request.smartmeet.student.RequestCheckOut
import com.projectbelajar.yuukbelajar.databinding.ActivityStudentMeetBinding
import com.projectbelajar.yuukbelajar.ui.smartmeet.adapter.StudentMeetAdapter
import com.projectbelajar.yuukbelajar.ui.smartmeet.model.RoomInfo
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import org.jitsi.meet.sdk.BroadcastEvent
import org.jitsi.meet.sdk.JitsiMeet
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import timber.log.Timber
import java.net.MalformedURLException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class StudentMeetActivity : AppCompatActivity() {

    private var binding: ActivityStudentMeetBinding? = null
    private var codeRoom: String? = null
    private var refrence = FirebaseDatabase.getInstance().getReference("Room")
    private var preferences: Preferences? = null
    private var id: String? = null
    private var nis: String? = null
    private var nama: String? = null
    private var kodeSekolah: String? = null
    private var kelas: String? = null

    private var listRoom = ArrayList<RoomInfo>()

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

    private fun initVar() {

        id = preferences?.getValues("id")
        nis = preferences?.getValues("nis")
        nama = preferences?.getValues("nama")
        kodeSekolah = preferences?.getValues("kodesekolah")
        kelas = preferences?.getValues("kelas")

        refrence.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@StudentMeetActivity, "Something Error...", Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                listRoom.clear()
                for (data in snapshot.children) {
                    if (data.child("kelas").value.toString() == preferences?.getValues("kelas")
                            && data.child("kode_sekolah").value.toString() == preferences?.getValues("kodesekolah")
                    ) {
                        listRoom.add(data.getValue(RoomInfo::class.java)!!)
                    }
                }
                binding?.rvMeetRoom?.adapter = StudentMeetAdapter(this@StudentMeetActivity, listRoom,
                        object : StudentMeetAdapter.onItemClick {
                            override fun join(item: RoomInfo) {
                                codeRoom = item?.kode_ruangan
                                AlertDialog.Builder(this@StudentMeetActivity).apply {
                                    setTitle("Konfirmasi Join")
                                    setMessage("Apakah anda ingin bergabung dengan kelas ${item.nama_guru} ?")
                                            .setNegativeButton("Ya") { dialog, which ->
                                                startMeet(item?.kode_ruangan)
                                                dialog.dismiss()
                                            }
                                            .setPositiveButton("Tidak") { dialog, which ->
                                                dialog.dismiss()
                                            }
                                }.show()
                            }
                        })

            }
        })
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
                .setFeatureFlag("pip.enabled", false)
                .setFeatureFlag("kick-out.enabled", false)
                .setWelcomePageEnabled(false)
                .build()
        JitsiMeet.setDefaultConferenceOptions(defaultOptions)

        registerForBroadcastMessages()
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

                BroadcastEvent.Type.CONFERENCE_TERMINATED -> {
                    getTime("check_out")
                    Timber.i("Conference Joined with url%s", event.data.get("url"))
                }
                BroadcastEvent.Type.PARTICIPANT_JOINED -> {
                    getTime("check_in")
                    Timber.i("Participant joined%s", event.data.get("name"))
                }
            }
        }
    }


    override fun onDestroy() {

//        val current = LocalDateTime.now()
//        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
//        val formatted = current.format(formatter)
//        println("Current Date and Time is: $formatted")

        super.onDestroy()
        binding = null
        getTime("check_out")
    }

    @SuppressLint("SimpleDateFormat")
    private fun getTime(type: String) {

        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat("HH:mm:ss")
        val formatedDate = formatter.format(date)


        when (type) {
            "check_in" -> {
                NetworkConfig.service().insert_chec_in(RequestCheckIn(nis
                        ?: "", nama
                        ?: "", kodeSekolah ?: "", kelas ?: "", formatedDate ?: "", "-", codeRoom
                        ?: ""))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            Toast.makeText(applicationContext, "berhasil record data", Toast.LENGTH_SHORT).show()
                        }, {

                        })
            }
            "check_out" -> {
                NetworkConfig.service().insert_check_out(RequestCheckOut(id
                        ?: "", formatedDate ?: ""))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({

                        }, {

                        })
            }
        }

    }

}