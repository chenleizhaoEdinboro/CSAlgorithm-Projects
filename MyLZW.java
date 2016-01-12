

public class MyLZW
{
	private static final int R = 256;         // number of input chars
	private static       int W = 9;           // codeword width
	private static       int L = 1 << W;      // number of codewords = 2^W
	private static final int Lmax = 1 << 16;  // max number of codewords = 2^16
	private static       int M;               // dictionary full what do

	public static void compress()
	{
		int expandedIntegralSizeInCharCount = 0, compressedIntegralSizeInBits = 0;
		boolean hasMeasuredRatioThreshold = false;
		double ratioThreshold = 0;
		String input = BinaryStdIn.readString();
		TST<Integer> st = new TST<Integer>();

		for ( int i = 0; i < R; i++ )
		{
			st.put( "" + ( char ) i, i );
		}

		int code = R + 1; // R is codeword for EOF
		BinaryStdOut.write( M, 2 ); // Print M, two bits containing dictionary full handle mode.

		while ( input.length() > 0 )
		{
			String s = st.longestPrefixOf( input ); // Find max prefix match s.
			expandedIntegralSizeInCharCount += s.length();
			BinaryStdOut.write( st.get( s ), W );  // Print s's encoding.
			compressedIntegralSizeInBits += W;
			int t = s.length();

			if ( t < input.length() && code < Lmax )  // Add s to symbol table.
			{
				if ( code >= L )
				{
					W++;
					L *= 2;
				}

				st.put( input.substring( 0, t + 1 ), code++ );
			}
			else if ( t < input.length() && M == 1 ) // Dictionary is full and to be reset.
			{
				st = new TST<Integer>();

				for ( int i = 0; i < R; i++ )
				{
					st.put( "" + ( char ) i, i );
				}

				code = R + 1; // R is codeword for EOF
				W = 9;
				L = 1 << W;
				st.put( input.substring( 0, t + 1 ), code++ );
			}
			else if ( t < input.length() && M == 2 ) // Dictionary is full and we ought to monitor the compression ratio.
			{
				if ( !hasMeasuredRatioThreshold )
				{
					ratioThreshold = 8.0 * expandedIntegralSizeInCharCount / compressedIntegralSizeInBits / 1.1;
					hasMeasuredRatioThreshold = true;
				}
				else if ( 8.0 * expandedIntegralSizeInCharCount / compressedIntegralSizeInBits < ratioThreshold )
				{
					st = new TST<Integer>();

					for ( int i = 0; i < R; i++ )
					{
						st.put( "" + ( char ) i, i );
					}

					code = R + 1; // R is codeword for EOF
					W = 9;
					L = 1 << W;
					st.put( input.substring( 0, t + 1 ), code++ );
				}
			}

			
			input = input.substring( t );          // Scan past s in input.
		}

		BinaryStdOut.write( R, W );
		BinaryStdOut.close();
	}


	public static void expand()
	{
		int expandedIntegralSizeInCharCount = 0, compressedIntegralSizeInBits = 0;
		boolean hasMeasuredRatioThreshold = false;
		double ratioThreshold = 0;
		String[] st = new String[Lmax];
		int i; // next available codeword value

		// initialize symbol table with all 1-character strings
		for ( i = 0; i < R; i++ )
		{
			st[i] = "" + ( char ) i;
		}

		st[i++] = "";                        // (unused) lookahead for EOF
		M = BinaryStdIn.readInt( 2 );
		int codeword = BinaryStdIn.readInt( W );
		compressedIntegralSizeInBits += W;
		String val = st[codeword];

		while ( true )
		{
			BinaryStdOut.write( val );//if (M==1) System.err.println(""+W+" , "+i+" , "+codeword);
			expandedIntegralSizeInCharCount += val.length();
			codeword = BinaryStdIn.readInt( W );
			compressedIntegralSizeInBits += W;

			if ( codeword == R )
			{
				break;
			}

			String s = st[codeword];

			if ( i == codeword )
			{
				s = val + val.charAt( 0 );    // special case hack
			}

			if ( i == Lmax - 1 )
			{
				switch ( M )
				{
					case 0:
						st[i++] = val + s.charAt( 0 );
						break;

					case 1:
						st = new String[Lmax];

						// initialize symbol table with all 1-character strings
						for ( i = 0; i < R; i++ )
						{
							st[i] = "" + ( char ) i;
						}

						st[i++] = "";                        // (unused) lookahead for EOF
						W = 9;
						L = 1 << W;
						//st[i++] = val + s.charAt(0);  // 
						break;  // one-step-behind taken into account by having this block before the next one.

					case 2:
						if ( !hasMeasuredRatioThreshold )
						{
							//alway output 8 bit char to standard output
							ratioThreshold = 8.0 * ( expandedIntegralSizeInCharCount + s.length() ) / compressedIntegralSizeInBits / 1.1;  // changed during debug #2
							hasMeasuredRatioThreshold = true;
							st[i++] = val + s.charAt( 0 );  //
						}
						else if ( 8.0 * ( expandedIntegralSizeInCharCount + s.length() ) / compressedIntegralSizeInBits < ratioThreshold )  //  changed during debug #2
						{
							st = new String[Lmax];

							// initialize symbol table with all 1-character strings
							for ( i = 0; i < R; i++ )
							{
								st[i] = "" + ( char ) i;
							}

							st[i++] = "";                        // (unused) lookahead for EOF
							W = 9;
							L = 1 << W;
							//st[i++] = val + s.charAt(0);  
						}
						else
						{
							st[i++] = val + s.charAt( 0 );    
						}

						break;
				}
			}
			else if ( i < Lmax )
			{
				if ( i >= ( L - 1 ) )
				{
					W++;
					L *= 2;
				}

				st[i++] = val + s.charAt( 0 );
			}
			else if ( M == 2 && 8.0 * ( expandedIntegralSizeInCharCount + s.length() ) / compressedIntegralSizeInBits < ratioThreshold ) // if (i==Lmax...), for reset triggered by comp ratio ratio, added during debug #3
			{
				st = new String[Lmax];

				// initialize symbol table with all 1-character strings
				for ( i = 0; i < R; i++ )
				{
					st[i] = "" + ( char ) i;
				}

				st[i++] = "";                        // (unused) lookahead for EOF
				W = 9;
				L = 1 << W;
				//st[i++] = val + s.charAt(0);  
			}

			val = s;
		}

		BinaryStdOut.close();
	}



	public static void main( String[] args )
	{
		if ( args[0].equals( "-" ) )
		{
			if ( args[1].equals( "n" ) )
			{
				M = 0;
			}
			else if ( args[1].equals( "r" ) )
			{
				M = 1;
			}
			else if ( args[1].equals( "m" ) )
			{
				M = 2;
			}
			else
			{
				throw new IllegalArgumentException( "Illegal command line argument" );
			}

			compress();
		}
		else if ( args[0].equals( "+" ) )
		{
			expand();
		}
		else
		{
			throw new IllegalArgumentException( "Illegal command line argument" );
		}
	}

}
