/*
 * Team SAGA - Shelly Sun, Andrew Bell, Greg Brown, Andrew Terrado
 * 5-30-2019
 *
 *
 *
 * The following program is built to produce a specific output per
 * assignment specifications.
 *
 * Note**  The last card is played automatically.
 */



import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.util.*;
import javax.swing.border.LineBorder;
import javax.swing.border.Border;


public class MVC
{



   public static void main(String[] args)
   {


      View theView = new View();
      Model theModel = new Model(theView);

      Controller mainController = new Controller(theModel, theView);


   }



   static void endGame()
   {
      //logic for ending of game
   }



   //This method generates a Random card;
   public static Card generateRandomCard()
   {
      Card card = new Card();
      Card.Suit suitValue;
      char cardValue;

      //This would select a random card value for char cardValue.
      String randChar = "23456789TJQKAX";
      Random rand = new Random();
      cardValue = randChar.charAt(rand.nextInt(14));

      //This selects a random card from the Card.Suit enumeration.
      Random randNumber = new Random();
      int randNum = randNumber.nextInt(4)+1;

      switch(randNum)
      {
         case 1:
            card.set(cardValue, Card.Suit.spades);
            break;
         case 2:
            card.set(cardValue, Card.Suit.hearts);
            break;
         case 3:
            card.set(cardValue, Card.Suit.clubs);
            break;
         case 4:
            card.set(cardValue, Card.Suit.diamonds);
            break;

      }

      return card;
   }
}


/**
 * Model for the card game. Holds the data and computations for the game.
 * human - human player
 * computer - computer opponent
 * lastPlayedLeftCard - the last played card to the left stack
 * lastPlayedRightCard - the last played card to the right stack
 * attachedView - the corresponding MVC View object
 * framework - the CardGameFramework for deck/card interactions
 * Direction - enumeration to better control directional indications (left, right)
 */
class Model
{
   private Player human;
   private Player computer;
   private static Card lastPlayedLeftCard;
   private static Card lastPlayedRightCard;
   private View attachedView;
   private CardGameFramework framework = new CardGameFramework(1, 4, 0,
      null, 2, 7 );

   public enum Direction
   {
      LEFT
         {
            void setCardAt(Card card)
            {
               lastPlayedLeftCard = card;
            }
         },
      RIGHT
         {
            void setCardAt(Card card)
            {
               lastPlayedRightCard = card;
            }
         };
      abstract void setCardAt(Card card);
   }






   /**
    * This model holds the data and computation work for the card game.
    * The model updates the view at relevant points.
    *
    */
   Model(View theView)
   {
      human = new HumanPlayer(framework.getHand(0));
      computer = new ComputerPlayer(framework.getHand(1));
      attachedView = theView;
      framework.deal();
      updateScore();
      updatePlayedCardArea();
      human.updateCardArea();
      computer.updateCardArea();

   }


   Model()
   {
      this(new View());
   }




   /**
    * Updates the score attributes of the two players in the game
    */
   private void calculateScore()
   {
      {
         if (human.skippedTurn)
            human.score -= 1;
         if (computer.skippedTurn)
            computer.score -= 1;
      }
   }






   /**
    * Plays the card indicated from the human player's hand onto the
    * stack chosen.
    *
    * Functions by calling overloaded playCard(Player, int, Direction)
    *
    * @param cardIndex  Index within the player's hand of the card to be played
    * @param locationToPlay Which "stack" to play the chosen card to
    */
   void playCard(int cardIndex, Direction locationToPlay)
   {
      playCard(human, cardIndex, locationToPlay);
   }






   /**
    * Plays the card indicated from the chosen Player objet's hand to the
    * stack chosen.
    * @param playerOrComputer
    * @param cardIndex
    * @param locationToPlay
    */
   private void playCard(Player playerOrComputer, int cardIndex, Direction locationToPlay)
   {

      boolean gameGoodToGo = true;

      if (playerOrComputer != null && cardIndex != -1 && locationToPlay != null)
      {
         locationToPlay.setCardAt((playerOrComputer.playerHand.playCard(cardIndex)));

         gameGoodToGo = framework.takeCard(playerOrComputer.toInt());
         playerOrComputer.updateCardArea();
         updatePlayedCardArea();
         playerOrComputer.usedTurn = true;
         turnPass();
      }
      if (!gameGoodToGo)
         MVC.endGame();
   }






