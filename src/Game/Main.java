package Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {


    // need an additional check winner function for seeing it the WHOLE game is over, and we need to modify check winner to just look at a single sub board

    //get legal moves needs to keep track of what boards we can play on

    //fix our game tree to keep track of both the board an subBoard

    static String[][][][] board = new String[3][3][3][3];
    static String[][] bigBoard = new String[3][3];
    static boolean isXsTurn = true;
    static Scanner s;
    static String xPlayer;
    static String oPlayer;
    static int[] bestMoveSoFar = new int[2];

    static int timesRecursed = 0;

    /** The main function that runs the game.*/
    public static void main(String[] args) {

        s = new Scanner(System.in);

        System.out.println("Who is X? (type \"ai\" or \"h\") for AI or Human respectively.");
        String input = s.next();
        while(!input.equals("ai") && !input.equals("h")) {
            input = s.next();
        }
        xPlayer = input;

        System.out.println("Who is O? (type \"ai\" or \"h\") for AI or Human respectively.");
        input = s.next();
        while(!input.equals("ai") && !input.equals("h")) {
            input = s.next();
        }
        oPlayer = input;

        initNewGame();
        while (true) {
            printBoard();
            if (isXsTurn && xPlayer.equals("ai")) {
                AITurn();
            } else if (!isXsTurn && oPlayer.equals("ai")) {
                AITurn();
            } else {
                playerTurn();
            }
            if (gameOver()) {
                printBoard();
                if (checkWinner("X")) {
                    System.out.println("GAME OVER! X Wins!");
                } else if (checkWinner("O")) {
                    System.out.println("GAME OVER! O Wins!");
                } else {
                    System.out.println("GAME OVER! Tie!");
                }
                break;
            }
        }
    }

    /** The recursive move finding algorithm that represents the game tree min/max search.*/
    static int findMove(int depth, int sense,  boolean saveMove) {

        timesRecursed++;

        // sense is either -1 or 1, for minimizing and maximizing respectively

        // Base Case (if we've reached out max depth OR if the game is over)
        //  - return the heuristic evaluation of board

        if (gameOver() || depth == 0) {
            return heuristic();
        }

        // (set up some variables to store the best move so far)

        int bestSoFar = 100 * (sense * -1);
        int moveToMake = -1;

        //For loop that operates on all of the legal moves "M"

        for (int M : getLegalMoves(board[0][0]) ) { //FIXME temp
            //  - make the move M on the board
            //makeMove(M);
            //  - recursively call findMove() on this board state
            int response = findMove(depth - 1, sense * -1, false);
            //  - * keep track of the best move so far (either the min valued move, or the max valued move)
            if (sense == 1) { //if we are maximizing
                if(response > bestSoFar) {
                    bestSoFar = response;
                    moveToMake = M;
                }

            } else if (sense == -1) { //if we are minimizing
                if(response < bestSoFar) {
                    bestSoFar = response;
                    moveToMake = M;
                }
            }
            //  - retract move M
            //retractMove(M);
        }
        //set the global variable to the best move we've found
        if (saveMove) {
            //bestMoveSoFar = moveToMake;
        }

        if (depth == 8) {
            System.out.println("Best possible value from this position: " + bestSoFar);
        }
        // return the value of the best move
        return bestSoFar;

        /*

        ArrayList<Integer> legalMoves = getLegalMoves();

        bestMoveSoFar = getLegalMoves().get((int) (Math.random() * legalMoves.size()));

         */

    }

    /** A simple move function that resets the global best move so far and calls findMove.*/
    static int[] getBestMove() {
        bestMoveSoFar = new int[] {0, 0};
        int sense;
        if (isXsTurn) {
            sense = 1;
        } else {
            sense = -1;
        }

        findMove(9, sense, true); //FIXME
        System.out.println(timesRecursed);
        timesRecursed = 0;
        return bestMoveSoFar;
    }

    /** Resets the board for a new game by setting all indexes in board to spaces.*/
    static void initNewGame() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                for (int k = 0; k < board[0][0].length; k++) {
                    for (int l = 0; l < board[0][0][0].length; l++) {
                        board[i][j][k][l] = " ";
                    }
                }
            }
        }

    }

    /** Prints the current state of the board.*/
    static void printBoard() {
        System.out.print("Player to move: ");
        if (isXsTurn) {
            System.out.println("X");
        } else {
            System.out.println("O");
        }
        for (int i = 0; i < board.length; i++) {
            System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
            System.out.println("  0  |  1  |  2  |*|  0  |  1  |  2  |*|  0  |  1  |  2  |");
            System.out.format("  %s  |  %s  |  %s  |*|  %s  |  %s  |  %s  |*|  %s  |  %s  |  %s  |*|\n", board[i][0][0][0], board[i][0][0][1], board[i][0][0][2], board[i][1][0][0], board[i][1][0][1], board[i][1][0][2], board[i][2][0][0], board[i][2][0][1], board[i][2][0][2]);
            System.out.println("     |     |     |*|     |     |     |*|     |     |     |");
            System.out.println("-----+-----+-----|*|-----+-----+-----|*|-----+-----+-----|");
            System.out.println("  3  |  4  |  5  |*|  3  |  4  |  5  |*|  3  |  4  |  5  |");
            System.out.format("  %s  |  %s  |  %s  |*|  %s  |  %s  |  %s  |*|  %s  |  %s  |  %s  |*|\n", board[i][0][1][0], board[i][0][1][1], board[i][0][1][2], board[i][1][1][0], board[i][1][1][1], board[i][1][1][2], board[i][2][1][0], board[i][2][1][1], board[i][2][1][2]);
            System.out.println("     |     |     |*|     |     |     |*|     |     |     |");
            System.out.println("-----+-----+-----|*|-----+-----+-----|*|-----+-----+-----|");
            System.out.println("  6  |  7  |  8  |*|  6  |  7  |  8  |*|  6  |  7  |  8  |");
            System.out.format("  %s  |  %s  |  %s  |*|  %s  |  %s  |  %s  |*|  %s  |  %s  |  %s  |*|\n", board[i][0][2][0], board[i][0][2][1], board[i][0][2][2], board[i][1][2][0], board[i][1][2][1], board[i][1][2][2], board[i][2][2][0], board[i][2][2][1], board[i][2][2][2]);
            System.out.println("     |     |     |*|     |     |     |*|     |     |     |");
            System.out.println("-----+-----+-----|*|-----+-----+-----|*|-----+-----+-----|");


//            for (int j = 0; j < board[0].length; j++) {
//                System.out.println("  0  |  1  |  2  ");
//                System.out.format("  %s  |  %s  |  %s  \n", board[i][j][0][0], board[i][j][0][1], board[i][j][0][2]);
//                System.out.println("     |     |     ");
//                System.out.println("-----+-----+-----");
//                System.out.println("  3  |  4  |  5  ");
//                System.out.format("  %s  |  %s  |  %s  \n", board[i][j][1][0], board[i][j][1][1], board[i][j][1][2]);
//                System.out.println("     |     |     ");
//                System.out.println("-----+-----+-----");
//                System.out.println("  6  |  7  |  8  ");
//                System.out.format("  %s  |  %s  |  %s  \n", board[i][j][2][0], board[i][j][2][1], board[i][j][2][2]);
//                System.out.println("     |     |     ");
//                System.out.println("* * * * * * * * * *");
//            }
        }

    }



    /** Updates a square on the board based on whose turn it is and whether the move is a retraction.*/
    static void setSquare(int subBoardMove, int move, boolean retraction) {
        int subRow = subBoardMove / 3;
        int subCol = subBoardMove % 3;

        int row = move / 3;
        int col = move % 3;

        if (retraction && board[subRow][subCol][row][col].equals(" ")) {
            System.out.println("No move to retract on this square.");
        }
        else if (!retraction && !board[subRow][subCol][row][col].equals(" ")) {
            System.out.println("Cannot make move, square is occupied.");
        } else if (retraction) {
            board[subRow][subCol][row][col] = " ";
        } else {
            board[subRow][subCol][row][col] = isXsTurn ? "X" : "O";
        }

    }

    /** Makes the passed in move if legal.*/
    static void makeMove(int subBoard, int move) {
        setSquare(subBoard, move, false);
        isXsTurn = !isXsTurn;
    }

    /** Retracts the passed in move, if the square is not already blank.*/
    static void retractMove(int subBoard, int move) {
        setSquare(subBoard, move, true);
        isXsTurn = !isXsTurn;
    }

    /** Preforms a player turn.*/
    static void playerTurn() {
        int subMove = s.nextInt();
        int subRow = subMove / 3;
        int subCol = subMove % 3;
        int move = s.nextInt();
        ArrayList<Integer> legalMoves = getLegalMoves(board[subRow][subCol]);

        while(!legalMoves.contains(move)) {
            System.out.println("That is not a legal move.");
            move = s.nextInt();
        }
        makeMove(subMove, move);
    }

    /** Preforms an AI turn.*/
    static void AITurn() {
        int[] move = getBestMove();
        makeMove(move[0], move[1]);
    }

    /** Returns an ArrayList of the legal move for the current board state.*/
    static ArrayList<Integer> getLegalMoves(String[][] curBoard) {
        ArrayList<Integer> legalMoves = new ArrayList<>();
        for (int i = 0; i < curBoard.length; i++) {
            for (int j = 0; j < curBoard[i].length; j++) {
                if (curBoard[i][j].equals(" ")) {
                    legalMoves.add(i * 3 + j);
                }
            }
        }
        return legalMoves;
    }


    /** The heuristic evaluation of the current board array represented as an integer value.*/
    static int heuristic() {
        if (checkWinner("X")) {
            return 1;
        } else if (checkWinner("O")) {
            return -1;
        } else {
            return 0;
        }
    }

    /** Returns true or false depending on whether the game is over.*/
    static boolean gameOver() {
        boolean isAMove = false;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (!getLegalMoves(board[i][j]).isEmpty()) {
                    isAMove = true;
                    break;
                }
            }
            if (isAMove) {
                break;
            }
        }
        return checkWinner("X") || checkWinner("O") || !isAMove;
    }

    /** Returns true or false depending on whether the passed in symbol has won the game.*/
    static boolean checkWinner(String sym) {
        //Check rows
        for (int i = 0; i < board.length; i++) {
            boolean won = true;
            for (int j = 0; j < board[1].length; j++) {
                if (!board[i][j].equals(sym)) {
                    won = false;
                    break;
                }
            }
            if (won) {
                return true;
            }
        }
        //Check cols
        for (int i = 0; i < board.length; i++) {
            boolean won = true;
            for (int j = 0; j < board[i].length; j++) {
                if (!board[j][i].equals(sym)) {
                    won = false;
                    break;
                }
            }
            if (won) {
                return true;
            }
        }
        //Check diags
        boolean won = true;
        for (int i = 0; i < board.length; i++) {
            if (!board[i][i].equals(sym)) {
                won = false;
                break;
            }
        }
        if (won) {
            return true;
        }
        won = true;
        for (int i = 0; i < board.length; i++) {
            if (!board[i][2 - i].equals(sym)) {
                won = false;
                break;
            }
        }
        return won;
    }
}
