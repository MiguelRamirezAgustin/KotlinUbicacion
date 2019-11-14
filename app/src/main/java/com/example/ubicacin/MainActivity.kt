package com.example.ubicacin

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.tasks.OnSuccessListener

class MainActivity : AppCompatActivity() {

    //Declaracion de variables
    private val permisoFineLocation = android.Manifest.permission.ACCESS_FINE_LOCATION
    private val permisoCoarseLocation = android.Manifest.permission.ACCESS_COARSE_LOCATION
    //variable para identificar el permiso asignado a la ubicación
    private val CODIGO_SOLICITUD_PERMISO = 100
    //variable que permite obtener datos de la ubicación
    var fusedLocationClient: FusedLocationProviderClient? = null
    var locationRequest:LocationRequest? = null
    var callback:LocationCallback? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationClient = FusedLocationProviderClient( this@MainActivity)
        //objeto para configura la ubicacion
        inicializarLocationRequest()

    }


    private fun inicializarLocationRequest(){
        locationRequest = LocationRequest()
        locationRequest?.interval = 10000
        locationRequest?.fastestInterval = 5000
        locationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    //valida permisos
    private fun validarPermisosUbicacion():Boolean{
        val hayUbicacionPrecisa = ActivityCompat.checkSelfPermission(this@MainActivity, permisoFineLocation) == PackageManager.PERMISSION_GRANTED
        val hayUbicacionOrdinario = ActivityCompat.checkSelfPermission(this@MainActivity, permisoCoarseLocation) ==  PackageManager.PERMISSION_GRANTED

        //cuando hay permisos ya otorgados regresa un true
        return hayUbicacionOrdinario && hayUbicacionPrecisa
    }


    // @SuppressLint("MissingPermission") para no validar los permisos  que estan el al funtion validarPermisosUbicacion (hayUbicacionPrecisa, hayUbicacionOrdinario)
    @SuppressLint("MissingPermission")
    private fun obtenerUbicacion(){

        //obtener ubicacion de forma estatica, sucede al ingresar aun mapa y ingreso a la palicacion obtiene la ubicacion del mapa
       /* fusedLocationClient?.lastLocation?.addOnSuccessListener(this@MainActivity, object :OnSuccessListener<Location>{
            //si se pudo obtener la ultima ubicacion
            override fun onSuccess(location: Location?) {
                if(location != null){
                    Toast.makeText(applicationContext, location?.latitude.toString() + " - " + location?.longitude.toString(), Toast.LENGTH_LONG).show()
                }
            }

        })*/

         callback = object :LocationCallback(){
            //contiene las coodenadas de la ubicacion  locationResult:contine arrego de ubicaciones mas recientes
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)
                for (ubicacion in locationResult?.locations!!){
                    Toast.makeText(applicationContext, ubicacion.latitude.toString() + " , " + ubicacion.longitude.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }

        fusedLocationClient?.requestLocationUpdates(locationRequest, callback, null)
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

    //se detiene el servicio
    private fun detenetActualizacionUbicacion(){
        fusedLocationClient?.removeLocationUpdates(callback)
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


    //detiene la ubicacion
    override fun onPause() {
        super.onPause()
        detenetActualizacionUbicacion()
    }
}
