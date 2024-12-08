package challenges.day08;

import java.util.List;

import aocutil.io.FileReader;

public class Day08 {

	/**
	 * Day 8 of the Advent of Code 2024
	 * 
	 * https://adventofcode.com/2024/day/8
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day08.class.getResource( "example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day08.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Projects single antinodes for every pair of matching antennas and counts
	 * the total number of projections within the antenna map boundaries.
	 * 
	 * @param input The list of strings that describes the antenna layout grid
	 * @return The number of single antinode projections within the grid
	 */
	private static long part1( final List<String> input ) {
		return new AntennaMap( input ).countAntiNodes( false );
	}

	/**
	 * Projects repeating antinodes for every pair of matching antennas and counts
	 * the total number of projections within the antenna map boundaries.
	 * 
	 * @param input The list of strings that describes the antenna layout grid
	 * @return The number of repeated antinode projections within the grid
	 */
	private static long part2( final List<String> input ) {
		return new AntennaMap( input ).countAntiNodes( true );
	}
}