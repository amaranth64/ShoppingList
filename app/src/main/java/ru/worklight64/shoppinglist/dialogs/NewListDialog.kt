package ru.worklight64.shoppinglist.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import ru.worklight64.shoppinglist.databinding.NewListDialogBinding

object NewListDialog {

    fun showDialog(context:Context, listener: Listener){
        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(context)
        val form = NewListDialogBinding.inflate(LayoutInflater.from(context))
        builder.setView(form.root)
        form.bCreate.setOnClickListener {
            val listName = form.edNewListName.text.toString()
            if (listName.isNotEmpty()){
                listener.onClick(listName)
            }
            dialog?.dismiss()
        }
        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(null)
        dialog.show()
    }

    interface Listener{
        fun onClick(newListName: String)
    }

}