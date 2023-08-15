package com.angler.anglerdownloadpush

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.FileProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import retrofit2.Response
import java.io.FileOutputStream

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DownloadFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DownloadFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val NOTIFICATION_ID = 123

    private lateinit var progressBar: ProgressBar
    private val baseUrl = "https://www.tutorialkart.com"
    private var downloadProgress: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_download, container, false)
        progressBar = view.findViewById(R.id.progressBar)
        val startDownloadButton: Button = view.findViewById(R.id.startDownloadButton)

        startDownloadButton.setOnClickListener {
            startDownload()
        }

        return view
    }

    private fun startDownload() {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.downloadPdf()
            if (response != null) {
                savePdf(response)
            }
        }
    }

    private fun savePdf(responseBody: ResponseBody?) {
        responseBody?.let {
            val inputStream = it.byteStream()
            val contentLength = it.contentLength()

            val notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val notificationBuilder = NotificationCompat.Builder(requireContext(), "download_channel_id")
                .setContentTitle("File Download")
                .setContentText("Download in progress")
                .setSmallIcon(R.drawable.ic_notification)
                .setProgress(contentLength.toInt(), 0, false) // Initial progress
                .setPriority(NotificationCompat.PRIORITY_LOW)
            val outputDirectory = requireContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
            val outputFile = File(outputDirectory, "anglerpdf.pdf")

            val outputStream = FileOutputStream(outputFile)
            val buffer = ByteArray(4096)
            var bytesRead: Int
            var totalBytesRead: Long = 0

            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
                totalBytesRead += bytesRead
                val progress = (totalBytesRead * 100 / contentLength).toInt()
                val notificationIntent = Intent(requireContext(), MainActivity::class.java)
                val pendingIntent = PendingIntent.getActivity(
                    requireContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                notificationBuilder.setContentIntent(pendingIntent)
                notificationBuilder.setSmallIcon(R.drawable.ic_notification)
                notificationBuilder.setProgress(contentLength.toInt(), progress, false)
                notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
                updateProgressBar(progress)

                // Save progress to SharedPreferences
                saveProgressToSharedPreferences(progress)
            }

            outputStream.flush()
            outputStream.close()

            notificationBuilder
                .setContentText("Download complete")
                .setProgress(0, 0, false)
            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())

            updateProgressBar(0)
            openPdfFile(outputFile)// Download completed
        }
    }

    private fun openPdfFile(pdfFile: File) {
        val uri = FileProvider.getUriForFile(
            requireContext(),
            "com.angler.anglerdownloadpush.provider",
            pdfFile
        )

        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "application/pdf")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // Handle the case where no PDF viewer app is installed
        }
    }


    private fun saveProgressToSharedPreferences(progress: Int) {
        val sharedPreferences = requireContext().getSharedPreferences("DownloadPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("downloadProgress", progress)
        editor.apply()
    }

    private fun updateProgressBar(progress: Int) {
        requireActivity().runOnUiThread {
            progressBar.progress = progress
        }
    }

    override fun onResume() {
        super.onResume()
        // Retrieve stored progress from SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("DownloadPrefs", Context.MODE_PRIVATE)
        downloadProgress = sharedPreferences.getInt("downloadProgress", 0)
        updateProgressBar(downloadProgress)
    }





    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DownloadFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DownloadFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}