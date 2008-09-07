/**
 *  WISE Invokes Services Easily - Stefano Maestri / Alessio Soldano
 *  
 *  http://www.javalinuxlabs.org - http://www.javalinux.it 
 *
 *  Wise is free software; you can redistribute it and/or modify it under the 
 *  terms of the GNU Lesser General Public License as published by the Free Software Foundation; 
 *  either version 2.1 of the License, or (at your option) any later version.
 *
 *  Wise is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without 
 *  even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 *  See the GNU Lesser General Public License for more details at gnu.org.
 */
package it.javalinux.testedby;

/**
 * @author stefano.maestri@javalinux.it
 */
@TestedBy( testClass = "it.javalinux.testedby.TestedBySampleTest" )
public class TestedBySample {

    /**
     * @param args
     */
    public static void main( String[] args ) {
        TestedBySample sample = new TestedBySample();
        System.out.print(sample.add(1, 2));

    }

    @TestedBy( testClass = "it.javalinux.testedby.TestedBySampleTest", testMethod = "addShouldWork" )
    public int add( int i,
                    int j ) {
        return i + j;
    }
    
    public void pippo() {
    	
    }

}
