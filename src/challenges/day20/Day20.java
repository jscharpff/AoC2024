package challenges.day20;

import java.util.List;

import aocutil.io.FileReader;

public class Day20 {

	/**
	 * Day 20 of the Advent of Code 2024
	 * 
	 * https://adventofcode.com/2024/day/20
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day20.class.getResource( "example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day20.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + solve( ex_input, 2, 1 ) );
		System.out.println( "Answer : " + solve( input, 2, 100 ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + solve( ex_input, 20, 50 ) );
		System.out.println( "Answer : " + solve( input, 20, 100 ) );
	}
	
	/**
	 * Finds all possible cheats the CPU maze
	 * 
	 * @param input The CPU Maze layout as a list of strings. one string per row
	 *   of the maze
	 * @param cheatlength The maximum length allowed to cheat walls
	 * @param minsaving The minimum saving a cheat needs to achieve compared to
	 *   the shortest path from start to end of the race without cheating
	 * @return The number of cheats possible in the CPU Maze that achieve the
	 *   specified minimal saving of steps
	 */
	private static long solve( final List<String> input, final int cheatlength, final int minsaving ) {
		final CPUMaze maze = new CPUMaze( input );
		return maze.findCheatCount( cheatlength, minsaving );
	}
}