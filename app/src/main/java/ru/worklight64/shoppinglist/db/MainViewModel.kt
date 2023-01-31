package ru.worklight64.shoppinglist.db

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.worklight64.shoppinglist.entities.NoteItem
import ru.worklight64.shoppinglist.entities.ShoppingListItem
import ru.worklight64.shoppinglist.entities.ShoppingListName

class MainViewModel(database: MainDataBase): ViewModel() {

    private val dao = database.getDao()

    //==============================================
    val allNotes: LiveData<List<NoteItem>> = dao.getAllNotes().asLiveData()
    fun insertNote(noteItem: NoteItem) = viewModelScope.launch {
        dao.insertNote(noteItem)
    }
    fun updateNote(noteItem: NoteItem) = viewModelScope.launch {
        dao.updateNote(noteItem)
    }
    fun deleteNote(id: Int) = viewModelScope.launch {
        dao.deleteNote(id)
    }

    //==============================================
    val allShoppingListNames: LiveData<List<ShoppingListName>> = dao.getAllShoppingListNames().asLiveData()
    fun insertShoppingListName(listName: ShoppingListName) = viewModelScope.launch {
        dao.insertShoppingListName(listName)
    }
    fun deleteShoppingListName(id: Int) = viewModelScope.launch {
        dao.deleteShoppingListName(id)
    }
    fun updateNShoppingListName(listName: ShoppingListName) = viewModelScope.launch {
        dao.updateShoppingListName(listName)
    }

    //==============================================
    fun getAllShoppingListItems(listId: Int): LiveData<List<ShoppingListItem>> {
        return dao.getAllShoppingListItems(listId).asLiveData()
    }

    fun insertShoppingListItem(item: ShoppingListItem) = viewModelScope.launch {
        dao.insertShoppingListItem(item)
    }
    fun updateShoppingListItem(item: ShoppingListItem) = viewModelScope.launch {
        dao.updateShoppingListItem(item)
    }
    //==============================================
    class MainViewModelFactory(private val database: MainDataBase): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(database) as T
            }
            throw IllegalArgumentException("Unknown ViewModelClass")
        }
    }

}