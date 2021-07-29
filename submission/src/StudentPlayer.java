package student_player;

import boardgame.Move;

import Saboteur.SaboteurPlayer;
import Saboteur.cardClasses.SaboteurDrop;
import Saboteur.cardClasses.SaboteurMap;
import Saboteur.cardClasses.SaboteurTile;
import Saboteur.SaboteurBoardState;
import Saboteur.SaboteurMove;

import java.util.*;




/** A player file submitted by a student. */
public class StudentPlayer extends SaboteurPlayer {

    /**
     * You must modify this constructor to return your student number. This is
     * important, because this is what the code that runs the competition uses to
     * associate you with your agent. The constructor should do nothing else.
     */
    public StudentPlayer() {
        super("260730745");
    }
    
    private int turn = 0;
    
    private boolean nuggetFound = false;
    private int nuggetY;
    
    private int[] hiddenObjectives = {0, 0, 0};
    private int[] hiddenObjectivesX = {12, 12, 12};
    private int[] hiddenObjectivesY = {3, 5, 7};
    
    
    //first entry of each array represents a coordinate tuple (x, y)
    // this should ALWAYS include the nugget...We only remove when we see a HIDDEN OBJECTIVE revealed as "hidden1"/"hidden2"
    private ArrayList<Integer> adjacentToObjectiveX = new ArrayList<Integer>(Arrays.asList(12, 11, 13, 12, 11, 13, 12, 11, 13, 12));
    private ArrayList<Integer> adjacentToObjectiveY = new ArrayList<Integer>(Arrays.asList(2, 3, 3, 4, 5, 5, 6, 7, 7, 8));
    
    private ArrayList<String> deadendIDs = new ArrayList<String>(Arrays.asList("3", "3_flip", "1", "2", "2_flip", "4", "4_flip",
    		"11", "11_flip", "12", "12_flip", "13", "14", "14_flip", "15"));
    private ArrayList<String> verticalIDs = new ArrayList<String>(Arrays.asList("0", "6", "6_flip", "8"));
    private ArrayList<String> horizontalIDs = new ArrayList<String>(Arrays.asList("8", "9", "9_flip", "10"));
    private ArrayList<String> northTurnIDs = new ArrayList<String>(Arrays.asList("5_flip", "6", "6_flip", "7", "8", "9_flip"));
    private ArrayList<String> southTurnIDs = new ArrayList<String>(Arrays.asList("5", "6", "6_flip", "7_flip", "8", "9"));
    
    private SaboteurTile[][] lastBoard = null;
    private ArrayList<Saboteur.SaboteurMove> destroyedTiles = new ArrayList<>();
    
    
    class moveValueTuple  implements Comparable<moveValueTuple>{
    	public int value;
    	public Saboteur.SaboteurMove move;
    	public moveValueTuple(int value, Saboteur.SaboteurMove move) {
    		this.value = value;
    		this.move = move;
    	}
    	
    	@Override
    	public int compareTo(moveValueTuple b) {
    		if (this.value > b.value) {
    			return 1;
    		}
    		else {
    			return -1;
    		}
    	}
    }
    
//    class moveValueTupleComparator implements Comparable<moveValueTuple> {
//    	@Override
//    	public int compareTo(moveValueTuple a, moveValueTuple b) {
//    		if (a.value > b.value) {
//    			return 1;
//    		}
//    		else {
//    			return -1;
//    		}
//    	}
//    }

