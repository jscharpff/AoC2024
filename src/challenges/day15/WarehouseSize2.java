package challenges.day15;

import java.util.List;

import aocutil.geometry.Coord2D;
import aocutil.geometry.Direction;
import aocutil.grid.CoordGrid;

/**
 * Another Warehouse, but this time it is twice as wide and all walls and boxes
 * span 2 horizontal tiles instead of 1.
 */
public class WarehouseSize2 extends Warehouse {
	
	/**
	 * Creates a new WarehouseSize2 object
	 * 
	 * @param layout The grid that describes the warehouse layout
	 * @param moves The set of moves the robot is to perform
	 * @param robot The initial position of the robot
	 */
	public WarehouseSize2( CoordGrid<Character> layout, Direction[] moves, Coord2D robot ) {
		super( layout, moves, robot );
	}

	/**
	 * Reconstructs a WarehouseSize2 layout from a list of strings that contain
	 * the warehouse layout, a new line and the robot moves.
	 * 
	 * @param input The list of strings that describe the warehouse initial state
	 *   and the robot moves
	 * @return The initiated WarehouseSize2
	 */
	public static WarehouseSize2 fromStringList( final List<String> input ) {	
		// create a regular warehouse
		final Warehouse w = Warehouse.fromStringList( input );
		
		// and expand its layout into a Size2 warehouse type
		final CoordGrid<Character> newmap = new CoordGrid<Character>( '.' );
		for( final Coord2D c : w.map.getKeys( ) ) {
			final Coord2D nc = new Coord2D( c.x * 2, c.y );
			final char ch = w.map.get( c );
			if( ch == '#' ) {
				newmap.set( nc, '#' );
				newmap.set( nc.move( 1, 0 ), '#' );
			} else if( ch == 'O' ) {
				newmap.set( nc, '[' );
				newmap.set( nc.move( 1, 0 ), ']' );
			} else throw new IllegalArgumentException( "Invalid character '" + ch + "' in input at " + c );
		}
		
		// then create and return the Size2 warehouse object
		return new WarehouseSize2( newmap, w.moves, new Coord2D( w.robot.x * 2, w.robot.y ) );
	}
	
	/**
	 * Performs all the robot moves
	 * 
	 * @return The warehouse score, determined by the position of boxes after all
	 *   the moves have been processed
	 */
	@Override
	public long move( ) {
		// process all moves
		for( int i = 0; i < moves.length; i++ ) {
			// get direction, determine next position and check what is there now
			final Direction d = moves[i];
			final Coord2D moveTo = robot.move( d, 1 );
			final char moveCh = map.get( moveTo );
			
			// nothing? simply move the robot
			if( moveCh == '.' ) {
				robot = moveTo;
			} else if( moveCh == '[' || moveCh == ']' ) {
				// there is a box here, see if the robot can push it (check via its
				// leftmost coordinate)
				final Coord2D bcoord = moveCh == '[' ? moveTo : moveTo.move( -1, 0 );
				if( canPush( bcoord, d ) ) {
					// yes! push the box and all boxes behind it, then move the robot
					push( bcoord, d );
					robot = moveTo;
				}
			} else {
				// no valid move to perform..
			}
		}
		
		// all moves have been processed, return box score of resulting warehouse
		// situation
		return countBoxes( );
	}
	
	/**
	 * Checks if we can the box, specified by its leftmost coordinate, in the
	 * given direction
	 * 
	 * @param bcoord The leftmost coordinate of the box we would like to test for
	 *   pushing
	 * @param d The direction in which we want to push
	 * @return True iff this box, and all boxes behind it, can be moved by
	 *   pushing into the specified direction
	 */
	private boolean canPush( final Coord2D bcoord, final Direction d ) {
		// check for any boxes that may be after me
		switch( d ) {
			case North:
			case South: {
				// check the two coordinates at which a box may be behind me
				for( final Coord2D moveTo : new Coord2D[] { bcoord.move( d, 1 ), bcoord.move( d, 1 ).move( 1, 0 ) } ) {
					final char moveCh = map.get( moveTo );
					final boolean canmove = moveCh == '.' || (moveCh == ']' && canPush( moveTo.move( -1, 0 ), d ) ) || (moveCh == '[' && canPush( moveTo, d ) );
					
					// if we cannot move the left box, no need to try the right box
					if( !canmove ) return false;
				}
				return true;
			}
				
			case West:
			case East: {
				// check immediate left or right from me
				final Coord2D moveTo = bcoord.move( d, d == Direction.West ? 1 : 2 );
				final char moveCh = map.get( moveTo );
				return moveCh == '.' || (moveCh == ']' && canPush( moveTo.move( -1, 0 ), d ) || (moveCh == '[' && canPush( moveTo, d ) ) );
			}
			
			default: throw new IllegalArgumentException( "Invalid direction: " + d );					
		}
	}
	
	/**
	 * Pushes this box, identified through its left or right coordinate, in the
	 * given direction. Will first push box after it before moving itself.
	 * 
	 * @param bcoord The coordinate used to identify the box
	 * @param d
	 */
	private void push( final Coord2D bcoord, final Direction d ) {
		// correct box coordinate here, easier than to do it in North & South push moves
		final Coord2D bc = map.get( bcoord ) == ']' ? bcoord.move( -1, 0 ) : bcoord;

		// check if there is free space after the box, if not we should push the
		// box there first. If a wall is encountered, the move is invalid
		if( map.get( bc ) == '.' ) return;
		if( map.get( bc ) == '#' ) throw new RuntimeException( "Block cannot be pushed to " + bc + " in direction " + d + "!\n" + this );
		
		// recursively first try and move all boxes resting on me
		switch( d ) {
			case North:
			case South:
				push( bc.move( d, 1 ), d );
				push( bc.move( d, 1 ).move( 1, 0 ), d );
				break;

			case West:
			case East:
				push( bc.move( d, d == Direction.West ? 1 : 2 ), d );
				break;
		}
		
		// all boxes behind this box were moved, now move this one
		map.unset( bc ); map.unset( bc.move( 1, 0 ) );
		map.set( bc.move( d, 1 ), '[' );
		map.set( bc.move( d, 1 ).move( 1, 0 ), ']' );
	}
	
	/**
	 * @return The sum of box values, given by their coordinates
	 */
	@Override
	protected long countBoxes( ) {
		long sum = 0;
		for( final Coord2D c : map.find( '[' ) ) sum += 100 * c.y + c.x;
		return sum;
	}
}
