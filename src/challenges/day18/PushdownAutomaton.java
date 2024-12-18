package challenges.day18;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import aocutil.geometry.Coord2D;

/**
 * Class that represents a pushdown automaton with a NxN memory that is not too
 * healthy...
 */
public class PushdownAutomaton {
	/** The current state of the memory such that true equals a corrupted memory
	 * block */
	protected final boolean[][] corrupt;
	
	/**
	 * Creates a new Pushdown Automaton with the specified memory size
	 * 
	 * @param memsize The size N of the NxN memory layout
	 */
	public PushdownAutomaton( final int memsize ) {
		corrupt = new boolean[memsize][memsize];
	}
	
	/**
	 * Corrupts a single memory segment specified by the  coordinates
	 * 
	 * @param segment The coordinate of the segment to corrupt
	 */
	public void corrupt( final Coord2D segment ) {
		corrupt[segment.x][segment.y] = true;
	}

	/**
	 * Finds the shortest path through the memory bank without touching any of
	 * the corrupt memory segments
	 * 
	 * @param start The starting memory segment
	 * @param end The memory segment we want to reach
	 * @return The length of the shortest path from start to end without moving
	 *   over corrupted memory segments
	 */
	public long findShortestPath( final Coord2D start, final Coord2D end ) {
		// use a breadth-first search to find the shortest path from start to end
		// through the memory without moving over corrupt memory segments
		final Stack<Coord2D> explore = new Stack<>( );
		final Set<Coord2D> V = new HashSet<>( );
		explore.push( start );
		long length = -1;
		
		while( !explore.isEmpty( ) ) {
			final Set<Coord2D> next = new HashSet<>( );
			length++;
			
			while( !explore.isEmpty( ) ) {
				// next segment to explore, check if we are at the goal
				final Coord2D c = explore.pop( );
				if( c.equals( end ) ) return length;
				
				// nope, add this segment to the visited list and explore its neighbours
				// if they are valid memory segments
				V.add( c );
				for( final Coord2D n : c.getAdjacent( false ) ) {
					// already seen before?
					if( V.contains( n ) ) continue;
					
					// within the memory grid?
					if( n.x < 0 || n.x >= size( ) || n.y < 0 || n.y >= size( ) ) continue;
					
					// not corrupted?
					if( corrupt[n.x][n.y] ) continue;
					
					// yes! add to the set to explore in the next iteration of BFS
					next.add( n );
				}
			}
			
			// this round is exhausted, add all coordinates to explore in the next round
			explore.addAll( next );
		}
		
		// the end was not visited so it is not possible to reach the goal
		return -1;
	}
	
	/** @return The size N of the NxN memory layout */
	public int size( ) { return corrupt.length; }
}