   /**
    * Requests the View stop its clock
    */
   public void stopClock()
   {
      attachedView.mainClock.stopClock();
   }






   /**
    * Requests the view restarts its clock
    */
   public void startClock()
   {
      attachedView.mainClock.run();
   }






   /**
    * Passes the human player's turn.
    * Calls doNothing()
    */
   public void skipTurn()
   {
      doNothing(human);
   }






   /**
    * Passes the chosen player's turn.
    * Calls turnPass()
    * @param humanOrComputer
    */
   private void doNothing(Player humanOrComputer)
   {
      humanOrComputer.usedTurn = true;
      turnPass();
   }






   /**
    * Passes one "half turn" and gives control to the next player
    * depending on whose turn has been completed. If both players
    * have completed a turn, scores are calculated and updated.
    * human always goes first.
    *
    * Calls computerTurn(), calculateScore(), updateScore()
    */
   private void turnPass()
   {
      if (!human.usedTurn)
      {
         // wait for player to do something
      }
      else if (!computer.usedTurn)
         computerTurn();
      else
      {
         calculateScore();
         updateScore();
         human.usedTurn = false;
         human.skippedTurn = false;
         computer.usedTurn = false;
         computer.skippedTurn = false;
      }
   }






   /**
    * Requests the view update the card area for the given Player object.
    *
    * Calls Player.updateCardArea()
    *
    * @param humanOrComputer Player object whose visual card area should
    *                        be updated
    */
   private void updateCardArea(Player humanOrComputer)
   {
      humanOrComputer.updateCardArea();
   }






   /**
    * Requests the view update the play area
    *
    * Calls View.updatePlayedCardImagesArray()
    */
   private void updatePlayedCardArea()
   {
      attachedView.updatePlayedCardImagesArray(new Card[]{lastPlayedLeftCard, lastPlayedRightCard});
   }






   /**
    * Requests the view update the score display
    *
    * Calls View.updateScores()
    */
   private void updateScore()
   {
      attachedView.updateScores(new String[]{(Integer.toString(human.score)), Integer.toString(computer.score)});
   }






   /**
    * Performs the computer Player's turn actions
    */
   private void computerTurn()
   {
      //find playable cards
      //   Vector<Card> playableCards = computer.getPlayableCards();
      //select which one is least like the other cards in hand
      //     if(playableCards.size())
      {
         //pick a card
      }
      //     else
      {
         //can not play
      }

   }






   /**
    * Abstract class for the two main players of the game
    */
   abstract class Player
   {
      Hand playerHand;
      int score;
      boolean usedTurn = false;
      boolean skippedTurn = false;
      Card stackPosition;


      Player()
      {

      }

      //using the left and right table cards
      //returns an array of playable cards in the player's hand
      public Vector<Card> getPlayableCards(Card left, Card right)
      {
         return playerHand.getPlayableCards(left, right);
      }

      public abstract void updateCardArea();
      public abstract int toInt();
   }






   /**
    * Player class for the human player of the game
    */
   class HumanPlayer extends Player
   {
      /**
       * Constructor for the human player.  stackPosition is
       * set to the left stack in the event the highCardGame is
       * reimplemented.
       * @param hand The hand that should be assigned to the human player.
       */
      HumanPlayer(Hand hand)
      {
         stackPosition = Model.lastPlayedLeftCard;
         playerHand = hand;
      }






      /**
       * returns an int representation of the player as it relates to the
       * CardGameFramework array.
       * @return 0, the human player's position in the cardGameFramework array.
       */
      public int toInt()
      {
         return 0;
      }






      /**
       * Requests the view update the human player's card area.
       *
       * Calls View.updatePlayerCardImagesArray()
       */
      public void updateCardArea()
      {
         attachedView.updatePlayerCardImagesArray(playerHand);

      }

   }






   /**
    * Player class for the computer opponent of the game
    */
   class ComputerPlayer extends Player
   {
      /**
       * Constructor for the human player.  stackPosition is
       * set to the left stack in the event the highCardGame is
       * reimplemented.
       * @param hand The hand that should be assigned to the computer player.
       */
      ComputerPlayer(Hand hand)
      {
         stackPosition = Model.lastPlayedRightCard;
         playerHand = hand;
      }






