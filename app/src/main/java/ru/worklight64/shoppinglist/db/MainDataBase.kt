package ru.worklight64.shoppinglist.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.worklight64.shoppinglist.entities.LibraryItem
import ru.worklight64.shoppinglist.entities.NoteItem
import ru.worklight64.shoppinglist.entities.ShoppingListItem
import ru.worklight64.shoppinglist.entities.ShoppingListNames

@Database(entities = [LibraryItem::class, NoteItem::class, ShoppingListItem::class,ShoppingListNames::class], version = 1)
abstract class MainDataBase: RoomDatabase() {

    abstract fun getDao():Dao

    companion object{

        @Volatile
        private var INSTANCE: MainDataBase? = null
        fun getDataBase(context: Context):MainDataBase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MainDataBase::class.java,
                    "shopping_list.db"
                ).build()
                instance
            }
        }
    }

}