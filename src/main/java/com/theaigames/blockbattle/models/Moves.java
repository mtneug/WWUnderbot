/*
 * Copyright (c) 2016. WWUnderbot team
 */

package com.theaigames.blockbattle.models;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * @author Alexander
 * @author Frederik
 * @author Marco
 * @author Matthias
 */
public class Moves implements List<MoveType>, Cloneable {
  public final Shape shape;
  public final Point originalLocation;
  public final int originalRotation;
  private ArrayList<MoveType> moves = new ArrayList<>();
  private boolean hasAppliedMoves = false;

  public Moves(final Shape shape) {
    this.shape = shape;
    this.originalLocation = shape.getLocation().clone();
    this.originalRotation = shape.getRotation();
  }

  public Moves(final Shape shape, final Collection<? extends MoveType> moves) {
    this(shape);
    this.moves.addAll(moves);
  }

  @Override
  @SuppressWarnings("unchecked")
  public Moves clone() {
    Moves cloned;
    try {
      cloned = (Moves) super.clone();
    } catch (CloneNotSupportedException e) {
      // this shouldn't happen, since we are Cloneable
      throw new InternalError(e);
    }
    cloned.moves = (ArrayList<MoveType>) moves.clone();
    return cloned;
  }

  public void reset() {
    shape.setLocation(originalLocation.clone());
    shape.setRotation(originalRotation);
    hasAppliedMoves = false;
  }

  public void checkAndApply() {
    checkAndApply(true);
  }

  public void checkAndApply(boolean shouldDrop) {
    if (hasAppliedMoves) return;

    for (int i = 0; i < moves.size(); i++) {
      switch (moves.get(i)) {
        case DROP:
          shape.drop();
          break;

        case DOWN:
          if (cannotPerformMove(shape.oneDown())) undoAndRemove(shape.oneUp(), i);
          break;

        case LEFT:
          if (cannotPerformMove(shape.oneLeft())) undoAndRemove(shape.oneRight(), i);
          break;

        case RIGHT:
          if (cannotPerformMove(shape.oneRight())) undoAndRemove(shape.oneLeft(), i);
          break;

        case TURNLEFT:
          if (cannotPerformMove(shape.turnLeft())) undoAndRemove(shape.turnRight(), i);
          break;

        case TURNRIGHT:
          if (cannotPerformMove(shape.turnRight())) undoAndRemove(shape.turnLeft(), i);
          break;
      }
    }

    if (shouldDrop) shape.drop();

    hasAppliedMoves = true;
  }

  private void undoAndRemove(Shape ignore, int i) {
    moves.remove(i);
  }

  private boolean cannotPerformMove(Shape shape) {
    return !(
        shape.getField().canBeAdded(shape) ||
            (shape.getLocation().y == -1
                && 0 <= shape.getShape()[shape.getEmptyCellsLeft()][0].getLocation().x
                && shape.getShape()[shape.getSize() - 1 - shape.getEmptyCellsRight()][0].getLocation().x < shape.getField().getWidth()
            )
    );
  }

  /**
   * Returns the number of elements in this list.  If this list contains
   * more than <tt>Integer.MAX_VALUE</tt> elements, returns
   * <tt>Integer.MAX_VALUE</tt>.
   *
   * @return the number of elements in this list
   */
  @Override
  public int size() {
    return moves.size();
  }

  /**
   * Returns <tt>true</tt> if this list contains no elements.
   *
   * @return <tt>true</tt> if this list contains no elements
   */
  @Override
  public boolean isEmpty() {
    return moves.isEmpty();
  }

  /**
   * Returns <tt>true</tt> if this list contains the specified element.
   * More formally, returns <tt>true</tt> if and only if this list contains
   * at least one element <tt>e</tt> such that
   * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>.
   *
   * @param o element whose presence in this list is to be tested
   * @return <tt>true</tt> if this list contains the specified element
   * @throws ClassCastException   if the type of the specified element
   *                              is incompatible with this list
   *                              (<a href="Collection.html#optional-restrictions">optional</a>)
   * @throws NullPointerException if the specified element is null and this
   *                              list does not permit null elements
   *                              (<a href="Collection.html#optional-restrictions">optional</a>)
   */
  @Override
  public boolean contains(Object o) {
    return moves.contains(o);
  }

  /**
   * Returns an iterator over the elements in this list in proper sequence.
   *
   * @return an iterator over the elements in this list in proper sequence
   */
  @Override
  public Iterator<MoveType> iterator() {
    return moves.iterator();
  }

