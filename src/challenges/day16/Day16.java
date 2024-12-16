package challenges.day16;

import java.util.List;

import aocutil.io.FileReader;

public class Day16 {

	/**
	 * Day 16 of the Advent of Code 2024
	 * 
	 * https://adventofcode.com/2024/day/16
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day16.class.getResource( "example.txt" ) ).readLines( );
		final List<String> ex2_input = new FileReader( Day16.class.getResource( "example2.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day16.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Example: " + part1( ex2_input ) );
		System.out.println( "Answer : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Example: " + part2( ex2_input ) );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Finds the best path for the reindeer to traverse through the maze to the
	 * finish with the lowest cost
	 * 
	 * @param input The list of strings that describe the maze layout
	 * @return The cost of the least-cost path from reindeer starting position to
	 *   the finish line
	 */
	private static long part1( final List<String> input ) {
		return new ReindeerMaze( input ).findBestPath( );
	}

	/**
	 * Finds the tiles that offer the best view on the reindeer races. I.e.,
	 * those tiles that are in at least one lowest-cost path
	 * 
	 * @param input The list of strings that describe the maze layout
	 * @return The number of unique tiles that are part of at least one lowest-
	 *   cost path
	 */
	private static long part2( final List<String> input ) {
		return new ReindeerMaze( input ).findBestSeats( );
	}
}