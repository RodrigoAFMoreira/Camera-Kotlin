package com.example.picturemode

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


@Suppress("DEPRECATION")
class CameraActivity : AppCompatActivity() {

    val REQUEST_IMAGE_CAPTURE = 100
    val PERMISSION_REQUEST_CODE = 200
    lateinit var imageView: ImageView
    lateinit var buttonCamera: Button
    lateinit var buttonFiltro: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        imageView = findViewById(R.id.imageView)
        buttonCamera = findViewById(R.id.btnCamera)
        buttonFiltro = findViewById(R.id.btnColor)

        if (checkPermission()) {
            buttonCamera.setOnClickListener {
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                try {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(this, "Error: " + e.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }

        buttonFiltro.setOnClickListener{
            imageView.colorFilter = ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(0f)})
        }
    }


    private fun checkPermission(): Boolean {
        return if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermission()
            true
        } else false
    }
    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf<String>(android.Manifest.permission.CAMERA),
            PERMISSION_REQUEST_CODE
        )
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}