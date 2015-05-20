/**
 * This file Copyright (c) 2015 Magnolia International
 * Ltd.  (http://www.magnolia-cms.com). All rights reserved.
 *
 *
 * This file is dual-licensed under both the Magnolia
 * Network Agreement and the GNU General Public License.
 * You may elect to use one or the other of these licenses.
 *
 * This file is distributed in the hope that it will be
 * useful, but AS-IS and WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, or NONINFRINGEMENT.
 * Redistribution, except as permitted by whichever of the GPL
 * or MNA you select, is prohibited.
 *
 * 1. For the GPL license (GPL), you can redistribute and/or
 * modify this file under the terms of the GNU General
 * Public License, Version 3, as published by the Free Software
 * Foundation.  You should have received a copy of the GNU
 * General Public License, Version 3 along with this program;
 * if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * 2. For the Magnolia Network Agreement (MNA), this file
 * and the accompanying materials are made available under the
 * terms of the MNA which accompanies this distribution, and
 * is available at http://www.magnolia-cms.com/mna.html
 *
 * Any modifications to this file must keep this entire header
 * intact.
 *
 */
package info.magnolia.demo.travel.tours.service;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import info.magnolia.cms.beans.config.URI2RepositoryManager;
import info.magnolia.cms.beans.config.URI2RepositoryMapping;
import info.magnolia.cms.core.AggregationState;
import info.magnolia.cms.i18n.DefaultI18nContentSupport;
import info.magnolia.cms.i18n.I18nContentSupport;
import info.magnolia.context.MgnlContext;
import info.magnolia.dam.api.Asset;
import info.magnolia.dam.templating.functions.DamTemplatingFunctions;
import info.magnolia.demo.travel.tours.ToursModule;
import info.magnolia.jcr.util.NodeTypes;
import info.magnolia.jcr.util.NodeUtil;
import info.magnolia.link.LinkTransformerManager;
import info.magnolia.module.categorization.functions.CategorizationTemplatingFunctions;
import info.magnolia.rendering.template.type.TemplateTypeHelper;
import info.magnolia.repository.RepositoryConstants;
import info.magnolia.templating.functions.TemplatingFunctions;
import info.magnolia.test.ComponentsTestUtil;
import info.magnolia.test.mock.MockWebContext;
import info.magnolia.test.mock.jcr.MockSession;

import javax.inject.Provider;
import javax.jcr.Node;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link TourServices}.
 */
public class TourServicesTest {

    private final static String CONTEXT_PATH = "/contextPath";
    private final static String URI_PREFIX = "/tours";

    private MockWebContext context;

    private MockSession session;
    private MockSession sessionTours;
    private DamTemplatingFunctions damTemplatingFunctions;
    private TourServices tourServices;

    @Before
    public void setUp() {
        session = new MockSession(RepositoryConstants.WEBSITE);
        sessionTours = new MockSession(ToursModule.TOURS_REPOSITORY_NAME);

        final AggregationState aggregationState = new AggregationState();

        context = new MockWebContext();
        context.setContextPath(CONTEXT_PATH);
        context.setAggregationState(aggregationState);
        context.addSession(RepositoryConstants.WEBSITE, session);
        context.addSession(ToursModule.TOURS_REPOSITORY_NAME, sessionTours);

        MgnlContext.setInstance(context);

        final LinkTransformerManager linkTransformerManager = new LinkTransformerManager();
        linkTransformerManager.setAddContextPathToBrowserLinks(true);
        ComponentsTestUtil.setInstance(LinkTransformerManager.class, linkTransformerManager);
        ComponentsTestUtil.setImplementation(I18nContentSupport.class, DefaultI18nContentSupport.class);

        final URI2RepositoryMapping tourMapping = new URI2RepositoryMapping(URI_PREFIX, ToursModule.TOURS_REPOSITORY_NAME, "");
        final URI2RepositoryManager uri2RepositoryManager = new URI2RepositoryManager();
        uri2RepositoryManager.addMapping(tourMapping);
        ComponentsTestUtil.setInstance(URI2RepositoryManager.class, uri2RepositoryManager);

        final ToursModule toursModule = new ToursModule();
        final TemplateTypeHelper templateTypeHelper = mock(TemplateTypeHelper.class);
        final Provider<AggregationState> aggregationStateProvider = new Provider<AggregationState>() {
            @Override
            public AggregationState get() {
                return aggregationState;
            }
        };
        final TemplatingFunctions templatingFunctions = new TemplatingFunctions(aggregationStateProvider, templateTypeHelper);
        final CategorizationTemplatingFunctions categorizationTemplatingFunctions = mock(CategorizationTemplatingFunctions.class);

        damTemplatingFunctions = mock(DamTemplatingFunctions.class);

        tourServices = new TourServices(toursModule, templateTypeHelper, templatingFunctions, categorizationTemplatingFunctions, damTemplatingFunctions);
    }

    @After
    public void tearDown() {
        ComponentsTestUtil.clear();
        MgnlContext.setInstance(null);
    }

    @Test
    public void marshallCategoryNode() throws Exception {
        // GIVEN
        final Node node = NodeUtil.createPath(session.getRootNode(), "test-node", NodeTypes.ContentNode.NAME);
        node.setProperty(Category.PROPERTY_NAME_DISPLAY_NAME, "displayName");
        node.setProperty(Category.PROPERTY_NAME_BODY, "bodyText");
        node.setProperty(Category.PROPERTY_NAME_DESCRIPTION, "description");
        node.setProperty(Category.PROPERTY_NAME_IMAGE, "jcr:cafebabe-cafe-babe-cafe-babecafebabe");
        final Asset asset = mock(Asset.class);

        when(damTemplatingFunctions.getAsset(anyString())).thenReturn(asset);

        // WHEN
        final Category category = tourServices.marshallCategoryNode(node);

        // THEN
        assertThat(category, not(nullValue()));
        assertThat(category.getName(), is("displayName"));
        assertThat(category.getBody(), is("bodyText"));
        assertThat(category.getDescription(), is("description"));
        assertThat(category.getImage(), is(asset));
    }

    @Test
    public void marshallTourNode() throws Exception {
        // GIVEN
        final Node node = NodeUtil.createPath(session.getRootNode(), "test-node", NodeTypes.ContentNode.NAME);
        node.setProperty(Tour.PROPERTY_NAME_DISPLAY_NAME, "tourDisplayName");
        node.setProperty(Tour.PROPERTY_NAME_DESCRIPTION, "description");
        node.setProperty(Tour.PROPERTY_NAME_IMAGE, "jcr:cafebabe-cafe-babe-cafe-babecafebabe");
        final Asset asset = mock(Asset.class);

        when(damTemplatingFunctions.getAsset(anyString())).thenReturn(asset);

        // WHEN
        final Tour tour = tourServices.marshallTourNode(node);

        // THEN
        assertThat(tour, not(nullValue()));
        assertThat(tour.getName(), is("tourDisplayName"));
        assertThat(tour.getDescription(), is("description"));
        assertThat(tour.getImage(), is(asset));
    }

    @Test
    public void tourLinkReturnsProperLinkToTour() throws Exception {
        // GIVEN
        final String pathToTour = "/path/to/test-tour";
        final Node tourNode = NodeUtil.createPath(sessionTours.getRootNode(), pathToTour, NodeTypes.ContentNode.NAME);

        // WHEN
        final String tourLink = tourServices.getTourLink(tourNode);

        // THEN
        assertThat(tourLink, is(CONTEXT_PATH + URI_PREFIX + pathToTour));
    }

}