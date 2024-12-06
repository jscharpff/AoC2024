package challenges.day06;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import aocutil.geometry.Coord2D;
import aocutil.geometry.Direction;
import aocutil.grid.CoordGrid;

/**
 * Models the North Pole suit prototype lab and the guard patrolling it
 */
public class LabPatrol {
	/** The layout of the lab and its obstacles */
	protected CoordGrid<Character> map;

	/** The starting position of the guard patrolling the lab */
	protected Coord2D guardstart;

	/**
	 * Creates a new lab patrol simulation from the given lab layout
	 * 
	 * @param maplayout List of strings that describe the rows of the lab layout
	 *   such that '#' marks an obstacle, '.' a free space and '^' the initial
	 *   position of the guard 
	 */
	public LabPatrol( final List<String> maplayout ) {
		// create grid from the description
		map = CoordGrid.fromCharGrid( maplayout, '.' );
		
		// get starting position of guard and remove it from the grid
		for( final Coord2D g : map.find( '^' ) ) {
			map.unset( g );
			guardstart = g;
		}
	}
	
	/**
	 * Counts the number of grid spaces the guard traverses during his patrol
	 * 
	 * @return The number of visited grid squares
	 */
	public long countCoverage( ) {
		// determine and count positions the guard visits
		return getVisitable( 'X' ).count( 'X' );
	}
	
	/**
	 * Simulates the patrol route of the guard from its initial starting
	 * position through the lab with its obstacles. Marks all visited positions
	 * on a copy of the grid layout.
	 * 
	 * @param visitChar The char used to mark a visited position
	 * @return The copy of the lab layout with visited positions marked
	 */
	private CoordGrid<Character> getVisitable( final char visitChar ) {
		// copy the grid and setup the starting position and direction of the guard
		final CoordGrid<Character> M = map.copy( );
		Coord2D g = guardstart;
		Direction gd = Direction.North;
		
		// then simulate the guard moving until it exits the lab
		while( M.contains( g ) ) {
			// mark position as visited
			M.set( g, visitChar );

			// find the next move, possibly requiring one or more turns if the next
			// move would end up in an obstacle
			Coord2D next = g.move( gd, 1 );
			while( M.get( next ) == '#' ) {
				gd = gd.turn( 1 );
				next = g.move( gd, 1 );
			}
			
			// next position is clear, move to it
			g = next;
		}
		
		// guard exited the lab grounds
		return M;
	}
	
	/**
	 * Checks if the patrol route of the guard ends in a loop
	 * 
	 * @return True if the guard will end up in a patrol loop when traversing
	 *   the current lab layout
	 */
	private boolean hasLoop( ) {
		// move the guard but keep track of visited positions
		final Set<String> V = new HashSet<>( );
		
		// start simulation again, continue moving until we either exit the lab
		// (no loop) or end up at a position + direction we've seen before (loop)
		Coord2D g = guardstart;
		Direction gd = Direction.North;
		while( map.contains( g ) ) {
			// check if we already seen this position and direction, otherwise add it
			// to the history
			final String key = g.toString( ) + "|" + gd.toString( );
			if( V.contains( key ) ) return true;
			V.add( key );

			// not visited before, continue and determine next move
			Coord2D next = g.move( gd, 1 );
			while( map.get( next ) == '#' ) {
				gd = gd.turn( 1 );
				next = g.move( gd, 1 );
			}			
			g = next;
		}
		
		// guard exited the lab grounds, the route has no loops
		return false;
	}
	
	/**
	 * Counts the number of unique layout configurations that introduce a loop in
	 * the guards patrol route by adding a single obstacle to the original layout
	 * 
	 * @return The number of positions where adding an obstacle will introduce a
	 *   loop in the guard's patrol route
	 */
	public long countLoops( ) {
		// find positions that guard visits first, these are potential positions to
		// create loops. But exclude the starting position of the guard
		final CoordGrid<Character> V = getVisitable( 'X' );
		V.unset( guardstart );
		
		// then simply try adding an obstacle to each of these positions and see if
		// this will introduce a loop
		long looping = 0;
		for( final Coord2D b : V.find( 'X' ) ) {
			// block the position, try if this creates a loop and unblock again
			map.set( b, '#' );
			if( hasLoop( ) ) looping++;
			map.unset( b );
		}
		
		// return count of looping obstacle positions
		return looping;
	}
}
