package challenges.day21;

import java.util.List;

import aocutil.io.FileReader;

public class Day21 {

	/**
	 * Day 21 of the Advent of Code 2024
	 * 
	 * https://adventofcode.com/2024/day/21
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day21.class.getResource( "example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day21.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + findInstructions( ex_input, 3 ) );
		System.out.println( "Answer : " + findInstructions( input, 3 ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Answer : " + findInstructions( input, 26 ) );
	}
	
	/**
	 * Finds the shortest instruction sequence to input on a keypad so that all
	 * robots are, in turn, instructed to perform their moves and eventually the
	 * codes on the digital numpad are successfully input.
	 * 
	 * @param input The list of codes that need to be entered at the numpad
	 * @param robots The number of robots that are (recursively) involved in
	 *   typing each of the codes
	 * @return The sum of numeric code values times the length of the shortest
	 *   instruction string that will eventually input that code
	 */
	private static long findInstructions( final List<String> input, final int robots ) {
		long result = 0;
		final RobotController rc = new RobotController( robots );
		for( final String s : input )
			result += Long.parseLong( s.substring( 0, s.indexOf( 'A' ) ) ) * rc.optimiseEncoding( s );
		return result;
	}
}