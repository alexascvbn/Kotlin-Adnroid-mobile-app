package com.example.bottonavigation.sidebarFragment

import android.media.AudioManager
import android.media.SoundPool
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.text.format.Time
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bottonavigation.MainActivity
import com.example.bottonavigation.R
import com.example.bottonavigation.adapter.MessageListAdapter
import com.example.bottonavigation.model.ChatMsgModel
import com.example.bottonavigation.model.UserModel
import com.example.bottonavigation.util.Util
import com.google.firebase.auth.FirebaseAuth
import java.security.Key

private const val USERNAME = "username"

// A fragment that might not use
class ChatBotFragment : BaseSideFragment(), View.OnClickListener {
    // TODO: Rename and change types of parameters
    private var myName: String? = null
    override val titleId: Int = R.string.menu_chatbot

    private val rvMessageList by bindView<RecyclerView>(R.id.rvMessageList)
    private val btnSend by bindView<Button>(R.id.button_chatbox_send)
    private val etMsg by bindView<EditText>(R.id.edittext_chatbox)
    private lateinit var messageAdapter: MessageListAdapter
    private val messageList: ArrayList<ChatMsgModel> = ArrayList<ChatMsgModel>()
//    private val mSoundPlayer : SoundPool = SoundPool(1, AudioManager.STREAM_SYSTEM, 1)

    private lateinit var auth: FirebaseAuth

    companion object {
        /**
         * @param myName Parameter 1.
         * @return A new instance of fragment OrderFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(myName: String) =
            OrderFragment().apply {
                arguments = Bundle().apply {
                    putString(USERNAME, myName)
                }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            myName = it.getString(USERNAME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.side_fragment_chatbot, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        setAdapter()
        btnSend.setOnClickListener(this)
    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    private fun setAdapter() {
        messageList.add(ChatMsgModel("hello world", Util.getCurrentUser(), System.currentTimeMillis())) // August 12, 2014, 8:58PM
        messageList.add(ChatMsgModel("hello world", UserModel("Bot", "www@gmail.com", "should not have password, this use for making chatbot only"), 1407869895000L)) // August 12, 2014, 8:58PM
        messageList.add(ChatMsgModel("how are you", Util.getCurrentUser(), 1407869895000L)) // August 12, 2014, 8:58PM))
        messageList.add(ChatMsgModel("how are you", UserModel("Bot", "www@gmail.com", "should not have password, this use for making chatbot only"), 1407869895000L)) // August 12, 2014, 8:58PM
        messageAdapter = MessageListAdapter(context!!, messageList)

        rvMessageList.setHasFixedSize(true)
        rvMessageList.adapter = messageAdapter
        val layoutManager = LinearLayoutManager(context)
//        layoutManager.reverseLayout = true
        rvMessageList.layoutManager = layoutManager
        etMsg.setOnEditorActionListener(object : TextView.OnEditorActionListener{
            override fun onEditorAction(v: TextView?, actionId: Int, keyEvent: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                    keyEvent?.action == KeyEvent.ACTION_DOWN ||
                    keyEvent?.action == KeyEvent.KEYCODE_ENTER) {
                    btnSend.performClick()
                    return true
                }
                return false
            }
        })
        etMsg.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {texts ->
                    Log.d("johnson***textnum", s.length.toString())

                }
            }
        })
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            btnSend.id -> {
                if (etMsg.text.toString() != "") {
                    val text = etMsg.text.toString()
                    val msg = ChatMsgModel(text, Util.getCurrentUser(), System.currentTimeMillis())
                    messageList.add(msg)
                    messageAdapter.updateList(messageList)
                    etMsg.setText("")
                    rvMessageList.scrollToPosition(messageAdapter.itemCount - 1)
                    val hander = Handler()
                    hander.postDelayed({
                        val msg2 = ChatMsgModel(text, UserModel("Bot", "www@gmail.com", "no password anyway, not safety"), System.currentTimeMillis())
                        messageList.add(msg2)
                        rvMessageList.scrollToPosition(messageAdapter.itemCount - 1)
                    }, 1500)
                }
            }
        }
    }

    override fun onPause() {
        Util.hideKeyboard(activity as MainActivity)
        super.onPause()
    }

}
