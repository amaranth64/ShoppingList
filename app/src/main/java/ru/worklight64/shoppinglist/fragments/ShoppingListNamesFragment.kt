package ru.worklight64.shoppinglist.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ru.worklight64.shoppinglist.activities.MainApp
import ru.worklight64.shoppinglist.activities.NewNoteActivity
import ru.worklight64.shoppinglist.databinding.FragmentShoppingListNamesBinding
import ru.worklight64.shoppinglist.db.MainViewModel
import ru.worklight64.shoppinglist.db.ShopListAdapter
import ru.worklight64.shoppinglist.dialogs.DeleteDialog
import ru.worklight64.shoppinglist.dialogs.NewListDialog
import ru.worklight64.shoppinglist.entities.ShoppingListName
import ru.worklight64.shoppinglist.utils.TimeManager


class ShoppingListNamesFragment : BaseFragment(), ShopListAdapter.ShopListListener {
    private lateinit var fragForm: FragmentShoppingListNamesBinding
    private lateinit var adapter: ShopListAdapter
    private lateinit var editLauncher: ActivityResultLauncher<Intent>

    private val mainViewModel: MainViewModel by activityViewModels{
        MainViewModel.MainViewModelFactory((context?.applicationContext as MainApp).database)
    }

    override fun onClickNew() {
        NewListDialog.showDialog(activity as AppCompatActivity, object : NewListDialog.Listener{
            override fun onClick(newListName: String) {
                val shopListNameItem = ShoppingListName(
                    null,
                    newListName,
                    TimeManager.getData(),
                    0,
                    0,
                    ""
                )
                mainViewModel.insertShoppingListName(shopListNameItem)
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragForm = FragmentShoppingListNamesBinding.inflate(inflater, container, false)
        return fragForm.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()
        observer()
    }

    private fun observer(){
        mainViewModel.allShoppingListNames.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun initRcView() = with(fragForm){
        rcShoppingListNames.layoutManager = LinearLayoutManager(activity)
        adapter = ShopListAdapter(this@ShoppingListNamesFragment)
        rcShoppingListNames.adapter = adapter
    }

    override fun deleteItem(id: Int) {
        DeleteDialog.showDialog(context as AppCompatActivity, object : DeleteDialog.Listener{
            override fun onClick() {
                mainViewModel.deleteShoppingListName(id)
            }
        })
    }

    override fun editItem(shopList: ShoppingListName) {


        NewListDialog.showDialog(activity as AppCompatActivity, object : NewListDialog.Listener{
            override fun onClick(newListName: String) {
                mainViewModel.updateNShoppingListName(shopList.copy(name = newListName))
            }
        },shopList.name)

    }

    override fun onClickItem(shopList: ShoppingListName) {

    }

    companion object {
        @JvmStatic
        fun newInstance() = ShoppingListNamesFragment()
    }


}