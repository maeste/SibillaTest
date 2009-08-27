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
public class StatusMetadata {
    
    private boolean valid;
    private boolean justCreated;
    private boolean fromAnnotation;
    private boolean fromInstrumentation;
    
    /**
     * @return valid
     */
    public boolean isValid() {
        return valid;
    }
    /**
     * @param valid Sets valid to the specified value.
     */
    public void setValid(boolean valid) {
        this.valid = valid;
    }
    /**
     * @return justCreated
     */
    public boolean isJustCreated() {
        return justCreated;
    }
    /**
     * @param justCreated Sets justCreated to the specified value.
     */
    public void setJustCreated(boolean justCreated) {
        this.justCreated = justCreated;
    }
    /**
     * @return fromAnnotation
     */
    public boolean isFromAnnotation() {
        return fromAnnotation;
    }
    /**
     * @param fromAnnotation Sets fromAnnotation to the specified value.
     */
    public void setFromAnnotation(boolean fromAnnotation) {
        this.fromAnnotation = fromAnnotation;
    }
    /**
     * @return fromInstrumentation
     */
    public boolean isFromInstrumentation() {
        return fromInstrumentation;
    }
    /**
     * @param fromInstrumentation Sets fromInstrumentation to the specified value.
     */
    public void setFromInstrumentation(boolean fromInstrumentation) {
        this.fromInstrumentation = fromInstrumentation;
    }
    
}
