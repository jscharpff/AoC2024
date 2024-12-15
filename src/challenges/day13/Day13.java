package challenges.day13;

import java.util.List;

import aocutil.io.FileReader;

public class Day13 {

	/**
	 * Day 13 of the Advent of Code 2024
	 * 
	 * https://adventofcode.com/2024/day/13
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day13.class.getResource( "example.txt" ) ).readLineGroups( ";" );
		final List<String> input = new FileReader( Day13.class.getResource( "input.txt" ) ).readLineGroups( ";" );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + clawMoves( ex_input, false ) );
		System.out.println( "Answer : " + clawMoves( input, false ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + clawMoves( ex_input, true ) );
		System.out.println( "Answer : " + clawMoves( input, true ) );
	}
	
	/**
	 * Computes the cost of getting the price out of all claw machines described
	 * by the input, if attainable at all.
	 * 
	 * @param input The list of strings that describe the Claw Machine
	 *   configurations
	 * @param biggoal False to use the original machine configuration as
	 *   specified in the input string, True to add 1B to its goal values
	 * @return The sum of costs of getting all prices out of the claw machines
	 *   that can be won
	 */
	private static long clawMoves( final List<String> input, final boolean biggoal ) {
		return input.stream( ).mapToLong( s -> ClawMachine.fromString( s, biggoal ).findWinningMoves( ) ).sum( );
	}
}