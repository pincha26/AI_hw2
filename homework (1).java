import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Scanner;

/** 
 * AI Game using Min Max and Alpha Beta
 * @author Aakanksha
 * TO DO - write function to calculate points - stake and raid
 */

public class homework {

	public static int boardValues[][];
	public static int finalx;
	public static int finaly;
	public static String finalMove;
	
	public static void main(String[] args) throws Exception{
		
		String mode = null;
		String player = null;
		int sizeOfMatrix;
		int depth;
		int i = 0,j = 0;
		ArrayList<Integer[]> emptySpaces = new ArrayList<Integer[]>();
		ArrayList<Integer[]> playerX = new ArrayList<Integer[]>();
		ArrayList<Integer[]> playerO = new ArrayList<Integer[]>();
		Integer[] point;
		try{
				Scanner inputFileData = new Scanner(new File("/Users/Aakanksha/Desktop/USC/AI/Homework/HW2/TestCasesHW2/Test19/input.txt"));
				sizeOfMatrix = inputFileData.nextInt();
			    mode = inputFileData.next();
			    player = inputFileData.next();
			    depth = inputFileData.nextInt();
			    inputFileData.nextLine();
			    
			    boardValues = new int[sizeOfMatrix][sizeOfMatrix];
			    char playerPosition[][] = new char[sizeOfMatrix][sizeOfMatrix];
			    String[] rowInformation = new String[sizeOfMatrix];
			    String position;
			    
			    for(i=0;i<sizeOfMatrix;i++){
			    	rowInformation = inputFileData.nextLine().split(" ");
			    	for(j=0;j<sizeOfMatrix;j++){
			    		boardValues[i][j] = Integer.valueOf(rowInformation[j]);
			    	}
			    }
			    for(i=0;i<sizeOfMatrix;i++){
			    	position = inputFileData.next();
		    		for(j=0;j<sizeOfMatrix;j++){
		    			point = new Integer[2];
		    			point[0] = i;
		    			point[1] = j;
		    			if(position.charAt(j) == '.'){
		    				emptySpaces.add(point);
		    			}else if(position.charAt(j) == 'X'){
		    				playerX.add(point);
		    			}else if(position.charAt(j) == 'O'){
		    				playerO.add(point);
		    			}
		    			playerPosition[i][j] = position.charAt(j);
		    		}
			    }
			    
				if(mode.equals("MINIMAX")){
					MinMax(playerPosition, player.charAt(0), emptySpaces, depth, "MAX");
				}else if(mode.equals("ALPHABETA")){
					AlphaBeta(playerPosition, player.charAt(0), emptySpaces, depth, "MAX", -100000, 100000);
				}
				
				File outputFile = new File("output.txt");
				FileWriter fout = new FileWriter(outputFile);
				fout.write(Character.toString((char)(finaly+65)) + (finalx+1) + " " + finalMove);
				fout.write("\n");
				playerPosition[finalx][finaly] = player.charAt(0);
				
				if(finalMove.equals("Raid")){
						
					if((finalx>=1 && playerPosition[finalx - 1][finaly] == player.charAt(0)) ||
						(finaly>=1 && playerPosition[finalx][finaly - 1] == player.charAt(0)) ||
						(finalx<sizeOfMatrix-1 && playerPosition[finalx + 1][finaly] == player.charAt(0)) ||
						(finaly<sizeOfMatrix-1 && playerPosition[finalx][finaly + 1] == player.charAt(0))){
							
							if(finalx>=1 && playerPosition[finalx - 1][finaly] == inversePlayer(player.charAt(0))){
								playerPosition[finalx - 1][finaly] = player.charAt(0);
							}
							if(finaly>=1 && playerPosition[finalx][finaly - 1] == inversePlayer(player.charAt(0))){
								playerPosition[finalx][finaly - 1] = player.charAt(0);
							}
							if(finalx<sizeOfMatrix-1 && playerPosition[finalx + 1][finaly] == inversePlayer(player.charAt(0))){
								playerPosition[finalx + 1][finaly] = player.charAt(0);
							}
							if(finaly<sizeOfMatrix-1 && playerPosition[finalx][finaly + 1] == inversePlayer(player.charAt(0))){
								playerPosition[finalx][finaly + 1] = player.charAt(0);
							}
							
					}
				}
				
				for(i=0;i<sizeOfMatrix;i++){
					for(j=0;j<sizeOfMatrix;j++){
						fout.write(playerPosition[i][j]);
					}
					fout.write("\n");
				}
				fout.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
		}
	}
	
