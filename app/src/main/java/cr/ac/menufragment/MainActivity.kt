package cr.ac.menufragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.view.Gravity
import android.view.MenuItem
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView


lateinit var drawerLayout : DrawerLayout

// Extends the class AppCompat and implements the interface NavigationView
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)

        var toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState() // After doing the addDrawerListener this method has to be called

        var navigationView = findViewById<NavigationView>(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener ( this )

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

    }

    // What to do when a navigation item is selected
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val fragment : Fragment
        val title : Int
        when(item.itemId) {
            R.id.nav_camera -> {
                fragment = CameraFragment.newInstance(getString(R.string.menu_camera))
                title = R.string.cameraFragment
            }
            R.id.nav_gallery -> {
                fragment = GalleryFragment.newInstance(getString(R.string.menu_galeria))
                title = R.string.galleryFragment
            }
            else -> {
                fragment = HomeFragment.newInstance(getString(R.string.homeFragment))
                title = R.string.homeFragment
            }
        }

        // From AppCompatActivity
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.home_content, fragment)
            .commit()
        setTitle(getString(title))
        drawerLayout.closeDrawer(GravityCompat.START)

        return true
    }
}