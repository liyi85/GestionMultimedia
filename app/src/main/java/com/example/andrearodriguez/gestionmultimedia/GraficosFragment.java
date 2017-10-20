package com.example.andrearodriguez.gestionmultimedia;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class GraficosFragment extends Fragment {

    public GraficosFragment() {
    }

    private GLSurfaceView lienzo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graficos, container, false);

        lienzo = (GLSurfaceView) view.findViewById(R.id.lienzo);
        lienzo.setRenderer(new MyRenderer(this.getActivity()));

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            activity.updateView(getString(R.string.titulo), (getString(R.string.graficos)));
            activity.navigationView.setCheckedItem(R.id.nav_graficos);

        }
    }

}
