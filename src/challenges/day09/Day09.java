package challenges.day09;

import aocutil.io.FileReader;

public class Day09 {

	/**
	 * Day 9 of the Advent of Code 2024
	 * 
	 * https://adventofcode.com/2024/day/9
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final String ex_input = new FileReader( Day09.class.getResource( "example.txt" ) ).readLines( ).get( 0 );
		final String input = new FileReader( Day09.class.getResource( "input.txt" ) ).readLines( ).get( 0 );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Performs defragmentation of the amphipod's computer by moving individual
	 * data fragments from the right side to free space on the left side of the
	 * memory, given the disk layout as input
	 * 
	 * @param input The memory layout as compacted string
	 * @return The checksum of the memory after defragmentation
	 */
	private static long part1( final String input ) {
		return Defragmenter.defragmentSingle( input );
	}

	/**
	 * Performs defragmentation of the amphipod's computer by moving blocks of
	 * data fragments from the right side to free space on the left side of the
	 * memory, given the disk layout as input
	 * 
	 * @param input The memory layout as compacted string
	 * @return The checksum of the memory after defragmentation
	 */
	private static long part2( final String input ) {
		return Defragmenter.defragmentBlock( input );
	}
}