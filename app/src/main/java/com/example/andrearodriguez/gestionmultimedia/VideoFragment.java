package com.example.andrearodriguez.gestionmultimedia;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.widget.MediaController;
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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;

import static android.app.Activity.RESULT_OK;

public class VideoFragment extends Fragment {

    private static final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private VideoView videoCamara;
    private TextView rutaVideo;
    private Uri videoUri;

    private static final int REQUEST_CODE = 1;

    public VideoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        int read = ActivityCompat.checkSelfPermission(getActivity(), PERMISSIONS[0]);
        int write = ActivityCompat.checkSelfPermission(getActivity(), PERMISSIONS[1]);

        if (read == PackageManager.PERMISSION_DENIED || write == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, REQUEST_CODE);
        }

        videoCamara = (VideoView) view.findViewById(R.id.video_storage);
        rutaVideo = (TextView) view.findViewById(R.id.txt_ruta_video);



        view.findViewById(R.id.btn_abrir_video)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 2);
                    }
                });

        view.findViewById(R.id.btn_capturar_video)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                        File videoFolder = new File(Environment.getExternalStorageDirectory(), "CameraVideoFolder");
                        videoFolder.mkdirs();

                        File video = new File(videoFolder, "video.mp4");
                        videoUri = Uri.fromFile(video);

                        videoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
                        startActivityForResult(videoIntent, 1);
                    }
                });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            activity.updateView(getString(R.string.titulo), (getString(R.string.video)));
            activity.navigationView.setCheckedItem(R.id.nav_video);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        MediaController mediaController = new MediaController(getActivity());

        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    rutaVideo.setText(Environment.getExternalStorageDirectory()+ "/CameraVideoFolder/video.mp4");
                    videoCamara.setMediaController(mediaController);
                    videoCamara.setVideoURI(videoUri);
                    videoCamara.start();
                    mediaController.setAnchorView(videoCamara);

                    videoCamara.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            videoCamara.start();
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "Ha ocurrido un error al guardar el video", Toast.LENGTH_SHORT).show();
                }
                break;

            case 2:{
                if(resultCode == Activity.RESULT_OK && data != null){
                    Uri selectedVideo = data.getData();
                    String[] path = {MediaStore.Video.Media.DATA};
                    Cursor cursor = getActivity().getContentResolver().query(selectedVideo, path, null, null, null);
                    assert cursor != null;
                    cursor.moveToFirst();
                    int column = cursor.getColumnIndex(path[0]);
                    String videoPath = cursor.getString(column);
                    rutaVideo.setText(videoPath);

                    videoCamara.setMediaController(mediaController);
                    videoCamara.setVideoPath(videoPath);
                    videoCamara.start();
                    mediaController.setAnchorView(videoCamara);

                    videoCamara.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            videoCamara.start();
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "Ha ocurrido un error al guardar el video", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
}


