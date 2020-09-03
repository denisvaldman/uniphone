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
import sk.unimobile.moj.R

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel



    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)

        homeViewModel.text.value = "original value"
        val nameObserver = Observer<String> { newName ->
            // Update the UI, in this case, a TextView.
            textView.text = newName
        }

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        homeViewModel.text.observe(viewLifecycleOwner, nameObserver)


        lateText()
        return root


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
