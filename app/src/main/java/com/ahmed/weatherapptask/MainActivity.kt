package com.ahmed.weatherapptask


import android.Manifest
import android.R
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import com.ahmed.weatherapptask.Utils.PermissionManager
import com.ahmed.weatherapptask.ViewModels.MainActivityViewModel
import com.ahmed.weatherapptask.databinding.ActivityMainBinding
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker


class MainActivity : AppCompatActivity() {

    //Binding
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val model = ViewModelProvider(this).get(MainActivityViewModel::class.java)

    }


}