  /**
   * Returns an array containing all of the elements in this list in proper
   * sequence (from first to last element).
   * <p>
   * <p>The returned array will be "safe" in that no references to it are
   * maintained by this list.  (In other words, this method must
   * allocate a new array even if this list is backed by an array).
   * The caller is thus free to modify the returned array.
   * <p>
   * <p>This method acts as bridge between array-based and collection-based
   * APIs.
   *
   * @return an array containing all of the elements in this list in proper
   * sequence
   * @see Arrays#asList(Object[])
   */
  @Override
  public Object[] toArray() {
    return moves.toArray();
  }

  /**
   * Returns an array containing all of the elements in this list in
   * proper sequence (from first to last element); the runtime type of
   * the returned array is that of the specified array.  If the list fits
   * in the specified array, it is returned therein.  Otherwise, a new
   * array is allocated with the runtime type of the specified array and
   * the size of this list.
   * <p>
   * <p>If the list fits in the specified array with room to spare (i.e.,
   * the array has more elements than the list), the element in the array
   * immediately following the end of the list is set to <tt>null</tt>.
   * (This is useful in determining the length of the list <i>only</i> if
   * the caller knows that the list does not contain any null elements.)
   * <p>
   * <p>Like the {@link #toArray()} method, this method acts as bridge between
   * array-based and collection-based APIs.  Further, this method allows
   * precise control over the runtime type of the output array, and may,
   * under certain circumstances, be used to save allocation costs.
   * <p>
   * <p>Suppose <tt>x</tt> is a list known to contain only strings.
   * The following code can be used to dump the list into a newly
   * allocated array of <tt>String</tt>:
   * <p>
   * <pre>{@code
   *     String[] y = x.toArray(new String[0]);
   * }</pre>
   * <p>
   * Note that <tt>toArray(new Object[0])</tt> is identical in function to
   * <tt>toArray()</tt>.
   *
   * @param a the array into which the elements of this list are to
   *          be stored, if it is big enough; otherwise, a new array of the
   *          same runtime type is allocated for this purpose.
   * @return an array containing the elements of this list
   * @throws ArrayStoreException  if the runtime type of the specified array
   *                              is not a supertype of the runtime type of every element in
   *                              this list
   * @throws NullPointerException if the specified array is null
   */
  @Override
  public <T> T[] toArray(T[] a) {
    return moves.toArray(a);
  }

  /**
   * Appends the specified element to the end of this list (optional
   * operation).
   * <p>
   * <p>Lists that support this operation may place limitations on what
   * elements may be added to this list.  In particular, some
   * lists will refuse to add null elements, and others will impose
   * restrictions on the type of elements that may be added.  List
   * classes should clearly specify in their documentation any restrictions
   * on what elements may be added.
   *
   * @param moveType element to be appended to this list
   * @return <tt>true</tt> (as specified by {@link Collection#add})
   * @throws UnsupportedOperationException if the <tt>add</tt> operation
   *                                       is not supported by this list
   * @throws ClassCastException            if the class of the specified element
   *                                       prevents it from being added to this list
   * @throws NullPointerException          if the specified element is null and this
   *                                       list does not permit null elements
   * @throws IllegalArgumentException      if some property of this element
   *                                       prevents it from being added to this list
   */
  @Override
  public boolean add(MoveType moveType) {
    return moves.add(moveType);
  }

  /**
   * Removes the first occurrence of the specified element from this list,
   * if it is present (optional operation).  If this list does not contain
   * the element, it is unchanged.  More formally, removes the element with
   * the lowest index <tt>i</tt> such that
   * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>
   * (if such an element exists).  Returns <tt>true</tt> if this list
   * contained the specified element (or equivalently, if this list changed
   * as a result of the call).
   *
   * @param o element to be removed from this list, if present
   * @return <tt>true</tt> if this list contained the specified element
   * @throws ClassCastException            if the type of the specified element
   *                                       is incompatible with this list
   *                                       (<a href="Collection.html#optional-restrictions">optional</a>)
   * @throws NullPointerException          if the specified element is null and this
   *                                       list does not permit null elements
   *                                       (<a href="Collection.html#optional-restrictions">optional</a>)
   * @throws UnsupportedOperationException if the <tt>remove</tt> operation
   *                                       is not supported by this list
   */
  @Override
  public boolean remove(Object o) {
    return moves.remove(o);
  }

