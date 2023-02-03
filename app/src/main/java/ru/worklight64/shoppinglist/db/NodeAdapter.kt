package ru.worklight64.shoppinglist.db

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

import androidx.recyclerview.widget.RecyclerView
import ru.worklight64.shoppinglist.R
import ru.worklight64.shoppinglist.databinding.NoteListItemBinding
import ru.worklight64.shoppinglist.entities.NoteItem
import ru.worklight64.shoppinglist.utils.HtmlManager
import ru.worklight64.shoppinglist.utils.TimeManager

class NodeAdapter(private var listener: NoteItemListener, private val defPref: SharedPreferences): ListAdapter<NoteItem, NodeAdapter.ItemHolder>(ItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setData(getItem(position), listener, defPref)
    }

    class ItemHolder(view:View):RecyclerView.ViewHolder(view){
        private val itemForm = NoteListItemBinding.bind(view)
        fun setData(note: NoteItem, listener: NoteItemListener, defPref: SharedPreferences)= with(itemForm){
            tvTitle.text = note.title
            tvDescription.text = HtmlManager.getFromHtml(note.content).trim()
            tvTime.text = TimeManager.getTimeFormat(note.time, defPref)

            itemView.setOnClickListener {
                listener.onClickItem(note)
            }

            imDelete.setOnClickListener {
                listener.deleteItem(note.id!!)
            }
        }

        companion object{
            fun create(parent:ViewGroup):ItemHolder{
                return ItemHolder(LayoutInflater.from(parent.context).inflate(
                    R.layout.note_list_item, parent, false))
            }
        }
    }

    class ItemComparator: DiffUtil.ItemCallback<NoteItem>(){
        override fun areItemsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
            return oldItem == newItem
        }

    }

    interface NoteItemListener{
        fun deleteItem(id: Int)
        fun onClickItem(note: NoteItem)
    }

}