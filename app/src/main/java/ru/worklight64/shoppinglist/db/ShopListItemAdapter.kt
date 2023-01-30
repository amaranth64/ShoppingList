package ru.worklight64.shoppinglist.db

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

import androidx.recyclerview.widget.RecyclerView
import ru.worklight64.shoppinglist.R
import ru.worklight64.shoppinglist.databinding.ListNameItemBinding
import ru.worklight64.shoppinglist.entities.ShoppingListItem
import ru.worklight64.shoppinglist.entities.ShoppingListName
import ru.worklight64.shoppinglist.fragments.ShoppingListNamesFragment

class ShopListItemAdapter(private var listener: ShopListListener): ListAdapter<ShoppingListItem, ShopListItemAdapter.ItemHolder>(ItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return if (viewType == 0) ItemHolder.createShoppingList(parent) else ItemHolder.createLibraryList(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        if (getItem(position).itemType == 0) holder.setItemData(getItem(position), listener)
        else holder.setLibraryData(getItem(position), listener)
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).itemType
    }

    class ItemHolder(view:View):RecyclerView.ViewHolder(view){
        private val itemForm = ListNameItemBinding.bind(view)
        fun setItemData(shopList: ShoppingListItem, listener: ShopListListener)= with(itemForm){

        }

        fun setLibraryData(shopList: ShoppingListItem, listener: ShopListListener)= with(itemForm){

        }

        companion object{
            fun createShoppingList(parent:ViewGroup):ItemHolder{
                return ItemHolder(LayoutInflater.from(parent.context).inflate(
                    R.layout.shop_list_item, parent, false))
            }

            fun createLibraryList(parent:ViewGroup):ItemHolder{
                return ItemHolder(LayoutInflater.from(parent.context).inflate(
                    R.layout.shop_library_list_item, parent, false))
            }
        }
    }

    class ItemComparator: DiffUtil.ItemCallback<ShoppingListItem>(){
        override fun areItemsTheSame(oldItem: ShoppingListItem, newItem: ShoppingListItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ShoppingListItem, newItem: ShoppingListItem): Boolean {
            return oldItem == newItem
        }

    }

    interface ShopListListener{
        fun deleteItem(id: Int)
        fun editItem(shopList: ShoppingListName)

        fun onClickItem(shopList: ShoppingListName)
    }

}