      /**
       * Returns the int value of the computer player as it corresponds
       * to the CardGameFramework array.
       * @return 1, the computer's index in the CardGameFramework array
       */
      public int toInt()
      {
         return 1;
      }






      /**
       * Requests the view update the computer's card area.
       *
       * Calls View.updateComputerHandImagesArray()
       */
      public void updateCardArea()
      {
         attachedView.updateComputerHandImagesArray(playerHand);

      }

   }
}






//This is the interface for the card game.
class View
{
   private static CardTable table = new CardTable("Card Table", 7, 2);
   JLabel[] computerHandImages;
   JButton[] playerHandImages;
   public Clock mainClock = new Clock();


   View()
   {
      GridLayout layout = new GridLayout(5,1);
      table.setLayout(layout);
      table.setSize(800, 600);
      table.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      table.setVisible(true);



      Thread clock = new Thread(mainClock);
      clock.start();
   }


   //Sends update from the model, this updates the play card images.
   void updatePlayerCardImagesArray(Hand playerHand)
   {

      table.pnlHumanHand.removeAll();


      int handSize = playerHand.getNumCards();
      playerHandImages = new JButton[handSize];
      for (int i = 0; i < handSize; ++i)
      {
         playerHandImages[i] = new JButton(GUICard.getIcon(playerHand.inspectCard(i)));
         playerHandImages[i].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               System.out.println("Test");
            }
         });
         System.out.println ("can you see this");
         table.pnlHumanHand.add(playerHandImages[i]);
      }
      table.pnlHumanHand.revalidate();
      table.pnlHumanHand.repaint();
   }

   //Sends update from the model, this updates the computer hand images.
   void updateComputerHandImagesArray(Hand computerHand)
   {
      table.pnlComputerHand.removeAll();


      int handSize = computerHand.getNumCards();
      computerHandImages = new JLabel[handSize];
      for (int i = 0; i < handSize; ++i)
      {
         computerHandImages[i] = new JLabel(GUICard.getIcon(computerHand.inspectCard(i)));
         table.pnlComputerHand.add(computerHandImages[i]);
      }
      table.pnlComputerHand.revalidate();
      table.pnlComputerHand.repaint();
   }

   //Sends updates from model this should update the two cards in playArea.
   void updatePlayedCardImagesArray(Card[] twoCardArray)
   {
      for (int i = 0; i < twoCardArray.length; ++i){
         //table.pnlPlayArea.add(new JLabel(GUICard.getIcon(twoCardArray[i])));
      }
      table.pnlPlayArea.revalidate();
      table.pnlPlayArea.repaint();

   }
   //Sends updates from model this should update the two scores.
   //Player score is at scores[0]. Cpu score is at scores[1].
   void updateScores(String[] scores)
   {
      JLabel playersScores = new JLabel(scores[0]);
      JLabel cupsScores = new JLabel(scores[1]);

      for (int i = 0; i <1; ++i){
         table.scoresPanel.add(playersScores, 1, 0);
         playersScores.setFont(new Font("Times New Roman", Font.PLAIN, 25));
      }

      for (int i = 0; i <1; ++i){
         table.scoresPanel.add(cupsScores, 1 , 1);
         cupsScores.setFont(new Font("Times New Roman", Font.PLAIN, 25));

      }
      table.scoresPanel.revalidate();
      table.scoresPanel.repaint();

   }
}




class Controller
{
   //Initializes
   private Model coreModel;
   private View coreView;



   // Standard Constructor
   Controller(Model coreModel, View coreView)
   {
      this.coreModel = coreModel;
      this.coreView = coreView;

   }

   //buttonListener
   public class buttonListener implements ActionListener{

      /*
      public int chosenCardPosition;
      public buttonListener (int cardLocation)
      {
         this.chosenCardPosition = cardLocation;
       }
       */

      public void actionPerformed(ActionEvent cardClick)
      {
         System.out.println("Hello the button worked");

         gameLogic(humanCardPosition(Integer.valueOf
            (cardClick.getActionCommand())));
      }

      void gameLogic(int humanCardPosition)
      { int num = humanCardPosition;
         System.out.println(num);
      }
   };


   public static int humanCardPosition (int chosenCardPosition)
   {
      int cardLocation = chosenCardPosition;
      return cardLocation;
   }

}

