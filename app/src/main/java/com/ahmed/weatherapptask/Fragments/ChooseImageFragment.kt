package com.ahmed.weatherapptask.Fragments

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ahmed.weatherapptask.DataModels.WeatherDataModel
import com.ahmed.weatherapptask.R
import com.ahmed.weatherapptask.Utils.PermissionManager
import com.ahmed.weatherapptask.ViewModels.MainActivityViewModel
import com.ahmed.weatherapptask.databinding.EditSaveButtonsBinding
import com.ahmed.weatherapptask.databinding.FragmentChooseImageBinding
import com.ahmed.weatherapptask.databinding.OverlayWeatherDataViewBinding
import com.bumptech.glide.Glide
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import java.io.File


class ChooseImageFragment : Fragment() {


    //Binding
    private var _binding: FragmentChooseImageBinding? = null
    private var editSaveButtonsBinding: EditSaveButtonsBinding? = null
    private var overlayBinding: OverlayWeatherDataViewBinding? = null
    private val binding get() = _binding!!
    //viewModel
    private lateinit var viewModel: MainActivityViewModel
    //permissionManager
    private lateinit var permissionManager: PermissionManager


    private var selectedImagePath: String? = null
    private var selectedImageName: String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Initialize viewModel
        viewModel = ViewModelProvider(requireActivity()).get(MainActivityViewModel::class.java)

        //Initialize permissionManager
        permissionManager = PermissionManager()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseImageBinding.inflate(inflater, container, false)
        editSaveButtonsBinding = _binding?.editDoneLayout
        overlayBinding = _binding?.overlayLayout

        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //handle btns visibility
        hideOrShowSaveEditButtons(false)

        //handle overlay visibility
        hideOrShowOverlay(false)

        //choose image
        onChooseImageClicked()

        //Observe WeatherData
        observerWeatherData()



    }


    /**
     * To initialize Location
     * */
    private fun initUserLocation() {
        if (isLocationPermissionGranted()) {
            viewModel.initUserLocation()
        } else {
            requestLocationPermission()
        }
    }


    /**
     * Request permissions
     * Open Gallery to Choose Images
     * */
    private fun onChooseImageClicked() {
        binding.btnChooseImage.setOnClickListener { chooseImage() }
        editSaveButtonsBinding?.btnEdit?.setOnClickListener { chooseImage() }
        editSaveButtonsBinding?.btnDone?.setOnClickListener { saveImage() }
        binding.btnShowSavedImages.setOnClickListener { navigateToSavedImagesFragment() }

    }


    /**
     * To navigate to saved Images Fragment
     * */

    private fun navigateToSavedImagesFragment(){
        findNavController().navigate(R.id.action_chooseImageFragment_to_savedImagesFragment2)
    }


    /**
     * To choose image or capture
     * */
    private fun chooseImage() {
        if (requestAllPermission()) {
            ImagePicker.with(this)
                .setFolderMode(true)
                .setFolderTitle("Album")
                .setRootDirectoryName(Config.ROOT_DIR_DCIM)
                .setDirectoryName("WeatherImages")
                .setMultipleMode(true)
                .setShowNumberIndicator(true)
                .setMaxSize(1)
                .setLimitMessage("You can select up to 1 images")
                //.setSelectedImages(images)
                .setRequestCode(100)
                .start()
        }
    }




    /**
     * To Load the selectedImage into the ImageView
     * */
    private fun loadImage(image: Image) {
        Glide.with(requireContext())
            .load(image.path)
            .into(_binding!!.img)
    }



    /**
     * Observe WeatherData
     * */
    private fun observerWeatherData(){
        viewModel.getWeatherData().observe(viewLifecycleOwner, { weatherData ->
            Log.e(TAG, "observerWeatherData: weatherData $weatherData" )

            setViews(weatherData)

            val bitmap: Bitmap = viewModel.convertViewToBitmap(binding.photoLayout)

            viewModel.replaceImage(bitmap, selectedImagePath!!)
        })
    }


    /**
     * To set views with weather data
     * */
    private fun setViews(weatherData:WeatherDataModel){
        overlayBinding?.tvTemp?.text = "%s°".format(weatherData.temp)
        overlayBinding?.tvHighLow?.text = weatherData.tempHL
        overlayBinding?.tvWeatherCon?.text = weatherData.weather_status
        overlayBinding?.tvPlace?.text = weatherData.location
        Glide.with(requireContext())
            .load(weatherData.icon)
            .into(overlayBinding!!.imgIcon)
    }


    /**
     * Insert selected image into DB
     * */
    private fun insertImage(image: Image) {
        viewModel.insertImage(image)
    }

    /**
     * To save image in live data and listen to file changes
     * */
    private fun saveImage() {

        //To get the user location
        initUserLocation()

        //Initialize image path in live data
        viewModel.setImagePathLiveData(selectedImagePath!!)

        //Listener on File changes
        observeFileChanges(File(selectedImagePath!!))
    }


    /**
     * Listener on File changes
     * */
    lateinit var observer: FileObserver
    private fun observeFileChanges(photoPath: File) {

        Log.d(TAG, "FileObserver Created")
        observer = object : FileObserver(photoPath) {
            override fun onEvent(event: Int, file: String?) {
                Handler(Looper.getMainLooper()).post {
                    //  hideLoading()
                    if (event == CLOSE_WRITE) {
                        Log.e(TAG, "onEvent: FileUpdated")
                        findNavController().navigate(R.id.action_chooseImageFragment_to_shareImageFragment)
                    }
                }
            }
        }

        observer.startWatching() //START OBSERVING
    }






