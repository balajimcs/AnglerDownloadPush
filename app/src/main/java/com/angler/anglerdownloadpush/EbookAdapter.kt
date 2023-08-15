package com.angler.anglerdownloadpush

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class EbookAdapter(context: Context, private val ebooks: List<Ebook>) : ArrayAdapter<Ebook>(context, 0, ebooks) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val ebook = getItem(position) ?: return convertView ?: View(context)

        var itemView = convertView
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_ebook, parent, false)
        }

        val bookImage: ImageView = itemView!!.findViewById(R.id.bookImage)
        val bookTitle: TextView = itemView.findViewById(R.id.bookTitle)
        val bookDescription: TextView = itemView.findViewById(R.id.bookDescription)
        val downloadButton: ImageButton = itemView.findViewById(R.id.downloadButton)

        bookImage.setImageResource(ebook.imageUrl)
        bookTitle.text = ebook.title
        bookDescription.text = ebook.description

        downloadButton.setOnClickListener {
            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_tabs, null)

            val dialog = AlertDialog.Builder(context)
                .setView(dialogView)
                .create()

            val viewPager: ViewPager2 = dialogView.findViewById(R.id.viewPager)
            val tabLayout: TabLayout = dialogView.findViewById(R.id.tabLayout)

            val fragmentAdapter = DialogFragmentAdapter(context as AppCompatActivity)
            viewPager.adapter = fragmentAdapter

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = when (position) {
                    0 -> "Download"
                    1 -> "Ebook Details"
                    else -> ""
                }
            }.attach()

            dialog.show()
        }

        return itemView
    }
}
