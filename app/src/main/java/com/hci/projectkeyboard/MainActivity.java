package com.hci.projectkeyboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton radioButton;
    Button button;

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addListenerButton();
    }

    private void addListenerButton() {
        radioGroup = findViewById(R.id.radioGroup);
        button = findViewById(R.id.button);
        button.setOnClickListener(v -> {
            int selectedID = radioGroup.getCheckedRadioButtonId();
            radioButton = findViewById(selectedID);
            Intent intent = new Intent(MainActivity.this, TrialActivity.class);
            intent.putExtra("keyboard_type", radioButton.getText().toString());
            startActivity(intent);
        });
    }
}