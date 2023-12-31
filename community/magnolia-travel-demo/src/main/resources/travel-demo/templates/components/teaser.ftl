[#-------------- ASSIGNMENTS --------------]
[#include "/mtk2/templates/includes/init.ftl"]

[#assign title = content.teaserTitle!]
[#assign linkType = content.linkType!]
[#assign resolveError = false]
[#assign linkTarget = ""]
[#assign abstract = content.teaserAbstract!]

[#if linkType?has_content]
    [#assign divClass = "${linkType} ${divClass}"]
[/#if]

[#assign contentHighlight = content.highlight!false]
[#if contentHighlight]
    [#assign divClass = "${divClass} highlight"]
[/#if]

[#assign hideTeaserImage = content.hideTeaserImage!false]
[#if hideTeaserImage]
    [#assign divClass = "${divClass} no-img"]
[/#if]

[#-- Set the imageLink / divClass when image should be displayed --]
[#if !hideTeaserImage && content.image?has_content]
    [#assign image = damfn.getRendition(content.image, "original")]
    [#if !image?has_content]
        [#assign divClass = "${divClass} no-img"]
    [/#if]
[/#if]


[#-------------- ASSIGNMENTS FOR EACH TYPE --------------]
[#if linkType=="page"]
    [#include "/mtk2/templates/includes/teaserPage.ftl"]
[#elseif linkType=="external"]
    [#include "/mtk2/templates/includes/teaserExternal.ftl"]
[#elseif linkType=="download"]
    [#include "/mtk2/templates/includes/teaserDownload.ftl"]
[/#if]

[#if resolveError && cmsfn.editMode]
    [#assign divClass = "${divClass} note-for-editor"]
[/#if]

[#if image?has_content && image.title?has_content]
    [#assign imageTitle = image.title!]
[/#if]


[#-------------- RENDERING --------------]
<div class="${divClass}"${divID}>

[#if cmsfn.editMode && resolveError]

    <${headlineLevel}>${i18n["reference.resolveError"]}</${headlineLevel}>

[#else]

    <${headlineLevel}><a href="${link!}" ${linkTarget!}>${title!}</a></${headlineLevel}>
    [#if image?has_content]
        [#include "/travel-demo/templates/macros/imageResponsive.ftl"]
        [#assign imageClass = "content-image-below"]
        [#assign imageHtml][@imageResponsive image content imageClass false def.parameters /][/#assign]

        <a href="${link}"${linkTarget!}>${imageHtml}</a>
    [/#if]
    [#if abstract?has_content]<p>${abstract}</p>[/#if]

[/#if]

</div><!-- end ${divClass} -->
