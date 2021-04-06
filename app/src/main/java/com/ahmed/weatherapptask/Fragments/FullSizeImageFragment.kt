package com.ahmed.weatherapptask.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ahmed.weatherapptask.R
import com.ahmed.weatherapptask.databinding.FragmentChooseImageBinding
import com.ahmed.weatherapptask.databinding.FragmentFullSizeImageBinding
import com.bumptech.glide.Glide




/**
 * A simple [Fragment] subclass.
 * Use the [FullSizeImageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FullSizeImageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var imagePath: String? = null


    private var _binding: FragmentFullSizeImageBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imagePath = it.getString(IMAGE_PATH)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFullSizeImageBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //To load imagePath  into image
        loadImage()
    }

    /**
     * To load imagePath from argument  into image
     * */
    private fun loadImage(){
        Glide.with(requireContext())
            .load(imagePath)
            .into(binding.imgFullSize)
    }

    companion object {
        public const val IMAGE_PATH = "param1"
        @JvmStatic
        fun newInstance(param1: String) =
            FullSizeImageFragment().apply {
                arguments = Bundle().apply {
                    putString(IMAGE_PATH, param1)
                }
            }
    }
}