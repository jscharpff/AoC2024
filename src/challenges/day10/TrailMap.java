package challenges.day10;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import aocutil.geometry.Coord2D;
import aocutil.grid.CoordGrid;

/**
 * Class to represent a height map of a hiking area and find trails with even,
 * gradually upwards slopes
 */
public class TrailMap {
	/** The height map of the hiking area */
	protected final CoordGrid<Integer> map;
	
	/**
	 * Creates the trail map from a list of strings that represent the heights
	 * of hills in the area
	 * 
	 * @param input Grid that describes the elevation in the hiking area, one 
	 *   string per row of the grid
	 */
	public TrailMap( final List<String> input ) {
		map = CoordGrid.fromDigitGrid( input );
	}

	/**
	 * Counts either the number of unique trail ends or unique trails that can be
	 * reached from all of the trail heads in the area. That is, the grid 
	 * positions with the lowest elevation
	 * 
	 * @param distinctTrail True to return count of distinct trails or false for
	 *   a count of distinct trail heads only
	 * @return The count of distinct trails or trail heads, depending on the flag
	 */
	public long countTrails( final boolean distinctTrail ) {
		// find and sum counts from every possible trail head
		return map.find( 0 ).stream( ).mapToLong( start -> countTrails( start, distinctTrail ) ).sum( );
	}
	
	/**
	 * Finds all unique, complete trails that start from the given trail head and
	 * returns the requested count
	 * 
	 * @param start The trail head to start trail search from
	 * @param distinctTrail True to count distinct trails, false to count distinct
	 *   trail end
	 * @return The count of distinct trails or trail ends
	 */
	private long countTrails( final Coord2D start, final boolean distinctTrail ) {
		// do a BFS search from the given trail start and traverse all paths that 
		// are monotonically increasing in elevation
		
		// setup stack of next search nodes, initialise with trail start and no elevation
		Stack<Coord2D> explore = new Stack<>( );
		explore.push( start );
		int level = 0;

		// initialise set to hold all trail ends and count of paths
		final Set<Coord2D> ends = new HashSet<>( );
		int count = 0;
		
		// keep on exploring until no more new nodes to explore
		while( !explore.isEmpty( ) ) {
			final Stack<Coord2D> next = new Stack<>( );
			level++;
			
			// try exploring each node in this BFS round
			while( !explore.isEmpty( ) && level < 10 ) {
				// final all neighbours of this node
				final Coord2D c = explore.pop( );
				for( final Coord2D n : map.getNeighbours( c, false ) ) {
					// consider only neighbouring slopes that increase by one level
					if( map.get( n ) != level ) continue;
					
					// have we encountered a trail end?
					if( map.get( n  ) == 9 ) {
						// yes, count it as a unique way to reach it (e.g., a unique path)
						// and add the end to the set of reachable ends
						count++;
						ends.add( n );
					} else {
						// not a trail end yet, add to the set of nodes to explore in the
						// next round
						next.push( n );
					}
				}
			}

			// swap stacks of nodes explore in next round of BFS
			explore = next;
		}
		
		// return count of distinct ends or trails depending on flag
		return distinctTrail ? count : ends.size( );
	}
}
