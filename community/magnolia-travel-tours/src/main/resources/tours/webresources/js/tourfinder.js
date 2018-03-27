var TourFinder = TourFinder || (function(){
    return {
        init : function(args) {
            angular
                .module('TourFinder', ['ngAnimate', 'ngRoute'])
                .config(function($routeProvider) {
                    $routeProvider.when("/", {
                        templateUrl: args.contextPath + '/.resources/tours/webresources/views/index.html',
                        controller: 'MainController',
                        reloadOnSearch: false
                    });
                })
                .controller('MainController', ['$scope', '$routeParams', '$http', '$location', function($scope, $routeParams, $http, $location) {
                    var notFoundMessages = ["Oops! Didn't find anything. How about these tours instead?",
                                            "No matches, d'oh! Would you like to browse some?",
                                            "bleep bloop 404 Not found. Go for a random adventure?"];
                    var randomIndex = Math.floor(Math.random() * notFoundMessages.length);
                    $scope.notFoundMessage = notFoundMessages[randomIndex];
                    $scope.durations = [{ value: 2, name: 'Weekend' },
                                        { value: 7, name: '7 days' },
                                        { value: 14, name: '14 days' },
                                        { value: 21, name: '21 days' }];
                    $scope.useDurations = {};
                    $scope.useDestinations = {};
                    $scope.useTourTypes = {};
                    $scope.search = {};
                    $scope.contextPath = args.contextPath;

                    if ($routeParams.duration) {
                        $scope.useDurations = {};
                        var split = $routeParams.duration.split(',');
                        for (var i in split) {
                            $scope.useDurations[split[i]] = true;
                        }
                    }
                    if ($routeParams.q) {
                        $scope.search.query = $routeParams.q;
                    }

                    // obtain the data
                    $http.get(args.restBase + '/destinations/v1/').then(function(response) {
                        $scope.destinations = response.data.results;
                        if ($routeParams.destination) {
                            var split = $routeParams.destination.split(',');
                            for (var i in split) {
                                $scope.useDestinations[split[i]] = true;
                            }
                        }
                    }, function(response) {
                        console.error("Couldn't reach endpoint.");
                    });
                    $http.get(args.restBase + '/tourTypes/v1/').then(function(response) {
                        $scope.tourTypes = response.data.results;
                        if ($routeParams.tourTypes) {
                            var split = $routeParams.tourTypes.split(',');
                            for (var i in split) {
                                $scope.useTourTypes[split[i]] = true;
                            }
                        }
                    }, function(response) {
                        console.error("Couldn't reach endpoint.");
                    });
                    $http.get(args.restBase + '/tours/v1/').then(function(response) {
                        $scope.tours = response.data.results;
                    }, function(response) {
                        console.error("Couldn't reach endpoint.");
                    });

                    // watch for changes
                    $scope.$watch(function() {
                        return {
                            useDurations: $scope.useDurations,
                            useDestinations: $scope.useDestinations,
                            useTourTypes: $scope.useTourTypes,
                            search: $scope.search,
                            destinations: $scope.destinations,
                            tourTypes: $scope.tourTypes,
                        };
                    }, function (newValues, oldValues) {
                        // wait for both tourTypes & destinations to be populated by async calls
                        if (newValues !== oldValues && newValues.tourTypes && newValues.destinations) {
                            var randomIndex = Math.floor(Math.random() * notFoundMessages.length);
                            $scope.notFoundMessage = notFoundMessages[randomIndex];

                            var qs = '';
                            var parameters = {duration: [], destination: [], tourTypes: [], q: []};
                            var durations = Object.keys(newValues.useDurations).reduce(function (filtered, key) {
                                    if (newValues.useDurations[key]) filtered.push(key);
                                    return filtered;
                            }, []);
                            var destinationKeys = Object.keys(newValues.useDestinations).reduce(function (filtered, key) {
                                    if (newValues.useDestinations[key]) filtered.push(key);
                                    return filtered;
                            }, []);
                            var tourTypeKeys = Object.keys(newValues.useTourTypes).reduce(function (filtered, key) {
                                    if (newValues.useTourTypes[key]) filtered.push(key);
                                    return filtered;
                            }, []);
                            if (durations.length > 0 && durations.length < $scope.durations.length) {
                                parameters.duration = durations;
                            }
                            if (destinationKeys.length > 0 && destinationKeys.length < newValues.destinations.length) {
                                parameters.destination = destinationKeys;
                            }
                            if (tourTypeKeys.length > 0 && tourTypeKeys.length < newValues.tourTypes.length) {
                                parameters.tourTypes = tourTypeKeys;
                            }
                            if (newValues.search.query) {
                                parameters.q = [newValues.search.query];
                            }
                            var qs = '';
                            if (Object.keys(parameters).length > 0) {
                                var p = [];
                                Object.keys(parameters).forEach(function(key) {
                                    if (parameters[key].length > 0) {
                                        var values = parameters[key];
                                        p.push(key + '=' + values.join('|'));
                                        $location.search(key, values.join(','));
                                    } else {
                                        $location.search(key, null);
                                    }
                                });
                                qs = '?' + p.join('&');
                            }

                            $http.get(args.restBase + '/tours/v1/' + qs).then(function(response) {
                                $scope.filteredTours = response.data.results;
                            });
                        }
                    }, true);
                }]);
        }
    }
})();
