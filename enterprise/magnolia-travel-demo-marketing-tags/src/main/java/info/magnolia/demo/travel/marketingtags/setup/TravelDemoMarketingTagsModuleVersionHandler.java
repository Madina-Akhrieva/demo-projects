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
package info.magnolia.demo.travel.marketingtags.setup;

import info.magnolia.module.DefaultModuleVersionHandler;
import info.magnolia.module.InstallContext;
import info.magnolia.module.delta.ArrayDelegateTask;
import info.magnolia.module.delta.BootstrapSingleResource;
import info.magnolia.module.delta.DeltaBuilder;
import info.magnolia.module.delta.NodeExistsDelegateTask;
import info.magnolia.module.delta.SetPropertyTask;
import info.magnolia.module.delta.Task;
import info.magnolia.repository.RepositoryConstants;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.ImportUUIDBehavior;

/**
 * Install the travel-demo-marketing-tags module.
 */
public class TravelDemoMarketingTagsModuleVersionHandler extends DefaultModuleVersionHandler {

    private NodeExistsDelegateTask cookieConsentPluginSetup = new NodeExistsDelegateTask("Setup Cookie Consent", "Adds Cookie Consent to the Travel demo.", RepositoryConstants.WEBSITE, "/travel", new ArrayDelegateTask("",
            new SetPropertyTask(RepositoryConstants.WEBSITE, "/travel", "bannerbackground", "#000"),
            new SetPropertyTask(RepositoryConstants.WEBSITE, "/travel", "buttonbackground", "#ef6155"),
            new SetPropertyTask(RepositoryConstants.WEBSITE, "/travel", "buttontext", "#fff"),
            new SetPropertyTask(RepositoryConstants.WEBSITE, "/travel", "complianceType", "info"),
            new SetPropertyTask(RepositoryConstants.WEBSITE, "/travel", "dismiss", "Got it!"),
            new SetPropertyTask(RepositoryConstants.WEBSITE, "/travel", "layout", "block"),
            new SetPropertyTask(RepositoryConstants.WEBSITE, "/travel", "link", "Learn more"),
            new SetPropertyTask(RepositoryConstants.WEBSITE, "/travel", "message", "This website uses cookies to ensure you get the best experience on our website."),
            new SetPropertyTask(RepositoryConstants.WEBSITE, "/travel", "position", "bottom"),
            new SetPropertyTask(RepositoryConstants.WEBSITE, "/travel", "readMoreLink", "external"),
            new SetPropertyTask(RepositoryConstants.WEBSITE, "/travel", "readMoreLinkexternal", "https://cookiesandyou.com/"))
    );

    public TravelDemoMarketingTagsModuleVersionHandler() {
        // We re-bootstrap every config/content item upon update
        register(DeltaBuilder.update("1.2", "")
                .addTask(new BootstrapSingleResource("", "", "/mgnl-bootstrap-samples/travel-demo-marketing-tags/marketing-tags.Clicky-for-Travel-Demo.xml", ImportUUIDBehavior.IMPORT_UUID_COLLISION_REPLACE_EXISTING))
                .addTask(new BootstrapSingleResource("", "", "/mgnl-bootstrap-samples/travel-demo-marketing-tags/marketing-tags.Google-Analytics-for-Travel-Demo.xml", ImportUUIDBehavior.IMPORT_UUID_COLLISION_REPLACE_EXISTING))
        );
        register(DeltaBuilder.update("1.2.5", "")
                .addTask(cookieConsentPluginSetup)
        );
    }

    @Override
    protected List<Task> getExtraInstallTasks(InstallContext installContext) {
        List<Task> tasks = new ArrayList<>(super.getExtraInstallTasks(installContext));
        tasks.add(cookieConsentPluginSetup);
        return tasks;
    }
}
