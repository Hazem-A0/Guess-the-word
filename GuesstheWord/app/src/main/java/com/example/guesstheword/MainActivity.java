package com.example.guesstheword;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView correctAnswer, rightAnswer, questionContainer;
    EditText etUserInput;
    Button btShow, btCheck, btNext;

    API apiService;
    String day;
    Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        correctAnswer = findViewById(R.id.CorrectAnswer);
        rightAnswer = findViewById(R.id.RightAnswer);
        questionContainer = findViewById(R.id.QuestionContainer);
        etUserInput = findViewById(R.id.etUserInput);
        btShow = findViewById(R.id.btShow);
        btCheck = findViewById(R.id.btCheck);
        btNext = findViewById(R.id.btNext);

        Retrofit retrofit = APIClient.getClient();
        apiService = retrofit.create(API.class);

        random = new Random();
        fetchRandomWord();

        btCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etUserInput.getText().toString().equalsIgnoreCase(day)) {
                    Dialog dialog = new Dialog(MainActivity.this);
                    dialog.setContentView(R.layout.correct_dialog);
                    Button bthide = dialog.findViewById(R.id.btConfirmDialogue);
                    dialog.show();

                    bthide.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            correctAnswer.setVisibility(View.INVISIBLE);
                            rightAnswer.setVisibility(View.INVISIBLE);
                            fetchRandomWord();
                            etUserInput.setText("");
                            dialog.dismiss();
                        }
                    });

                } else {
                    Toast.makeText(MainActivity.this, "Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etUserInput.setText("");
                fetchRandomWord();
            }
        });

        btShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                correctAnswer.setVisibility(View.VISIBLE);
                rightAnswer.setText(day);
                rightAnswer.setVisibility(View.VISIBLE);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void fetchRandomWord() {
        apiService.getRandomWord().enqueue(new Callback<String[]>() {
            @Override
            public void onResponse(Call<String[]> call, Response<String[]> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String[] words = response.body();
                    if (words.length > 0) {
                        day = words[0];
                        questionContainer.setText(mixWords(day));
                    }
                }
            }

            @Override
            public void onFailure(Call<String[]> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error fetching word", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String mixWords(String word) {
        List<String> words = Arrays.asList(word.split(""));

        Collections.shuffle(words);

        StringBuilder mixed = new StringBuilder();

        for (String w : words) {
            mixed.append(w);
        }
        return mixed.toString();
    }
}
