package challenges.day16;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aocutil.geometry.Coord2D;
import aocutil.geometry.Direction;
import aocutil.grid.CoordGrid;

/**
 * Class that models a game of Reindeer Maze and offers methods to both solve
 * finding the optimal paths through it as well as finding the best positions
 * to watch the reindeer races
 */
public class ReindeerMaze {
	/** The maze layout */
	protected final CoordGrid<Character> maze;
	
	/** The starting position of the reindeer */
	protected final ReindeerState reindeer;
	
	/** The position of the finish line */
	protected final Coord2D finish;
	
	/**
	 * Creates a new Reindeer Maze game from a grid that visualises the initial
	 * state of the maze
	 * 
	 * @param input A list of strings that describes the maze layout of walls,
	 *   the reindeer starting position and the finish line. The list contains a
	 *   single line per row of the maze
	 */
	public ReindeerMaze( final List<String> input ) {
		// read the maze layout from the input
		maze = CoordGrid.fromCharGrid( input, '.' );

		// extract the starting position of the reindeer and create a new initial
		// state of it
		Coord2D r = null;
		for( final Coord2D c : maze.find( 'S' ) ) r = c;
		maze.unset( r );
		reindeer = new ReindeerState( r, Direction.East, 0 );

		// extract the position of the finish line
		Coord2D f = null;
		for( final Coord2D c : maze.find( 'E' ) ) f = c;
		maze.unset( f );
		finish = f;
	}
	
	/**
	 * Finds the path to the goal that has the lowest score when traversing it
	 * through the maze
	 * 
	 * @return The score of the best path through the maze, i.e., the one with
	 *   the lowest score.
	 */
	public long findBestPath( ) {
		// finds all best paths and return the value of any of them
		// note that this could be optimised by terminating the solve algorithm
		// when we find the first path that reaches the finish
		return solve( ).get( 0 ).value( );
	}
	
	/**
	 * Finds the count of unique tiles that are part of at least one of the
	 * best solution paths
	 * 
	 * @return The count of unique tiles that occur in one or more optimal
	 *   solution paths
	 */
	public long findBestSeats( ) {
		// first find all possible optimal paths
		final List<RSPath> solutions = solve( );
		
		// then use a set to keep only unique tiles of each path
		final Set<Coord2D> tiles = new HashSet<>( );
		for( final RSPath p : solutions ) tiles.addAll( p.path );
		
		// and return the set size, i.e., the unique tiles part of at least one
		// solution path
		return tiles.size( );
	}
	
	/**
	 * Actually performs the solving part, finding all lowest score paths that
	 * reach the finish line.
	 * 
	 * @return The list of unique paths through the maze that realise the lowest
	 *   possible score
	 */
	protected List<RSPath> solve( ) {
		// keep a queue of paths to explore next and a separate map for convenient
		// value look up of previously visited states
		final LinkedList<RSPath> Q = new LinkedList<>( );
		final Map<String, ReindeerState> V = new HashMap<>( );
		
		// add initial path to the queue to start exploring from
		Q.add( new RSPath( reindeer ) );

		// keep track of all found solutions and the lowest solution score so far
		final List<RSPath> solutions = new ArrayList<>( );
		long shortest = Long.MAX_VALUE;

		// keep extending paths until no more improvement can be made
		while( Q.size( ) > 0 ) {
			// try to extend the lowest value path
			final RSPath r = Q.poll( );

			// path too long? abandon it! We only want shortest paths to the goal
			if( r.value( ) > shortest ) continue;
			
			// are we there yet? 
			if( r.head.pos.equals( finish ) ) {
				// yes, store it and stop exploring this one further 
				solutions.add( r );
				shortest = r.value( );
				continue;
			};
			
			// get possible next moves from the path's head and extend path if possible
			for( final ReindeerState next : r.head.getNextStates( ) ) {
				// get the current score we have for this state
				final ReindeerState inQ = V.get( next.stateKey( ) );
				
				// if we have seen this one before with a lower score, do not extend 
				if( inQ != null && (inQ.value < next.value) ) continue;

				// nope, extend the path and add it to the queue for further exploration
				Q.offer( new RSPath( r, next ) );
				
				// also update best value we've seen for this state
				V.put( next.stateKey( ), next );
			}
			
			// sort the list on value so that it acts as a priority queue
			Q.sort( RSPath::compareTo );
		}
		
		// return list of solutions. Note that we do not have to filter solutions
		// here as the list acts as a priority queue and hence the first solution
		// must be the lowest possible cost. Thereafter all longer solutions are
		// discarded during the search
		return solutions;
	}
	
