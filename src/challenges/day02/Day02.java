package challenges.day02;

import java.util.List;

import aocutil.io.FileReader;

public class Day02 {

	/**
	 * Day 2 of the Advent of Code 2024
	 * 
	 * https://adventofcode.com/2024/day/2
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day02.class.getResource( "example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day02.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Count the number of reports that are safe
	 * 
	 * @param input The list of report strings
	 * @return The count of reports that are safe according to the safety rules
	 */
	private static long part1( final List<String> input ) {
		return input.stream( ).filter( r -> RNRReport.fromString( r ).isSafe( ) ).count( );
	}

	/**
	 * Counts the number of reports that are safe, allowing one unsafe
	 * measurement value to be removed from the report. 
	 * 
	 * @param input The list of report strings
	 * @return  The count of reports that are safe, accounting for a single
	 *   unsafe value
	 */
	private static long part2( final List<String> input ) {
		int count = 0;
		for( final String s : input ) {
			// check if the report is already safe without removing a value
			final RNRReport r = RNRReport.fromString( s );
			if( r.isSafe( ) ) {
				count++;
				continue;
			}

			// if not, try removing one value and check if that makes the report safe
			for( int i = 0; i < r.size( ); i++ ) {
				final RNRReport r2 = r.remove( i );
				if( r2.isSafe( ) ) {
					count++;
					break;
				}
			}
		}
		return count;
	}
}