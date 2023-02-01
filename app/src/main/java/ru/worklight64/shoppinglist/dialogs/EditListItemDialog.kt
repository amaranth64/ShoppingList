package ru.worklight64.shoppinglist.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import ru.worklight64.shoppinglist.R
import ru.worklight64.shoppinglist.databinding.EditListItemDialogBinding
import ru.worklight64.shoppinglist.databinding.NewListDialogBinding
import ru.worklight64.shoppinglist.entities.ShoppingListItem

object EditListItemDialog {

    fun showDialog(context:Context, item: ShoppingListItem, listener: Listener){
        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(context)
        val form = EditListItemDialogBinding.inflate(LayoutInflater.from(context))
        builder.setView(form.root)
        form.edTitle.setText(item.name)
        form.edInfo.setText(item.itemInfo)

        form.bUpdate.setOnClickListener {

            if (form.edTitle.text.toString().isNotEmpty()){
                listener.onClick(item.copy( name = form.edTitle.text.toString(), itemInfo = form.edInfo.text.toString()))
            }
            dialog?.dismiss()
        }
        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(null)
        dialog.show()
    }

    interface Listener{
        fun onClick(item: ShoppingListItem)
    }

}