/**
* This is a TicTacToe game which uses the minimax algorithm to pick the optimal
* move for the computer.
* @author Alexei Ivanov
*/

import java.lang.StringBuilder;
import java.util.Scanner;

public class TicTacToe{

   private CellValue[][] board;
   private Scanner sc = new Scanner(System.in);

   //Constructor for the game
   public TicTacToe(){
      this.board = new CellValue[3][3]; // initialize board
      fillEmpty(); //fill board with empty CellValue's
      play(); //start the game.
      sc.close();
   }

   /**
   * floods the board with empty cells
   */
   private void fillEmpty(){
      for (int i=0;i<3;i++){
         for (int j =0;j<3;j++){
            board[i][j] = CellValue.EMPTY;
         }
      }
   }

   /**
   * Makes sure that the users move is valid and then returns an arrays
   * containing the row and column of the desired move of the player.
   * @return an array of length 2. (arr[0] = row and arr[1] = col)
   */
   private int[] getMoveLocation(){
      int[] ret;
      Boolean validnum = false;
      Boolean validpos = false;
      int row=-1;
      int col=-1;
      while(!validpos){
         while(!validnum){
            try{
               System.out.println("Enter a row number: ");
               if (sc.hasNextInt()){
                  row = sc.nextInt()-1;
               }
               else{
                  sc.next();
                  continue;
               }
               if (row >= 0 && row <= 2){
                  validnum = true;
               }
            }
            catch(Exception e){
               System.out.println("Please enter a valid integer between 1 and 3.");
            }
         }
         validnum = false;
         while(!validnum){
            try{
               System.out.println("Enter a column number: ");
               if (sc.hasNextInt()){
                  col = sc.nextInt()-1;
               }
               else{
                  sc.next();
                  continue;
               }
               if (col >= 0 && col <= 2){
                  System.out.println("yes");
                  validnum = true;
               }
            }
            catch(Exception e){
               System.out.println("Please enter a valid integer between 1 and 3.");
            }
         }
         if (board[row][col] == CellValue.EMPTY){
            validpos = true;
         }
         else{
            System.out.println("This position has already been played.");
            validnum=false;
         }
      }
      return new int[] {row, col};
   }

   /**
   * Starts playing the game. Asks for user move and automatically makes the
   * computer move too. Game will end with either a win or a draw.
   */
   private void play(){
      do {
         int[] aiMove = findBestMove();
         board[aiMove[0]][aiMove[1]] = CellValue.X;
         System.out.println(this);
         if (movesLeft() && checkWin() != 10 && checkWin() != -10){
            int[] personMove = getMoveLocation();
            board[personMove[0]][personMove[1]] = CellValue.O;
         }
      }while(movesLeft() && checkWin() != 10 && checkWin() != -10);
      if (!movesLeft()){
         System.out.println("Draw!");
      }
      else if (checkWin() == 10){
         System.out.println("X Won!");
      }
      else{
         System.out.println("O Won!");
      }
      sc.close();
   }

   /**
   * Checks if the game is over. If X wins then it returns 10 to quantify the
   * win, if O wins then it returns -10, and returns 0 on a draw or not a win.
   * @return integer quantifing the win.
   */
   private int checkWin(){
      //check rows for a potential win
      for (int i=0;i<3;i++){
         if (board[i][0] == board[i][1] && board[i][1] == board[i][2]){
               if (board[i][0] == CellValue.X){
                  return 10;
               }
               else if (board[i][0] == CellValue.O){
                  return -10;
               }
         }
      }

      // check columsn for a potential win
      for (int j=0;j<3;j++){
         if (board[0][j] == board[1][j] && board[1][j] == board[2][j]){
            if (board[0][j] == CellValue.X){
               return 10;
            }
            else if (board[0][j] == CellValue.O){
               return -10;
            }
         }
      }

      //check diagonals for a potential win
      if (board[0][0] == board[1][1] && board[1][1] == board[2][2]){
         if (board[0][0] == CellValue.X){
            return 10;
         }
         else if (board[0][0] == CellValue.O){
            return -10;
         }
      }

      if (board[0][2] == board[1][1] && board[1][1] == board[2][0]){
         if (board[0][2] == CellValue.X){
            return 10;
         }
         else if (board[0][2] == CellValue.O){
            return -10;
         }
      }

      // draw or no one has won yet.
      return 0;
   }

