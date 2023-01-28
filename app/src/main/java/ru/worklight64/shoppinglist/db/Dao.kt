package ru.worklight64.shoppinglist.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.worklight64.shoppinglist.entities.NoteItem
import ru.worklight64.shoppinglist.entities.ShoppingListName

@Dao
interface Dao {
    //===========================================
    @Query("SELECT * FROM note_list")
    fun getAllNotes(): Flow<List<NoteItem>>
    @Query("DELETE FROM note_list WHERE id IS :id")
    suspend fun deleteNote(id: Int)
    @Insert
    suspend fun insertNote(note: NoteItem)
    @Update
    suspend fun updateNote(note: NoteItem)

    //===========================================
    @Query("SELECT * FROM shopping_list_names")
    fun getAllShoppingListNames(): Flow<List<ShoppingListName>>
    @Insert
    suspend fun insertShoppingListName(note: ShoppingListName)
}