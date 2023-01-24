package ru.worklight64.shoppinglist.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import ru.worklight64.shoppinglist.R
import ru.worklight64.shoppinglist.databinding.ActivityNewNoteBinding

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

        } else if (item.itemId == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun actionBarSettings(){
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}