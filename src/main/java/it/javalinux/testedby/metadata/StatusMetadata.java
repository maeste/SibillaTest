/*
 * Stefano Maestri, javalinuxlabs.org Copyright 2008, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors. 
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package it.javalinux.testedby.metadata;

/**
 * The status of a link
 * 
 * @author alessio.soldano@javalinux.it
 * 
 */
public class StatusMetadata {

    private boolean valid;

    private boolean justCreated;

    private boolean fromAnnotation;

    private boolean fromInstrumentation;

    public StatusMetadata()
    {
	super();
    }
    
    /**
     * @param valid
     * @param justCreated
     * @param fromAnnotation
     * @param fromInstrumentation
     */
    public StatusMetadata(boolean valid, boolean justCreated, boolean fromAnnotation, boolean fromInstrumentation) {
	super();
	this.valid = valid;
	this.justCreated = justCreated;
	this.fromAnnotation = fromAnnotation;
	this.fromInstrumentation = fromInstrumentation;
    }

    /**
     * @return valid
     */
    public boolean isValid() {
	return valid;
    }

    /**
     * @param valid
     *            Sets valid to the specified value.
     * @return the instance itself
     */
    public StatusMetadata setValid(boolean valid) {
	this.valid = valid;
	return this;
    }

    /**
     * @return justCreated
     */
    public boolean isJustCreated() {
	return justCreated;
    }

    /**
     * @param justCreated
     *            Sets justCreated to the specified value.
     * @return the instance itself
     */
    public StatusMetadata setJustCreated(boolean justCreated) {
	this.justCreated = justCreated;
	return this;
    }

    /**
     * @return fromAnnotation
     */
    public boolean isFromAnnotation() {
	return fromAnnotation;
    }

    /**
     * @param fromAnnotation
     *            Sets fromAnnotation to the specified value.
     * @return the instance itself
     */
    public StatusMetadata setFromAnnotation(boolean fromAnnotation) {
	this.fromAnnotation = fromAnnotation;
	return this;
    }

    /**
     * @return fromInstrumentation
     */
    public boolean isFromInstrumentation() {
	return fromInstrumentation;
    }

    /**
     * @param fromInstrumentation
     *            Sets fromInstrumentation to the specified value.
     * @return the instance itself
     */
    public StatusMetadata setFromInstrumentation(boolean fromInstrumentation) {
	this.fromInstrumentation = fromInstrumentation;
	return this;
    }

}
