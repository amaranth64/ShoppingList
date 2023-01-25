package ru.worklight64.shoppinglist.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import ru.worklight64.shoppinglist.R
import ru.worklight64.shoppinglist.databinding.ActivityNewNoteBinding
import ru.worklight64.shoppinglist.fragments.NoteFragment

class NewNoteActivity : AppCompatActivity() {
    private lateinit var form: ActivityNewNoteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        form = ActivityNewNoteBinding.inflate(layoutInflater)
        setContentView(form.root)
        actionBarSettings()
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
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setMainResult(){
        val i = Intent().apply {
            putExtra(NoteFragment.TITLE_KEY, form.edTitle.text.toString())
            putExtra(NoteFragment.DESC_KEY, form.edDescription.text.toString())
        }
        setResult(RESULT_OK, i)
        finish()
    }

    private fun actionBarSettings(){
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}