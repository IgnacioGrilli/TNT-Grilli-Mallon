package com.example.intentsapptp3

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class BrowserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browser)

        val etUrl = findViewById<EditText>(R.id.etUrl)
        val btnAbrir = findViewById<Button>(R.id.btnAbrir)

        btnAbrir.setOnClickListener {
            var url = etUrl.text.toString().trim()

            if (url.isEmpty()) {
                Toast.makeText(this, "Ingresá una URL", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Extra: agregar https:// si no tiene prefijo
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "https://$url"
            }

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(this, "No hay navegador disponible", Toast.LENGTH_SHORT).show()
            }
        }
    }
}