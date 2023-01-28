package ru.worklight64.shoppinglist.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import ru.worklight64.shoppinglist.fragments.FragmentManager
import ru.worklight64.shoppinglist.R
import ru.worklight64.shoppinglist.databinding.ActivityMainBinding
import ru.worklight64.shoppinglist.fragments.NoteFragment
import ru.worklight64.shoppinglist.fragments.ShoppingListNamesFragment

class MainActivity : AppCompatActivity() {
    lateinit var form: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        form = ActivityMainBinding.inflate(layoutInflater)
        setContentView(form.root)
        FragmentManager.setFragment(ShoppingListNamesFragment.newInstance(), this)
        form.bNav.selectedItemId = R.id.shop_list
        setBottomNavListener()
    }

    private fun setBottomNavListener(){
        form.bNav.setOnItemSelectedListener {
            when (it.itemId){
                R.id.settings->{
                    Toast.makeText(this, this.resources.getString(R.string.settings), Toast.LENGTH_LONG).show()
                }
                R.id.notes->{
                    FragmentManager.setFragment(NoteFragment.newInstance(), this)
                }
                R.id.shop_list->{
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