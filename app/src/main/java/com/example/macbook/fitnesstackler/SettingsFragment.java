package com.example.macbook.fitnesstackler;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {

    public static SettingsFragment NewInstance()
    {
        var frag4 = new SettingsFragment { Arguments = new Bundle() };
        return frag4;
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.settings_codec);
    }
}
