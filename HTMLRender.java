import java.util.Scanner;
/**
 *	HTMLRender
 *	This program renders HTML code into a JFrame window.
 *	It requires your HTMLUtilities class and
 *	the SimpleHtmlRenderer and HtmlPrinter classes.
 *
 *	The tags supported:
 *		<html>, </html> - start/end of the HTML file
 *		<body>, </body> - start/end of the HTML code
 *		<p>, </p> - Start/end of a paragraph.
 *					Causes a newline before and a blank line after. Lines are restricted
 *					to 80 characters maximum.
 *		<hr>	- Creates a horizontal rule on the following line.
 *		<br>	- newline (break)
 *		<b>, </b> - Start/end of bold font print
 *		<i>, </i> - Start/end of italic font print
 *		<q>, </q> - Start/end of quotations
 *		<hX>, </hX> - Start/end of heading with size X = 1, 2, 3, 4, 5, 6
 *		<pre>, </pre> - Preformatted text
 *
 *	@author    Joshua Cao
 *	@since     9/15/2024
 */
public class HTMLRender {
	
	// the array holding all the tokens of the HTML file
	private String [] tokens;
	private final int TOKENS_SIZE = 10000;	// size of array
	
	// SimpleHtmlRenderer fields
	private SimpleHtmlRenderer render;
	private HtmlPrinter browser;
	private HTMLUtilities util;			// HTMLUtilities used in tester
	
	
	// NONE = not nested in a block, PARAGRAPH = inside a paragraph block,
	// BOLD = inside a bold block, ITALIC = inside an italic block,
	// QUOTE = inside a quote block, PREFORMAT = inside a pre-format block, 
	// HEAD1-6 = inside a heading block (from 1-6)
	private enum TokenState { NONE, PARAGRAPH, BOLD, ITALIC, QUOTE, PREFORMAT, HEAD1,HEAD2, HEAD3, HEAD4, HEAD5, HEAD6};

	// the current tokenizer state
	private TokenState state; 

	private int letCount;         // letter count of the line
		
	private String [] stateStack; // keep track of nested states
	
	private int stateCount;       // index of the state stack array
	
	
	public HTMLRender() {
		// Initialize token array
		tokens = new String[TOKENS_SIZE];
		
		// Initialize util
		util = new HTMLUtilities();

		// Initialize Simple Browser
		render = new SimpleHtmlRenderer();
		browser = render.getHtmlPrinter();
		
		//Initialize counts, arrays, and states
		letCount = 0;
		state = state.NONE;		
		stateStack = new String[10000];
		stateCount = 0;
	}
	
	
	public static void main(String[] args) {
		HTMLRender hf = new HTMLRender();
		hf.run(args);
	}
	
	public void run(String [] args) {
		Scanner input = null;
		String fileName = "";
		// if the command line contains the file name, then store it
		if (args.length > 0)
			fileName = args[0];
		// otherwise print out usage message
		else {
			System.out.println("Usage: java HTMLTester <htmlFileName>");
			System.exit(0);
		}
		
		// Open the HTML file
		input = FileUtils.openToRead(fileName);
		
		// Read each line of the HTML file, tokenize, then print tokens
		while (input.hasNext()) {
			String line = input.nextLine();
			System.out.println("\n" + line);
			String [] tokens = util.tokenizeHTMLString(line);
			util.printTokens(tokens);
			renderTokens(tokens);
		}
		input.close();
	}
	
