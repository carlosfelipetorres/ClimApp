package com.domicilios.carlos.climapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Api del clima
 *
 * @author <a href="mailto:carlosfelipetorres75@gmail.com">Carlos F. Torres J.</a>
 */
public interface IClimaApi {
    /**
     * Devuelve las ciudades segun sus coordenadas
     *
     * @param north    coordenada norte
     * @param south    coordenada sur
     * @param east     coordenada este
     * @param west     coordenada oeste
     * @param username nombre de usuario
     * @param maxRows  maximo de filas
     * @return reportes del clima
     */
    @GET("citiesJSON")
    Call<RespuestaCiudades> getCiudades(@Query("north") String north, @Query("south") String south,
                                                @Query("east") String east, @Query("west") String west,
                                                @Query("username") String username, @Query("maxRows") Integer maxRows);

    /**
     * Obtiene el reporte del clima para una ubicacion especifica
     *
     * @param lat      latitud
     * @param lng      longitud
     * @param username nombre de usuario
     * @return reporte del clima
     */
    @GET("findNearByWeatherJSON")
    Call<RespuestaReporte> getClima(@Query("lat") String lat, @Query("lng") String lng,
                                                   @Query("username") String username);
}
