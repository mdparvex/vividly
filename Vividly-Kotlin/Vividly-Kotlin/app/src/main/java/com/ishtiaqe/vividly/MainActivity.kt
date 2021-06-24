package com.ishtiaqe.vividly

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.ishtiaqe.vividly.fragmentGroup.MyAdapter

class MainActivity : AppCompatActivity() {

    var refUser: DatabaseReference? = null
    var firebaseUser: FirebaseUser? = null


    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUser = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)


        title = "Title is Working"
        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)
        tabLayout.addTab(tabLayout.newTab().setText("Chats"))
        tabLayout.addTab(tabLayout.newTab().setText("Search"))
        tabLayout.addTab(tabLayout.newTab().setText("Settings"))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        val adapter = MyAdapter(
            this, supportFragmentManager,
            tabLayout.tabCount
        )
        viewPager.adapter = adapter

        //display username and profile picture
        /*refUser!!.addValueEventListener(object : ValueEventListener){
            fun onDataChange(p0: DataSnapshot) {
            if(p0.exists()){
                val user: User? = p0.getvalue(Users::class.java)

                user_name.text = user!!.getUserName()
                Picasso.get().load(user.getProfile()).placeholder(R.drawable.profile).into(profile_image)

            }

        }
        }*/
        fun onCancelled(p0: DatabaseError){



        }


        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }
/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.action_logout){
        FirebaseAuth.getInstance().signOut()

        val intent = Intent(this@MainActivity, WelcomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()

        return true

    }

}

return false
*/

}