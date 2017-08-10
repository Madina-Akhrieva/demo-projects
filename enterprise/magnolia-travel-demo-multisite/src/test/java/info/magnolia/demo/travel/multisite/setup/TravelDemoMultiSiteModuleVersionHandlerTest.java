/**
 * This file Copyright (c) 2015-2017 Magnolia International
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
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

import info.magnolia.context.MgnlContext;
import info.magnolia.module.InstallContext;
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
    public void cleanInstall() throws Exception {
        // GIVEN
        // Looks weird (clean install?) and it is indeed – well travel-demo does the migration itself
        setupConfigNode("/modules/multisite/config/sites/travel");

        // WHEN
        final InstallContext ctx = executeUpdatesAsIfTheCurrentlyInstalledVersionWas(null);

        // THEN
        assertTrue(session.nodeExists("/modules/multisite/config/sites/travel/domains"));
        assertTrue(session.nodeExists("/modules/multisite/config/sites/travel/mappings"));
        assertThat(session.getNode("/modules/multisite/config/sites/sportstation/domains/"), hasProperty("extends"));
        assertNoMessages(ctx);
    }

    @Test
    public void updateFrom07DoesNotFailInstallation() throws Exception {
        // GIVEN
        setupConfigNode("/modules/multisite/config/sites/sportstation");
        setupConfigNode("/modules/multisite/config/sites/travel/domains");
        setupConfigNode("/modules/multisite/config/sites/travel/mappings");

        // WHEN
        final InstallContext ctx = executeUpdatesAsIfTheCurrentlyInstalledVersionWas(Version.parseVersion("0.7"));

        // THEN
        assertNoMessages(ctx);
    }

    @Test
    public void updateFrom06CreatesMappingAndDomainConfigWhenItDoesNotExist() throws Exception {
        // GIVEN
        setupConfigNode("/modules/multisite/config/sites/sportstation");
        setupConfigNode("/modules/multisite/config/sites/travel");

        // WHEN
        executeUpdatesAsIfTheCurrentlyInstalledVersionWas(Version.parseVersion("0.6"));

        // THEN
        assertTrue(session.nodeExists("/modules/multisite/config/sites/travel/domains"));
        assertTrue(session.nodeExists("/modules/multisite/config/sites/travel/mappings"));
    }

    @Test
    public void upgradeFrom014RemovesSportstationThemeFromJCR() throws Exception {
        // GIVEN
        setupConfigNode("/modules/site/config/themes/sportstation-theme");

        // WHEN
        final InstallContext ctx = executeUpdatesAsIfTheCurrentlyInstalledVersionWas(Version.parseVersion("0.14"));

        // THEN
        assertThat(session.getRootNode(), not(hasNode("modules/site/config/themes/sportstation-theme")));
        this.assertNoMessages(ctx);
    }

    @Test
    public void updateFrom10AddsExtendsPropertyToSportStationWebsite() throws Exception {
        // GIVEN
        setupConfigNode("/modules/multisite/config/sites/sportstation/domains/");
        setupConfigNode("/modules/multisite/config/sites/sportstation/mappings/");

        // WHEN
        executeUpdatesAsIfTheCurrentlyInstalledVersionWas(Version.parseVersion("1.0"));

        // THEN
        assertThat(session.getNode("/modules/multisite/config/sites/sportstation/domains/"), hasProperty("extends"));
        assertThat(session.getNode("/modules/multisite/config/sites/sportstation/mappings/"), hasProperty("extends"));
    }

    @Test
    public void updateFrom11MovesSportstationTemplatesUnderTravelDemoMultisiteFolder() throws Exception {
        // GIVEN
        setupProperty(RepositoryConstants.CONFIG, "/modules/multisite/config/sites/sportstation/templates", "prototypeId", "sportstation:pages/prototype", null);
        setupProperty(RepositoryConstants.CONFIG, "/modules/multisite/config/sites/sportstation/templates/availability/templates/tour", "id", "sportstation:pages/tourSportstation", null);

        // WHEN
        executeUpdatesAsIfTheCurrentlyInstalledVersionWas(Version.parseVersion("1.1"));

        // THEN
        assertThat(session.getNode("/modules/multisite/config/sites/sportstation/templates"), hasProperty("prototypeId", "travel-demo-multisite:pages/prototype"));
        assertThat(session.getNode("/modules/multisite/config/sites/sportstation/templates/availability/templates/tour"), hasProperty("id", "travel-demo-multisite:pages/tourSportstation"));
    }

    @Test
    public void updateChangesVirtualUriMappings() throws Exception {
        //GIVEN
        String travelToursMappingPath = "/modules/tours/virtualUriMappings/travelToursMapping";
        String sportstationToursMapping = "/modules/tours/virtualUriMappings/sportstationToursMapping";

        setupConfigNode(sportstationToursMapping);
        setupConfigProperty(travelToursMappingPath, "toUri", "forward:/travel/tour?tour=$1");
        setupConfigNode(sportstationToursMapping);
        setupConfigProperty(sportstationToursMapping, "toUri", "forward:/sportstation/tour?tour=$1");

        // WHEN
        executeUpdatesAsIfTheCurrentlyInstalledVersionWas(Version.parseVersion("1.1.2"));

        //THEN
        assertThat(session.getNode(travelToursMappingPath), hasProperty("toUri", "forward:/tour?tour=$1"));
        assertThat(session.getNode(sportstationToursMapping), hasProperty("toUri", "forward:/tour?tour=$1"));
    }
}