  /**
   * Returns <tt>true</tt> if this list contains all of the elements of the
   * specified collection.
   *
   * @param c collection to be checked for containment in this list
   * @return <tt>true</tt> if this list contains all of the elements of the
   * specified collection
   * @throws ClassCastException   if the types of one or more elements
   *                              in the specified collection are incompatible with this
   *                              list
   *                              (<a href="Collection.html#optional-restrictions">optional</a>)
   * @throws NullPointerException if the specified collection contains one
   *                              or more null elements and this list does not permit null
   *                              elements
   *                              (<a href="Collection.html#optional-restrictions">optional</a>),
   *                              or if the specified collection is null
   * @see #contains(Object)
   */
  @Override
  public boolean containsAll(Collection<?> c) {
    return moves.containsAll(c);
  }

  /**
   * Appends all of the elements in the specified collection to the end of
   * this list, in the order that they are returned by the specified
   * collection's iterator (optional operation).  The behavior of this
   * operation is undefined if the specified collection is modified while
   * the operation is in progress.  (Note that this will occur if the
   * specified collection is this list, and it's nonempty.)
   *
   * @param c collection containing elements to be added to this list
   * @return <tt>true</tt> if this list changed as a result of the call
   * @throws UnsupportedOperationException if the <tt>addAll</tt> operation
   *                                       is not supported by this list
   * @throws ClassCastException            if the class of an element of the specified
   *                                       collection prevents it from being added to this list
   * @throws NullPointerException          if the specified collection contains one
   *                                       or more null elements and this list does not permit null
   *                                       elements, or if the specified collection is null
   * @throws IllegalArgumentException      if some property of an element of the
   *                                       specified collection prevents it from being added to this list
   * @see #add(Object)
   */
  @Override
  public boolean addAll(Collection<? extends MoveType> c) {
    return moves.addAll(c);
  }

  /**
   * Inserts all of the elements in the specified collection into this
   * list at the specified position (optional operation).  Shifts the
   * element currently at that position (if any) and any subsequent
   * elements to the right (increases their indices).  The new elements
   * will appear in this list in the order that they are returned by the
   * specified collection's iterator.  The behavior of this operation is
   * undefined if the specified collection is modified while the
   * operation is in progress.  (Note that this will occur if the specified
   * collection is this list, and it's nonempty.)
   *
   * @param index index at which to insert the first element from the
   *              specified collection
   * @param c     collection containing elements to be added to this list
   * @return <tt>true</tt> if this list changed as a result of the call
   * @throws UnsupportedOperationException if the <tt>addAll</tt> operation
   *                                       is not supported by this list
   * @throws ClassCastException            if the class of an element of the specified
   *                                       collection prevents it from being added to this list
   * @throws NullPointerException          if the specified collection contains one
   *                                       or more null elements and this list does not permit null
   *                                       elements, or if the specified collection is null
   * @throws IllegalArgumentException      if some property of an element of the
   *                                       specified collection prevents it from being added to this list
   * @throws IndexOutOfBoundsException     if the index is out of range
   *                                       (<tt>index &lt; 0 || index &gt; size()</tt>)
   */
  @Override
  public boolean addAll(int index, Collection<? extends MoveType> c) {
    return moves.addAll(index, c);
  }

  /**
   * Removes from this list all of its elements that are contained in the
   * specified collection (optional operation).
   *
   * @param c collection containing elements to be removed from this list
   * @return <tt>true</tt> if this list changed as a result of the call
   * @throws UnsupportedOperationException if the <tt>removeAll</tt> operation
   *                                       is not supported by this list
   * @throws ClassCastException            if the class of an element of this list
   *                                       is incompatible with the specified collection
   *                                       (<a href="Collection.html#optional-restrictions">optional</a>)
   * @throws NullPointerException          if this list contains a null element and the
   *                                       specified collection does not permit null elements
   *                                       (<a href="Collection.html#optional-restrictions">optional</a>),
   *                                       or if the specified collection is null
   * @see #remove(Object)
   * @see #contains(Object)
   */
  @Override
  public boolean removeAll(Collection<?> c) {
    return moves.removeAll(c);
  }

