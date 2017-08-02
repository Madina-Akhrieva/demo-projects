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

import com.google.common.collect.Sets;
import info.magnolia.contenttags.delta.AddTagsToNodesTask;
import info.magnolia.contenttags.manager.TagManager;
import info.magnolia.module.DefaultModuleVersionHandler;
import info.magnolia.module.InstallContext;
import info.magnolia.module.delta.ArrayDelegateTask;
import info.magnolia.module.delta.Task;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Install the travel-demo-content-tags module.
 */
public class TravelDemoContentTagsModuleVersionHandler extends DefaultModuleVersionHandler {

    private final TagManager tagManager;

    @Inject
    public TravelDemoContentTagsModuleVersionHandler(TagManager tagManager) {
        this.tagManager = tagManager;
    }

    @Override
    protected List<Task> getExtraInstallTasks(InstallContext installContext) {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new ArrayDelegateTask("Add tags to tours", "Tag the tours with set of tags.",
                new AddTagsToNodesTask("tours", Sets.newHashSet("all-inclusive"),
                        Sets.newHashSet("/magnolia-travels/Spectacular-Ammouliani-Island",
                                "/magnolia-travels/Belize-for-Families",
                                "/magnolia-travels/The-Trans-Siberian-Railway",
                                "/magnolia-travels/Antarctic-Active-Adventure",
                                "/magnolia-travels/Hawaii-Five-O",
                                "/magnolia-travels/Dubai-and-Oman",
                                "/magnolia-travels/Jordan-s-Pearls",
                                "/magnolia-travels/Arctic-Archipelagos",
                                "/magnolia-travels/Lapland-for-Families",
                                "/magnolia-travels/Go-Fly-a-Kite"), tagManager),
                new AddTagsToNodesTask("tours", Sets.newHashSet("city"),
                        Sets.newHashSet("/magnolia-travels/Inside-New-Delhi",
                                "/magnolia-travels/Kyoto",
                                "/magnolia-travels/Dubai-and-Oman",
                                "/magnolia-travels/A-Unique-Basel-Holiday"), tagManager),
                new AddTagsToNodesTask("tours", Sets.newHashSet("exotic"),
                        Sets.newHashSet("/magnolia-travels/Vietnam--Tradition-and-Today",
                                "/magnolia-travels/A-Taste-of-Malaysia",
                                "/magnolia-travels/Temples-of-the-Orient",
                                "/magnolia-travels/The-Baikal-Experience",
                                "/magnolia-travels/Hawaii-Five-O",
                                "/magnolia-travels/Beach-Paradise-in-Brazil",
                                "/magnolia-travels/Island-hopping-in-Indonesia",
                                "/magnolia-travels/Fabulous-Costa-Rica",
                                "/magnolia-travels/Beautiful-Botswana",
                                "/magnolia-travels/The-Big-African-adventure"), tagManager),
                new AddTagsToNodesTask("tours", Sets.newHashSet("food"),
                        Sets.newHashSet("/magnolia-travels/Vietnam--Tradition-and-Today",
                                "/magnolia-travels/Inside-New-Delhi",
                                "/magnolia-travels/A-Taste-of-Malaysia",
                                "/magnolia-travels/Last-Bike-out-of-Hanoi",
                                "/magnolia-travels/The-Czech-Republic",
                                "/magnolia-travels/France-for-Families"), tagManager),
                new AddTagsToNodesTask("tours", Sets.newHashSet("health"),
                        Sets.newHashSet("/magnolia-travels/Hut-to-Hut-in-the-Swiss-Alps",
                                "/magnolia-travels/Grand-Canyon---the-American-South-West",
                                "/magnolia-travels/Dolomites-on-Bike",
                                "/magnolia-travels/The-Long-Trail",
                                "/magnolia-travels/Surfin--Safari",
                                "/magnolia-travels/Hawaii-Five-O",
                                "/magnolia-travels/Go-Fly-a-Kite"), tagManager),
                new AddTagsToNodesTask("tours", Sets.newHashSet("history"),
                        Sets.newHashSet("/magnolia-travels/West-Coast---Highway-101",
                                "/magnolia-travels/Kyoto",
                                "/magnolia-travels/Temples-of-the-Orient",
                                "/magnolia-travels/The-Trans-Siberian-Railway",
                                "/magnolia-travels/Jordan-s-Pearls",
                                "/magnolia-travels/The-Maya-and-Aztec-Empires",
                                "/magnolia-travels/France-for-Families",
                                "/magnolia-travels/A-Unique-Basel-Holiday"), tagManager),
                new AddTagsToNodesTask("tours", Sets.newHashSet("islands"),
                        Sets.newHashSet("/magnolia-travels/Spectacular-Ammouliani-Island",
                                "/magnolia-travels/Belize-for-Families",
                                "/magnolia-travels/Surfin--Safari",
                                "/magnolia-travels/Antarctic-Active-Adventure",
                                "/magnolia-travels/Hawaii-Five-O",
                                "/magnolia-travels/Island-hopping-in-Indonesia",
                                "/magnolia-travels/Fabulous-Costa-Rica",
                                "/magnolia-travels/North-Sea-Islands",
                                "/magnolia-travels/Arctic-Archipelagos",
                                "/magnolia-travels/Wood-Water-Stone",
                                "/magnolia-travels/Scuba-Diving-in-Bahamas--famed-Tiger-Beach"), tagManager),
                new AddTagsToNodesTask("tours", Sets.newHashSet("luxury"),
                        Sets.newHashSet("/magnolia-travels/Spectacular-Ammouliani-Island",
                                "/magnolia-travels/The-Trans-Siberian-Railway",
                                "/magnolia-travels/Dubai-and-Oman"), tagManager),
                new AddTagsToNodesTask("tours", Sets.newHashSet("mountains"),
                        Sets.newHashSet("/magnolia-travels/Hut-to-Hut-in-the-Swiss-Alps",
                                "/magnolia-travels/Grand-Canyon---the-American-South-West",
                                "/magnolia-travels/Dolomites-on-Bike",
                                "/magnolia-travels/The-Long-Trail"), tagManager),
                new AddTagsToNodesTask("tours", Sets.newHashSet("sale"),
                        Sets.newHashSet("/magnolia-travels/Hut-to-Hut-in-the-Swiss-Alps",
                                "/magnolia-travels/Inside-New-Delhi",
                                "/magnolia-travels/The-Baikal-Experience",
                                "/magnolia-travels/The-other-side-of-Iran",
                                "/magnolia-travels/North-Sea-Islands",
                                "/magnolia-travels/France-for-Families",
                                "/magnolia-travels/Go-Fly-a-Kite"), tagManager),
                new AddTagsToNodesTask("tours", Sets.newHashSet("snow"),
                        Sets.newHashSet("/magnolia-travels/The-Trans-Siberian-Railway",
                                "/magnolia-travels/The-Baikal-Experience",
                                "/magnolia-travels/Antarctic-Active-Adventure",
                                "/magnolia-travels/Arctic-Archipelagos",
                                "/magnolia-travels/Lapland-for-Families"), tagManager),
                new AddTagsToNodesTask("tours", Sets.newHashSet("spirituality"),
                        Sets.newHashSet("/magnolia-travels/Kyoto",
                                "/magnolia-travels/Temples-of-the-Orient",
                                "/magnolia-travels/Last-Bike-out-of-Hanoi",
                                "/magnolia-travels/Island-hopping-in-Indonesia",
                                "/magnolia-travels/The-Czech-Republic"), tagManager),
                new AddTagsToNodesTask("tours", Sets.newHashSet("unique"),
                        Sets.newHashSet("/magnolia-travels/A-Taste-of-Malaysia",
                                "/magnolia-travels/Temples-of-the-Orient",
                                "/magnolia-travels/The-Baikal-Experience",
                                "/magnolia-travels/The-other-side-of-Iran",
                                "/magnolia-travels/Jordan-s-Pearls",
                                "/magnolia-travels/The-Maya-and-Aztec-Empires",
                                "/magnolia-travels/A-Unique-Basel-Holiday"), tagManager),
                new AddTagsToNodesTask("tours", Sets.newHashSet("wild animals"),
                        Sets.newHashSet("/magnolia-travels/Beach-Paradise-in-Brazil",
                                "/magnolia-travels/Fabulous-Costa-Rica",
                                "/magnolia-travels/Beautiful-Botswana",
                                "/magnolia-travels/Arctic-Archipelagos",
                                "/magnolia-travels/The-Big-African-adventure",
                                "/magnolia-travels/Wood-Water-Stone",
                                "/magnolia-travels/Scuba-Diving-in-Bahamas--famed-Tiger-Beach"), tagManager)));
        return tasks;
    }
}
