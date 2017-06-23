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
package info.magnolia.demo.travel.marketingtags.setup;

import static info.magnolia.test.hamcrest.NodeMatchers.hasNode;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import info.magnolia.context.MgnlContext;
import info.magnolia.marketingtags.MarketingTagsModule;
import info.magnolia.marketingtags.app.TagsNodeTypes;
import info.magnolia.module.ModuleVersionHandler;
import info.magnolia.module.ModuleVersionHandlerTestCase;
import info.magnolia.repository.RepositoryConstants;

import java.util.Arrays;
import java.util.List;

import javax.jcr.Session;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link info.magnolia.demo.travel.marketingtags.setup.TravelDemoMarketingTagsModuleVersionHandler}.
 */
public class TravelDemoMarketingTagsModuleVersionHandlerTest extends ModuleVersionHandlerTestCase {

    private Session config;

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
        return new String[]{"marketing-tags"};
    }

    @Override
    protected String getExtraNodeTypes() {
        return "/mgnl-nodetypes/magnolia-tags-nodetypes.xml";
    }

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        addSupportForSetupModuleRepositoriesTask(MarketingTagsModule.WORKSPACE);
        config = MgnlContext.getJCRSession(RepositoryConstants.CONFIG);
    }

    @Test
    public void cleanInstall() throws Exception {
        // GIVEN

        // WHEN
        executeUpdatesAsIfTheCurrentlyInstalledVersionWas(null);

        // THEN
        assertThat(config.getNode("/modules"), not(hasNode("multisite")));
        assertThat(MgnlContext.getJCRSession(MarketingTagsModule.WORKSPACE).getRootNode(), hasNode("Google-Analytics-for-Travel-Demo", TagsNodeTypes.Tag.NAME));
    }

}
