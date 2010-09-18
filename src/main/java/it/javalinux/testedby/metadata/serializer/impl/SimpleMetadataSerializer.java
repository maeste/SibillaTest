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
package it.javalinux.testedby.metadata.serializer.impl;

import it.javalinux.testedby.metadata.TestsMetadata;
import it.javalinux.testedby.metadata.serializer.MetadataSerializer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * @author Stefano Maestri stefano.maestri@javalinux.it
 * 
 */
public class SimpleMetadataSerializer implements MetadataSerializer {

	/**
	 * {@inheritDoc}
	 * 
	 * @see it.javalinux.testedby.metadata.serializer.MetadataSerializer#deserialize()
	 */
	public TestsMetadata deserialize() {
		return this.deserialize("testedbyMetadata.bin");

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see it.javalinux.testedby.metadata.serializer.MetadataSerializer#serialize(it.javalinux.testedby.metadata.TestsMetadata)
	 */
	public boolean serialize(TestsMetadata metadata) {
		return this.serialize(metadata, "testedbyMetadata.bin");
	}

	/**
	 * 
	 * @param filename
	 *            from which deserialise metadatas
	 * @return deserialised TestsMetadata It gets them from a file on current
	 *         directory named testedbyMetadata.bin
	 */
	public TestsMetadata deserialize(String filename) {
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		TestsMetadata metadata = null;
		try {
			fis = new FileInputStream(filename);

			ois = new ObjectInputStream(fis);

			metadata = (TestsMetadata) ois.readObject();
		} catch (Exception e) {
			return null;
		} finally {
			try {
				ois.close();
			} catch (Exception e) {
			}

			try {
				fis.close();
			} catch (Exception e) {
			}
		}
		return metadata;
	}

	/**
	 * 
	 * @param metadata
	 * @param fileName
	 *            filename where serialise the metadata
	 * @return true if serialisations is possible and went well. False if
	 *         serialisation fails.
	 */
	public boolean serialize(TestsMetadata metadata, String fileName) {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = new FileOutputStream(fileName);

			oos = new ObjectOutputStream(fos);

			oos.writeObject(metadata);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				oos.close();
			} catch (Exception e) {
			}

			try {
				fos.close();
			} catch (Exception e) {
			}
		}

		try {
			XStream xstream = new XStream(new DomDriver());
			fos = new FileOutputStream(fileName + ".debug");
			fos.write(xstream.toXML(metadata).getBytes());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {

			try {
				fos.close();
			} catch (Exception e) {
			}
		}
		return true;

	}
}
