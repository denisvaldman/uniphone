package sk.unimobile.moj.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import sk.unimobile.moj.R
import sk.unimobile.moj.model.HomeMenuItem

class HomeMenuAdapter(private val list: List<HomeMenuItem>) : RecyclerView.Adapter<HomeMenuAdapter.MenuHolder>()  {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MenuHolder(inflater, parent)
    }

    override fun getItemCount(): Int = list.size


    override fun onBindViewHolder(holder: MenuHolder, position: Int) {
        val item: HomeMenuItem = list[position]
        holder.bind(item)
    }


    class MenuHolder (inflater: LayoutInflater, parent: ViewGroup) :
            RecyclerView.ViewHolder(inflater.inflate(R.layout.home_menu_item, parent, false)) {
        private var mTitleView: TextView? = null
        private var mYearView: TextView? = null


        init {
            mTitleView = itemView.findViewById(R.id.list_title)
            mYearView = itemView.findViewById(R.id.list_description)
        }

        fun bind(item: HomeMenuItem) {
            mTitleView?.text = item.getTitle()
        }
    }
}