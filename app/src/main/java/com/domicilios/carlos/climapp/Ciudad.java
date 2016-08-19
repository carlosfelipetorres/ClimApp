package com.domicilios.carlos.climapp;

import com.google.gson.annotations.SerializedName;

/**
 * Esta clase representa las ciudades
 *
 * @author <a href="mailto:carlosfelipetorres75@gmail.com">Carlos Torres</a>
 */
public class Ciudad {

    /**Longitud**/
    @SerializedName("lng")
    private String lon;

    /**Latitud**/
    @SerializedName("lat")
    private String lat;

    /**Nombre**/
    @SerializedName("toponymName")
    private String username;

    /**id de la ciudad**/
    @SerializedName("geonameId")
    private Integer id;

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return username;
    }
}
