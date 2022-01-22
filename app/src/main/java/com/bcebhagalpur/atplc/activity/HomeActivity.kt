package com.bcebhagalpur.atplc.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.bcebhagalpur.atplc.BuildConfig
import com.bcebhagalpur.atplc.R
import com.bcebhagalpur.atplc.fragment.*
import com.bcebhagalpur.atplc.model.Students
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.lang.IllegalStateException
import java.lang.NullPointerException
import java.util.*

class HomeActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var coordinatorLayout: CoordinatorLayout
    private lateinit var toolBar: androidx.appcompat.widget.Toolbar
    private lateinit var frameLayout: FrameLayout
    private lateinit var navigationView: NavigationView
    private lateinit var txtName: TextView
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var mAppBarConfiguration: AppBarConfiguration
    private lateinit var  navController:NavController

    private lateinit var homeFragment: HomeFragment
    private lateinit var profileFragment: ProfielFragment
    private lateinit var studentFragment: StudentFragment
    private var previousMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        bottomNavigation=findViewById(R.id.bottom_navigation)
        myHomeFragment()
        bottom()

        toolBar = findViewById(R.id.toolbar)
        setSupportActionBar(toolBar)
        Objects.requireNonNull(supportActionBar)!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        drawerLayout = findViewById(R.id.navigation_layout)
        navigationView = findViewById(R.id.navigation_view)


        mAppBarConfiguration =  AppBarConfiguration.Builder(
            R.id.home, R.id.student, R.id.profile
        )
        .setDrawerLayout(drawerLayout)
            .build()
        try {
            navController = Navigation.findNavController(this, R.id.nav_host_fragment)
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration)
            NavigationUI.setupWithNavController(navigationView, navController)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            bottomNavigation = findViewById(R.id.bottom_navigation)
            NavigationUI.setupWithNavController(bottomNavigation, navController)

            val navView = navigationView.inflateHeaderView(R.layout.item_header)

            txtName = navView.findViewById(R.id.txtName)

            //Handle visibility of the application bottom navigation
            navController.addOnDestinationChangedListener { _, destination, _ ->
                if (destination.id == R.id.home || destination.id == R.id.feedBack
                    || destination.id == R.id.aboutUs || destination.id == R.id.contactUs || destination.id == R.id.qna
                ) {
                    bottomNavigation.visibility = View.GONE
                } else {
                    bottomNavigation.visibility = View.VISIBLE
                }
            }
        }catch (e:IllegalStateException){
            e.printStackTrace()
        }

        var previousMenuItem: MenuItem? = null

        setUpToolbar()

        openHome()
        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@HomeActivity
            , drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()


        navigationView.setNavigationItemSelectedListener{


                if (previousMenuItem!=null){
                    previousMenuItem?.isChecked=false
                }
                it.isCheckable=true
                it.isChecked=true
                previousMenuItem=it

                when(it.itemId){

                    R.id.home ->{ openHome()
                        drawerLayout.closeDrawers()
                    }

                    R.id.qna ->{

                        supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment,QnaFragment()).commit()
                        supportActionBar?.title="Question and answer"
                        drawerLayout.closeDrawers()

                    }

                    R.id.feedBack ->{

                        supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment,FeedbackFragment()).commit()
                        supportActionBar?.title="Feedback"
                        drawerLayout.closeDrawers()

                    }
                    R.id.aboutUs ->{

                        supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment,AboutUsFragment()).commit()
                        supportActionBar?.title="About Us"
                        drawerLayout.closeDrawers()

                    }
                    R.id.contactUs ->{

                        supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment,ContactUsFragment()).commit()
                        supportActionBar?.title="Contact Us"
                        drawerLayout.closeDrawers()

                    }
                    R.id.share ->{
                        try {
                            val shareIntent = Intent(Intent.ACTION_SEND)
                            shareIntent.type = "text/plain"
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name")
                            var shareMessage =
                                "\nLet me recommend you CheAshu which help you to find the rooms on rent\n\n"
                            shareMessage =
                                """
                            ${shareMessage}https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}
                            """.trimIndent()
                            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                            startActivity(Intent.createChooser(shareIntent, "choose one"))
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        drawerLayout.closeDrawers()
                    }

                    R.id.rateUs ->{
                        try{
                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
                        }
                        catch (e: ActivityNotFoundException){
                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=$packageName")))
                        }
                        drawerLayout.closeDrawers()
                    }
                    R.id.logOut ->{
                        logOut()
                        drawerLayout.closeDrawers()
                    }
                }

                return@setNavigationItemSelectedListener true

            }

        }

    override fun onSupportNavigateUp(): Boolean {
            try {
                return (NavigationUI.navigateUp(
                    Navigation.findNavController(
                        this,
                        R.id.nav_host_fragment
                    ), mAppBarConfiguration
                )
                        || super.onSupportNavigateUp())
            }catch (e:Exception){
                e.printStackTrace()
            }
        return false
    }

    private fun setUpToolbar(){
        setSupportActionBar(toolBar)
        supportActionBar?.title="Toolbar Title"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id= item.itemId
        if(id==android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }
    private fun openHome(){
        val fragment= HomeFragment()
        val transaction=supportFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_host_fragment,fragment)
        transaction.commit()
        supportActionBar?.title="ATPLC"
        navigationView.setCheckedItem(R.id.home)
    }

    override fun onBackPressed() {
        drawerLayout.closeDrawers()
        when(supportFragmentManager.findFragmentById(R.id.nav_host_fragment)){
            !is HomeFragment ->openHome()
            else ->super.onBackPressed()
        }
    }

    private fun bottom(){
       bottomNavigation.setOnNavigationItemSelectedListener {

            if (previousMenuItem != null){
                previousMenuItem?.isChecked = false
            }

            it.isCheckable = true
            it.isChecked = true
            previousMenuItem = it

            when(it.itemId){
                R.id.home ->{
                    myHomeFragment()
                }
                R.id.student ->{
                    studentFragment= StudentFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment,studentFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
                    supportActionBar?.title = "Students"
                }
                R.id.profile ->{
                    profileFragment= ProfielFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment,profileFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
                    supportActionBar?.title="Profile"
                }
            }
            true
        }
    }
    @SuppressLint("ResourceAsColor")
    private fun myHomeFragment(){
        homeFragment = HomeFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, homeFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
        supportActionBar?.title ="ATPLC"
        supportActionBar?.show()
    }
    private fun logOut(){
        val students = Students()
        val mAlertDialogBuilder = AlertDialog.Builder(this)
        mAlertDialogBuilder.setTitle("Message")
        mAlertDialogBuilder.setMessage("This operation erase your all data! are you sure to logOut")
        mAlertDialogBuilder.setCancelable(false)
        mAlertDialogBuilder.setPositiveButton("Ok"){_,_->
            val storageRef = FirebaseStorage.getInstance().getReference("profile images")
            val desertRef = storageRef.child(students.iPath.toString())
            desertRef.delete().addOnSuccessListener {
            }.addOnFailureListener {
                it.printStackTrace()
            }
            val myDatabase= FirebaseDatabase.getInstance().getReference("STUDENTS")
            try {
                val myDatabase3 = myDatabase.child(FirebaseAuth.getInstance().currentUser!!.uid)
                myDatabase3.removeValue()
            }catch (e: NullPointerException){
                e.printStackTrace()
            }
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            Toast.makeText(this,"logged Out successfully", Toast.LENGTH_SHORT).show()

        }
        mAlertDialogBuilder.setNegativeButton("cancel"){_,_->
            Toast.makeText(this,"You cancel the operation", Toast.LENGTH_SHORT).show()
        }
        mAlertDialogBuilder.create().show()
    }

}


