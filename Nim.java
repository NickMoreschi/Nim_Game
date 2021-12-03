import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.File;

/**
 * Text interface for Nim game. Has an interactive mode which prompts for
 * user moves, or a file-based mode which reads user moves line by line.
 *
 * @author sna4
 * 07/05/18
 */

public class Nim
{
    private boolean gameOver = false;
    private static boolean interactive = false;
    private static Scanner input;
    private State state;
    private GameTree tree;
    private GameTree.Node curr;
    
    /**
     * Nim interface setup.
     * 
     *
     */   
    public Nim( )
    {
        int n = getValue( "Number of stacks: " );
        
        boolean playerTurn = false;
        int turn = getValue( "Who moves first? Player (0) or computer (1): " );
        if( turn == 0 )
            playerTurn = true;
        
        state = new NimState( n, playerTurn );
        tree = new GameTree( state );
        curr = tree.getRoot();
        System.out.println( "" + tree.getSize() + " nodes in tree." );
        
        playGame();
    }
    
    /**
     * Main game loop.
     * 
     */ 
    public void playGame( )
    {
        while( !gameOver )
        {
            System.out.print( state.toString() );
            if( curr != null )
                System.out.println( "Predicted win for " +
                                    curr.getPrediction() + ".\n" );
            State.Move m;
            if( state.isPlayerTurn() )
            {
                //get user move
                int num = getValue( "Number to take (1-3): " );
                int stack = getValue( "From stack: " );

                m = new NimState.Move(stack, num);
                if( state.doMove( m ) )
                {
                    System.out.println( "Player move: " + m );
                    curr = curr.findChild( m );
                }
                else
                {
                    System.out.println( "Can't take " + num +
                                        " objects from " + stack );
                    System.out.println( "Please try again!" );
                }
            }
            else
            {
                //get move from tree
                if( curr != null )
                {
                    curr = curr.getBestChild();
                    m = curr.getMove();
                    System.out.println( "Computer move: " + m );
                    state.doMove( m );
                }
                else
                {
                    System.out.println( "Tree not built. Exiting." );
                    return;
                }
            }

            gameOver = state.gameOver();
        }

        System.out.println( "Game over!" );
        if( !state.isPlayerTurn() )
            System.out.println( "Computer won." );
        else
            System.out.println( "Player won." );
    }

    /**
     * Gets a value from input. If in interactive mode, prompts user.
     * 
     * @param prompt String Text to provide to user.
     *
     * @return Integer provided by user.
     */ 
    public int getValue( String prompt )
    {
        if( interactive )
            System.out.print( prompt );

        return input.nextInt();
    }
    
    //------------------ main ------------------------------------------
    /**
     * Application startup.
     * 
     * @param args String[]   command line arguments
     */   
    public static void main( String[] args )
    {

        if( args.length > 0 )
        {
            Nim.interactive = false;
            try
            {
                Nim.input = new Scanner( new File( args[0] ) );
            }
            catch( FileNotFoundException ex )
            {
                System.out.println( "File not found: " + args[0] );
                return;
            }
        }
        else
        {
            Nim.interactive = true;
            Nim.input = new Scanner( System.in );
        }
        
        new Nim( );
        
    }
    
}
