package com.domicilios.carlos.climapp.model;

import com.domicilios.carlos.climapp.model.Mensaje;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Esta es la clase utilizada para recibir las respuestas del reporte
 *
 * @author <a href="mailto:carlosfelipetorres75@gmail.com">Carlos Torres</a>
 */
public class RespuestaReporte implements Serializable {

    /** Weather Observation **/
    @SerializedName("weatherObservation")
    private ReporteClima weatherObservation;

    /** status **/
    @SerializedName("status")
    private Mensaje status;

    public Mensaje getStatus() {
        return status;
    }

    public void setStatus(Mensaje status) {
        this.status = status;
    }

    public ReporteClima getWeatherObservations() {
        return weatherObservation;
    }

    public void setWeatherObservations(ReporteClima weatherObservations) {
        this.weatherObservation = weatherObservations;
    }
}
