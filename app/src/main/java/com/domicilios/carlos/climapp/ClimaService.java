package com.domicilios.carlos.climapp;

import android.content.Context;

import com.github.johnpersano.supertoasts.SuperToast;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Response;

/**
 * @author <a href="mailto:carlosfelipetorres75@gmail.com">Carlos Torres</a>
 */
public class ClimaService implements IClimaService {

    /**
     * Cliente servidor
     **/
    @Inject
    IClienteClimaSystem mCliente = new ClienteClimaSystem();

    /**
     * Singleton API visitas
     **/
    private IClimaApi mClimaApi;

    /**
     * Application context
     **/
    private Context mContext;

    public ClimaService(Context context) {
        this.mContext = context;
    }

    @Override
    public ReporteClima obtenerReporteClima(String lat, String lng) {
        IClimaApi climaApi = getClimaApi();

        Call<RespuestaReporte> call = climaApi.getClima(lat, lng, mContext.getString(R.string.username));
        Response<RespuestaReporte> response = mCliente.execute(call);
        if (!isSuccessful(response)) {
            if(response.code() == 15){
                AppUtils.crearToast(mContext, "No existe informacion para este punto", SuperToast.Duration.MEDIUM,
                        TipoNotificacion.ALERTA).show();
            }
            if(response.code() == 13){
                AppUtils.crearToast(mContext, "Hubo un problema con la conexion a internet", SuperToast.Duration.MEDIUM,
                        TipoNotificacion.ALERTA).show();
            }
            return null;
        }
        return response.body().getWeatherObservations();
    }

    @Override
    public List<Ciudad> obtenerCiudades() {
        IClimaApi climaApi = getClimaApi();

        Call<RespuestaCiudades> call = climaApi.getCiudades(mContext.getString(R.string.north),
                mContext.getString(R.string.south), mContext.getString(R.string.east),
                mContext.getString(R.string.west), mContext.getString(R.string.username), 275);
        Response<RespuestaCiudades> response = mCliente.execute(call);
        if (!isSuccessful(response)) {
            if(response.code() == 13){
                AppUtils.crearToast(mContext, "Hubo un problema con la conexion a internet", SuperToast.Duration.MEDIUM,
                        TipoNotificacion.ALERTA).show();
            }
            return null;
        }
        return response.body().getGeonames();
    }

    /**
     * Obtiene el API de las visitas del cliente
     *
     * @return La interfaz del API de vencimisntos de producto
     */
    private IClimaApi getClimaApi() {
        if (mClimaApi == null) {
            mClimaApi = mCliente.getApi(IClimaApi.class);
        }
        return mClimaApi;
    }

    /**
     * Este metodo verifica si la respuesta fue exitosa o no
     *
     * @param response La respuesta para ser verificada
     * @return True si fue exitoso. False de lo contrario.
     */
    public static boolean isSuccessful(Response response) {
        return response != null && response.isSuccessful();
    }
}
