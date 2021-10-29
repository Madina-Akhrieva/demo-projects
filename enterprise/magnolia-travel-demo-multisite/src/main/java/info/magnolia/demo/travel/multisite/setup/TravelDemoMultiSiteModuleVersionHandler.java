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

import info.magnolia.demo.travel.setup.MigrateCorsFilterConfigurationToSiteCorsConfiguration;
import info.magnolia.module.DefaultModuleVersionHandler;
import info.magnolia.module.InstallContext;
import info.magnolia.module.delta.ArrayDelegateTask;
import info.magnolia.module.delta.BootstrapConditionally;
import info.magnolia.module.delta.BootstrapSingleResource;
import info.magnolia.module.delta.CheckAndModifyPropertyValueTask;
import info.magnolia.module.delta.DeltaBuilder;
import info.magnolia.module.delta.IsInstallSamplesTask;
import info.magnolia.module.delta.NodeExistsDelegateTask;
import info.magnolia.module.delta.RemoveNodeTask;
import info.magnolia.module.delta.RemovePropertyTask;
import info.magnolia.module.delta.SetPropertyTask;
import info.magnolia.module.delta.Task;
import info.magnolia.repository.RepositoryConstants;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.ImportUUIDBehavior;

/**
 * Default {@link info.magnolia.module.ModuleVersionHandler} for travel-demo multi site example.
 */
public class TravelDemoMultiSiteModuleVersionHandler extends DefaultModuleVersionHandler {

    private final Task mappingAndDomainConfigurationTask = new ArrayDelegateTask("Add domain and mapping configuration to travel site definition in multisite", "",
            new BootstrapConditionally("Add domain configuration to travel site definition in multisite", "/info/magnolia/demo/travel/multisite/setup/config.modules.multisite.config.sites.travel.domains.xml"),
            new BootstrapConditionally("Add mapping configuration to travel site definition in multisite", "/info/magnolia/demo/travel/multisite/setup/config.modules.multisite.config.sites.travel.mappings.xml"));

    public TravelDemoMultiSiteModuleVersionHandler() {
        register(DeltaBuilder.update("1.2", "")
                .addTask(new BootstrapSingleResource("Re bootstrap sportstation site", "", "/mgnl-bootstrap/travel-demo-multisite/config.modules.multisite.config.sites.sportstation.xml", ImportUUIDBehavior.IMPORT_UUID_COLLISION_REPLACE_EXISTING))
                .addTask(mappingAndDomainConfigurationTask)
                .addTask(new NodeExistsDelegateTask("Remove sportstation-theme configuration from JCR", "/modules/site/config/themes/sportstation-theme",
                        new RemoveNodeTask("", "/modules/site/config/themes/sportstation-theme")))

                .addTask(new IsInstallSamplesTask("Re-Bootstrap website content for sportstation pages", "Re-bootstrap website content to account for all changes",
                        new BootstrapSingleResource("", "", "/mgnl-bootstrap-samples/travel-demo-multisite/website.sportstation.yaml")))
        );

        register(DeltaBuilder.update("1.5.1", "")
                .addTask(new NodeExistsDelegateTask("Disable addCORSHeaders filter and migrate to new CORS configuration", "/server/filters/addCORSHeaders", new ArrayDelegateTask("",
                        new CheckAndModifyPropertyValueTask("/server/filters/addCORSHeaders", "enabled", "true", "false"),
                        new MigrateCorsFilterConfigurationToSiteCorsConfiguration("/modules/multisite/config/sites/travel")
                )))
        );

        register(DeltaBuilder.update("1.5.2", "")
                .addTask(new NodeExistsDelegateTask("Make SPA templates available by default.", "/modules/multisite/config/sites/travel/templates/availability/enableAllWithRenderType", new ArrayDelegateTask("",
                        new RemovePropertyTask("Remove JSP configuration", "", RepositoryConstants.CONFIG, "/modules/multisite/config/sites/travel/templates/availability/enableAllWithRenderType", "jsp"),
                        new SetPropertyTask("Add SPA configuration", RepositoryConstants.CONFIG, "/modules/multisite/config/sites/travel/templates/availability/enableAllWithRenderType", "spa", "spa")
                )))
        );

        register(DeltaBuilder.update("1.6.4", "")
                .addTask(new RemoveNodeTask("Remove virtualUriMappings from JCR configuration", "/modules/tours/virtualUriMappings"))
        );
    }

    @Override
    protected List<Task> getExtraInstallTasks(InstallContext installContext) {
        final List<Task> tasks = new ArrayList<>();
        tasks.addAll(super.getExtraInstallTasks(installContext));
        tasks.add(new NodeExistsDelegateTask("Add mapping and domain configuration to travel site definition in multisite", "/modules/multisite/config/sites/travel", mappingAndDomainConfigurationTask));
        return tasks;
    }
}
