package challenges.day05;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that can test and modify the order of pages to print of the sleigh
 * safety manual against a set of precedence rules.
 */
public class ManualPrinter {
	/** The set of precedence rules of page X before page Y */
	protected List<PageRule> rules;
	
	/**
	 * Creates a new manual printer from the given rule set as strings
	 * 
	 * @param ruleset The strings describing the page precedence rules
	 */
	public ManualPrinter( final String ruleset ) {
		rules = new ArrayList<>( );
		for( final String r : ruleset.split( ";" ) )
			rules.add( new PageRule( r ) );
	}
	
	/**
	 * Converts a string of page numbers into an array list of integers
	 * @param ordering The pages in their current order
	 * @return An array list with the same pages and order
	 */
	private List<Integer> pagesFromString( final String ordering ) {
		final List<Integer> pages = new ArrayList<>( );
		for( final String p : ordering.split( "," ) ) pages.add( Integer.parseInt( p ) );
		return pages;
	}
	
	/**
	 * Checks the given page ordering against the rule set and returns the middle
	 * page if the order is valid.
	 * 
	 * @param ordering The order in which pages are currently
	 * @return If the series ordering is valid according to the rules the middle
	 *   page number is returned, otherwise a 0 is returned
	 */
	public long getMiddleIfValid( final String ordering ) {
		// convert page list to array of integers
		final List<Integer> pages = pagesFromString( ordering );
		
		// check every rule if satisfied
		for( final PageRule pr : rules ) {
			// get index of both the numbers specified by the rule in the input
			final int x = pages.indexOf( pr.X );
			final int y = pages.indexOf( pr.Y );
			
			// only check a rule if both page numbers are present, then make sure
			// that the rule X|Y is satisfied. I.e., X must be before Y
			if( x != -1 && y != -1 && x > y ) return 0;
		}
		
		// all satisfied, return mid of list
		return pages.get( pages.size( ) / 2 );
	}
	
	/**
	 * Will reorder the input page list until all rules are satisfied
	 * 
	 * @param ordering The initial ordering of pages
	 * @return The middle entry of the list, after reordering
	 */
	public long reorder( final String ordering ) {
		// convert page numbers string into list of integers
		final List<Integer> pages = pagesFromString( ordering );
		
		// keep on performing shuffles until no more change occur. If no more
		// changes are applied, all rules must be satisfied
		boolean changed = true;
		while( changed ) {
			// assume no changes in this iteration
			changed = false;
			
			// then try and apply all rules again
			for( final PageRule pr : rules ) {
				final int x = pages.indexOf( pr.X );
				final int y = pages.indexOf( pr.Y );
				
				// check if this rule satisfied, then skip it
				if( x == -1 || y == -1 || x < y ) continue;

				// nope, we need to reorder pages to satisfy the rule
				changed = true;
				pages.add( x, pages.remove( y ) );
			}
		}
		
		// no more changes means all rules satisfied, return mid of list
		return pages.get( pages.size( ) / 2 );		
	}

	/**
	 * Simple container for a X|Y rule in which page X must come before page Y
	 */
	protected static class PageRule {
		/** The page X that must come before Y */
		final int X;
		
		/** The page Y that must come after X */
		final int Y;
		
		/**
		 * Creates a new page rule from a string description
		 * 
		 * @param in The rule as X|Y string
		 */
		protected PageRule( final String in ) {
			final String[] s = in.split( "\\|" );
			X = Integer.parseInt( s[0] );
			Y = Integer.parseInt( s[1] );
		}
		
		/**
		 * @return The page rule as X|Y string
		 */
		@Override
			public String toString( ) {
				return X + "|" + Y;
			}
	}
}
