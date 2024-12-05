package challenges.day05;

import java.util.List;
import java.util.stream.Stream;

import aocutil.io.FileReader;

public class Day05 {

	/**
	 * Day 5 of the Advent of Code 2024
	 * 
	 * https://adventofcode.com/2024/day/5
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day05.class.getResource( "example.txt" ) ).readLineGroups( ";" );
		final List<String> input = new FileReader( Day05.class.getResource( "input.txt" ) ).readLineGroups( ";" );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Sums the middle page numbers of all orderings that are valid
	 * 
	 * @param input The ordering rule set and page orderings as two strings
	 * @return The sum of middle page numbers of valid orderings
	 */
	private static long part1( final List<String> input ) {
		// create manual printer from rule set description
		final ManualPrinter mp = new ManualPrinter( input.get( 0 ) );
		
		// sum the middle page numbers of only those orderings that match the rules
		return Stream.of( input.get( 1 ).split( ";" ) ).mapToLong( mp::getMiddleIfValid ).sum( );
	}

	/**
	 * Sums the middle page numbers of all orderings that are not valid, after
	 * first reordering them conform the rule set
	 * 
	 * @param input The ordering rule set and page orderings as two strings
	 * @return The sum of middle page numbers of valid orderings after reorder
	 */
	private static long part2( final List<String> input ) {
		// create manual printer from rule set description
		final ManualPrinter mp = new ManualPrinter( input.get( 0 ) );

		// now skip the page orderings that are already valid. Instead, sum the 
		// middle page numbers of non-valid orderings after reordering them conform
		// the ruleset
		return Stream.of( input.get( 1 ).split( ";" ) ).filter( s -> mp.getMiddleIfValid( s ) == 0 ).mapToLong( mp::reorder ).sum( );
	}
}