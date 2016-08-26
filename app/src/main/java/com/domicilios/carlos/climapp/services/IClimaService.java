package com.domicilios.carlos.climapp.services;

import com.domicilios.carlos.climapp.model.Ciudad;
import com.domicilios.carlos.climapp.model.ReporteClima;
import com.facebook.AccessToken;

import java.util.List;

/**
 * @author <a href="mailto:carlosfelipetorres75@gmail.com">Carlos Torres</a>
 */
public interface IClimaService {

    /**
     * Obtiene el reporte del clima
     *
     * @param lat latitud
     * @param lng longitud
     * @return reporte
     */
    ReporteClima obtenerReporteClima(String lat, String lng);

    /**
     * Obtiene las ciudades
     *
     * @return ciudades
     */
    List<Ciudad> obtenerCiudades();

    /**
     * obtiene el token guardado
     *
     * @return access token
     */
    AccessToken getAccessToken();

    /**
     * Guarda el token
     *
     * @param accessToken the accessToken to set
     */
    void setAccessToken(AccessToken accessToken);
}
