package com.app.mit_prototype;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SpeechToText extends AppCompatActivity
{
    protected static final int RESULT_SPEECH = 1;
    private ImageButton buttonSpeak;
    private TextView textView;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        Intent intent = new Intent(getApplicationContext(), Dashboard.class);
        startActivityForResult(intent, 0);
        return  true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speechtext_screen);
        textView = findViewById(R.id.textView);
        buttonSpeak = findViewById(R.id.buttonSpeak);
        buttonSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
                try {
                    startActivityForResult(intent, RESULT_SPEECH);
                    textView.setText("");
                }
                catch (ActivityNotFoundException e)
                {
                    Toast.makeText(getApplicationContext(), "Your device does not support speech to text", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        Toolbar actionBar = findViewById(R.id.toolbar_stt);
        setSupportActionBar(actionBar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case RESULT_SPEECH:
                if (resultCode == RESULT_OK && data != null)
                {
                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    textView.setText(text.get(0));
                }
                break;
        }
    }
}