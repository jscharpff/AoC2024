package challenges.day02;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Class that holds the safety measurement levels for a Red Nosed Reindeer
 * nuclear reactor and can check if the report is safe  
 */
public class RNRReport {
	/** The measured levels */
	final protected List<Integer> levels;
	
	/**
	 * Creates a new report from a list of measurement values
	 * 
	 * @param values The report measurement values
	 */
	private RNRReport( final List<Integer> values ) {
		levels = new ArrayList<>( values );
	}

	/**
	 * Checks if a report is safe, that is, it complies with the safety rules
	 * of measured levels:
	 * 1. Increases or decreases between values must be between 1 and 3
	 * 2. The values must be either all increasing or decreasing 
	 * 
	 * @return True iff the report is safe
	 */
	public boolean isSafe( ) {
		// build array of differentials between levels
		final int[] diffs = new int[ size( ) - 1 ];
		for( int i = 0; i < size( ) - 1; i++ ) diffs[i] = levels.get( i + 1 ) - levels.get( i );
		
		// check all differentials against the safety rules
		for( final int d : diffs ) {
			// difference must be between 1 and 3
			if( Math.abs( d ) < 1 || Math.abs( d ) > 3 ) return false;
			
			// the series must be either increasing or decreasing
			if( diffs[0] < 0 && d > 0 ) return false;
			if( diffs[0] > 0 && d < 0 ) return false;
		}

		// all safety checks passed, this is a valid report
		return true;
	}
	
	/**
	 * Creates a new report without the value at the specified index
	 * 
	 * @param idx The report value to remove
	 * @return The new report without the value at the given index
	 */
	public RNRReport remove( final int idx ) {
		final RNRReport r = new RNRReport( this.levels );
		r.levels.remove( idx );
		return r;
	}
	
	/** @return The number of measurements in this report */
	public int size( ) { return levels.size( ); }
	
	/**
	 * Creates a report from a string description of measured levels
	 * 
	 * @param values The measurements of the report, separated by spaces
	 * @return The report
	 */
	public static RNRReport fromString( final String values ) {
		final List<Integer> r = new ArrayList<>( );
		Stream.of( values.split( " " ) ).forEach( s -> r.add( Integer.parseInt( s ) ) );
		return new RNRReport( r );
	}
}
