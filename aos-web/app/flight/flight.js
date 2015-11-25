'use strict';

(function() {
angular.module('aos.flight', ['ngRoute'])

  .config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/flight', {
      templateUrl: 'flight/flight.html',
      controller: 'FlightController',
      controllerAs: 'flightCtrl'
    });
  }])
  .controller('FlightController', ['$scope', '$http', function($scope, $http) {
    $scope.flights = [ ];
    
    $http.get('flight/flight.json').then(function(response) {
      $scope.flights = response.data;
      console.log($scope.flights);
    }, function(msg) {
      console.log(msg);
    });

  }]);
  
})();