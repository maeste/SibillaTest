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
 * @author alessio.soldano@javalinux.it
 * 
 */
public class MethodLinkMetadata extends LinkMetadata {

    private static final long serialVersionUID = 1L;

    private String clazz;

    private MethodMetadata method;

    /**
     * @param status
     * @param clazz
     * @param method
     */
    public MethodLinkMetadata(StatusMetadata status, String clazz, MethodMetadata method) {
	super(status);
	this.clazz = clazz;
	this.method = method;
    }

    /**
     * @return clazz
     */
    public String getClazz() {
	return clazz;
    }

    /**
     * @return method
     */
    public MethodMetadata getMethod() {
	return method;
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
	result = prime * result + ((clazz == null) ? 0 : clazz.hashCode());
	result = prime * result + ((method == null) ? 0 : method.hashCode());
	return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	MethodLinkMetadata other = (MethodLinkMetadata) obj;
	if (clazz == null) {
	    if (other.clazz != null)
		return false;
	} else if (!clazz.equals(other.clazz))
	    return false;
	if (method == null) {
	    if (other.method != null)
		return false;
	} else if (!method.equals(other.method))
	    return false;
	return true;
    }

    /**
     * {@inheritDoc}
     * 
     * @see it.javalinux.testedby.metadata.Mergeable#merge(it.javalinux.testedby.metadata.Mergeable)
     */
    public boolean merge(Mergeable right) {
	if (right instanceof MethodLinkMetadata && this.equals((right))) {
	    return this.getStatus().merge(((MethodLinkMetadata) right).getStatus());
	} else {
	    return false;
	}
    }

    @Override
	public String toString() {
		return "MethodLinkMetadata [clazz=" + clazz + ", method=" + method
				+ ", status=" + status + "]";
	}

}
