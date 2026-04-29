package com.example.intentsapptp3

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class CamaraActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var btnFoto: Button
    private lateinit var progressBar: ProgressBar

    // API moderna reemplaza startActivityForResult
    private val camaraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->

            progressBar.visibility = View.GONE

            if (bitmap != null) {
                imageView.setImageBitmap(bitmap)
            } else {
                Toast.makeText(this, "No se tomó la foto", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camara)

        imageView = findViewById(R.id.imageViewFoto)
        btnFoto = findViewById(R.id.btnTomarFoto)
        progressBar = findViewById(R.id.progressBar)

        progressBar.visibility = View.GONE

        btnFoto.setOnClickListener {

            // indicador visual mientras abre cámara
            progressBar.visibility = View.VISIBLE

            camaraLauncher.launch(null)
        }
    }
}