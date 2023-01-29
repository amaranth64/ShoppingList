package ru.worklight64.shoppinglist.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import ru.worklight64.shoppinglist.R
import ru.worklight64.shoppinglist.databinding.ActivityMainBinding
import ru.worklight64.shoppinglist.databinding.ActivityShopListBinding
import ru.worklight64.shoppinglist.db.MainViewModel
import ru.worklight64.shoppinglist.entities.ShoppingListName

class ShopListActivity : AppCompatActivity() {
    private lateinit var form: ActivityShopListBinding
    private var shopListName: ShoppingListName? = null

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
        return true
    }

    private fun init(){
        shopListName = intent.getSerializableExtra(SHOP_LIST_KEY) as ShoppingListName
    }

    companion object{
        const val SHOP_LIST_KEY = "shop_list_key"
    }
}