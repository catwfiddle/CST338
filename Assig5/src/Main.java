/*
 * Team SAGA - Shelly Sun, Andrew Bell, Greg Brown, Andrew Terrado
 * 5-30-2019
 *
 *
 *
 * The following program is built to produce a specific output per
 * assignment specifications.
 */

import javax.swing.*;
import java.awt.*;
import java.util.Random;


public class Main
{


   public static void main(String[] args)
   {

      /*
      //testing for lessthan ignore
      Card card = new Card('A', Card.Suit.spades);
      Card sFive = new Card('A', Card.Suit.hearts);
      System.out.println(card.lessThan(sFive));
      */

      // prepare the image icon array

      // establish main frame in which program will run
      JFrame frmMyWindow = new JFrame("Card Room");
      frmMyWindow.setSize(1150, 650);
      frmMyWindow.setLocationRelativeTo(null);
      frmMyWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      // set up layout which will control placement of buttons, etc.
      FlowLayout layout = new FlowLayout(FlowLayout.CENTER, 5, 20);
      frmMyWindow.setLayout(layout);

      JLabel label = new JLabel(GUICard.getIcon(card));

      frmMyWindow.add(label);

      // show everything to the user
      frmMyWindow.setVisible(true);

      /*  Main from phase 2 assignment spec

          int k;
          Icon tempIcon;

      // establish main frame in which program will run
      CardTable myCardTable
      = new CardTable("CardTable", NUM_CARDS_PER_HAND, NUM_PLAYERS);
      myCardTable.setSize(800, 600);
      myCardTable.setLocationRelativeTo(null);
      myCardTable.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      // show everything to the user
      myCardTable.setVisible(true);

      // CREATE LABELS ----------------------------------------------------
      code goes here ...

      // ADD LABELS TO PANELS -----------------------------------------
      code goes here ...

      // and two random cards in the play region (simulating a computer/hum ply)
      code goes here ...

      // show everything to the user
      myCardTable.setVisible(true);
      */
   }
}






/**
 * The following class acts a static controller for the
 * interaction with the card images, implemented as ImageIcons.
 *
 * Note that this class is designed to be used in conjunction with
 * a card class that implements 'char card.getValue' and
 * 'Card.suit card.getSuit()'
 *
 * Icon[][] iconCards - Core 2d array to hold the images for the cards
 *    as ImageIcons
 * Icon iconBack - Individual IconImage to hold the "back of card" image
 * NUM_CARD_VALUES - corresponds to the number of traditional playing card
 *    values, joker included
 * NUM_CARD_SUITES - corresponds to the number of traditional playinc card
 *    suites (i.e., Spades, Clubs, Diamonds, Hearts)
 */
class GUICard
{
   private static Icon[][] iconCards = new ImageIcon[14][4]; // 14 = A thru K + joker
   private static Icon iconBack;
   private static final int NUM_CARD_VALUES = 14;
   private static final int NUM_CARD_SUITES = 4;


   /**
    * Loads iconCards with the relevant ImageIcons, initialized to their
    * corresponding images.
    *
    * Preconditions: Images must be locally located in a folder
    * named "images" and adhere to a specific naming structure per
    * the assignment specification.
    */
   private static void loadCardIcons()
   {
      if (iconCards[0][0] ==null)
      {
         for (int cardNum = 1; cardNum <= NUM_CARD_VALUES; ++cardNum)
         {
            for (int suitNum = 0; suitNum < NUM_CARD_SUITES; ++suitNum)
            {
               String filename = turnIntIntoCardValue(cardNum) +
                                 turnIntIntoCardSuit(suitNum) + ".gif";
               iconCards[cardNum-1][suitNum] = new ImageIcon("images\\"+filename);
            }
         }
         iconBack = new ImageIcon("images\\BK.gif");
      }
   }






   /**
    * Retrieves a card's "face" image as an Icon based
    * off of the given card value. If loadCardIcons() preconditions
    * are fulfilled, cards will always be initialized before retrieval.
    *
    * @param card Card for which the "face" image is desired.
    * @return Icon holding the card's "face" image.
    */
   public static Icon getIcon(Card card)
   {
      loadCardIcons();
      return iconCards[turnCardValueIntoInt(card.getValue())-1][turnSuitIntoInt(card.getSuit())];
   }






   /**
    * Retrieves the "Back of card" image. If loadCardIcons() preconditions
    * are fulfilled, cards will always be initialized before retrieval.
    *
    * @return Icon holding the "Back of card" image
    */
   public static Icon getBackCardIcon()
   {
      loadCardIcons();
      return iconBack;
   }






