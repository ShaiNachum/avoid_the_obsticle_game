package com.example.avoid_the_obsticle_game.UI_Controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.avoid_the_obsticle_game.Logic.GameManager;
import com.example.avoid_the_obsticle_game.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {

    private static final int ASTEROIDCOLS = 3;
    private static final int SPACESHIPSROW = 3;
    private static final int ASTEROIDSROWS = 8;
    private static final int NUMOFHEARTS = 3;
    private ShapeableImageView main_IMG_background;
    private MaterialTextView main_LBL_score;
    private ShapeableImageView[] main_IMG_hearts;
    private ShapeableImageView[] main_IMG_spaceship;
    private ShapeableImageView[][] main_IMG_asteroids;
    private ShapeableImageView main_BTN_right;
    private ShapeableImageView main_BTN_left;
    private GameManager gameManager;
    private static final long DELAY = 1000;
    final Handler handler = new Handler();
    private boolean timerOn = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();

        gameManager = new GameManager(main_IMG_hearts.length);

        updateSpaceshipsUI();

        Glide
                .with(this)
                .load(R.drawable.space)// can also be url address for the image
                .centerCrop()
                .placeholder(R.drawable.space)
                .into(main_IMG_background);

        startTimer();

        main_BTN_right.setOnClickListener(View -> rightMoveClicked());
        main_BTN_left.setOnClickListener(View -> leftMoveClicked());
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, DELAY);
            gameManager.clockTick();
            updateAsteroidUI();
            collisionCheck();
            updateScore();
        }
    };

    void collisionCheck() {
        if(gameManager.getCollision()) {
            longVibrate();
            collisionToast();
            updateLives();
        }
    }

    private void updateLives() {
        for (int i = 0; i < NUMOFHEARTS ; i++) {
            if (!gameManager.getHearts()[i])
                main_IMG_hearts[i].setVisibility(View.INVISIBLE);
            else
                main_IMG_hearts[i].setVisibility(View.VISIBLE);
        }
    }
    @SuppressLint("SetTextI18n")
    void updateScore(){
        main_LBL_score.setText(gameManager.getScore() + " ");
    }

    private void startTimer() {
        if (!timerOn) {
            handler.postDelayed(runnable, 0);
        }
    }

    private void stopTimer() {
        timerOn = false;
        handler.removeCallbacks(runnable);
    }

    private void rightMoveClicked() {
        if (gameManager.getSpaceShipIndex() == 2) {
            mediumVibrate();
            moveToast();
        } else {
            gameManager.rightMove();
            updateSpaceshipsUI();
            smallVibrate();
        }
    }

    private void leftMoveClicked() {
        if (gameManager.getSpaceShipIndex() == 0) {
            mediumVibrate();
            moveToast();
        } else {
            gameManager.leftMove();
            updateSpaceshipsUI();
            smallVibrate();
        }
    }

    private void updateSpaceshipsUI(){
        for (int i = 0; i < SPACESHIPSROW; i++) {
            if(!gameManager.getSpaceships()[i])
                main_IMG_spaceship[i].setVisibility(View.INVISIBLE);
            else
                main_IMG_spaceship[i].setVisibility(View.VISIBLE);
        }
    }
    private void updateAsteroidUI(){
        boolean[][] asteroids = gameManager.getAsteroids();
        for (int i = 0; i < ASTEROIDSROWS; i++) {
            for (int j = 0; j < ASTEROIDCOLS; j++) {
                if (!asteroids[i][j])
                    main_IMG_asteroids[i][j].setVisibility(View.INVISIBLE);
                else
                    main_IMG_asteroids[i][j].setVisibility(View.VISIBLE);
            }
        }
    }

    private void smallVibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
    }

    private void mediumVibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
    }

    private void longVibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
    }

    private void moveToast() {
        Toast.makeText(this, "Cant move anymore", Toast.LENGTH_SHORT).show();
    }

    private void collisionToast() {
        Toast.makeText(this, "COLLISION!!!", Toast.LENGTH_SHORT).show();
    }


    private void findViews() {
        main_IMG_background = findViewById(R.id.main_IMG_background);
        main_IMG_hearts = new ShapeableImageView[]{
                findViewById(R.id.main_IMG_heart1),
                findViewById(R.id.main_IMG_heart2),
                findViewById(R.id.main_IMG_heart3)
        };
        main_IMG_spaceship = new ShapeableImageView[]{
                findViewById(R.id.main_IMG_spaceship1),
                findViewById(R.id.main_IMG_spaceship2),
                findViewById(R.id.main_IMG_spaceship3)
        };
        main_LBL_score = findViewById(R.id.main_LBL_score);
        main_IMG_asteroids = new ShapeableImageView[ASTEROIDSROWS][ASTEROIDCOLS];
        String basename = "main_IMG_asteroid";
        for (int i = 0; i < main_IMG_asteroids.length; i++) {
            for (int j = 0; j < main_IMG_asteroids[0].length; j++) {
                try {
                    Field idField = R.id.class.getDeclaredField(basename + i + j);
                    int viewID = idField.getInt(idField);

                    main_IMG_asteroids[i][j] = findViewById(viewID);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        main_BTN_right = findViewById(R.id.main_BTN_right);
        main_BTN_left = findViewById(R.id.main_BTN_left);
    }
}

