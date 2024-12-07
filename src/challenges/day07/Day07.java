package challenges.day07;

import java.util.List;

import aocutil.io.FileReader;

public class Day07 {

	/**
	 * Day 74 of the Advent of Code 2024
	 * 
	 * https://adventofcode.com/2024/day/7
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day07.class.getResource( "example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day07.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Sums the left hand value of all equations in the list iff this value can
	 * be attained through a combination of summation and multiplication of its
	 * right hand terms.
	 * 
	 * @param input The list of equations
	 * @return The sum of all equation values that can be achieved through sums
	 *   and products of its terms
	 */
	private static long part1( final List<String> input ) {
		return input.stream( ).mapToLong( s -> BridgeCalibrator.testEquation( s, false ) ).sum( );
	}

	/**
	 * Same as part 1 but now also introducing a third concatenate operator
	 * 
	 * @param input The list of equations
	 * @return The sum of all equation values that can be achieved through sums,
	 *   products and concatenations of its terms
	 */
	private static long part2( final List<String> input ) {
		return input.stream( ).mapToLong( s -> BridgeCalibrator.testEquation( s, true ) ).sum( );
	}
}