  /**
   * Retains only the elements in this list that are contained in the
   * specified collection (optional operation).  In other words, removes
   * from this list all of its elements that are not contained in the
   * specified collection.
   *
   * @param c collection containing elements to be retained in this list
   * @return <tt>true</tt> if this list changed as a result of the call
   * @throws UnsupportedOperationException if the <tt>retainAll</tt> operation
   *                                       is not supported by this list
   * @throws ClassCastException            if the class of an element of this list
   *                                       is incompatible with the specified collection
   *                                       (<a href="Collection.html#optional-restrictions">optional</a>)
   * @throws NullPointerException          if this list contains a null element and the
   *                                       specified collection does not permit null elements
   *                                       (<a href="Collection.html#optional-restrictions">optional</a>),
   *                                       or if the specified collection is null
   * @see #remove(Object)
   * @see #contains(Object)
   */
  @Override
  public boolean retainAll(Collection<?> c) {
    return moves.retainAll(c);
  }

  /**
   * Replaces each element of this list with the result of applying the
   * operator to that element.  Errors or runtime exceptions thrown by
   * the operator are relayed to the caller.
   *
   * @param operator the operator to apply to each element
   * @throws UnsupportedOperationException if this list is unmodifiable.
   *                                       Implementations may throw this exception if an element
   *                                       cannot be replaced or if, in general, modification is not
   *                                       supported
   * @throws NullPointerException          if the specified operator is null or
   *                                       if the operator result is a null value and this list does
   *                                       not permit null elements
   *                                       (<a href="Collection.html#optional-restrictions">optional</a>)
   * @implSpec The default implementation is equivalent to, for this {@code list}:
   * <pre>{@code
   *     final ListIterator<E> li = list.listIterator();
   *     while (li.hasNext()) {
   *         li.set(operator.apply(li.next()));
   *     }
   * }</pre>
   * <p>
   * If the list's list-iterator does not support the {@code set} operation
   * then an {@code UnsupportedOperationException} will be thrown when
   * replacing the first element.
   * @since 1.8
   */
  @Override
  public void replaceAll(UnaryOperator<MoveType> operator) {
    moves.replaceAll(operator);
  }

  /**
   * Removes all of the elements from this list (optional operation).
   * The list will be empty after this call returns.
   *
   * @throws UnsupportedOperationException if the <tt>clear</tt> operation
   *                                       is not supported by this list
   */
  @Override
  public void clear() {
    moves.clear();
  }

  /**
   * Returns the element at the specified position in this list.
   *
   * @param index index of the element to return
   * @return the element at the specified position in this list
   * @throws IndexOutOfBoundsException if the index is out of range
   *                                   (<tt>index &lt; 0 || index &gt;= size()</tt>)
   */
  @Override
  public MoveType get(int index) {
    return moves.get(index);
  }

  /**
   * Replaces the element at the specified position in this list with the
   * specified element (optional operation).
   *
   * @param index   index of the element to replace
   * @param element element to be stored at the specified position
   * @return the element previously at the specified position
   * @throws UnsupportedOperationException if the <tt>set</tt> operation
   *                                       is not supported by this list
   * @throws ClassCastException            if the class of the specified element
   *                                       prevents it from being added to this list
   * @throws NullPointerException          if the specified element is null and
   *                                       this list does not permit null elements
   * @throws IllegalArgumentException      if some property of the specified
   *                                       element prevents it from being added to this list
   * @throws IndexOutOfBoundsException     if the index is out of range
   *                                       (<tt>index &lt; 0 || index &gt;= size()</tt>)
   */
  @Override
  public MoveType set(int index, MoveType element) {
    return moves.set(index, element);
  }

  /**
   * Inserts the specified element at the specified position in this list
   * (optional operation).  Shifts the element currently at that position
   * (if any) and any subsequent elements to the right (adds one to their
   * indices).
   *
   * @param index   index at which the specified element is to be inserted
   * @param element element to be inserted
   * @throws UnsupportedOperationException if the <tt>add</tt> operation
   *                                       is not supported by this list
   * @throws ClassCastException            if the class of the specified element
   *                                       prevents it from being added to this list
   * @throws NullPointerException          if the specified element is null and
   *                                       this list does not permit null elements
   * @throws IllegalArgumentException      if some property of the specified
   *                                       element prevents it from being added to this list
   * @throws IndexOutOfBoundsException     if the index is out of range
   *                                       (<tt>index &lt; 0 || index &gt; size()</tt>)
   */
  @Override
  public void add(int index, MoveType element) {
    moves.add(index, element);
  }

