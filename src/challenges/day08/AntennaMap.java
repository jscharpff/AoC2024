package challenges.day08;

import java.util.List;
import java.util.Map.Entry;

import aocutil.geometry.Coord2D;
import aocutil.grid.CoordGrid;

/**
 * Class that models a grid of antennas and simulates the projection of
 * antinodes from these antennas
 */
public class AntennaMap {
	/** The layout of antennas as a 2D grid */
	protected CoordGrid<Character> map;
	
	/**
	 * Reconstructs an antenna map from a list of strings that describes a 2D
	 * antenna layout grid
	 * 
	 * @param input The list of strings that describes the antenna layout, one
	 *   row per string
	 */
	public AntennaMap( final List<String> input ) {
		// create grid
		map = CoordGrid.fromCharGrid( input, '.' );
		
		// fix the size to be the same as the input window, otherwise the CoordGrid
		// will assume the antenna positions to define the boundaries
		map.fixWindow( new Coord2D( 0, 0 ), new Coord2D( input.get( 0 ).length( ) - 1, input.size( ) - 1) );
	}
	
	/**
	 * Projects the antinodes of the antennas in the grid and counts the number
	 * of unique projections. Here, unique means that if several antenna pairs
	 * project to the same grid position, it is only counted as one.
	 * 
	 * @param repeated True to enable repeated projections of antinodes, one at
	 *   every x distance from an antenna pair such that x is the distance
	 *   between the antenna's in the pair
	 * @return The count of projected antinodes within the antenna map size
	 */
	public long countAntiNodes( final boolean repeated ) {
		// build map of antinodes, which must be the same size as the antenna map
		final CoordGrid<Character> AN = new CoordGrid<Character>( map.window( ).getWidth( ), map.window( ).getHeight( ), '.' );
		
		// go over each antenna and project the antinodes it creates with every
		// other antenna of the same frequency (i.e., same letter or digit)
		for( final Entry<Coord2D, Character> e : map.getEntries( ) ) {
			final Coord2D a = e.getKey( );
			final char ant = e.getValue( );
			
			// find all other antennas of the same type and project the antinodes
			for( final Coord2D c : map.find( ant ) ) {
				// do not project if this coordinate is the one we are currently
				// processing
				if( c.equals( a ) ) continue;
				
				// compute Manhattan distance between both nodes and project along that
				// vector, or once if no repeated projection is used
				final Coord2D d = c.diff( a );
				Coord2D an = repeated ? a : a.move( d );
				while( AN.contains( an ) ) {
					AN.set( an, '#' );
					an = an.move( d );
					
					// single projection? then we are done
					if( !repeated ) break;
				}
				
			}
		}
		
		// return count of all projections within the antinode grid
		return AN.count( '#' );
	}
}