   /**
   * Returns true if there are still moves left to be played. Returns false
   * if there are no more moves to be played. Used in code to check for draw.
   * @return boolean representing if there are or aren't moves remaining.
   */
   private boolean movesLeft(){
      for (int i=0;i<3;i++){
         for (int j=0;j<3;j++){
            if (board[i][j] == CellValue.EMPTY){
               return true;
            }
         }
      }
      return false;
   }

   /**
   * Minimax algorithm used to evaluate all possible move combinations
   * recursively. An integer is returned to represent the strength of the move.
   * A return of -10 would represent a combination where the player(O) wins.
   * A return of 10 would represent a combination where the AI(X) wins.
   * The depth of the combination is subtracted from the AI's score and added to
   * the players score so to that it will account for earlier wins or earlier
   * losses and choose the most advantageous position.
   *
   * @param depth represents the depth of the combination
   * @param isMax if true, then it is the AI's move, if false then players move
   * @return represents the strength of the move
   */

   private int minimax(int depth, Boolean isMax){
      int score = checkWin();

      //if X wins return 10
      if (score == 10){
         return score;
      }
      // if O wins return -10
      if (score == -10){
         return score;
      }
      //if draw return 0
      if (!movesLeft()){
         return 0;
      }

      // if AI find the maximizing score by calculating score from all positions
      if (isMax){
         int best = -1000;
         for (int i=0;i<3;i++){
            for (int j=0;j<3;j++){
               if (board[i][j] == CellValue.EMPTY){
                  board[i][j] = CellValue.X;
                  best = Math.max(best, minimax(depth +1, !isMax));
                  board[i][j] = CellValue.EMPTY;
               }
            }
         }
         // account for the depth
         return best - depth;
      }

      // if players turn consider that he plays optimally and find which
      // move he is likely to play
      else{
         int best = 1000;
         for (int i=0;i<3;i++){
            for (int j=0;j<3;j++){
               if (board[i][j] == CellValue.EMPTY){
                  board[i][j] = CellValue.O;
                  best = Math.min(best, minimax(depth + 1, !isMax));
                  board[i][j] = CellValue.EMPTY;
               }
            }
         }
         //account for depth
         return best + depth;
      }
   }

   /**
   * Finds the best move to play by playing in every position and calling
   * minimax to evaluate the strength of the specific move.Returns an array
   * containing the row and column for the best move.
   * @return array with best move coordinates. (arr[0] = row and arr[1] = column)
   */

   private int[] findBestMove(){
      int bestVal = -1000;
      int[] move = new int[] {-1, -1};

      for (int i=0;i<3;i++){
         for (int j =0;j<3;j++){
            if (board[i][j] == CellValue.EMPTY){
               board[i][j] = CellValue.X;
               int moveVal = minimax(0, false);
               board[i][j] = CellValue.EMPTY;
               if (moveVal > bestVal){
                  move[0] = i;
                  move[1] = j;
                  bestVal = moveVal;
               }
            }
         }
      }
      return move;
   }

   /**
   * Returns the string representation of the game state.
   * @return the current game state in a string representation.
   */

   public String toString(){
       StringBuilder sb = new StringBuilder();
       for (int i=0;i<3;i++){
          for (int j=0;j<3;j++){
             switch (this.board[i][j]){
                case X: sb.append(" X |");
                        break;
                case O: sb.append(" O |");
                        break;
                case EMPTY: sb.append("   |");
                        break;
             }
          }
          sb.delete(sb.length()-1, sb.length()+1);
          sb.append("\n");
          sb.append("-----------");
          sb.append("\n");
       }
       sb.delete(sb.length()-12,sb.length()+1);

       return sb.toString();
   }
}
