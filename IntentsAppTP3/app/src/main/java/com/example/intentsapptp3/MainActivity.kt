package com.example.intentsapptp3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)  // ← SIEMPRE PRIMERO

        val etTexto = findViewById<EditText>(R.id.etTexto)
        val btnCompartir = findViewById<Button>(R.id.btnCompartir)
        val btnIrEjercicio2 = findViewById<Button>(R.id.btnIrEjercicio2)

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