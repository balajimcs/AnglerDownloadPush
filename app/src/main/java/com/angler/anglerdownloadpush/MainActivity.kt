package com.angler.anglerdownloadpush

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (intent.action == Intent.ACTION_VIEW) {
            // Handle the action, e.g., navigate to a specific fragment
            navigateToDownloadFragment()
        }

        val ebooks = listOf(
            Ebook("Angler Book 1", "Partner for Innovation", R.drawable.angler1),
            Ebook("Angler Book 2", "Cutting-edge Business Products", R.drawable.angler2),
            Ebook("Angler Book 3", "Creative Design Solutions", R.drawable.angler3),
            Ebook("Angler Book 4", "Offshore Outsourcing Services", R.drawable.angler4),
            Ebook("Angler Book 5", "Web Application Development", R.drawable.angler5),
            Ebook("Angler Book 6", "Mobile App Development Services", R.drawable.angler6),
            Ebook("Angler Book 7", "Vendora – Vendor Management Software", R.drawable.angler7),
            Ebook("Angler Book 8", "TimeCheck – Time and Attendance Software", R.drawable.angler8),
            Ebook("Angler Book 9", "Store’n Shipfast – ERP for Logistics", R.drawable.angler9),
            Ebook("Angler Book 10", "Aura – Quality Management Software", R.drawable.angler10),
            Ebook("Angler Book 11", "iStore – E Commerce Store Front", R.drawable.angler11),
            Ebook("Angler Book 12", "Showtime – Mobile App for Associations", R.drawable.angler12),
            Ebook("Angler Book 13", "Showtime – Mobile App for Events", R.drawable.angler13),
            Ebook("Angler Book 14", "Saas Marketplace Platform", R.drawable.angler14),
            Ebook("Angler Book 15", "Taxi Booking Mobile App", R.drawable.angler15),
            Ebook("Angler Book 16", "Expenses & Reimbursement Management App", R.drawable.angler16),
            Ebook("Angler Book 17", "Value Added Resellers (VARs)", R.drawable.angler17),
            Ebook("Angler Book 18", "QA Automation Tools", R.drawable.angler18),
            Ebook("Angler Book 19", "Native Mobile App Development", R.drawable.angler19),
            Ebook("Angler Book 20", "Hybrid Mobile App Development", R.drawable.angler20),
            // Add more ebooks as needed
        )

        val ebookAdapter = EbookAdapter(this, ebooks)
        val listView: ListView = findViewById(R.id.listView)
        listView.adapter = ebookAdapter
    }
    private fun navigateToDownloadFragment() {
        // You can replace DownloadFragment with the fragment you want to navigate to
        val fragment = DownloadFragment.newInstance("param1", "param2")
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}