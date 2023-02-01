package ru.worklight64.shoppinglist.db

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
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
            progressBar.max = shopList.allItemsCounter
            progressBar.progress = shopList.checkedItemsCounter
            val color = ColorStateList.valueOf(getProgressBarColorState(shopList, itemForm.root.context))
            progressBar.progressTintList = color
            cardCounter.backgroundTintList = color
            val str = "${shopList.checkedItemsCounter}/${shopList.allItemsCounter}"
            tvCounter.text = str

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

        private fun getProgressBarColorState(item: ShoppingListName, context: Context):Int{
            var color: Int = 0
            val pbar_100 = item.allItemsCounter
            val pbar_75 = (item.allItemsCounter * 0.75).toInt()
            val pbar_50 = (item.allItemsCounter * 0.50).toInt()
            val pbar_25 = (item.allItemsCounter * 0.25).toInt()

            color = when (item.checkedItemsCounter){
                in 0..pbar_25 -> ContextCompat.getColor(context, R.color.pbar_0_25)
                in pbar_25..pbar_50 -> ContextCompat.getColor(context, R.color.pbar_25_50)
                in pbar_50..pbar_75 -> ContextCompat.getColor(context, R.color.pbar_50_75)
                in pbar_75..pbar_100 -> ContextCompat.getColor(context, R.color.pbar_75_100)
                else -> ContextCompat.getColor(context, R.color.pbar_100)
            }

            return color
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