   /**
    * Converts a number from 1-14 into the appropriate
    * playing card value as a one-character string.
    * If an invalid number is passed, "A" is returned.
    *
    * @param cardNum An integer to be converted into
    *                a one-character string representation
    * @return A one-character string representation that
    *                corresponds to a playing card value
    */
   private static String turnIntIntoCardValue(int cardNum)
   {
      char cardValue;

      if (cardNum >= 1 && cardNum <= 14)
      {
         switch (cardNum)
         {
         case 1:
         {
            cardValue = 'A';
            break;
         }
         case 10:
         {
            cardValue = 'T';
            break;
         }
         case 11:
         {
            cardValue = 'J';
            break;
         }
         case 12:
         {
            cardValue = 'Q';
            break;
         }
         case 13:
         {
            cardValue = 'K';
            break;
         }
         case 14:
         {
            cardValue = 'X';
            break;
         }
         default:
            cardValue = (char) ('0' + cardNum);
         }
      }
      else
         cardValue = 'A';

      return new String(new char[] {cardValue});
   }






   /**
    * Converts a playing card value from char form to
    * an integer. The value must be "valid" in order
    * for a meaningful return.
    *
    * @param cardValue A char from '2'-'9', 'A', 'T', 'J',
    *                  'Q', 'K', or 'X'. Using other values
    *                  will result in garbage being returned.
    * @return An integer representation of cardValue.
    */
   public static int turnCardValueIntoInt(char cardValue)
   {
      switch (cardValue)
      {
      case 'A':
         return 1;
      case 'T':
         return 10;
      case 'J':
         return 11;
      case 'Q':
         return 12;
      case 'K':
         return 13;
      case 'X':
         return 14;
      default:
         return cardValue - '0';
      }
   }






   /**
    * Converts an integer to a single-character string
    * representation of a playing card suite. 0 = Hearts,
    * 1 = Clubs, 2 = Diamonds, 3 (or a garbage value) = Spades.
    *
    * @param toBeConverted A value to be converted to a suite
    * @return A single-character string representation of a suite.
    */
   private static String turnIntIntoCardSuit(int toBeConverted)
   {
      switch (toBeConverted)
      {
      case 0:
         return "H";
      case 1:
         return "C";
      case 2:
         return "D";
      default:
         return "S";
      }
   }






   /**
    * Converts a Card.Suit to an integer value. Simplifies
    * calculations.  0 = Hearts,
    * 1 = Clubs, 2 = Diamonds, 3 (or a garbage value) = Spades.
    *
    * @param suit Card.Suit desired to be converted to int form.
    * @return int representation of a Card.Suit
    */
   public static int turnSuitIntoInt(Card.Suit suit)
   {
      switch (suit)
      {
      case hearts:
         return 0;
      case clubs:
         return 1;
      case diamonds:
         return 2;
      default:
         return 3;
      }
   }
}



class CardTable extends JFrame
{
   static int MAX_CARDS_PER_HAND = 56;
   static int MAX_PLAYERS = 2;  // for now, we only allow 2 person games

   private int numCardsPerHand;
   private int numPlayers;

   public JPanel pnlComputerHand, pnlHumanHand, pnlPlayArea;

   CardTable(String title, int numCardsPerHand, int numPlayers)
   {

   }

   public int getNumCardsPerHand()
   {
      return 0;
   }

   public int getNumPlayers()
   {
      return 0;
   }
}

class Card
{
   public enum Suit {spades, hearts, clubs, diamonds};
   //Characters to hold the values cards can be
   private static final char ACE = 'A', KING = 'K', QUEEN = 'Q', JACK = 'J';
   private static final char TEN = 'T';
   private static final char MIN_VALUE = '2', MAX_VALUE = '9';

   //private class values of suit, card value, and errorflag
   private Suit suit;
   private char value;
   //holds if an error has occured
   private boolean errorFlag = false;

   //Static finals for default card values
   public static final Suit DEFAULT_SUIT = Suit.spades;
   public static final char DEFAULT_VALUE = ACE;

   //card to string converter
   public String toString()
   {
      if(errorFlag)
         return "[Invalid]";
      return value + " of " + suit;

   }

   //returns if this card and another card are the same
   public boolean equals(Card card)
   {
      return card.getValue() == value && card.getSuit() == suit;
   }

   //for use in the sorting
   //return true if this is lessThan card
   public boolean lessThan(Card card)
   {
      int lhs = GUICard.turnCardValueIntoInt(this.getValue());
      lhs += GUICard.turnSuitIntoInt(this.getSuit()) * 100;
      int rhs = GUICard.turnCardValueIntoInt(card.getValue());
      rhs += GUICard.turnSuitIntoInt(card.getSuit()) * 100;
      return lhs < rhs;
   }

