package challenges.day14;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aocutil.geometry.Coord2D;
import aocutil.grid.CoordGrid;
import aocutil.string.RegexMatcher;

/**
 * Models a grid area in which robots patrol
 */
public class RobotGrid {
	/** The size of the grid */
	private final Coord2D size;
	
	/** The set of robots that patrol the grid */
	protected Set<Robot> bots = new HashSet<>( );

	/**
	 * Creates a new grid of the specified size
	 * 
	 * @param width The grid width
	 * @param height The grid height
	 */
	public RobotGrid( final int width, final int height ) {
		size = new Coord2D( width, height );
	}
	
	/**
	 * Adds robots to the grid, reconstructed from their string description
	 * 
	 * @param robots The list of strings that describe the initial position and
	 *   movement vector of each robot 
	 */
	public void addRobots( final List<String> robots ) {
		robots.stream( ).forEach( s -> bots.add( Robot.fromString( s ) ) );
	}
	
	/**
	 * Simulates the robot movements for the specified number of seconds.
	 * 
	 * @param seconds The number of seconds to simulate the robot movement
	 * @return The product of robot counts per quadrant of the area after their
	 *   movement has been simulated for the specified number of seconds
	 */
	public long simulate( final int seconds ) {
		// determine the end position of each robot after time has passed
		final Map<Coord2D, Integer> end = new HashMap<>( bots.size( ) );		
		for( final Robot r : bots ) {
			final Coord2D endpos = new Coord2D( 
					((r.pos.x + r.vec.x * seconds) % size.x + size.x) % size.x , 
					((r.pos.y + r.vec.y * seconds) % size.y + size.y) % size.y
			);
			end.put( endpos, end.getOrDefault( endpos, 0 ) + 1 );
		}
		
		// then count robots per quadrant
		final int[][] Q = new int[2][2];
		final int hW = size.x / 2;
		final int hH = size.y / 2;
		for( final Coord2D c : end.keySet( ) ) {
			// don't count robots exactly in the middle
			if( c.x == hW || c.y == hH ) continue;
			Q[(c.x - 1) / hW][(c.y - 1) / hH] += end.get( c );
		}
		
		// and return their product
		return Q[0][0] * Q[0][1] * Q[1][0] * Q[1][1];
	}
	
	/**
	 * Simulate the movement of robots until we find the hidden Easter Egg: a
	 * display of a Christmas Tree
	 * 
	 * @param timelimit The maximum number of seconds to try before giving up the
	 *   Easter Egg hunt
	 * @return The second at which the Christmas Tree becomes first visible, -1
	 *   if not found within the time limit
	 */
	public long simulateEasterEgg( final int timelimit ) {
		// keep simulating for the max time until we find the Christmas Tree
		for( int i = 1; i <= timelimit; i++ ) {
			// keep track of positions but also the maximum count of robots per
			// position. Having all robots in a unique spot sure seems a promising
			// start to look for an Easter Egg...
			final CoordGrid<Integer> end = new CoordGrid<Integer>( size.x, size.y, 0 );
			int maxcount = 0;
			for( final Robot r : bots ) {
				final Coord2D endpos = new Coord2D( 
						((r.pos.x + r.vec.x * i) % size.x + size.x) % size.x , 
						((r.pos.y + r.vec.y * i) % size.y + size.y) % size.y
				);
				final int newcount = end.get( endpos, 0 ) + 1;
				end.set( endpos, newcount );
				if( newcount > maxcount ) maxcount = newcount;
			}
			
			// indeed! When all robots are in a unique position, the Christmas Tree
			// shows up!
			if( maxcount == 1 ) {
				System.out.println( "--[ ROUND " + i + " ]--"  );
				System.out.println( end.toString( c -> c == 0 ? "." : "" + c ) );
				System.out.println(  );
				return i;
			}
		}
		
		// no Easter Egg found within the time limt
		return -1;
	}
	
	/**
	 * Container for a simple robot that has a position and movement velocity
	 * vector
	 */
	private static class Robot {
		/** The robot position */
		protected final Coord2D pos;
		
		/** The movement velocity vector */
		protected final Coord2D vec;
		
		/**
		 * Creates a new robot
		 * 
		 * @param pos The robot's position
		 * @param vec The robot's movement velocity vector
		 */
		public Robot( final Coord2D pos, final Coord2D vec ) {
			this.pos = pos;
			this.vec = vec;
		}
		
		/**
		 * Reconstructs a robot from a string description in the form p=x,y v=x,y
		 * where p is its initial position, v its movement velocity
		 * 
		 * @param input The string that describes the robot
		 * @return The robot
		 */
		public static Robot fromString( final String input ) {
			final RegexMatcher rm = new RegexMatcher( "p=#D,#D v=#D,#D" );
			rm.match( input );
			return new Robot( new Coord2D( rm.getInt( 1 ), rm.getInt( 2 ) ), new Coord2D( rm.getInt( 3 ), rm.getInt( 4 ) ) );
		}
	}
}
