package challenges.day13;

import aocutil.algebra.Linear;
import aocutil.string.RegexMatcher;

/**
 * A simple, two-button Claw Machine with a prize!
 */
public class ClawMachine {
	/** The cost of the A and B button */
	protected final long[] cost = new long[] { 3, 1 };
	
	/** The X and Y positions of the claw machine prize */
	protected final long[] goal;
	
	/** The X and Y movement when pressing the A button */
	protected final long[] moveA;
	
	/** The X and Y movement when pressing the B button */
	protected final long[] moveB;
	
	/** Maximal rounding error at which doubles are still considered integer */
	private final static double PRECISION = 0.001;
	
	/**
	 * Creates a new instance of a claw machine
	 * 
	 * @param A The A button configuration in terms of x and y movement 
	 * @param A The B button configuration in terms of x and y movement 
	 * @param goal The goal X and Y at which the prize is sitting
	 */
	private ClawMachine( final long[] A, final long[] B, final long[] goal ) {
		this.moveA = A;
		this.moveB = B;
		this.goal = goal;
	}
	
	/**
	 * Finds the winning combination of A and B moves that will grab the prize of
	 * the claw machine, if possible at all.
	 * 
	 * @return The cost of the A and B moves required to win the price
	 */
	public long findWinningMoves( ) {
		// express A in terms of B by rewriting X = A.x * A + B.x * B for both X and Y
		final Linear Ax = new Linear( -moveB[0] / (double)moveA[0], goal[0]/(double)moveA[0] );  
		final Linear Ay = new Linear( -moveB[1] / (double)moveA[1], goal[1]/(double)moveA[1] );
		
		// then find their intersection. This is the only point at which both X and
		// Y are satisfied due to the shape of their linear functions
		final double[] isect = Ax.intersects( Ay );
		
		// check if this intersection is integer (enough), if not the equation is
		// not solvable with integer values. Allow some slack for precision errors
		// that occur when dealing with large doubles
		final long[] isr = new long[] { (long) Math.round( isect[0] ), (long)Math.round( isect[1] ) };
		if( Math.abs( isect[0] - isr[0] ) > PRECISION || Math.abs( isect[1] - isr[1] ) > PRECISION ) return 0;
		
		// integer solvable! return cost of the moves performed
		return isr[1] * cost[0] + isr[0] * cost[1];
	}
	
	/**
	 * @return The claw machine configuration as a line-separated string
	 */
	@Override
	public String toString( ) {
		final StringBuilder sb = new StringBuilder( );
		sb.append( "Button A: X+" + moveA[0] + ", Y+" + moveA[1] + "\n" );
		sb.append( "Button B: X+" + moveB[0] + ", Y+" + moveB[1] + "\n" );
		sb.append( "Prize: X=" + goal[0] + ", Y=" + goal[1] );
		return sb.toString( );
	}
	
	/**
	 * Reconstructs a Claw Machine from a string description like (although one a
	 * single line and separated by a semicolon):
	 * 
	 * 	  Button A: X+15, Y+29
	 *    Button B: X+56, Y+23
	 *    Prize: X=9778, Y=15506		
	 *    
	 * @param input The string that describes this claw machines configuration
	 * @param biggoal True to add 1B to the goal value after decoding it from the
	 *   input configuration. False will not affect the machine configuration.
	 * @return The reconstructed claw machine
	 */
	public static ClawMachine fromString( final String input, final boolean biggoal ) {
		// split the string that describes the configuration
		final String[] s = input.split( ";" );
		
		// then first read the A and B button move configurations
		final RegexMatcher rm = new RegexMatcher( "X\\+(\\d+), Y\\+(\\d+)" );
		rm.match( s[0] );
		final long[] A = new long[] { rm.getLong( 1 ), rm.getLong( 2 ) };
		rm.match( s[1] );
		final long[] B = new long[] { rm.getLong( 1 ), rm.getLong( 2 ) };
		
		// and get goal values out of the configuration, which are encoded slightly
		// different. Additionally, if biggoal is True, they will be offset by 1B
		final RegexMatcher rm2 = new RegexMatcher( "X=(\\d+), Y=(\\d+)" );
		rm2.match( s[2] );
		final long[] G = new long[] { rm2.getLong( 1 ) + (biggoal ? 10000000000000l : 0), rm2.getLong( 2 ) + (biggoal ? 10000000000000l : 0) };
		
		// return the claw machine with this configuration
		return new ClawMachine( A, B, G );
	}
}
