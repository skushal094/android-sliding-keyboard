package com.hci.projectkeyboard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SelectionScreen extends AppCompatActivity {

    ListView listview;
    String mlabel[] = {"Practice", "Start Trial"};
    Intent intent;

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_screen);

        listview = findViewById(R.id.list_view);
        MyAdapter adapter = new MyAdapter(this, mlabel);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener((parent, view, position, id) -> {
            if (position == 0) {
                intent = new Intent(SelectionScreen.this, TrialActivity.class);
                startActivity(intent);
                finish();
            }
            if (position == 1) {
                intent = new Intent(SelectionScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        String rlabel[];

        MyAdapter(Context c, String label[]) {
            super(c, R.layout.list_design, R.id.textView2, label);
            this.context = c;
            this.rlabel = label;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            @SuppressLint("ViewHolder") View row = layoutInflater.inflate(R.layout.list_design, parent, false);
            TextView myLabel = row.findViewById(R.id.textView2);

            myLabel.setText(rlabel[position]);
            return row;
        }
    }
}