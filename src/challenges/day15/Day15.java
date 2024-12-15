package challenges.day15;

import java.util.List;

import aocutil.io.FileReader;

public class Day15 {

	/**
	 * Day 15 of the Advent of Code 2024
	 * 
	 * https://adventofcode.com/2024/day/15
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day15.class.getResource( "example.txt" ) ).readLines( );
		final List<String> ex2_input = new FileReader( Day15.class.getResource( "example2.txt" ) ).readLines( );
		final List<String> ex3_input = new FileReader( Day15.class.getResource( "example3.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day15.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Example: " + part1( ex2_input ) );
		System.out.println( "Answer : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex3_input ) );
		System.out.println( "Example: " + part2( ex2_input ) );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Parses input into a warehouse layout and set of moves for the logistic
	 * robot to process. Then simulates the processing of the moves and returns
	 * the score of the resulting layout.
	 * 
	 * @param input The list of strings that describe the initial warehouse
	 *   layout, including the starting position of the robot, followed by a 
	 *   newline and then a list of move instruction strings.
	 * @return The score of the warehouse layout after all moves have been
	 *   processed by the robot
	 */
	private static long part1( final List<String> input ) {
		return Warehouse.fromStringList( input ).move( );
	}

	/**
	 * Same as part 1 but now the warehouse is twice as wide, with walls and 
	 * boxes now being of size 2.
	 * 
	 * @param input The list of strings that describe the initial warehouse
	 *   layout, including the starting position of the robot, followed by a 
	 *   newline and then a list of move instruction strings.
	 * @return The score of the warehouse layout after all moves have been
	 *   processed by the robot
	 */
	private static long part2( final List<String> input ) {
		return WarehouseSize2.fromStringList( input ).move( );
	}
}