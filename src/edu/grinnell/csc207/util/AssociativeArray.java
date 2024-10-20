package edu.grinnell.csc207.util;

import static java.lang.reflect.Array.newInstance;

/**
 * A basic implementation of Associative Arrays with keys of type K
 * and values of type V. Associative Arrays store key/value pairs
 * and permit you to look up values by key.
 *
 * @param <K> the key type
 * @param <V> the value type
 *
 * @author Richard Lin
 * @author Samuel A. Rebelsky
 */
public class AssociativeArray<K, V> {
  // +-----------+---------------------------------------------------
  // | Constants |
  // +-----------+

  /**
   * The default capacity of the initial array.
   */
  static final int DEFAULT_CAPACITY = 16;

  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The size of the associative array (the number of key/value pairs).
   */
  int size;

  /**
   * The array of key/value pairs.
   */
  KVPair<K, V> pairs[];

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new, empty associative array.
   */
  @SuppressWarnings({ "unchecked" })
  public AssociativeArray() {
    // Creating new arrays is sometimes a PITN.
    this.pairs = (KVPair<K, V>[]) newInstance((new KVPair<K, V>()).getClass(),
        DEFAULT_CAPACITY);
    this.size = 0;
  } // AssociativeArray()

  // +------------------+--------------------------------------------
  // | Standard Methods |
  // +------------------+

  /**
   * Create a copy of this AssociativeArray.
   *
   * @return a new copy of the array
   */
  public AssociativeArray<K, V> clone() {

    AssociativeArray<K, V> copy = new AssociativeArray<K, V>();

    for (int i = 0; i < this.size; i++) {
      KVPair<K, V> copyPair = this.pairs[i].clone();
      try {
        copy.set(copyPair.key, copyPair.val);
      } catch (NullKeyException e) { 
        // do nothing since won't happen as pairs in size should all be valid.
      } // try/catch
    } // for
    copy.size = this.size;
    return copy;
  } // clone()

  /**
   * Convert the array to a string.
   *
   * @return a string of the form "{Key0:Value0, Key1:Value1, ... KeyN:ValueN}"
   */
  public String toString() {
    String arr2Str = "{";

    for (int i = 0; i < this.size; i++) {
      arr2Str = arr2Str.concat(this.pairs[i].key + ":" + this.pairs[i].val + ", ");
    } // for

    // removes comma and space and replaces it with end curly bracket.
    arr2Str = arr2Str.substring(0, arr2Str.length() - 2).concat("}");

    return arr2Str;
  } // toString()

  // +----------------+----------------------------------------------
  // | Public Methods |
  // +----------------+

  /**
   * Set the value associated with key to value. Future calls to
   * get(key) will return value.
   *
   * @param K
   *   The key whose value we are seeting.
   * @param V
   *   The value of that key.
   *
   * @throws NullKeyException
   *   If the client provides a null key.
   */
  public void set(K key, V value) throws NullKeyException {

    // Check if the key is null
    if (key == null) {
      throw (new NullKeyException());
    } // if

    try {
      int keyIndex = find(key);

      // keyIndex exists since exception not thrown to run lines below.
      this.pairs[keyIndex].val = value;

    } catch (Exception e) {

      // Expands array if there is no more space for entries.
      if (this.pairs.length == this.size) {
        this.expand();
      } // if
      this.pairs[size] = new KVPair<K,V>(key, value);
      this.size++;
    } // try/catch
  } // set(K,V)

  /**
   * Get the value associated with key.
   *
   * @param key
   *   A key
   *
   * @throws KeyNotFoundException
   *   when the key is null or does not appear in the associative array.
   */
  public V get(K key) throws KeyNotFoundException {

    // Checks if the key exists in the array.
    if (hasKey(key) == false) {
      throw (new KeyNotFoundException());
    } // if

    // (hasKey(key)) == true, i.e. key exists.
    int keyIndex = find(key);

    return this.pairs[keyIndex].val;
  } // get(K)

  /**
   * Determine if key appears in the associative array. Should
   * return false for the null key.
   */
  public boolean hasKey(K key) {
    try{
      // if key exists
      if (find(key) != -1) {
        return true;
      } // if
    } catch (Exception e) {
      // key does not exist
      return false;
    } // try/catch

    return false;
  } // hasKey(K)

  /**
   * Remove the key/value pair associated with a key. Future calls
   * to get(key) will throw an exception. If the key does not appear
   * in the associative array, does nothing.
   */
  public void remove(K key) {

    try {
      int keyIndex = find(key);

      // key exists if no exception thrown.
      this.pairs[keyIndex] = this.pairs[size - 1];
      this.pairs[size - 1] = null;
      size--;
      
    } catch (Exception e){
      // do nothing if key does not exist.
    } // try/catch
  } // remove(K)

  /**
   * Determine how many key/value pairs are in the associative array.
   */
  public int size() {
    return this.size;
  } // size()

  // +-----------------+---------------------------------------------
  // | Private Methods |
  // +-----------------+

  /**
   * Expand the underlying array.
   */
  void expand() {
    int newExpandedLen = this.pairs.length * 2;
    if (this.pairs.length == 0){
      newExpandedLen = 1;
    }
    this.pairs = java.util.Arrays.copyOf(this.pairs, newExpandedLen);
  } // expand()

  /**
   * Find the index of the first entry in `pairs` that contains key.
   * If no such entry is found, throws an exception.
   *
   * @param key
   *   The key of the entry.
   *
   * @throws KeyNotFoundException
   *   If the key does not appear in the associative array.
   */
  int find(K key) throws KeyNotFoundException {
    for (int i = 0; i < this.size; i++) {
      if (this.pairs[i].key.equals(key)) {
        return i;
      } // if
    } // for
    throw new KeyNotFoundException();
  } // find(K)

} // class AssociativeArray
