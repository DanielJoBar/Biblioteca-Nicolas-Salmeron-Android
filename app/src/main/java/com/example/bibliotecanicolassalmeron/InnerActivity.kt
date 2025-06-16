package com.example.bibliotecanicolassalmeron

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.bibliotecanicolassalmeron.data.database.entities.UsuarioEntity
import com.example.bibliotecanicolassalmeron.data.repository.UsuarioRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class InnerActivity : AppCompatActivity() {

    var loggedUserName: String? = null
    var loggedUserEmail: String? = null

    // Nueva propiedad para almacenar los datos completos del usuario
    var currentUser: UsuarioEntity? = null

    // Instancia de UsuarioRepository, inyectada con Hilt
    @Inject
    lateinit var usuarioRepository: UsuarioRepository

    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.inner_containerView) as NavHostFragment).navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inner_activity)

        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        // Cargar idioma antes de inflar vistas
        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        val lang = prefs.getString("app_language", Locale.getDefault().language)
        setLocale(lang ?: "es")
        onCreate(savedInstanceState)
        setContentView(R.layout.inner_activity)

        // Recuperar los datos enviados desde Login/Register
        loggedUserEmail = intent.getStringExtra("email")
        loggedUserName = intent.getStringExtra("name")

        // Carga los datos completos del usuario en InnerActivity.
        // Puedes hacerlo dentro de un launch en lifecycleScope:
        loggedUserEmail?.let { email ->
            lifecycleScope.launch {
                currentUser = usuarioRepository.getUserByEmail(email)
                // Puedes también actualizar el toolbar, si necesitas mostrar el nombre, por ejemplo:
                currentUser?.let {
                    loggedUserName = it.name   // O lo que desees mostrar
                    invalidateOptionsMenu()
                }
            }
        }

        // Configuración del NavHostFragment y Toolbar (como ya lo tienes)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.inner_containerView) as NavHostFragment
        val navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.catalogFragment,
                R.id.reservationFragment,
                R.id.aboutFragment
            )
        )
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment,
                R.id.registerFragment -> {
                    toolbar.visibility = View.GONE
                }

                R.id.bookFormFragment,
                R.id.libroDetailFragment -> {
                    toolbar.visibility = View.VISIBLE
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    invalidateOptionsMenu()
                }

                else -> {
                    toolbar.visibility = View.VISIBLE
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                    invalidateOptionsMenu()
                }
            }
        }
    }
    fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        applicationContext.createConfigurationContext(config)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Aquí no ponemos condición, ya que InnerActivity es la actividad principal
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val currentDest = navController.currentDestination?.id

        if (currentDest == R.id.bookFormFragment || currentDest == R.id.libroDetailFragment) {
            menu?.clear()
        } else {
            loggedUserName?.let { name ->
                menu?.findItem(R.id.menu_username)?.title = name
            }
        }

        return super.onPrepareOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_home -> {
                loggedUserEmail?.let { email ->
                    val bundle = Bundle().apply { putString("email", email) }
                    navController.navigate(R.id.homeFragment, bundle)
                }
                true
            }
            R.id.menu_catalog -> {
                navController.navigate(R.id.catalogFragment)
                true
            }
            R.id.menu_reservations -> {
                navController.navigate(R.id.reservationFragment)
                true
            }
            R.id.menu_about -> {
                navController.navigate(R.id.aboutFragment)
                true
            }
            R.id.menu_username -> {
                navController.navigate(R.id.userProfileFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


}
