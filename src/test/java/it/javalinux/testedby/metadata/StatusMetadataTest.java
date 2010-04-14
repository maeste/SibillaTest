/*
 * Unilan S.r.l. 
 * Copyright 2008
 */
package it.javalinux.testedby.metadata;

import static org.hamcrest.core.Is.is;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author oracle
 *
 */
public class StatusMetadataTest {

    /**
     * Test method for {@link it.javalinux.testedby.metadata.StatusMetadata#merge(it.javalinux.testedby.metadata.Mergeable)}.
     */
    @Test
    public void mergeShouldSetPassedOnLastRunIfRightMergedPassedOnLastRun() {
	StatusMetadata left = new StatusMetadata(true, true, true, true);
	StatusMetadata right = new StatusMetadata(true, true, true, true);
	right.passedOnLastRun();
	assertThat(left.merge(right), is(true));
	assertThat(left.isFailedOnLastRun(), is(false));
    }

}
