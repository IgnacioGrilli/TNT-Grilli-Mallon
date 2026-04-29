package com.example.intentsapptp3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)  // ← primer pantalla que se muestra

        val etTexto = findViewById<EditText>(R.id.etTexto)
        val btnCompartir = findViewById<Button>(R.id.btnCompartir)
        val btnIrEjercicio2 = findViewById<Button>(R.id.btnIrEjercicio2)
        val btnIrEjercicio3 = findViewById<Button>(R.id.btnIrEjercicio3)
        val btnIrEjercicio4 = findViewById<Button>(R.id.btnIrEjercicio4)
        val btnIrEjercicio5 = findViewById<Button>(R.id.btnIrEjercicio5)
        val tvTextoCompartido = findViewById<TextView>(R.id.tvTextoCompartido) //




        btnCompartir.setOnClickListener {
            val texto = etTexto.text.toString().trim()
            if (texto.isEmpty()) {
                Toast.makeText(this, "El texto no puede estar vacío", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            compartirTexto(texto)
        }

        btnIrEjercicio2.setOnClickListener {
            startActivity(Intent(this, BrowserActivity::class.java))
        }

        // manejo las dos actividades dentro de Geolocalizacion ejercicio A y B
        btnIrEjercicio3.setOnClickListener {
            startActivity(Intent(this, GeolocalizacionActivity::class.java))
        }

        btnIrEjercicio4.setOnClickListener {
            startActivity(Intent(this, CamaraActivity::class.java))
        }

        btnIrEjercicio5.setOnClickListener {
            startActivity(Intent(this, Ejercicio5Activity::class.java))
        }

        //recepcion del texto de otra app que quiere compartir
        if (intent?.action == Intent.ACTION_SEND &&
            intent.type == "text/plain") {

            val texto = intent.getStringExtra(Intent.EXTRA_TEXT)

            tvTextoCompartido.text =
                "Texto recibido:\n$texto"
        }


    }

    private fun compartirTexto(texto: String) {
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, texto)
        }
        if (sendIntent.resolveActivity(packageManager) != null) {
            startActivity(Intent.createChooser(sendIntent, "Compartir con..."))
        } else {
            Toast.makeText(this, "No hay apps disponibles", Toast.LENGTH_SHORT).show()
        }
    }


}