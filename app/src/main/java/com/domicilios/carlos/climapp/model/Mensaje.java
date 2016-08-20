package com.domicilios.carlos.climapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author <a href="mailto:carlosfelipetorres75@gmail.com">Carlos Torres</a>
 */
public class Mensaje {
    @SerializedName("message")
    private String message;

    @SerializedName("value")
    private Integer value;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
