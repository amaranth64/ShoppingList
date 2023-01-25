package ru.worklight64.shoppinglist.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ru.worklight64.shoppinglist.activities.MainApp
import ru.worklight64.shoppinglist.activities.NewNoteActivity
import ru.worklight64.shoppinglist.databinding.FragmentNoteBinding
import ru.worklight64.shoppinglist.db.MainViewModel
import ru.worklight64.shoppinglist.db.NodeAdapter
import ru.worklight64.shoppinglist.entities.NoteItem

class NoteFragment : BaseFragment(), NodeAdapter.DeleteListener {

    private lateinit var fragForm: FragmentNoteBinding
    private lateinit var editLauncher: ActivityResultLauncher<Intent>
    private lateinit var adapter: NodeAdapter

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()
        observer()
    }
    private fun initRcView() = with(fragForm){
        rcViewNote.layoutManager = LinearLayoutManager(activity)
        adapter = NodeAdapter(this@NoteFragment)
        rcViewNote.adapter = adapter

    }

    private fun observer(){
        mainViewModel.allNotes.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun onEditResult(){
        editLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
            if (it.resultCode == Activity.RESULT_OK){
                mainViewModel.insertData(it.data?.getSerializableExtra(NEW_NOTE_KEY) as NoteItem)
            }
        }
    }



    companion object {
        const val NEW_NOTE_KEY = "title_key"
        @JvmStatic
        fun newInstance() = NoteFragment()
    }

    override fun deleteItem(id: Int) {
        mainViewModel.deleteData(id)
    }
}