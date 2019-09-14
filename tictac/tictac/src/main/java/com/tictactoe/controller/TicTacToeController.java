package com.tictactoe.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tictactoe.model.TicTacToeConstant;
import com.tictactoe.model.TicTacToeModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.tictactoe.model.TicTacToeConstant.NOUGHT;
import static com.tictactoe.model.TicTacToeConstant.PLAYING;
import static com.tictactoe.tictac.TictacApplication.*;

@Controller
public class TicTacToeController {

    //	public static Scanner in = new Scanner(System.in); // the input Scanner
    Logger logger = LoggerFactory.getLogger(getClass());
    int boards[][] = null;
    @Autowired
    private ObjectMapper mapper;

    @GetMapping("/addScale")
    public String products(Model model) {
        model.addAttribute("model", new TicTacToeModel());
        return "addScale";
    }

    @PostMapping("/addScale")
    public ModelAndView products(@ModelAttribute("model") TicTacToeModel model, HttpServletResponse response) throws IOException {
        model.setCols(model.getRows());
        this.boards = new int[model.getRows()][model.getCols()];
        initGame(model, response);
//        printBoard(model, response);
        logger.info("scale : {}", model);
        ModelAndView m = new ModelAndView("board");
        m.addObject("model", model);
        return m;
    }

    @PostMapping("/board")
    public ModelAndView process(@ModelAttribute("model") TicTacToeModel model, HttpServletResponse response) throws IOException {
//        do {
        ModelAndView m = new ModelAndView("board");
        m.addObject("model", model);
			playerMove(model, response); // update currentRow and currentCol

            updateGame(model, response); // update currentState
            printBoard(model, response);
            // Print message if game-over
            int currentState = model.getCurrentState();
            if (currentState == TicTacToeConstant.CROSS_WON) {
                response.getWriter().print("'X' won! Bye!");
                return m;
            } else if (currentState == TicTacToeConstant.NOUGHT_WON) {
                response.getWriter().print("'O' won! Bye!");
                return m;
            } else if (currentState == TicTacToeConstant.DRAW) {
                response.getWriter().print("It's a Draw! Bye!");
                return m;
            }

            // Switch player
            model.setCurrentPlayer((model.getCurrentPlayer() == TicTacToeConstant.CROSS) ? TicTacToeConstant.NOUGHT : TicTacToeConstant.CROSS);
			model.setCurrentState(currentState);

//        updateGame(model,response);
//        printBoard(model,response);

        return m;
    }


//    @RequestMapping("/tictactoe")
//    public String tictactoe(TicTacToeModel model) {
//        /** Initialize the game-board contents and the current states */
//
//        StringBuilder build = new StringBuilder();
//        int[][] board = new int[model.getRows()][model.getCols()]; // game board in 2D array
//        initGame(model, board);
//        do {
//            playerMove(model, board);
////            updateGame(model,board); // update currentState
////            printBoard(model);
//            // Print message if game-over
//            if (model.getCurrentState() == TicTacToeConstant.CROSS_WON) {
//                System.out.println("'X' won! Bye!");
//            } else if (model.getCurrentState() == TicTacToeConstant.NOUGHT_WON) {
//                System.out.println("'O' won! Bye!");
//            } else if (model.getCurrentState() == TicTacToeConstant.DRAW) {
//                System.out.println("It's a Draw! Bye!");
//            }
//            // Switch player
//            model.setCurrentPlayer((model.getCurrentPlayer() == TicTacToeConstant.CROSS) ? TicTacToeConstant.NOUGHT : TicTacToeConstant.CROSS);
//        } while (model.getCurrentState() == TicTacToeConstant.PLAYING);
//        return "WELCOME";
//    }

    private void initGame(TicTacToeModel model, HttpServletResponse response) throws IOException {
        for (int row = 0; row < model.getRows(); ++row) {
            for (int col = 0; col < model.getCols(); ++col) {
                this.boards[row][col] = TicTacToeConstant.EMPTY;  // all cells empty
            }
        }
        printBoard(model, response);
        model.setCurrentState(TicTacToeConstant.PLAYING); // ready to play
        model.setCurrentPlayer(TicTacToeConstant.CROSS);  // cross plays first
    }