	/**
	 *	This method renders all the tokens created in HTMLUtilities onto
	 *  a new window. It identifies each tag and prints out
	 *  the according print style to the screen (with the nested regular
	 *  text inside it). It does this line by line and keeps track of being
	 *  in a tag or in nested tags even if they are on seperate lines.
	 *	@param tokensIn	  array of tokens in that line
	 */
	public void renderTokens(String [] tokensIn)
	{
		int i = 0;
		// Loops through each token of the current line
		while(i < tokensIn.length)
		{
			switch(state)
			{
				case NONE:
					// Ignores html, body tag
					if(!(tokensIn[i].equalsIgnoreCase("<html>")) && !(tokensIn[i].equalsIgnoreCase("</html>"))
						&& !(tokensIn[i].equalsIgnoreCase("<body>")) && !(tokensIn[i].equalsIgnoreCase("</body>")))
					{
						// If the current token is a paragraph tag,
						// enter the paragraph state
						if(tokensIn[i].equalsIgnoreCase("<p>"))
						{
							state = state.PARAGRAPH;
							browser.println();
							browser.println();
							letCount = 0;
							stateStack[stateCount] = "PARAGRAPH";
							stateCount++;
						}
						// If the current token is a preformat tag,
						// enter the preformat state
						else if(tokensIn[i].equalsIgnoreCase("<pre>"))
						{
							state = state.PREFORMAT;
							browser.println();
							browser.println();
						}
						// If the current token is one of the heading tags (1-6),
						// enter the the corresponding heading state 
						else if(tokensIn[i].equalsIgnoreCase("<h1>"))
						{
							state = state.HEAD1;
							browser.println();
							browser.println();
							letCount = 0;
						}
						else if(tokensIn[i].equalsIgnoreCase("<h2>"))
						{
							state = state.HEAD2;
							browser.println();
							browser.println();
							letCount = 0;
						}
						else if(tokensIn[i].equalsIgnoreCase("<h3>"))
						{
							state = state.HEAD3;
							browser.println();
							browser.println();
							letCount = 0;
						}
						else if(tokensIn[i].equalsIgnoreCase("<h4>"))
						{
							state = state.HEAD4;
							browser.println();
							browser.println();
							letCount = 0;
						}
						else if(tokensIn[i].equalsIgnoreCase("<h5>"))
						{
							state = state.HEAD5;
							browser.println();
							browser.println();
							letCount = 0;
						}
						else if(tokensIn[i].equalsIgnoreCase("<h6>"))
						{
							state = state.HEAD6;
							browser.println();
							browser.println();
							letCount = 0;
						}
						// If the current token is a quote tag,
						// enter the quote state
						else if(tokensIn[i].equalsIgnoreCase("<q>"))
						{
							state = state.QUOTE;
							browser.print("\"");

						}
						// If the current token is a horizontal rule tag,
						// print a horizontal rule
						else if(tokensIn[i].equalsIgnoreCase("<hr>"))
						{
							   browser.printHorizontalRule();
							   letCount = 0;
						}
						// If the current token is a line break tag,
						// print out a line break
						else if(tokensIn[i].equalsIgnoreCase("<br>"))
						{
							browser.printBreak();
							letCount = 0;
						}
						// If the current token is a bold tag,
						// enter the bold state
						else if(tokensIn[i].equalsIgnoreCase("<b>"))
						{
							
							 state = state.BOLD;
						}
						else if(tokensIn[i].equalsIgnoreCase("<i>"))
						{		
							  state = state.ITALIC;
						}
						else
						
						{
							String value = tokensIn[i];
							// If the word after isn't punctuation, then add a space after the word
							if (!((i + 1 < tokensIn.length) && isPunctuation(tokensIn[i + 1]))) 
							{
								value += " ";
								letCount++;
							}
								browser.print(value);
								letCount = letCount + tokensIn[i].length();
							// If the next word exceeds the 80 character limit, go to the next line;
							if( i < tokensIn.length - 1 && letCount + tokensIn[i + 1].length() > 80 )
							{
								browser.println();
								letCount = 0;
							}
							
						}
					}
				break;
				case PARAGRAPH:
					// Once corresponding paragraph tag is found, 
					// print out a blank line and the state returns to NONE
					if(tokensIn[i].equalsIgnoreCase("</p>"))
					{
						stateStack[stateCount] = "PARAFOUND";
						stateCount++;
						browser.println();
						browser.println();
						letCount = 0;
						state = state.NONE;
					}
					else
					{
						// Ignores html, body tag
						if(!(tokensIn[i].equalsIgnoreCase("<html>")) && !(tokensIn[i].equalsIgnoreCase("</html>"))
							&& !(tokensIn[i].equalsIgnoreCase("<body>")) && !(tokensIn[i].equalsIgnoreCase("</body>")))
						{
							// If the current token is a horizontal rule tag,
							// print a horizontal rule
							if(tokensIn[i].equalsIgnoreCase("<hr>"))
							{
							   browser.printHorizontalRule();
							}
							// If the current token is a line break tag,
							// print out a line break
							else if(tokensIn[i].equalsIgnoreCase("<br>"))
							{
								browser.printBreak();
								letCount = 0;
							}
							// If the current token is a bold tag,
							// enter the bold state
								else if(tokensIn[i].equalsIgnoreCase("<b>"))
							{
							   state = state.BOLD;
							}
							// If the current token is an italic tag,
							// enter the italic state
							else if(tokensIn[i].equalsIgnoreCase("<i>"))
							{
							   state = state.ITALIC;
							}
							else
						
							{
								String value = tokensIn[i];
								// If the word after isn't punctuation, then add a space after the word
								if (!((i + 1 < tokensIn.length) && isPunctuation(tokensIn[i + 1]))) 
								{
									value += " ";
									letCount++;
								}
								browser.print(value);
								letCount = letCount + tokensIn[i].length();
								// If the next word exceeds the 80 character limit, go to the next line;
								if( i < tokensIn.length - 1 && letCount + tokensIn[i + 1].length() > 80 )
								{
									browser.println();
									letCount = 0;
								}
							}
						}
					}
				break;
				case BOLD:
					// Once corresponding bold tag is found
					if(tokensIn[i].equalsIgnoreCase("</b>"))
					{
						//browser.print(tokensIn[i]);
						boolean paragraph = true;
						String states = "";
						
						//Checks if the end tag is nested
						for(int k = 0; k < stateStack.length; k++)
						{
							if(stateStack[k] != null)
							states += stateStack[k];				
						}
						if(states.lastIndexOf("PARAGRAPH") == -1)
							paragraph = true;
						else if(states.lastIndexOf("PARAGRAPH") > states.lastIndexOf("PARAFOUND"))
							paragraph = false;
							
						if(!paragraph)
							state = state.PARAGRAPH;
						else
							state = state.NONE;
					}
					else
					{
						// Ignores html, body tag
						if(!(tokensIn[i].equalsIgnoreCase("<html>")) && !(tokensIn[i].equalsIgnoreCase("</html>"))
							&& !(tokensIn[i].equalsIgnoreCase("<body>")) && !(tokensIn[i].equalsIgnoreCase("</body>")))
						{
							// If the current token is a horizontal rule tag,
							// print a horizontal rule
							if(tokensIn[i].equalsIgnoreCase("<hr>"))
							{
							   browser.printHorizontalRule();
							}
							// If the current token is a line break tag,
							// print out a line break
							else if(tokensIn[i].equalsIgnoreCase("<br>"))
							{
								browser.print("<br>");
								browser.printBreak();
								letCount = 0;
							}
							else
						
							{
								String value = tokensIn[i];
								// If the word after isn't punctuation, then add a space after the word
								if (!((i + 1 < tokensIn.length) && isPunctuation(tokensIn[i + 1]))) 
								{
									value += " ";
									letCount++;
								}
								browser.printBold(value);
								letCount = letCount + tokensIn[i].length();
								// If the next word exceeds the 80 character limit, go to the next line;
								if( i < tokensIn.length - 1 && letCount + tokensIn[i + 1].length() > 80 )
								{
									browser.println();
									letCount = 0;
								}
							}
						}
					}
				break;
				case ITALIC:
				// Once corresponding italic tag is found
				if(tokensIn[i].equalsIgnoreCase("</i>"))
					{
						//browser.print(tokensIn[i]);
						boolean paragraph = true;
						String states = "";
						
						//Checks if the end tag is nested
						for(int k = 0; k < stateStack.length; k++)
						{
							if(stateStack[k] != null)
							states += stateStack[k];				
						}
						if(states.lastIndexOf("PARAGRAPH") == -1)
							paragraph = true;
						else if(states.lastIndexOf("PARAGRAPH") > states.lastIndexOf("PARAFOUND"))
							paragraph = false;	
									
						if(!paragraph)
						state = state.PARAGRAPH;
						else
						state = state.NONE;
					}
					else
					{
						// Ignores html, body tag
						if(!(tokensIn[i].equalsIgnoreCase("<html>")) && !(tokensIn[i].equalsIgnoreCase("</html>"))
							&& !(tokensIn[i].equalsIgnoreCase("<body>")) && !(tokensIn[i].equalsIgnoreCase("</body>")))
						{
							// If the current token is a horizontal rule tag,
							// print a horizontal rule
							if(tokensIn[i].equalsIgnoreCase("<hr>"))
							{
							   browser.printHorizontalRule();
							}
							// If the current token is a line break tag,
							// print out a line break
							else if(tokensIn[i].equalsIgnoreCase("<br>"))
							{
								browser.printBreak();
								letCount = 0;
							}
							else	
							{
								String value = tokensIn[i];
								// If the word after isn't punctuation, then add a space after the word
								if (!((i + 1 < tokensIn.length) && isPunctuation(tokensIn[i + 1]))) 
								{
									value += " ";
									letCount++;
								}
								browser.printItalic(value);
								letCount = letCount + tokensIn[i].length(); 
								// If the next word exceeds the 80 character limit, go to the next line
								if( i < tokensIn.length - 1 && letCount + tokensIn[i + 1].length() > 80 )
								{
									browser.println();
									letCount = 0;
								}
							}
						}
					}
				break;
				case QUOTE:
					// Once corresponding quote tag is found
					if(tokensIn[i].equalsIgnoreCase("</q>"))
					{
						browser.print("\" ");	
						state = state.NONE;
					}
					else
					{
						// Ignores html, body tag
						if(!(tokensIn[i].equalsIgnoreCase("<html>")) && !(tokensIn[i].equalsIgnoreCase("</html>"))
							&& !(tokensIn[i].equalsIgnoreCase("<body>")) && !(tokensIn[i].equalsIgnoreCase("</body>")))
						{
							// If the current token is a horizontal rule tag,
							// print a horizontal rule
							if(tokensIn[i].equalsIgnoreCase("<hr>"))
							{
							   browser.printHorizontalRule();
							}
							// If the current token is a line break tag,
							// print out a line break
							else if(tokensIn[i].equalsIgnoreCase("<br>"))
							{
								browser.printBreak();
								letCount = 0;
							}
							else
							{
								String value = tokensIn[i];
								// If the word after isn't punctuation, then add a space after the word
								if (!((i + 1 < tokensIn.length) && isPunctuation(tokensIn[i + 1]))
								    && !(tokensIn[i + 1].equalsIgnoreCase("</q>"))) 
								{
									value += " ";
									letCount++;
								}	
								browser.print(value);
								letCount = letCount + tokensIn[i].length();
								// If the next word exceeds the 80 character limit, go to the next line
								if( i < tokensIn.length - 1 && letCount + tokensIn[i + 1].length() > 80 )
								{
									browser.println();
									letCount = 0;
								}
							}
						}
					}
				break;
				case PREFORMAT:
					// Once corresponding preformat tag is found
					if(tokensIn[i].equalsIgnoreCase("</pre>"))
					{
						state = state.NONE;
					}
					else
					{
						// Ignores html, body tag
						if(!(tokensIn[i].equalsIgnoreCase("<html>")) && !(tokensIn[i].equalsIgnoreCase("</html>"))
							&& !(tokensIn[i].equalsIgnoreCase("<body>")) && !(tokensIn[i].equalsIgnoreCase("</body>")))
						{
								String value = tokensIn[i];
								browser.printPreformattedText(value);
								letCount = letCount + tokensIn[i].length(); 
								browser.println();
								browser.println();
								letCount = 0;
							
						}
					}
				break;
				case HEAD1:
					// Processes token render for heading one to six
					processHeading("h1", tokensIn, i);
				break;
				case HEAD2:
					// Processes token render for heading two
					processHeading("h2", tokensIn, i);

				break;
				case HEAD3:
					// Processes token render for heading three
					processHeading("h3", tokensIn, i);

				break;
				case HEAD4:
					// Processes token render for heading four
					processHeading("h4", tokensIn, i);

				break;
				case HEAD5:
					// Processes token render for heading five
				    processHeading("h5", tokensIn, i);
				break;
				case HEAD6:
					// Processes token render for heading six
					processHeading("h6", tokensIn, i);
				break;
			}
			i++;
		}
	}
	
