[#-------------- ASSIGNMENTS --------------]
[#if content.tourFinder??]
    [#assign tourFinderLink = cmsfn.link(cmsfn.contentById(content.tourFinder))]
[/#if]


[#-------------- RENDERING --------------]
<!-- Tour Finder -->
<div class="container">
    <div class="row">
        <div class="finder-home col-md-12">
            <form action="${tourFinderLink!'/'}" method="get"
                onsubmit="location.href=this.action+'#!/?q='+this.searchQuery.value+'&duration='+this.duration.value+'&tourTypes='+this.tourTypes.value; return false">
            <div class="finder-home-segment-icon"><img src="${ctx.contextPath}/.resources/tours/webresources/img/map_red.png"></div>
            <div class="finder-home-segment" style="flex:1; min-width:200px">
                <div>Tour Finder</div>
                <input class="finder-search-home" placeholder="What would you like to see?" type="text" name="searchQuery">
            </div>
            <div class="finder-home-segment">
                <div>Duration</div>
                <select name="duration">
                    <option value="">For how long?</option>
                    <option value="2">Weekend</option>
                    <option value="7">7 days</option>
                    <option value="14">14 days</option>
                    <option value="21">21 days</option>
                </select>
            </div>
            <div class="finder-home-segment">
                <div>Type</div>
                <select name="tourTypes">
                    <option value="">What type?</option>
                    [#assign allTourTypes = navfn.navItemsFromApp("category", "/tour-types", "mgnl:category")]
                    [#list allTourTypes as tourType]
                       <option value="${tourType.@uuid}">${tourType.displayName}</option>
                    [/#list]
                </select>
            </div>
            <div class="finder-home-segment-icon">
                <button type="submit" class="btn btn-primary">Find Tour</button>
            </div>
            </form>
        </div>
    </div>
</div>
