package com.ahmed.weatherapptask.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmed.weatherapptask.Adapters.SavedImagesAdapter
import com.ahmed.weatherapptask.R
import com.ahmed.weatherapptask.RoomDB.Models_DB.ImageEntity
import com.ahmed.weatherapptask.ViewModels.MainActivityViewModel
import com.ahmed.weatherapptask.databinding.FragmentSavedImagesBinding


class SavedImagesFragment : Fragment(), SavedImagesAdapter.OnItemClickListener {


    private var _binding: FragmentSavedImagesBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainActivityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(MainActivityViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSavedImagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Observe images from DB
        observerImages()
    }


    /**
     * Observe images from DB
     * */
    private fun observerImages() {
        viewModel.getAllImages().observe(
            viewLifecycleOwner,
            { images ->
                // update UI
                if (images != null) {
                    if (images.isNotEmpty()) {
                        setupRecyclerView(images)
                    } else {
                        Log.e(TAG, "observerImages: images is zero size")
                    }

                } else {
                    Log.e(TAG, "observerImages: images is null")
                }
            })
    }


    /**
     * To setup recyclerViews
     * */
    private fun setupRecyclerView(imagesList: List<ImageEntity>){
        val imagesAdapter  = SavedImagesAdapter(requireContext(), imagesList, this)
        binding.savedImagesRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = imagesAdapter
        }

    }

    /**
     * To navigate to full size image fragmnet
     * */
    private fun navigateToFullSizeImageFragment(photoPath: String) {
        val bundle = Bundle()
        bundle.putString(FullSizeImageFragment.IMAGE_PATH, photoPath)
        view?.findNavController()?.navigate(R.id.action_savedImagesFragment_to_fullSizeImageFragment, bundle)
    }


    /**
     * Call back of item clicked
     * */
    override fun onItemClick(position: Int, image: ImageEntity) {
        Log.e(TAG, "onItemClick: position $position, ImagePath: ${image.image_path}")
        navigateToFullSizeImageFragment(image.image_path!!)
    }

    companion object {
        private const val TAG = "SavedImagesFragment"
    }
}