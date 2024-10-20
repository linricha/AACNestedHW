import java.util.Scanner;
import de.dfki.lt.freetts.mbrola.ParametersToMbrolaConverter;
import java.io.File;
import java.io.FileWriter;

import java.util.NoSuchElementException;
import edu.grinnell.csc207.util.AssociativeArray;

/**
 * Creates a set of mappings of an AAC that has two levels,
 * one for categories and then within each category, it has
 * images that have associated text to be spoken. This class
 * provides the methods for interacting with the categories
 * and updating the set of images that would be shown and handling
 * an interactions.
 * 
 * @author Catie Baker & Richard Lin
 *
 */
public class AACMappings implements AACPage {

	/**
	 * Represents the index at which the toplevel can be accessed.
	 */
	public static final int TOPLEVEL = 0;

	/**
	 * Represents the index at which the category level can be accessed.
	 */
	public static final int CATEGORYLEVEL = 1;

	/**
	 * Stores image and imageLocations.
	 */
	AssociativeArray<String, AACCategory> table;

	/**
	 * Level in table (either on AssociativeArray of table or AACCategory)
	 */
	int arrayLevel;

	/**
	 * Stores the current AACCategory.
	 */
	AACCategory currentCategory;
	
	/**
	 * Stores the topLevelImageLocs with regular array indexing.
	 */
	AssociativeArray<Integer, String> topLevelImageLocs;
	
	/**
	 * Creates a set of mappings for the AAC based on the provided
	 * file. The file is read in to create categories and fill each
	 * of the categories with initial items. The file is formatted as
	 * the text location of the category followed by the text name of the
	 * category and then one line per item in the category that starts with
	 * > and then has the file name and text of that image
	 * 
	 * for instance:
	 * img/food/plate.png food
	 * >img/food/icons8-french-fries-96.png french fries
	 * >img/food/icons8-watermelon-96.png watermelon
	 * img/clothing/hanger.png clothing
	 * >img/clothing/collaredshirt.png collared shirt
	 * 
	 * represents the file with two categories, food and clothing
	 * and food has french fries and watermelon and clothing has a 
	 * collared shirt
	 * @param filename the name of the file that stores the mapping information
	 */
	public AACMappings(String filename) {
		table = new AssociativeArray<String, AACCategory>();
		topLevelImageLocs = new AssociativeArray<Integer, String>();
		arrayLevel = TOPLEVEL;
		currentCategory = null; // default category

		File toRead = new File(filename);

		// Assuming that file format is correct

		// Tokenized Array of Strings of one line
		String[] lineRead;

		// Contains Category
		String topicPlaceholder = "";

		// Contains rest of the line after the first part
		String totalWord = "";

		// Used to read the next line
		Scanner readNextLine = null;

		try {
			// Used to read from file
			Scanner reading = new Scanner(toRead);
			while (reading.hasNextLine()) {

				// Read one line at a time
				readNextLine = new Scanner(reading.nextLine());

				// array of strings of one line
				lineRead = readNextLine.tokens().toArray(String[] :: new);


				totalWord = lineRead[1];

				for (int i = 2; i < lineRead.length; i++) {
					totalWord = totalWord + " " + lineRead[i];
				} // for

				if ((lineRead[0].substring(0, 1).compareTo(">") != 0)){
					AACCategory topic = new AACCategory(totalWord);
					this.table.set(lineRead[0], topic);
					this.topLevelImageLocs.set(this.topLevelImageLocs.size(), lineRead[0]);

					topicPlaceholder = lineRead[0]; 

				} else {
					this.table.get(topicPlaceholder).addItem(lineRead[0].substring(1), totalWord);
				} // if/elseif
			} // while
			reading.close();
			readNextLine.close();
		} catch (Exception e) {
			// file not found
		} // try/catch
	} // AACMappings(String)
	
	/**
	 * Given the image location selected, it determines the action to be
	 * taken. This can be updating the information that should be displayed
	 * or returning text to be spoken. If the image provided is a category, 
	 * it updates the AAC's current category to be the category associated 
	 * with that image and returns the empty string. If the AAC is currently
	 * in a category and the image provided is in that category, it returns
	 * the text to be spoken.
	 * @param imageLoc the location where the image is stored
	 * @return if there is text to be spoken, it returns that information, otherwise
	 * it returns the empty string
	 * @throws NoSuchElementException if the image provided is not in the current 
	 * category
	 */
	public String select(String imageLoc) throws NoSuchElementException {

		try {
			if (arrayLevel == TOPLEVEL) {
				currentCategory = this.table.get(imageLoc);
				arrayLevel = CATEGORYLEVEL;
				return "";
			} else if (arrayLevel == CATEGORYLEVEL) {
				return currentCategory.select(imageLoc);
			} // if/else-if
		} catch (Exception e) { 
			throw new NoSuchElementException();
		} // try/catch
		return null;
	} // select(String)
	