    private void playerMove(TicTacToeModel model, HttpServletResponse res) {
        do {
        int row = model.getRowTemp() - 1;  // array index starts at 0 instead of 1
        int col = model.getColTemp() - 1;
        if (row >= 0 && row < model.getRows() && col >= 0 && col < model.getCols() && boards[row][col] == TicTacToeConstant.EMPTY) {
            model.setCurrntRow(row);
            model.setCurrentCol(col);
            this.boards[model.getCurrntRow()][model.getCurrentCol()] = model.getCurrentPlayer();  // update game-board content
            model.setError(false);
        } else {
            if(row > 0 && col > 0)
                model.setError(true);
            return;
        }
        } while (true);  // repeat until input is valid
    }

    /**
     * Update the "currentState" after the player with "theSeed" has placed on
     * (currentRow, currentCol).
     */
    private void updateGame(TicTacToeModel model, HttpServletResponse response) {
        if (hasWon(model.getCurrentPlayer(), model.getCurrntRow(), model.getCurrentCol(), this.boards)) {  // check if winning move
            model.setCurrentState((model.getCurrentPlayer() == TicTacToeConstant.CROSS) ? TicTacToeConstant.CROSS_WON : TicTacToeConstant.NOUGHT_WON);
        } else if (isDraw(model, this.boards)) {  // check for draw
            model.setCurrentState(TicTacToeConstant.DRAW);
        }
        // Otherwise, no change to currentState (still PLAYING).
    }

    /**
     * Return true if it is a draw (no more empty cell)
     */
    // TODO: Shall declare draw if no player can "possibly" win
    private static boolean isDraw(TicTacToeModel model, int[][] board) {
        for (int row = 0; row < model.getRows(); ++row) {
            for (int col = 0; col < model.getCols(); ++col) {
                if (board[row][col] == TicTacToeConstant.EMPTY) {
                    return false;  // an empty cell found, not draw, exit
                }
            }
        }
        return true;  // no empty cell, it's a draw
    }

    /**
     * Return true if the player with "theSeed" has won after placing at
     * (currentRow, currentCol)
     */
    public static boolean hasWon(int theSeed, int currentRow, int currentCol, int[][] board) {
        return (board[currentRow][0] == theSeed         // 3-in-the-row
                && board[currentRow][1] == theSeed
                && board[currentRow][2] == theSeed
                || board[0][currentCol] == theSeed      // 3-in-the-column
                && board[1][currentCol] == theSeed
                && board[2][currentCol] == theSeed
                || currentRow == currentCol            // 3-in-the-diagonal
                && board[0][0] == theSeed
                && board[1][1] == theSeed
                && board[2][2] == theSeed
                || currentRow + currentCol == 2  // 3-in-the-opposite-diagonal
                && board[0][2] == theSeed
                && board[1][1] == theSeed
                && board[2][0] == theSeed);
    }

    /**
     * Print the game board
     */
    private void printBoard(TicTacToeModel model, HttpServletResponse response) throws IOException {
        for (int row = 0; row < model.getRows(); ++row) {
            for (int col = 0; col < model.getCols(); ++col) {
                printCell(boards[row][col], response); // print each of the cells
                if (col != model.getCols() - 1) {
                    response.getWriter().print("|");   // print vertical partition
                }
            }
            response.getWriter().print("<br>");
//            if (row != model.getRows() - 1) {
//                response.getWriter().print("<br>"+"-----------"); // print horizontal partition
//            }
        }
        response.getWriter().print("<br>");
    }

    /**
     * Print a cell with the specified "content"
     */
    private void printCell(int content, HttpServletResponse response) throws IOException {
        switch (content) {
            case TicTacToeConstant.EMPTY:
                response.getWriter().print(" - ");
                break;
            case NOUGHT:
                response.getWriter().print(" O ");
                break;
            case TicTacToeConstant.CROSS:
                response.getWriter().print(" X ");
                break;
        }
    }
}
