package challenges.day17;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that helps reverse engineer the IntCodeMachine to make it produce the
 * specified target output
 */
public class ReverseEngineerICM {
	/** The IntCodeMachine to reverse engineer */
	protected final IntCodeMachine ICM;
	
	/**
	 * Sets up a new ReverseEngineer instance for the given IntCodeMachine
	 * program
	 * 
	 * @param icm The IntCodeMachine to reverse engineer
	 */
	public ReverseEngineerICM( final IntCodeMachine icm ) {
		this.ICM = icm;
	}
	
	/**
	 * Simulates the IntCodeMachine with varying inputs to reverse engineer the
	 * input for registry A that will produce the desired target output
	 * 
	 * @param output The required output as a comma separated string of integers
	 * @return The input value for memory registry A that will make the ICM
	 *   program produce the desired output
	 */
	public long findOutput( final String output ) {
		// convert desired output into array of integers
		final String[] s = output.split( "," );
		final int[] target = new int[ s.length ];
		for( int i = 0; i < s.length; i++ ) target[i] = Integer.parseInt( s[i] );

		// then find all input values for which the program produces this output
		// and return the lowest thereof
		final List<Long> solutions = new ArrayList<>( );
		findOutput( solutions, target, target.length - 1, 0 );
		solutions.sort( Long::compareTo );
		return solutions.get( 0 );
	}
	
	/**
	 * Recursively try and fix 3-bit parts of the input value that correspond to
	 * each of the output digits of the program until all digits are matched.
	 * Every time all digits are matched, the input value is added to a list of
	 * possible solutions for input variable A. Then, once all recursions have
	 * finished all solution values are returned.
	 * 
	 * Note that this algorithm has a worst case complexity of O( 8^n ). In
	 * practice, however, only a few 3-bit pairs actually result in the target
	 * output for its corresponding digit and hence only very few values actually
	 * lead to recursion.
	 * 
	 * @param solutions The set of solutions found so far
	 * @param target The array of target output values
	 * @param index The index of the digit we are currently trying to match to
	 *   the desired output by finding a 3-bit configuration for the bits in the
	 *   input value that correspond to changing the output digit at this index.
	 * @param input The current long value we are feeding into memory registry A
	 *   as input to the program, which is a sum of all 3-bit configurations for
	 *   the digits considered so far.
	 */
	protected void findOutput( final List<Long> solutions, final int[] target, final int index, final long input ) {
		// no more digits to match? we found a solution!
		if( index == -1 ) {
			solutions.add( input );
			return;
		}
		
		// not yet done. Try all 3-bit configurations for the 3 bits of the memory
		// A registry input that correspond to the output digit at the current
		// index we are matching		
		for( long i = 7; i >= (index != target.length - 1 ? 0 : 1); i-- ) {		
			// get long value of the bit configuration shifted to the bits that
			// correspond to the output digit
			final long bits = i << (index * 3);
			
			// then reset the memory, feed this value into registry A and run the ICM
			ICM.setMemory( 0, input + bits );
			ICM.setMemory( 1, 0l );
			ICM.setMemory( 2, 0l );
			final int validx = ICM.run( ).get( index ).intValue( );

			// does this input result in a matching of the digit we are currently
			// trying to match?
			if( validx != target[index] ) continue;
			
			// yes! try the next digit
			findOutput( solutions, target, index - 1, input + bits );		
		}
	}

}
