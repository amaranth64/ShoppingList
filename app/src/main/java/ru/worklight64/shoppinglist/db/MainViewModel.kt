package ru.worklight64.shoppinglist.db

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.worklight64.shoppinglist.entities.NoteItem

class MainViewModel(database: MainDataBase): ViewModel() {

    private val dao = database.getDao()
    val allNotes: LiveData<List<NoteItem>> = dao.getAllNotes().asLiveData()
    fun insertData(noteItem: NoteItem) = viewModelScope.launch {
        dao.insertNote(noteItem)
    }

    class MainViewModelFactory(val database: MainDataBase): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(database) as T
            }
            throw IllegalArgumentException("Unknown ViewModelClass")
        }
    }

}