package com.tictactoe.model;

import java.util.Arrays;

public class TicTacToeModel {

    // The game board and the game status
    private int rows;
    private int cols; // number of rows and columns
    private int rowTemp;
    private int colTemp;
    private int[][] boards;// game board in 2D array
    private boolean error;
    //  containing (EMPTY, CROSS, NOUGHT)
    private int currentState;  // the current state of the game
    // (PLAYING, DRAW, CROSS_WON, NOUGHT_WON)
    private int currentPlayer; // the current player (CROSS or NOUGHT)

    private int currntRow, currentCol; // current seed's row and column

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public int getCurrentState() {
        return currentState;
    }

    public void setCurrentState(int currentState) {
        this.currentState = currentState;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public int getCurrntRow() {
        return currntRow;
    }

    public void setCurrntRow(int currntRow) {
        this.currntRow = currntRow;
    }

    public int getCurrentCol() {
        return currentCol;
    }

    public void setCurrentCol(int currentCol) {
        this.currentCol = currentCol;
    }

    public int[][] getBoards() {
        return boards;
    }

    public void setBoards(int[][] boards) {
        this.boards = boards;
    }

    public int getRowTemp() {
        return rowTemp;
    }

    public void setRowTemp(int rowTemp) {
        this.rowTemp = rowTemp;
    }

    public int getColTemp() {
        return colTemp;
    }

    public void setColTemp(int colTemp) {
        this.colTemp = colTemp;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "TicTacToeModel{" +
                "rows=" + rows +
                ", cols=" + cols +
                ", rowTemp=" + rowTemp +
                ", colTemp=" + colTemp +
                ", boards=" + Arrays.toString(boards) +
                ", error=" + error +
                ", currentState=" + currentState +
                ", currentPlayer=" + currentPlayer +
                ", currntRow=" + currntRow +
                ", currentCol=" + currentCol +
                '}';
    }
}
