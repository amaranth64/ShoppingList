package ru.worklight64.shoppinglist.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.TypefaceSpan
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.core.text.getSpans
import androidx.core.view.isVisible
import ru.worklight64.shoppinglist.R
import ru.worklight64.shoppinglist.databinding.ActivityNewNoteBinding
import ru.worklight64.shoppinglist.entities.NoteItem
import ru.worklight64.shoppinglist.fragments.NoteFragment
import ru.worklight64.shoppinglist.utils.HtmlManager
import ru.worklight64.shoppinglist.utils.MyTouchListener
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
        init()
        onClickColorPicker()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun  init(){
        form.colorPicker.setOnTouchListener(MyTouchListener())
    }


    private fun onClickColorPicker() = with(form){
        val diffPos = edDescription.selectionStart - edDescription.selectionEnd
        ibBlack.setOnClickListener {

            ForegroundColorSpan(Color.BLACK)
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
            content = HtmlManager.toHtml(form.edDescription.text)
        )
    }

    private fun createNote(): NoteItem{
        return NoteItem(
            null,
            form.edTitle.text.toString(),
            HtmlManager.toHtml(form.edDescription.text),
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
}