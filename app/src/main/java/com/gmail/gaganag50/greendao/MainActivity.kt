package com.gmail.gaganag50.greendao

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView.OnEditorActionListener
import com.gmail.gaganag50.greendao.db.Note
import com.gmail.gaganag50.greendao.db.Note_
import com.gmail.gaganag50.greendao.db.NotesAdapter
import io.objectbox.Box
import io.objectbox.query.Query
import kotlinx.android.synthetic.main.activity_main.*
import java.text.DateFormat
import java.util.*

class MainActivity : Activity() {

    val TAG: String = "MainAct"

    private var notesBox: Box<Note>? = null
    private var notesQuery: Query<Note>? = null
    private var notesAdapter: NotesAdapter? = null

    private var noteClickListener: OnItemClickListener = OnItemClickListener { _, _, position, _ ->
        val note = notesAdapter!!.getItem(position)

        notesBox!!.remove(note)
        Log.d(App.TAG, "Deleted note, ID: " + note.id)
        updateNotes()
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpViews()

        val boxStore = (application as App).boxStore
        notesBox = boxStore?.boxFor(Note::class.java)
        Log.d(TAG, ": notesBox ${notesBox.toString()}")


        // query all notes, sorted a-z by their text (https://docs.objectbox.io/queries)
        notesQuery = notesBox?.query()!!.order(Note_.text).build()
        Log.d(TAG, ": notesQuery ${notesQuery.toString()}")

        updateNotes()
    }

    /** Manual trigger to re-query and update the UI. For a reactive alternative check [ReactiveNoteActivity].  */
    private fun updateNotes() {
        Log.d(TAG, ": updateNotes called")


        val notes = notesQuery?.find()
        val notesAdapter1 = notesAdapter

        if (notesAdapter1 != null) notes?.let { notesAdapter1.setNotes(notes = it) }
    }

    protected fun setUpViews() {
        listViewNotes.onItemClickListener = noteClickListener

        notesAdapter = NotesAdapter()
        listViewNotes.adapter = notesAdapter

        buttonAdd!!.isEnabled = false

        // works when screen is rotated
        editTextNote?.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                Log.d(TAG, ": setOnEditorActionListener called")

                addNote()
                return@OnEditorActionListener true
            }
            false
        })
        // to change the highlighting color of the button
        editTextNote!!.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val enable = s.length != 0
                buttonAdd!!.isEnabled = enable
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable) {}
        })

    }

    fun onAddButtonClick(view: View) {
        addNote()
    }

    private fun addNote() {
        val noteText = editTextNote?.text.toString()
        editTextNote?.setText("")

        val df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM)
        val comment = "Added on ${df.format(Date())}"

        val note = Note()
        note.text = noteText
        note.comment = comment
        note.date = Date()
        notesBox?.put(note)
        Log.d(App.TAG, "Inserted new note, ID: " + note.id)

        updateNotes()
    }

}

//package com.gmail.gaganag50.greendao
//
//import android.os.Bundle
//import android.support.v7.app.AppCompatActivity
//import android.text.Editable
//import android.text.TextWatcher
//import android.util.Log
//import android.view.View
//import android.view.inputmethod.EditorInfo
//import android.widget.AdapterView
//import android.widget.TextView
//import com.gmail.gaganag50.greendao.db.Note
//import com.gmail.gaganag50.greendao.db.Note_
//import com.gmail.gaganag50.greendao.db.NotesAdapter
//import io.objectbox.Box
//import io.objectbox.query.Query
//import kotlinx.android.synthetic.main.activity_main.*
//import java.text.DateFormat
//import java.util.*
//
//class MainActivity : AppCompatActivity() {
//    private var notesQuery: Query<Note>? = null
//    private var notesAdapter: NotesAdapter? = null
//    private var notesBox: Box<Note>? = null
//
//    internal var noteClickListener: AdapterView.OnItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
//        val note = notesAdapter!!.getItem(position)
//        notesBox!!.remove(note)
//        Log.d(App.TAG, "Deleted note, ID: " + note.id)
//        updateNotes()
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        setUpViews()
//
//        // do this in your activities/fragments to get hold of a Box
//        notesBox = (application as App).boxStore?.boxFor<Note>(Note::class.java)
//
////        btnsave.setOnClickListener {
////            val noteText = etText.text.toString()
////            val note = Note(0, noteText)
////            notesBox?.put(note);
////            Log.d(App.TAG, "Inserted new note, ID: " + note.id + " " + note.text)
////
////            note.text = "This note has changed."
////            Log.d(App.TAG, "Inserted new note, ID: " + note.id + " " + note.text)
////            noteDao.put(note);
////        }
//
//        notesQuery = notesBox?.query()?.order(Note_.text)?.build()
//        updateNotes()
//
//    }
//
//    protected fun setUpViews() {
//        listViewNotes.setOnItemClickListener(noteClickListener)
//
//        notesAdapter = NotesAdapter()
//        listViewNotes.setAdapter(notesAdapter)
//
//        buttonAdd!!.isEnabled = false
//
//
//        editTextNote!!.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
//            if (actionId == EditorInfo.IME_ACTION_DONE) {
//                addNote()
//                return@OnEditorActionListener true
//            }
//            false
//        })
//        editTextNote!!.addTextChangedListener(object : TextWatcher {
//
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                val enable = s.isNotEmpty()
//                buttonAdd!!.isEnabled = enable
//            }
//
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
//
//            override fun afterTextChanged(s: Editable) {}
//        })
//
//    }
//
//    fun onAddButtonClick(view: View) {
//        addNote()
//    }
//
//    fun updateNotes() {
//        val notes = notesQuery?.find()
//        if (notes != null) {
//            notesAdapter?.setNotes(notes)
//        }
//    }
//
//    fun addNote() {
//        val noteText = editTextNote.getText().toString()
//        editTextNote.setText("")
//
//        val df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM)
//        val comment = "Added on " + df.format(Date())
//
//        val note = Note()
//        note.text = (noteText)
//        note.comment = comment
//        note.date = Date()
//        notesBox?.put(note)
//        Log.d(App.TAG, "Inserted new note, ID: " + note.id)
//
//        updateNotes()
//    }
//}
