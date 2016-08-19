package com.domicilios.carlos.climapp;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

/**
 * @author <a href="mailto:carlosfelipetorres75@gmail.com">Carlos Torres</a>
 */
public class ReporteClima implements Serializable {

    /** Longitud **/
    @SerializedName("lng")
    private Double longitud;

    /**nubes**/
    @SerializedName("clouds")
    private String nubes;

    /**Codigo de nubes**/
    @SerializedName("cloudsCode")
    private String codigoNubes;

    /**Latitud**/
    @SerializedName("lat")
    private Double lat;

    /**temperatura**/
    @SerializedName("temperature")
    private String temperatura;

    /**humedad**/
    @SerializedName("humidity")
    private Integer humedad;

    /**nombre de la estacion**/
    @SerializedName("stationName")
    private String nombreEstacion;

    public String getNombreEstacion() {
        return nombreEstacion;
    }

    public void setNombreEstacion(String nombreEstacion) {
        this.nombreEstacion = nombreEstacion;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public String getNubes() {
        return nubes;
    }

    public void setNubes(String nubes) {
        this.nubes = nubes;
    }

    public String getCodigoNubes() {
        return codigoNubes;
    }

    public void setCodigoNubes(String codigoNubes) {
        this.codigoNubes = codigoNubes;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public String getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(String temperatura) {
        this.temperatura = temperatura;
    }

    public Integer getHumedad() {
        return humedad;
    }

    public void setHumedad(Integer humedad) {
        this.humedad = humedad;
    }
}
