package ru.worklight64.shoppinglist.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import ru.worklight64.shoppinglist.fragments.FragmentManager
import ru.worklight64.shoppinglist.R
import ru.worklight64.shoppinglist.databinding.ActivityMainBinding
import ru.worklight64.shoppinglist.fragments.NoteFragment
import ru.worklight64.shoppinglist.fragments.ShoppingListNamesFragment
import ru.worklight64.shoppinglist.settings.SettingsActivity

class MainActivity : AppCompatActivity() {
    lateinit var form: ActivityMainBinding
    private var currentMenuItemId = R.id.shop_list
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        form = ActivityMainBinding.inflate(layoutInflater)
        setContentView(form.root)
        FragmentManager.setFragment(ShoppingListNamesFragment.newInstance(), this)
        form.bNav.selectedItemId = R.id.shop_list
        setBottomNavListener()
    }

    override fun onResume() {
        super.onResume()
        form.bNav.selectedItemId = currentMenuItemId
    }



    private fun setBottomNavListener(){
        form.bNav.setOnItemSelectedListener {
            when (it.itemId){
                R.id.settings->{
                    startActivity(Intent(this, SettingsActivity::class.java))
                }
                R.id.notes->{
                    currentMenuItemId = R.id.notes
                    FragmentManager.setFragment(NoteFragment.newInstance(), this)
                }
                R.id.shop_list->{
                    currentMenuItemId = R.id.shop_list
                    FragmentManager.setFragment(ShoppingListNamesFragment.newInstance(), this)
                }
                R.id.new_item->{
                    FragmentManager.currentFragment?.onClickNew()
                }
            }
            true
        }
    }
}