  /**
   * Removes the element at the specified position in this list (optional
   * operation).  Shifts any subsequent elements to the left (subtracts one
   * from their indices).  Returns the element that was removed from the
   * list.
   *
   * @param index the index of the element to be removed
   * @return the element previously at the specified position
   * @throws UnsupportedOperationException if the <tt>remove</tt> operation
   *                                       is not supported by this list
   * @throws IndexOutOfBoundsException     if the index is out of range
   *                                       (<tt>index &lt; 0 || index &gt;= size()</tt>)
   */
  @Override
  public MoveType remove(int index) {
    return moves.remove(index);
  }

  /**
   * Returns the index of the first occurrence of the specified element
   * in this list, or -1 if this list does not contain the element.
   * More formally, returns the lowest index <tt>i</tt> such that
   * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>,
   * or -1 if there is no such index.
   *
   * @param o element to search for
   * @return the index of the first occurrence of the specified element in
   * this list, or -1 if this list does not contain the element
   * @throws ClassCastException   if the type of the specified element
   *                              is incompatible with this list
   *                              (<a href="Collection.html#optional-restrictions">optional</a>)
   * @throws NullPointerException if the specified element is null and this
   *                              list does not permit null elements
   *                              (<a href="Collection.html#optional-restrictions">optional</a>)
   */
  @Override
  public int indexOf(Object o) {
    return moves.indexOf(o);
  }

  /**
   * Performs the given action for each element of the {@code Iterable}
   * until all elements have been processed or the action throws an
   * exception.  Unless otherwise specified by the implementing class,
   * actions are performed in the order of iteration (if an iteration order
   * is specified).  Exceptions thrown by the action are relayed to the
   * caller.
   *
   * @param action The action to be performed for each element
   * @throws NullPointerException if the specified action is null
   * @implSpec <p>The default implementation behaves as if:
   * <pre>{@code
   *     for (T t : this)
   *         action.accept(t);
   * }</pre>
   * @since 1.8
   */
  @Override
  public void forEach(Consumer<? super MoveType> action) {
    moves.forEach(action);
  }

  /**
   * Returns a possibly parallel {@code Stream} with this collection as its
   * source.  It is allowable for this method to return a sequential stream.
   * <p>
   * <p>This method should be overridden when the {@link #spliterator()}
   * method cannot return a spliterator that is {@code IMMUTABLE},
   * {@code CONCURRENT}, or <em>late-binding</em>. (See {@link #spliterator()}
   * for details.)
   *
   * @return a possibly parallel {@code Stream} over the elements in this
   * collection
   * @implSpec The default implementation creates a parallel {@code Stream} from the
   * collection's {@code Spliterator}.
   * @since 1.8
   */
  @Override
  public Stream<MoveType> parallelStream() {
    return moves.parallelStream();
  }

  /**
   * Removes all of the elements of this collection that satisfy the given
   * predicate.  Errors or runtime exceptions thrown during iteration or by
   * the predicate are relayed to the caller.
   *
   * @param filter a predicate which returns {@code true} for elements to be
   *               removed
   * @return {@code true} if any elements were removed
   * @throws NullPointerException          if the specified filter is null
   * @throws UnsupportedOperationException if elements cannot be removed
   *                                       from this collection.  Implementations may throw this exception if a
   *                                       matching element cannot be removed or if, in general, removal is not
   *                                       supported.
   * @implSpec The default implementation traverses all elements of the collection using
   * its {@link #iterator}.  Each matching element is removed using
   * {@link Iterator#remove()}.  If the collection's iterator does not
   * support removal then an {@code UnsupportedOperationException} will be
   * thrown on the first matching element.
   * @since 1.8
   */
  @Override
  public boolean removeIf(Predicate<? super MoveType> filter) {
    return moves.removeIf(filter);
  }

  /**
   * Returns a sequential {@code Stream} with this collection as its source.
   * <p>
   * <p>This method should be overridden when the {@link #spliterator()}
   * method cannot return a spliterator that is {@code IMMUTABLE},
   * {@code CONCURRENT}, or <em>late-binding</em>. (See {@link #spliterator()}
   * for details.)
   *
   * @return a sequential {@code Stream} over the elements in this collection
   * @implSpec The default implementation creates a sequential {@code Stream} from the
   * collection's {@code Spliterator}.
   * @since 1.8
   */
  @Override
  public Stream<MoveType> stream() {
    return moves.stream();
  }

