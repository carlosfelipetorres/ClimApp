package com.domicilios.carlos.climapp;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Esta es la clase utilizada para recibir las respuestas de ciudades
 *
 * @author <a href="mailto:carlosfelipetorres75@gmail.com">Carlos Torres</a>
 */
public class RespuestaCiudades implements Serializable {

    /** Lista de elementos **/
    @SerializedName("geonames")
    private List<Ciudad> geonames;

    /** status **/
    @SerializedName("status")
    private Mensaje status;

    public List<Ciudad> getGeonames() {
        return geonames;
    }

    public void setGeonames(List<Ciudad> geonames) {
        this.geonames = geonames;
    }

    public Mensaje getStatus() {
        return status;
    }

    public void setStatus(Mensaje status) {
        this.status = status;
    }
}
