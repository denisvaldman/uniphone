package sk.unimobile.moj.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.testfairy.TestFairy
import sk.unimobile.moj.HashCreator
import sk.unimobile.moj.R

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TestFairy.begin(this, "ec61557a22c2f0cf667be2e77e4e1043ffe04346")
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        HashCreator.createHash(this)
    }
}