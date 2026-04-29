package com.example.intentsapptp3

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class Ejercicio5Activity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var btnSiguiente: Button

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == Activity.RESULT_OK) {

                val nuevoNombre =
                    result.data?.getStringExtra("nombreEditado") ?: return@registerForActivityResult

                etNombre.setText(nuevoNombre)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ejercicio5)

        etNombre = findViewById(R.id.etNombre)
        btnSiguiente = findViewById(R.id.btnSiguiente)

        btnSiguiente.setOnClickListener {

            val nombre = etNombre.text.toString().trim()

            val intent = Intent(this, SaludoActivity::class.java)
            intent.putExtra("nombre", nombre)

            launcher.launch(intent)
        }
    }
}