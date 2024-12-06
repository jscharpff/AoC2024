package challenges.day06;

import java.util.List;

import aocutil.io.FileReader;

public class Day06 {

	/**
	 * Day 6 of the Advent of Code 2024
	 * 
	 * https://adventofcode.com/2024/day/6
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day06.class.getResource( "example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day06.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Counts all positions in the lab layout that are visited by the guard in its
	 * patrol route
	 * 
	 * @param input The layout of the lab
	 * @return The number of positions visited by the guard
	 */
	private static long part1( final List<String> input ) {
		return new LabPatrol( input ).countCoverage( );
	}

	/**
	 * Counts the number of positions in the lab layout that, if blocked, will
	 * make the guard patrol end up in a loop
	 * 
	 * @param input The layout of the lab
	 * @return The number of positions that can be blocked to introduce a patrol
	 *   loop
	 */
	private static long part2( final List<String> input ) {
		return new LabPatrol( input ).countLoops( );
	}
}