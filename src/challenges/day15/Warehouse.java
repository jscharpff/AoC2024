package challenges.day15;

import java.util.ArrayList;
import java.util.List;

import aocutil.geometry.Coord2D;
import aocutil.geometry.Direction;
import aocutil.grid.CoordGrid;

/**
 * Models boxes in a warehouse and simulates the movements of a logistics robot
 * that is able to push boxes around
 */
public class Warehouse {
	/** The layout of the warehouse */
	protected final CoordGrid<Character> map;
	
	/** The robot's current position */
	protected Coord2D robot;
	
	/** The array of robot moves */
	protected final Direction[] moves;

	/**
	 * Creates a new WarehouseSize2 object
	 * 
	 * @param layout The grid that describes the warehouse layout
	 * @param moves The set of moves the robot is to perform
	 * @param robot The initial position of the robot
	 */
	public Warehouse( final CoordGrid<Character> layout, final Direction[] moves, final Coord2D robot ) {
		this.map = layout.copy( );
		this.moves = new Direction[ moves.length ];
		for( int i = 0; i < moves.length; i++ ) this.moves[i] = moves[i];
		this.robot = robot;
	}
	
	/**
	 * Reconstructs a Warehouse layout from a list of strings that contain the
	 * warehouse layout, a new line and the robot moves.
	 * 
	 * @param input The list of strings that describe the warehouse initial state
	 *   and the robot moves
	 * @return The initiated Warehouse
	 */
	public static Warehouse fromStringList( final List<String> input ) {
		// copy input so that we can modify it. First move all robot move
		// instructions into a single string
		final List<String> in = new ArrayList<>( input );
		final StringBuilder m = new StringBuilder( );
		while( in.get( in.size( ) - 1 ).length( ) > 0 )
			m.insert( 0, in.remove( in.size( ) - 1 ) );
	
		// remove new line from input araay and process instruction string into
		// array of moves
		in.remove( in.size( ) - 1	);
		final Direction[] moves = new Direction[ m.length( ) ];
		for( int i = 0; i < m.length( ); i++ ) moves[i] = Direction.fromSymbol( m.charAt( i ) );

		// process the remainder of the input to produce the warehouse layout
		final CoordGrid<Character> map = CoordGrid.fromCharGrid( in, '.' );
		Coord2D robot = null;
		for( final Coord2D r : map.find( '@' ) ) {
			robot = r;
			map.unset( r );
		}
		
		// finally create and return the warehouse object
		return new Warehouse( map, moves, robot );
	}
	
	/**
	 * Performs all the robot moves
	 * 
	 * @return The warehouse score, determined by the position of boxes after all
	 *   the moves have been processed
	 */
	public long move( ) {
		// process all moves
		for( int i = 0; i < moves.length; i++ ) {
			// get direction, determine next position and check what is there now
			final Direction d = moves[i];
			final Coord2D moveTo = robot.move( d, 1 );

			// check if we can move to free space or push a box, possibly moving
			// multiple boxes that may be behind it. First count number of boxes to
			// push, if any.
			int boxes = 0;
			while( map.get( robot.move( d, 1 + boxes ) ) == 'O' ) boxes++;
			
			// then check if there is free spaces to move into, possibly behind all
			// pushable boxes
			if( map.get( robot.move( d, boxes + 1 ) ) == '#' ) continue;
			
			// yes, move all boxes by simply adding one after the last and removing
			// the one we start the push at (if any)
			if( boxes > 0 ) map.set( robot.move( d, boxes + 1 ), map.unset( moveTo ) );
			
			// finally move the robot itself
			robot = moveTo;
		}
		
		// done moving boxes around, return score of resulting box layout
		return countBoxes( );
	}

	/**
	 * @return The sum of box values, given by their coordinates
	 */
	protected long countBoxes( ) {
		long sum = 0;
		for( final Coord2D c : map.find( 'O' ) ) sum += 100 * c.y + c.x;
		return sum;
	}
	
	/**
	 * @return The string description that visualises the warehouse layout and
	 *   the robot moves
	 */
	@Override
	public String toString( ) {
		final StringBuilder sb = new StringBuilder( );
		map.set( robot, '@' );
		sb.append( map.toString( ) );
		map.unset( robot );
		sb.append( "\n\n" );
		for( final Direction d : moves ) sb.append( d.toSymbol( ) );
		return sb.toString( );
	}
}
