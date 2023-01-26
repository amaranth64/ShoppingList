package ru.worklight64.shoppinglist.activities

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.style.StyleSpan
import android.text.style.TypefaceSpan
import android.view.Menu
import android.view.MenuItem
import androidx.core.text.getSpans
import ru.worklight64.shoppinglist.R
import ru.worklight64.shoppinglist.databinding.ActivityNewNoteBinding
import ru.worklight64.shoppinglist.entities.NoteItem
import ru.worklight64.shoppinglist.fragments.NoteFragment
import java.text.SimpleDateFormat
import java.util.*

class NewNoteActivity : AppCompatActivity() {
    private lateinit var form: ActivityNewNoteBinding
    private var note: NoteItem? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        form = ActivityNewNoteBinding.inflate(layoutInflater)
        setContentView(form.root)
        actionBarSettings()
        getNoteItem()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.new_note_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.id_save){
            setMainResult()
        } else if (item.itemId == android.R.id.home){
            finish()
        } else if (item.itemId == R.id.id_bold){
            setBoldForSelectedText()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setBoldForSelectedText() = with(form) {
        val startPos = edDescription.selectionStart
        val endPos = edDescription.selectionEnd

        val styles = edDescription.text.getSpans(startPos, endPos, StyleSpan::class.java)
        var boldStyle:StyleSpan? = null
        if (styles.isNotEmpty()){
            edDescription.text.removeSpan(styles[0])
        }else{
            boldStyle = StyleSpan(Typeface.BOLD)
            edDescription.text.setSpan(boldStyle, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        edDescription.text.trim()
        edDescription.setSelection(startPos)
    }

    private fun getNoteItem() = with(form){
        val sNote = intent.getSerializableExtra(NoteFragment.NEW_NOTE_KEY)
        if (sNote != null){
            note = sNote as NoteItem
        }
        if (note != null) {
            edTitle.setText(note?.title)
            edDescription.setText(note?.content)
        }
    }

    private fun setMainResult(){
        var editSate = "new"
        var tempNote: NoteItem?
        if (note == null) {
            tempNote = createNote()
        } else {
            tempNote = updateNote()
            editSate = "update"
        }

        val i = Intent().apply {
            putExtra(NoteFragment.EDIT_STATE_KEY, editSate)
            putExtra(NoteFragment.NEW_NOTE_KEY, tempNote)

        }
        setResult(RESULT_OK, i)
        finish()
    }

    private fun updateNote(): NoteItem? {
        return note?.copy(
            title = form.edTitle.text.toString(),
            content = form.edDescription.text.toString()
        )
    }

    private fun createNote(): NoteItem{
        return NoteItem(
            null,
            form.edTitle.text.toString(),
            form.edDescription.text.toString(),
            getData(),
            ""
        )
    }

    private fun getData():String{
        val formatter = SimpleDateFormat("HH:mm:ss - dd/MM/yy", Locale.getDefault())
        return formatter.format(Calendar.getInstance().time)
    }
    private fun actionBarSettings(){
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}