//*************************************************************************************************************\\
//*                                                                                                             *\\
//*                                         Handle View  visibility                                               *\\
//*                                                                                                                 *\\
//**********************************************************************************************************************/
    /**
     * To show or hide save and edit buttons
     * */
    private fun hideOrShowSaveEditButtons(isImageChosen: Boolean) {
        if (isImageChosen) {
            _binding?.btnChooseImage?.visibility = View.GONE
            _binding?.btnShowSavedImages?.visibility = View.GONE

            editSaveButtonsBinding?.editDoneLayout?.visibility = View.VISIBLE
           // overlayBinding?.overlayLayout?.visibility = View.VISIBLE

        } else {
            _binding?.btnChooseImage?.visibility = View.VISIBLE
            editSaveButtonsBinding?.editDoneLayout?.visibility = View.GONE
           // overlayBinding?.overlayLayout?.visibility = View.GONE
        }

    }

    /**
     * To show or hide overlay
     * */
    private fun hideOrShowOverlay(bool: Boolean) {
        if(bool){
             overlayBinding?.overlayLayout?.visibility = View.VISIBLE
        }else{
            overlayBinding?.overlayLayout?.visibility = View.GONE
        }
    }




//*************************************************************************************************************\\
//*                                                                                                             *\\
//*                                         Handle Permissions                                                   *\\
//*                                                                                                                 *\\
//**********************************************************************************************************************/

    /**
     * @return true if location permission is granted
     * */
    private fun isLocationPermissionGranted(): Boolean {
        return permissionManager.isPermissionGranted(this, Manifest.permission.ACCESS_FINE_LOCATION)
    }

    /**
     * To request Location permission
     * */
    private fun requestLocationPermission() {
        permissionManager.requestPermissionLocationPermission(this)
    }

    /**
     * To Request Needed Permissions
     * */
    private fun requestAllPermission(): Boolean {

        val permissions =
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)

        return if (permissionManager.isAllPermissionGranted(this, permissions)) {
            true
        } else {
            permissionManager.checkForPermissions(this, permissions)
            false
        }
    }


//*************************************************************************************************************\\
//*                                                                                                             *\\
//*                                        Call back onActivityResult                                               *\\
//*                                                                                                                 *\\
//**********************************************************************************************************************/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (ImagePicker.shouldHandleResult(requestCode, resultCode, data, 100)) {
            val images: ArrayList<Image> = ImagePicker.getImages(data)

            //load the image
            if (images.size > 0) {
                val firstImage = images[0]

                selectedImagePath = firstImage.path
                selectedImageName = firstImage.name

                //Load the selected image into ImageView
                loadImage(firstImage)

                //Insert selected into DB
                insertImage(firstImage)

                //To show or hide save and edit buttons
                hideOrShowSaveEditButtons(true)

                hideOrShowOverlay(true)



            }


        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        //Release binding
        _binding = null
        editSaveButtonsBinding = null
        overlayBinding = null
    }

    companion object {
        private const val TAG = "ChooseImageFragment"
    }
}