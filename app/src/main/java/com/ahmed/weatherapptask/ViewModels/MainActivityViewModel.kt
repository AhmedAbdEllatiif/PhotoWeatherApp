package com.ahmed.weatherapptask.ViewModels

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ahmed.weatherapptask.Api.ApiHelpers.WeatherResponseHelper
import com.ahmed.weatherapptask.Api.ApiManager
import com.ahmed.weatherapptask.Api.ApiRepository
import com.ahmed.weatherapptask.Api.WeatherApi.WeatherResponse
import com.ahmed.weatherapptask.Api.WeatherApi.WeatherResponse.Weather
import com.ahmed.weatherapptask.DataModels.WeatherDataModel
import com.ahmed.weatherapptask.RoomDB.Models_DB.ImageEntity
import com.ahmed.weatherapptask.RoomDB.Repos.ImageRepository
import com.ahmed.weatherapptask.Utils.AccessUserLocationHelper
import com.ahmed.weatherapptask.Utils.LocationProvider
import com.nguyenhoanglam.imagepicker.model.Image
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.lang.String.format


class MainActivityViewModel(application: Application) : AndroidViewModel(application),
    AccessUserLocationHelper, WeatherResponseHelper {


    //init modifiedImagePath live date
    private var modifiedImagePath: MutableLiveData<String> = MutableLiveData<String>()

    //init WeatherDataModel live date
    private var weatherDataModel: MutableLiveData<WeatherDataModel> = MutableLiveData<WeatherDataModel>()

    //init location provider
    private var locationProvider = LocationProvider(application,this)

    //init image repository
    private val imageRepo: ImageRepository = ImageRepository(application)

    //init Api repository
    private val api: ApiRepository = ApiRepository()




    /**
     * To init current user location
     * */
    fun initUserLocation(): Location? {
        return locationProvider.initCurrentLocation()
    }

    /**
     * To convert form Kelvin to Celsius
     * */
    private fun convertKelvinToCelsius(temp: Double?):Int{
        if (temp != null) {
            return temp.toInt()  - 273.15.toInt()
        }
        return 0
    }

    //*************************************************************************************************************\\
//*                                                                                                             *\\
//*                                           Calling Api                                                         *\\
//*                                                                                                                 *\\
//**********************************************************************************************************************/
    private fun getWeatherData(lat: String, lon: String){
        api.getWeatherData(lat, lon,this)
    }



//*************************************************************************************************************\\
//*                                                                                                             *\\
//*                                 Converting original image to weather image                                    *\\
//*                                                                                                                 *\\
//**********************************************************************************************************************/
    /**
     * To create a new file if not already exists
     * */
    private fun createNewFile(modifiedImage: File) {
        try {
            val wasCreated = modifiedImage.createNewFile()
            if (!wasCreated) {
                Log.d(TAG, "createNewFile: Failed to create directory")
            } else {
                Log.d(TAG, "createNewFile: wasCreated passed")
            }
        } catch (e: IOException) {
            Log.e(TAG, "createNewFile: IOException >> " + e.message)
        }
    }


    /**
     * To convert view
     * @param v
     * to bitmap
     * */
    fun convertViewToBitmap(v: ConstraintLayout): Bitmap {
        val b: Bitmap = Bitmap.createBitmap(v.width, v.height, Bitmap.Config.RGB_565)
        val c = Canvas(b)
        v.draw(c)
        return b
    }

    /**
     * To replace the original image with the new bitmap
     * */
    fun replaceImage(capturedImageBitmap: Bitmap?, imgPath: String) {
        val newBitmap = capturedImageBitmap?.copy(Bitmap.Config.ARGB_8888, true)
        val newModifiedCapturedImage = File(imgPath)
        Log.e(TAG, "Data: newModifiedCapturedImage: $newModifiedCapturedImage")

        if (!newModifiedCapturedImage.exists()) {
            createNewFile(newModifiedCapturedImage)
        } else {
            var outputStream: FileOutputStream? = null
            try {
                Log.e(TAG, "replaceImage: Start try >>")
                outputStream = FileOutputStream(newModifiedCapturedImage)
                newBitmap?.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            } catch (e: FileNotFoundException) {
                Log.e(TAG, "replaceImage: FileNotFoundException >> " + e.message)
                // Log.e(TAG, e.getLocalizedMessage());
            } finally {
                if (outputStream != null) {
                    try {
                        Log.e(TAG, "replaceImage: Start try_2")
                        outputStream.flush()
                        outputStream.fd.sync()
                        outputStream.close()
                    } catch (e: IOException) {
                        Log.e(TAG, "replaceImage: IOException_2 >> " + e.message)
                        //Log.e(TAG, e.getLocalizedMessage());
                    }
                } else Log.e(TAG, "replaceImage: outputStream is null")
            }
        }

    }


//*************************************************************************************************************\\
//*                                                                                                             *\\
//*                                           Api CallBacks                                                            *\\
//*                                                                                                                 *\\
//**********************************************************************************************************************/
    override fun onWeatherReady(weatherResponse: WeatherResponse) {
        //Prepare Data
        val weatherStatus: String =  if (weatherResponse.weather.isNotEmpty())weatherResponse.weather[0].main else ""
        val icon: String =  if (weatherResponse.weather.isNotEmpty())weatherResponse.weather[0].icon else ""
        val currentTemp:String = convertKelvinToCelsius(weatherResponse.main?.temp).toString()
        val tempMax:String = convertKelvinToCelsius(weatherResponse.main?.temp_max).toString()
        val tempMin:String = convertKelvinToCelsius(weatherResponse.main?.temp_min).toString()
        val country:String? = weatherResponse.sys?.country
        val region:String? = weatherResponse.name
        val tempHL = "$tempMin°/$tempMax°"
    // "pi = %.2f".format(pi)
        //Location name
        //val locationName = String.format("$, $",country,region)
        val locationName = "$country, $region"

    Log.e(TAG, "onWeatherReady: temp_max: ${weatherResponse.main?.temp_max}")
    Log.e(TAG, "onWeatherReady: temp_min: ${weatherResponse.main?.temp_min}")

        //Initialize new WeatherDataModel
        val weatherData = WeatherDataModel(
            temp = currentTemp,
            temp_max = tempMax,
            temp_min = tempMin,
            location = locationName,
            tempHL = tempHL,
            icon = "${ApiManager.baseIconUrl}@2x$icon.png",
            weather_status = weatherStatus
        )

        //Add WeatherDataModel to live data
        weatherDataModel.value = weatherData

    }

    override fun onErrorBodyWeather(errorBody: String) {
        Log.e(TAG, "onErrorBodyWeather:")
    }

    override fun onBadInternetConnection(str: String) {
        Log.e(TAG, "onBadInternetConnection: " )
    }




//*************************************************************************************************************\\
//*                                                                                                             *\\
//*                                           CallBacks                                                            *\\
//*                                                                                                                 *\\
//**********************************************************************************************************************/
    /**
     * call of AccessUserLocationHelper
     * */
    override fun currentUserLocation(location: Location?) {
        if (location != null){
            Log.e(TAG, "currentUserLocation: Lat: ${location.latitude}"  )
            Log.e(TAG, "currentUserLocation: Long: ${location.longitude}"  )
            val lat:String =  location.latitude.toString()
            val lon:String =  location.longitude.toString()
            getWeatherData(lat,lon)
        }else{
            Log.e(TAG, "currentUserLocation: Location is null")
        }
    }




//*************************************************************************************************************\\
//*                                                                                                             *\\
//*                                        getters and setters of MutableLiveData                                 *\\
//*                                                                                                                 *\\
//**********************************************************************************************************************/
    /**
     * Getter & Setters of modifiedImagePath
     * */
    fun setImagePathLiveData(imagePath: String){ modifiedImagePath.value = imagePath }
    fun getImagePath():LiveData<String>{ return modifiedImagePath }

    /**
     * Getter & Setters of WeatherDataModel
     * */
    fun setWeatherDataLiveData(weatherData: WeatherDataModel){ weatherDataModel.value = weatherData }
    fun getWeatherData():LiveData<WeatherDataModel>{ return weatherDataModel }



//*************************************************************************************************************\\
//*                                                                                                             *\\
//*                                         Deal with Room DB                                                    *\\
//*                                                                                                                 *\\
//**********************************************************************************************************************/
    /**
     * To insert new image in DB
     * */
    fun insertImage(image: Image) {
        CoroutineScope(Dispatchers.IO).launch {
            val imageEntity = ImageEntity(
                image_name = image.name,
                image_path = image.path,
                image_uri = image.uri.toString(),
            )
            imageRepo.insertImage(imageEntity)
        }
    }

    /**
     * @return List of saved images from DB
     * */
    fun getAllImages(): LiveData<List<ImageEntity>> {
        return imageRepo.getAllImages()
    }



    companion object {
        private const val TAG = "MainActivityViewModel"
    }

}