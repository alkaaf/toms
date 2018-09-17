package com.spil.dev.tms.Activity.Fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.spil.dev.tms.Activity.Maps.LocationBroadcaster;

public class FragmentBase extends Fragment {

    @Override
    public void onStart() {
        super.onStart();

    }

    public void uiRunner(Runnable runnable) {
        if (getActivity() != null && getContext() != null && !getActivity().isFinishing() && runnable != null) {
            getActivity().runOnUiThread(runnable);
        }
    }
}