	public static int calculateScore(char[][] branch, int boardValues[][], char player){
		
		int length = branch.length;
		int i,j;
		int score = 0, bestScore; 
		bestScore = -100;
			score = 0;
			//instead of iterating store the positions where variable is der
			for(i= 0; i<length; i++){
				for(j=0;j<length;j++){
					if(branch[i][j] == player){
						score += boardValues[i][j];
					}else if(branch[i][j] == inversePlayer(player)){
						score -= boardValues[i][j];
					}
				}
			}
			if(bestScore<score){
				bestScore = score;
			}
		return bestScore;
	}
	
	
	public static int MinMax(char[][] playerPosition, char player, ArrayList<Integer[]> emptySpaces, int depth, String mode){
		
		int x = 0,y =0;
		Boolean flag;
		int score = 0 ;
		int bestValue = 0;
		String move, bestMove = null;
		if(depth == 0 || movePossible(playerPosition)){
			return calculateScore(playerPosition,boardValues, player);
		}
		
		if(mode.equals("MAX")){
			flag = false;
			move = "Stake";
			bestValue = -10000;
			int length = playerPosition.length;
			char[][] tempSucc;
			char currentPlayer, nextPlayer; 
	         Integer emptySpaceCoordinate []= new Integer[2]; 
	         ListIterator<Integer[]> ListOfEmptySpace = emptySpaces.listIterator();
	         
	         while(ListOfEmptySpace.hasNext()){
	        	 emptySpaceCoordinate = ListOfEmptySpace.next();
	        	 tempSucc = new char[length][length];
	        	 tempSucc = cloneArray(playerPosition);
	        	 if(player == 'X'){
	        		 currentPlayer = 'X';
	        		 nextPlayer = 'O';
	        	 }else{
	        		 currentPlayer = 'O';
	        		 nextPlayer = 'X';
	        	 }
	        	 
	        	 if(tempSucc[emptySpaceCoordinate[0]][emptySpaceCoordinate[1]] == '.'){
	        		 tempSucc[emptySpaceCoordinate[0]][emptySpaceCoordinate[1]] = currentPlayer;
	        		 score = MinMax(tempSucc, player, emptySpaces, depth - 1, inverseMode(mode));
	        		 if(bestValue<score){
	 					bestValue = score;
	 					x = emptySpaceCoordinate[0];
	 					y = emptySpaceCoordinate[1];
	 					bestMove = move;
	 				}
	        	 }
	         }
	         ListOfEmptySpace = emptySpaces.listIterator();
	         while(ListOfEmptySpace.hasNext()){
	        	 flag = false;
	        	 emptySpaceCoordinate = ListOfEmptySpace.next();
	        	 tempSucc = new char[length][length];
	        	 tempSucc = cloneArray(playerPosition);
	        	 if(player == 'X'){
	        		 currentPlayer = 'X';
	        		 nextPlayer = 'O';
	        	 }else{
	        		 currentPlayer = 'O';
	        		 nextPlayer = 'X';
	        	 }
	        	 
	        	 if(tempSucc[emptySpaceCoordinate[0]][emptySpaceCoordinate[1]] == '.'){
	        		 tempSucc[emptySpaceCoordinate[0]][emptySpaceCoordinate[1]] = currentPlayer;
	        		 
	        		 //for raid - improvise later
	        		 if((emptySpaceCoordinate[0]>0 && tempSucc[emptySpaceCoordinate[0]-1][emptySpaceCoordinate[1]] == currentPlayer) ||
	        				 (emptySpaceCoordinate[1]>0 && tempSucc[emptySpaceCoordinate[0]][emptySpaceCoordinate[1]-1] == currentPlayer) || 
	        				 (emptySpaceCoordinate[0]<(length-1) && tempSucc[emptySpaceCoordinate[0]+1][emptySpaceCoordinate[1]] == currentPlayer) ||
	        				 (emptySpaceCoordinate[1]<(length-1) && tempSucc[emptySpaceCoordinate[0]][emptySpaceCoordinate[1]+1] == currentPlayer)){
	        			 
	        			 if(emptySpaceCoordinate[0]>0 && tempSucc[emptySpaceCoordinate[0]-1][emptySpaceCoordinate[1]] == nextPlayer){
	        				 tempSucc[emptySpaceCoordinate[0]-1][emptySpaceCoordinate[1]] = currentPlayer;
	        				 move = "Raid";
	        				 flag = true;
	        			 }
	        			 if(emptySpaceCoordinate[1]>0 && tempSucc[emptySpaceCoordinate[0]][emptySpaceCoordinate[1]-1] == nextPlayer){
	        				 tempSucc[emptySpaceCoordinate[0]][emptySpaceCoordinate[1]-1] = currentPlayer;
	        				 move = "Raid";
	        				 flag = true;
	        			 }
	        			 if(emptySpaceCoordinate[0]<(length-1) && tempSucc[emptySpaceCoordinate[0]+1][emptySpaceCoordinate[1]] == nextPlayer){
	        				 tempSucc[emptySpaceCoordinate[0]+1][emptySpaceCoordinate[1]] = currentPlayer;
	        				 move = "Raid";
	        				 flag = true;
	        			 }
	        			 if(emptySpaceCoordinate[1]<(length-1) && tempSucc[emptySpaceCoordinate[0]][emptySpaceCoordinate[1]+1] == nextPlayer){
	        				 tempSucc[emptySpaceCoordinate[0]][emptySpaceCoordinate[1]+1] = currentPlayer;
	        				 move = "Raid";
	        				 flag = true;
	        			 }
	        		 }
	        		 if(flag == true){
	        			 score = MinMax(tempSucc, player, emptySpaces, depth - 1, inverseMode(mode));
	        			 if(bestValue<score){
	        				 bestValue = score;
	        				 x = emptySpaceCoordinate[0];
	        				 y = emptySpaceCoordinate[1];
	        				 bestMove = move;
	        			 }
        			 }
	        	 }
	         }
	         finalx = x;
	         finaly = y;
	         finalMove = bestMove;
	         return bestValue;
		}else{
			bestValue = 10000;
			ArrayList<char[][]> branches = generate_successor(playerPosition, inversePlayer(player), emptySpaces);
			for(char[][] branch : branches){
				score = MinMax(branch, player, emptySpaces, depth - 1, inverseMode(mode));
				if(bestValue>score){
					bestValue = score;
				}
			}
			return bestValue;
		}
	}
	
	
	private static boolean movePossible(char[][] playerPosition) {
		int i=0,j=0;
		for(i=0;i<playerPosition.length;i++){
			for(j=0;j<playerPosition.length;j++){
				if(playerPosition[i][j] == '.')
					return false;
			}
		}
		return true;
	}

