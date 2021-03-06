package com.ishtiaqe.vividly.fragmentGroup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ishtiaqe.vividly.AdapterClasses.UserAdapter
import com.ishtiaqe.vividly.ModelClasses.Users
import com.ishtiaqe.vividly.R
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"


class SearchFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var userAdapter: UserAdapter? = null
    private var mUsers: List<Users>? = null
    private var recyclerView: RecyclerView? = null
    private var searchEditText: EditText? = null
/*
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_search, container, false)
        val view: View = inflater.inflate(R.layout.fragment_search,container, false)

        recyclerView=view.findViewById(R.id.searchList)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager= LinearLayoutManager(context)
        searchEditText= view.findViewById(R.id.searchUsersET)
        mUsers= ArrayList()
        retriveAllUsers()

        searchEditText!!.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(cs: CharSequence?, start: Int, before: Int, count: Int) {
                searchForUsers(cs.toString().toLowerCase(Locale.ROOT))

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        return view

    }
    private fun retriveAllUsers(){
        val firebaseUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val refUsers=FirebaseDatabase.getInstance().reference.child("User")

        refUsers.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                (mUsers as java.util.ArrayList<Users>).clear()
                if(searchEditText!!.text.toString()=="")
                {
                    for (snapshot in p0.children){
                        val user:Users? = snapshot.getValue(Users::class.java)
                        if (!(user!!.getUID()).equals(firebaseUserID)){
                            (mUsers as ArrayList<Users>).add(user)
                        }
                    }
                    userAdapter= UserAdapter(context!!,mUsers!!, isChatCheck = false)
                    recyclerView!!.adapter=userAdapter
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
    private fun searchForUsers(str: String){
        val firebaseUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val queryUsers=FirebaseDatabase.getInstance().reference.child("Users").orderByChild("search").startAt(str).endAt(str+"\uf8ff")
        queryUsers.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                (mUsers as java.util.ArrayList<Users>).clear()
                for (snapshot in p0.children){
                    val user:Users? = snapshot.getValue(Users::class.java)
                    if (!(user!!.getUID()).equals(firebaseUserID)){
                        (mUsers as ArrayList<Users>).add(user)
                    }
                }
                userAdapter= UserAdapter(context!!,mUsers!!, isChatCheck = false)
                recyclerView!!.adapter=userAdapter

            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })


    }

    /*companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }*/
}