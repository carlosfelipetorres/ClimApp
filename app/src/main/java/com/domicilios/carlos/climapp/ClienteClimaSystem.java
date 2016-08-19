package com.domicilios.carlos.climapp;


import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *@author <a href="mailto:carlosfelipetorres75@gmail.com">Carlos Torres</a>
 */
public class ClienteClimaSystem implements IClienteClimaSystem {

    /**
     * Tag for logs
     **/
    private static final String TAG_LOG = ClienteClimaSystem.class.getName();

    /**
     * The retrofit client
     **/
    private Retrofit mRetrofit;

    public ClienteClimaSystem() {}

    /**
     * This method returns the API of the selected class
     *
     * @param clazz Class of the desired API
     * @param <T>   API class
     * @return the instance API
     */
    @Override
    public <T> T getApi(Class<T> clazz) {

        synchronized (ClienteClimaSystem.class) {
            if (mRetrofit == null) {
                Gson gson = new GsonBuilder()
                        .setDateFormat("dd/MM/yyyy HH:mm:ss")
                        .create();
                mRetrofit = new Retrofit.Builder().baseUrl("http://api.geonames.org/")
                        .addConverterFactory(GsonConverterFactory.create(gson)).client(getClient())
                        .build();
            }
            return mRetrofit.create(clazz);
        }
    }

    /**
     * This method returns the specific client for this interceptor
     *
     * @return Client interceptor
     */
    @NonNull
    private OkHttpClient getClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        // Just for debug level
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder().addInterceptor(loggingInterceptor).build();
    }

    /**
     * This method executes a call to the server and returns the result. If an error occurs, then
     * null is returned
     *
     * @param call Call to be executed
     * @return The response. Returns null if an error occurs
     */
    @Override
    public <T> Response<T> execute(Call<T> call) {
        Response<T> response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            Log.e(TAG_LOG, "An error occurs talking to the server", e);
        } catch (RuntimeException e) {
            Log.e(TAG_LOG, "unexpected error occurs creating the request or decoding the response",
                    e);
        }
        return response;
    }
}
