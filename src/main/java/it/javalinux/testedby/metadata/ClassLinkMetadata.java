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
public class ClassLinkMetadata extends LinkMetadata {

    private static final long serialVersionUID = 1L;

    private String clazz;

    /**
     * @param status
     * @param clazz
     */
    public ClassLinkMetadata(StatusMetadata status, String clazz) {
	super(status);
	this.clazz = clazz;
    }

    /**
     * @return clazz
     */
    public String getClazz() {
	return clazz;
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
	ClassLinkMetadata other = (ClassLinkMetadata) obj;
	if (clazz == null) {
	    if (other.clazz != null)
		return false;
	} else if (!clazz.equals(other.clazz))
	    return false;
	return true;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "ClassLinkMetadata [clazz=" + clazz + "]";
    }

}
