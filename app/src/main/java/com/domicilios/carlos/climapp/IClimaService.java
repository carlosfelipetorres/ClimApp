package com.domicilios.carlos.climapp;

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
}
