package challenges.day04;

import java.util.List;

import aocutil.io.FileReader;

public class Day04 {

	/**
	 * Day 4 of the Advent of Code 2024
	 * 
	 * https://adventofcode.com/2024/day/4
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day04.class.getResource( "example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day04.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Counts the number of times the word XMAS is present in the crossword
	 * puzzle, in all directions including diagonals
	 * 
	 * @param input The list of strings that describe the rows of the crossword
	 *   puzzle
	 * @return The count of XMASes in the puzzle  
	 */
	private static long part1( final List<String> input ) {
		return CrossWord.fromStringList( input ).count( "XMAS" );
	}

	/**
	 * Counts the number of X-MAS crosses in the crossword puzzle, i.e., pairs
	 * of "MAS" with overlapping 'A's in the shape of a cross
	 * 
	 * @param input The list of strings that describe the rows of the crossword
	 *   puzzle
	 * @return The count of X-MAS crosses in the puzzle  
	 */
	private static long part2( final List<String> input ) {
		return CrossWord.fromStringList( input ).countXMAS( );
	}
}