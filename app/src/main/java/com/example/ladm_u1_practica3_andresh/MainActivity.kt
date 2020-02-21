package com.example.ladm_u1_practica3_andresh

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

class MainActivity : AppCompatActivity() {

    val vector = intArrayOf (0,0,0,0,0,0,0,0,0,0)
    var valorToAdd = 0
    var en = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        asignar.setOnClickListener {
            if(valor.text.isEmpty()){
                AlertDialog.Builder(this).setMessage("Escriba un valor").show()
            } else if (posicion.text.isEmpty() || posicion.text.toString().toInt()>9){
                AlertDialog.Builder(this).setMessage("Escriba un posición valida").show()
            } else {
                valorToAdd = valor.text.toString().toInt()
                en = posicion.text.toString().toInt()

                vector.set(en,valorToAdd)
            }
        }

        mostrar.setOnClickListener {
            var texto = "["+vector.joinToString()+"]"
            enPantalla.setText(texto)
        }

        guardar.setOnClickListener {
            if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE),0)
        } else if(noSD()){
                AlertDialog.Builder(this).setMessage("No hay memoria SD").show()
                return@setOnClickListener
            }
            if(nombreg.text.isEmpty()){ AlertDialog.Builder(this).setMessage("Asigne un nombre para guardar").show()
            }else {
                try {
                    var rutaSD = Environment.getExternalStorageDirectory()
                    var datosrAchivo = File(rutaSD.absolutePath, nombreg.text.toString() + ".txt")
                    var flujoSalida = OutputStreamWriter(FileOutputStream(datosrAchivo))

                    var data = enPantalla.text.toString()

                    flujoSalida.write(data)
                    flujoSalida.flush()
                    flujoSalida.close()

                    AlertDialog.Builder(this).setMessage("El archivo se ha guardado").show()
                    limpiarCampos()

                } catch (error: IOException) {
                    AlertDialog.Builder(this).setMessage(error.message.toString())
                }
            }
        }

        leer.setOnClickListener {
            if(noSD()){
                AlertDialog.Builder(this).setMessage("No hay memoria SD")
                return@setOnClickListener
            }

            if(nombrel.text.isEmpty()){AlertDialog.Builder(this).setMessage("Por favor, indique el nombre")
            } else {
                try {
                    var rutaSD = Environment.getExternalStorageDirectory()
                    var datosrAchivo = File(rutaSD.absolutePath, nombrel.text.toString() + ".txt")

                    var flujoEntrada = BufferedReader(InputStreamReader(FileInputStream(datosrAchivo)))

                    var data = flujoEntrada.readLine()

                    enPantalla.setText(data.toString())

                } catch (error: IOException) {
                    AlertDialog.Builder(this).setMessage(error.message.toString())
                }
            }
        }

    }
    fun noSD():Boolean{
        var estado = Environment.getExternalStorageState()
        if(estado!= Environment.MEDIA_MOUNTED)
            return true

        return false
    }

    fun limpiarCampos (){
        valor.setText("")
        posicion.setText("")
        nombreg.setText("")
        nombrel.setText("")
        enPantalla.setText("")
    }
}
