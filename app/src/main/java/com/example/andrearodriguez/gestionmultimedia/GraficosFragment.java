package com.example.andrearodriguez.gestionmultimedia;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class GraficosFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_graficos, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

    /**private GLSurfaceView lienzo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lienzo = new GLSurfaceView(getContext());
        lienzo.setRenderer(new MyRenderer(getContext()));

        getActivity().setContentView(lienzo);
    }*/
}
