package io.sentry.jvmti;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.HashMap;

/**
 * Class representing a single call frame.
 */
public final class Frame {
    /**
     * Java method this frame is for.
     */
    private final Method method;
    /**
     * Object method was called on.
     */
    private final Object localThis;
    /**
     * Local variable information for this frame.
     */
    private final LocalVariable[] locals;
    /**
     * The index of the instruction executing in this frame. -1 if the frame is executing a native method.
     */
    private final int location;
    /**
     * Line number of the frame.
     */
    private final int linenumber;
    /**
     * Local variable information as a map of name -> value.
     */
    private final Map<String, LocalVariable> namedLocals = new HashMap<>();

    /**
     * Construct a {@link Frame}.
     *
     * @param method Java method this frame is for.
     * @param localThis Object method was called on.
     * @param locals Local variable information for this frame.
     * @param location The index of the instruction executing in this frame.
     *                 -1 if the frame is executing a native method.
     * @param lineno Line number of the frame.
     */
    public Frame(Method method, Object localThis, LocalVariable[] locals, int location, int lineno) {
        this.method = method;
        this.localThis = localThis;
        this.locals = locals;
        this.location = location;
        this.linenumber = lineno;
        if (locals != null) {
            for (LocalVariable local : locals) {
                if (local != null) {
                    namedLocals.put(local.name, local);
                }
            }
        }
    }

    public Method getMethod() {
        return method;
    }

    public Object getLocalThis() {
        return localThis;
    }

    public LocalVariable[] getLocals() {
        return locals;
    }

    public int getLocation() {
        return location;
    }

    public int getLinenumber() {
        return linenumber;
    }

    public Map<String, LocalVariable> getNamedLocals() {
        return namedLocals;
    }

    /**
     * Class representing a single local variable.
     */
    public static final class LocalVariable {
        /**
         * Whether or not the object is live.
         */
        final boolean live;
        /**
         * Variable name.
         */
        final String name;
        /**
         * Variable signature.
         */
        final String signature;
        /**
         * Java generics signature (if available).
         */
        final String genericSignature;
        /**
         * Variable value.
         */
        final Object value;

        /**
         * Construct a {@link LocalVariable} for a live object.
         *
         * @param name Variable name.
         * @param signature Variable signature.
         * @param genericSignature Java generics signature (if available).
         * @param value Variable value.
         */
        private LocalVariable(String name, String signature, String genericSignature, Object value) {
            this.live = true;
            this.name = name;
            this.signature = signature;
            this.genericSignature = genericSignature;
            this.value = value;
        }

        /**
         * Construct a {@link LocalVariable} for a dead object.
         *
         * @param name Variable name.
         * @param signature Variable signature.
         * @param genericSignature Java generics signature (if available).
         */
        private LocalVariable(String name, String signature, String genericSignature) {
            this.live = false;
            this.name = name;
            this.signature = signature;
            this.genericSignature = genericSignature;
            this.value = null;
        }

        public boolean isLive() {
            return live;
        }

        public String getName() {
            return name;
        }

        public String getSignature() {
            return signature;
        }

        public String getGenericSignature() {
            return genericSignature;
        }

        public Object getValue() {
            return value;
        }
    }
}
