package com.example.ubicacin

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient

class MainActivity : AppCompatActivity() {

    //Declaracion de variables
    private val permisoFineLocation = android.Manifest.permission.ACCESS_FINE_LOCATION
    private val permisoCoarseLocation = android.Manifest.permission.ACCESS_COARSE_LOCATION
    //variable para identificar el permiso asignado a la ubicación
    private val CODIGO_SOLICITUD_PERMISO = 100
    //variable que permite obtener datos de la ubicación
    var fusedLocationClient: FusedLocationProviderClient? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationClient = FusedLocationProviderClient( this@MainActivity)
    }

    //valida permisos
    private fun validarPermisosUbicacion():Boolean{
        val hayUbicacionPrecisa = ActivityCompat.checkSelfPermission(this@MainActivity, permisoFineLocation) == PackageManager.PERMISSION_GRANTED
        val hayUbicacionOrdinario = ActivityCompat.checkSelfPermission(this@MainActivity, permisoCoarseLocation) ==  PackageManager.PERMISSION_GRANTED

        //cuando hay permisos ya otorgados regresa un true
        return hayUbicacionOrdinario && hayUbicacionPrecisa
    }

    private fun obtenerUbicacion(){
        

    }

    private fun pedirPermisos(){
        //validar caso si el usuario nego el permiso o aun no los a otorgado
        val deboProveerContexto = ActivityCompat.shouldShowRequestPermissionRationale(this, permisoFineLocation)

        if(deboProveerContexto){
            //manda un mensaje con explicación adicional
            solicitudPermiso()
        }else{
            solicitudPermiso()
        }
    }

    private fun solicitudPermiso(){
        requestPermissions(arrayOf(permisoFineLocation, permisoCoarseLocation),CODIGO_SOLICITUD_PERMISO)
    }

    //funcion que identidfica si el usuario dio permiso o no
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        //requestCode continen el codigo de la solicitud
        when(requestCode){
            CODIGO_SOLICITUD_PERMISO ->{
                //grantResults contine las respuesa del permiso
                if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //obtener ubicacion
                    obtenerUbicacion()
                }else{
                    //no se dio permiso para la ubicacion
                    Toast.makeText(this@MainActivity, "No diste permiso para acceder a la ubicació", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //inicaliza la palicacion
    override fun onStart() {
        super.onStart()
        //validacion para obtener ubicacion
        if(validarPermisosUbicacion()){
            obtenerUbicacion()
        }else{
            pedirPermisos()
        }
    }
}
