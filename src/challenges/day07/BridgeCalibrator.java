package challenges.day07;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to find equations that belong to the rope bridge calibration process
 */
public class BridgeCalibrator {
	/** The value we are looking for in the equation currently being tested */
	private static long value;
	
	/** True if the concatenation operator || is also included in the tests */
	private static boolean concat;
	
	/**
	 * Tests whether a given equation without operators is actually used in the
	 * calibration of the rope bridge. That is, its left hand value can be
	 * attained by summing, multiplying or concatenating the right hand values in
	 * any way
	 * 
	 * @param equation The equation that is missing operator
	 * @param useconcat True iff we include the concatenation operator in the
	 *   search
	 * @return The left hand value of the equation if it is part of the
	 *   calibration, otherwise 0 is returned
	 */
	public static long testEquation( final String equation, final boolean useconcat ) {
		// do we allow the use of the concatenation operator in the equation?
		concat = useconcat;

		// first split goal value from equation terms
		final String[] eq = equation.split( ": " );
		value = Long.parseLong( eq[0] );
		
		// then parse equation terms
		final List<Long> terms = new ArrayList<>( );
		for( final String t : eq[1].split( " " ) ) terms.add( Long.parseLong( t ) );
		
		// and start recursively testing if this could be a bridge calibration equation
		return testEq( terms.remove( 0 ), terms ) ? value : 0;
	}
	
	/**
	 * Recursively tries possible operators on the terms until either the terms
	 * list is exhausted or the goal value has been overshot
	 * 
	 * @param curr The current value after processing the terms so far
	 * @param terms The remaining terms
	 * @return True if any combination of operators will make the terms attain
	 *   exactly the goal value, false otherwise
	 */
	private static boolean testEq( final long curr, final List<Long> terms ) {
		// overshot the value?
		if( curr > value ) return false;
		
		// no more terms? test if value has been reached
		if( terms.isEmpty( ) ) return value == curr;
		
		// terms left, try all of the operators on the next term through recursion
		boolean test = false;
		final long next = terms.remove( 0 );
		test |= testEq( curr * next, new ArrayList<>( terms ) );
		if( !test ) test |= testEq( curr + next, new ArrayList<>( terms ) );
		if( !test && concat ) test |= testEq( Long.parseLong( "" + curr + next ), new ArrayList<>( terms ) );
		
		// return the value after testing any of the operators
		return test;
	}


}
