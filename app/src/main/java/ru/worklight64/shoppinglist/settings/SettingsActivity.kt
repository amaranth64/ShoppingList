package ru.worklight64.shoppinglist.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.preference.PreferenceManager
import ru.worklight64.shoppinglist.R

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(getSelectedTheme())
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        if (savedInstanceState == null){
            supportFragmentManager.beginTransaction().replace(R.id.placeHolder, SettingsFragment()).commit()
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    private fun getSelectedTheme(): Int{
        val defPref = PreferenceManager.getDefaultSharedPreferences(this)
        val theme = defPref.getString("theme_key","green").toString()
        return if (theme == "green") {
            R.style.Theme_ShoppingListGreen
        } else {
            R.style.Theme_ShoppingListBlue
        }
    }
}