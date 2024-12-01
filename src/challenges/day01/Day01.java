package challenges.day01;

import java.util.ArrayList;
import java.util.Collections;
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
	 * Find distance between pairs in both lists, ordered from smallest to
	 * largest.
	 * 
	 * @param input The list of strings that hold a pair of integers from both
	 *   lists, separated by spaces
	 * @return The sum of distances between sorted list elements
	 */
	private static long part1( final List<String> input ) {
		// parse input into two lists
		final List<Integer> l1 = new ArrayList<>( input.size( ) );
		final List<Integer> l2 = new ArrayList<>( input.size( ) );
		for( final String i : input ) {
			final String[] s = i.split( "   " );
			l1.add( Integer.parseInt( s[0] ) );
			l2.add( Integer.parseInt( s[1] ) );
		}
		
		// sort lists smallest to largest
		Collections.sort( l1 );
		Collections.sort( l2 );
		
		// return sum of distances between each pair in both lists
		long sum = 0;
		for( int i = 0; i < l1.size( ); i++ )
			sum += Math.abs( l2.get( i ) - l1.get( i ) );
		return sum;
	}

	/**
	 * Finds the similarity score between the two lists. This is the sum of
	 * each element from the left list times the number of times it occurs in the
	 * right list.
	 * 
	 * @param input The list of strings that hold a pair of integers from both
	 *   lists, separated by spaces
	 * @return 
	 */
	private static long part2( final List<String> input ) {
		// parse input into two lists
		final List<Integer> l1 = new ArrayList<>( input.size( ) );
		final List<Integer> l2 = new ArrayList<>( input.size( ) );		
		for( final String i : input ) {
			final String[] s = i.split( "   " );
			l1.add( Integer.parseInt( s[0] ) );
			l2.add( Integer.parseInt( s[1] ) );
		}

		// for every number in the left list, find the number of times it occurs in
		// the right list and multiply it by that value. Return the sum thereof
		long sum = 0;
		for( final int i : l1 ) {
			int count = 0;
			for( final int j : l2 )
				if( i == j ) count++;
			sum += i * count;
		}
		return sum;
	}
}