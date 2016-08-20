package com.domicilios.carlos.climapp.controllers;

import com.domicilios.carlos.climapp.R;
import com.domicilios.carlos.climapp.clients.DiComponent;
import com.domicilios.carlos.climapp.model.ReporteClima;
import com.domicilios.carlos.climapp.model.TipoNotificacion;
import com.domicilios.carlos.climapp.services.IClimaService;
import com.domicilios.carlos.climapp.utils.AnimationUtils;
import com.domicilios.carlos.climapp.utils.AppUtils;
import com.domicilios.carlos.climapp.views.ProgressWheel;
import com.github.johnpersano.supertoasts.SuperToast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Actividad donde se muestra el mapa y el estado del clima en la ubicacion dada
 *
 * @author <a href="mailto:carlosfelipetorres75@gmail.com">Carlos Torres</a>
 */
public class MapsActivity extends BaseActivity implements OnMapReadyCallback {

    /**Current location **/
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
     * humedad
     **/
    @BindView(R.id.titulo_humedad_tv)
    ImageView mTituloHumedadTv;

    /**
     * nubes
     **/
    @BindView(R.id.titulo_nubes_tv)
    ImageView mTituloNubesTv;

    /**
     * Latitud
     **/
    @BindView(R.id.titulo_latitud_tv)
    TextView mTituloLatTv;

    /**
     * Longitud
     **/
    @BindView(R.id.titulo_longitud_tv)
    TextView mTituloLonTv;

    /**
     * Marker Icon
     **/
    @BindView(R.id.marker_iv)
    ImageView mMarkerIcon;

    /** Loading Progress Wheel **/
    @BindView(R.id.loading_pw)
    ProgressWheel mLoadingPw;

    /**
     * Informacion sobre el clima en el mapa
     **/
    @BindView(R.id.info_mapa_ll)
    LinearLayout mInfoMapaLl;

    /**
     * Google Map
     **/
    private GoogleMap mMap;

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
     * Current location
     **/
    private LatLng current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mMarkerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCurrentPosition();
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        enableProgressWheel(true);
        current = (LatLng) getIntent().getExtras().get(CURRENT);
    }

    @Override
    protected void injectComponent(DiComponent diComponent) {
        diComponent.inject(this);
    }

    /**
     * This method selects the current position
     */
    private void selectCurrentPosition() {
        CameraPosition cameraPosition = mMap.getCameraPosition();
        if (current != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(current));
            showLocation(current);
            current = null;
        } else {
            showLocation(cameraPosition.target);
        }
    }

    /**
     * This method shows the location confirmation. If the user confirms the location, then it is
     * returned as a result of this activity and finish it. Otherwise just hide the dialog
     *
     * @param latLng Selected Latitude and Longitude
     */
    private void showLocation(final LatLng latLng) {
        new CargaReporteAsyncTask().execute(latLng);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(Boolean.TRUE);
        selectCurrentPosition();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MapsActivity.this, ClimaActivity.class);
        AnimationUtils.configurarAnimacion(MapsActivity.this, mInfoMapaLl, true, intent);
    }

    /**
     * Esta clase realiza la carga inicial de manera asíncrona
     */
    private class CargaReporteAsyncTask extends AsyncTask<LatLng, Void, Void> {
        /** Progress Dialog **/
        private ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(MapsActivity.this, "", "Loading");
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(LatLng... params) {
            mReporte = climaService.obtenerReporteClima(String.valueOf(params[0].latitude), String.valueOf(params[0].longitude));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (mReporte == null) {
                enableProgressWheel(false);
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                return;
            }
            mEstacionTv.setText(mReporte.getNombreEstacion());
            mTempTv.setText(mReporte.getTemperatura() + "°C");
            mHumedadTv.setText(String.valueOf(mReporte.getHumedad()) + "%");
            mNubesTv.setText(mReporte.getNubes());
            mLatTv.setText(String.valueOf(mReporte.getLat()));
            mLonTv.setText(String.valueOf(mReporte.getLongitud()));

            enableProgressWheel(false);
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            AnimationUtils.configurarAnimacion(MapsActivity.this, mInfoMapaLl, false);

            super.onPostExecute(aVoid);
        }
    }

    /**
     * This method enables the Progress Wheel and hide the related views
     *
     * @param enable
     *         Enable/Disable
     */
    private void enableProgressWheel(boolean enable) {
        mLoadingPw.setVisibility(enable ? View.VISIBLE : View.GONE);
        mMarkerIcon.setVisibility(enable ? View.GONE : View.VISIBLE);

        if (mLoadingPw.isSpinning()) {
            mLoadingPw.stopSpinning();
        }

        if (enable) {
            mLoadingPw.spin();
        }
    }
}
