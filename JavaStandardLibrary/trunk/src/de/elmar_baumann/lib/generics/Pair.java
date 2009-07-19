package de.elmar_baumann.lib.generics;

// Code: http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6229146
/**
 * Keeps the references of an object pair.
 *
 * @param <A> first object
 * @param <B> second object
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 * @version 2008-09-18
 */
public class Pair<A, B> {

    private final A first;
    private final B second;

    /**
     * Constructs a pair of objects.
     *
     * @param first   first object
     * @param second  second object
     */
    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Returns the first object.
     *
     * @return first object
     */
    public A getFirst() {
        return first;
    }

    /**
     * Returns the second object.
     *
     * @return second object
     */
    public B getSecond() {
        return second;
    }

    /**
     * Returns in braces separated by a comma the string concatenation of
     * the two objects.
     *
     * @return string representation of the two objects
     */
    @Override
    public String toString() {
        return "(" + first + ", " + second + ")"; // NOI18N
    }

    private static boolean equals(Object x, Object y) {
        return (x == null && y == null) || (x != null && x.equals(y));
    }

    /**
     * Two pairs are equal if their referenced objects are equal. This is
     * true if the first and/or the second objects of the pairs are both null
     * or their <code>equals()</code> methods return both true.
     *
     * @param   o object
     * @return  true, if the object is a pair and it's objects are equals to
     *          this pair's objects
     */
    @Override
    public boolean equals(Object o) {
        return o instanceof Pair &&
            equals(first, ((Pair) o).first) &&
            equals(second, ((Pair) o).second);
    }

    @Override
    public int hashCode() {
        if (first == null) {
            return (second == null) ? 0 : second.hashCode() + 1;
        } else if (second == null) {
            return first.hashCode() + 2;
        } else {
            return first.hashCode() * 17 + second.hashCode();
        }
    }
}
