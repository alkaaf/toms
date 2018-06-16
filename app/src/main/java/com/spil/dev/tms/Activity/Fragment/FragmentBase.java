package com.spil.dev.tms.Activity.Fragment;

import android.support.v4.app.Fragment;

public class FragmentBase extends Fragment {
    public void uiRunner(Runnable runnable) {
        if (getActivity() != null && getContext() != null && !getActivity().isFinishing() && runnable != null) {
            getActivity().runOnUiThread(runnable);
        }
    }
}