    /**
     * This is the primary method that you need to implement. The ``boardState``
     * object contains the current state of the game, which your agent must use to
     * make decisions.
     */
    public Move chooseMove(SaboteurBoardState boardState) {
        // You probably will make separate functions in MyTools.
        // For example, maybe you'll need to load some pre-processed best opening
        // strategies...
    	turn++;
    	
    	int player_id = boardState.getTurnPlayer();
    	int isBlocked = boardState.getNbMalus(player_id);
    	int oppIsBlocked = boardState.getNbMalus(Math.abs(player_id - 1));
    	System.out.println("oppIsBlocked: " + oppIsBlocked);
    	
        SaboteurTile[][] board = boardState.getHiddenBoard();
        ArrayList<Saboteur.cardClasses.SaboteurCard> myCards = boardState.getCurrentPlayerCards();
        
        
        //adds removed tiles to destroyedTiles
    	if (turn == 1) { lastBoard = board; }
    	else {
    		for (int i = 0; i < board.length; i++) {
    			for (int j = 0; j < board[0].length; j++) {
    				if (lastBoard[i][j] != null && board[i][j] == null) {
    					destroyedTiles.add(new SaboteurMove(lastBoard[i][j], i, j, 0));
    				}
    			}
    		}
    		lastBoard = board;
    	}

        //see if the opponent destroyed a tile
        
        
//        System.out.println("Current Objectives:");
//        System.out.println("X  Y");
//        for (int i = 0; i < adjacentToObjectiveX.size(); i++) {
//        	System.out.println(adjacentToObjectiveX.get(i) + "  " + adjacentToObjectiveY.get(i));
//        }
        
        //iterate through the HIDDEN OBJECTIVES; if a HIDDEN OBJECTIVE has been revealed (is no longer named '8'), 
        // then set that Hidden Objective index in the HiddenObjectives array to 1, signifying it's been uncovered, and remove 
        // any coordinate potentials that were exclusive to that HIDDEN OBJECTIVE
        // if Hidden Objective is instead named 'nugget', additionally set nuggetFound to true and nuggetY to the HO's y-index.
        
        for (int i = 0; i < 3; i++) {
        	if (hiddenObjectives[i] == 0) {
        		//case where HIDDEN OBJECTIVE is revealed as empty.
        		if (board[hiddenObjectivesX[i]][hiddenObjectivesY[i]].getName().contains("hidden")) {
        			//set hidden objective to revealed
                	hiddenObjectives[i] = 1;
                	
                	//remove coordinates exclusive to HIDDEN OBJECTIVE 1
                	if (i == 0) {
                		adjacentToObjectiveX.remove(0);
                		adjacentToObjectiveY.remove(0);
                		adjacentToObjectiveX.remove(1);
                		adjacentToObjectiveY.remove(1);
                		adjacentToObjectiveX.remove(2);
                		adjacentToObjectiveY.remove(2);
                	}
                	//remove coordinates exclusive to HIDDEN OBJECTIVE 2
                	else if (i == 1) {
                		adjacentToObjectiveX.remove(4);
                		adjacentToObjectiveY.remove(4);
                		adjacentToObjectiveX.remove(5);
                		adjacentToObjectiveY.remove(5);
                	}
                	//remove coordinates exclusive to HIDDEN OBJECTIVE 3
                	else if (i == 2) {
                		adjacentToObjectiveX.remove(7);
                		adjacentToObjectiveY.remove(7);
                		adjacentToObjectiveX.remove(8);
                		adjacentToObjectiveY.remove(8);
                	}
                	//IF WE GET HERE IT'S A PROBLEM
                	else {
                		throw new RuntimeException("fuck");
                	}
                }

        		//case where HIDDEN OBJECTIVE is revealed as NUGGET.
        		if (board[hiddenObjectivesX[i]][hiddenObjectivesY[i]].getName().contains("nugget")) {
        			//set HIDDEN OBJECTIVES to revealed
        			hiddenObjectives[0] = 1;
        			hiddenObjectives[1] = 1;
        			hiddenObjectives[2] = 1;
        			
        			//set nuggetFound to true
        			nuggetFound = true;
        			
        			//set correct y-coordinate of NUGGET
        			nuggetY = hiddenObjectivesY[i];
        			
        			//update adjacency lists
        			adjacentToObjectiveX = new ArrayList<Integer>(Arrays.asList(12, 11, 13, 12));
        		    adjacentToObjectiveY = new ArrayList<Integer>(Arrays.asList(nuggetY-1, nuggetY, nuggetY, nuggetY+1));
        			
        			
        		}
        		
        		//case where NUGGET is revealed through process of elimination
        		//we always check HIDDEN OBJECTIVES from left to right so this always means nugget is (12,7).
        		if (hiddenObjectives[0] + hiddenObjectives[1] == 2) {
        			if (!nuggetFound ) {
	        			//System.out.println("PROCESS OF ELIM");
	    				nuggetFound = true;
	    				nuggetY = hiddenObjectivesY[2];
	    				
	        			hiddenObjectives[2] = 1;
	    				hiddenObjectives[2] = 1;
	    				//update adjacency lists
	        			adjacentToObjectiveX = new ArrayList<Integer>(Arrays.asList(12, 11, 13, 12));
	        		    adjacentToObjectiveY = new ArrayList<Integer>(Arrays.asList(nuggetY-1, nuggetY, nuggetY, nuggetY+1));
        			}
        		}
        	}
        }
        
        int deadendMovesInHand = 0;
        int bonusMovesInHand = 0;
        int destroyMovesInHand = 0;
        int malusMovesInHand = 0;
        int mapMovesInHand = 0;
        boolean haveEight = false;
        
        //iterate through cards and take stock
        for (Saboteur.cardClasses.SaboteurCard c : boardState.getCurrentPlayerCards()) {
        	
        	//tracking card quantities to inform our Malus/Destroy usage, Discard strategy, and Map usage
        	if (c.getClass() == Saboteur.cardClasses.SaboteurTile.class && deadendIDs.contains(c.getName().split(":")[1])) {
        		deadendMovesInHand++;
        	}
       
        	if (c.getName().equals("Bonus")) {
        		bonusMovesInHand++;
        	}
        	      
        	if (c.getName().equals("Destroy")) {
        		destroyMovesInHand++;
        	}
        	
        	if (c.getName().equals("Malus")) {
        		malusMovesInHand++;
        	}
        	
        	if (c.getName().equals("Map")) {
        		mapMovesInHand++;
        	}
        	
        	if (c.getClass() == Saboteur.cardClasses.SaboteurTile.class && c.getName().split(":")[1].equals("8")) {
        		haveEight = true;
        	}
        }
        
//        System.out.println("Is Nugget Found? " + nuggetFound);
//        System.out.println("Y-Coordinate? " + nuggetY);
        
        //iterator for all potential moves
        List<Saboteur.SaboteurMove> potentialMoves = boardState.getAllLegalMoves();
        Iterator<Saboteur.SaboteurMove> iter = potentialMoves.iterator();
        
        List<Saboteur.SaboteurMove> movesReachingNugget = new ArrayList<>();
        
        List<Saboteur.SaboteurMove> movesReachingHiddenObj = new ArrayList<>();
        PriorityQueue<moveValueTuple> moveValueTuples = new PriorityQueue<>();
        
        SaboteurMove closestMove = null;
        
        while ( iter.hasNext() ) {
        	Saboteur.SaboteurMove move = iter.next();
        	Saboteur.cardClasses.SaboteurCard card = move.getCardPlayed();
        	int x = move.getPosPlayed()[0];
        	int y = move.getPosPlayed()[1];
        	//System.out.println("Move: " + card.getName() + "  X: " + move.getPosPlayed()[0] + "  Y: " + move.getPosPlayed()[1] + "  Move Value: " + evaluateMove(move));
        	
        	if (!destroyedTiles.isEmpty()) {
        		SaboteurMove destroyedMove = destroyedTiles.get(destroyedTiles.size() - 1);
        		int[] destroyedPosition = {destroyedMove.getPosPlayed()[0], destroyedMove.getPosPlayed()[1]};

        		
        		if (card.getClass() == SaboteurTile.class) {
        			SaboteurTile destroyedTile = new SaboteurTile(move.getCardPlayed().getName().split(":")[1]);
	        		if (verticalIDs.contains(destroyedTile.getIdx()) && verticalIDs.contains(card.getName().split(":")[1])) {
	        			if (boardState.verifyLegit(destroyedTile.getPath(), destroyedPosition)) {
	        				destroyedTiles.remove(destroyedTiles.size() - 1);
	        				return new SaboteurMove(card, destroyedPosition[0], destroyedPosition[1], player_id);
	        			}
	        		}
	        		if (southTurnIDs.contains(destroyedTile.getIdx()) && southTurnIDs.contains(card.getName().split(":")[1])) {
	        			if (boardState.verifyLegit(destroyedTile.getPath(), destroyedPosition)) {
	        				destroyedTiles.remove(destroyedTiles.size() - 1);
	        				return new SaboteurMove(card, destroyedPosition[0], destroyedPosition[1], player_id);
	        			}
	        		}
	        		if (northTurnIDs.contains(destroyedTile.getIdx()) && northTurnIDs.contains(card.getName().split(":")[1])) {
	        			if (boardState.verifyLegit(destroyedTile.getPath(), destroyedPosition)) {
	        				destroyedTiles.remove(destroyedTiles.size() - 1);
	        				return new SaboteurMove(card, destroyedPosition[0], destroyedPosition[1], player_id);
	        			}
	        		}
	        		if (horizontalIDs.contains(destroyedTile.getIdx()) && horizontalIDs.contains(card.getName().split(":")[1])) {
	        			if (boardState.verifyLegit(destroyedTile.getPath(), destroyedPosition)) {
	        				destroyedTiles.remove(destroyedTiles.size() - 1);
	        				return new SaboteurMove(card, destroyedPosition[0], destroyedPosition[1], player_id);
	        			}
	        		}
        		}
        	}
        	
        	//NUGGGGGETTT
        	//if nugget is found and move is adjacent to NUGGET, call doesMoveReachNugget to see if it connects a path
        	//if it does, return move
        	//** We can do this during iteration because it's the best move possible in all cases. For everything else, we need to observe all our cards first. ** 
        	
        	if (nuggetFound) {
	    		if ((x == 11 && y == nuggetY) || (x == 13 && y == nuggetY) || (x == 12 && y == nuggetY + 1) || (x == 12 && y == nuggetY - 1)) {
	    			if (card.getClass() == Saboteur.cardClasses.SaboteurTile.class && doesMoveReachNugget(boardState, move)) {
	    				movesReachingNugget.add(move); 
	    			}
	    			
	    		}
        	}
        	
        	//populate movesReachingHiddenObj
        	for (int i = 0; i < adjacentToObjectiveX.size(); i++) {
        		if (x == adjacentToObjectiveX.get(i) && y == adjacentToObjectiveY.get(i)) {
        			
        			if (card.getClass() == Saboteur.cardClasses.SaboteurTile.class && doesMoveReachHiddenObjective(boardState, move)) { 
        				//System.out.println("This move is adjacent, and it reaches hidden objective... ");
        				movesReachingHiddenObj.add(move);
        			}
        			else {
        				//System.out.println("This move is adjacent, but it doesn't reach hidden objective... ");
        			}
        			
        		}
        	}
        	
//        	//HIDDDDDDENN OBJEEEEECTIVEE
//        	//if move is adjacent to hidden objective, call doesMoveReachHiddenObjective to see if it connects a path
//        	//if it does, return move
//        	
//        	for (int i = 0; i < adjacentToObjectiveX.size(); i++) {
//        		if (x == adjacentToObjectiveX.get(i) && y == adjacentToObjectiveY.get(i)) {
//        			System.out.println("Holy Shit we're adjacent to a HO");
//        			if (doesMoveReachHiddenObjective(boardState, move)) { return move; }
//        			else { System.out.println("Couldn't connect");}
//        		}
//        	}
//        	
        	
//        	//calculate moves distance to object =, either nugget or hidden
//        	int distanceToGoal = 100;
//        	
//        	if (nuggetFound) {
//        		distanceToGoal = manhattanDistance(x, y, 12, nuggetY);
//        	}
//        	else {
//        		if (hiddenObjectives[0] == 1 && hiddenObjectives[1] == 1 && hiddenObjectives[2] == 0) {
//        			distanceToGoal = manhattanDistance(x, y, hiddenObjectivesX[2], hiddenObjectivesY[2]);
//        		}
//        		else if (hiddenObjectives[0] == 1 && hiddenObjectives[1] == 0 && hiddenObjectives[2] == 1) {
//        			distanceToGoal = manhattanDistance(x, y, hiddenObjectivesX[1], hiddenObjectivesY[1]);
//        		}
//        		else if (hiddenObjectives[0] == 0 && hiddenObjectives[1] == 1 && hiddenObjectives[2] == 1) {
//        			distanceToGoal = manhattanDistance(x, y, hiddenObjectivesX[0], hiddenObjectivesY[0]);
//        		}
//        		else {
//        			distanceToGoal = manhattanDistance(x, y, hiddenObjectivesX[1], hiddenObjectivesY[1]);
//        		}
//        	}
//        	
//        	//updates our closest move
//        	if (card.getClass() == Saboteur.cardClasses.SaboteurTile.class && !deadendIDs.contains(card.getName().split(":")[1]) && distanceToGoal <= minManhattan) {
//        		minManhattan = distanceToGoal;
//        		closestMove = move;
//        	}
        	
        	//populate moveValueTuples with tuples of the move and it's value
        	if (card.getClass() == Saboteur.cardClasses.SaboteurTile.class) {
        		int moveValue = evaluateMove(move);
        		moveValueTuples.add(new moveValueTuple(moveValue, move));
        	}
        }
        
        //if we can replace a destroyed tile, do it
        
        
        //if we can get a NUGGET, get it
        if (!movesReachingNugget.isEmpty()) {
        	return movesReachingNugget.get(0);
        }
        
        //if we have a move that reaches HIDDEN OBJECTIVE, play it
        //I think this method is art
       
        if (!movesReachingHiddenObj.isEmpty()) {
        	//if (oppIsBlocked < 1) {
	        	Saboteur.SaboteurMove scoringMoveNotCreatingAdjacency = null;
	        	for (Saboteur.SaboteurMove move : movesReachingHiddenObj) {
	        		boolean moveCreatesAdjacency = false;
	        		List<Saboteur.SaboteurMove> newMoves = generateMovesBasedOnMove(boardState, move, player_id);
	        		for (Saboteur.SaboteurMove newMove : newMoves) {
	        			int newX = newMove.getPosPlayed()[0];
	        			int newY = newMove.getPosPlayed()[1];
	        			for (int i = 0; i < adjacentToObjectiveX.size(); i++) {
	        				if (newX == adjacentToObjectiveX.get(i) && newY == adjacentToObjectiveY.get(i)) {
	        					moveCreatesAdjacency = true;
	        					break;
	        				}
	        			}
	        			if (moveCreatesAdjacency == true) {
	        				break;
	        			}
	        		}
	        		if (moveCreatesAdjacency == false) {
	        			if (evaluateMove(move) < evaluateMove(scoringMoveNotCreatingAdjacency)) {
	        				//System.out.println("New Best Move: " + move.getCardPlayed().getName() + " x-" + move.getPosPlayed()[0] + " y-" + move.getPosPlayed()[1]);
	        				scoringMoveNotCreatingAdjacency = move;
	        			}
	        		}
	        	}
	        	if (scoringMoveNotCreatingAdjacency != null) {
	        		return scoringMoveNotCreatingAdjacency;
	        	}
	        	else {
	        		return movesReachingHiddenObj.get(0);
	        	}
        	//}
        	//else {
        	//	return movesReachingHiddenObj.get(0);
        	//}
        }
        
    	//FLEXXXX ON THEMMM
    	//if the board is scorable, play MALUS or DESTROY.
    	//**if we don't know where nugget is, that's a judgement call. it might be worth to block but i kinda like risking it for the biscuit.**
        
    	if (isBoardScorable(board)) {
    		if (malusMovesInHand > 0 ) {
    			return new Saboteur.SaboteurMove(new Saboteur.cardClasses.SaboteurMalus(), 0, 0, player_id);
    		}
    		if (destroyMovesInHand > 0) {
    			for (Saboteur.SaboteurMove move : potentialMoves) {
    				int x = move.getPosPlayed()[0];
    				int y = move.getPosPlayed()[1];
    				if (move.getCardPlayed().getName().equals("Destroy")) {
    					if (x == 12 && y == 2) { return move; }
    					if (x == 11 && y == 3) { return move; }
    					if (x == 13 && y == 3) { return move; }
    					if (x == 12 && y == 4) { return move; }
    					if (x == 11 && y == 5) { return move; }
    					if (x == 13 && y == 5) { return move; }
    					if (x == 12 && y == 6) { return move; }
    					if (x == 11 && y == 7) { return move; }
    					if (x == 13 && y == 7) { return move; }
    					if (x == 12 && y == 8) { return move; }
    				}
    			}
    		}
    	}
    	
    	//play Bonus if we're blocked
    	if (isBlocked > 0 && bonusMovesInHand > 0) {
    			return new Saboteur.SaboteurMove( new Saboteur.cardClasses.SaboteurBonus(), 0, 0, player_id);
    	}
    	
    	//Now that we know that we can't score, and that they won't be able to score, we can use a Map to gain advantage.
    	//Confirm we haven't already found nugget, and that we have a map. then call getMapY() to see what HIDDEN OBJECTIVE 
    	//we need to search next
    	if (!nuggetFound && mapMovesInHand > 0) {
    		return playMap();
    	}
    	
    	
    	if (turn < 4) {
        	if (deadendMovesInHand > 0) {
        		return discardDeadend(myCards);
        	}
        	
        	if (bonusMovesInHand > 2) {
        		return discardBonus(myCards);
        	}
        	
        	if (destroyMovesInHand > 2) {
        		return discardBonus(myCards);
        	}
    	}
    	
    	if (deadendMovesInHand > 2) {
    		for (int i = 0; i < myCards.size(); i++) {
    			if (myCards.get(i).getClass() == Saboteur.cardClasses.SaboteurTile.class && deadendIDs.contains(myCards.get(i).getName().split(":")[1])) {
    				return new Saboteur.SaboteurMove(new Saboteur.cardClasses.SaboteurDrop(), i, 0, player_id);
    			}
    		}
    	}
    	
    	if (bonusMovesInHand > 2) {
    		for (int i = 0; i < myCards.size(); i++) {
    			if (myCards.get(i).getName().equals("Bonus")) {
    				return new Saboteur.SaboteurMove(new Saboteur.cardClasses.SaboteurDrop(), i, 0, player_id);
    			}
    		}
    	}
    	
    	//check the moves that our move will generate to make sure they won't score. if it'll score, discard.
    	//THIS MAKES US NOT SET THEM UP BUT IT DOESNT BLOCK THEM
    	//List<Saboteur.SaboteurMove> generatedMoves = generateMovesBasedOnMove(boardState, closestMove, player_id);

    	Saboteur.SaboteurMove bestMoveNotCreatingAdjacency = null;
    	
    	for (moveValueTuple tuple : moveValueTuples) {
    		
    		Saboteur.SaboteurMove move = tuple.move;
    		boolean moveCreatesAdjacency = false;
    		//System.out.println("Move: " + move.getCardPlayed().getName() + "  x: " + move.getPosPlayed()[0] + "  y: " + move.getPosPlayed()[1]);
    		List<Saboteur.SaboteurMove> newMoves = generateMovesBasedOnMove(boardState, move, player_id);
    		
    		for (Saboteur.SaboteurMove newMove : newMoves) {
    			int newX = newMove.getPosPlayed()[0];
    			int newY = newMove.getPosPlayed()[1];
    			
    			for (int i = 0; i < adjacentToObjectiveX.size(); i++) {
    				if (newX == adjacentToObjectiveX.get(i) && newY == adjacentToObjectiveY.get(i)) {
    					moveCreatesAdjacency = true;
    					break;
    				}
    			}
    			if (moveCreatesAdjacency == true) {
    				
    				break;
    			}
    			else {
					
    			}
    		}
    		
    		if (moveCreatesAdjacency == false) {
    			
    			if (evaluateMove(move) < evaluateMove(bestMoveNotCreatingAdjacency)) {
    				//System.out.println("New Best Move: " + move.getCardPlayed().getName() + " x-" + move.getPosPlayed()[0] + " y-" + move.getPosPlayed()[1]);
    				bestMoveNotCreatingAdjacency = move;
    			}
    		}
    	}
    	
    	if (bestMoveNotCreatingAdjacency != null) {
    		return bestMoveNotCreatingAdjacency;
    	}
    	else if (!moveValueTuples.isEmpty()){
    		return moveValueTuples.peek().move;
    	}
    	else {
    		return discard(myCards, deadendMovesInHand, bonusMovesInHand, destroyMovesInHand);
    	}
    	
    	

    	
////        
//        // Is random the best you can do?
//        Move myMove = boardState.getRandomMove();
//
//        // Return your move to be processed by the server.
//        return myMove;
    }
    
    
    public Saboteur.SaboteurMove playMap() {
    	return new Saboteur.SaboteurMove(new Saboteur.cardClasses.SaboteurMap(), 12, getMapY(), player_id);
    }
    