	/**
	 *	Processes the output for the heading tags. When in the current
	 *  heading state, words nested in the tags will be printed in the
	 *  corresponding heading style. Spaces will put in between each
	 *  token with the exception of punctuation. Word count is also kept
	 *  track of to make sure a line does not exceed 80 characters. Once
	 *  the corresponding ending heading tag is found, the word count is
	 *  reset and the state returns to NONE.
	 *	@param headingTag	the number of the type of heading tag
	 *  @param tokensIn     the array of tokens in the current line
	 *  @param i            current index of the tokensIn array
	 */
	public void processHeading(String headingTag, String[] tokensIn, int i)
	{
		int limit = lineLimit(headingTag);
		// Once corresponding heading tag is found, state returns to NONE
		if (tokensIn[i].equalsIgnoreCase("</" + headingTag + ">")) 
		{
			state = state.NONE;
			letCount = 0;
		}
        else 
        {	
			// Ignores html, body tag
			if(!(tokensIn[i].equalsIgnoreCase("<html>")) && !(tokensIn[i].equalsIgnoreCase("</html>"))
				&& !(tokensIn[i].equalsIgnoreCase("<body>")) && !(tokensIn[i].equalsIgnoreCase("</body>")))
            { 
				String value = tokensIn[i];
				// If the word after isn't punctuation, then add a space after the word
				if (!((i + 1 < tokensIn.length) && isPunctuation(tokensIn[i + 1]))) 
				{
					value += " ";
					letCount++;
				}
				printHeading(headingTag, value);
				letCount = letCount + tokensIn[i].length();
				
				// If the next word exceeds the 80 character limit, go to the next line;

				if( i < tokensIn.length - 1 && letCount + tokensIn[i + 1].length() > limit )
				{
					browser.println();
					letCount = 0;
				}
			}
		}
	}
	
