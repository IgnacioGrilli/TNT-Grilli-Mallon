package com.example.intentsapptp3

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class GeolocalizacionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_geolocalizacion)

        val ubi = findViewById<EditText>(R.id.localizacionText)
        val btnLocalizar = findViewById<Button>(R.id.buttonMapa)

        btnLocalizar.setOnClickListener {

            val ubicacion = ubi.text.toString().trim()

            if (ubicacion.isEmpty()) {
                Toast.makeText(this, "Ingresá una ubicación", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Convierte texto a URI geo
            val uri = Uri.parse("geo:0,0?q=${Uri.encode(ubicacion)}")

            val intent = Intent(Intent.ACTION_VIEW, uri)

            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(this, "No hay aplicación de mapas instalada", Toast.LENGTH_SHORT).show()
            }
        }

        val etNumero = findViewById<EditText>(R.id.etNumeroTelefono)
        val btnMarcar = findViewById<Button>(R.id.btnMarcar)

        btnMarcar.setOnClickListener {
            val numero = etNumero.text.toString().trim()

            /*
             Se utiliza ACTION_DIAL porque únicamente abre la aplicación
             telefónica con el número cargado para que el usuario confirme
             manualmente la llamada.

             Esto NO realiza llamadas automáticas y NO requiere permisos
             sensibles en Android.

             ACTION_CALL ejecuta la llamada directamente sin
             intervención del usuario, por lo que requiere el permiso
             peligroso android.permission.CALL_PHONE y autorización en
             tiempo de ejecución (runtime permission).
            */

            if (numero.isEmpty()) {
                Toast.makeText(this, "Ingresá un número", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$numero")

            try {
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this, "No se pudo abrir la app telefónica", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

/*dificultad:
En versiones modernas de Android (especialmente Android 11+), existen restricciones de visibilidad de paquetes/apps.
Eso significa que la  aplicación no siempre puede consultar libremente qué otras apps hay instaladas.
Entonces puede pasar que hay app Teléfono instalada, pero resolveActivity() devuelve null.
Al principio no me dejaba ingresar a la app del dispositivo,
solucion, cambiar a startActivity(intent) donde directamente, le delego la acción al sistema operativo.
Android resuelve qué app usar y la abre correctamente.
*/