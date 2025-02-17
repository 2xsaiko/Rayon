package dev.lazurite.rayon.core.impl.util.model;

import com.jme3.bounding.BoundingBox;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import dev.lazurite.rayon.core.api.PhysicsElement;
import dev.lazurite.rayon.core.impl.bullet.math.QuaternionHelper;
import dev.lazurite.rayon.core.impl.bullet.math.VectorHelper;

/**
 * A {@link Frame} can be used for interpolation on the render thread.
 * {@link Frame}s are stored in {@link PhysicsElement}s and are updated
 * each tick.
 */
public class Frame {
    private Vector3f prevLocation;
    private Vector3f tickLocation;
    private Quaternion prevRotation;
    private Quaternion tickRotation;

    public Frame() {
        this(new Vector3f(), new Quaternion(), new BoundingBox());
    }

    public Frame(Vector3f location, Quaternion rotation, BoundingBox box) {
        this.set(location, location, rotation, rotation);
    }

    public void set(Vector3f prevLocation, Vector3f tickLocation, Quaternion prevRotation, Quaternion tickRotation) {
        this.prevLocation = prevLocation;
        this.tickLocation = tickLocation;
        this.prevRotation = prevRotation;
        this.tickRotation = tickRotation;
    }

    public void from(Frame frame) {
        this.set(frame.prevLocation, frame.tickLocation, frame.prevRotation, frame.tickRotation);
    }

    public void from(Frame prevFrame, Vector3f tickLocation, Quaternion tickRotation, BoundingBox tickBox) {
        this.set(prevFrame.tickLocation, tickLocation, prevFrame.tickRotation, tickRotation);
    }

    public Vector3f getLocation(Vector3f store, float tickDelta) {
        return store.set(VectorHelper.lerp(prevLocation, tickLocation, tickDelta));
    }

    public Quaternion getRotation(Quaternion store, float tickDelta) {
        return store.set(QuaternionHelper.slerp(prevRotation, tickRotation, tickDelta));
    }

    public Vector3f getLocationDelta(Vector3f store) {
        store.set(tickLocation.subtract(prevLocation));
        return store;
    }

    public Vector3f getRotationDelta(Vector3f store) {
        store.set(QuaternionHelper.toEulerAngles(tickRotation).subtract(QuaternionHelper.toEulerAngles(prevRotation)));
        return store;
    }
}