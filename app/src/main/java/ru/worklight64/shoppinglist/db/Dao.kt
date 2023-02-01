package ru.worklight64.shoppinglist.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.worklight64.shoppinglist.entities.LibraryItem
import ru.worklight64.shoppinglist.entities.NoteItem
import ru.worklight64.shoppinglist.entities.ShoppingListItem
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
    suspend fun insertShoppingListName(shopList: ShoppingListName)
    @Query("DELETE FROM shopping_list_names WHERE id IS :id")
    suspend fun deleteShoppingListName(id: Int)
    @Update
    suspend fun updateShoppingListName(shopList: ShoppingListName)

    //===========================================
    @Query("SELECT * FROM shop_list_item WHERE listId LIKE :linkId")
    fun getAllShoppingListItems(linkId: Int): Flow<List<ShoppingListItem>>
    @Insert
    suspend fun insertShoppingListItem(item: ShoppingListItem)
    @Update
    suspend fun updateShoppingListItem(item: ShoppingListItem)
    @Query("DELETE FROM shop_list_item WHERE id IS :id")
    suspend fun deleteShoppingListItem(id: Int)
    @Query("DELETE FROM shop_list_item WHERE listId LIKE :linkId")
    suspend fun deleteShoppingListItems(linkId: Int)

    //===========================================
    @Query("SELECT * FROM library WHERE name LIKE :name")
    suspend fun getAllLibraryItems(name: String): List<LibraryItem>
    @Insert
    suspend fun insertLibraryItem(item: LibraryItem)
    @Query("DELETE FROM library WHERE id IS :id")
    suspend fun deleteLibraryItem(id: Int)

}