package challenges.day12;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import aocutil.geometry.Coord2D;
import aocutil.geometry.Direction;
import aocutil.grid.CoordGrid;

/**
 * Models a garden of plants and helps computing the cost of fencing plant
 * regions
 */
public class GardenLayout {
	/** The garden plot layout */
	protected final CoordGrid<Character> plot;
	
	/**
	 * Reconstructs a garden from a list of strings that describe the plant
	 * layout
	 * 
	 * @param input List of strings that describe the plant layouts in the
	 *   garden. One string is a single row of the garden
	 */
	public GardenLayout( final List<String> input ) {
		this.plot = CoordGrid.fromCharGrid( input, '.' );
	}
	
	/**
	 * Computes the cost of fencing all regions of plants in the garden
	 * 
	 * @param sidecount False to use number of perimeter segments to compute the
	 *   fencing costs, True to use sides (consecutive perimeter segments)
	 * @return The cost of fencing this region
	 */
	public long fenceCost( final boolean sidecount ) {
		// keep track of costs so far and visited plants
		long cost = 0;
		boolean[][] V = new boolean[plot.window( ).getWidth( )][plot.window( ).getHeight( )];
		
		// sum unvisited regions of the garden
		for( final Coord2D c : plot ) {
			if( V[c.x][c.y] ) continue;
			cost += fenceCost( V, c, sidecount );
		}
		
		// and return total fence costs
		return cost;
	}

	/**
	 * Breadth-first search algorithm to determine the perimeter of the garden
	 * region starting from the given coordinate and then computes the cost of
	 * fencing the region based upon area and perimeter.
	 * 
	 * @param V The matrix of garden plots visited in this or earlier iterations
	 *   of the algorithm
	 * @param start The coordinate to start the region search from
	 * @param sidecount False to use the number of perimeter segments for the
	 *   fence cost computation, True to use number of sides (connected perimeter
	 *   segments)
	 * @return The cost of fencing the region of plants that contains the
	 *   starting coordinate
	 */
	private long fenceCost( final boolean[][] V, final Coord2D start, final boolean sidecount ) {
		// get the plant type of this region
		final char plant = plot.get( start );		
	
		// keep track of area, perimeter section types and count
		long area = 0;
		long perim = 0;
		final Map<Coord2D, Integer> P = new HashMap<>( );
		
		// do a BFS search from the starting coordinate to identify all plants in
		// this region and the perimeter of the region
		Stack<Coord2D> explore = new Stack<>( );
		explore.push( start );		
		while( !explore.isEmpty( ) ) {
			final Stack<Coord2D> next = new Stack<>( );

			while( !explore.isEmpty( ) ) {
				// explore neighbouring plant
				final Coord2D c = explore.pop( );
				
				// not in garden or already seen? skip
				if( !plot.contains( c ) || V[c.x][c.y] ) continue;
				
				// found one more plant, mark it visited
				area++;
				V[c.x][c.y] = true;
				
				// see whether the neighbouring plant is of the same type and therefore
				// part of the same region or, if not, what type of perimeter this is
				for( final Direction d : Direction.values( ) ) {
					// get neighbouring plant
					final Coord2D n = c.move( d, 1 );
					
					// is this either of grid or a different plant, then we have a
					// perimeter segment here
					if( !plot.contains( n ) || plot.get( n ) != plant ) {
						// count 1 segment and store the type of perimeter in the map
						P.put( c, P.getOrDefault( c, 0 ) | (int)Math.pow( 2, d.ordinal( ) ) );
						perim++;
					}	else {
						// nope, part of the same region. Explore in next round of BFS
						next.push( n );
					}
				}
			}
			
			// swap stacks to explore and start a new round
			explore = next;
		}		
		
		// if we simply compute the cost from the number of perimeter segments we
		// are done now, otherwise count sides
		return !sidecount ? area * perim : area * countSides( P );
	}
		
	/**
	 * Compute number of sides of the region. That is, the number of unique,
	 * consecutive segments of perimeters around the region. We do this by
	 * picking a random perimeter segment, traversing the side and removing the
	 * segments from the perimeter type map we built earlier
	 * 
	 * @param P The perimeter map that was constructed while reconstructing the
	 *   region
	 * @return The number of unique sides in the region
	 */
	private long countSides( final Map<Coord2D, Integer> P ) {		
		// keep on finding sides until all segments have been used in the counting
		long sides = 0;
		while( !P.isEmpty( ) ) {
			// pick a random segment to explore next and expand along all of its
			// sides
			final Coord2D c = P.keySet( ).iterator( ).next( );
			for( final Direction d : Direction.values( ) ) {
				// is there a perimeter in this direction?
				final int dp = (int)Math.pow( 2, d.ordinal( ) );
				if( (P.get( c ) & dp) == 0 ) continue;
				
				// yes, then we must have an unexplored side. Count it and remove all
				// of its segments from the perimeter map
				sides++;
				
				// then move along the wall to its starting point and remove it from
				// the perimeter map
				for( final int i : new int[] { 1, 3 } ) {
					// move along wall in 90 and 270 degree directions to find complete
					// side and remove these segments from the map
					final Direction dd = d.turn( i );
					Coord2D n = c;
					while( n.equals( c ) || P.containsKey( n ) && (P.get( n ) & dp) > 0 ) {
						P.put( n, P.get( n ) & ~dp );					
						n = n.move( dd, 1 );
					}
				}
			}
			
			// no more perimeter segments at the coordinate, remove it from the map
			// so that we won't check this coordinate again
			if( P.get( c ) == 0 ) P.remove( c );
		}

		// found all sides, return the count
		return sides;
	}
}
