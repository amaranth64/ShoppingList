package ru.worklight64.shoppinglist.db

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

import androidx.recyclerview.widget.RecyclerView
import ru.worklight64.shoppinglist.R
import ru.worklight64.shoppinglist.databinding.ShopListItemBinding
import ru.worklight64.shoppinglist.entities.ShoppingListItem

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

    class ItemHolder(val view:View):RecyclerView.ViewHolder(view){

        fun setItemData(shopList: ShoppingListItem, listener: ShopListListener){
            val itemForm = ShopListItemBinding.bind(view)
            itemForm.apply {
                tvName.text = shopList.name
                tvInfo.text = shopList.itemInfo
                checkBox.isChecked = shopList.itemChecked
                if (shopList.itemInfo.isEmpty()) tvInfo.visibility = View.GONE
                else tvInfo.visibility = View.VISIBLE
                setPaintFlagAndColor(itemForm)

                checkBox.setOnClickListener {
                    listener.onCheckItem(shopList.copy(itemChecked = checkBox.isChecked))
                }
                ibEdit.setOnClickListener {
                    listener.onEditItem(shopList)
                }
            }
        }

        private fun setPaintFlagAndColor(itemForm: ShopListItemBinding){
            itemForm.apply {
                if (checkBox.isChecked){
                    tvName.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    tvInfo.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    tvName.setTextColor(ContextCompat.getColor(itemForm.root.context, R.color.text_grey))
                    tvInfo.setTextColor(ContextCompat.getColor(itemForm.root.context, R.color.text_grey))
                } else {
                    tvName.paintFlags = Paint.ANTI_ALIAS_FLAG
                    tvInfo.paintFlags = Paint.ANTI_ALIAS_FLAG
                    tvName.setTextColor(ContextCompat.getColor(itemForm.root.context, R.color.text_black))
                    tvInfo.setTextColor(ContextCompat.getColor(itemForm.root.context, R.color.text_black))
                }
            }
        }

        fun setLibraryData(shopList: ShoppingListItem, listener: ShopListListener){

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

        fun onCheckItem(item: ShoppingListItem)
        fun onEditItem(item: ShoppingListItem)

    }

}