   static void arraySort(Card[] cards, int arraySize)
   {
      for(int i = 0; i < arraySize; i++)
      {
         Card max = cards[i];
         int index = i;
         for(int j = 0; j < arraySize; j++)
         {
            if(max.lessThan(cards[j]))
            {
               max = cards[j];
               index = j;
            }
         }
         //swap max with cards[i]
         cards[index] = cards[i];
         cards[i] = max;
      }
   }

   //default constructor
   public Card()
   {
      value = ACE;
      suit = Suit.spades;
   }

   //For use of checking of the values are valid
   private boolean isValid(char newValue, Suit newSuit)
   {
      if ((newValue >= MIN_VALUE && newValue <= MAX_VALUE)
            || newValue == ACE || newValue == KING || newValue == QUEEN
            || newValue == TEN || newValue == JACK)
      {
         return true;
      }
      return false;
   }

   //get Number for the card returns if its valid and value was set
   public boolean setValue(char newValue)
   {
      if (isValid(newValue, suit))
      {
         value = newValue;
         errorFlag = false;
         return true;
      }
      errorFlag = true;
      return false;
   }

   //takes in a new suit and if its valid sets the suit and returns true
   public boolean setSuit(Suit newSuit)
   {
      suit = newSuit;
      return true;
   }

   //takes both values and sets them
   public boolean set(char newValue, Suit newSuit)
   {
      setSuit(newSuit);
      return setValue(newValue);
   }

   //returns the value of the card as a char
   public char getValue()
   {
      return value;
   }

   //returns the suit of the card as an enum
   public Suit getSuit()
   {
      return suit;
   }

   //returns if an error has occured
   public boolean getErrorFlag()
   {
      return errorFlag;
   }

   //collection of constructors
   public Card(char newValue, Suit newSuit)
   {
      errorFlag = !setValue(newValue);
      setSuit(newSuit);
   }
   public Card(Suit newSuit)
   {
      this(ACE, newSuit);
   }
   public Card(char newValue)
   {
      this(newValue, Suit.spades);
   }
   //constructor for duplicating cards
   public Card(Card copy)
   {
      errorFlag = !setValue(copy.getValue());
      setSuit(copy.getSuit());

   }
}






/**
 * Class containing data for a deck of cards.  Contains static
 * information of all possible cards as well as current in-use
 * cards/order within the Deck object.
 *
 * public static int MAX_CARDS - Maximum number of cards allowed in play at
 *    once.
 * private Card[] cards - current "in use" cards for playing
 * private int topCard - top card of the deck, next to be "dealt".
 * private static Card[] masterPack - static representation of all possible
 *    cards
 */
class Deck
{


   private Card[] cards;
   private int topCard;
   public final int MAX_CARDS = 6*52;
   private static Card[] masterPack = new Card[52];






   /**
    * Constructor that creates the deck based on the number of "packs" given.
    * Note that only a maximum of MAX_CARDS cards can be used.
    *
    * @param numPacks - number of traditional 52 card packs to be used in
    *                 deck creation
    */
   public Deck(int numPacks)
   {

      allocateMasterPack();

      if (52*numPacks > MAX_CARDS)
         cards = new Card[MAX_CARDS];
      else
         cards = new Card[52*numPacks];

      init(numPacks);
   }






   /**
    * Empty constructor. Initializes the deck with a default of a single
    * 52 card pack.
    */
   public Deck()
   {
      this(1);
   }






   /**
    * initializes the playable cards in cards[] based on the number of packs.
    * The resulting cards[] object will contain ((int)numPacks) copies of the
    * masterPack.
    * Called by the constructors.
    *
    * @param numPacks number of 52 card packs to be used
    */
   public void init(int numPacks)
   {
      topCard = 0;
      int totalCardIndex = 0;
      for (int packNumber = 0; packNumber < numPacks; ++packNumber)
      {
         for (int masterIndex = 0; masterIndex < masterPack.length;
               ++masterIndex)
         {
            cards[totalCardIndex] = new Card(masterPack[masterIndex]);
            ++totalCardIndex;
         }
      }
   }






   /**
    * Randomizes the cards within the in-use cards[] object and resets
    * the topCard to the first position.
    */
   public void shuffle()
   {
      Random randomGenerator = new Random();
      int randomIndex = randomGenerator.nextInt(cards.length);

      for (int i = 0; i < cards.length; ++i)
      {
         Card placeholder = new Card(cards[randomIndex]);
         cards[randomIndex] = cards[i];
         cards[i] = placeholder;
         randomIndex = randomGenerator.nextInt(cards.length);
      }

      topCard = 0;
   }


   /**
    * "Deals" the top card from the deck. The next card in cards[] becomes
    * the new topCard.
    *
    * @return the topCard at the time of calling
    */
   public Card dealCard()
   {
      return new Card(cards[topCard++]);
   }


