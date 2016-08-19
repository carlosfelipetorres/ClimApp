package com.domicilios.carlos.climapp;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author <a href="mailto:carlosfelipetorres75@gmail.com">Carlos Torres</a>
 */
@Module
public class ServicesModule {

    /**
     * Context to be injected into dependencies
     **/
    private final Context mContext;

    /**
     * Services Module constructor
     *
     * @param context Application context
     */
    public ServicesModule(Context context) {
        mContext = context;
    }

    /**
     * Bind of the {@link IClimaService} with its implementation
     *
     * @return Climate Service implementation
     */
    @Provides
    @Singleton
    public IClimaService ClimaService() {
        return new ClimaService(mContext);
    }

    /**
     * Injection of the application context
     *
     * @return Application context
     */
    @Provides
    public Context context() {
        return mContext;
    }

}