/* Pseudo Code
 * CONTROLER: Controller takes in MODEL and VIEW item
 * In VIEW, cards and buttons are created for player, button listeners are assigned using the CONTROLLER
 * In VIEW, CPU cards are generated on screen, they do not change (since game ends when deck is empty)
 * Player selects card via CONTROLLER assigned buttons, maybe change background of card to show selection?
 * Player then selects which stack, left or right, via CONTROLLER assigned buttons
 * Player is locked out of selecting stack if stack number is higher than selected card
 * After selection is made, card is removed from hand and a new card is dealt the player
 * Player can opt to skip turn if they have no card is higher than the stack, via CONTROLLER assigned button
 * CPU will check to see which stack has a smaller card, then place its largest card (in the array) on that stack
 * If CPU also skips, it refreshes the hands of both (MODEL)
 * Once deck reaches zero, game ends. (Based on deck counter)
 * End game displays win or lose message at the end
 *
 */
/*
//temporary, need to implement controller functions
//controller should probably have listeners for all buttons
class Controller
{

   Controller()
   {
      Model coreModel = new Model();

   }
   /*
     suggested functions:
     skipTurn()

     playCard()



}
   */

class Clock implements Runnable
{

   private JFrame display;
   private long timeInSeconds = -1;
   private String timeDisplay = "";
   private JPanel lcdPanel;
   private JLabel timeLabel;
   private boolean clockCountingDown = true;

   Clock()
   {
      display = new JFrame("Timer");
      display.setSize(150, 90);
      display.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      timeLabel = new JLabel(timeDisplay);
      lcdPanel = new JPanel();
      lcdPanel.add(timeLabel);
      display.add(lcdPanel);
      display.setVisible(true);
   }

   @Override
   public void run() {
      long startingTime = System.currentTimeMillis()/1000;
      long currentTimerTime = (System.currentTimeMillis()/1000)-startingTime;
      while (clockCountingDown)
      {
         timeInSeconds = (System.currentTimeMillis() / 1000) - startingTime;
         if (timeInSeconds != currentTimerTime)
         {
            System.out.println(timeDisplay);
            lcdPanel.remove(timeLabel);
            timeDisplay = convertTimeToString();
            timeLabel = new JLabel(timeDisplay);
            lcdPanel.add(timeLabel);
            lcdPanel.revalidate();
            lcdPanel.repaint();
            currentTimerTime = timeInSeconds;
         }
      }
   }

   private String convertTimeToString()
   {
      long hours = timeInSeconds/60;
      long minutes = timeInSeconds%60;
      DecimalFormat format = new DecimalFormat("00");
      return format.format(hours) + ":" + format.format(minutes);
   }


   void stopClock()
   {
      clockCountingDown = false;
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
   // 14 = A thru K + joker
   private static Icon[][] iconCards = new ImageIcon[15][4];
   private static Icon iconBack;
   private static final int NUM_CARD_VALUES = 15;
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
               String filename = Card.turnIntIntoCardValue(cardNum) +
                  turnIntIntoCardSuit(suitNum) + ".gif";
               iconCards[cardNum-1][suitNum]
                  = new ImageIcon("images/"+filename);
            }
         }
         iconBack = new ImageIcon("images/BK.gif");
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

      return iconCards[card.valueToInt()-1]
         [card.SuitToInt()-1];
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




}






/*This class holds creates the GUI for the card table. */
class CardTable extends JFrame
{

   static int MAX_CARDS_PER_HAND = 56;
   static int MAX_PLAYERS = 2;  // for now, we only allow 2 person games
   // The default information is from the given main.
   static int DEFAULT_NUM_CARDS_PER_HAND = 7;
   static int DEFAULT_NUM_PLAYERS = 2;
   private int numCardsPerHand;
   private int numPlayers;
   public JPanel pnlComputerHand, pnlHumanHand, pnlPlayArea, timeButtons, scoresPanel;
   public JButton start, stop, cantPlayHumanHand;
   public JLabel cpuScore, playerScore;

