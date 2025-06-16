package com.example.bibliotecanicolassalmeron.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.bibliotecanicolassalmeron.data.database.dao.LibroDao
import com.example.bibliotecanicolassalmeron.data.database.dao.UsuarioDao
import com.example.bibliotecanicolassalmeron.data.database.entities.LibroEntity
import com.example.bibliotecanicolassalmeron.data.database.entities.UsuarioEntity

@Database(entities = [LibroEntity::class, UsuarioEntity::class], version = 2, exportSchema = false)
abstract class BibliotecaDatabase : RoomDatabase() {

    abstract fun libroDao(): LibroDao
    abstract fun usuarioDao(): UsuarioDao

    companion object {
        @Volatile
        private var INSTANCE: BibliotecaDatabase? = null

        fun getInstance(context: Context): BibliotecaDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context)
                    .also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): BibliotecaDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                BibliotecaDatabase::class.java,
                "biblioteca_db"
            )
                .addMigrations(MIGRATION_1_2)
                .build()

        }
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE usuarios ADD COLUMN password TEXT NOT NULL DEFAULT 'undefined'")            }
        }
    }
}
