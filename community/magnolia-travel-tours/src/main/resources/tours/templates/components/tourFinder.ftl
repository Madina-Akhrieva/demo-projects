<div class="finder-background" style="background-image: url(${ctx.contextPath}/.resources/tours/webresources/img/tour-finder-background-ross-parmly-25230.jpg);"></div>

<div class="finder-container" ng-app="TourFinder">
    <div ng-view>
    </div>
</div>

<script type="text/javascript">
    TourFinder.init({ contextPath: "${ctx.contextPath}", restBase: "${ctx.contextPath}/.rest/delivery" });
</script>
