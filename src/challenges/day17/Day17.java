package challenges.day17;

import java.util.List;

import aocutil.io.FileReader;

public class Day17 {

	/**
	 * Day 17 of the Advent of Code 2024
	 * 
	 * https://adventofcode.com/2024/day/17
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day17.class.getResource( "example.txt" ) ).readLines( );
		final List<String> ex2_input = new FileReader( Day17.class.getResource( "example2.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day17.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex2_input ) );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Runs the program specified by the list of strings.
	 * 
	 * @param input The list of strings that describe first the initial memory
	 *   values of the program, followed by a list of integer encoded
	 *   instructions and operands
	 * @return The output string that results from running the program, which is
	 *   a comma separated string of all integers it outputs
	 */
	private static String part1( final List<String> input ) {
		final IntCodeMachine icm = new IntCodeMachine( input );
		final String out = icm.run( ).toString( );
		return out.substring( 1, out.length( ) - 1 ).replaceAll( ", ", "," );
	}

	/**
	 * Reverse engineer the input to memory register A of the specified program
	 * such that it will make the program output its own instruction set exactly
	 * 
	 * @param input The program to reverse engineer
	 * @return The value to set to register A to make the program reproduce
	 *   itself through its output instructions
	 */
	private static long part2( final List<String> input ) {
		final ReverseEngineerICM re = new ReverseEngineerICM( new IntCodeMachine( input ) );
		final String targetoutput = input.get( 4 ).split( ": " )[1];
		return re.findOutput( targetoutput );
	}
}