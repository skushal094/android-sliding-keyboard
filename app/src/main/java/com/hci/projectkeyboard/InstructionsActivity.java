package com.hci.projectkeyboard;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class InstructionsActivity extends AppCompatActivity {

    Object res;
    Button buttonAccept;

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);

        addListenerButton();
    }

    public void Permission_Message() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }

    public void openDialog() {
        System.out.println("open dialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(InstructionsActivity.this);
        builder.setTitle("Notification");
        builder.setMessage("Please allow to access the storage");
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Permission_Message();
            }
        });

    }

    public void addListenerButton() {
        buttonAccept = findViewById(R.id.buttonAccept);
        buttonAccept.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(InstructionsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(InstructionsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // this will request for permission when user has not granted permission for the app
                ActivityCompat.requestPermissions(InstructionsActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

                if (ActivityCompat.checkSelfPermission(InstructionsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == -1) {
                    Toast.makeText(InstructionsActivity.this, "You must grant storage access.", Toast.LENGTH_SHORT).show();
                }

//                    InstructionsActivity.this.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(InstructionsActivity.this, "You must grant storage access.", Toast.LENGTH_SHORT).show();
//                        }
//                    });
            } else {
                Intent intent = new Intent(InstructionsActivity.this, SelectionScreen.class);
                startActivity(intent);
            }
        });
    }

}
//    public void SubmitButton(View view){
//        res = this;
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
//                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            // this will request for permission when user has not granted permission for the app
//            Permission_Message();
//            System.out.println("in if");

//            Toast.makeText(getBaseContext(),"hello",Toast.LENGTH_SHORT).show();
//        }
//        else{
//            Intent intent = new Intent(InstructionsActivity.this,StartScreenActivity.class);
//            startActivity(intent);
//        }
//    }