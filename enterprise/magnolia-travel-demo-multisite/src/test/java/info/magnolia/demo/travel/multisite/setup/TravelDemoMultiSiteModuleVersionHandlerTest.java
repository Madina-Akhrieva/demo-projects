/**
 * This file Copyright (c) 2015-2023 Magnolia International
 * Ltd.  (http://www.magnolia-cms.com). All rights reserved.
 *
 *
 * This program and the accompanying materials are made
 * available under the terms of the Magnolia Network Agreement
 * which accompanies this distribution, and is available at
 * http://www.magnolia-cms.com/mna.html
 *
 * Any modifications to this file must keep this entire header
 * intact.
 *
 */
package info.magnolia.demo.travel.multisite.setup;

import static info.magnolia.test.hamcrest.NodeMatchers.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import info.magnolia.context.MgnlContext;
import info.magnolia.module.ModuleVersionHandler;
import info.magnolia.module.ModuleVersionHandlerTestCase;
import info.magnolia.module.model.Version;
import info.magnolia.repository.RepositoryConstants;

import java.util.Arrays;
import java.util.List;

import javax.jcr.Session;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link TravelDemoMultiSiteModuleVersionHandler}.
 */
public class TravelDemoMultiSiteModuleVersionHandlerTest extends ModuleVersionHandlerTestCase {

    private Session session;

    @Override
    protected String getModuleDescriptorPath() {
        return "/META-INF/magnolia/travel-demo-multisite.xml";
    }

    @Override
    protected ModuleVersionHandler newModuleVersionHandlerForTests() {
        return new TravelDemoMultiSiteModuleVersionHandler();
    }

    @Override
    protected List<String> getModuleDescriptorPathsForTests() {
        return Arrays.asList("/META-INF/magnolia/core.xml");
    }

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        session = MgnlContext.getJCRSession(RepositoryConstants.CONFIG);
    }

    @Test
    public void updateFrom163RemovesVirtualUriMappings() throws Exception {
        //GIVEN
        setupConfigNode("/modules/tours/virtualUriMappings");

        // WHEN
        executeUpdatesAsIfTheCurrentlyInstalledVersionWas(Version.parseVersion("1.6.3"));

        //THEN
        assertThat(session.getNode("/modules/tours"), not(hasNode("virtualUriMappings")));
    }
}