    //evaluates value of move based on its proximity to the nugget, or a hidden objective
    //** moveValue represents general distance to objective, so a lower value means a better move **
    public int evaluateMove(Saboteur.SaboteurMove move) {
    	if (move == null || move.getCardPlayed().getClass() != Saboteur.cardClasses.SaboteurTile.class) { return 100; }
    	String tileType = move.getCardPlayed().getName().split(":")[1];
    	int moveX = move.getPosPlayed()[0];
    	int moveY = move.getPosPlayed()[1];
    	
    	int minDistanceToHiddenObj = 100;
    	
    	//if NUGGET is known, goalY is set to nuggetY. Else, loop through objectives and find minimum distance to one that's not revealed yet.
    	if (nuggetFound) { 
    		minDistanceToHiddenObj = manhattanDistance(moveX, moveY, 12, nuggetY);
    	}
    	else {
    		for (int i = 0; i < hiddenObjectives.length; i++) {
    			int distanceToHiddenObjective;
    			//if HIDDEN OBJECTIVE hasn't been revealed yet, calculate distance from it and the move. 
    			if (hiddenObjectives[i] == 0) {
    				distanceToHiddenObjective = manhattanDistance(moveX, moveY, hiddenObjectivesX[i], hiddenObjectivesY[i]);
    				if (distanceToHiddenObjective < minDistanceToHiddenObj) { minDistanceToHiddenObj = distanceToHiddenObjective; }
    			}
    		}
    	}
    	//now we have minDistanceToHiddenObjective for the given move. we just need to add a term that prefers tiles that move down board.
   
    	int tileValue;
    	
    	if (verticalIDs.contains(tileType) || southTurnIDs.contains(tileType)) {
    		tileValue = 0;
    	}
    	else if (horizontalIDs.contains(tileType)) {
    		tileValue = 1;
    	}
    	else if (northTurnIDs.contains(tileType)) {
    		tileValue = 2;
    	}
    	else {
    		tileValue = 10;
    	}

    	int moveValue = tileValue + minDistanceToHiddenObj;
    	
    	return moveValue;
    }
    
