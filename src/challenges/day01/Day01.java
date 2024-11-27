package challenges.day01;

import java.util.List;

import aocutil.io.FileReader;

public class Day01 {

	/**
	 * Day 1 of the Advent of Code 2024
	 * 
	 * https://adventofcode.com/2024/day/1
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day01.class.getResource( "example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day01.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * 
	 * @param input
	 * @return 
	 */
	private static long part1( final List<String> input ) {
		return -1;
	}

	/**
	 * 
	 * @param input
	 * @return 
	 */
	private static long part2( final List<String> input ) {
		return -1;
	}}