   /**
    * Gives information from the card at the given index within cards[].
    * Note that the card at the index is not returned, only a copy in order
    * to prevent the card itself from being altered in any way.
    *
    * @param k - index of cards[] to be checked
    * @return a copy of the Card object at cards[k]
    */
   public Card inspectCard(int k)
   {
      Card cardToInspect;

      if (k < cards.length && k >= 0)
      {
         cardToInspect = new Card(cards[k]);
      }
      else
         cardToInspect  = new Card('0', Card.Suit.spades);

      return cardToInspect;
   }


   /**
    * Functions similarly to inspectCard, but only inspects the current
    * topCard.
    *
    * @return a copy of the topCard; the Card object at cards[0]
    */
   public Card getTopCard()
   {
      return new Card(cards[topCard]);
   }






   /**
    * The following function converts int values to a Card.Suit enum.
    * For use with quick access of enums.
    *
    * @param toBeConverted - integer to be converted to a Card.Suit enum
    * @return one of the 4 suits as defined in Card class. 0 = hearts,
    * 1 = clubs, 2 = diamonds, all other ints will return spades
    */
   private static Card.Suit intToSuit(int toBeConverted)
   {
      switch (toBeConverted)
      {
      case 0:
         return Card.Suit.hearts;
      case 1:
         return Card.Suit.clubs;
      case 2:
         return Card.Suit.diamonds;
      default:
         return Card.Suit.spades;
      }

   }


   /**
    * Handles creation of the masterPack.  Called by init and
    * indirectly by constructors.
    * Note that this only occurs once per object lifecycle.
    * Repeated calls from the same object
    * will have no effect.
    */
   private static void allocateMasterPack()
   {
      if (masterPack[0] == null)
      {
         int masterIndex = 0;

         for(int suitInt = 0; suitInt < 4; ++suitInt)
         {
            for (int cardNum = 1; cardNum <=13; ++cardNum)
            {
               char cardValue = (char)('0' + cardNum);
               switch (cardNum)
               {
               case 1:
               {
                  cardValue = 'A';
                  break;
               }
               case 10:
               {
                  cardValue = 'T';
                  break;
               }
               case 11:
               {
                  cardValue = 'J';
                  break;
               }
               case 12:
               {
                  cardValue = 'Q';
                  break;
               }
               case 13:
               {
                  cardValue = 'K';
                  break;
               }
               }

               masterPack[masterIndex] =
                  new Card(cardValue, intToSuit(suitInt));
               ++masterIndex;
            }
         }
      }
   }






   /**
    * Tests deck init, shuffle, and dealCard
    */
   public void testDeck(int numPacks)
   {
      init(numPacks);

      for (int currentCard = 0; currentCard < cards.length; ++ currentCard)
      {
         System.out.println(dealCard());
      }

      init(numPacks);
      shuffle();


      for (int currentCard = 0; currentCard < cards.length; ++ currentCard)
      {
         System.out.println(dealCard());
      }
   }
}






/*This is the hand class. It holds data for the player's hand.*/
class Hand
{

   /*Keeps track the numbers of cards. */
   private int numCards;
   public static int MAX_CARDS = 100;
   private Card[] myCards = new Card[MAX_CARDS];

   /*Default constructor for the hand class.*/
   public Hand()
   {
      this.myCards= new Card[MAX_CARDS];
   }

   /*This function remove all cards from the hand. */
   public void resetHand()
   {

      myCards = new Card[MAX_CARDS];
      numCards = 0;

   }

   /*This is a helper function to take a newCard
    * from the table and add to myCards array if
    * there is room in the hand.*/
   public boolean takeCard(Card newCard)
   {
      if(numCards >=MAX_CARDS)
      {
         return false;
      }

      myCards[numCards] = new Card(newCard.getValue(), newCard.getSuit());
      numCards++;

      return true;
   }

   /*This function remove card from hand and
    *  return that card. */

   public Card playCard()
   {
      --numCards;
      Card cardDrawn = myCards[numCards];
      myCards[numCards] = null;
      return cardDrawn;
   }

   /*This function is used prior to displaying
    *the entire hand.*/
   public String toString()
   {

      String cards = "";
      for(int i = 0; i<numCards; i++)
      {
         cards += myCards[i].getValue() + " of " + myCards[i].getSuit() + ", ";
      }
      return "Hand = (" + cards + ") ";
   }
   /* This is the accessor method for numCards. */

   public int getnumCards()
   {

      return numCards;
   }

   /* This is accessor for an individual card and
    * it returns a card with errorFalg if K is not valid.*/

   public Card inspectCard(int k)
   {

      if (k<=MAX_CARDS && k>=0)
      {
         return myCards[k];
      }
      return new Card('T', Card.Suit.hearts);
   }


}




