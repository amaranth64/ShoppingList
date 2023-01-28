package ru.worklight64.shoppinglist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import ru.worklight64.shoppinglist.activities.MainApp
import ru.worklight64.shoppinglist.databinding.FragmentShoppingListNamesBinding
import ru.worklight64.shoppinglist.db.MainViewModel
import ru.worklight64.shoppinglist.dialogs.NewListDialog
import ru.worklight64.shoppinglist.entities.ShoppingListName
import ru.worklight64.shoppinglist.utils.TimeManager


class ShoppingListNamesFragment : BaseFragment() {
    private lateinit var fragForm: FragmentShoppingListNamesBinding

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

        }
    }

    private fun initRcView() = with(fragForm){

    }

    companion object {
        @JvmStatic
        fun newInstance() = ShoppingListNamesFragment()
    }
}