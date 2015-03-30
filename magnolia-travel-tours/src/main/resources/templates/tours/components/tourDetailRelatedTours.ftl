[#include "/templates/tours/macros/relatedTours.ftl"]

[#assign relatedCategories = model.relatedCategoriesByParameter]

<!-- Tour Detail - Related Tours -->
[#list relatedCategories as category]

    [#assign tours = model.getRelatedToursByCategory(category.identifier)]
    [@relatedTours category.name tours /]

[/#list]
