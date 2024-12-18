package challenges.day18;

import java.util.List;

import aocutil.geometry.Coord2D;
import aocutil.io.FileReader;

public class Day18 {

	/**
	 * Day 18 of the Advent of Code 2024
	 * 
	 * https://adventofcode.com/2024/day/18
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day18.class.getResource( "example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day18.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input, 7, 12 ) );
		System.out.println( "Answer : " + part1( input, 71, 1024 ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input, 7 ) );
		System.out.println( "Answer : " + part2( input, 71 ) );
	}
	
	/**
	 * Creates a Pushdown Automaton of the given size and will simulate the
	 * corruption of its memory segments using the given list of segments to
	 * corrupt and a number of segments to corrupt using that list. Then finds
	 * the shortest path from top left to bottom right of the memory without
	 * traversing any corrupt memory segments, if possible.
	 * 
	 * @param corrupt The list that describes the segments that will be corrupted,
	 *   ordered on the time they will go corrupt
	 * @param memsize The size N of the 2 dimensional NxN memory
	 * @param nanoseconds The number of nanoseconds to simulate the segment
	 *   corruption, as specified by the list
	 * @return The length of the shortest path from top left to bottom right
	 *   without traversing any corrupted memory segments. If no such path exists,
	 *   -1 is returned.
	 */
	private static long part1( final List<String> corrupt, final int memsize, final int nanoseconds ) {
		// create the automaton
		final PushdownAutomaton pa = new PushdownAutomaton( memsize );
		
		// keep corrupting segments until all nanoseconds have passed
		int corruptcount = nanoseconds;
		for( final String s : corrupt ) {
			if( --corruptcount < 0 ) break;
			pa.corrupt( Coord2D.fromString( s ) );
		}

		// then find the shortest path from top left to bottom right in the memory
		return pa.findShortestPath( new Coord2D( 0, 0 ), new Coord2D( memsize - 1, memsize - 1 ) );
	}

	/**
	 * Finds the first segment that, if corrupted, will make the end unreachable.
	 * Reuses part1 to run a binary search that in each iteration halves the
	 * index of segments until we pinpoint the exact index at which the memory
	 * can no longer be traversed from start to end.
	 * 
	 * @param input The list that describes the sequence in which memory segments
	 *   become corrupt over time
	 * @return The first segment that will corrupt the memory such that it no
	 *   longer can be traversed from top left to bottom right without crossing
	 *   any corrupted memory segment
	 */
	private static String part2( final List<String> input, final int memsize ) {
		// perform binary search on the segment list index
		int lb = 0;
		int ub = input.size( ) - 1;
		while( lb != ub ) {
			final int half = (ub - lb) / 2 + lb;
			// reuse part 1 to see if there is a path possible. If part1 returns -1
			// it is not longer possible to reach the end
			if( part1( input, memsize, half ) > 0 ) {
				// use a minor correction here if the lb already has the value of half
				// as an effect of using integer indexes
				lb = (lb == half ? half + 1 : half);
			} else {
				ub = half;
			}
		}
		
		// we found the index at which the memory becomes too corrupted to traverse
		// return the corresponding offending segment
		return input.get( lb - 1 );
	}
}