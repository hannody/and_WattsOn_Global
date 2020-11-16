package com.abunayla.wattson.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.abunayla.wattson.R
import com.abunayla.wattson.ui.dialogs.AboutMeDialogFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.about_me, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val dialog = AboutMeDialogFragment()
        dialog.show(supportFragmentManager, "")
        return super.onOptionsItemSelected(item)
    }

}