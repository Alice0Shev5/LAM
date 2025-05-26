package com.example.views;


import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.ArrayAdapter;
import android.widget.ListView;


import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ImageView img = findViewById(R.id.animationView);
        // устанавливаем ресурс анимации
        img.setBackgroundResource(R.drawable.animation);
        // получаем объект анимации
        AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();
        // по нажатию на ImageView
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // запускаем анимацию
                frameAnimation.start();
            }
        });
    }

    // Hueta
    public void startActivity(View v) {
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }






}
