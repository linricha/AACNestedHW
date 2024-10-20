import java.util.NoSuchElementException;
import edu.grinnell.csc207.util.AssociativeArray;

/**
 * Represents the mappings for a single category of items that should
 * be displayed
 * 
 * @author Catie Baker & Richard Lin
 *
 */
public class AACCategory implements AACPage {

	/**
	 * Name of category
	 */
	String name;

	/**
	 * Image Locations and image names stored here.
	 */
	AssociativeArray<String, String> storage;
	
	/**
	 * Stores string Locations with standard array index.
	 */
	AssociativeArray<Integer, String> stringLoc;

	
	/**
	 * Creates a new empty category with the given name
	 * @param name the name of the category
	 */
	public AACCategory(String name) {
		this.name = name;
		this.storage = new AssociativeArray<String, String>();
		this.stringLoc = new AssociativeArray<Integer, String>();
	}
	
	/**
	 * Adds the image location, text pairing to the category
	 * @param imageLoc the location of the image
	 * @param text the text that image should speak
	 */
	public void addItem(String imageLoc, String text) {
		try { 
			this.storage.set(imageLoc, text);
			this.stringLoc.set(this.stringLoc.size(), imageLoc);
		} catch (Exception e) {
			// invalid parameters
		} // try/catch
	} // addItem(String, String)

	/**
	 * Returns an array of all the images in the category
	 * @return the array of image locations; if there are no images,
	 * it should return an empty array
	 */
	public String[] getImageLocs() {

		String[] strArr = new String[this.storage.size()];

		for (int i = 0; i < this.stringLoc.size(); i++) {
			try {
				strArr[i] = this.stringLoc.get(i);
			} catch (Exception e) {
				// do nothing since key in bounds
			}
		} // for
	
		return strArr;
	} // getImageLocs()

	/**
	 * Returns the name of the category
	 * @return the name of the category
	 */
	public String getCategory() {
		return this.name;
	}

	/**
	 * Returns the text associated with the given image in this category
	 * @param imageLoc the location of the image
	 * @return the text associated with the image
	 * @throws NoSuchElementException if the image provided is not in the current
	 * 		   category
	 */
	public String select(String imageLoc) throws NoSuchElementException{
		
		try {
			return this.storage.get(imageLoc);
		} catch (Exception e) {
			throw new NoSuchElementException();
		} // try/catch
	} // select(String)

	/**
	 * Determines if the provided images is stored in the category
	 * @param imageLoc the location of the category
	 * @return true if it is in the category, false otherwise
	 */
	public boolean hasImage(String imageLoc) {
		return this.storage.hasKey(imageLoc);
	} // hasImage(String)
} // class AACCategory
