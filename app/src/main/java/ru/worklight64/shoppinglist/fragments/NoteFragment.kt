package ru.worklight64.shoppinglist.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

import androidx.fragment.app.activityViewModels
import ru.worklight64.shoppinglist.activities.MainApp
import ru.worklight64.shoppinglist.activities.NewNoteActivity
import ru.worklight64.shoppinglist.databinding.FragmentNoteBinding
import ru.worklight64.shoppinglist.db.MainViewModel

class NoteFragment : BaseFragment() {

    private lateinit var fragForm: FragmentNoteBinding
    private lateinit var editLauncher: ActivityResultLauncher<Intent>

    private val mainViewModel: MainViewModel by activityViewModels{
        MainViewModel.MainViewModelFactory((context?.applicationContext as MainApp).database)
    }

    override fun onClickNew() {
        editLauncher.launch(Intent(activity, NewNoteActivity::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onEditResult()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragForm = FragmentNoteBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return fragForm.root
    }

    private fun onEditResult(){
        editLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
            if (it.resultCode == Activity.RESULT_OK){
                Toast.makeText(activity, it.data?.getStringExtra(TITLE_KEY),Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        const val TITLE_KEY = "title_key"
        const val DESC_KEY = "desc_key"
        @JvmStatic
        fun newInstance() = NoteFragment()
    }
}