package com.example.andrearodriguez.gestionmultimedia;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AnimationFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.app_bar_main, container, false);

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
            activity.updateView(getString(R.string.titulo), (getString(R.string.animacion)));
            activity.navigationView.setCheckedItem(R.id.nav_animation);
        }
    }


    /*public void onClickPlay(View view) {
        ImageView imageView = (ImageView) getActivity().findViewById(R.id.image_storage);

        AnimatorSet animatorSet = (AnimatorSet) AnimatorInflater
                .loadAnimator(this.getActivity(), R.animator.animation_star);
        animatorSet.setTarget(imageView);
        animatorSet.start();
    }*/

}
