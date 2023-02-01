package ru.worklight64.shoppinglist.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import ru.worklight64.shoppinglist.databinding.DeleteDialogBinding
import ru.worklight64.shoppinglist.databinding.NewListDialogBinding

object DeleteDialog {

    fun showDialog(context:Context, listener: Listener){
        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(context)
        val form = DeleteDialogBinding.inflate(LayoutInflater.from(context))
        builder.setView(form.root)
        form.bDelete.setOnClickListener {
            listener.onClick()
            dialog?.dismiss()
        }

        form.bCancel.setOnClickListener {
            dialog?.dismiss()
        }

        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(null)
        dialog.show()
    }

    interface Listener{
        fun onClick()
    }

}