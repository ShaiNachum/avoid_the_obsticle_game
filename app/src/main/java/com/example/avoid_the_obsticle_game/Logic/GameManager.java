package com.example.avoid_the_obsticle_game.Logic;

import android.view.View;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.Random;

public class GameManager {
    private static final int POINTS = 10;
    private static final int COLLISIONFINE = 50;
    private static final int ASTEROIDCOLS = 3;
    private static final int ASTEROIDSROWS = 9;//it's bigger by one from the actual size in order
    //                                           to create an option to avoid the asteroid
    private static final int SPACESHIPSROW = 3;
    private static final int NUMOFHEARTS = 3;
    private int spaceShipIndex;
    private boolean isCollision;
    private int score = 0;
    private int life;
    private boolean[][] asteroids;
    private boolean[] spaceships;
    private boolean[] hearts;

    public GameManager(int life) {
        this.life = life;

        this.spaceShipIndex = 1;

        this.isCollision = false;

        this.spaceships = new boolean[SPACESHIPSROW];
        spaceships[1] = true;

        this.hearts = new boolean[NUMOFHEARTS];
        for(int i = 0 ; i < NUMOFHEARTS ; i++)
            hearts[i] = true;

        this.asteroids = new boolean[ASTEROIDSROWS][ASTEROIDCOLS];
        for(int i = 0 ; i < ASTEROIDSROWS ; i++)
            for(int j = 0 ; j < ASTEROIDCOLS ; j++)
                asteroids[i][j] = false;
    }

    public boolean[] getSpaceships() {
        return spaceships;
    }

    public boolean[][] getAsteroids() {
        return asteroids;
    }

    public boolean[] getHearts(){return hearts;}

    private int getRandom() {
        Random rnd = new Random();
        return rnd.nextInt(ASTEROIDCOLS);
    }

    public int getSpaceShipIndex() {
        return spaceShipIndex;
    }

    public void rightMove(){
        spaceships[spaceShipIndex] = false;
        spaceShipIndex++;
        spaceships[spaceShipIndex] = true;
    }

    public void leftMove(){
        spaceships[spaceShipIndex] = false;
        spaceShipIndex--;
        spaceships[spaceShipIndex] = true;
    }

    public void clockTick() {
        for (int i = ASTEROIDSROWS - 1 ; i > 0 ; i--) {
            for (int j = 0 ; j < ASTEROIDCOLS ; j++) {
                asteroids[i][j] = asteroids[i-1][j];
            }
        }

        int rnd = getRandom();
        for (int i = 0; i < ASTEROIDCOLS; i++) {
            if (i == rnd)
                asteroids[0][i] = true;
            else
                asteroids[0][i] = false;
        }
        checkCollision();
        checkScore();
    }

    private void checkCollision(){
        if (asteroids[ASTEROIDSROWS-1][spaceShipIndex]){
            //from here, for unlimited life:
            if(this.life == 0){
                this.life = 3;
                for (int i = 0; i < NUMOFHEARTS; i++) {
                    hearts[i] = true;
                }
            }//till here
            this.isCollision = true;
            this.life--;
            hearts[NUMOFHEARTS - this.life - 1] = false;
        }
        else
            this.isCollision = false;
    }

    private void checkScore(){
        if (!asteroids[ASTEROIDSROWS-1][spaceShipIndex]) {
            this.score += POINTS;
        }
        else{
            if(this.score >= 50)
                this.score -= COLLISIONFINE;
            else
                this.score = 0;
        }
    }

    public int getScore(){
        return score;
    }

    public int getLife() {
        return life;
    }

    public boolean getCollision(){
        return this.isCollision;
    }
}