	/**
	 * Provides an array of all the images in the current category
	 * @return the array of images in the current category; if there are no images,
	 * it should return an empty array
	 */
	public String[] getImageLocs() {

		String[] strArr;

		if (arrayLevel == TOPLEVEL) {
			strArr = new String[this.table.size()];

			for (int i = 0; i < this.topLevelImageLocs.size(); i ++) {
				try {
					strArr[i] = this.topLevelImageLocs.get(i);
				} catch (Exception e) {
					// do nothing since index in bounds
				} // try/catch
			} // for
			return strArr;
		}	else {
			try {
				return this.currentCategory.getImageLocs();
			} catch (Exception e) {
				return null;
			} // try/catch
		} // if/else
	} // getImageLocs

	/**
	 * Resets the current category of the AAC back to the default
	 * category
	 */
	public void reset() {
		arrayLevel = TOPLEVEL;
		currentCategory = null;
	} // reset()
	
	
	/**
	 * Writes the ACC mappings stored to a file. The file is formatted as
	 * the text location of the category followed by the text name of the
	 * category and then one line per item in the category that starts with
	 * > and then has the file name and text of that image
	 * 
	 * for instance:
	 * img/food/plate.png food
	 * >img/food/icons8-french-fries-96.png french fries
	 * >img/food/icons8-watermelon-96.png watermelon
	 * img/clothing/hanger.png clothing
	 * >img/clothing/collaredshirt.png collared shirt
	 * 
	 * represents the file with two categories, food and clothing
	 * and food has french fries and watermelon and clothing has a 
	 * collared shirt
	 * 
	 * @param filename the name of the file to write the
	 * AAC mapping to
	 */
	public void writeToFile(String filename) {
		File writeIn = new File(filename);
		try {
			FileWriter writing = new FileWriter(writeIn);
			String[] topLevelStrings = this.getImageLocs();
			String[] currentCategoryString = null;
			AACCategory currentAACC = null;

			for (int i = 0; i < topLevelStrings.length; i++){ // loop through png on the top
				writing.write(topLevelStrings[i] + " " + this.table.get(topLevelStrings[i]).getCategory() + "\n");
				currentAACC = this.table.get(topLevelStrings[i]);
				currentCategoryString = currentAACC.getImageLocs();


				for (int j = 0; j < currentCategoryString.length; j++) { // loop through png inside category
					writing.write(">" + currentCategoryString[j] + " " + currentAACC.select(currentCategoryString[j]) + "\n");
				} // for
			} // for
			writing.close();
			
		} catch (Exception e) {
			return; // file not found.
		} // try/catch
	} // writeToFile(String)
	
	/**
	 * Adds the mapping to the current category (or the default category if
	 * that is the current category)
	 * @param imageLoc the location of the image
	 * @param text the text associated with the image
	 */
	public void addItem(String imageLoc, String text) {
		try {
			if (arrayLevel == TOPLEVEL) {
				this.table.set(imageLoc, new AACCategory(text));
				this.topLevelImageLocs.set(this.topLevelImageLocs.size(), imageLoc);
			} else if (arrayLevel == CATEGORYLEVEL) {
				currentCategory.addItem(imageLoc, text);
			} // if/elseif
		} catch (Exception e) {
			// do nothing if fail --> imageLoc is null.
		} // try/catch
	} // addItem(String, String)


	/**
	 * Gets the name of the current category
	 * @return returns the current category or the empty string if 
	 * on the default category
	 */
	public String getCategory() {
		if (currentCategory == null){
			return "";
		} // if
		return currentCategory.getCategory();
	} // getCategory()


	/**
	 * Determines if the provided image is in the set of images that
	 * can be displayed and false otherwise
	 * @param imageLoc the location of the category
	 * @return true if it is in the set of images that
	 * can be displayed, false otherwise
	 */
	public boolean hasImage(String imageLoc) {
		if (currentCategory.equals(null)){
			return this.table.hasKey(imageLoc);
		} else {
			return this.currentCategory.hasImage(imageLoc);
		} // if/else
	} // hasImage(String)
} // class AACMappings
