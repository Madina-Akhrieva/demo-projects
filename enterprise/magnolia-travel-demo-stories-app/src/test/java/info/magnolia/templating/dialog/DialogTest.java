/**
 * This file Copyright (c) 2021 Magnolia International
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
package info.magnolia.templating.dialog;

import static info.magnolia.test.hamcrest.NodeMatchers.hasProperty;
import static org.hamcrest.MatcherAssert.assertThat;

import info.magnolia.ui.VaadinLookup;
import info.magnolia.ui.field.LinkField;

import javax.jcr.Node;

import org.junit.Test;

import com.vaadin.ui.Component;

public class DialogTest extends AbstractDialogTest {

    @Override
    public String getModuleName() {
        return "travel-demo-stories-app";
    }

    @Test
    public void storiesLead() throws Exception {
        // GIVEN
        formView = createForm("/dialogs/components/storiesLead.yaml");
        final Component form = formView.asVaadinComponent();

        // WHEN
        final LinkField<Node> url = VaadinLookup.findByType(form, LinkField.class);
        url.setValue(destination);
        formView.write(destination);

        // THEN
        assertThat(destination, hasProperty("storiesFolder", destination.getIdentifier()));
    }
}
