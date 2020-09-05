package sk.unimobile.moj.ui.home

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_home.*
import sk.unimobile.moj.R
import sk.unimobile.moj.model.HomeMenuItem

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var recyclerView: RecyclerView

    private lateinit var homeMenuItems: List<HomeMenuItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        recyclerView = root.findViewById(R.id.home_menu)
        homeViewModel.text.value = "original value"
        val nameObserver = Observer<String> { newName ->
            // Update the UI, in this case, a TextView.
            textView.text = newName
        }

        initMenu()
        //linearLayoutManager = LinearLayoutManager(activity)
        //recyclerView.layoutManager = linearLayoutManager
        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        homeViewModel.text.observe(viewLifecycleOwner, nameObserver)

        lateText()
        return root

    }

    fun initMenu(){
        homeMenuItems = listOf(
                HomeMenuItem(getString(R.string.home_menu_payments), HomeMenuItem.HOME_MENU_PAYMENTS),
                HomeMenuItem(getString(R.string.home_menu_usage), HomeMenuItem.HOME_MENU_USAGE),
                HomeMenuItem(getString(R.string.home_menu_settings), HomeMenuItem.HOME_MENU_SETTINGS),

        )
    }

    // populate the views now that the layout has been inflated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // RecyclerView node initialized here
        home_menu.apply {
            // set a LinearLayoutManager to handle Android
            // RecyclerView behavior
            layoutManager = LinearLayoutManager(activity)
            // set the custom adapter to the RecyclerView
            adapter = HomeMenuAdapter(homeMenuItems)
        }
    }
    fun lateText(){

        Handler().postDelayed(
                {
                    homeViewModel.text.value = "new value"
                },
                5000 // value in milliseconds
        )
    }
}
