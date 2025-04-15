/**
 *  HTMLUtilities.java
 *	This program handles HTML by tokenizing several aspects of an HTML.
 *  file. This includes HTML tags, letters, punctuation, and numbers. 
 *  Tokenizing is done by looking each line character by character,
 *  each line is tokenized seperately so the number of tokens resets
 *  each line. Additionally, comments are ignored as tokens, and 
 *  preformatted text is tokenized as is.
 *
 *	@author	 Joshua Cao
 *	@since	 11/13/24
 */
public class HTMLUtilities {

	// NONE = not nested in a block, COMMENT = inside a comment block
	// PREFORMAT = inside a pre-format block
	private enum TokenState { NONE, COMMENT, PREFORMAT };

	// the current tokenizer state
	private TokenState state; 

	public HTMLUtilities()
	{
		state = state.NONE;
	}

	/**
	 *	Break the HTML string into tokens. The array returned is
	 *	exactly the size of the number of tokens in the HTML string.
	 *	Example:	HTML string = "Goodnight moon goodnight stars"
	 *				returns { "Goodnight", "moon", "goodnight", "stars" }
	 *	@param str			the HTML string
	 *	@return				the String array of tokens
	 */
	public String[] tokenizeHTMLString(String str) 
	{
		String[] result = new String[10000]; // large array to hold the HTML file
		int counter = 0;     // number of tokens
		String wordFormat = "";    // preformatted text per each line

		// Loops through the entire HTML file and checks character by character
		for(int i = 0; i < str.length(); i++)
		{	
			char letter = str.charAt(i);   // current letter being checked
			char letterBefore = '\0';      // previous letter being checked
			char letterAfter = '\0';       // next letter being checked

			// Ensures there is only intialzation of the before and after
			// letters if we are not checking the first or last letter
			// to prvent an IndexOutOfBoundsExeception.
			if(i != 0)
				letterBefore = str.charAt(i - 1);
			if(i != str.length() - 1)
				letterAfter = str.charAt(i + 1);
			
			// Switch statement to control the current state of the tokenization
			// process, whether if we are in a comment or preformat block
			switch(state)
			{
				case NONE:
				
					// Set the state to comment if there is the start to
					// the comment tag
					if(letter == '<' && letterAfter == '!' && str.charAt(i + 2) == '-' 
					   && str.charAt(i + 3) == '-')
					{
						state = state.COMMENT;
					}
					
					// Set the state to preformat and tokenize it if there
					// is the start to the preformat tag
					else if(letter == '<' && letterAfter == 'p' && str.charAt(i + 2) == 'r' 
					        && str.charAt(i + 3) == 'e'  && str.charAt(i + 4) == '>')
					{
						state = state.PREFORMAT;
						result[i] = "<pre>";
						counter++;
						i += 4;
					} 
					
					else
					{
						// Tokenize the HTML tags
						if(letter == '<')
						{
							int recentIndex = i; // the current character
							int endIndex = str.indexOf(">", i); // where the tag ends
							
							// Take the substring of the start of the 
							// current character to the end of the HTML tag
							result[i] = str.substring(recentIndex, endIndex + 1);
							i += endIndex - recentIndex;
							counter++;
						}
						
						// Tokenize if there if there is a space between 
						// a minus symbol and a number
						else if(letter == '-' && Character.isWhitespace(letterAfter) 
						        && Character.isDigit(str.charAt(i + 2)))
						{
							boolean found = true;  // end character found or not
							int recentIndex = i;   // the current character
							int endIndex = -1;     // where the number ends
							int j;
							int [] hyphen = new int[3]; // store count of hypen
							int [] e = new int[2]; // store count of e
							int index = 0; // index of hyphen
							int [] period = new int[2]; // store count of period
							
							// Loops at plus two of the current index until
							// the index of when the number ends is found
							for(j = i + 2; j < str.length() && found; j++)
							{
								
								 
								// If the character is no longer a digit, 
								// not a hyphen, 'e', or a period.		
								
								if(str.charAt(j) == '-' && hyphen[0] == -1 && hyphen[1] == -1)
								{
									endIndex = j;
									found = false;
								}
								if(str.charAt(j) == '-')
								{
									hyphen[index] = -1;
									index++;
								}
								if(str.charAt(j) == 'e' && e[0] == -1)
								{
									endIndex = j;
									found = false;
								}
								if(str.charAt(j) == 'e')
								{
									e[0] = -1;
								}
								 					
								if(str.charAt(j) == '.' && period[0] == -1)
								{
									endIndex = j;
									found = false;
								}
								if(str.charAt(j) == '.')
								{
									period[0] = -1;
								}
								
								if (!Character.isDigit(str.charAt(j)) && 
								    !(str.charAt(j) == '-') && !(str.charAt(j) == 'e') 
								    && !(str.charAt(j) == '.')) 
								{		
									endIndex = j;
									found = false;
								}
							}
							
							// If not found set the index to the end of the line
							if(endIndex == -1)
								endIndex =  j;
						
							result[i] = "-";
							result[i + 1] = str.substring(recentIndex + 2, endIndex);
							i += endIndex - recentIndex + 2;
							counter+=2;
						}
						
						// Tokenize numbers
						else if(Character.isDigit(letter) || letter == '-' 
						        && Character.isDigit(letterAfter))
						{
							boolean found = true;  // end character found or not
							int recentIndex = i;   // the current character
							int endIndex = -1;     // where the number end
							int j;
							int [] hyphen = new int[3]; // store count of hypen
							int [] e = new int[2]; // store count of e
							int index = 0; // index of hyphen
							int [] period = new int[2]; // store count of period
							
							
							// Loops at the current index until
							// the index of when the  number ends is found
							for(j = i; j < str.length() && found; j++)
							{
							
								// If the character is no longer a digit, 
								// not a hyphen, 'e', or a period	
								
								if(str.charAt(j) == '-' && hyphen[0] == -1 && hyphen[1] == -1)
								{
									endIndex = j;
									found = false;
								}
								if(str.charAt(j) == '-')
								{
									hyphen[index] = -1;
									index++;
								}
								if(str.charAt(j) == 'e' && e[0] == -1)
								{
									endIndex = j;
									found = false;
								}
								if(str.charAt(j) == 'e')
								{
									e[0] = -1;
								}
														
								if(str.charAt(j) == '.' && period[0] == -1)
								{
									endIndex = j;
									found = false;
								}
								if(str.charAt(j) == '.')
								{
									period[0] = -1;
								}
								
								if (!Character.isDigit(str.charAt(j)) && 
								    !(str.charAt(j) == '-') && !(str.charAt(j) == 'e') 
								    && !(str.charAt(j) == '.')) 
								{		
									endIndex = j;
									found = false;
								}
								
						
							}
							
							// If not found set the index to the end of the line
							if(endIndex == -1)
							{
								endIndex =  j;
							}
							result[i] = str.substring(recentIndex, endIndex);
							i += endIndex - recentIndex - 1;
							counter++;
						}
						
						// Tokenize letters
						else if(Character.isLetter(letter))
						{
							boolean found = true;  // end character found or not
							int recentIndex = i;   // the current character
							int endIndex = -1;     // where the word end
							int j;
							
							// Loops at the current index until
							// the indnex of when the word ends is found
							for(j = i; j < str.length() && found; j++)
							{
								// If the character is no longer a letter,
								// and it is not a hyphen.
								if (!Character.isLetter(str.charAt(j)))
								{		
									if(str.charAt(j) == '-')
									{
										if(!Character.isLetter(str.charAt(j + 1)))
										{
											endIndex = j;
											found = false;
										}
									}
									else
									{
										endIndex = j;
										found = false;
									}
								}
							}
							
							// If not found set the index to the end of the line
							if(endIndex == -1)
								endIndex = j;

							result[i] = str.substring(recentIndex, endIndex);
							i += endIndex - recentIndex - 1;
							counter++;
						}
						
						// Tokenize punctuation
						else if(isPunctuation(letter))
						{
							result[i] = letter + "";
							counter++;
						}
					}
					break;

				case COMMENT:
					
					// Ignore and do not tokenize anything until
					// comment end tag is reached.
					if(letter == '-' && letterAfter == '-' && str.charAt(i + 2) == '>')
					{
						state = state.NONE;
						i += 2;
					}
					break;
					
				case PREFORMAT:
					
					// Tokenize each line as is until the pre end tag
					// is reached.
					if(letter == '<' && letterAfter == '/' && str.charAt(i + 2) == 'p' 
					   && str.charAt(i + 3) == 'r'  && str.charAt(i + 4) == 'e'
					   && str.charAt(i + 5) == '>')
					{
						state = state.NONE;
						result[i] = "</pre>";
						counter++;
						i += 5;
					}
					else
					{
						wordFormat += letter;
						
						// If the index is at the end of the line, tokenize
						if(i == str.length() - 1) 
						{
							result[i] = wordFormat;
							counter++;
						}
					} 	
					break;
				}		
		}
		
		// Put all tokenized words into the ans array
		String[] ans = new String[counter];
		int index = 0;
		for(int i = 0; i < result.length; i++)
		{
			if(result[i] != null)
			{
				ans[index] = result[i];
				index++;
			}
		}
		
		// return the correctly sized array
		return ans;
	}

	/**
	 *	Deals with determining if the current character counts as 
	 *  punctuation or not.
	 *	@param letterIn			the current charactter
	 */
	public boolean isPunctuation(char letterIn)
	{
		if(letterIn == '.' || letterIn == ',' || letterIn == '~' 
		   || letterIn == '(' || letterIn == ')' || letterIn == '!' 
		   || letterIn == '?' || letterIn == '=' || letterIn == ';' 
		   || letterIn == '&' || letterIn == '-' || letterIn == '+' 
		   || letterIn == ':')
			return true;
		else
			return false;
	}
	
	/**
	 *	Print the tokens in the array to the screen
	 *	Precondition: All elements in the array are valid String objects.
	 *				(no nulls)
	 *	@param tokens		an array of String tokens
	 */
	public void printTokens(String[] tokens) {
		if (tokens == null) return;
		for (int a = 0; a < tokens.length; a++) {

			if (a % 5 == 0) 
				System.out.print("\n  ");
			System.out.print("[token " + a + "]: " + tokens[a] + " ");
		}
		System.out.println();
	}

}