    //returns y of hidden objective
    public int getMapY() {
    	
    	if (hiddenObjectives[0] == 0) {
    		return hiddenObjectivesY[0];
    	}
    	else if (hiddenObjectives[1] == 0) {
    		return hiddenObjectivesY[1];
    	}
    	else {
    		nuggetFound = true;
    		//should never reach here because this would mean the first two hidden objectives are revealed so nuggetFound should be true;s
    		return hiddenObjectivesY[2];
    	}
    }
    
    private SaboteurMove discard(ArrayList<Saboteur.cardClasses.SaboteurCard> hand, int numDeadends, int numBonus, int numDestroy) {
    	if (numDeadends > 0) {
    		return discardDeadend(hand);
    	}
    	if (numBonus > 2) {
    		return discardBonus(hand);
    	}
    	if (numDestroy > 2) {
    		return discardDestroy(hand);
    	}
    	return new SaboteurMove(new Saboteur.cardClasses.SaboteurDrop(), 0, 0, 0);
    }
    
    private SaboteurMove discardDeadend(ArrayList<Saboteur.cardClasses.SaboteurCard> hand) {
    	int index = 0;
    	for (int i = 0; i < hand.size(); i++) {
    		if (hand.get(i).getClass() == Saboteur.cardClasses.SaboteurTile.class && deadendIDs.contains(hand.get(i).getName().split(":")[1])) {
    			index = i;
    		}
    	}
    	return new SaboteurMove(new Saboteur.cardClasses.SaboteurDrop(), index, 0, 0);
    }
    
