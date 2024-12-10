package challenges.day09;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Class that helps defragmenting memory
 */
public class Defragmenter {
	/**
	 * Performs defragmentation of memory by moving individual data fragments
	 * into free memory segments to the left of it, given the initial memory
	 * layout
	 * 
	 * @param input The memory layout
	 * @return The checksum of the memory after defragmentation
	 */
	public static long defragmentSingle( final String input ) {
		// first convert memory layout string into integer array for easy manipulation
		final int[] in = new int[ input.length( ) ];
		for( int i = 0; i < input.length( ); i++ )
			in[i] = input.charAt( i ) - '0';
		
		// keep track of target position and consequential checksum while moving memory
		int targetpos = 0;
		long chksum = 0;
		
		// pointers to data to write and move
		int ldx = -1;
		int rdx = in.length - 1;

		// keep moving left hand pointer until there is no more data to write
		while( ++ldx <= rdx ) {
			// fetch data to write or free space to fill
			final int ldata = in[ldx];
			
			// if the left pointer is at even position, we write the data
			if( ldx % 2 == 0 ) {
				// get block ID to compute checksum of this block and add it
				final int ID = ldx / 2;
				for( int i = 0; i < ldata; i++ ) chksum += ID * targetpos++;
				continue;
			} 

			// at odd position we copy data from right into the free space at the
			// left index until all free space is filled or there is no more to write
			int free = ldata;
			while( free > 0 ) {
				// get data to write and its ID
				int rdata = in[rdx];
				final int ID = rdx / 2;
				
				// enough space to write all right data blocks?
				if( free < rdata ) {
					// no, write only what we can, update the checksum and decrease free
					// space left to write after this block is fitted
					in[rdx] -= free;
					while( free-- > 0 ) chksum += ID * targetpos++;
				} else {
					// yes, write the remaining part and move right hand pointer to next
					// data block to be written into this gap
					while( in[rdx]-- > 0 ) {
						free--;
						chksum += ID * targetpos++;
					}												
					
					// move rdx to next block and check if there is still data to write
					rdx -= 2;
					if( rdx < ldx ) break;
				}
			}
		}

		// return the checksum that is computed during defragmentation
		return chksum;
	}
	
	/**
	 * Runs a defragmentation that will try to move FileBlocks as a whole into
	 * the available space on the left side of its position, given the memory
	 * layout. It will try to move FileBlocks only once and in their order from
	 * right to left as they occur in the starting layout.
	 * 
	 * @param input The starting layout of the computer memory
	 * @return The checksum of the resulting memory layout
	 */
	public static long defragmentBlock( final String input ) {
		// step 1, parse input and convert to file blocks
		final List<FileBlock> files = new ArrayList<>( );
		int pos = 0;
		for( int i = 0; i < input.length( ); i++ ) {
			final int size = input.charAt( i ) - '0';
			if( i % 2 == 0 ) files.add( new FileBlock( i / 2, size, pos ) );
			pos += size;
		}

		// step 2, try to fill the empty space with file blocks right to left
		// use a stack here to make sure all blocks are only considered once
		final Stack<FileBlock> fit = new Stack<>( );
		fit.addAll( files );
		while( !fit.isEmpty( ) ) {
			// try to fit next block
			final FileBlock b = fit.pop( );
		
			// go over memory gaps from left to right to see if this block might fit
			for( int ldx = 0; ldx < files.size( ) - 1; ldx++ ) {
				final FileBlock fl = files.get( ldx );
				
				// do not consider moving further to the right than the block already is
				if( fl.index >= b.index ) break;

				// check if we can fit the current block in this slot of free space
				final int free = files.get( ldx + 1 ).index - (fl.index + fl.size);
				if( free >= b.size ) {
					// yes, move the block to the start of the gap and update the file
					// ordering accordingly
					b.index = fl.index + fl.size;
					files.add( ldx + 1, files.remove( files.indexOf( b ) ) );
					break;
				}
			}
		}

		// step 3, do the checksum computation and return it
		long chksum = 0;
		for( final FileBlock b : files ) chksum += b.checksum( );
		return chksum;
	}

	/**
	 * Simple container that holds a file with given ID, position and size
	 */
	private static class FileBlock {
		/** The ID of the file */
		final int ID;
		
		/** The size of the file in memory */
		final int size;
		
		/** The index it is currently positioned at in memory */
		protected int index;
		
		/**
		 * Creates a new FileBlock
		 * 
		 * @param ID The ID of the file
		 * @param size The size of the file
		 * @param index The index of the file in memory
		 */
		protected FileBlock( final int ID, final int size, final int index ) {
			this.ID = ID;
			this.size = size;
			this.index = index;
		}
		
		/**
		 * Computes the checksum of the file, based upon ID, size and position
		 * 
		 * @return The checksum
		 */
		protected long checksum( ) {
			long chksum = 0;
			for( int i = index; i < index + size; i++ ) chksum += i * ID;
			return chksum;
		}
		
		/** @return The string describing the block */
		@Override
		public String toString( ) {
			return "[" + ID + "@" + index + "-" + (index+size-1) + "|" + size + "]";
		}
		
		/**
		 * Checks if the specified object is equal to this FileBlock.
		 * 
		 * @param obj The object to test equality against
		 * @return True iff the object is not null, is a FileBlock and has the same
		 *   ID
		 */
		@Override
		public boolean equals( Object obj ) {
			if( obj == null || !(obj instanceof FileBlock) ) return false;
			return ((FileBlock)obj).ID == ID;
		}
	}
}
