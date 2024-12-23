package challenges.day23;

import java.util.List;

import aocutil.io.FileReader;

public class Day23 {

	/**
	 * Day 23 of the Advent of Code 2024
	 * 
	 * https://adventofcode.com/2024/day/23
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day23.class.getResource( "example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day23.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Reconstructs the LAN Party network from a list of connections, then counts
	 * the number of connected triplets that have at least one computer in it of
	 * which the name starts with a 't'.
	 * 
	 * @param input The list of connections in the LAN Network, formatted as:
	 *   <computer 1 name>-<computer 2 name>
	 * @return The count of connected triplets with at least one computer that
	 *   has a 't' starting the name
	 */
	private static long part1( final List<String> input ) {
		return new LANParty( input ).countConnectedTriplets( "t" );
	}

	/**
	 * Reconstructs the LAN Party and finds the single, maximal-size clique of
	 * connected computers.
	 * 
	 * @param input The list of connections in the LAN Network, formatted as:
	 *   <computer 1 name>-<computer 2 name>
	 * @return The largest fully connected sub graph of computers, described by
	 *   a comma separated string of their labels
	 */
	private static String part2( final List<String> input ) {
		return new LANParty( input ).getPassword( );
	}
}