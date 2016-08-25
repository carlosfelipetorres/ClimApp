package com.domicilios.carlos.climapp.controllers;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.domicilios.carlos.climapp.R;
import com.domicilios.carlos.climapp.clients.DiComponent;
import com.domicilios.carlos.climapp.model.Ciudad;
import com.domicilios.carlos.climapp.model.ReporteClima;
import com.domicilios.carlos.climapp.model.TipoNotificacion;
import com.domicilios.carlos.climapp.services.IClimaService;
import com.domicilios.carlos.climapp.utils.AnimationUtils;
import com.domicilios.carlos.climapp.utils.AppUtils;
import com.domicilios.carlos.climapp.utils.GPSTracker;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.github.johnpersano.supertoasts.SuperToast;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Actividad que maneja la pantalla inicial del clima
 *
 * @author <a href="mailto:carlosfelipetorres75@gmail.com">Carlos Torres</a>
 */
public class ClimaActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    /** Posicion actual **/
    public static final String CURRENT = "CURRENT";
    /**
     * Estacion
     **/
    @BindView(R.id.estacion_tv)
    TextView mEstacionTv;

    /**
     * temp
     **/
    @BindView(R.id.temperatura_tv)
    TextView mTempTv;

    /**
     * humedad
     **/
    @BindView(R.id.humedad_tv)
    TextView mHumedadTv;

    /**
     * nubes
     **/
    @BindView(R.id.nubes_tv)
    TextView mNubesTv;

    /**
     * Latitud
     **/
    @BindView(R.id.lat_tv)
    TextView mLatTv;

    /**
     * Longitud
     **/
    @BindView(R.id.lon_tv)
    TextView mLonTv;

    /**
     * spinner de ciudades
     **/
    @BindView(R.id.ciudades_1_sp)
    Spinner mCiudades1Sp;

    /**
     * spinner de ciudades
     **/
    @BindView(R.id.ciudades_2_sp)
    Spinner mCiudades2Sp;

    /**
     * Longitud
     **/
    @BindView(R.id.humedad_ciudad_1_tv)
    TextView mHumedadCiudad1Tv;

    /**
     * Longitud
     **/
    @BindView(R.id.humedad_ciudad_2_tv)
    TextView mHumedadCiudad2Tv;

    /**
     * Longitud
     **/
    @BindView(R.id.nubes_ciudad_1_tv)
    TextView mNubesCiudad1Tv;

    /**
     * Longitud
     **/
    @BindView(R.id.nubes_ciudad_2_tv)
    TextView mNubesCiudad2Tv;

    /**
     * Longitud
     **/
    @BindView(R.id.temperatura_ciudad_1_tv)
    TextView mTempCiudad1Tv;

    /**
     * Longitud
     **/
    @BindView(R.id.temperatura_ciudad_2_tv)
    TextView mTempCiudad2Tv;

    /**
     * Linear layout de ciudad 1
     **/
    @BindView(R.id.ciudad_1_ll)
    LinearLayout mCiudad1Ll;

    /**
     * Linear layout de ciudad 2
     **/
    @BindView(R.id.ciudad_2_ll)
    LinearLayout mCiudad2Ll;

    /**
     * Reporte del climas
     **/
    @BindView(R.id.reporte_ll)
    LinearLayout mReporteLl;

    /**
     * Lista de ciudades
     **/
    private List<Ciudad> mCiudades;

    /**
     * Reporte actual
     **/
    private ReporteClima mReporte;

    /**
     * Reporte ciudad 1
     **/
    private ReporteClima mReporteCiudad1;

    /**
     * Reporte ciudad 2
     **/
    private ReporteClima mReporteCiudad2;

    /**
     * Servicio de clima
     **/
    @Inject
    IClimaService climaService;

    /**
     * arreglo de coordenadas lat lng
     **/
    private String[] coordenadas = new String[3];

    /**
     * Latitud actual
     **/
    private String lat;

    /**
     * longitud actual
     **/
    private String lng;

    /**
     * Swipe and Refresh layout
     **/
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;

    /**
     * Floating action button
     **/
    @BindView(R.id.map_fab)
    FloatingActionButton fab;

    /**
     * Info de usuario
     **/
    @BindView(R.id.nombre_usuario_tv)
    TextView mNomUserTv;

    /**
     * FB login button
     **/
    @BindView(R.id.login_button)
    LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clima);

        final GPSTracker tracker = new GPSTracker(this);
        if (!tracker.canGetLocation()) {
            tracker.showSettingsAlert();
        } else {
            mLatTv.setText(String.valueOf(tracker.getLatitude()));
            mLonTv.setText(String.valueOf(tracker.getLongitude()));
            lat = String.valueOf(tracker.getLatitude());
            lng = String.valueOf(tracker.getLongitude());
        }

        AppUtils.initSwipeRefreshLayout(mRefreshLayout);
        mRefreshLayout.setOnRefreshListener(this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng current = new LatLng(tracker.getLatitude(), tracker.getLongitude());

                Intent intent = new Intent(ClimaActivity.this, MapsActivity.class);
                intent.putExtra(CURRENT, current);
                AnimationUtils.configurarAnimacion(ClimaActivity.this, mReporteLl, true, intent);

            }
        });

        if(FacebookSdk.isInitialized()){
            loginButton.setVisibility(View.GONE);
            mNomUserTv.setVisibility(View.VISIBLE);

            GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            try {
                                mNomUserTv.setText("Hi, " + object.getString("name"));
                            } catch (JSONException ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender, birthday");
            request.setParameters(parameters);
            request.executeAsync();
        }
        loginButton.setReadPermissions("email");
        final CallbackManager callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                loginButton.setVisibility(View.GONE);
                mNomUserTv.setVisibility(View.VISIBLE);
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    mNomUserTv.setText("Hi, " + object.getString("name"));
                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.e("FACEBOOK", "onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.e("FACEBOOK", "onError");
            }
        });

        new CargaReporteAsyncTask().execute();
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    /**
     * Injection component. This should be done if there are fields to be injected
     *
     * @param diComponent
     *         Dependency injection
     */
    @Override
    protected void injectComponent(DiComponent diComponent) {
        diComponent.inject(this);
    }

    @Override
    public void onRefresh() {
        new CargaReporteAsyncTask().execute();
    }

    /**
     * Esta clase realiza la carga inicial de manera asíncrona
     */
    private class CargaReporteAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            mReporte = climaService.obtenerReporteClima(lat, lng);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (mReporte == null) {
                return;
            }
            mEstacionTv.setText(mReporte.getNombreEstacion());
            mTempTv.setText(mReporte.getTemperatura() + "°C");
            mHumedadTv.setText(String.valueOf(mReporte.getHumedad()) + "%");
            mNubesTv.setText(mReporte.getNubes().toUpperCase());
            mLatTv.setText(mReporte.getLat().toString());
            mLonTv.setText(mReporte.getLongitud().toString());

            AnimationUtils.configurarAnimacion(ClimaActivity.this, mReporteLl, false);
            super.onPostExecute(aVoid);

            new CargaCiudadesAsyncTask().execute();

            if (mRefreshLayout.isRefreshing()) {
                mRefreshLayout.setRefreshing(false);
            }
        }
    }

    /**
     * Esta clase realiza la carga inicial de manera asíncrona
     */
    private class CargaCiudadesAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            mCiudades = climaService.obtenerCiudades();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (mCiudades == null) {
                return;
            }
            setSpinner(mCiudades1Sp, mCiudades);
            setSpinner(mCiudades2Sp, mCiudades);

            mCiudades1Sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    coordenadas[0] = mCiudades.get(i).getLat();
                    coordenadas[1] = mCiudades.get(i).getLon();
                    coordenadas[2] = "1";
                    new CargaReporteCiudadesAsyncTask().execute(coordenadas);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            mCiudades2Sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    coordenadas[0] = mCiudades.get(i).getLat();
                    coordenadas[1] = mCiudades.get(i).getLon();
                    coordenadas[2] = "2";
                    new CargaReporteCiudadesAsyncTask().execute(coordenadas);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            super.onPostExecute(aVoid);

            if (mRefreshLayout.isRefreshing()) {
                mRefreshLayout.setRefreshing(false);
            }
        }
    }

    /**
     * Esta clase realiza la carga inicial de manera asíncrona
     */
    private class CargaReporteCiudadesAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            mReporte = climaService.obtenerReporteClima(params[0], params[1]);
            return params[2];
        }

        @Override
        protected void onPostExecute(String spinner) {
            if (mReporte == null) {
                AppUtils.crearToast(ClimaActivity.this, "Hubo un error obteniendo los datos", SuperToast.Duration.MEDIUM,
                        TipoNotificacion.ERROR).show();
                return;
            }
            if (spinner.equals("1")) {
                mTempCiudad1Tv.setText(mReporte.getTemperatura() + "°C");
                mHumedadCiudad1Tv.setText(String.valueOf(mReporte.getHumedad()) + "%");
                mNubesCiudad1Tv.setText(mReporte.getNubes());
                mCiudad1Ll.setVisibility(View.VISIBLE);
                mReporteCiudad1 = mReporte;
                evaluarCiudades();
            }
            if (spinner.equals("2")) {
                mTempCiudad2Tv.setText(mReporte.getTemperatura() + "°C");
                mHumedadCiudad2Tv.setText(String.valueOf(mReporte.getHumedad()) + "%");
                mNubesCiudad2Tv.setText(mReporte.getNubes());
                mCiudad2Ll.setVisibility(View.VISIBLE);
                mReporteCiudad2 = mReporte;
                mCiudades1Sp.setSelection(2);
                evaluarCiudades();
            }
            mCiudades1Sp.setVisibility(View.VISIBLE);
            mCiudades2Sp.setVisibility(View.VISIBLE);
            super.onPostExecute(null);

            if (mRefreshLayout.isRefreshing()) {
                mRefreshLayout.setRefreshing(false);
            }
        }
    }

    /**
     * Set de spinner con lista especificada
     *
     * @param spinner
     *         Vista spinner
     * @param objetos
     *         Lista de objetos
     */
    private void setSpinner(Spinner spinner, List<?> objetos) {
        ArrayAdapter<?> adapterTipoMaterial =
                new ArrayAdapter<>(ClimaActivity.this, R.layout.list_item_simple, R.id.texto_rtv,
                        objetos);
        spinner.setAdapter(adapterTipoMaterial);
    }

    /**
     * Evalua las ciudades para ver cual es mejor para vacacionar
     */
    private void evaluarCiudades() {
        Boolean validacion = null;
        if (mReporteCiudad1 == null || mReporteCiudad2 == null ||
                mReporteCiudad1.getNombreEstacion().equals(mReporteCiudad2.getNombreEstacion())) {
            int transparent = R.color.caldroid_transparent;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCiudad1Ll.setBackgroundColor(getColor(transparent));
                mCiudad2Ll.setBackgroundColor(getColor(transparent));
            } else {
                mCiudad1Ll.setBackgroundColor(getResources().getColor(transparent));
                mCiudad2Ll.setBackgroundColor(getResources().getColor(transparent));
            }
            return;
        }
        Integer temp1 = Integer.parseInt(mReporteCiudad1.getTemperatura());
        Integer temp2 = Integer.parseInt(mReporteCiudad2.getTemperatura());
        Integer nubes1 = obtenerCodigoNubes(mReporteCiudad1.getCodigoNubes());
        Integer nubes2 = obtenerCodigoNubes(mReporteCiudad2.getCodigoNubes());
        if (temp2.equals(temp1)) {
            if (!nubes2.equals(nubes1)) {
                validacion = nubes1 > nubes2;
            }
        } else {
            validacion = temp1 > temp2;
        }

        if (validacion == null) {
            return;
        }
        int green = R.color.material_green_300;
        int red = R.color.material_red_300;
        if (validacion) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCiudades1Sp.setBackgroundColor(getColor(green));
                mCiudad1Ll.setBackgroundColor(getColor(green));
                mCiudades2Sp.setBackgroundColor(getColor(red));
                mCiudad2Ll.setBackgroundColor(getColor(red));
            } else {
                mCiudades1Sp.setBackgroundColor(getResources().getColor(green));
                mCiudad1Ll.setBackgroundColor(getResources().getColor(green));
                mCiudades2Sp.setBackgroundColor(getResources().getColor(red));
                mCiudad2Ll.setBackgroundColor(getResources().getColor(red));
            }
            AppUtils.crearToast(ClimaActivity.this, "Es preferible que visites " + mCiudades1Sp.getSelectedItem(), SuperToast.Duration.MEDIUM,
                    TipoNotificacion.INFORMATIVA).show();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCiudades1Sp.setBackgroundColor(getColor(red));
                mCiudad1Ll.setBackgroundColor(getColor(red));
                mCiudades2Sp.setBackgroundColor(getColor(green));
                mCiudad2Ll.setBackgroundColor(getColor(green));
            } else {
                mCiudades1Sp.setBackgroundColor(getResources().getColor(red));
                mCiudad1Ll.setBackgroundColor(getResources().getColor(red));
                mCiudades2Sp.setBackgroundColor(getResources().getColor(green));
                mCiudad2Ll.setBackgroundColor(getResources().getColor(green));
            }
            AppUtils.crearToast(ClimaActivity.this, "Es preferible que visites " + mCiudades2Sp.getSelectedItem(), SuperToast.Duration.MEDIUM,
                    TipoNotificacion.INFORMATIVA).show();
        }
    }

    /**
     * Obtiene un valor cuantitativo para los codigos de nubes. Tomados de la documentacion METAR
     *
     * @param codigo
     *         Codigo de nubes
     *
     * @return Valor numerico
     */
    private Integer obtenerCodigoNubes(String codigo) {
        Integer valor = 0;
        switch (codigo) {
            case "SKC":
                valor = 1;
                break;
            case "CLR":
                valor = 2;
                break;
            case "NSC":
                valor = 3;
                break;
            case "FEW":
                valor = 4;
                break;
            case "SCT":
                valor = 5;
                break;
            case "BKN":
                valor = 6;
                break;
            case "OVC":
                valor = 7;
                break;
            case "VV":
                valor = 8;
                break;
        }
        return valor;
    }

}