   CardTable(String title, int numCardsPerHand, int numPlayers)
   {
      super(title);

      /*This sets each hand and each player back to the default limits for hand
       * and the player.
       */

      if(numCardsPerHand<=MAX_CARDS_PER_HAND )
      {
         this.numCardsPerHand= DEFAULT_NUM_CARDS_PER_HAND;
      }
      else
      {
         this.numCardsPerHand= numCardsPerHand;
      }

      if(numPlayers<=MAX_PLAYERS)
      {
         this.numPlayers = DEFAULT_NUM_PLAYERS;
      }
      else
      {
         this.numPlayers = numCardsPerHand;
      }

      /*This creates the card table GUI. */

      pnlComputerHand = new JPanel();
      pnlComputerHand.setLayout(new GridLayout(1,1, 10, 10));

      pnlComputerHand.setBackground(Color.LIGHT_GRAY);
      Border textColor = new LineBorder(Color.BLACK);
      pnlComputerHand.setBorder(new TitledBorder(textColor, "Computer Hand"));

      add(BorderLayout.NORTH, pnlComputerHand);


      pnlPlayArea = new JPanel();
      pnlPlayArea.setLayout(new GridLayout(1, 1, 300, 50));
      Border textColorPlayArea = new LineBorder(Color.BLACK);
      pnlPlayArea.setBorder(new TitledBorder(textColorPlayArea, "Play Area"));
      pnlPlayArea.setBackground(Color.LIGHT_GRAY);
      add(BorderLayout.CENTER,pnlPlayArea);

      pnlHumanHand = new JPanel();
      pnlHumanHand.setLayout(new GridLayout(1,1, 10, 10));
      Border textColorHumanHand= new LineBorder(Color.BLACK);
      pnlHumanHand.setBorder(new TitledBorder(textColorHumanHand,
         "Human Hand"));
      pnlHumanHand.setBackground(Color.LIGHT_GRAY);
      add(BorderLayout.SOUTH,pnlHumanHand);



      scoresPanel = new JPanel();
      scoresPanel.setLayout(new GridLayout(2,2,10, 10));
      Border textColorScore= new LineBorder(Color.BLACK);
      scoresPanel.setBorder(new TitledBorder(textColorScore,
         "Scores"));
      cpuScore = new JLabel("Computer's Score");
      playerScore = new JLabel("Player's Score");
      cpuScore.setFont(new Font("Times New Roman", Font.PLAIN, 18));
      playerScore.setFont(new Font("Times New Roman", Font.PLAIN, 18));
      scoresPanel.add(playerScore, 2, 0);
      scoresPanel.add(cpuScore, 2, 1);

      scoresPanel.setBackground(Color.LIGHT_GRAY);
      add(BorderLayout.SOUTH,scoresPanel);

      timeButtons = new JPanel();
      timeButtons.setLayout(new FlowLayout());
      Border textColorTimer= new LineBorder(Color.BLACK);
      timeButtons.setBorder(new TitledBorder(textColorTimer,
         "Card Game"));
      stop = new JButton("Start timer");
      start = new JButton("Stop timer");
      cantPlayHumanHand = new JButton("I cannot play!");
      timeButtons.add(stop);
      timeButtons.add(start);
      timeButtons.add(cantPlayHumanHand);
      //This sets the size of the buttons.
      stop.setPreferredSize(new Dimension (100, 50));
      start.setPreferredSize(new Dimension (100, 50));
      cantPlayHumanHand.setPreferredSize(new Dimension (200, 50));

      timeButtons.setBackground(Color.LIGHT_GRAY);
      add(BorderLayout.SOUTH,timeButtons);

   }






   //Getter for NumCardsPerHand
   public int getNumCardsPerHand()
   {
      return numCardsPerHand;
   }






   //Getter for getNumPlayers
   public int getNumPlayers()
   {
      return numPlayers;
   }
}







class Card
{
   public enum Suit {spades, hearts, clubs, diamonds};
   //Characters to hold the values cards can be
   private static final char ACE = 'A', KING = 'K', QUEEN = 'Q', JACK = 'J',
      JOKER = 'X';
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
   private static Character[] valuRanks;
   private static final Suit[] suitOrder = new Suit[]{Suit.hearts, Suit.clubs, Suit.diamonds, Suit.spades};








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
   static String turnIntIntoCardValue(int cardNum)
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
    * @return An integer representation of cardValue.
    */
   int valueToInt()
   {
      setUpValuRanks();
      return indexOf(value, valuRanks);
   }

