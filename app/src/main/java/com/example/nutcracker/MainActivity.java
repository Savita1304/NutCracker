package com.example.nutcracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button btn1,btn2;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbarr);
        btn1=findViewById(R.id.btn1);

        setSupportActionBar(toolbar);


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(MainActivity.this, UploadActivity.class);
                MainActivity.this.startActivity(mainIntent);
                MainActivity.this.finish();
            }
        });



       /* if (getSupportActionBar()!=null){
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        }*/
       // toolbar.setTitle("nutcracker");

   }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.opt_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId==R.id.about){
            Toast.makeText(this, "Create new file", Toast.LENGTH_SHORT).show();
        }else if (itemId== R.id.term){
            Toast.makeText(this, "term file", Toast.LENGTH_SHORT).show();

        }else {
            Toast.makeText(this, "else file", Toast.LENGTH_SHORT).show();

        }
        return super.onOptionsItemSelected(item);
    }
}


/*package com.example.nutcracker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UploadActivity extends AppCompatActivity {

    private static final int SELECT_VIDEO = 3;
    private Uri videoUri;
    public static final String UPLOADIMG_URL = "http://www.visitindia.com/fbapp/bgreeting/mediaimgup.jsp";
    public static final String UPLOADVIDEO_URL = "http://www.visitindia.com/fbapp/bgreeting/mediavideoup.jsp?gid=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        Button btnSelectVideo = findViewById(R.id.btnSelectVideo);
        Button btnUploadVideo = findViewById(R.id.btnUploadVideo);

        btnSelectVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectVideo();
            }
        });

        btnUploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoUri != null) {
                    String videoPath = getPath(videoUri);
                    if (videoPath != null) {
                        new UploadVideoTask().execute(videoPath);
                    } else {
                        Toast.makeText(UploadActivity.this, "Failed to get video path", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(UploadActivity.this, "Please select a video first", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void selectVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a Video"), SELECT_VIDEO);
    }

    @Nullable
    private String getPath(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_VIDEO && resultCode == RESULT_OK && data != null && data.getData() != null) {
            videoUri = data.getData();
            String videoPath = getPath(videoUri);
            if (videoPath != null) {
                Toast.makeText(this, "Video selected: " + videoPath, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to get video path", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class UploadVideoTask extends AsyncTask<String, Void, String> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(UploadActivity.this);
            progressDialog.setMessage("Uploading video...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            Upload upload = new Upload();
            return upload.uploadVideo(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            Toast.makeText(UploadActivity.this, result, Toast.LENGTH_LONG).show();
        }
    }

    public class Upload {
        public static final String UPLOAD_URL = "http://www.visitindia.com/fbapp/bgreeting/mygreeting.jsp?gid=";
        private int serverResponseCode;

        public String uploadVideo(String file) {
            String fileName = file;
            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;

            File sourceFile = new File(file);
            if (!sourceFile.isFile()) {
                Log.e("Upload", "Source File Does not exist: " + file);
                return "Source File Does not exist";
            }

            try {
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(UPLOAD_URL);
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("myFile", fileName);
                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"myFile\";filename=\"" + fileName + "\"" + lineEnd);
                dos.writeBytes(lineEnd);

                bytesAvailable = fileInputStream.available();
                Log.d("Upload", "Initial .available: " + bytesAvailable);

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.d("Upload", "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);

                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {
                Log.e("Upload", "URL error: " + ex.getMessage(), ex);
                return "URL error: " + ex.getMessage();
            } catch (Exception e) {
                Log.e("Upload", "Exception: " + e.getMessage(), e);
                return "Exception: " + e.getMessage();
            }

            if (serverResponseCode == 200) {
                StringBuilder sb = new StringBuilder();
                try {
                    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;
                    while ((line = rd.readLine()) != null) {
                        sb.append(line);
                    }
                    rd.close();
                } catch (IOException ioex) {
                    Log.e("Upload", "IO Exception: " + ioex.getMessage(), ioex);
                    return "IO Exception: " + ioex.getMessage();
                }
                return "Video uploaded successfully";
            } else {
                Log.e("Upload", "Could not upload, Server Response Code: " + serverResponseCode);
                return "Could not upload, Server Response Code: " + serverResponseCode;
            }
        }
    }
}
*/