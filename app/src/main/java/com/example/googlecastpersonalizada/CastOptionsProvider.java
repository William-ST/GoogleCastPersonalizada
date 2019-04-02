package com.example.googlecastpersonalizada;

import android.content.Context;

import com.google.android.gms.cast.framework.CastOptions;
import com.google.android.gms.cast.framework.OptionsProvider;
import com.google.android.gms.cast.framework.SessionProvider;

import java.util.List;

public class CastOptionsProvider implements OptionsProvider {

        @Override
        public CastOptions getCastOptions(Context appContext) {
            CastOptions castOptions = new CastOptions.Builder().setReceiverApplicationId("848CAF5C").build();
            return castOptions;
        }

        @Override
        public List<SessionProvider> getAdditionalSessionProviders(Context context) {
            return null;
        }

    }
