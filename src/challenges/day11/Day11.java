package challenges.day11;

import aocutil.io.FileReader;

public class Day11 {

	/**
	 * Day 11 of the Advent of Code 2024
	 * 
	 * https://adventofcode.com/2024/day/11
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final String ex_input = new FileReader( Day11.class.getResource( "example.txt" ) ).readLines( ).get( 0 );
		final String input = new FileReader( Day11.class.getResource( "input.txt" ) ).readLines( ).get( 0 );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + blink( ex_input, 25 ) );
		System.out.println( "Answer : " + blink( input, 25 ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + blink( ex_input, 75 ) );
		System.out.println( "Answer : " + blink( input, 75 ) );
	}
	
	/**
	 * Plays a number of rounds of Plutonian Pebbles with the given startup
	 * configuration of pebbles
	 * 
	 * @param input The starting configuration of pebbles
	 * @param rounds The number of rounds to play
	 * @return The number of pebbles after the game has finished
	 */
	private static long blink( final String input, final int rounds ) {
		return new PlutonianPebbles( input ).blink( rounds );
	}
}