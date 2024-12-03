package challenges.day03;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aocutil.io.FileReader;

public class Day03 {

	/**
	 * Day 3 of the Advent of Code 2024
	 * 
	 * https://adventofcode.com/2024/day/3
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final String ex_input = new FileReader( Day03.class.getResource( "example.txt" ) ).readLines( ).get( 0 );
		final String ex2_input = new FileReader( Day03.class.getResource( "example2.txt" ) ).readLines( ).get( 0 );
		final String input = new FileReader( Day03.class.getResource( "input.txt" ) ).readLines( ).get( 0 );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex2_input ) );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Extract only the sensible mul(\d,\d) operations from the input and then sum
	 * the results of these multiplications
	 * 
	 * @param input The string of commands that has been corrupted
	 * @return The sum of all valid multiply operation results in there
	 */
	private static long part1( final String input ) {
		final Matcher m = Pattern.compile( "mul\\((\\d{1,3}),(\\d{1,3})\\)" ).matcher( input );
		long sum = 0;
		while( m.find( ) ) sum += Integer.parseInt( m.group( 1 ) ) * Integer.parseInt( m.group( 2 ) );
		return sum;
	}

	/**
	 * Same as part1 but now skip any part of the command string that is
	 * encapsulated by "don't()...do()"
	 * 
	 * @param input The string of commands that has been corrupted
	 * @return The sum of all valid multiply operation results in there, now only
	 *   
	 */
	private static long part2( final String input ) {
		long sum = 0;
		
		// run part1 only on those subsequences of the input that are not
		// encapsulated by "don't()..do()" pairs
		int previdx = 0;
		int idx = -1;
		while( (idx = input.indexOf( "don't()", previdx )) != -1 ) {
			// process part up to the "don't()" keyword 
			sum += part1( input.subSequence( previdx, idx ).toString( ) );
			
			// then move index until after the next "do()" or end of string if
			// there are no more
			final int newdo = input.indexOf( "do()", idx );
			previdx = newdo != -1 ? newdo + 4 : input.length( ) + 1;
		}
		
		// also add the end part of the input if it was not preceded by a "don't()"
		if( previdx < input.length( ) ) sum += part1( input.substring( previdx ).toString( ) );
		
		// return the sum of all parts not encapsulated
		return sum;
	}
}