package com.example.bibliotecanicolassalmeron.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.bibliotecanicolassalmeron.data.database.dao.ReservaDao
import com.example.bibliotecanicolassalmeron.data.database.entities.ReservaEntity

/**
 * Base de datos Room que gestiona la persistencia de reservas.
 *
 * @property reservaDao DAO para operaciones sobre la entidad [ReservaEntity].
 */
@Database(entities = [ReservaEntity::class], version = 2, exportSchema = false)
abstract class ReservasDatabase : RoomDatabase() {

    /**
     * Devuelve una instancia del DAO de reservas.
     */
    abstract fun reservaDao(): ReservaDao

    companion object {
        @Volatile
        private var INSTANCE: ReservasDatabase? = null

        /**
         * Obtiene una instancia singleton de [ReservasDatabase].
         *
         * @param context Contexto de la aplicación.
         * @return Instancia de la base de datos.
         */
        fun getInstance(context: Context): ReservasDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        /**
         * Crea y configura la base de datos con soporte para migraciones.
         *
         * @param context Contexto de la aplicación.
         * @return Nueva instancia de [ReservasDatabase].
         */
        private fun buildDatabase(context: Context): ReservasDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                ReservasDatabase::class.java,
                "reservas_db"
            )
                .addMigrations(MIGRATION_1_2)
                .build()
        }

        /**
         * Migración de la base de datos de la versión 1 a la 2.
         * Se añade la columna `titulo` a la tabla `reservas`.
         */
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE reservas ADD COLUMN titulo TEXT NOT NULL DEFAULT 'undefined'")
            }
        }
    }
}
