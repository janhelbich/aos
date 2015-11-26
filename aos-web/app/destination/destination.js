
'use strict';

(function() {
angular.module('aos.destination', ['ngRoute'])

  .config(['$routeProvider', function($routeProvider) {
  	$routeProvider.when('/destination', {
  		templateUrl: 'destination/destination.html',
  		controller: 'DestinationController',
  		controllerAs: 'destinationCtrl'
  	})
  	.when('/destination/:destinationId', {
  		templateUrl: 'destination/destination-detail.html',
  		controller: 'DestinationDetailController',
  		controllerAs: 'destinationDetailCtrl'
  	});
  }])

  .controller('DestinationController', ['$scope', '$http', '$routeParams', '$location', 'DESTINATION_PATH', 'API_URL',
  										function($scope, $http, $routeParams, $location, DESTINATION_PATH, API_URL) {
  	$scope.destinations = [ ];
    $scope.currentUrl = window.location.href;

  	$http.get(API_URL + DESTINATION_PATH).then(function(response) {
  		$scope.destinations = response.data;
  	}, HandleHttpError);

    this.editDestination = function(dest) {
      $location.path(DESTINATION_PATH + dest.destinationId);
    };

    this.deleteDestination = function(dest) {
      $http.delete(API_URL + DESTINATION_PATH + dest.destinationId).then(function(response) {
        console.log('destination delete successful');
        $location.path(DESTINATION_PATH);
      }, function(errResp) {
        console.log('error', errResp);
      });
    };

    this.newDestination = function() {
      $location.path(DESTINATION_PATH + 'new');
    };

  }])

  .controller('DestinationDetailController', ['$scope', '$http', '$routeParams', '$location', 'DESTINATION_PATH', 'API_URL', 
  										                        function($scope, $http, $routeParams, $location, DESTINATION_PATH, API_URL) {
    $scope.destination = {};
    var destinationId = $routeParams.destinationId;
    var isNew = destinationId === 'new';

    if (!isNew) {
      $http.get(API_URL + DESTINATION_PATH + destinationId).then(function(response) {
      	$scope.destination = response.data;
      }, HandleHttpError);
    }

    this.updateDestination = function() {
      if (isNew) {
        $http.post(API_URL + DESTINATION_PATH, $scope.destination).then(function(response) {
          console.log('create destination OK');
          $location.path(DESTINATION_PATH);
        }, HandleHttpError);
      } else {
        $http.put(API_URL + DESTINATION_PATH + $scope.destination.id, $scope.destination).then(function(response) {
          console.log('update destination OK');
          $location.path(DESTINATION_PATH);
        }, HandleHttpError);
      }
    };

  }]);	

  function HandleHttpError(response) {
    console.log('error', response);
    if (response.status === 404) {
      window.location.href = '/not-found.html';
    } else {
      window.location.href = '/error.html';
    }
  }

})();