package org.jphototagger.lib.util;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Resource that should't be used by two different objects at the same time. The
 * owner gets the resource only <em>once</em> by calling
 * {@code #getResource(java.lang.Object)}. After that call the resource is
 * locked until {@code #releaseResource(java.lang.Object)} will be called.
 *
 * <em>The protection of the object is very weak! An object that keeps a
 * reference to the resource can do anything with it. That means, You have
 * to call {@code #isAvailable()} every time before using the resource,
 * even if You got previously a reference to it or You set the reference to
 * null before You call {@code #releaseResource(java.lang.Object)}!</em>
 *
 * Specialized classes are singletons and set spezialized objects
 * through {@code #setResource(java.lang.Object)}.
 *
 * @param <T> Type of resource
 * @author    Elmar Baumann
 */
public class MutualExcludedResource<T> {

    private T resource = null;
    private boolean locked = false;
    private Object owner = null;
    private static final Logger logger = Logger.getLogger(MutualExcludedResource.class.getName());

    /**
     * Returns, whether a resource can be used: It exists (is not null)
     * and it is not locked.
     *
     * @return true, if the resource can be used
     */
    public synchronized boolean isAvailable() {
        return !isLocked() && (resource != null);
    }

    /**
     * Returns the resource, locks it and sets the owner of the resource.
     *
     * <em>If the resource isn't needed anymore, it has to be released with
     * {@code #releaseResource(java.lang.Object)}!</em>
     *
     * @param  owner  owner. Only the owner can unlock the resource an has to
     *                do that!
     * @return Resource or null, if not available. If the returned resource
     *         is not null, {@code #isAvailable()} returns <code>false</code>.
     */
    public synchronized T getResource(Object owner) {
        if (owner == null) {
            throw new NullPointerException("owner == null");
        }

        if (isAvailable()) {
            setLocked(true);
            setOwner(owner);
            logGained(owner);

            return resource;
        }

        return null;
    }

    private void logGained(Object owner) {
        String resourceName = resource.getClass().getSimpleName();
        String ownerName = owner.getClass().getSimpleName();
        int ownerId = owner.hashCode();
        String ownerString = owner.toString();

        logger.log(Level.FINEST, "Resource {0} give to {1} [{2}, {3}]", new Object[]{resourceName, ownerName, ownerId, ownerString});
    }

    /**
     * Releases (unlocks) the resource.
     *
     * @param  owner owner of the resource. Only the owner can release the
     *               resource.
     * @return true, if released. If the return value is true,
     *         {@code #isAvailable()} returns <code>true</code>.
     */
    public synchronized boolean releaseResource(Object owner) {
        if (owner == null) {
            throw new NullPointerException("o == null");
        }

        if (isLocked() && (owner != null) && (owner == getOwner())) {
            this.owner = null;
            setLocked(false);
            logReleased(owner);

            return true;
        }

        return false;
    }

    private void logReleased(Object owner) {
        String resourceName = resource.getClass().getSimpleName();
        String ownerName = owner.getClass().getSimpleName();
        int ownerId = owner.hashCode();
        String ownerString = owner.toString();

        logger.log(Level.FINEST, "Resource {0} released from {1} [{2}, {3}]]", new Object[]{resourceName, ownerName, ownerId, ownerString});
    }

    /**
     * Sets the ressource. Until this method is called,
     * {@code #isAvailable()} will be <code>false</code>.
     *
     * @param resource resource
     */
    protected synchronized void setResource(T resource) {
        if (resource == null) {
            throw new NullPointerException("resource == null");
        }

        this.resource = resource;
    }

    /**
     * Locks or unlocks the resource.
     *
     * @param lock  true, if the resource shall be locked and false if it
     *              shall be unlocked
     */
    private synchronized void setLocked(boolean lock) {
        this.locked = lock;
    }

    /**
     * Returns, whether the resource is locked. Does not test, whether the
     * resource exists (is not null) contrary to {@code #isAvailable()}.
     *
     * @return true, if the resource is locked
     */
    private synchronized boolean isLocked() {
        return locked;
    }

    /**
     * Sets the owner of the resource.
     *
     * @param owner  owner
     */
    private synchronized void setOwner(Object owner) {
        this.owner = owner;
    }

    /**
     * Returns the owner of the resource.
     *
     * @return owner or null if nobody owns the resource
     */
    public synchronized Object getOwner() {
        return owner;
    }

    protected MutualExcludedResource() {
    }
}
