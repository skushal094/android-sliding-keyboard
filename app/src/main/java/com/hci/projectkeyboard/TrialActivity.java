package com.hci.projectkeyboard;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrialActivity extends AppCompatActivity {

    private String keyboardType;
    private Intent trialIntent;
    private boolean isActualTrial = false;

    private final int TRIALS = 15;

    private List<String> phrases;

    private ArrayList<Integer> indexes;
    private int previousIndex;

    private TextView parentSentence;
    private Button nextButton;
    private EditText phraseEditText;

    private long startTypingTimer, endTypingTimer;
    private long timeTaken = 0;

    private String originalPhrase, transcribedPhrase;

    private DatabaseHelper db_helper;

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
            db_helper = new DatabaseHelper(this);
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
            if (isActualTrial) {
                timeTaken = endTypingTimer - startTypingTimer;

                // calculate error rate here
                originalPhrase = (String) parentSentence.getText();
                transcribedPhrase = phraseEditText.getText().toString();

//            // replace multiple spaces with one
//            originalPhrase = originalPhrase.replaceAll("\\s+", " ");
//            transcribedPhrase = transcribedPhrase.replaceAll("\\s+", " ");
//
//            // trim the string
//            originalPhrase = originalPhrase.trim();
//            transcribedPhrase = transcribedPhrase.trim();

                int[][] D = new int[originalPhrase.length()][transcribedPhrase.length()];

                for (int i = 0; i < originalPhrase.length(); i++) {
                    D[i][0] = i;
                }

                for (int i = 0; i < transcribedPhrase.length(); i++) {
                    D[0][i] = i;
                }

                for (int i = 1; i < originalPhrase.length(); i++) {
                    for (int j = 1; j < transcribedPhrase.length(); j++) {
                        if (originalPhrase.charAt(i) == transcribedPhrase.charAt(j)) {
                            D[i][j] = Math.min(Math.min(D[i - 1][j] + 1, D[i][j - 1] + 1), (D[i - 1][j - 1]));
                        } else {
                            D[i][j] = Math.min(Math.min(D[i - 1][j] + 1, D[i][j - 1] + 1), (D[i - 1][j - 1] + 1));
                        }
                    }
                }

                int MSD = D[originalPhrase.length() - 1][transcribedPhrase.length() - 1];
                double errorRate = MSD * 100.0 / Math.max(originalPhrase.length(), transcribedPhrase.length());

                writeToDB(MSD, errorRate);
            }
            switchActivity();
        });
    }

    private void writeToDB(int MSD, double errorRate) {
        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.CEILING);

        SessionDataEntry sessionDataEntry = new SessionDataEntry(
                keyboardType, previousIndex + 1, originalPhrase, transcribedPhrase,
                timeTaken, errorRate, startTypingTimer, endTypingTimer, MSD
        );

        db_helper.addSessionDataEntry(sessionDataEntry);
    }

    private void attachListenerToEditText() {
        phraseEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isActualTrial) {
                    // first letter
                    if (s.length() == 1) {
                        startTypingTimer = System.currentTimeMillis();
                    }
                    endTypingTimer = System.currentTimeMillis();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initiatePhrase() {
        if (indexes.size() == 0) {

            // delete all old data from database
            if (isActualTrial) {
                db_helper.deleteAllTrialDataEntry();
            }

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
            Intent intent = new Intent(TrialActivity.this, AfterSubmitActivity.class);
            intent.putExtra("keyboard_type", keyboardType);
            intent.putExtra("is_actual_trial", isActualTrial);
            startActivity(intent);
        } else {
            Intent nextIntent = new Intent(TrialActivity.this, TrialActivity.class);
            nextIntent.putExtra("keyboard_type", keyboardType);
            nextIntent.putIntegerArrayListExtra("indexes", indexes);
            nextIntent.putExtra("previous_index", previousIndex);

            TrialActivity.this.startActivity(nextIntent);
        }
    }
}
