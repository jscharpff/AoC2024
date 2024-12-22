package challenges.day21;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import aocutil.geometry.Coord2D;
import aocutil.grid.CoordGrid;

/**
 * Models a controller for a number of robots that operate in sequence to
 * eventually enter a code into a numerical keypad.
 */
public class RobotController {
	/** The number of robots to instruct */
	protected final int N;
	
	/** The memoisation table that stores lengths of already encoded movements */
	private final HashMap<String, Long> M;
	
	/** Table that holds the movement encodings */
	private final HashMap<String, List<String>> E;
	
	/**
	 * Creates a new RobotController
	 * 
	 * @param robots The number of robots to control
	 */
	public RobotController( final int robots ) {
		N = robots;
		
		// initialise memoisation table that holds length of already expanded
		// instruction string parts
		M = new HashMap<>( );

		// precomputes a 'move table' that holds the instruction to give to a robot
		// for every move from one character to another
		E = new HashMap<>( );
		buildMoveTable( new String[] { "789", "456", "123", ".0A" } );
		buildMoveTable( new String[] { ".^A", "<v>" } );
	}
	
	/**
	 * Starts recursive search for the shortest instruction string that, if
	 * supplied to the first robot, will eventually result in the given code
	 * being typed into a digipad
	 * 
	 * @param code The code to type
	 * @return The length of the shortest instruction string that will result in
	 *   the given code being entered
	 */
	public long optimiseEncoding( final String code ) {
		return optimiseEncoding( code, N );
	}
	
	/**
	 * Performs the actual search in a divider and conquer way that splits the
	 * instruction string into isolated parts ending with 'A' (as the 'A' command
	 * will always be at the end of a sequence to actually submit the commands)
	 * and then for each part performs the encoding and recursive search on the
	 * result thereof. This ends when we have performed depth number of
	 * recursions as now all robots can be instructed.
	 * 
	 * To prevent many recomputations, a memoisation table is used that caches
	 * results of similar recursive searches.
	 * 
	 * @param moves The moves to encode in the shortest way possible
	 * @param depth The number of encoding iterations still remaining
	 * @return The length of the shortest instruction string that for this depth
	 *   will make the robot perform the instructions given by moves
	 */
	protected long optimiseEncoding( final String moves, final int depth ) {
		// done with recursion, stop encoding and return the length of the moves
		if( depth == 0 ) return moves.length( );
		
		// have we seen this input before?
		final String key = depth + "|" + moves;
		if( M.containsKey( key ) ) return M.get( key );
		
		// nope, we have to determine the best moves for the string. We do this by
		// cutting up the string into substrings that end with an 'A'. There may be
		// multiple ways to input the directional commands but each robot will
		// always have to confirm the moves with an 'A' command.
		int lastindex = 0;
		int index = -1;
		long length = 0;
		while( (index = moves.indexOf( 'A', index + 1 )) != -1 ) {
			// extract the part of the moves to be encoded
			final String part = moves.substring( lastindex, index + 1 );

			// find the shortest encoding for this part
			long shortest = Long.MAX_VALUE;
			for( final String m : encodeMoves( part ) ) {
				final long l = optimiseEncoding( m, depth - 1 );				
				if( l < shortest ) shortest = l;
			}
			
			// add the shortest length to the total encoding length so far
			length += shortest;
			
			// and move to next part in the instruction
			lastindex = index + 1;
		}
		
		
		// done, store for future reuse and return the value
		M.put( key, length );
		return length;
	}

	/**
	 * Encodes the (partial) string of commands so that it can be entered at the
	 * keypad of the previous robot and will achieve the specified move. This
	 * function assumes that the robot starts at the 'A' position.
	 * 
	 * @param instructions The instruction string to encode
	 * @return The encoded instruction string
	 */
	protected List<String> encodeMoves( final String instructions ) {
		// process every pair of (from, to) moves and encode it. Start with an
		// empty list of all possible encodings
		List<String> moves = new ArrayList<>( );
		moves.add( "" );
		for( int i = 0; i < instructions.length( ); i++ ) {
			// get the (from, to) pair. For the first move use (A, move) as the pair
			// so that we start from 'A' always
			final char from = i == 0 ? 'A' : instructions.charAt( i - 1 );
			final char to = instructions.charAt( i );
			
			// get all encodings of this pair and produce Cartesian product of the
			// current possible instruction encodings with all possible ways to
			// encode this pair. We use a global hashmap for the latter
			final List<String> newmoves = new ArrayList<>( );
			final List<String> M2 = E.get( from + "|" + to );
			for( final String m1 : moves )
				for( final String m2: M2 )
					// add sequence and always end on an 'A' to actually submit the
					// instructions to the next robot
					newmoves.add( m1 + m2 + "A" );
			
			// continue next iteration from the result of the Cartesian product
			moves = newmoves;
		}
		
		// completed encoding, return list of all possible unique encoded
		// instruction strings that will make the robot perform the action we want
		return moves;
	}

	/**
	 * Builds move tables for the given keypad layout such that for every pair of
	 * characters it holds all possible instruction strings that will produce
	 * exactly that move.
	 * 
	 * Will store the result in the global hash table
	 * 
	 * @param keypad The moves that can be instructed via the keypad
	 */
	private void buildMoveTable( final String[] moves ) {
		// convert keypad layout into grid
		final List<String> layout = new ArrayList<>( moves.length );
		for( final String m : moves ) layout.add( m );
		final CoordGrid<Character> digits = CoordGrid.fromCharGrid( layout, '.' );
		
		// then for every combination of moves generate the corresponding 
		// instruction string to perform that move
		for( final Coord2D a : digits.getKeys( ) ) {
			for( final Coord2D b : digits.getKeys( ) ) {
				final String key = "" + digits.get( a ) + "|" + digits.get( b );
				E.put( key, new ArrayList<>( ) );
				buildMoveTable( digits, a, b, key, "" );
			}
		}
	}
	
	/**
	 * Recursive function that generates every unique instruction sequence to
	 * move from the instruction at 'from' to the instruction at 'to' on the
	 * keypad
	 * 
	 * @param keypad The keypad layout as a 2D grid
	 * @param from The starting position of the move
	 * @param to The target position to move to
	 * @param key The key to store each of the possible sequence under in the
	 *   global instruction hash table
	 * @param moves The moves taken so far
	 */
	private void buildMoveTable( final CoordGrid<Character> keypad, final Coord2D from, final Coord2D to, final String key, final String moves ) {
		// illegal move?
		if( !keypad.hasValue( from ) ) return;
		
		// are we there yet?
		if( from.equals( to ) ) {
			E.get( key ).add( moves );
			return;
		}
		
		// nope, move towards the specified target command by moving one unit along
		// the x or y axis, or both
		final Coord2D diff = from.diff( to );
		if( diff.x != 0 ) buildMoveTable( keypad, from.move( diff.x > 0 ? 1 : -1, 0 ), to, key, moves + (diff.x > 0 ? ">" : "<" ) );
		if( diff.y != 0 ) buildMoveTable( keypad, from.move( 0, diff.y > 0 ? 1 : -1 ), to, key, moves + (diff.y > 0 ? "v" : "^" ) );
	}
}
