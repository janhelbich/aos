'use strict';

(function() {
angular.module('aos.flight', ['ngRoute', 'constants'])

  .config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/flight', {
      templateUrl: 'flight/flight.html',
      controller: 'FlightController',
      controllerAs: 'flightCtrl'
    })
    .when('/flight/:flightId', {
      templateUrl: 'flight/flight-detail.html',
      controller: 'FlightDetailController',
      controllerAs: 'flightDetailCtrl'
    });
  }])

  .controller('FlightController', ['$scope', '$http', '$location', 'API_URL', 'FLIGHT_PATH', 
                                   function($scope, $http, $location, API_URL, FLIGHT_PATH) {
    $scope.flights = [ ];
    $scope.criteria = { 'orderBy': 'id', 'sort': 'asc' };

    this.editFlight = function(flight) {
      $location.path(FLIGHT_PATH + flight.id);
    };

    this.deleteFlight = function(flight) {
      $http.delete(API_URL + FLIGHT_PATH + flight.id).then(function(response) {
        console.log('flight delete successful');
        $location.path(FLIGHT_PATH);
      }, function(errResp) {
        console.log('error', errResp);
      });
    };

    this.newFlight = function() {
      $location.path(FLIGHT_PATH + 'new');
    };

    this.bookFlight = function(flight) {
      $location.path('/reservation/new/' + flight.id);
    };

    this.fetchFlights = function() {
      var hdrs = CriteriaHeaders($scope.criteria);
      $http.get(API_URL + FLIGHT_PATH, hdrs).then(function(response) {
        $scope.flights = response.data;
      }, HandleHttpError);
    };

    this.fetchFlights();

  }])
  .controller('FlightDetailController', ['$scope', '$http', '$routeParams', '$location', 
                                         'API_URL', 'FLIGHT_PATH', 'DESTINATION_PATH',
                                         function($scope, $http, $routeParams, $location, 
                                                  API_URL, FLIGHT_PATH, DESTINATION_PATH) {
    $scope.flight = {};
    $scope.destinations = [ ];
    var flightId = $routeParams.flightId;
    var isNew = flightId === 'new';

    if (!isNew) {
      $http.get(API_URL + FLIGHT_PATH + flightId).then(function(response) {
        console.log(response);
        $scope.flight = response.data;
        $scope.flight.dateOfDeparture = new Date($scope.flight.dateOfDeparture);
      }, HandleHttpError);
    }

    $http.get(API_URL + DESTINATION_PATH).then(function(response) {
      var dests = response.data;
      dests.forEach(function(dest) {
        console.log(dest);
        console.log(dest.destinationId);
        $scope.destinations.push(dest.destinationId);
      });

    }, HandleHttpError);

    this.updateFlight = function() {
      console.log('called updateFlight function, flight:', $scope.flight);

      $scope.flight.dateOfDeparture = moment($scope.flight.dateOfDeparture).format("YYYY-MM-DDTHH:mm:ssZ");
      console.log($scope.flight.dateOfDeparture);

      if (isNew) {
        $http.post(API_URL + FLIGHT_PATH, $scope.flight).then(function(response) {
          console.log('flight create ok');
          $location.path(FLIGHT_PATH);
        }, HandleHttpError);
      } else {
        console.log('calling put', $scope.flight);
        $http.put(API_URL + FLIGHT_PATH + flightId, $scope.flight).then(function(response) {
          console.log('flight update ok');
          $location.path('/flight');
        }, HandleHttpError);
      }
    };

  }]);

  function HandleHttpError(response) {
    console.log('error', response);
    if (response.status === 404) {
      window.location.href = '/not-found.html';
    } else {
      //window.location.href = '/error.html';
    }
  }

  function CriteriaHeaders(crit) {
    var v = { 'X-Filter': [ ] };
    if (crit.orderBy !== undefined && crit.orderBy != null) {
      v['X-Order'] = crit.orderBy + ':' + crit.sort;
    }
    if (crit.dateFrom !== undefined && crit.dateFrom !== null) {
      v['X-Filter'].push('dateOfDepartureFrom=' + moment(crit.dateFrom).format("YYYY-MM-DDTHH:mm:ssZ"));
    }
    if (crit.dateTo !== undefined && crit.dateTo !== null) {
      v['X-Filter'].push('dateOfDepartureTo=' + moment(crit.dateTo).format("YYYY-MM-DDTHH:mm:ssZ"));
    }

    console.log('filter', v);

    return {
      headers: v
    };
  }

})();
