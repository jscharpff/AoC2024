package challenges.day17;

import java.util.ArrayList;
import java.util.List;

import aocutil.string.RegexMatcher;
import aocutil.string.StringUtil;

/**
 * Simple processor that uses integer encoded instructions and operands as its
 * instruction set.
 */
public class IntCodeMachine {
	/** The program that is currently loaded */
	protected final int[] program;
	
	/** The memory registers A, B and C */
	protected final long[] mem;
	
	/** The instruction pointer */
	protected int ip;
	
	/**
	 * Creates a new IntCodeMachine from the specified program listing
	 * 
	 * @param program List of strings that describe the program in the following
	 *   structure:
	 *   
	 *   Register A: <value>
	 *   Register B: <value>
	 *   Register C: <value>
	 *   
	 *   Program: <instruction set>
	 */
	public IntCodeMachine( final List<String> program ) {
		// first read initial memory register values
		mem = new long[3];
		final RegexMatcher rm = new RegexMatcher( "Register [ABC]: #D" );
		for( int i = 0; i < mem.length; i++ ) {
			rm.match( program.get( i ) );
			mem[i] = rm.getInt( 1 );
		}
		
		// then process the instruction set into an array of integers
		final String[] in = program.get( 4 ).split( ": " )[1].split( "," );
		this.program = new int[ in.length ];
		for( int i = 0; i < in.length; i++ ) this.program[i] = Integer.parseInt( in[i] );

		// set instruction pointer to -1 to indicate not initialised
		ip = -1;
	}
	
	/**
	 * Sets the value of memory register A, B or C
	 * 
	 * @param register The register to set, identified by its numerical index
	 * @param value The value to set in the register
	 * @return The value it previously held
	 */
	public long setMemory( final int register, final long value ) {
		if( register < 0 || register > 2 ) throw new IllegalArgumentException( "Invalid memory register index " + register );
		final long old = mem[ register ];
		mem[ register ] = value;
		return old;
	}
	
	/**
	 * Runs the program by simply processing its instruction set. Note that this
	 * program run will use the memory register values as is. If the program is
	 * to be run more than once, it is probably a good idea to (re)set the memory
	 * registers first
	 * 
	 * @return The list of all integers that were output by the program over the
	 *   course of its execution
	 */
	public List<Long> run( ) {
		final List<Long> output = new ArrayList<>( );
		ip = 0;
		while( ip < program.length - 1 ) {
			// fetch the operation and the operand at the current instruction pointer
			final int opcode = program[ip];
			final int operand = program[ip+1];
			
			// perform the operation
			switch( opcode ) {
				// adv: divide A register and store in A
				case 0: mem[0] = mem[0] / (int) Math.pow( 2, read( operand, true ) ); break;
				
				// bxl: bitwise XOR of B and literal operand, store in B
				case 1: mem[1] = mem[1] ^ read( operand, false ); break;
				
				// bst: write operand modulo 8 into B register
				case 2: mem[1] = read( operand, true ) % 8; break;
				
				// jnz: jumps to instruction specified by operand if A != 0
				case 3: {
					if( mem[0] == 0 ) break;
					ip = (int)read( operand, false ) - 2;
					break;
				}
				
				// bxc: bitwise XOR of B and C, store in B
				case 4: mem[1] = mem[1] ^ mem[2]; break;

				// out: ouputs the value mod 8 of its operand
				case 5: output.add( read( operand, true ) % 8 ); break;
				
				// bdv: divide A register and store in B
				case 6: mem[1] = mem[0] / (int) Math.pow( 2, read( operand, true ) ); break;
				
				// cdv: divide A register and store in C
				case 7: mem[2] = mem[0] / (int) Math.pow( 2, read( operand, true ) ); break;

				default: throw new UnsupportedOperationException( "Opcode " + opcode + " at IP " + ip + " is not supported." );
			}

			// move instruction point to the next instruction
			ip += 2;
		}
		
		// instruction pointer passed the last instruction, program halts and
		// returns all outputs it generated
		return output;
	}
		
	/**
	 * Reads the operand as literal value (combo = false) or as a combo value.
	 * A combo value 0-3 is read as literal value, 4-6 indicate registry A, B and
	 * C respectively and a value of 7 is ignored.
	 * 
	 * @param operand The operand to read
	 * @param combo True to interpret this as a combo operand, false to interpret
	 *   it as literal
	 * @return The interpreted operand value, either literal or from the
	 *   specified memory register
	 */
	protected long read( final int operand, final boolean combo ) {
		if( !combo ) return operand;
		
		if( operand < 4 ) return operand;
		if( operand < 7 ) return mem[ operand - 4 ];
		throw new UnsupportedOperationException( "Unsupported operand code " + operand );
	}
	
	/** @return The string that describes the program */
	@Override
	public String toString( ) {
		final StringBuilder sb = new StringBuilder( );
		sb.append( "Registers: A=" + mem[0] + ", B=" + mem[1] + ", C=" + mem[2] + "\n" );
		sb.append( "Program: " + StringUtil.fromArray( program ) + " (IP: " + ip + ")" );
		return sb.toString( );
	}
}
