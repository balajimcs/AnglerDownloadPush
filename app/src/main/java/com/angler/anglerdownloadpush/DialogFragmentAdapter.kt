package com.angler.anglerdownloadpush

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class DialogFragmentAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2 // Number of tabs

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DownloadFragment()
            1 -> EbookDetailsFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}