	public static int AlphaBeta(char[][] playerPosition, char player, ArrayList<Integer[]> emptySpaces, int depth, String mode, int alpha, int beta){
		
		int x = 0,y =0;
		Boolean flag;
		int score = 0 ;
		int bestValue = 0;
		String move, bestMove = null;
		if(depth == 0 || movePossible(playerPosition)){
			return calculateScore(playerPosition,boardValues, player);
		}
		
		if(mode.equals("MAX")){
			flag = false;
			move = "Stake";
			bestValue = -10000;
			int length = playerPosition.length;
			char[][] tempSucc;
			char currentPlayer, nextPlayer; 
	         Integer emptySpaceCoordinate []= new Integer[2]; 
	         ListIterator<Integer[]> ListOfEmptySpace = emptySpaces.listIterator();
	         
	         while(ListOfEmptySpace.hasNext()){
	        	 emptySpaceCoordinate = ListOfEmptySpace.next();
	        	 tempSucc = new char[length][length];
	        	 tempSucc = cloneArray(playerPosition);
	        	 if(player == 'X'){
	        		 currentPlayer = 'X';
	        		 nextPlayer = 'O';
	        	 }else{
	        		 currentPlayer = 'O';
	        		 nextPlayer = 'X';
	        	 }
	        	 
	        	 if(tempSucc[emptySpaceCoordinate[0]][emptySpaceCoordinate[1]] == '.'){
	        		 tempSucc[emptySpaceCoordinate[0]][emptySpaceCoordinate[1]] = currentPlayer;
	        		 score = AlphaBeta(tempSucc, player, emptySpaces, depth - 1, inverseMode(mode), alpha, beta);
	        		 if(bestValue<score){
	 					bestValue = score;
	 					x = emptySpaceCoordinate[0];
	 					y = emptySpaceCoordinate[1];
	 					bestMove = move;
	 				}
	        		 if(bestValue>= beta)
	        			 return bestValue;
	        		 alpha = (alpha<bestValue) ? bestValue : alpha;
	        	 }
	         }
	         
	         ListOfEmptySpace = emptySpaces.listIterator();
	         
	         while(ListOfEmptySpace.hasNext()){
	        	 flag = false;
	        	 emptySpaceCoordinate = ListOfEmptySpace.next();
	        	 tempSucc = new char[length][length];
	        	 tempSucc = cloneArray(playerPosition);
	        	 if(player == 'X'){
	        		 currentPlayer = 'X';
	        		 nextPlayer = 'O';
	        	 }else{
	        		 currentPlayer = 'O';
	        		 nextPlayer = 'X';
	        	 }
	        	 
	        	 if(tempSucc[emptySpaceCoordinate[0]][emptySpaceCoordinate[1]] == '.'){
	        		 tempSucc[emptySpaceCoordinate[0]][emptySpaceCoordinate[1]] = currentPlayer;
	        		 
	        		 //for raid - improvise later
	        		 if((emptySpaceCoordinate[0]>0 && tempSucc[emptySpaceCoordinate[0]-1][emptySpaceCoordinate[1]] == currentPlayer) ||
	        				 (emptySpaceCoordinate[1]>0 && tempSucc[emptySpaceCoordinate[0]][emptySpaceCoordinate[1]-1] == currentPlayer) || 
	        				 (emptySpaceCoordinate[0]<(length-1) && tempSucc[emptySpaceCoordinate[0]+1][emptySpaceCoordinate[1]] == currentPlayer) ||
	        				 (emptySpaceCoordinate[1]<(length-1) && tempSucc[emptySpaceCoordinate[0]][emptySpaceCoordinate[1]+1] == currentPlayer)){
	        			 
	        			 if(emptySpaceCoordinate[0]>0 && tempSucc[emptySpaceCoordinate[0]-1][emptySpaceCoordinate[1]] == nextPlayer){
	        				 tempSucc[emptySpaceCoordinate[0]-1][emptySpaceCoordinate[1]] = currentPlayer;
	        				 move = "Raid";
	        				 flag = true;
	        			 }
	        			 if(emptySpaceCoordinate[1]>0 && tempSucc[emptySpaceCoordinate[0]][emptySpaceCoordinate[1]-1] == nextPlayer){
	        				 tempSucc[emptySpaceCoordinate[0]][emptySpaceCoordinate[1]-1] = currentPlayer;
	        				 move = "Raid";
	        				 flag = true;
	        			 }
	        			 if(emptySpaceCoordinate[0]<(length-1) && tempSucc[emptySpaceCoordinate[0]+1][emptySpaceCoordinate[1]] == nextPlayer){
	        				 tempSucc[emptySpaceCoordinate[0]+1][emptySpaceCoordinate[1]] = currentPlayer;
	        				 move = "Raid";
	        				 flag = true;
	        			 }
	        			 if(emptySpaceCoordinate[1]<(length-1) && tempSucc[emptySpaceCoordinate[0]][emptySpaceCoordinate[1]+1] == nextPlayer){
	        				 tempSucc[emptySpaceCoordinate[0]][emptySpaceCoordinate[1]+1] = currentPlayer;
	        				 move = "Raid";
	        				 flag = true;
	        			 }
	        			 if(flag == true){
	        				// successor.add(tempSucc);
	        				 score = AlphaBeta(tempSucc, player, emptySpaces, depth - 1, inverseMode(mode), alpha, beta);
	    	        		 if(bestValue<score){
	    	 					bestValue = score;
	    	 					x = emptySpaceCoordinate[0];
	    	 					y = emptySpaceCoordinate[1];
	    	 					bestMove = move;
	    	 				}
	    	        		 if(bestValue>= beta)
	    	        			 return bestValue;
	    	        		 alpha = (alpha<bestValue) ? bestValue : alpha;
	        			 }
	        		 }
	        		 
	        	 }
	         }
	         
	         
	         finalx = x;
	         finaly = y;
	         finalMove = bestMove;
	         return bestValue;
		}else{
			bestValue = 10000;
			ArrayList<char[][]> branches = generate_successor(playerPosition, inversePlayer(player), emptySpaces);
			for(char[][] branch : branches){
				score = AlphaBeta(branch, player, emptySpaces, depth - 1, inverseMode(mode), alpha, beta);
				if(bestValue>score){
					bestValue = score;
				}
				if(bestValue <= alpha)
       			 return bestValue;
       		 	beta = (bestValue < beta) ? bestValue : beta;
			}
			return bestValue;
		}
	}
	
	
	 public static String inverseMode(String level)  
	 { //inverse level from MIN to MAX  
	      return (level.equals("MIN")) ? "MAX" : "MIN" ;  
	 } 
	 
