package ru.worklight64.shoppinglist.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import ru.worklight64.shoppinglist.R
import ru.worklight64.shoppinglist.databinding.ActivityShopListBinding
import ru.worklight64.shoppinglist.db.MainViewModel
import ru.worklight64.shoppinglist.db.ShopListItemAdapter
import ru.worklight64.shoppinglist.entities.ShoppingListItem
import ru.worklight64.shoppinglist.entities.ShoppingListName

class ShopListActivity : AppCompatActivity(),ShopListItemAdapter.ShopListListener {
    private lateinit var form: ActivityShopListBinding
    private var shopListName: ShoppingListName? = null
    private lateinit var saveItemMenu: MenuItem
    private var edName: EditText? = null
    private var adapter: ShopListItemAdapter? = null

    private val mainViewModel: MainViewModel by viewModels{
        MainViewModel.MainViewModelFactory((applicationContext as MainApp).database)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        form = ActivityShopListBinding.inflate(layoutInflater)
        setContentView(form.root)
        init()
        itemsObserver()
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.save_item) addNewShopListItem()
        return super.onOptionsItemSelected(item)
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
        edName?.setText("")
        mainViewModel.insertShoppingListItem(item)
    }

    private fun init() = with(form){
        shopListName = intent.getSerializableExtra(SHOP_LIST_KEY) as ShoppingListName

        rcShopListItems.layoutManager= LinearLayoutManager(this@ShopListActivity)
        adapter= ShopListItemAdapter(this@ShopListActivity)
        rcShopListItems.adapter = adapter
    }

    private fun itemsObserver(){
        mainViewModel.getAllShoppingListItems(shopListName?.id!!).observe(this, {
            adapter?.submitList(it)
        })
    }

    companion object{
        const val SHOP_LIST_KEY = "shop_list_key"
    }

    override fun deleteItem(id: Int) {

    }

    override fun editItem(shopList: ShoppingListName) {

    }

    override fun onClickItem(shopList: ShoppingListName) {

    }
}