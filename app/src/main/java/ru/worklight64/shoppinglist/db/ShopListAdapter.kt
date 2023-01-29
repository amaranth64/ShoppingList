package ru.worklight64.shoppinglist.db

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

import androidx.recyclerview.widget.RecyclerView
import ru.worklight64.shoppinglist.R
import ru.worklight64.shoppinglist.databinding.ListNameItemBinding
import ru.worklight64.shoppinglist.entities.ShoppingListName
import ru.worklight64.shoppinglist.fragments.ShoppingListNamesFragment

class ShopListAdapter(private var listener: ShopListListener): ListAdapter<ShoppingListName, ShopListAdapter.ItemHolder>(ItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setData(getItem(position), listener)
    }

    class ItemHolder(view:View):RecyclerView.ViewHolder(view){
        private val itemForm = ListNameItemBinding.bind(view)
        fun setData(shopList: ShoppingListName, listener: ShopListListener)= with(itemForm){
            tvListName.text = shopList.name
            tvTime.text = shopList.time

            itemView.setOnClickListener {
                listener.onClickItem(shopList)
            }

            ibEdit.setOnClickListener {
                listener.editItem(shopList)
            }

            ibDelete.setOnClickListener {
                listener.deleteItem(shopList.id!!)
            }
        }

        companion object{
            fun create(parent:ViewGroup):ItemHolder{
                return ItemHolder(LayoutInflater.from(parent.context).inflate(
                    R.layout.list_name_item, parent, false))
            }
        }
    }

    class ItemComparator: DiffUtil.ItemCallback<ShoppingListName>(){
        override fun areItemsTheSame(oldItem: ShoppingListName, newItem: ShoppingListName): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ShoppingListName, newItem: ShoppingListName): Boolean {
            return oldItem == newItem
        }

    }

    interface ShopListListener{
        fun deleteItem(id: Int)
        fun editItem(shopList: ShoppingListName)

        fun onClickItem(shopList: ShoppingListName)
    }

}