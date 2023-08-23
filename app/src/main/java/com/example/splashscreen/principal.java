package com.example.splashscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class principal extends Activity implements OnInitListener {

    private TextToSpeech mtts;
    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private TextView entradaVoz;
    private ImageButton btnHablar;

    /*private EditText texto;
    private SeekBar PitchBar;
    private SeekBar SpeedBar;
    private Button btnSpeak;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        mtts = new TextToSpeech(this, this);

        entradaVoz=findViewById(R.id.textoEntrada);
        btnHablar=findViewById(R.id.btnHablar);

        btnHablar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inicarEntradaVoz();
            }
        });


        /*btnSpeak=findViewById(R.id.btnSpeak);

        mtts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS){
                    int result = mtts.setLanguage(Locale.ENGLISH);
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e("tts", "lenguaje no soportado");
                    }else{
                        btnSpeak.setEnabled(true);
                    }
                }else{
                    Log.e("tts", "Fallo al iniciar");
                }
            }
        });

        texto=findViewById(R.id.edit_Text);
        PitchBar=findViewById(R.id.seekBarPitch);
        SpeedBar=findViewById(R.id.seekBarSpeed);

        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speak();
            }
        });*/

    }

    private void inicarEntradaVoz() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Empieza a hablar");
        try{
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        }catch (ActivityNotFoundException e){
            Log.e("tts", "error al iniciar");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQ_CODE_SPEECH_INPUT:{
                if (resultCode==RESULT_OK && null!=data){
                    ArrayList<String> result= data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    entradaVoz.setText(result.get(0));
                }
                break;
            }
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            // Configurar el idioma del TextToSpeech (en este caso, el idioma predeterminado)
            int result = mtts.setLanguage(Locale.getDefault());

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("tts", "lenguaje no soportado");
            } else {
                // TextToSpeech inicializado correctamente, reproducción del mensaje
                String mensaje = "¡Hola! Bienvenido a la aplicación.";
                mtts.speak(mensaje, TextToSpeech.QUEUE_FLUSH, null, null);
            }
        } else {
            Log.e("tts", "error al iniciar");
        }
    }



    /*private void speak(){
        String text= texto.getText().toString();
        float pitch= (float) PitchBar.getProgress()/50;
        if(pitch<0.1) pitch=0.1f;
        float speed= (float) SpeedBar.getProgress()/50;
        if(speed<0.1) speed=0.1f;

        mtts.setPitch(pitch);
        mtts.setSpeechRate(speed);
        
        mtts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }*/

   @Override
    protected void onDestroy() {
       super.onDestroy();

       if (mtts != null){
            mtts.stop();
            mtts.shutdown();
       }
    }
}