   /**
    * Converts a Card.Suit to an integer value. Simplifies
    * calculations.  0 = Hearts,
    * 1 = Clubs, 2 = Diamonds, 3 (or a garbage value) = Spades.
    *
    * @return int representation of a Card.Suit
    */
   int SuitToInt()
   {
      return indexOf(suit, suitOrder);
   }


   private int indexOf(Object target, Object[] array)
   {
      int index = -1;
      for (int i = 0; i < array.length; ++i) {
         if (target.equals(array[i]))
            index = i;
      }
      return index+1;
   }


   //card to string converter
   public String toString()
   {
      if(errorFlag)
         return "[Invalid]";
      return value + " of " + suit;

   }






   //returns if this card and another card are the same
   boolean equals(Card card)
   {
      return card.getValue() == value && card.getSuit() == suit;
   }






   //for use in the sorting
   //for use in the sorting
   private boolean lessThan(Card card)
   {
      int lhs = valueToInt();
      lhs += SuitToInt() * 100;
      int rhs = valueToInt();
      rhs += SuitToInt() * 100;
      return lhs < rhs;
   }






   //passed an array of cards and the size of the array it sorts
   //using the bubble sort algorithum
   static void arraySort(Card[] cards, int arraySize)
   {
      for(int i = 0; i < arraySize; i++)
      {
         boolean changed = false;
         for(int j = 0; j < arraySize - 1; j++)
         {
            if(cards[j].lessThan(cards[j + 1]))
            {
               Card temp = cards[j];
               cards[j] = cards[j + 1];
               cards[j + 1] = temp;
               changed = true;
            }
         }
         if(!changed)
            break;
      }
   }






   //default constructor
   Card()
   {
      value = ACE;
      suit = Suit.spades;
      setUpValuRanks();
   }






   //For use of checking of the values are valid
   private boolean isValid(char newValue, Suit newSuit)
   {
      return ((newValue >= MIN_VALUE && newValue <= MAX_VALUE)
         || newValue == ACE || newValue == KING || newValue == QUEEN
         || newValue == TEN || newValue == JACK || newValue == JOKER);
   }





