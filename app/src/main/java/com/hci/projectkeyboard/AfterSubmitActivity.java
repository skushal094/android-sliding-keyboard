package com.hci.projectkeyboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AfterSubmitActivity extends AppCompatActivity {

    TextView receiver_msg;
    boolean isActualTrial = false;
    DatabaseHelper db_helper;
    List<SessionDataEntry> dataEntryList;
    Button shareButton;

    @Override
    public void onBackPressed() {

    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_submit);

        receiver_msg = findViewById(R.id.text_input_type);
        Intent intent = getIntent();
        String str = intent.getStringExtra("keyboard_type");
        isActualTrial = intent.getBooleanExtra("is_actual_trial", false);
        if (!isActualTrial) {
            shareButton = findViewById(R.id.button);
            shareButton.setText("GO TO HOME");
        }
        db_helper = new DatabaseHelper(this);
        receiver_msg.setText(str);
    }

    public void export(View view) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            // this will request for permission when user has not granted permission for the app
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        } else if (isActualTrial) {

            //generate data
            dataEntryList = db_helper.getAllSessionDataEntry();

            StringBuilder data = new StringBuilder();
            data.append("keyboard,phrase_no,original_phrase,transcribed_phrase,time_taken,error_rate," +
                    "start_time,end_time,MSD,id");
            for (SessionDataEntry sessionDataEntry : dataEntryList) {
                data.append("\n")
                        .append(sessionDataEntry.keyboard).append(",")
                        .append(sessionDataEntry.phrase_no).append(",")
                        .append(sessionDataEntry.original_phrase).append(",")
                        .append(sessionDataEntry.transcribed_phrase).append(",")
                        .append(sessionDataEntry.time_taken).append(",")
                        .append(sessionDataEntry.error_rate).append(",")
                        .append(sessionDataEntry.start_time).append(",")
                        .append(sessionDataEntry.end_time).append(",")
                        .append(sessionDataEntry.msd).append(",")
                        .append(sessionDataEntry.id);
            }

            try {
                Intent intent = getIntent();
                String str = intent.getStringExtra("keyboard_type");
                String date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ssZ", Locale.getDefault()).format(new Date());
                String filename = str.replace(" ", "").toLowerCase() + "_" + date + ".csv";

                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), filename);

                FileOutputStream out = new FileOutputStream(file);
                out.write((data.toString()).getBytes());
                out.close();

                Toast.makeText(AfterSubmitActivity.this, "Saved", Toast.LENGTH_SHORT).show();

                Intent intent1 = new Intent(AfterSubmitActivity.this, ThankYouActivity.class);
                intent1.putExtra("file_location", file.getAbsolutePath());
                startActivity(intent1);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(AfterSubmitActivity.this, "File not found", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(AfterSubmitActivity.this, "Error saving", Toast.LENGTH_SHORT).show();
            }
        } else {
            Intent intent = new Intent(AfterSubmitActivity.this, SelectionScreen.class);
            startActivity(intent);
            finish();
        }
    }
}
