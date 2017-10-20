package com.example.andrearodriguez.gestionmultimedia;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class ImagenFragment extends Fragment {

    private static final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private ImageView imgCamara;
    private TextView rutaImagen;

    private static final int REQUEST_CODE = 1;

    public ImagenFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_imagen, container, false);
        int read = ActivityCompat.checkSelfPermission(getActivity(), PERMISSIONS[0]);
        int write = ActivityCompat.checkSelfPermission(getActivity(), PERMISSIONS[1]);

        if(read == PackageManager.PERMISSION_DENIED || write == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, REQUEST_CODE);
        }

        imgCamara = (ImageView) view.findViewById(R.id.image_storage);
        rutaImagen = (TextView) view.findViewById(R.id.txt_ruta);

        view.findViewById(R.id.btn_abrir)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 2);
                    }
                });

        view.findViewById(R.id.btn_capturar)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        File cameraFolder = new File(Environment.getExternalStorageDirectory(), "CameraPicturesFolder");
                        cameraFolder.mkdirs();

                        File picture = new File(cameraFolder, "imagen.jpg");
                        Uri pictureUri = Uri.fromFile(picture);

                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
                        startActivityForResult(cameraIntent, 1);
                    }
                });

        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:{
                if(resultCode == Activity.RESULT_OK){
                    String picturePath = Environment.getExternalStorageDirectory() + "/CameraPicturesFolder/imagen.jpg";
                    Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                    int height = bitmap.getHeight();
                    int width = bitmap.getWidth();
                    float scaleA = ((float) (height/2))/width;
                    float scaleB = ((float) (height/2))/height;
                    Matrix matrix = new Matrix();
                    matrix.postScale(scaleA, scaleB);
                    Bitmap newPicture = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
                    imgCamara.setImageBitmap(newPicture);
                    rutaImagen.setText(String.format("Ruta: %s", picturePath));
                }else{
                    Toast.makeText(getActivity(), "Ha ocurrido un error al guardar la foto", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case 2:{
                if(resultCode == Activity.RESULT_OK && data != null){
                    Uri selectedPicture = data.getData();
                    String[] path = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getActivity().getContentResolver().query(selectedPicture, path, null, null, null);
                    assert cursor != null;
                    cursor.moveToFirst();
                    int column = cursor.getColumnIndex(path[0]);
                    String picturePath = cursor.getString(column);
                    Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    int height = bitmap.getHeight();
                    int width = bitmap.getWidth();
                    float scaleA = ((float) (height/2))/width;
                    float scaleB = ((float) (height/2))/height;
                    Matrix matrix = new Matrix();
                    matrix.postScale(scaleA, scaleB);
                    Bitmap newPicture = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
                    imgCamara.setImageBitmap(newPicture);
                    rutaImagen.setText(String.format("Ruta: %s", picturePath));
                }else{
                    Toast.makeText(getActivity(), "Ha ocurrido un error al cargar la foto", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            default:{
                Toast.makeText(getActivity(), "Ha ocurrido un error con la foto", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            activity.updateView(getString(R.string.titulo), (getString(R.string.imagen)));
            activity.navigationView.setCheckedItem(R.id.nav_imagenes);

        }
    }

}
