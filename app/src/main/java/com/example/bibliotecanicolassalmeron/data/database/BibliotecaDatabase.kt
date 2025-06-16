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

/**
 * Clase que representa la base de datos principal de la aplicación.
 * Utiliza Room para persistencia local de datos.
 *
 * @property libroDao Acceso a operaciones relacionadas con libros.
 * @property usuarioDao Acceso a operaciones relacionadas con usuarios.
 */
@Database(entities = [LibroEntity::class, UsuarioEntity::class], version = 2, exportSchema = false)
abstract class BibliotecaDatabase : RoomDatabase() {

    /**
     * Devuelve el DAO para operaciones sobre la entidad [LibroEntity].
     */
    abstract fun libroDao(): LibroDao

    /**
     * Devuelve el DAO para operaciones sobre la entidad [UsuarioEntity].
     */
    abstract fun usuarioDao(): UsuarioDao

    companion object {
        @Volatile
        private var INSTANCE: BibliotecaDatabase? = null

        /**
         * Devuelve una instancia singleton de la base de datos.
         *
         * @param context Contexto de la aplicación.
         * @return Instancia de [BibliotecaDatabase].
         */
        fun getInstance(context: Context): BibliotecaDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context)
                    .also { INSTANCE = it }
            }
        }

        /**
         * Crea y configura la base de datos usando Room.
         *
         * @param context Contexto de la aplicación.
         * @return Instancia de [BibliotecaDatabase].
         */
        private fun buildDatabase(context: Context): BibliotecaDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                BibliotecaDatabase::class.java,
                "biblioteca_db"
            )
                .addMigrations(MIGRATION_1_2)
                .build()
        }

        /**
         * Migración de la versión 1 a la 2 de la base de datos.
         * Se añade la columna `password` a la tabla `usuarios`.
         */
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE usuarios ADD COLUMN password TEXT NOT NULL DEFAULT 'undefined'"
                )
            }
        }
    }
}