    private SaboteurMove discardBonus(ArrayList<Saboteur.cardClasses.SaboteurCard> hand) {
    	int index = 0;
    	for (int i = 0; i < hand.size(); i++) {
    		if (hand.get(i).getName().equals("Bonus")) {
    			index = i;
    		}
    	}
    	return new SaboteurMove(new Saboteur.cardClasses.SaboteurDrop(), index, 0, 0);
    }
    
    private SaboteurMove discardDestroy(ArrayList<Saboteur.cardClasses.SaboteurCard> hand) {
    	int index = 0;
    	for (int i = 0; i < hand.size(); i++) {
    		if (hand.get(i).getName().equals("Destroy")) {
    			index = i;
    		}
    	}
    	return new SaboteurMove(new Saboteur.cardClasses.SaboteurDrop(), index, 0, 0);
    }
    
    private boolean isBoardScorable(Saboteur.cardClasses.SaboteurTile[][] tiles) {
    	if ( tiles[12][2] != null || tiles[11][3] != null || tiles[13][3] != null || tiles[12][4] != null || tiles[11][5] != null || 
    			tiles[13][5] != null || tiles[12][6] != null || tiles[11][7] != null || tiles[13][7] != null || tiles[12][8] != null) {
    		return true;
    	}
    	
    	return false;
    }
    
