package challenges.day14;

import java.util.List;

import aocutil.io.FileReader;

public class Day14 {

	/**
	 * Day 14 of the Advent of Code 2024
	 * 
	 * https://adventofcode.com/2024/day/14
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day14.class.getResource( "example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day14.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input, 11, 7 ) );
		System.out.println( "Answer : " + part1( input, 101, 103 ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Answer : " + part2( input, 101, 103 ) );
	}
	
	/**
	 * Simulates the movement of the bathroom guarding robots in the Eater Bunny
	 * HQ for 100 seconds.
	 * 
	 * @param input The initial robot positions and movement vectors
	 * @param width The width of the patrol area
	 * @param height The height of the patrol ares
	 * @return The product of the robot counts per quadrant of the area
	 */
	private static long part1( final List<String> input, final int width, final int height ) {
		final RobotGrid g = new RobotGrid( width, height );
		g.addRobots( input );
		return g.simulate( 100 );
	}

	/**
	 * Simulates the movement of the bathroom guarding robots until they are in a
	 * very specific position that results in the display of a Christmas Tree.
	 * 
	 * @param input The initial robot positions and movement vectors
	 * @param width The width of the patrol area
	 * @param height The height of the patrol ares
	 * @return The number of seconds required for the patrol robots to form the
	 *   Christmas Tree
	 */
	private static long part2( final List<String> input, final int width, final int height ) {
		final RobotGrid g = new RobotGrid( width, height );
		g.addRobots( input );
		return g.simulateEasterEgg( 10000000 );
	}
}