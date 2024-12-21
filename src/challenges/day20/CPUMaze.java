package challenges.day20;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import aocutil.geometry.Coord2D;
import aocutil.grid.CoordGrid;

/**
 * Models a CPU race track on which programs can race. To make things more
 * interesting, the programs can cheat once in the race by moving through walls
 * within the CPU.
 */
public class CPUMaze {
	/** The maze layout */
	protected final CoordGrid<Character> maze;
	
	/** The race starting coordinate */
	protected final Coord2D start;

	/** The race finish */
	protected final Coord2D end;
	
	/**
	 * Reconstructs the CPU Maze from a list of strings that describes the layout
	 * of the CPU Maze.
	 * 
	 * @param layout The maze layout as a list of strings such that each string
	 *   models a row of the maze, describing the walls, tiles, start and end
	 */
	public CPUMaze( final List<String> layout ) {
		maze = CoordGrid.fromCharGrid( layout, '.' );
		
		// find start and end positions and remove them from the layout
		Coord2D s = null, e = null;
		for( final Coord2D c : maze.find( 'S' ) ) s = c;
		for( final Coord2D c : maze.find( 'E' ) ) e = c;
		maze.unset( s );
		maze.unset( e );
		this.start = s;
		this.end = e;
	}
	
	/**
	 * Finds the number of unique cheats that can be used by programs to reach
	 * the race finish faster
	 * 
	 * @param maxcheatdist The maximum distance that programs may cheat (in one
	 *   go and only once)
	 * @param minsaving The minimal saving required by the cheat before counting
	 *   the cheat
	 * @return The number of unique cheats of the specified max length possible
	 *   within the CPU maze that realise at least the required saving compared
	 *   to the shortest path from start to end without cheating
	 */
	public long findCheatCount( final int maxcheatdist, final int minsaving ) {
		// first build distance matrix that for every coordinate describes the
		// length of the shortest path to the start and the finish coordinates
		final Map<Coord2D, Integer> S = buildDistanceMatrix( start );
		final Map<Coord2D, Integer> D = buildDistanceMatrix( end );
		final Set<Cheat> C = new HashSet<>( );
		
		// store shortest path length for computation of cheat saving
		final int shortest = D.get( start );

		// then for every combination of maze positions for which a cheat may help
		// shorten the path length, see if we can get there by cheating and then
		// keep those that save us the minimal required time
		for( final Coord2D s : D.keySet( ) ) {
			for( final Coord2D e : D.keySet( ) ) {
				// valid to cheat this route?
				final int chdist = s.getManhattanDistance( e );
				if( chdist > maxcheatdist ) continue;
				
				// does it actually help us?
				final int stepstogoal = D.get( e );
				if( D.get( s ) < stepstogoal ) continue;				
				
				// yes, does it help enough?
				final Cheat ch = new Cheat( s, e );
				final int total = S.get( s ) + stepstogoal + chdist;
				if( shortest - total < minsaving ) continue;
				
				// yes, add this cheat to set of available ones
				C.add( ch );
			}
		}
		
		// return sum of cheat counts
		return C.size( );
	}

	/**
	 * Build a distance matrix from a given coordinate that holds the shortest
	 * distance to reach it from every other tile in the maze
	 * 
	 * @param to The goal coordinate to build distance matrix to
	 * @return A distance matrix that holds an entry for every tile that can be
	 *   reached from the given start coordinate the distance to it
	 */
	protected Map<Coord2D, Integer> buildDistanceMatrix( final Coord2D to ) {
		// use a simple BFS search that starts from the given coordinate and moves
		// into every reachable tile of the maze while recording the travel
		// distance to it
		final Map<Coord2D, Integer> D = new HashMap<>( );
		final Stack<Coord2D> explore = new Stack<>( );

		// start with the given goal coordinate and distance 0, then in every round
		// find its neighbours and record them at distance + 1
		explore.push( to );
		int dist = 0;
		while( !explore.isEmpty( ) ) {
			final Set<Coord2D> next = new HashSet<>( );
			
			// explore all current distance coordinates, record distance and add
			// its neighbours to the set to explore in next round
			while( !explore.isEmpty( ) ) {
				final Coord2D c = explore.pop( );
				if( D.containsKey( c ) ) continue;
				D.put( c, dist );
				next.addAll( maze.getNeighbours( c, false, n -> maze.get( n ) != '#' ) );
			}
			
			// next round, increase distance and add all next candidates
			explore.addAll( next );
			dist++;
		}
		
		// return completed distance matrix
		return D;
	}
	
	/** Class that uniquely describes a single cheat */
	private class Cheat {
		/** The starting coordinate of the cheat */
		protected final Coord2D from;
		
		/** The position we end up in once the cheat is done */
		protected final Coord2D to;
		
		/** Cached hash code that uniquely identifies this cheat */
		private final int hashCode;
		
		/**
		 * Creates a new cheat from start to end position
		 * 
		 * @param start The position this cheat starts from
		 * @param end The position this cheat ends in
		 */
		public Cheat( final Coord2D start, final Coord2D end ) {
			this.from = start;
			this.to = end;
			
			// precompute and cache its hash code
			hashCode = (from.toString( ) + to.toString( )).hashCode( );
		}
		
		/** @return The Manhattan distance from start to end of the cheat */
		public int distance( ) {
			return from.getManhattanDistance( to );
		}
		
		/**
		 * Tests this cheat to another object for equality
		 * 
		 * @param obj The object to test equality against
		 * @return True iff the other object is a non-null Cheat and has the same
		 *   start and end coordinates
		 */
		@Override
		public boolean equals( Object obj ) {
			if( obj == null || !(obj instanceof Cheat) ) return false;
			final Cheat c = (Cheat)obj;
			return from.equals( c.from ) && to.equals( c.to );
		}
		
		/**
		 * @return The string description of the cheat, which is given by its
		 * coordinates
		 */
		@Override
		public String toString( ) {
			return from + "->" + to + ": " + distance( );
		}
		
		/** @return The hash code that unique identifies this cheat */
		@Override
		public int hashCode( ) {
			return hashCode;
		}
	}
}