    private boolean isNuggetFound(Saboteur.cardClasses.SaboteurTile[][] board) {
        
        //check if nugget was found
        if (board[12][3].getName().equals("nugget") ) {
        	nuggetY = 3;
        	nuggetFound = true;
        	return true;
        }
        else if (board[12][3].getName().equals("nugget")) {
        	nuggetY = 5;
        	nuggetFound = true;
        	return true;
        }
        else if (board[12][7].getName().equals("nugget")) {
        	nuggetY = 7;
        	nuggetFound = true;
        	return true;
        }
        return false;
    }
    
    private int manhattanDistance(int x1, int y1, int x2, int y2) {
    	return (Math.abs(x1 - x2) + Math.abs(y1 - y2));
    }
    
    private boolean doesMoveReachHiddenObjective(Saboteur.SaboteurBoardState boardState, Saboteur.SaboteurMove move) {
    	int x = move.getPosPlayed()[0];
    	int y = move.getPosPlayed()[1];
    	String tileType = move.getCardPlayed().getName().split(":")[1];
    	
    	
    	//cases where move is Above a HIDDEN OBJECTIVE
    	if (x == 11 && (y == 3 || y == 5 || y == 7)) {
    		if (tileType.equals("0") || tileType.equals("5") || tileType.equals("9") || tileType.equals("7_flip") || tileType.equals("6") || 
    				tileType.equals("8") || tileType.equals("6_flip")) {
    			return true;
    		}
    		return false;
    	}
    	
    	//cases where move is Below HIDDEN OBJECTIVE
    	if (x == 13 && (y == 3 || y == 5 || y == 7)) {
    		if (tileType.equals("0") || tileType.equals("5_flip") || tileType.equals("9_flip") || tileType.equals("7") || tileType.equals("6") || 
    				tileType.equals("8") || tileType.equals("6_flip")) {
    			return true;
    		}
    		return false;
    	}

    	//cases where move is Between HIDDEN OBJECTIVES
    	if (x == 12 && (y == 4 || y == 6)) {
    		if (tileType.equals("5") || tileType.equals("5_flip") || tileType.equals("7") || tileType.equals("7_flip") || tileType.equals("9") || tileType.equals("9_flip") || tileType.equals("8") ||
    				tileType.equals("6") || tileType.equals("6_flip") || tileType.equals("10")) {
    			return true;
    		}
    		return false;
    	}
    	
    	//case where move is Left of Left-most HIDDEN OBJECTIVE
    	if (x == 12 && y == 2) {
    		if (tileType.equals("5") || tileType.equals("7") || tileType.equals("9") || tileType.equals("9_flip") || tileType.equals("8") ||
    				tileType.equals("6_flip") || tileType.equals("10")) {
    			return true;
    		}
    		return false;
    	}
    	
    	//case where move is Right of Right-most HIDDEN OBJECTIVE
    	if (x == 12 && y == 8) {
    		if (tileType.equals("5_flip") || tileType.equals("7_flip") || tileType.equals("9") || tileType.equals("9_flip") || tileType.equals("8") ||
    				tileType.equals("6") || tileType.equals("10")) {
    			return true;
    		}
    		return false;
    	}
    	
    	return false;
    }
    
