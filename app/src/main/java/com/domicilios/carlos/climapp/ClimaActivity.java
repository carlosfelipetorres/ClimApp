package com.domicilios.carlos.climapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.johnpersano.supertoasts.SuperToast;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * @author <a href="mailto:carlosfelipetorres75@gmail.com">Carlos Torres</a>
 */
public class ClimaActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

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
     * Lista de ciudades
     **/
    private List<Ciudad> mCiudades;

    /**
     * Reporte actual
     **/
    private ReporteClima mReporte;

    /**
     * Servicio de clima
     **/
    @Inject
    IClimaService climaService;

    /**
     * arreglo de coordenadas lat lng
     **/
    private String[] coordenadas = new String[3];

    /**Latitud actual**/
    private String lat;

    /**longitud actual**/
    private String lng;

    /**
     * Swipe and Refresh layout
     **/
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clima);

        GPSTracker tracker = new GPSTracker(this);
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

        new CargaReporteAsyncTask().execute();
        new CargaCiudadesAsyncTask().execute();
    }

    /**
     * Injection component. This should be done if there are fields to be injected
     *
     * @param diComponent Dependency injection
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
                AppUtils.crearToast(ClimaActivity.this, "Hubo un error obteniendo los datos", SuperToast.Duration.MEDIUM,
                        TipoNotificacion.ERROR).show();
                return;
            }
            mEstacionTv.setText(mReporte.getNombreEstacion());
            mTempTv.setText(mReporte.getTemperatura() + "°C");
            mHumedadTv.setText(String.valueOf(mReporte.getHumedad())+"%");
            mNubesTv.setText(mReporte.getNubes());
            mLatTv.setText(String.valueOf(mReporte.getLat()));
            mLonTv.setText(String.valueOf(mReporte.getLongitud()));

            super.onPostExecute(aVoid);

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
                AppUtils.crearToast(ClimaActivity.this, "Hubo un error obteniendo las ciudades", SuperToast.Duration.MEDIUM,
                        TipoNotificacion.ERROR).show();
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
            if(spinner == "1") {
                mTempCiudad1Tv.setText(mReporte.getTemperatura() + "°C");
                mHumedadCiudad1Tv.setText(String.valueOf(mReporte.getHumedad())+"%");
                mNubesCiudad1Tv.setText(mReporte.getNubes());
            }
            if(spinner == "2") {
                mTempCiudad2Tv.setText(mReporte.getTemperatura() + "°C");
                mHumedadCiudad2Tv.setText(String.valueOf(mReporte.getHumedad())+"%");
                mNubesCiudad2Tv.setText(mReporte.getNubes());
            }
            super.onPostExecute(null);

            if (mRefreshLayout.isRefreshing()) {
                mRefreshLayout.setRefreshing(false);
            }
        }
    }

    /**
     * Set de spinner con lista especificada
     *
     * @param spinner Vista spinner
     * @param objetos Lista de objetos
     */
    private void setSpinner(Spinner spinner, List<?> objetos) {
        ArrayAdapter<?> adapterTipoMaterial =
                new ArrayAdapter<>(ClimaActivity.this, R.layout.list_item_simple, R.id.texto_rtv,
                        objetos);
        spinner.setAdapter(adapterTipoMaterial);
    }

}
