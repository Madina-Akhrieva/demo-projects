/**
 * This file Copyright (c) 2015-2018 Magnolia International
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
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertTrue;

import info.magnolia.context.MgnlContext;
import info.magnolia.module.InstallContext;
import info.magnolia.module.ModuleVersionHandler;
import info.magnolia.module.ModuleVersionHandlerTestCase;
import info.magnolia.module.model.Version;
import info.magnolia.repository.RepositoryConstants;

import java.util.Arrays;
import java.util.List;

import javax.jcr.Node;
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
    public void updateTo151_MigrateCorsToTravelSite() throws Exception {
        // GIVEN
        setupConfigNode("/modules/multisite/config/sites/travel");
        setupConfigNode("/server/filters/addCORSHeaders/headers");
        setupConfigProperty("/server/filters/addCORSHeaders", "enabled", "true");
        setupConfigProperty("/server/filters/addCORSHeaders/headers", "Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
        setupConfigProperty("/server/filters/addCORSHeaders/headers", "Access-Control-Allow-Methods", "GET");
        setupConfigProperty("/server/filters/addCORSHeaders/headers", "Access-Control-Allow-Origin", "*");

        // WHEN
        executeUpdatesAsIfTheCurrentlyInstalledVersionWas(Version.parseVersion("1.5.0"));

        // THEN
        assertThat(session.nodeExists("/modules/multisite/config/sites/travel/cors/travel"), is(true));
        final Node node = session.getNode("/modules/multisite/config/sites/travel/cors/travel");
        assertThat(node.getNode("uris").getNode("rest"), hasProperty("patternString", "/.rest/*"));
        assertThat(node.getNode("allowedOrigins"), hasProperty("0", "*"));
        assertThat(node.getNode("allowedMethods"), hasProperty("0", "GET"));
        assertThat(node.getNode("allowedHeaders"), allOf(
                hasProperty("0", "X-PINGOTHER"),
                hasProperty("1", "Origin"),
                hasProperty("2", "X-Requested-With"),
                hasProperty("3", "Content-Type"),
                hasProperty("4", "Accept")
        ));
    }

    @Test
    public void updateTo152_removeJSPAndAddSPAAvailability() throws Exception {
        setupConfigNode("/modules/multisite/config/sites/travel/templates/availability/enableAllWithRenderType");
        setupConfigProperty("/modules/multisite/config/sites/travel/templates/availability/enableAllWithRenderType", "jsp", "jsp");

        executeUpdatesAsIfTheCurrentlyInstalledVersionWas(Version.parseVersion("1.5.1"));

        final Node node = session.getNode("/modules/multisite/config/sites/travel/templates/availability/enableAllWithRenderType");
        assertThat(node, allOf(
                hasProperty("spa", "spa"),
                not(hasProperty("jsp", "jsp"))
        ));
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
