package challenges.day12;

import java.util.List;

import aocutil.io.FileReader;

public class Day12 {

	/**
	 * Day 12 of the Advent of Code 2024
	 * 
	 * https://adventofcode.com/2024/day/12
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day12.class.getResource( "example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day12.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Computes the total cost of fencing all regions of plants in the garden.
	 * 
	 * @param input The garden layout, described by a list of strings
	 * @return The cost of fencing all regions
	 */
	private static long part1( final List<String> input ) {
		return new GardenLayout( input ).fenceCost( false );
	}

	/**
	 * Computes the total cost of fencing all regions of plants in the garden.
	 * This time, however, we use the number of unique sides instead of
	 * individual perimeter segments to compute the costs.
	 * 
	 * @param input The garden layout, described by a list of strings
	 * @return The cost of fencing all regions
	 */
	private static long part2( final List<String> input ) {
		return new GardenLayout( input ).fenceCost( true );
	}
}