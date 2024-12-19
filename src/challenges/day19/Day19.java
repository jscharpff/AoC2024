package challenges.day19;

import java.util.List;

import aocutil.io.FileReader;

public class Day19 {

	/**
	 * Day 19 of the Advent of Code 2024
	 * 
	 * https://adventofcode.com/2024/day/19
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day19.class.getResource( "example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day19.class.getResource( "input.txt" ) ).readLines( );

		// due to reuse of memoisation created in part 1 for part 2, we first
		// compute the pattern count per arrangement and then print results for the
		// separate parts in the typical way
		final long[] ex_result = solve( ex_input );
		final long[] result = solve( input );

		for( final int part : new int[] { 1, 2 } ) {
			System.out.println( "---[ Part " + part + " ]---" );
			System.out.println( "Example: " + ex_result[part-1] );
			System.out.println( "Answer : " + result[part-1] );
			System.out.println(  );
		}
	}
	
	/**
	 * Solves part1 and part2 of this puzzle concurrently. The solving algorithm
	 * finds the number of unique configuration of towel patterns that realises a
	 * specific arrangement of towels. For part 1 we return the count of towel
	 * arrangements for which such a configuration exists at all, while part 2
	 * asks to sum the total number of unique configurations that can produce the
	 * required towel arrangement.
	 * 
	 * @param input The list of strings that start with a comma separated string
	 *   of available patterns, followed by a newline and then all the towel
	 *   configurations to match
	 * @return An array of size two that holds the count of arrangements that can
	 *   be matched and the sum of unique pattern configurations that are possible
	 *   to match these arrangements
	 */
	private static long[] solve( final List<String> input ) {
		// create a new OnsenTowel to find pattern configurations. Note that this
		// class maintains an internal memoisation table that holds for every (sub)
		// pattern the count of unique configurations that can produce it. Hence,
		// every future call for the same (partial) arrangement is answered from
		// memory rather than starting a new DFS search. As a consequence, summing
		// the counts in this loop can be done very efficiently as towel
		// arrangements typically contain similar parts.
		final OnsenTowelArranger ot = new OnsenTowelArranger( input.get( 0 ) );		
		final long[] result = new long[ 2 ];
		for( int i = 2; i < input.size( ); i++ ) {
			// get number of unique configurations that produce this arrangement, if any
			final long count = ot.countArrangements( input.get( i ) );
			
			// count whether we can produce the arrangement and with how many unique
			// configurations of patterns
			result[0] += count != 0 ? 1 : 0;
			result[1] += count;
		}
		
		// return the count of towel arrangements that can be matched and the sum
		// of unique configurations that produce such a matching
		return result;
	}
}