   private static void setUpValuRanks()
   {

      if (valuRanks == null)
      {
         valuRanks = new Character[15];
         for (int cardNum = 0; cardNum <= 14; ++cardNum)
         {
            char cardValue = turnIntIntoCardValue(cardNum).charAt(0);
            valuRanks[cardNum] = cardValue;
         }
      }
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
      setUpValuRanks();
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
      setUpValuRanks();

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


   private static int deckSize = 56;
   private Card[] cards;
   private int topCard;
   public final int MAX_CARDS = 6*deckSize;
   private static Card[] masterPack = new Card[deckSize];






   /**
    * Constructor that creates the deck based on the number of "packs" given.
    * Note that only a maximum of MAX_CARDS cards can be used.
    *
    * @param numPacks - number of traditional 56 card packs to be used in
    *                 deck creation
    */
   public Deck(int numPacks)
   {

      allocateMasterPack();

      if (deckSize*numPacks > MAX_CARDS)
         cards = new Card[MAX_CARDS];
      else
         cards = new Card[deckSize*numPacks];

      init(numPacks);
   }






   /**
    * Empty constructor. Initializes the deck with a default of a single
    * 56 card pack.
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
    * @param numPacks number of 56 card packs to be used
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
      Card toDeal = new Card(cards[topCard]);
      removeCard(cards[topCard]);
      return toDeal;
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
    * Adds a card to the deck if it is legal to do so,
    * illegal if more than 1 copy per deck used
    * @param card card to be added
    * @return true if it was legal to add the card
    */
   boolean addCard(Card card)
   {
      if (numCardsInDeck(card) < cards.length/deckSize)
      {
         Card[] newCards = new Card[cards.length+1];
         for (int i = 0; i < cards.length; ++i)
         {
            newCards[i+1] = cards[i];
         }
         newCards[0] = card;
         return true;
      }
      else
         return false;



   }






   /**
    * Determines the number of copies of a card in the deck
    * @param card card to be searched for
    * @return the number of copies of the card searched for
    */
   int numCardsInDeck(Card card)
   {
      int numFound = 0;
      for (Card examine:
         cards)
      {
         if (examine.equals(card))
            ++numFound;
      }

      return numFound;
   }






   /**
    * Removes the card from the deck
    * @param card card to be removed
    * @return True if card is in deck and successful
    */
   boolean removeCard(Card card)
   {
      if (cardInDeck(card))
      {
         boolean cardFound = false;
         Card[] newCards = new Card[cards.length-1];
         int newCardsIndex = 0;

         if (cards[0].equals(card))
            cardFound = true;
         for (int i = 1; i < cards.length; ++i)
         {
            if (cards[i].equals(card) && !cardFound)
            {
               cardFound = true;
               newCards[newCardsIndex] = cards[0];
               ++newCardsIndex;
            }
            else
            {
               newCards[newCardsIndex] = cards[i];
               ++newCardsIndex;
            }
         }
         cards = newCards;
         return true;
      }
      else
         return false;
   }






   /**
    * Determines whether a given card is in the deck still.
    * @param findMe Card to be searched for
    * @return The boolean status of "card is in the deck"
    */
   private boolean cardInDeck(Card findMe)
   {
      for (Card card: cards)
      {
         if (findMe.equals(card))
            return true;
      }
      return false;
   }






   void sort()
   {
      Card.arraySort(cards, cards.length);
   }


   /**
    * Gives the number of cards left
    * @return Number of cards left
    */
   int getNumCards()
   {
      return cards.length;
   }




   /**
    * The following function converts int values to a Card.Suit enum.
    * For use with quick access of enums.
    *
    * @param toBeConverted - integer to be converted to a Card.Suit enum
    * @return one of the 4 suits as defined in Card class. 0 = hearts,
    * 1 = clubs, 2 = diamonds, all other ints will return spades
    */
   public static Card.Suit intToSuit(int toBeConverted)
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
            for (int cardNum = 1; cardNum <=14; ++cardNum)
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
                  case 14:
                  {
                     cardValue = 'X';
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






/*This is the hand class. It holds data for the player's hand.
 * NEEDS TO UPDATE VIEW
 * */

class Hand
{

   /*Keeps track the numbers of cards. */
   private int numCards;
   public static int MAX_CARDS = 100;
   private Card[] myCards;

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

   //function gets the left and right table card
   //returns any cards that are in the hand that are with in 1
   public Vector<Card> getPlayableCards(Card left, Card right)
   {
      Vector<Card> playableCards = new Vector<Card>();
      int index = 0;
      int leftCardNum = left.valueToInt();
      int rightCardNum = right.valueToInt();
      for(int i = 0; i < numCards; i++)
      {
         int myCardNum = myCards[i].valueToInt();
         int leftdist = Math.abs(myCardNum - leftCardNum);
         int rightdist = Math.abs(myCardNum - rightCardNum);
         if(leftdist == 1 || rightdist == 1)
         {
            playableCards.addElement(myCards[i]);
            //playableCards[index++] = myCards[i];
         }
      }
      return playableCards;
   }

   //Required by assignment spec
   public Card playCard(int cardIndex)
   {
      if ( numCards == 0 ) //error
      {
         //Creates a card that does not work
         return new Card('M', Card.Suit.spades);
      }
      //Decreases numCards.
      Card card = myCards[cardIndex];

      numCards--;
      for(int i = cardIndex; i < numCards; i++)
      {
         myCards[i] = myCards[i+1];
      }

      myCards[numCards] = null;


      return card;
   }



   void sort()
   {
      Card.arraySort(myCards, myCards.length);
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

   public int getNumCards()
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







//class CardGameFramework  ----------------------------------------------------
class CardGameFramework
{
   private static final int MAX_PLAYERS = 50;

   private int numPlayers;
   private int numPacks;            // # standard 52-card packs per deck
   // ignoring jokers or unused cards
   private int numJokersPerPack;    // if 2 per pack & 3 packs per deck, get 6
   private int numUnusedCardsPerPack;  // # cards removed from each pack
   private int numCardsPerHand;        // # cards to deal each player
   private Deck deck;               // holds the initial full deck and gets
   // smaller (usually) during play
   private Hand[] hand;             // one Hand for each player
   private Card[] unusedCardsPerPack;   // an array holding the cards not used
   // in the game.  e.g. pinochle does not
   // use cards 2-8 of any suit

   public CardGameFramework( int numPacks, int numJokersPerPack,
                             int numUnusedCardsPerPack,
                             Card[] unusedCardsPerPack,
                             int numPlayers, int numCardsPerHand)
   {
      int k;

      // filter bad values
      if (numPacks < 1 || numPacks > 6)
         numPacks = 1;
      if (numJokersPerPack < 0 || numJokersPerPack > 4)
         numJokersPerPack = 0;
      if (numUnusedCardsPerPack < 0 || numUnusedCardsPerPack > 50) //  > 1 card
         numUnusedCardsPerPack = 0;
      if (numPlayers < 1 || numPlayers > MAX_PLAYERS)
         numPlayers = 4;
      // one of many ways to assure at least one full deal to all players
      if  (numCardsPerHand < 1 ||
         numCardsPerHand >  numPacks * (56 - numUnusedCardsPerPack)
            / numPlayers )
         numCardsPerHand = numPacks * (56 - numUnusedCardsPerPack) / numPlayers;

      // allocate
      this.unusedCardsPerPack = new Card[numUnusedCardsPerPack];
      this.hand = new Hand[numPlayers];
      for (k = 0; k < numPlayers; k++)
         this.hand[k] = new Hand();
      deck = new Deck(numPacks);

      // assign to members
      this.numPacks = numPacks;
      this.numJokersPerPack = numJokersPerPack;
      this.numUnusedCardsPerPack = numUnusedCardsPerPack;
      this.numPlayers = numPlayers;
      this.numCardsPerHand = numCardsPerHand;
      for (k = 0; k < numUnusedCardsPerPack; k++)
         this.unusedCardsPerPack[k] = unusedCardsPerPack[k];

      // prepare deck and shuffle
      newGame();
   }

   // constructor overload/default for game like bridge
   public CardGameFramework()
   {
      this(1, 0, 0, null, 4, 13);
   }






   public Hand getHand(int k)
   {
      // hands start from 0 like arrays

      // on error return automatic empty hand
      if (k < 0 || k >= numPlayers)
         return new Hand();

      return hand[k];
   }






   public Card getCardFromDeck()
   {
      return deck.dealCard();
   }






   public int getNumCardsRemainingInDeck()
   {
      return deck.getNumCards();
   }






   public void newGame()
   {
      int k, j;

      // clear the hands
      for (k = 0; k < numPlayers; k++)
         hand[k].resetHand();

      // restock the deck
      deck.init(numPacks);

      // remove unused cards
      for (k = 0; k < numUnusedCardsPerPack; k++)
         deck.removeCard( unusedCardsPerPack[k] );

      // add jokers
      for (k = 0; k < numPacks; k++)
         for ( j = 0; j < numJokersPerPack; j++)
            deck.addCard( new Card('X', Card.Suit.values()[j]) );

      // shuffle the cards
      deck.shuffle();
   }






   public boolean deal()
   {
      // returns false if not enough cards, but deals what it can
      int k, j;
      boolean enoughCards;

      // clear all hands
      for (j = 0; j < numPlayers; j++)
         hand[j].resetHand();

      enoughCards = true;
      for (k = 0; k < numCardsPerHand && enoughCards ; k++)
      {
         for (j = 0; j < numPlayers; j++)
            if (deck.getNumCards() > 0)
               hand[j].takeCard( deck.dealCard() );
            else
            {
               enoughCards = false;
               break;
            }
      }

      return enoughCards;
   }






   void sortHands()
   {
      int k;

      for (k = 0; k < numPlayers; k++)
         hand[k].sort();
   }






   Card playCard(int playerIndex, int cardIndex)
   {
      // returns bad card if either argument is bad
      if (playerIndex < 0 ||  playerIndex > numPlayers - 1 ||
         cardIndex < 0 || cardIndex > hand[playerIndex].getNumCards() - 1)
      {
         //Creates a card that does not work
         return new Card('M', Card.Suit.spades);
      }

      // return the card played
      return hand[playerIndex].playCard(cardIndex);

   }






   boolean takeCard(int playerIndex)
   {
      // returns false if either argument is bad
      if (playerIndex < 0 || playerIndex > numPlayers - 1)
         return false;

      // Are there enough Cards?
      if (deck.getNumCards() <= 0)
         return false;

      return hand[playerIndex].takeCard(deck.dealCard());
   }
}










