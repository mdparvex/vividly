package com.ishtiaqe.vividly.AdapterClasses
import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.ishtiaqe.vividly.ModelClasses.Users
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.ishtiaqe.vividly.R
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.user_search_item_layout.view.*
import java.nio.file.attribute.UserPrincipalLookupService

class UserAdapter(
    mContext: Context,
    mUsers:List<Users>,
    isChatCheck:Boolean)
    :RecyclerView.Adapter<UserAdapter.viewHolder?>()
{
    private val mContext: Context
    private val mUsers: List<Users>
    private var isChatCheck: Boolean

    init {
        this.mUsers=mUsers
        this.mContext=mContext
        this.isChatCheck=isChatCheck
    }

    override fun onCreateViewHolder(ViewGroup: ViewGroup, viewType: Int): UserAdapter.viewHolder {
        val view: View= LayoutInflater.from(mContext).inflate(R.layout.user_search_item_layout, ViewGroup, false)
        return UserAdapter.viewHolder(view)

    }

    override fun onBindViewHolder(holder: UserAdapter.viewHolder, i: Int) {
        val user:Users=mUsers[i]
        holder.userNameText.text= user.getUserName()
        Picasso.get().load(user.getProfile()).placeholder(R.drawable.ic_profile).into(holder.profileImageView)

    }

    override fun getItemCount(): Int {
        return mUsers.size

    }
        class viewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            var userNameText: TextView
            var profileImageView: CircleImageView
            var onlineImageView: CircleImageView
            var ofllineImageView: CircleImageView
            var lastMessageImageView: TextView

            init {
                userNameText = itemView.findViewById(R.id.username)
                profileImageView = itemView.findViewById(R.id.profile_image)
                onlineImageView = itemView.findViewById(R.id.image_online)
                ofllineImageView = itemView.findViewById(R.id.image_offline)
                lastMessageImageView = itemView.findViewById(R.id.message_last)
            }
        }

}