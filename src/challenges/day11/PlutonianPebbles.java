package challenges.day11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Models a game of Plutonian Pebbles in which a line of pebbles is changed
 * according to some very specific rules
 */
public class PlutonianPebbles {
	/** The list of starting pebbles */
	private final List<Long> pebbles;
	
	/**
	 * Creates a new game of Plutonian Pebbles from the given starting
	 * configuration
	 * 
	 * @param configuration A string of space-separated pebble numbers that is
	 *   the starting configuration of the game
	 */
	public PlutonianPebbles( final String configuration ) {
		pebbles = new ArrayList<>( );
		for( final String s : configuration.split( " " ) )
			pebbles.add( Long.parseLong( s ) );
	}
	
	/**
	 * Runs the game for the specified number of rounds (blinks)
	 * @param times The number of rounds to play
	 * @return THe number of pebbles once the game finishes
	 */
	public long blink( final int times ) {
		return pebbles.stream( ).mapToLong( l -> blink( new HashMap<>( ), l, times ) ).sum( );
	}
	
	/**
	 * Recursively performs blinks on the given pebble to determine the total
	 * number of pebbles after all blinks have finished. Uses memoisation to
	 * avoid recursing on the same problem over and over again.
	 * 
	 * @param M The memoization table
	 * @param pebble The current pebble value
	 * @param times The number of blinks remaining
	 * @return The number of pebbles after all blinks have occurred
	 */
	protected long blink( final Map<String, Long> M, final long pebble, final int times ) {
		// no more recursions? then only this single pebble is left
		if( times == 0 ) return 1;
		
		// have we computed this recursion before? return it
		final String key = pebble + "," + times;
		if( M.containsKey( key ) ) return M.get( key );
		
		// not see before, apply rules to pebble value
		final long result;
		if( pebble == 0 ) result = blink( M, 1, times - 1 );
		else if( ("" + pebble).length( ) % 2 == 0 ) {
			final String pb = "" + pebble;
			result = blink( M, Long.parseLong( pb.substring( 0, pb.length( ) / 2 ) ), times - 1 ) +
					blink( M, Long.parseLong( pb.substring( pb.length( ) / 2) ), times - 1 );
		} else
			result = blink( M, pebble * 2024, times - 1 );
		
		// memoize result and return it
		M.put( key, result );
		return result;
	}

}
