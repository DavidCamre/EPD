/* Copyright (c) 2011 Danish Maritime Authority.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dk.dma.epd.common.util;

import java.io.Serializable;

/**
 * Encapsulation of an ellipsoid, and declaration of common reference
 * ellipsoids.
 * 
 * @author Mike Gavaghan
 */
public final class Ellipsoid implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Semi major axis (meters). */
    private final double mSemiMajorAxis;

    /** Semi minor axis (meters). */
    private final double mSemiMinorAxis;

    /** Flattening. */
    private final double mFlattening;

    /** Inverse flattening. */
    private final double mInverseFlattening;

    /**
     * Construct a new Ellipsoid. This is private to ensure the values are
     * consistent (flattening = 1.0 / inverseFlattening). Use the methods
     * fromAAndInverseF() and fromAAndF() to create new instances.
     * 
     * @param semiMajor
     * @param semiMinor
     * @param flattening
     * @param inverseFlattening
     */
    private Ellipsoid(double semiMajor, double semiMinor, double flattening,
            double inverseFlattening) {
        mSemiMajorAxis = semiMajor;
        mSemiMinorAxis = semiMinor;
        mFlattening = flattening;
        mInverseFlattening = inverseFlattening;
    }

    /** The WGS84 ellipsoid. */
    public static final Ellipsoid WGS84 = fromAAndInverseF(6378137.0,
            298.257223563);

    /** The GRS80 ellipsoid. */
    public static final Ellipsoid GRS80 = fromAAndInverseF(6378137.0,
            298.257222101);

    /** The GRS67 ellipsoid. */
    public static final Ellipsoid GRS67 = fromAAndInverseF(6378160.0, 298.25);

    /** The ANS ellipsoid. */
    public static final Ellipsoid ANS = fromAAndInverseF(6378160.0, 298.25);

    /** The WGS72 ellipsoid. */
    public static final Ellipsoid WGS72 = fromAAndInverseF(6378135.0, 298.26);

    /** The Clarke1858 ellipsoid. */
    public static final Ellipsoid CLARKE1858 = fromAAndInverseF(6378293.645,
            294.26);

    /** The Clarke1880 ellipsoid. */
    public static final Ellipsoid CLARKE1880 = fromAAndInverseF(6378249.145,
            293.465);

    /** A spherical "ellipsoid". */
    public static final Ellipsoid SPHERE = fromAAndF(6371000, 0.0);

    /**
     * Build an Ellipsoid from the semi major axis measurement and the inverse
     * flattening.
     * 
     * @param semiMajor
     *            semi major axis (meters)
     * @param inverseFlattening
     * @return
     */
    public static Ellipsoid fromAAndInverseF(double semiMajor,
            double inverseFlattening) {
        double f = 1.0 / inverseFlattening;
        double b = (1.0 - f) * semiMajor;

        return new Ellipsoid(semiMajor, b, f, inverseFlattening);
    }

    /**
     * Build an Ellipsoid from the semi major axis measurement and the
     * flattening.
     * 
     * @param semiMajor
     *            semi major axis (meters)
     * @param flattening
     * @return
     */
    public static Ellipsoid fromAAndF(double semiMajor, double flattening) {
        double inverseF = 1.0 / flattening;
        double b = (1.0 - flattening) * semiMajor;

        return new Ellipsoid(semiMajor, b, flattening, inverseF);
    }

    /**
     * Get semi-major axis.
     * 
     * @return semi-major axis (in meters).
     */
    public double getSemiMajorAxis() {
        return mSemiMajorAxis;
    }

    /**
     * Get semi-minor axis.
     * 
     * @return semi-minor axis (in meters).
     */
    public double getSemiMinorAxis() {
        return mSemiMinorAxis;
    }

    /**
     * Get flattening
     * 
     * @return
     */
    public double getFlattening() {
        return mFlattening;
    }

    /**
     * Get inverse flattening.
     * 
     * @return
     */
    public double getInverseFlattening() {
        return mInverseFlattening;
    }
}
