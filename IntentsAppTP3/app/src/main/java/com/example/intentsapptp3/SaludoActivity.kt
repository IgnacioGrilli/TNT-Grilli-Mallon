package com.example.intentsapptp3

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SaludoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saludo)

        val tvSaludo = findViewById<TextView>(R.id.tvSaludo)
        val etEditarNombre = findViewById<EditText>(R.id.etEditarNombre)
        val btnVolver = findViewById<Button>(R.id.btnVolver)

        // Recibir dato enviado desde la primera pantalla
        val nombre = intent.getStringExtra("nombre") ?: ""

        // Mostrar
        tvSaludo.text = "Holaa, $nombre"

        // Cargar nombre en campo editable
        etEditarNombre.setText(nombre)

        btnVolver.setOnClickListener {

            val nombreEditado = etEditarNombre.text.toString().trim()

            val data = Intent()
            data.putExtra("nombreEditado", nombreEditado)

            setResult(Activity.RESULT_OK, data)
            finish()
        }
    }
}