    private boolean doesMoveReachNugget(Saboteur.SaboteurBoardState boardState, Saboteur.SaboteurMove move) {
    	int x = move.getPosPlayed()[0];
    	int y = move.getPosPlayed()[1];
    	String tileType = move.getCardPlayed().getName().split(":")[1];
    	
    	//cases where move is Above NUGGET
    	if (x == 11 && y == nuggetY) {
    		if (tileType.equals("0") || tileType.equals("5") || tileType.equals("9") || tileType.equals("7_flip") || tileType.equals("6") || 
    				tileType.equals("8") || tileType.equals("6_flip")) {
    			
    			return true;
    		}
    		return false;
    	}
    	
    	//cases where move is Below NUGGET
    	if (x == 13 && y == nuggetY) {
    		if (tileType.equals("0") || tileType.equals("5_flip") || tileType.equals("9_flip") || tileType.equals("7") || tileType.equals("6") || 
    				tileType.equals("8") || tileType.equals("6_flip")) {
    			return true;
    		}
    		return false;
    	}
    	
    	//case where move is Left of NUGGET
    	if (y == nuggetY - 1) {
    		if (tileType.equals("5") || tileType.equals("7") || tileType.equals("9") || tileType.equals("9_flip") || tileType.equals("8") ||
    				tileType.equals("6_flip") || tileType.equals("10")) {
    			return true;
    		}
    		return false;
    	}
    	
    	//case where move is Right of NUGGET
    	if (y == nuggetY + 1) {
    		if (tileType.equals("5_flip") || tileType.equals("7_flip") || tileType.equals("9") || tileType.equals("9_flip") || tileType.equals("8") ||
    				tileType.equals("6") || tileType.equals("10")) {
    			return true;
    		}
    		return false;
    	}
    	
    	return false;
    }
    
