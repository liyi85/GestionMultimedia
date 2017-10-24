package com.example.andrearodriguez.gestionmultimedia;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

import static android.app.Activity.RESULT_OK;


public class AudioFragment extends Fragment {

    static final int Pick_song=1;
    private static final int REQUEST_CODE=1;
    private static final String[] PERMISOS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
    };

    private Button abrir;
    private Button grabar;
    private Button reproducir;
    private TextView ruta;

    private static String nombreAudio = null;
    private MediaRecorder mediaRecorder = null;
    private MediaPlayer mediaPlayer = null;
    private MediaPlayer audio = null;

    boolean verificacion = true;
    boolean verificacion2 = true;
    boolean verificacion3 = true;

    private int x=0;

    private void limpiarAudio(){
        if (audio != null){
            audio.release();
            audio = null;}
    }

    private void limpiarMedia(){
        if (mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;}
    }

    private void limpiarRecorder(){
        if (mediaRecorder != null){
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    private void comenzarGrabacion(){
        limpiarMedia();
        limpiarAudio();
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(nombreAudio);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try{
            mediaRecorder.prepare();
        }catch(IOException e){
            Toast.makeText(getActivity(), "No se grabará correctamente", Toast.LENGTH_SHORT).show();
        }
        mediaRecorder.start();
    }

    private void detenerGrabacion(){
        limpiarAudio();
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
        Toast.makeText(getActivity(), "Se ha guardado el audio en:\n" + Environment.getExternalStorageDirectory() + "/audio.3gp", Toast.LENGTH_LONG).show();
    }

    private void grabando(boolean comenzado){
        limpiarAudio();
        if (comenzado){
            comenzarGrabacion();
        }else{
            detenerGrabacion();
        }
    }

    private void comenzarReproduccion(String ruta){
        limpiarAudio();
        mediaPlayer = new MediaPlayer();
        try{
            mediaPlayer.setDataSource(ruta);
            mediaPlayer.prepare();
            mediaPlayer.start();
        }catch(IOException e){
            Toast.makeText(getActivity(), "Ha ocurrido un error en la reproducción"+ ruta, Toast.LENGTH_SHORT).show();
        }
    }
    private void comenzarReproduccionMemoria(String ruta){
        limpiarMedia();
        audio = new MediaPlayer();
        try{
            audio.setDataSource(getActivity().getApplicationContext(), Uri.parse(ruta));
            audio.prepare();
            audio.start();
        }catch(IOException e){
            Toast.makeText(getActivity(), "Ha ocurrido un error en la reproducción"+ ruta, Toast.LENGTH_SHORT).show();
        }
    }

    private void detenerReproduccion(){
        mediaPlayer.release();
        mediaPlayer = null;
    }

    private void detenerReproduccionMemoria(){
        audio.release();
        audio = null;
    }

    private void onPlay(boolean comenzarRep){
        limpiarAudio();
        if (comenzarRep){
            comenzarReproduccion(nombreAudio);
        }else{
            detenerReproduccion();
        }
    }

    private void onPlayMemoria(boolean comenzarRep, String data){
        limpiarMedia();
        if (comenzarRep){
            comenzarReproduccionMemoria(data);
        }else{
            detenerReproduccionMemoria();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_audio, container, false);

        int leer = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int leer2 = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO);

        if (leer == PackageManager.PERMISSION_DENIED || leer2 == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(getActivity(),PERMISOS,REQUEST_CODE);
        }

        abrir = view.findViewById(R.id.btn_abrir_audio);
        grabar = view.findViewById(R.id.btn_capturar_audio);
        reproducir = view.findViewById(R.id.btn_reproducir_audio);
        ruta = view.findViewById(R.id.txt_ruta_audio);
        nombreAudio = Environment.getExternalStorageDirectory() + "/audio.3gp";


        abrir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limpiarMedia();
                Intent intent = new Intent();
                intent.setType("audio/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Selecciona un audio"),Pick_song);
            }
        });

        grabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                grabando(verificacion);
                    if (verificacion) {
                        grabar.setText("Detener Grabación");
                    } else {
                        grabar.setText("Iniciar Grabación");
                    }
                    verificacion = !verificacion;
            }
        });

        reproducir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlay(verificacion2);
                if (verificacion2){
                    reproducir.setText("Detener reproducción");
                }else{
                    reproducir.setText("Reproducir");
                }
                verificacion2 = !verificacion2;
            }
        });

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        limpiarMedia();
        switch (requestCode) {
            case Pick_song:
                if (resultCode == RESULT_OK) {
                    final String patch = data.getDataString();
                    onPlayMemoria(verificacion3, patch);
                    if(audio.isPlaying()){
                        reproducir.setText("Detener reproducción");
                        verificacion3 = false;
                    }
                    try {
                        reproducir.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (verificacion3==true){
                                    audio.seekTo(x);
                                    audio.start();
                                    reproducir.setText("Detener reproducción");

                                }else{
                                    reproducir.setText("Reproducir");
                                    audio.pause();
                                    x = audio.getCurrentPosition();
                                }
                                verificacion3 = !verificacion3;
                            }
                        });

                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "Erro al ejecutar el audio", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            activity.updateView(getString(R.string.titulo), (getString(R.string.audio)));
            activity.navigationView.setCheckedItem(R.id.nav_audio);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        limpiarRecorder();
        limpiarMedia();
        limpiarAudio();
    }
}

