package unpsjb.tnt.gestion_gastos

import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import unpsjb.tnt.gestion_gastos.databinding.ActivityMainBinding
import androidx.navigation.navOptions
import androidx.navigation.ui.navigateUp
import unpsjb.tnt.gestion_gastos.ui.ui.login.LoginViewModel
import unpsjb.tnt.gestion_gastos.ui.ui.login.LoginViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private val loginViewModel: LoginViewModel by viewModels {
        LoginViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

//        binding.appBarMain.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null)
//                .setAnchorView(R.id.fab).show()
//        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.fragment_login,
                R.id.fragment_home,
                R.id.fragment_categories,
                R.id.fragment_accounts,
                R.id.fragment_charts,
                R.id.fragment_alerts,
                R.id.fragment_about,
                R.id.nav_slideshow
                ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)

        binding.navView.setNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.nav_slideshow -> {
                    showLogoutDialog()
                    true
                }
                else -> {
                    val navController = findNavController(R.id.nav_host_fragment_content_main)
                    item.onNavDestinationSelected(navController)
                    binding.drawerLayout.closeDrawers()
                    true
                }
            }
        }
        loginViewModel.isLoggedIn.observe(this) { logged ->
            updateMenu(logged)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(this)
            .setTitle("Cerrar sesión")
            .setMessage("¿Estás seguro que querés salir?")
            .setPositiveButton("Sí") { _, _ ->
                logout()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun logout() {
        loginViewModel.logout()
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Primero limpiás todo
        navController.popBackStack(R.id.fragment_login, true)
        // Después navegás al login
        navController.navigate(R.id.fragment_login)
//        navController.navigate(R.id.fragment_login) {
//            popUpTo(navController.graph.id) {
//                inclusive = true
//            }
//        }

        binding.drawerLayout.closeDrawers()
    }

    private fun updateMenu(logged: Boolean) {
        val menu = binding.navView.menu
        Log.d("MainActivity", "Usuario logueado: $logged")
        // Opciones principales
        menu.findItem(R.id.fragment_home)?.isEnabled = logged
        menu.findItem(R.id.fragment_categories)?.isEnabled = logged
        menu.findItem(R.id.fragment_accounts)?.isEnabled = logged
        menu.findItem(R.id.fragment_charts)?.isEnabled = logged
        menu.findItem(R.id.fragment_alerts)?.isEnabled = logged
        menu.findItem(R.id.nav_slideshow)?.isVisible = logged
        menu.findItem(R.id.fragment_login)?.isVisible = !logged
        // Acerca de (siempre visible)
        menu.findItem(R.id.fragment_about)?.isEnabled = true
    }
}