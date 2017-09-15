package com.example.feiling.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_ID_WRITE_PERMISSION = 200;
    private final String rootDirectory = "/storage/1D11-6DF7/Stuff";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private boolean askPermission(int requestId, String permissionName) {
        // Check if we have permission
        int permission = ActivityCompat.checkSelfPermission(this, permissionName);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // If don't have permission so prompt the user.
            this.requestPermissions(
                    new String[]{permissionName},
                    requestId);
            return false;
        } else {
            return true;
        }
    }

    public void testButtonClicked(View view) {
        EditText editText = (EditText) findViewById(R.id.editText);
        String editTextString = editText.getText().toString();
        boolean changeBack = editTextString.equals("back");

        boolean canWrite = this.askPermission(REQUEST_ID_WRITE_PERMISSION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (!canWrite) {
            editText.setText("Can't write");
            return;
        }

        File root = new File(rootDirectory);
        File[] files = root.listFiles();
        if (files == null) {
            editText.setText("No files found");
            return;
        }

        boolean succeed = false;
        for (File f : files) {
            String fileName = f.getAbsolutePath();

            if (changeBack) {
                // remove ".tmp"
                if (fileName.endsWith(".tmp")) {
                    String newFileName = fileName.substring(0, fileName.length() - 4);
                    succeed = f.renameTo(new File(newFileName));
                }
            } else {
                // append ".tmp"
                if (!fileName.endsWith(".tmp")) {
                    String newFileName = fileName + ".tmp";
                    succeed = f.renameTo(new File(newFileName));
                }
            }
        }

        editText.setText("Done");
    }
}
