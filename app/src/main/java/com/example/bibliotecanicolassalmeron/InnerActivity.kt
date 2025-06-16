package com.example.bibliotecanicolassalmeron

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels // Importar viewModels para ViewModel
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.bibliotecanicolassalmeron.data.database.entities.UsuarioEntity
import com.example.bibliotecanicolassalmeron.data.repository.UsuarioRepository
import com.example.bibliotecanicolassalmeron.ui.viewmodels.ReservaListViewModel // Importar tu ViewModel
import com.example.bibliotecanicolassalmeron.ui.viewmodels.ReservationEvent
import com.google.android.material.snackbar.Snackbar // Importar Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class InnerActivity : AppCompatActivity() {

    var loggedUserName: String? = null
    var loggedUserEmail: String? = null
    var currentUser: UsuarioEntity? = null

    @Inject
    lateinit var usuarioRepository: UsuarioRepository

    // Obtener la instancia del ReservaListViewModel
    private val reservaListViewModel: ReservaListViewModel by viewModels()

    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.inner_containerView) as NavHostFragment).navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inner_activity)

        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        loggedUserEmail = intent.getStringExtra("email")
        loggedUserName = intent.getStringExtra("name")

        loggedUserEmail?.let { email ->
            lifecycleScope.launch {
                currentUser = usuarioRepository.getUserByEmail(email)
                currentUser?.let {
                    loggedUserName = it.name
                    invalidateOptionsMenu()
                }
            }
        }

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

        // Observar los mensajes del Snackbar desde el ViewModel
        val rootView: View = findViewById(android.R.id.content) // O la vista raíz de tu layout
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                reservaListViewModel.eventFlow.collect { event ->
                    when (event) {
                        is ReservationEvent.ReservationAddedSuccess -> {
                            Snackbar.make(rootView, "Reserva agregada con éxito", Snackbar.LENGTH_LONG).show()
                        }
                        is ReservationEvent.ReservationAlreadyExists -> {
                            Snackbar.make(rootView, "La reserva para ${event.bookTitle} ya existe", Snackbar.LENGTH_LONG).show()
                        }
                        is ReservationEvent.ReservationDeletedSuccess -> {
                            Snackbar.make(rootView, "Reserva eliminada con éxito", Snackbar.LENGTH_LONG).show()
                        }
                        is ReservationEvent.GeneralError -> {
                            Snackbar.make(rootView, "Ocurrió un error", Snackbar.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
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