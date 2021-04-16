package com.hci.projectkeyboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrialActivity extends AppCompatActivity {

    private String keyboardType;
    private Intent trialIntent;
    private boolean isActualTrial = false;

    private int TRIALS = 15;

    private List<String> phrases;

    private ArrayList<Integer> indexes;
    private int previousIndex;

    private TextView parentSentence;
    private Button nextButton;
    private EditText phraseEditText;

    private long startTypingTimer, endTypingTimer;
    private long timeTaken = 0;

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trial);

        trialIntent = getIntent();
        keyboardType = trialIntent.getStringExtra("keyboard_type");
        if (keyboardType != null && (keyboardType.equals("GBoard") || keyboardType.equals("Sliding Keyboard"))) {
            isActualTrial = true;
        }
        indexes = trialIntent.getIntegerArrayListExtra("indexes");
        if (indexes == null) {
            indexes = new ArrayList<Integer>();
        }
        previousIndex = trialIntent.getIntExtra("previous_index", -1);

        parentSentence = findViewById(R.id.textView3);

        nextButton = findViewById(R.id.button2);
        attachListenerToNextButton();

        preparePhrases();
        initiatePhrase();

        phraseEditText = findViewById(R.id.editTextTextPersonName);
        attachListenerToEditText();

        if (isActualTrial) {
            // TODO set up database helper here
//            db_helper = new DatabaseHelper(this);
        } else {

        }
    }

    private void preparePhrases() {
        phrases = new ArrayList<String>();
        try {
            Resources res = getResources();
            InputStream ins = res.openRawResource(R.raw.phrases);

//            File file = new File("phrases.txt");
//            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(ins));
            String line;
            while ((line = br.readLine()) != null) {
                phrases.add(line);
            }
//            fr.close();
        } catch (IOException ignored) {
        }
    }

    private void attachListenerToNextButton() {
        nextButton.setOnClickListener(v -> {
            timeTaken = endTypingTimer - startTypingTimer;
            // TODO calculate error rate here
            // TODO write into database here
            switchActivity();
        });
    }

    private void attachListenerToEditText() {
        phraseEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // first letter
                if (s.length() == 1) {
                    startTypingTimer = System.currentTimeMillis();
                }
                endTypingTimer = System.currentTimeMillis();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initiatePhrase() {
        if (indexes.size() == 0) {
            List<Integer> temporaryIndex = new ArrayList<Integer>();
            for (int i = 0; i < phrases.size(); i++) {
                temporaryIndex.add(i);
            }
            Collections.shuffle(temporaryIndex);
            for (int i = 0; i < TRIALS; i++) {
                indexes.add(temporaryIndex.get(i));
            }
        }
        int currentIndex = ++previousIndex;
        parentSentence.setText(phrases.get(indexes.get(currentIndex)));
        this.setTitle("Trial: " + (currentIndex + 1) + "/" + TRIALS);
    }

    private void switchActivity() {
        if (previousIndex >= TRIALS - 1) {
            setTitle("End");
        } else {
            Intent nextIntent = new Intent(TrialActivity.this, TrialActivity.class);
            nextIntent.putExtra("keyboard_type", keyboardType);
            nextIntent.putIntegerArrayListExtra("indexes", indexes);
            nextIntent.putExtra("previous_index", previousIndex);

            TrialActivity.this.startActivity(nextIntent);
        }
    }
}
