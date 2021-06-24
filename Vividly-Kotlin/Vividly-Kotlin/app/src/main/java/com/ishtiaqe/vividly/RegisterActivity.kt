package com.ishtiaqe.vividly

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var refUsers: DatabaseReference
    private var firebaseUserID: String = ""


    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar_register)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Register"
        supportActionBar!!.setDefaultDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            val intent = Intent(this@RegisterActivity, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        mAuth = FirebaseAuth.getInstance()
        register_btn.setOnClickListener {
            registerUser()


        }

    }

    private fun registerUser()
    {

        val username: String = username_register.text.toString()
        val email: String = email_register.text.toString()
        val password: String = password_register.text.toString()

        if(username == "")
        {

            TastyToast.makeText(this@RegisterActivity, "Enter Username", TastyToast.LENGTH_LONG, TastyToast.INFO).show()

        }
        else if (email == "")
        {
            TastyToast.makeText(this@RegisterActivity, "please write email.", TastyToast.LENGTH_LONG, TastyToast.INFO).show()

        }
        else if (password == "")
        {
            TastyToast.makeText(this@RegisterActivity, "please write password.", TastyToast.LENGTH_LONG, TastyToast.INFO).show()


        }

        else{
            mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener{task ->
                    if (task.isSuccessful)
                    {
                    firebaseUserID = mAuth.currentUser!!.uid
                        refUsers = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUserID)

                        val userHashMap = HashMap<String, Any>()
                        userHashMap["uid"] = firebaseUserID
                        userHashMap["username"] = username
                        userHashMap["profile"] = username
                        userHashMap["cover"] = username
                        userHashMap["status"] = "offline"
                        userHashMap["search"] = username.toLowerCase()
                        userHashMap["facebook"] = "https://m.facebook.com"
                        userHashMap["instagram"] = "https://m.instagram.com"
                        userHashMap["website"] = "https://www.google.com"

                        refUsers.updateChildren(userHashMap)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful)

                                intent = Intent(this@RegisterActivity, MainActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                                finish()

                            }

                    }
                    else
                    {
                        Toast.makeText(this@RegisterActivity, "Error Message:" + task.exception!!.message.toString(), Toast.LENGTH_LONG)

                    }
                }

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}



/*
 */