    //Input: BoardState, Move, player_id
    //Return: ArrayList of SaboteurMoves that become available based on the given move that could connect to HIDDEN OBJECTIVES (ie. they could connect to a new tile).
    public List<SaboteurMove> generateMovesBasedOnMove(SaboteurBoardState boardState, SaboteurMove move, int player_id) {
    	List<SaboteurMove> newMoves = new ArrayList<SaboteurMove>();
    	SaboteurTile[][] board = boardState.getHiddenBoard();
    	
    	
    	int x = move.getPosPlayed()[0];
    	int y = move.getPosPlayed()[1];
    	Saboteur.cardClasses.SaboteurCard card = move.getCardPlayed();
    	
    	//add potential moves Above our move that could connect to other tiles. If it isn't null, a tile already exists there so there's no possible moves. 
    	if (x > 0 && board[x-1][y] == null) {
    		newMoves.add(new SaboteurMove(new Saboteur.cardClasses.SaboteurTile("0"), x-1, y, player_id));
    		newMoves.add(new SaboteurMove(new Saboteur.cardClasses.SaboteurTile("5"), x-1, y, player_id));
    		newMoves.add(new SaboteurMove(new Saboteur.cardClasses.SaboteurTile("9"), x-1, y, player_id));
    		newMoves.add(new SaboteurMove(new Saboteur.cardClasses.SaboteurTile("7_flip"), x-1, y, player_id));
    		newMoves.add(new SaboteurMove(new Saboteur.cardClasses.SaboteurTile("6"), x-1, y, player_id));
    		newMoves.add(new SaboteurMove(new Saboteur.cardClasses.SaboteurTile("6_flip"), x-1, y, player_id));
    		newMoves.add(new SaboteurMove(new Saboteur.cardClasses.SaboteurTile("8"), x-1, y, player_id));
    	}
    	
    	//add potential moves Below our move. If it isn't null, a tile already exists there so there's no possible moves. 
    	if (x < 13 && board[x+1][y] == null) {
    		newMoves.add(new SaboteurMove(new Saboteur.cardClasses.SaboteurTile("0"), x+1, y, player_id));
    		newMoves.add(new SaboteurMove(new Saboteur.cardClasses.SaboteurTile("5_flip"), x+1, y, player_id));
    		newMoves.add(new SaboteurMove(new Saboteur.cardClasses.SaboteurTile("9_flip"), x+1, y, player_id));
    		newMoves.add(new SaboteurMove(new Saboteur.cardClasses.SaboteurTile("7"), x+1, y, player_id));
    		newMoves.add(new SaboteurMove(new Saboteur.cardClasses.SaboteurTile("6"), x+1, y, player_id));
    		newMoves.add(new SaboteurMove(new Saboteur.cardClasses.SaboteurTile("6_flip"), x+1, y, player_id));
    		newMoves.add(new SaboteurMove(new Saboteur.cardClasses.SaboteurTile("8"), x+1, y, player_id));
    		
    	}
    	
    	//add potential moves Left of our move. If it isn't null, a tile already exists there so there's no possible moves. 
    	if (y > 0 && board[x][y-1] == null) {
    		newMoves.add(new SaboteurMove(new Saboteur.cardClasses.SaboteurTile("5"), x, y-1, player_id));
    		newMoves.add(new SaboteurMove(new Saboteur.cardClasses.SaboteurTile("7"), x, y-1, player_id));
    		newMoves.add(new SaboteurMove(new Saboteur.cardClasses.SaboteurTile("9"), x, y-1, player_id));
    		newMoves.add(new SaboteurMove(new Saboteur.cardClasses.SaboteurTile("9_flip"), x, y-1, player_id));
    		newMoves.add(new SaboteurMove(new Saboteur.cardClasses.SaboteurTile("8"), x, y-1, player_id));
    		newMoves.add(new SaboteurMove(new Saboteur.cardClasses.SaboteurTile("6"), x, y-1, player_id));
    		newMoves.add(new SaboteurMove(new Saboteur.cardClasses.SaboteurTile("10"), x, y-1, player_id));
    		
    	}

    	//add potential moves Right of our move. If it isn't null, a tile already exists there so there's no possible moves. 
    	if (y < 13 && board[x][y+1] == null) {
    		newMoves.add(new SaboteurMove(new Saboteur.cardClasses.SaboteurTile("5_flip"), x, y+1, player_id));
    		newMoves.add(new SaboteurMove(new Saboteur.cardClasses.SaboteurTile("7_flip"), x, y+1, player_id));
    		newMoves.add(new SaboteurMove(new Saboteur.cardClasses.SaboteurTile("9"), x, y+1, player_id));
    		newMoves.add(new SaboteurMove(new Saboteur.cardClasses.SaboteurTile("9_flip"), x, y+1, player_id));
    		newMoves.add(new SaboteurMove(new Saboteur.cardClasses.SaboteurTile("8_flip"), x, y+1, player_id));
    		newMoves.add(new SaboteurMove(new Saboteur.cardClasses.SaboteurTile("6"), x, y+1, player_id));
    		newMoves.add(new SaboteurMove(new Saboteur.cardClasses.SaboteurTile("10"), x, y+1, player_id));
    	}
    	
    	return newMoves;
    }
}