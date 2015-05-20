[#-------------- ASSIGNMENTS --------------]
[#include "/tours/templates/macros/tourTeaser.ftl"]
[#include "/tours/templates/macros/editorAlert.ftl" /]

[#if def.parameters.tourType??]
    [#assign category = model.getCategoryByName(def.parameters.tourType)]
[#else]
    [#assign category = model.getCategoryByUrl()!]
[/#if]

[#assign tours = model.getToursByCategory(category.identifier)]
[#assign title = content.title!i18n.get('tour.all.tours', [category.name!""])!]

[#-------------- RENDERING --------------]
<!-- Tour List -->
<div class="container">

    <h2>${title}</h2>

    <div class="row">
        [#list tours as tour]
            [#assign link = tourfn.getTourLink(tour)!"#"]
            [#assign asset = damfn.getAsset(tour.img)!]
            [@tourTeaser tour.name tour.description link asset /]
        [/#list]
    </div>

    [@editorAlert i18n.get('note.for.editors.assign.category', [category.name!""]) /]
</div>

