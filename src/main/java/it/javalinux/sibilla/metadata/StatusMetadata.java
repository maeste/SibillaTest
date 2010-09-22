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
package it.javalinux.sibilla.metadata;

import java.util.Date;

/**
 * The status of a link
 * 
 * @author alessio.soldano@javalinux.it
 * @author stefano.maestri@javalinux.it
 * 
 */
public class StatusMetadata implements Metadata, Mergeable {

    private static final long serialVersionUID = 7755553308719275165L;

    private boolean valid;

    private boolean justCreated;

    private boolean fromAnnotation;

    private boolean fromInstrumentation;

    private boolean passedOnLastRun = false;
    
    private Class<?> upperMostClassInHierarchyDefiningThisMetadata;
    
    private final Long creationDateInMillis;
    
    private boolean onAbstract;

    public StatusMetadata() {
	super();
	creationDateInMillis = (new Date()).getTime();
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
	this.passedOnLastRun = false;
	creationDateInMillis = (new Date()).getTime();
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

    /**
     * @return passedOnLastRun
     */
    public synchronized boolean isPassedOnLastRun() {
	return passedOnLastRun;
    }

    /**
     * @return passedOnLastRun
     */
    public synchronized boolean isFailedOnLastRun() {
	return !passedOnLastRun;
    }

    /**
     * @param passedOnLastRun
     *            Sets passedOnLastRun to the specified value.
     */
    public synchronized void setPassedOnLastRun(boolean passedOnLastRun) {
	this.passedOnLastRun = passedOnLastRun;
    }

    /**
     * 
     * Sets passedOnLastRun to true.
     */
    public synchronized void passedOnLastRun() {
	this.passedOnLastRun = true;
    }

    /**
     * 
     * Sets passedOnLastRun to false
     */
    public synchronized void failedOnLastRun() {
	this.passedOnLastRun = false;
    }

    @Override
    public Object clone() {
	StatusMetadata status = new StatusMetadata();
	status.fromAnnotation = this.fromAnnotation;
	status.fromInstrumentation = this.fromInstrumentation;
	status.justCreated = this.justCreated;
	status.passedOnLastRun = this.passedOnLastRun;
	status.valid = this.valid;
	return status;
    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
	if (!(obj instanceof StatusMetadata)) {
	    return false;
	}
	StatusMetadata o = (StatusMetadata) obj;
	return (o.fromAnnotation == this.fromAnnotation && o.fromInstrumentation == this.fromInstrumentation && o.justCreated == this.justCreated && o.passedOnLastRun == this.passedOnLastRun && o.valid == this.valid);
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + (fromAnnotation ? 1231 : 1237);
	result = prime * result + (fromInstrumentation ? 1231 : 1237);
	result = prime * result + (justCreated ? 1231 : 1237);
	result = prime * result + (passedOnLastRun ? 1231 : 1237);
	result = prime * result + (valid ? 1231 : 1237);
	return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see it.javalinux.sibilla.metadata.Mergeable#merge(Mergeable)
     */
    public boolean merge(Mergeable right) {
	if (right instanceof StatusMetadata) {
	    StatusMetadata r = (StatusMetadata) right;
	    this.fromAnnotation |= r.isFromAnnotation();
	    this.fromInstrumentation |= r.isFromInstrumentation();
	    this.justCreated = this.justCreated && r.isJustCreated();
	    if (creationDateInMillis > r.getCreationDateInMillis()) {
		//do nothing this.passedOnLastRun is the one to be kept
	    } else {
		this.passedOnLastRun = r.isPassedOnLastRun();
	    }
	    this.valid = this.valid && r.isValid();
	    return true;
	} else {
	    return false;
	}
    }

    /**
     * @return upperMostClassInHierarchyDefiningThisMetadata
     */
    public Class<?> getUpperMostClassInHierarchyDefiningThisMetadata() {
        return upperMostClassInHierarchyDefiningThisMetadata;
    }

    /**
     * @param upperMostClassInHierarchyDefiningThisMetadata Sets upperMostClassInHierarchyDefiningThisMetadata to the specified value.
     */
    public void setUpperMostClassInHierarchyDefiningThisMetadata(Class<?> upperMostClassInHierarchyDefiningThisMetadata) {
        this.upperMostClassInHierarchyDefiningThisMetadata = upperMostClassInHierarchyDefiningThisMetadata;
    }

    /**
     * @return onAbstract
     */
    public boolean isOnAbstract() {
        return onAbstract;
    }

    /**
     * @param onAbstract Sets onAbstract to the specified value.
     * @return the modified object itself
     */
    public StatusMetadata setOnAbstract(boolean onAbstract) {
        this.onAbstract = onAbstract;
        return this;
    }

    @Override
	public String toString() {
		return "StatusMetadata [valid=" + valid + ", justCreated="
				+ justCreated + ", fromAnnotation=" + fromAnnotation
				+ ", fromInstrumentation=" + fromInstrumentation
				+ ", passedOnLastRun=" + passedOnLastRun
				+ ", upperMostClassInHierarchyDefiningThisMetadata="
				+ upperMostClassInHierarchyDefiningThisMetadata
				+ ", creationDateInMillis=" + creationDateInMillis
				+ ", onAbstract=" + onAbstract + "]";
	}

    /**
     * @return creationDateInMillis
     */
    public Long getCreationDateInMillis() {
        return creationDateInMillis;
    }
}
