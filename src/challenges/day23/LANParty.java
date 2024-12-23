package challenges.day23;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import aocutil.graph.Edge;
import aocutil.graph.Graph;
import aocutil.graph.Node;

/**
 * LAN Party at the Easter BUnny HQ
 */
public class LANParty {
	/** The graph of connected computers */
	protected final Graph network;

	/**
	 * Creates a new LAN Party network using the interface connections specified
	 * to build a network topology of connected computers
	 * 
	 * @param connections The connections between computers in the network
	 */
	public LANParty( final List<String> connections ) {
		network = new Graph( );
		for( final String conn : connections ) {
			final String[] iface = conn.split( "-" );
			final Node n1 = network.addNode( iface[0] );
			final Node n2 = network.addNode( iface[1] );
			network.addEdge( new Edge( n1, n2 ) );
		}
	}
	
	/**
	 * Uses k-clique algorithm to find all connected 3-cliques in the LAN party
	 * network topology graph, then filters out those cliques that do not have at
	 * least one computer name starting with the specified prefix
	 * 
	 * @param startswith The prefix that at least one computer in the 3-clique
	 *   must have for it to be counted
	 * @return The number of unique k-cliques that have at least one node label
	 *   starting with the specified prefix
	 */
	public long countConnectedTriplets( final String startswith ) {
		// get all connected cliques of size k
		final List<Set<Node>> T = getCliques( 3 );
		
		// count only those that have a node starting with the specified prefix
		long count = 0;
		for( final Set<Node> triplet : T )
			for( final Node n : triplet )
				if( n.getLabel( ).startsWith( startswith ) ) {
					count++;
					break;
				}
		return count;
	}
	
	/**
	 * Uses k-clique algorithm to find the largest possible connected subgraph in
	 * the network
	 * 
	 * @return The largest connected subgraph as a comma-separated string of its
	 *   nodes
	 */
	public String getPassword( ) {
		// find largest possible clique in the network and return it as sorted
		// string of its computer names
		return sorted( getCliques( network.size( ) ).get( 0 ) );
	}
	
	/**
	 * Simple implementation of the k-clique algorithm. The algorithm iteratively
	 * generates new k+1 cliques by finding all extensions of the k-cliques with
	 * new nodes such that the new set of nodes is still fully connected.
	 * 
	 * @param maxsize The maximum clique size to search for, use a high value to
	 *   find the largest possible clique
	 * @return The list that holds all cliques of size k in which k <= maxsize.
	 */
	private List<Set<Node>> getCliques( final int maxsize ) {
		// iteratively build set of connected graphs from individual nodes
		final Map<String, Set<Node>> C = new HashMap<>( );
		
		// initialise this map with single nodes
		for( final Node n : network.getNodes( ) ) {
			final Set<Node> ns = new HashSet<>( );
			ns.add( n );
			C.put( n.toString( ), ns );
		}
		
		// then iteratively generate connected graphs of one size bigger
		final Stack<String> explore = new Stack<>( );
		explore.addAll( C.keySet( ) );
		
		// keep searching until we end up with only the maximum sized cliques
		final List<Set<Node>> lastresult = new ArrayList<>( );
		int size = 1;
		while( !explore.isEmpty( ) && (++size <= maxsize + 1) ) {
			lastresult.clear( );
			
			while( !explore.isEmpty( ) ) {
				// extend the next k-clique into k+1-cliques
				final String key = explore.pop( );
				final Set<Node> nodes = C.get( key );
				lastresult.add( C.remove( key ) );
				
				// only search in the neighbours of any of the nodes already in the
				// connected graph, not over all nodes
				for( final Node n : nodes.iterator( ).next( ).getNeighbours( ) ) {
					// already in the clique? skip it
					if( nodes.contains( n ) ) continue;
					
					// check if the neighbour is connected to all other nodes in the
					// clique
					boolean connected = true;
					for( final Node n2 : nodes ) {
						connected &= n.getNeighbours( ).contains( n2 );
						if( !connected ) break;
					}
					if( !connected ) continue;
					
					// node can be added to create a fully connected graph, create the
					// new set and add to the map if not already present
					final Set<Node> newnodes = new HashSet<>( nodes );
					newnodes.add( n );
					final String newkey = sorted( newnodes );
					if( C.containsKey( newkey ) ) continue;
					
					// add the set key to expand in the next round and 
					C.put( newkey, newnodes );
				}				
			}
			explore.addAll( C.keySet( ) );
		}
		
		// return nodes that form the largest cliques
		return lastresult;
	}

	/**
	 * Generates a comma-separated string of the labels of the nodes in the set,
	 * ordered alphabetically
	 * 
	 * @param nodes The set of nodes to convert into sorted string
	 * @return The string of node labels, ordered alphabetically and separated by
	 *   commas
	 */
	private String sorted( final Set<Node> nodes ) {
		// empty set
		if( nodes.isEmpty( ) ) return "";
		
		// nope, sort the nodes based upon label
		final List<Node> sorted = new ArrayList<>( nodes );
		sorted.sort( (a, b) -> String.CASE_INSENSITIVE_ORDER.compare( a.getLabel( ), b.getLabel( ) ) );
		
		// and construct the output string
		final StringBuilder sb = new StringBuilder( );
		sb.append( sorted.get( 0 ).getLabel( ) );
		for( int i = 1; i < sorted.size( ); i++ ) sb.append( "," + sorted.get( i ).getLabel( ) );
		return sb.toString( );
	}
}