	/**
	 * @return The string that visualises the reindeer maze
	 */
	@Override
	public String toString( ) {
		final StringBuilder sb = new StringBuilder( );
		maze.set( reindeer.pos, 'S' );
		maze.set( finish, 'E' );
		sb.append( maze.toString( ) );
		maze.unset( finish );
		maze.unset( reindeer.pos );
		return sb.toString( );
	}	
	
	/**
	 * Holds a path of traversed coordinates
	 */
	private class RSPath implements Comparable<RSPath> {
		/** The head of this path */
		protected final ReindeerState head;
		
		/** The tiles of the maze that are traversed by this path */
		protected final List<Coord2D> path;
		
		/**
		 * Creates a new path from the given starting state
		 * 
		 * @param start The starting state of the reindeer
		 */
		public RSPath( final ReindeerState start ) {
			this.head = start;
			this.path = new ArrayList<>( );
			this.path.add( start.pos );
		}
		
		/**
		 * Creates a new path by extending the given one with the given state
		 * 
		 * @param path The path to extend
		 * @param rs The new reindeer state to add to the path
		 */
		public RSPath( final RSPath path, final ReindeerState rs ) {
			this.path = new ArrayList<>( path.path );
			this.path.add( rs.pos );
			this.head = rs;
		}
		
		/** @return The value of this path */
		protected long value( ) {
			return head.value;
		}

		/**
		 * Compares this path to another path in terms of value
		 * 
		 * @param o The other path to compare against
		 * @return The result of Long.compareTo( value, o.value ) 
		 */
		@Override
		public int compareTo( RSPath o ) {
			return Long.compare( value( ), o.value( ) );
		}
	}
	
	/**
	 * Describes the state the reindeer is in after possibly traversing many
	 * tiles through the maze
	 */
	private class ReindeerState {
		/** The position of the reindeer */
		protected final Coord2D pos;
		
		/** The direction the reindeer is currently facing */
		protected final Direction dir;
		
		/** The value of the moves taken so far */
		protected final long value;
		
		/**
		 * Creates a new state for the reindeer to be in
		 * 
		 * @param pos The position of the reindeer
		 * @param dir The direction it is facing in
		 * @param value The value of the path taken so far
		 */
		public ReindeerState( final Coord2D pos, final Direction dir, final long value ) {
			this.pos = pos;
			this.dir = dir;
			this.value = value;
		}
		
		/**
		 * Produces a list that holds zero to three valid next moves that can be
		 * taken from this state, considering the current position, direction and
		 * maze layout
		 * 
		 * @return The set of valid next moves for the reindeer, possibly empty
		 */
		protected Collection<ReindeerState> getNextStates( ) {
			final List<ReindeerState> next = new ArrayList<>( 3 );
			
			// move forward
			if( maze.get( pos.move( dir, 1 ) ) == '.' ) next.add( new ReindeerState( pos.move( dir, 1 ), dir, value + 1 ) );
			
			// turn 90 degrees left and move one step
			if( maze.get( pos.move( dir.turn( -1 ), 1 ) ) == '.' ) next.add( new ReindeerState( pos.move( dir.turn( -1 ), 1 ), dir.turn( -1 ), value + 1001 ) );
			
			// turn 90 degrees right and move one step
			if( maze.get( pos.move( dir.turn( 1 ), 1 ) ) == '.' ) next.add( new ReindeerState( pos.move( dir.turn( 1 ), 1 ), dir.turn( 1 ), value + 1001 ) );			
			
			return next;
		}
		
		/** @return A unique key without value for comparison of similar states */
		public String stateKey( ) {
			return pos.toString( ) + "|" + dir.toSymbol( );
		}
		
		/**
		 * Tests this ReindeerState for equality to another object
		 * 
		 * @param obj The other object
		 * @return True iff the other object is a ReindeerState, and it has the
		 *   same position, direction and value
		 */
		@Override
		public boolean equals( Object obj ) {
			if( obj == null || !(obj instanceof ReindeerState) ) return false;
			final ReindeerState r = (ReindeerState)obj;
			return pos.equals( r.pos ) && dir.equals( r.dir ) && value == r.value;
		}
		
		/** @return The hashcode of the state, given by hashing its unique string */
		public int hashCode( ) {
			return toString( ).hashCode( );
		}

		/** @return The string description of the state */
		public String toString( ) {
			return pos.toString( ) + "|" + dir.toSymbol( ) + ": " + value;
		}
	}
}
