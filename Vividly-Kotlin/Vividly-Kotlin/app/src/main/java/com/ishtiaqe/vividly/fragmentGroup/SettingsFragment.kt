package com.ishtiaqe.vividly.fragments

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.ishtiaqe.vividly.ModelClasses.Users
import com.ishtiaqe.vividly.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_settings.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    var userReference:DatabaseReference? = null
    var FirebaseUser:FirebaseUser? = null
    private val RequestCode = 438
    private var imageUri: Uri?=null
    private var storageRef: StorageReference?=null
    private var coverChecker: String? =""
    private var socialChecker: String? =""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        FirebaseUser = FirebaseAuth.getInstance().currentUser
        userReference= FirebaseDatabase.getInstance().reference.child("Users").child(FirebaseUser!!.uid)
        storageRef=FirebaseStorage.getInstance().reference.child("User images")
        userReference!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists())
                {
                    val user: Users?=p0.getValue(Users::class.java)
                    if (context!=null)
                    {
                        view.username_settings.text= user!!.getUserName()
                        Picasso.get().load(user.getProfile()).into(view.profile_image_settings)
                        Picasso.get().load(user.getCover()).into(view.cover_image_settings)
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        view.profile_image_settings.setOnClickListener {
            pickImage()
        }
        view.cover_image_settings.setOnClickListener {
            coverChecker ="cover"
            pickImage()
        }
        view.set_facebook.setOnClickListener {
            socialChecker="facebook"
            setSocialLink()
        }
        view.set_instagram.setOnClickListener {
            socialChecker="instagram"
            setSocialLink()
        }
        view.set_website.setOnClickListener {
            socialChecker="website"
            setSocialLink()
        }


        return view
    }
    private fun setSocialLink(){
        val builder: AlertDialog.Builder= AlertDialog.Builder(context!!, R.style.Theme_AppCompat_DayNight_Dialog_Alert)

        if (socialChecker=="website")
        {
            builder.setTitle(("write URL:"))
        }
        else
        {
            builder.setTitle(("write Username"))
        }
        val editTex= EditText(context)
        if (socialChecker=="website")
        {
            editTex.hint= "e.g www.google.com"
        }
        else
        {
            editTex.hint= "e.g parvez421"
        }
        builder.setView(editTex)
        builder.setPositiveButton("create", DialogInterface.OnClickListener { dialog, which ->
            val str = editTex.text.toString()
            if (str=="")
            {
                Toast.makeText(context,"please write something....", Toast.LENGTH_LONG).show()
            }
            else
            {
                saveSocialLink(str)
            }
        })
        builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
            dialog.cancel()

        })
        builder.show()
    }

    private fun saveSocialLink(str: String) {
        val mapSocial = HashMap<String,Any>()
        when(socialChecker)
        {
            "facebook" ->
            {
                mapSocial["facebook"]= "https://m.facebook.com/$str"
            }
            "instagram" ->
            {
                mapSocial["instagram"]= "https://m.facebook.com/$str"
            }
            "website" ->
            {
                mapSocial["instagram"]= "https://$str"
            }
        }

        userReference!!.updateChildren(mapSocial).addOnCompleteListener({
            task ->
            if (task.isSuccessful)
            {
                Toast.makeText(context,"updated successfully", Toast.LENGTH_LONG).show()
            }
        })

    }


    private fun pickImage(){
        val intent=Intent()
        intent.type="image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent,RequestCode)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode== RequestCode && requestCode== Activity.RESULT_OK && data!!.data!=null){
            imageUri=data.data
            Toast.makeText(context,"uploading....", Toast.LENGTH_LONG).show()
            uploadImageToDatabase()
        }
    }
    private fun uploadImageToDatabase(){
        val progressBar= ProgressDialog(context)
        progressBar.setMessage("photo is ploading wait....")
        progressBar.show()

        if(imageUri!=null){
            val fileRef= storageRef!!.child(System.currentTimeMillis().toString()+".jpg")
            var uploadTask: StorageTask<*>
            uploadTask = fileRef.putFile((imageUri!!))
            uploadTask.continueWithTask(Continuation <UploadTask.TaskSnapshot, Task<Uri>>{ task ->
            if (!task.isSuccessful){
                task.exception?.let {
                    throw it
                }

            }
                return@Continuation fileRef.downloadUrl
            }).addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    val downloadurl = task.result
                    val url = downloadurl.toString()
                    if (coverChecker=="cover")
                    {
                        val mapCoverImg = HashMap<String,Any>()
                        mapCoverImg["cover"]= url
                        userReference!!.updateChildren(mapCoverImg)
                        coverChecker=""
                    }
                    else
                    {
                        val mapProfileImg = HashMap<String,Any>()
                        mapProfileImg["cover"]= url
                        userReference!!.updateChildren(mapProfileImg)
                        coverChecker=""
                    }
                    progressBar.dismiss()
                }

            }
        }

    }

}