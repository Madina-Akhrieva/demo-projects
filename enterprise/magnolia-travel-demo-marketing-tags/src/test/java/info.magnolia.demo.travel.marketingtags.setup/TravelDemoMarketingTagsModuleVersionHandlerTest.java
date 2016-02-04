/**
 * This file Copyright (c) 2015-2016 Magnolia International
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
package info.magnolia.demo.travel.marketingtags.setup;

import static info.magnolia.test.hamcrest.NodeMatchers.*;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import info.magnolia.context.MgnlContext;
import info.magnolia.marketingtags.model.ScriptsAreaModel;
import info.magnolia.module.ModuleVersionHandler;
import info.magnolia.module.ModuleVersionHandlerTestCase;
import info.magnolia.module.model.Version;
import info.magnolia.repository.RepositoryConstants;

import java.util.Arrays;
import java.util.List;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link info.magnolia.demo.travel.marketingtags.setup.TravelDemoMarketingTagsModuleVersionHandler}.
 */
public class TravelDemoMarketingTagsModuleVersionHandlerTest extends ModuleVersionHandlerTestCase {

    private Session session;

    @Override
    protected String getModuleDescriptorPath() {
        return "/META-INF/magnolia/travel-demo-marketing-tags.xml";
    }

    @Override
    protected ModuleVersionHandler newModuleVersionHandlerForTests() {
        return new TravelDemoMarketingTagsModuleVersionHandler();
    }

    @Override
    protected List<String> getModuleDescriptorPathsForTests() {
        return Arrays.asList("/META-INF/magnolia/core.xml");
    }

    @Override
    protected String[] getExtraWorkspaces() {
        return new String[]{"tags"};
    }

    @Override
    protected String getExtraNodeTypes() {
        return "/mgnl-nodetypes/magnolia-tags-nodetypes.xml";
    }

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        addSupportForSetupModuleRepositoriesTask(null);
        session = MgnlContext.getJCRSession(RepositoryConstants.CONFIG);
    }

    @Test
    public void areasAreBootstrapedAfterCleanInstallIfMultisiteModuleIsInstalled() throws Exception {
        // GIVEN
        setupConfigNode(TravelDemoMarketingTagsModuleVersionHandler.MULTISITE_PROTOTYPE);
        session.getNode(TravelDemoMarketingTagsModuleVersionHandler.MULTISITE_PROTOTYPE).setProperty("templateScript", TravelDemoMarketingTagsModuleVersionHandler.DEFAULT_MAIN_LOCATION);

        // WHEN
        executeUpdatesAsIfTheCurrentlyInstalledVersionWas(null);

        // THEN
        assertThatAreasAreBootstraped();
    }

    @Test
    public void areasAreBootstrapedAfterUpdateIfMultisiteModuleIsInstalled() throws Exception {
        // GIVEN
        setupConfigNode(TravelDemoMarketingTagsModuleVersionHandler.MULTISITE_PROTOTYPE);
        session.getNode(TravelDemoMarketingTagsModuleVersionHandler.MULTISITE_PROTOTYPE).setProperty("templateScript", TravelDemoMarketingTagsModuleVersionHandler.DEFAULT_MAIN_LOCATION);

        // WHEN
        executeUpdatesAsIfTheCurrentlyInstalledVersionWas(Version.parseVersion("0.7"));

        // THEN
        assertThatAreasAreBootstraped();
    }

    @Test
    public void areasAreNotBootstrapedAfterCleanInstallIfMultisiteModuleIsNotInstalled() throws Exception {
        // GIVEN

        // WHEN
        executeUpdatesAsIfTheCurrentlyInstalledVersionWas(null);

        // THEN
        assertThat(session.getNode("/modules"), not(hasNode("multisite")));
    }

    private void assertThatAreasAreBootstraped() throws RepositoryException {
        assertThat(session.getNode("/modules/multisite/config/sites/travel/templates/prototype/areas/bodyBeginScripts"), hasProperty("extends", "../headerScripts"));
        assertThat(session.getNode("/modules/multisite/config/sites/travel/templates/prototype/areas/bodyEndScripts"), hasProperty("extends", "../headerScripts"));
        assertThat(session.getNode("/modules/multisite/config/sites/travel/templates/prototype/areas/headerScripts"), hasProperty("modelClass", ScriptsAreaModel.class.getName()));
        assertThat(session.getNode(TravelDemoMarketingTagsModuleVersionHandler.MULTISITE_PROTOTYPE), hasProperty("templateScript", "/travel-demo-marketing-tags/templates/pages/main-marketing-tags.ftl"));
    }

}
