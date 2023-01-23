package ru.worklight64.shoppinglist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.worklight64.shoppinglist.databinding.FragmentNoteBinding

class NoteFragment : BaseFragment() {

    private lateinit var fragForm: FragmentNoteBinding

    override fun onClickNew() {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragForm = FragmentNoteBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return fragForm.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = NoteFragment()
    }
}