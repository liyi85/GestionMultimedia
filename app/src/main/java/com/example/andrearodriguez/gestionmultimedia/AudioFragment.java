package com.example.andrearodriguez.gestionmultimedia;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by ronald on 4/10/17.
 */

public class AudioFragment extends Fragment {

    private static final int REQUEST_CODE=1;
    private static final String[] PERMISOS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
    };

    private Button btnAbrir;
    private Button btnGrabar;
    private Button btnReproducir;
    TextView txtRuta;

    MediaPlayer audio = null;
    private MediaRecorder mediaRecorder = null;
    String path, nombreAudio;

    boolean verificacion = true;
    boolean verificacion2 = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_audio, container, false);

        int leer = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int leer2 = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO);

        if (leer == PackageManager.PERMISSION_DENIED || leer2 == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(getActivity(),PERMISOS,REQUEST_CODE);
        }

        btnAbrir = (Button) view.findViewById(R.id.btn_abrir_audio);
        btnReproducir = (Button) view.findViewById(R.id.btn_reproducir_audio);
        btnGrabar = (Button) view.findViewById(R.id.btn_capturar_audio);
        txtRuta = (TextView) view.findViewById(R.id.txt_ruta_audio);
        nombreAudio = Environment.getExternalStorageDirectory() + "/grabacion.m4a";

        btnAbrir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                intent.setType("audio/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Selecciona un audio"), 1);
            }
        });

        btnReproducir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlay (verificacion);
            }
        });

        btnGrabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grabando(verificacion2);
            }
        });

        return view;
    }

    public void grabando (boolean verificacion2){
        if (verificacion2) {
            comenzarGrabacion();
        } else {
            detenerGrabacion();
        }
    }

    public void comenzarGrabacion(){
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(nombreAudio);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            Toast.makeText(getActivity(), "No se grabará correctamente", Toast.LENGTH_SHORT).show();
        }

        mediaRecorder.start();
        Toast.makeText(getActivity(), "Grabando audio...", Toast.LENGTH_SHORT).show();
        verificacion2 = !verificacion2;
        btnGrabar.setText(R.string.detener_grabacion);
    }

    public void detenerGrabacion(){
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;

        btnGrabar.setText(R.string.iniciar_grabacion);
        txtRuta.setText(nombreAudio);
        verificacion2 = !verificacion2;
        Toast.makeText(getActivity(), "Se guardó el audio correctamente", Toast.LENGTH_SHORT).show();
    }


    public void onPlay (boolean verificacion) {
        if (!verificacion){
            btnReproducir.setText(R.string.reproducir_audio);
            detenerReproduccion();
        } else {
            btnReproducir.setText(R.string.detener_reproduccion);
            comenzarReproduccion();
        }
    }

    public void comenzarReproduccion(){
        verificacion = !verificacion;

        try {
            audio = new MediaPlayer();
            audio.setDataSource(getActivity().getApplicationContext(), Uri.parse(txtRuta.getText().toString()));

            audio.prepare();
            audio.start();

            audio.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer audio) {
                    btnReproducir.setText(R.string.reproducir_audio);
                    Toast.makeText(getActivity(), "Audio finalizado", Toast.LENGTH_SHORT).show();
                    verificacion = !verificacion;
                }
            });

        } catch (IOException e) {
            Toast.makeText(getActivity(), "Error al reproducir audio", Toast.LENGTH_SHORT).show();;
            btnReproducir.setText(R.string.reproducir_audio);
            verificacion = !verificacion;
        }
    }

    public void detenerReproduccion(){
        audio.release();
        audio = null;
        verificacion = !verificacion;
        Toast.makeText(getActivity(), "Audio detenido", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == getActivity().RESULT_OK) {
            path = data.getDataString();
            txtRuta.setText(path);

            onPlay (verificacion);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mediaRecorder != null){
            mediaRecorder.release();
            mediaRecorder = null;
        }

        if (audio != null){
            audio.release();
            audio = null;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            activity.updateView(getString(R.string.titulo), (getString(R.string.audio)));
            activity.navigationView.setCheckedItem(R.id.nav_audio);
            activity.onBackPressed();
        }
    }
}