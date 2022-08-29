
public class Cards{
	
	private static final int KING = 13;
	private static final int QUEEN = 12;
	private static final int JACK = 11;
	private static final int ACE = 1;
	
	private static final int HEARTS = 1;
	private static final int SPADES = 2;
	private static final int CLUBS = 3;
	private static final int DIAMONDS = 4;
	
	public Cards(int givenRank, int givenSuit){
		cardRank = givenRank;
		cardSuit = givenSuit;
	}
	
	public int getRank(){
		return cardRank;
	}
	public int getSuit(){
		return cardSuit;
	}
	
	public String toString(){
		switch(cardRank){
		case 1:
			return ("ACE of" + cardSuit);
		default:
			return (cardRank + "of" + cardSuit);
		}
	}
	
	private int cardRank;
	private int cardSuit;
	
}