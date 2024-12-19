package challenges.day19;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that offers an algorithm to find the unique number of configurations
 * that can be made from its set of available patterns for every arrangement of
 * towels that is provided to it.
 * 
 * The arranger uses an internal cache to store configuration counts for towel
 * arrangements that have been explored. That is, if it needs to count the
 * number of unique ways to produce a configuration of patterns that will match
 * the given a (partial) arrangement of towels that it computed before, this
 * count will be reproduced from memory instead of computed anew.
 */
public class OnsenTowelArranger {
	/** The list of towel patterns available */
	protected final List<String> patterns;
	
	/** The memoisation table that holds the number of unique combinations of
	 * patterns that can form the arrangement of towels */
	final Map<String, Long> M;
	
	/**
	 * Creates a new Onsen towels arranger that helps in finding arrangements of
	 * towels from available patterns
	 * 
	 * @param input The list of patterns available to this arranger
	 */
	public OnsenTowelArranger( final String input ) {
		patterns = new ArrayList<>( );
		for( final String p : input.split( ", " ) ) patterns.add( p );
		M =  new HashMap<>( );
	}
	
	/**
	 * Counts the number of unique configurations of patterns that will match the
	 * specified input towel arrangement. It does so by trying to match a pattern
	 * to the start of the string and, if successful, recursing on the remaining  
	 * part of the arrangement.
	 * 
	 * The algorithm uses the memoisation table to reuses previous results if a
	 * (partial) arrangement was solved before.
	 * 
	 * @param arr The (partial) arrangement to match with our available patterns
	 * @return The count of unique pattern configurations that can produce this
	 *   arrangement, 0 if no such configuration is possible.
	 */
	protected long countArrangements( final String arr ) {
		// no more arrangement to match? return 1 as we have a complete arrangement
		if( arr.length( ) == 0 ) return 1;
		
		// already seen this (partial) arrangement before? reuse count
		if( M.containsKey( arr ) ) return M.get( arr );
		
		// try matching each of the towel patterns and, if it matches, recurse on
		// the remaining parts of the desired arrangement
		long count = 0;			
		for( final String p : patterns ) {
			// does the arrangement start with this pattern?
			if( !arr.startsWith( p ) ) continue;
			count += countArrangements( arr.substring( p.length( ) ) );
		}

		// memoise this arrangement for future searches and return the count
		M.put( arr, count );
		return count;
	}
}
