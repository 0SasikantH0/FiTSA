package com.fitsa.fitsaCM;

import android.support.annotation.NonNull;

/**
 * Created by sasikanth on 6/13/20.
 */

public class CMTypeDefs {
    public static class FEndpoint {
        @NonNull private final String id;
        @NonNull private final String name;

        protected FEndpoint(@NonNull String id, @NonNull String name) {
            this.id = id;
            this.name = name;
        }

        @NonNull
        public String getId() {
            return id;
        }

        @NonNull
        public String getName() {
            return name;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof FEndpoint) {
                FEndpoint other = (FEndpoint) obj;
                return id.equals(other.id);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }

        @Override
        public String toString() {
            return String.format("Endpoint{id=%s, name=%s}", id, name);
        }
    }
}

