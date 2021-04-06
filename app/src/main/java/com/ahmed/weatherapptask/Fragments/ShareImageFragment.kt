package com.ahmed.weatherapptask.Fragments

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ahmed.weatherapptask.ViewModels.MainActivityViewModel
import com.ahmed.weatherapptask.databinding.FragmentShareImageBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import java.io.File
import java.net.URI


class ShareImageFragment : Fragment() {

    //Binding
    private var _binding: FragmentShareImageBinding? = null
    private val binding get() = _binding!!
    //viewModel
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(MainActivityViewModel::class.java)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentShareImageBinding.inflate(inflater, container, false)



        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //observe ModifiedImage path
        observeModifiedImage()

        //handle btn clicked
        onBtnClicked()
    }


    /**
     * Handle btn clicked
     * */
    private fun onBtnClicked() {
        binding.btnShare.setOnClickListener { shareImage() }
    }


    /**
     * Observe ModifiedImage path
     * */
    private fun observeModifiedImage() {
        viewModel.getImagePath().observe(viewLifecycleOwner, { imagePath ->
            if (imagePath != null) {
                loadImage("file://$imagePath")
            }

        })
    }





    /**
     * To Load modified image into modifiedImgView
     * */
    private fun loadImage(imagePath: String) {
        Glide.with(requireContext())
            .load(imagePath)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(binding.modifiedImg)
    }


    /**
     * To Share Image via Apps
     * */
    private fun shareImage() {
        val intent = Intent()
        val chooser = Intent.createChooser(intent, "Share Image")

        try {
            val file =
                File(URI.create("file://${viewModel.getImagePath().value}"))
            val uriFile: Uri = FileProvider.getUriForFile(
                requireContext().applicationContext,
                "com.ahmed.photoweatherapptask.fileprovider", file
            )
            Log.d(TAG, "URI FILE: $uriFile")
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_STREAM, uriFile)
            intent.type = "*/*"
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            val resInfoList = requireContext().packageManager.queryIntentActivities(
                chooser,
                PackageManager.MATCH_DEFAULT_ONLY
            )
            for (resolveInfo in resInfoList) {
                val packageName = resolveInfo.activityInfo.packageName
                requireContext().grantUriPermission(
                    packageName,
                    uriFile,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
            startActivity(chooser)
        } catch (e: NullPointerException) {
            Log.e(TAG, "share_image: " + e.message)
        }
    }


    companion object {
        private const val TAG = "ShareImageFragment"

    }
}