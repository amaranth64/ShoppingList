package ru.worklight64.shoppinglist.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Delay
import kotlinx.coroutines.delay
import ru.worklight64.shoppinglist.R
import ru.worklight64.shoppinglist.databinding.ActivityShopListBinding
import ru.worklight64.shoppinglist.db.MainViewModel
import ru.worklight64.shoppinglist.db.ShopListItemAdapter
import ru.worklight64.shoppinglist.dialogs.EditListItemDialog
import ru.worklight64.shoppinglist.entities.ShoppingListItem
import ru.worklight64.shoppinglist.entities.ShoppingListName
import ru.worklight64.shoppinglist.utils.ShareShoppingListHelper

class ShopListActivity : AppCompatActivity(),ShopListItemAdapter.ShopListListener {
    private lateinit var form: ActivityShopListBinding
    private var shopListName: ShoppingListName? = null
    private lateinit var saveItemMenu: MenuItem
    private var edItem: EditText? = null
    private var adapter: ShopListItemAdapter? = null
    private lateinit var txtWatcher: TextWatcher

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
        edItem = newItem.actionView?.findViewById(R.id.edNewShopItem) as EditText
        newItem.setOnActionExpandListener(expandActionView())
        saveItemMenu.isVisible = false
        return true
    }

    private fun textWatcher(): TextWatcher{
        return object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                mainViewModel.getAllLibraryItems("%$p0%")
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_item -> {
                addNewShopListItem()
            }
            R.id.delete_list -> {
                mainViewModel.deleteShoppingListName(shopListName?.id!!)
                finish()
            }
            R.id.clear_list -> {
                mainViewModel.clearShoppingListItems(shopListName?.id!!)
            }
            R.id.share_list -> {
                startActivity(Intent.createChooser(
                    ShareShoppingListHelper.share(adapter?.currentList!!, shopListName?.name!!),
                    getString(R.string.share_by)
                ))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun expandActionView():MenuItem.OnActionExpandListener{
        return object : MenuItem.OnActionExpandListener{
            override fun onMenuItemActionExpand(p0: MenuItem): Boolean {
                saveItemMenu.isVisible = true
                edItem?.addTextChangedListener(txtWatcher)
                mainViewModel.getAllLibraryItems("%%")
                libraryObserver()
                mainViewModel.getAllShoppingListItems(shopListName?.id!!).removeObservers(this@ShopListActivity)
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem): Boolean {
                saveItemMenu.isVisible = false
                invalidateOptionsMenu()
                edItem?.removeTextChangedListener(txtWatcher)
                itemsObserver()
                mainViewModel.libraryItems.removeObservers(this@ShopListActivity)
                mainViewModel.libraryItems
                edItem?.setText("")
                return true
            }

        }
    }

    private fun addNewShopListItem(){
        if (edItem?.text.toString().isEmpty()) return
        val item = ShoppingListItem(
            null,
            edItem?.text.toString(),
            "",
            false,
            shopListName?.id!!,
            0
        )
        edItem?.setText("")
        mainViewModel.insertShoppingListItem(item)

    }

    private fun init() = with(form){
        shopListName = intent.getSerializableExtra(SHOP_LIST_KEY) as ShoppingListName

        rcShopListItems.layoutManager= LinearLayoutManager(this@ShopListActivity)
        adapter= ShopListItemAdapter(this@ShopListActivity)
        rcShopListItems.adapter = adapter

        txtWatcher = textWatcher()
    }

    private fun itemsObserver(){
        mainViewModel.getAllShoppingListItems(shopListName?.id!!).observe(this) {
            adapter?.submitList(it)
            if (it.isEmpty()) form.tvEmpty.visibility = View.VISIBLE else form.tvEmpty.visibility = View.GONE
        }
    }

    private fun libraryObserver(){
        mainViewModel.libraryItems.observe(this){
            val tempItem = ArrayList<ShoppingListItem>()
            it.forEach{item->
                val i = ShoppingListItem(
                    item.id,
                    item.name,
                    "",false,0,1
                )
                tempItem.add(i)
            }
            adapter?.submitList(tempItem)
        }
    }

    companion object{
        const val SHOP_LIST_KEY = "shop_list_key"
    }


    override fun onCheckItem(item: ShoppingListItem) {
        mainViewModel.updateShoppingListItem(item)
    }

    override fun onEditItem(item: ShoppingListItem) {
        EditListItemDialog.showDialog(this, item, object : EditListItemDialog.Listener{
            override fun onClick(item: ShoppingListItem) {
                mainViewModel.updateShoppingListItem(item)
            }

        })
    }

    override fun onDeleteItem(item: ShoppingListItem) {
        mainViewModel.deleteShoppingListItem(item.id!!)
    }

    override fun onDeleteLibraryItem(id: Int) {
        mainViewModel.deleteLibraryItem(id)
        Thread.sleep(100)
        mainViewModel.getAllLibraryItems("%${edItem?.text.toString()}%")
    }

    override fun onClickLibraryItem(item: ShoppingListItem) {
        val item = ShoppingListItem(
            null,
            item.name,
            "",
            false,
            shopListName?.id!!,
            0
        )
        edItem?.setText("")
        mainViewModel.insertShoppingListItem(item)
    }

}