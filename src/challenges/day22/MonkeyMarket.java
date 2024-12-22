package challenges.day22;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Models a MonkeyMarket where buyers sell bananas to obtain secret hidden
 * spots in the jungle. The pricing, however, works a bit strange...
 */
public class MonkeyMarket {
	/** The initial secret number of each of the buyers in the market*/
	protected final List<Long> seeds;
	
	/**
	 * Creates a new MonkeyMarket place with the number of buyers equal to the
	 * size of the list
	 * 
	 * @param buyerseeds The initial seeds of all buyers in the market
	 */
	public MonkeyMarket( final List<String> buyerseeds ) {
		this.seeds = new ArrayList<>( buyerseeds.size( ) );
		for( final String seed : buyerseeds ) seeds.add( Long.parseLong( seed ) );
	}
	
	/**
	 * Sums the buyer's secrets after running x rounds of generating new secrets
	 * 
	 * @param rounds The number of rounds to iterate the secret generation
	 *   procedure
	 * @return The sum of all secrets after x iterations
	 */
	public long sumSecrets( final int rounds ) {
		return seeds.stream( ).mapToLong( s -> getSecretNumber( s, rounds ) ).sum( );
	}
	
	/**
	 * Generates the secret number for x rounds starting from the given initial
	 * seed value
	 * 
	 * @param seed The initial seed to start generation from
	 * @param rounds The number of rounds to generate the secret code
	 * @return The secret code that results after the specified iterations
	 */
	protected long getSecretNumber( final long seed, final int rounds ) {
		long secret = seed;
		for( int i = 0; i < rounds; i++ ) secret = nextSecret( secret );
		return secret;
	}
	
	/**
	 * Generates the next secret code for the given starting secret value
	 * 
	 * @param secret The secret value to start from
	 * @return The newly generated secret after applying the generation procedure
	 */
	protected long nextSecret( final long secret ) {
		// Calculate the result of multiplying the secret number by 64. Then, mix
		// this result into the secret number. Finally, prune the secret number.
		long next = secret ^ (secret << 6) % 16777216;
		
		// Calculate the result of dividing the secret number by 32. Round the
		// result down to the nearest integer. Then, mix this result into the
		// secret number. Finally, prune the secret number.
		next = next ^ (next >> 5) % 16777216;
		
		// Calculate the result of multiplying the secret number by 2048. Then,
		// mix this result into the secret number. Finally, prune the secret number.
		next = next ^ (next << 11) % 16777216;
		
		return next;
	}

	/**
	 * Finds the sequence of price changes that will lead to the buying of the
	 * most bananas from all vendors together.
	 * 
	 * @param maxrounds The maximum number of rounds to simulate the market place
	 * @return The highest sum of bananas you can buy using the best price change
	 *   sequence as buying moment
	 */
	public long buyMostBananas( final int maxrounds ) {
		final int N = seeds.size( );
		final int k = 5;
		
		// initialise secret matrix that will be thereafter used as a rolling
		// window of secret values
		long[][] lastsecrets = new long[N][k];
		for( int j = 0; j < N; j++ ) {
			lastsecrets[j][0] = seeds.get( j );
			for( int i = 1; i < k - 1; i++ )
				lastsecrets[j][i] = nextSecret( lastsecrets[j][i-1] );
		}
		
		// now start the actual work. Run the secret generation algorithm in rounds
		// and for each new pricing difference sample (unique combination of four
		// consecutive price changes) store the count of bananas we can buy
		final Map<Integer, Integer> C = new HashMap<>( );
		final List<Set<Integer>> V = new ArrayList<>( N );
		for( int i = 0; i < N; i++ ) V.add( new HashSet<>( ) );
		
		// start from the first index from which we can generate a sample
		int index = k - 1;
		for( int i = index; i <= maxrounds; i++ ) {
			// generate next seed
			for( int j = 0; j < N; j++ ) lastsecrets[j][index] = nextSecret( lastsecrets[j][(index - 1 + k) % k] );
		
			// then sum the banana count for each sample
			for( int j = 0; j < N; j++ ) {
				final int bananas = (int)lastsecrets[j][index] % 10;
				int key = 0;
				for( int l = 0; l < k-1; l++ ) {
					key *= 100;
					key += (lastsecrets[j][(index - l + k) % k] % 10 - lastsecrets[j][(index - l - 1 + k) % k] % 10) + 10;
				}

				// check if we already have this sample for this buyer. If so, ignore it
				if( !V.get( j ).contains( key ) ) {
					C.put( key, C.getOrDefault( key, 0 ) + bananas );
					V.get( j ).add( key );
				}
			}

			// increase rolling window index
			index = (index + 1) % k;
		}
		
		// return number of bananas for maximum selling sample
		return C.values( ).stream( ).max( (x,y) -> x - y ).get( );
	}
}
