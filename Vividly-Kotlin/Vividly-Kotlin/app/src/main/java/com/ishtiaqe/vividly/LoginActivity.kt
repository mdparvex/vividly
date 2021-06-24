package com.ishtiaqe.vividly

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar_login)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Login"
        //supportActionBar!!.setDefaultDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {

            //Line is fixed.
            val intent = Intent(this@LoginActivity, WelcomeActivity::class.java)
            startActivity(intent)

            finish()

        }
        mAuth = FirebaseAuth.getInstance()
        login_btn.setOnClickListener{
            loginUser()

        }




    }

    private fun loginUser() {
        val email: String = email_login.text.toString()
        val password: String = password_login.text.toString()

         if (email == "")
        {
            TastyToast.makeText(this@LoginActivity, "please write email.", TastyToast.LENGTH_LONG, TastyToast.INFO).show()

        }
        else if (password == "")
        {
            TastyToast.makeText(this@LoginActivity, "please write password.", TastyToast.LENGTH_LONG, TastyToast.INFO).show()


        }
        else {

            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()

                    }
                 else{

                        Toast.makeText(this@LoginActivity, "Error Message:" + task.exception!!.message.toString(), Toast.LENGTH_LONG)

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