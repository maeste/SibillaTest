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
package it.javalinux.testedby.metadata_v2;

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
    
}
