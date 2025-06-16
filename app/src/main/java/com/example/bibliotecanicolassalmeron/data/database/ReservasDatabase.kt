package com.example.bibliotecanicolassalmeron.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.bibliotecanicolassalmeron.data.database.dao.ReservaDao
import com.example.bibliotecanicolassalmeron.data.database.entities.ReservaEntity

@Database(entities = [ReservaEntity::class], version = 2, exportSchema = false)
abstract class ReservasDatabase : RoomDatabase() {

    abstract fun reservaDao(): ReservaDao

    companion object {
        @Volatile
        private var INSTANCE: ReservasDatabase? = null

        fun getInstance(context: Context): ReservasDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): ReservasDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                ReservasDatabase::class.java,
                "reservas_db"
            )
                .addMigrations(MIGRATION_1_2)
                .build()
        }
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE reservas ADD COLUMN titulo TEXT NOT NULL DEFAULT 'undefined'")
            }
        }
    }
}
