package ru.worklight64.shoppinglist.fragments

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

import androidx.fragment.app.activityViewModels
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import ru.worklight64.shoppinglist.R
import ru.worklight64.shoppinglist.activities.MainApp
import ru.worklight64.shoppinglist.activities.NewNoteActivity
import ru.worklight64.shoppinglist.databinding.FragmentNoteBinding
import ru.worklight64.shoppinglist.db.MainViewModel
import ru.worklight64.shoppinglist.db.NodeAdapter
import ru.worklight64.shoppinglist.entities.NoteItem

class NoteFragment : BaseFragment(), NodeAdapter.NoteItemListener {

    private lateinit var fragForm: FragmentNoteBinding
    private lateinit var editLauncher: ActivityResultLauncher<Intent>
    private lateinit var adapter: NodeAdapter
    private lateinit var pref: SharedPreferences

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
        pref = PreferenceManager.getDefaultSharedPreferences(requireActivity())
        val linear = getString(R.string.pref_linear)
        if (pref.getString("note_style_key",linear) == linear) rcViewNote.layoutManager = LinearLayoutManager(activity)
        else rcViewNote.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        adapter = NodeAdapter(this@NoteFragment, pref)
        rcViewNote.adapter = adapter
    }

    private fun observer(){
        mainViewModel.allNotes.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            if (it.isEmpty()) fragForm.tvEmpty.visibility = View.VISIBLE else fragForm.tvEmpty.visibility = View.GONE
        }
    }

    private fun onEditResult(){
        editLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
            if (it.resultCode == Activity.RESULT_OK){
                val editState =it.data?.getStringExtra(EDIT_STATE_KEY)
                if (editState == "update"){
                    mainViewModel.updateNote(it.data?.getSerializableExtra(NEW_NOTE_KEY) as NoteItem)
                } else {
                    mainViewModel.insertNote(it.data?.getSerializableExtra(NEW_NOTE_KEY) as NoteItem)
                }
            }
        }
    }



    companion object {
        const val NEW_NOTE_KEY = "title_key"
        const val EDIT_STATE_KEY = "edit_state_key"
        @JvmStatic
        fun newInstance() = NoteFragment()
    }

    override fun deleteItem(id: Int) {
        mainViewModel.deleteNote(id)
    }

    override fun onClickItem(note: NoteItem) {
        editLauncher.launch(Intent(activity, NewNoteActivity::class.java).apply {
            putExtra(NEW_NOTE_KEY, note)
        })
    }
}