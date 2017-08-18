/**
 * This file Copyright (c) 2017 Magnolia International
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
package info.magnolia.demo.travel.contenttags.setup;

import static info.magnolia.test.hamcrest.NodeMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsArrayWithSize.arrayWithSize;

import info.magnolia.cms.util.ClasspathResourcesUtil;
import info.magnolia.contenttags.manager.TagManager;
import info.magnolia.context.MgnlContext;
import info.magnolia.module.InstallContext;
import info.magnolia.module.ModuleVersionHandler;
import info.magnolia.module.ModuleVersionHandlerTestCase;
import info.magnolia.objectfactory.Components;
import info.magnolia.repository.RepositoryManager;

import java.util.List;

import javax.jcr.Property;
import javax.jcr.Session;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;


public class TravelDemoContentTagsModuleVersionHandlerTest extends ModuleVersionHandlerTestCase {

    private Session tours;

    @Override
    protected String getModuleDescriptorPath() {
        return "/META-INF/magnolia/travel-demo-content-tags.xml";
    }

    @Override
    protected ModuleVersionHandler newModuleVersionHandlerForTests() {
        return new TravelDemoContentTagsModuleVersionHandler(new TagManager(MgnlContext::getInstance, null));
    }

    @Override
    protected List<String> getModuleDescriptorPathsForTests() {
        return Lists.newArrayList("/META-INF/magnolia/core.xml");
    }

    @Override
    protected String getRepositoryConfigFileName() {
        return "test-content-tags-repositories.xml";
    }

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        // Register mgnl:tag node type
        RepositoryManager repositoryManager = Components.getComponent(RepositoryManager.class);
        repositoryManager.getRepositoryProvider("magnolia").registerNodeTypes(ClasspathResourcesUtil.getResource("mgnl-nodetypes/content-tags-nodetypes.xml").openStream());

        tours = MgnlContext.getJCRSession("tours");
    }

    @Test
    public void cleanInstall() throws Exception {
        // GIVEN
        setupNode("tours", "/magnolia-travels/Kyoto");

        // WHEN
        final InstallContext installContext = executeUpdatesAsIfTheCurrentlyInstalledVersionWas(null);

        // THEN
        assertThat(tours.getNode("/magnolia-travels/Kyoto"), hasProperty(TagManager.TAGS_PROPERTY));
        Property tags = tours.getNode("/magnolia-travels/Kyoto").getProperty(TagManager.TAGS_PROPERTY);
        assertThat(tags.getValues(), arrayWithSize(3));

        assertThat(installContext.getConfigJCRSession().getRootNode(), hasNode("modules/content-tags/config/taggableWorkspaces/tours"));
    }
}
