package challenges.day22;

import java.util.List;

import aocutil.io.FileReader;

public class Day22 {

	/**
	 * Day 22 of the Advent of Code 2024
	 * 
	 * https://adventofcode.com/2024/day/22
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day22.class.getResource( "example.txt" ) ).readLines( );
		final List<String> ex2_input = new FileReader( Day22.class.getResource( "example2.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day22.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex2_input ) );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Sums the secret codes of all buyers in the Monkey Marketplace after
	 * running the secret generation procedure for 2000 rounds.
	 * 
	 * @param input The initial seed of each of the buyers
	 * @return The sum of secrets after 2000 iterations
	 */
	private static long part1( final List<String> input ) {
		final MonkeyMarket mm = new MonkeyMarket( input );
		return mm.sumSecrets( 2000 );
	}

	/**
	 * Runs the market place simulation and finds for 2000 rounds and finds the
	 * maximum number of bananas we can obtain from it.
	 * 
	 * @param input The initial seed of each of the buyers
	 * @return The sum of secrets after 2000 iterations
	 */
	private static long part2( final List<String> input ) {
		final MonkeyMarket mm = new MonkeyMarket( input );
		return mm.buyMostBananas( 2000 );
	}
}