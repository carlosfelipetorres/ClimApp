package com.domicilios.carlos.climapp;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;

/**
 * @author <a href="mailto:carlosfelipetorres75@gmail.com">Carlos Torres</a>
 */
public class AppUtils {
    /**
     * Este método crea un Toast básico
     *
     * @param context  Contexto donde se va a ejecutar el Toast
     * @param mensaje  Mensaje del Toast
     * @param duracion Duración (usando las variables SuperToast.Duration.LONG, SuperToast.Duration.MEDIUM,
     *                 etc)
     * @return SuperToast básico
     */
    public static SuperToast crearToast(Context context, String mensaje, int duracion,
                                        TipoNotificacion tipoNotificacion) {

        return SuperToast.create(context, mensaje, duracion,
                Style.getStyle(clasificarEstiloNotificacion(tipoNotificacion, Boolean.FALSE),
                        SuperToast.Animations.FLYIN));
    }

    /**
     * Este método clasifica el estilo de la notificación dependiendo de su tipo
     *
     * @param tipoNotificacion Tipo de notificación a ser mostrada
     * @return Color del estilo
     */
    private static int clasificarEstiloNotificacion(TipoNotificacion tipoNotificacion,
                                                    boolean esCard) {
        int style;
        switch (tipoNotificacion) {
            default:
            case INFORMATIVA:
                style = (esCard ? SuperToast.Background.BLUE : Style.BLUE);
                break;
            case ALERTA:
                style = (esCard ? SuperToast.Background.ORANGE : Style.ORANGE);
                break;
            case EXITOSA:
                style = (esCard ? SuperToast.Background.GREEN : Style.GREEN);
                break;
            case ERROR:
                style = (esCard ? SuperToast.Background.RED : Style.RED);
                break;
        }

        return style;
    }

    /**
     * Este método inicializa el swipe and refresh estándar de DBD+
     *
     * @param refreshLayout SwipeRefreshLayout a inicializar
     */
    public static void initSwipeRefreshLayout(SwipeRefreshLayout refreshLayout) {
        refreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.black,
                R.color.colorAccent, R.color.caldroid_sky_blue);
    }

    /**
     * This method enables/disables the progress wheel. All related views will be hide/visible
     * depending on the progress wheel state
     *
     * @param pw     The progress wheel
     * @param enable Enable/Disable
     * @param views  Related views that will be hide/visible depending on the progress wheel state
     */
    public static void enableProgressWheel(ProgressWheel pw, boolean enable, View... views) {
        if (pw == null) {
            return;
        }
        pw.setVisibility(enable ? View.VISIBLE : View.INVISIBLE);
        if (pw.isSpinning()) {
            pw.stopSpinning();
        }

        if (enable) {
            pw.spin();
        }

        for (View view : views) {
            if (view != null) {
                view.setVisibility(enable ? View.INVISIBLE : View.VISIBLE);
            }
        }
    }
}
