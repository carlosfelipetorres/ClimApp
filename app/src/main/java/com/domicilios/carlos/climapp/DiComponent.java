package com.domicilios.carlos.climapp;

import android.app.Activity;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Dependency Injection component where all Activities, fragments and adapters are injected
 *
 * @author <a href="mailto:carlosfelipetorres75@gmail.com">Carlos Torres</a>
 */
@Singleton
@Component(modules = {ServicesModule.class})
public interface DiComponent {

    /**
     * Context injection
     *
     * @return Injected context
     */
    Context context();

    // Clima Activitie
    void inject(ClimaActivity activity);

    // Maps Activity
    void inject(MapsActivity activity);

    // Main Activity
    void inject(MainActivity activity);

}
