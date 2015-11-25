'use strict';

angular.module('aos.flight', ['ngRoute'])

  .config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/flight', {
      templateUrl: 'flight/flight.html',
      controller: 'FlightController'
    });
  }])

  .controller('FlightController', [function() {
  
  }]);