  /**
   * Returns a view of the portion of this list between the specified
   * <tt>fromIndex</tt>, inclusive, and <tt>toIndex</tt>, exclusive.  (If
   * <tt>fromIndex</tt> and <tt>toIndex</tt> are equal, the returned list is
   * empty.)  The returned list is backed by this list, so non-structural
   * changes in the returned list are reflected in this list, and vice-versa.
   * The returned list supports all of the optional list operations supported
   * by this list.<p>
   * <p>
   * This method eliminates the need for explicit range operations (of
   * the sort that commonly exist for arrays).  Any operation that expects
   * a list can be used as a range operation by passing a subList view
   * instead of a whole list.  For example, the following idiom
   * removes a range of elements from a list:
   * <pre>{@code
   *      list.subList(from, to).clear();
   * }</pre>
   * Similar idioms may be constructed for <tt>indexOf</tt> and
   * <tt>lastIndexOf</tt>, and all of the algorithms in the
   * <tt>Collections</tt> class can be applied to a subList.<p>
   * <p>
   * The semantics of the list returned by this method become undefined if
   * the backing list (i.e., this list) is <i>structurally modified</i> in
   * any way other than via the returned list.  (Structural modifications are
   * those that change the size of this list, or otherwise perturb it in such
   * a fashion that iterations in progress may yield incorrect results.)
   *
   * @param fromIndex low endpoint (inclusive) of the subList
   * @param toIndex   high endpoint (exclusive) of the subList
   * @return a view of the specified range within this list
   * @throws IndexOutOfBoundsException for an illegal endpoint index value
   *                                   (<tt>fromIndex &lt; 0 || toIndex &gt; size ||
   *                                   fromIndex &gt; toIndex</tt>)
   */
  @Override
  public List<MoveType> subList(int fromIndex, int toIndex) {
    return moves.subList(fromIndex, toIndex);
  }

  /**
   * Creates a {@link Spliterator} over the elements in this list.
   * <p>
   * <p>The {@code Spliterator} reports {@link Spliterator#SIZED} and
   * {@link Spliterator#ORDERED}.  Implementations should document the
   * reporting of additional characteristic values.
   *
   * @return a {@code Spliterator} over the elements in this list
   * @implSpec The default implementation creates a
   * <em><a href="Spliterator.html#binding">late-binding</a></em> spliterator
   * from the list's {@code Iterator}.  The spliterator inherits the
   * <em>fail-fast</em> properties of the list's iterator.
   * @implNote The created {@code Spliterator} additionally reports
   * {@link Spliterator#SUBSIZED}.
   * @since 1.8
   */
  @Override
  public Spliterator<MoveType> spliterator() {
    return moves.spliterator();
  }

  /**
   * Returns a list iterator over the elements in this list (in proper
   * sequence), starting at the specified position in the list.
   * The specified index indicates the first element that would be
   * returned by an initial call to {@link ListIterator#next next}.
   * An initial call to {@link ListIterator#previous previous} would
   * return the element with the specified index minus one.
   *
   * @param index index of the first element to be returned from the
   *              list iterator (by a call to {@link ListIterator#next next})
   * @return a list iterator over the elements in this list (in proper
   * sequence), starting at the specified position in the list
   * @throws IndexOutOfBoundsException if the index is out of range
   *                                   ({@code index < 0 || index > size()})
   */
  @Override
  public ListIterator<MoveType> listIterator(int index) {
    return moves.listIterator(index);
  }

  /**
   * Returns a list iterator over the elements in this list (in proper
   * sequence).
   *
   * @return a list iterator over the elements in this list (in proper
   * sequence)
   */
  @Override
  public ListIterator<MoveType> listIterator() {
    return moves.listIterator();
  }

  /**
   * Returns the index of the last occurrence of the specified element
   * in this list, or -1 if this list does not contain the element.
   * More formally, returns the highest index <tt>i</tt> such that
   * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>,
   * or -1 if there is no such index.
   *
   * @param o element to search for
   * @return the index of the last occurrence of the specified element in
   * this list, or -1 if this list does not contain the element
   * @throws ClassCastException   if the type of the specified element
   *                              is incompatible with this list
   *                              (<a href="Collection.html#optional-restrictions">optional</a>)
   * @throws NullPointerException if the specified element is null and this
   *                              list does not permit null elements
   *                              (<a href="Collection.html#optional-restrictions">optional</a>)
   */
  @Override
  public int lastIndexOf(Object o) {
    return moves.lastIndexOf(o);
  }
}