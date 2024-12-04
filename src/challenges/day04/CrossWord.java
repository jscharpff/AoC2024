package challenges.day04;

import java.util.List;

import aocutil.geometry.Coord2D;
import aocutil.grid.CoordGrid;

/**
 * Holds a simple, character-based crossword puzzle
 * 
 * @author Joris
 */
public class CrossWord {
	/** The grid that represents the puzzle */
	protected final CoordGrid<Character> puzzle;

	/**
	 * Creates a new crossword puzzle from the grid 
	 * 
	 * @param input The character grid of the puzzle
	 */
	protected CrossWord( final CoordGrid<Character> input ) {
		this.puzzle = input;
	}
	
	/**
	 * Counts the occurrences of the specified word
	 * 
	 * @param word The word to search for
	 * @return The amounnt of times the word is present in the puzzle
	 */
	public long count( final String word  ) {
		long count = 0;
		// start search only when first character of the word is encountered
		for( final Coord2D c : puzzle.find( word.charAt( 0 ) ) ) { 
			
			// try find the word in all directions
			for( final int dx : new int[] { -1, 0, 1 } ) {
				for( final int dy : new int[] { -1, 0, 1 } ) {
					if( dx ==0 && dy == 0 ) continue;
					count += find( word, c, new Coord2D( dx,  dy ) ) ? 1 : 0;
				}
			}
		}
		return count;
	}
	
	/**
	 * Finds the specific word from the given start coordinate in the puzzle and
	 * looking in the specified direction
	 * 
	 * @param word The word to search
	 * @param start The puzzle coordinate to start at
	 * @param dir The direction to search in
	 * @return True iff all of the characters in the word are found in their
	 *   correct sequence in the given direction
	 */
	private boolean find( final String word, final Coord2D start, final Coord2D dir ) {
		// simply move along until we find an unexpected character
		Coord2D c = start;
		for( int i = 0; i < word.length( ) ; i++ ) {
			if( puzzle.get( c ) != word.charAt( i ) ) return false;
			c = c.move( dir );
		}
		
		// no unexpected character, we found the word!
		return true;
	}
	
	/**
	 * Count all occurrences of X-MAS, that is, all pairs of 2 MASes that are in
	 * the shape of an X like:
	 * 
	 *    M S       S S
	 *     A   or    A    etc.
	 *    M S       M M
	 * 
	 * @return The number of X-MAS crosses in the puzzle
	 */
	public long countXMAS( ) {
		long count = 0;
		// now find the centre 'A's to start search from
		for( final Coord2D c : puzzle.find( 'A' ) ) { 

			// check diagonals for combinations of M's and S's
			int MASCount = 0;
			for( final int dx : new int[] { -1, 1 } )
				for( final int dy : new int[] { -1, 1 } )
					if( puzzle.get( c.x - dx, c.y - dy ) == 'M' && puzzle.get( c.x + dx, c.y + dy ) == 'S' ) MASCount++;
			
			// this is a cross if we found exactly two M-S diagonals
			if( MASCount == 2 ) count ++;
		}
		return count;
	}
	
	/**
	 * Creates a Crossword puzzle object from a list of strings, each describing
	 * a single row of the puzzle
	 * 
	 * @param in The list of rows
	 * @return The crossword puzzle
	 */
	public static CrossWord fromStringList( final List<String> in ) {
		return new CrossWord( CoordGrid.fromCharGrid( in, '.' ) );
	}
}
