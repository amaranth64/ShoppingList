package ru.worklight64.shoppinglist.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import ru.worklight64.shoppinglist.activities.MainApp
import ru.worklight64.shoppinglist.activities.NewNoteActivity
import ru.worklight64.shoppinglist.databinding.FragmentNoteBinding
import ru.worklight64.shoppinglist.db.MainViewModel

class NoteFragment : BaseFragment() {

    private lateinit var fragForm: FragmentNoteBinding
    private val mainViewModel: MainViewModel by activityViewModels{
        MainViewModel.MainViewModelFactory((context?.applicationContext as MainApp).database)
    }

    override fun onClickNew() {
        startActivity(Intent(activity, NewNoteActivity::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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