package com.example.nutcracker;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import android.webkit.MimeTypeMap;

public class UploadActivity extends AppCompatActivity {

    private Uri imageUri;
    ImageView img;
    public static String UPLOADIMAGE_URL = "http://www.visitindia.com/fbapp/bgreeting/mediavideoup.jsp?gid=";

    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        Button btnSelectimg = findViewById(R.id.btnSelectimg);
        Button btnUploadimg = findViewById(R.id.btnUploadimg);
        img = findViewById(R.id.img);

        btnSelectimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        btnUploadimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri != null) {
                    String filePath = getPathFromUri(imageUri);
                    if (filePath != null) {
                        uploadImageToServer(filePath);
                    } else {
                        Toast.makeText(UploadActivity.this, "Failed to get file path", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(UploadActivity.this, "Please select an image first", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        openGallery.launch(intent);
    }

    ActivityResultLauncher<Intent> openGallery = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                imageUri = result.getData().getData();
                img.setImageURI(imageUri);
            }
        }
    });

    private String getPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        return null;
    }


    private void uploadImageToServer(String filePath) {

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("", "",
                        RequestBody.create(MediaType.parse("image/*"), encodedImage))
                .build();

        Request request = new Request.Builder()
                .url(serverUrl)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }
/*
    private void uploadImageToServer(String filePath) {
        File file = new File(filePath);
        String contentType = getMimeType(filePath);

        if (contentType == null) {
            contentType = "image/jpeg"; // Default to JPEG if mime type is unknown
        }

        RequestBody fileBody = RequestBody.create(file, MediaType.parse(contentType));

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), fileBody)
                .build();

        Request request = new Request.Builder()
                .url(UPLOADIMAGE_URL)  // Replace with your server's URL
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UploadActivity.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseBody = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(UploadActivity.this, "Upload successful: " + responseBody, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(UploadActivity.this, "Upload failed: " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
*/

    @Nullable
    private String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }
}







//*package com.example.nutcracker;
//
//import android.content.Intent;
//import android.database.Cursor;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import java.io.File;
//import java.io.IOException;
//
//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.MediaType;
//import okhttp3.MultipartBody;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;
//import android.webkit.MimeTypeMap;
//
//public class UploadActivity extends AppCompatActivity {
//
//    private static final int SELECT_VIDEO = 3;
//    private Uri videoUri;
//   // public static final String UPLOAD_URL = "http://www.visitindia.com/fbapp/bgreeting/mygreeting.jsp?gid=";
//  public static String UPLOADVIDEO_URL ="http://www.visitindia.com/fbapp/bgreeting/mediavideoup.jsp?gid=";
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_upload);
//
//        Button btnSelectimg = findViewById(R.id.btnSelectimg);
//        Button btnUploadimg = findViewById(R.id.btnUploadimg);
//
//        btnSelectimg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selectVideo();
//            }
//        });
//
//        btnUploadimg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (videoUri != null) {
//                    String storageUrl = getPath(videoUri); // Convert Uri to file path
//                    videoUploadTo(UPLOADVIDEO_URL, storageUrl);
//                } else {
//                    Toast.makeText(UploadActivity.this, "Please select a video first", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
//
//    private void selectVideo() {
//        Intent intent = new Intent();
//        intent.setType("video/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select a Video"), SELECT_VIDEO);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == SELECT_VIDEO && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            videoUri = data.getData();
//        }
//    }
//
//    public String getPath(Uri uri) {
//        String[] projection = {MediaStore.Video.Media.DATA};
//        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
//        if (cursor != null) {
//            cursor.moveToFirst();
//            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
//            String path = cursor.getString(columnIndex);
//            cursor.close();
//            return path;
//        }
//        return null;
//    }
//
//    private String getMimeType(String path) {
//        String extension = MimeTypeMap.getFileExtensionFromUrl(path);
//        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
//    }
//
//    public void videoUploadTo(String url, String storageUrl) {
//        OkHttpClient client = new OkHttpClient();
//        File videoFile = new File(storageUrl);
//        String content_type = getMimeType(videoFile.getPath());
//        RequestBody fileBody = RequestBody.create(MediaType.parse(content_type), videoFile);
//
//        RequestBody requestBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("type", content_type)
//                .addFormDataPart("uploaded_video", videoFile.getName(), fileBody)
//                .build();
//
//        Request request = new Request.Builder()
//                .url(url)
//                .put(requestBody)
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.d("onFailure video", e.getMessage());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    Log.d("onResponse video", "done"+ response.message());
//                } else {
//                    Log.d("onResponse video", "Error: " + response.message());
//                }
//            }
//        });
//    }
//}*/