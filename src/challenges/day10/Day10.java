package challenges.day10;

import java.util.List;

import aocutil.io.FileReader;

public class Day10 {

	/**
	 * Day 10 of the Advent of Code 2024
	 * 
	 * https://adventofcode.com/2024/day/10
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day10.class.getResource( "example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day10.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Counts number of trail ends that can be reached via gradually increasing
	 * hiking paths from every trail head at level 0
	 * 
	 * @param input The elevation layout of the hiking area
	 * @return The count of unique trail heads that can be reached
	 */
	private static long part1( final List<String> input ) {
		return new TrailMap( input ).countTrails( false );
	}

	/**
	 * Counts number of distinct paths to all trail heads that can be reached
	 * via gradually increasing hiking paths from every trail head at level 0
	 * 
	 * @param input The elevation layout of the hiking area
	 * @return The count of unique trails that can be taken to all trail ends
	 */
	private static long part2( final List<String> input ) {
		return new TrailMap( input ).countTrails( true );
	}
}