	 public static char inversePlayer(char level)  
	 { //inverse level from MIN to MAX  
	      return (level == 'X') ? 'O' : 'X' ;  
	 } 
	
	//copy array by value
	public static char[][] cloneArray(char[][] src) {
	    int length = src.length;
	    char[][] target = new char[length][src[0].length];
	    for (int i = 0; i < length; i++) {
	        System.arraycopy(src[i], 0, target[i], 0, src[i].length);
	    }
	    return target;
	}
	
	public static ArrayList<char[][]> generate_successor(char[][] playerPos, char player, ArrayList<Integer[]> emptySpaces)  
    {
		int length = playerPos.length;
		char[][] tempSucc;
		char currentPlayer, nextPlayer; 
		Boolean flag;
        ArrayList<char[][]> successor = new ArrayList<char[][]>();  
        Integer emptySpaceCoordinate []= new Integer[2]; 
        ListIterator<Integer[]> ListOfEmptySpace = emptySpaces.listIterator();
         
         while(ListOfEmptySpace.hasNext()){
        	 flag = false;
        	 emptySpaceCoordinate = ListOfEmptySpace.next();
        	 tempSucc = new char[length][length];
        	 tempSucc = cloneArray(playerPos);
        	 if(player == 'X'){
        		 currentPlayer = 'X';
        		 nextPlayer = 'O';
        	 }else{
        		 currentPlayer = 'O';
        		 nextPlayer = 'X';
        	 }
        	 
        	 if(tempSucc[emptySpaceCoordinate[0]][emptySpaceCoordinate[1]] == '.'){
        		 tempSucc[emptySpaceCoordinate[0]][emptySpaceCoordinate[1]] = currentPlayer;
        		 successor.add(tempSucc);
        	 }
         }
         
         //raid
         ListOfEmptySpace = emptySpaces.listIterator();
         while(ListOfEmptySpace.hasNext()){
        	 flag = false;
        	 emptySpaceCoordinate = ListOfEmptySpace.next();
        	 tempSucc = new char[length][length];
        	 tempSucc = cloneArray(playerPos);
        	 if(player == 'X'){
        		 currentPlayer = 'X';
        		 nextPlayer = 'O';
        	 }else{
        		 currentPlayer = 'O';
        		 nextPlayer = 'X';
        	 }
        	 
        	 if(tempSucc[emptySpaceCoordinate[0]][emptySpaceCoordinate[1]] == '.'){
        		 tempSucc[emptySpaceCoordinate[0]][emptySpaceCoordinate[1]] = currentPlayer;
        		 //for raid - improvise later
        		 if((emptySpaceCoordinate[0]>0 && tempSucc[emptySpaceCoordinate[0]-1][emptySpaceCoordinate[1]] == currentPlayer) ||
        				 (emptySpaceCoordinate[1]>0 && tempSucc[emptySpaceCoordinate[0]][emptySpaceCoordinate[1]-1] == currentPlayer) || 
        				 (emptySpaceCoordinate[0]<(length-1) && tempSucc[emptySpaceCoordinate[0]+1][emptySpaceCoordinate[1]] == currentPlayer) ||
        				 (emptySpaceCoordinate[1]<(length-1) && tempSucc[emptySpaceCoordinate[0]][emptySpaceCoordinate[1]+1] == currentPlayer)){
        			 
        			 if(emptySpaceCoordinate[0]>0 && tempSucc[emptySpaceCoordinate[0]-1][emptySpaceCoordinate[1]] == nextPlayer){
        				 tempSucc[emptySpaceCoordinate[0]-1][emptySpaceCoordinate[1]] = currentPlayer;
        				 flag = true;
        			 }
        			 if(emptySpaceCoordinate[1]>0 && tempSucc[emptySpaceCoordinate[0]][emptySpaceCoordinate[1]-1] == nextPlayer){
        				 tempSucc[emptySpaceCoordinate[0]][emptySpaceCoordinate[1]-1] = currentPlayer;
        				 flag = true;
        			 }
        			 if(emptySpaceCoordinate[0]<(length-1) && tempSucc[emptySpaceCoordinate[0]+1][emptySpaceCoordinate[1]] == nextPlayer){
        				 tempSucc[emptySpaceCoordinate[0]+1][emptySpaceCoordinate[1]] = currentPlayer;
        				 flag = true;
        			 }
        			 if(emptySpaceCoordinate[1]<(length-1) && tempSucc[emptySpaceCoordinate[0]][emptySpaceCoordinate[1]+1] == nextPlayer){
        				 tempSucc[emptySpaceCoordinate[0]][emptySpaceCoordinate[1]+1] = currentPlayer;
        				 flag = true;
        			 }
        			 if(flag == true){
        				 successor.add(tempSucc);
        			 }
        		 }
        	 }
         }
         return ( successor.size() == 0 ) ? null : successor ;  
    }  
	
}
