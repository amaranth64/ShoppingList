package ru.worklight64.shoppinglist.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.worklight64.shoppinglist.R
import ru.worklight64.shoppinglist.databinding.ActivityShopListBinding
import ru.worklight64.shoppinglist.db.MainViewModel
import ru.worklight64.shoppinglist.entities.ShoppingListItem
import ru.worklight64.shoppinglist.entities.ShoppingListName

class ShopListActivity : AppCompatActivity() {
    private lateinit var form: ActivityShopListBinding
    private var shopListName: ShoppingListName? = null
    private lateinit var saveItemMenu: MenuItem
    private var edName: EditText? = null

    private val mainViewModel: MainViewModel by viewModels{
        MainViewModel.MainViewModelFactory((applicationContext as MainApp).database)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        form = ActivityShopListBinding.inflate(layoutInflater)
        setContentView(form.root)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.shop_list_menu, menu)
        saveItemMenu = menu?.findItem(R.id.save_item)!!
        val newItem = menu.findItem(R.id.new_item)
        edName = newItem.actionView?.findViewById(R.id.edNewShopItem) as EditText
        newItem.setOnActionExpandListener(expandActionView())
        saveItemMenu.isVisible = false
        return true
    }

    private fun expandActionView():MenuItem.OnActionExpandListener{
        return object : MenuItem.OnActionExpandListener{
            override fun onMenuItemActionExpand(p0: MenuItem): Boolean {
                saveItemMenu.isVisible = true
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem): Boolean {
                saveItemMenu.isVisible = false
                invalidateOptionsMenu()
                return true
            }

        }
    }

    private fun addNewShopListItem(){
        if (edName?.text.toString().isEmpty()) return
        val item = ShoppingListItem(
            null,
            edName?.text.toString(),
            "",
            0,
            shopListName?.id!!,
            0
        )
        mainViewModel.insertShoppingListItem(item)
    }

    private fun init(){
        shopListName = intent.getSerializableExtra(SHOP_LIST_KEY) as ShoppingListName
    }

    companion object{
        const val SHOP_LIST_KEY = "shop_list_key"
    }
}