	/**
	 *	Takes in the heading tag and calculates the printing style it
	 *  will use, along with what will be printed in that printing style.
	 *	@param headingTag	the number of the type of heading tag
	 *  @param content      what will be printed to the screen
	 */
	public void printHeading(String headingTag, String content) 
	{
		// Print statement corresponding to the header
		if (headingTag.equalsIgnoreCase("h1")) 
			browser.printHeading1(content);
		else if (headingTag.equalsIgnoreCase("h2")) 
			browser.printHeading2(content);
		else if (headingTag.equalsIgnoreCase("h3")) 
			browser.printHeading3(content);
		else if (headingTag.equalsIgnoreCase("h4")) 
			browser.printHeading4(content);
		else if (headingTag.equalsIgnoreCase("h5")) 
			browser.printHeading5(content);
		else if (headingTag.equalsIgnoreCase("h6")) 
			browser.printHeading6(content);
	}
	
	/**
	 *	Takes in the heading tag and calculates the the line limit
	 *  the heading tag has.
	 *	@param headingTag	the number of the type of heading tag
	 *  @return             the line limit
	 */
	public int lineLimit(String headingTag) 
	{
		// Return line limit
		if (headingTag.equalsIgnoreCase("h1")) 
			return 40;
		else if (headingTag.equalsIgnoreCase("h2")) 
			return 50;
		else if (headingTag.equalsIgnoreCase("h3")) 
			return 60;
		else if (headingTag.equalsIgnoreCase("h4")) 
			return 80;
		else if (headingTag.equalsIgnoreCase("h5")) 
			return 100;
		else if (headingTag.equalsIgnoreCase("h6")) 
			return 120;
		return -1;
	}

	/**
	 *	Deals with determining if the current character counts as 
	 *  punctuation or not.
	 *	@param letterIn			the current charactter
	 */
	public boolean isPunctuation(String letterIn)
	{
		
		// Return true if the letter is a punctuation
		if(letterIn.equalsIgnoreCase(".") || letterIn.equalsIgnoreCase(",") || letterIn.equalsIgnoreCase("~") 
		   || letterIn.equalsIgnoreCase("(") || letterIn.equalsIgnoreCase(")") || letterIn.equalsIgnoreCase("!") 
		   || letterIn.equalsIgnoreCase("?") || letterIn.equalsIgnoreCase("=") || letterIn.equalsIgnoreCase(";") 
		   || letterIn.equalsIgnoreCase("&") || letterIn.equalsIgnoreCase("-") || letterIn.equalsIgnoreCase("+") 
		   || letterIn.equalsIgnoreCase(":"))
			return true;
		else
			return false;
	}
}
