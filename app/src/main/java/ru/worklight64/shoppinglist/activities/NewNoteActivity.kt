package ru.worklight64.shoppinglist.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.preference.PreferenceManager
import ru.worklight64.shoppinglist.R
import ru.worklight64.shoppinglist.databinding.ActivityNewNoteBinding
import ru.worklight64.shoppinglist.entities.NoteItem
import ru.worklight64.shoppinglist.fragments.NoteFragment
import ru.worklight64.shoppinglist.utils.HtmlManager
import ru.worklight64.shoppinglist.utils.MyTouchListener
import ru.worklight64.shoppinglist.utils.TimeManager
import java.text.SimpleDateFormat
import java.util.*

class NewNoteActivity : AppCompatActivity() {
    private lateinit var form: ActivityNewNoteBinding
    private var note: NoteItem? = null
    private var pref: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(getSelectedTheme())
        super.onCreate(savedInstanceState)
        form = ActivityNewNoteBinding.inflate(layoutInflater)
        setContentView(form.root)
        actionBarSettings()
        getNoteItem()
        init()
        onClickColorPicker()
        actionMenuCallback()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun  init(){
        form.colorPicker.setOnTouchListener(MyTouchListener())
        pref = PreferenceManager.getDefaultSharedPreferences(this)
        setTextSize()
    }

    private fun setTextSize() = with(form){
        edTitle.setTextSize(pref?.getString("title_size_key", "18"))
        edDescription.setTextSize(pref?.getString("content_size_key", "16"))
    }

    private fun onClickColorPicker() = with(form){
        ibBlack.setOnClickListener {

            setColorForSelectedText(R.color.picker_black)
        }
        ibBlue.setOnClickListener {
            setColorForSelectedText(R.color.picker_blue)
        }
        ibRed.setOnClickListener {
            setColorForSelectedText(R.color.picker_red)
        }
        ibYellow.setOnClickListener { setColorForSelectedText(R.color.picker_yellow)  }
        ibOrange.setOnClickListener { setColorForSelectedText(R.color.picker_orange)  }
        ibGreen.setOnClickListener { setColorForSelectedText(R.color.picker_green)  }
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
        } else if (item.itemId == R.id.id_color){
            if (form.colorPicker.isVisible) closeColorPicker() else openColorPicker()
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

    private fun setColorForSelectedText(colorId: Int) = with(form) {
        val startPos = edDescription.selectionStart
        val endPos = edDescription.selectionEnd

        val styles = edDescription.text.getSpans(startPos, endPos, ForegroundColorSpan::class.java)
        if (styles.isNotEmpty()) edDescription.text.removeSpan(styles[0])
        edDescription.text.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this@NewNoteActivity, colorId)),
            startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

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
            edDescription.setText(HtmlManager.getFromHtml(note?.content!!).trim())
        }
    }

    private fun setMainResult(){
        var editSate = "new"
        val tempNote: NoteItem?
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
            content = HtmlManager.toHtml(form.edDescription.text)
        )
    }

    private fun createNote(): NoteItem{
        return NoteItem(
            null,
            form.edTitle.text.toString(),
            HtmlManager.toHtml(form.edDescription.text),
            TimeManager.getData(),
            ""
        )
    }


    private fun actionBarSettings(){
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun openColorPicker(){
        val startAnim = AnimationUtils.loadAnimation(this, R.anim.open_color_picker)
        form.colorPicker.visibility = View.VISIBLE
        form.colorPicker.startAnimation(startAnim)
    }

    private fun closeColorPicker(){
        val closeAnim = AnimationUtils.loadAnimation(this, R.anim.close_color_picker)
        closeAnim.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationStart(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                form.colorPicker.visibility = View.GONE
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }

        })
        form.colorPicker.startAnimation(closeAnim)
    }

    private  fun actionMenuCallback(){
        val actionCallback = object : ActionMode.Callback{
            override fun onCreateActionMode(p0: ActionMode?, p1: Menu?): Boolean {
                p1?.clear()
                return true
            }

            override fun onPrepareActionMode(p0: ActionMode?, p1: Menu?): Boolean {
                p1?.clear()
                return true
            }

            override fun onActionItemClicked(p0: ActionMode?, p1: MenuItem?): Boolean {
                return true
            }

            override fun onDestroyActionMode(p0: ActionMode?) {

            }

        }
        form.edDescription.customSelectionActionModeCallback = actionCallback
    }

    private fun EditText.setTextSize(size: String?){
        if (size != null) this.textSize = size.toFloat()
    }

    private fun getSelectedTheme(): Int{
        val def = PreferenceManager.getDefaultSharedPreferences(this)
        val theme = def.getString("theme_key","green")
        return if (theme == "green") {
            R.style.Theme_NoFullWindowGreen
        } else {
            R.style.Theme_NoFullWindowBlue
        }
    }
}