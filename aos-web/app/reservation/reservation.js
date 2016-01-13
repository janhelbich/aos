'use strict';

(function() {
angular.module('aos.reservation', ['ngRoute', 'constants'])

  .config(['$routeProvider', function($routeProvider) {
  	$routeProvider.when('/reservation', {
  		templateUrl: 'reservation/reservation.html',
  		controller: 'ReservationController',
  		controllerAs: 'reservationCtrl'
  	})
  	.when('/reservation/:reservationId', {
  		templateUrl: 'reservation/reservation-detail.html',
  		controller: 'ReservationDetailController',
  		controllerAs: 'resDetailCtrl'
  	})
    .when('/reservation/:reservationId/:flightId', {
      templateUrl: 'reservation/reservation-detail.html',
      controller: 'ReservationDetailController',
      controllerAs: 'resDetailCtrl'
    });
  }])

	.controller('ReservationController', ['$scope', '$http', '$routeParams', '$location', 'RESERVATION_PATH', 'API_URL',
  										      function($scope, $http, $routeParams, $location, RESERVATION_PATH, API_URL) {
  	$scope.reservations = [ ];

  	$http.get(API_URL + RESERVATION_PATH).then(function(response) {
  		$scope.reservations = response.data;
  	}, HandleHttpError);

    this.editReservation = function(res) {
      $location.path(RESERVATION_PATH + res.id);
    };

    this.deleteReservation = function(res) {
      $http.delete(API_URL + RESERVATION_PATH + res.id).then(function(response) {
        console.log('reservation delete successful');
        $location.path(RESERVATION_PATH);
      }, function(errResp) {
        console.log('error', errResp);
      });
    };

  }])

  .controller('ReservationDetailController', ['$scope', '$http', '$routeParams', '$location', 
                                              'RESERVATION_PATH', 'API_URL', 'FLIGHT_PATH',
  										      function($scope, $http, $routeParams, $location, 
                                      RESERVATION_PATH, API_URL, FLIGHT_PATH) {

    $scope.errorMsg = null;
    $scope.reservation = { };
    $scope.reservationPassword = null;
    $scope.canAccess = false;
    $scope.email = '';

    var reservationId = $routeParams.reservationId;
    var flightId = $routeParams.flightId;
    this.isNew = reservationId === 'new';
    console.log('reservation url id', reservationId);

    $scope.getReservation = function(resPasswd) {
      $http.get(API_URL + RESERVATION_PATH + reservationId, 
        { headers: { 'X-Password': $scope.reservationPassword }})
          .then(function(response) {
            $scope.canAccess = true;
      	    $scope.reservation = response.data;

      }, function(errResp) {
        $scope.errorMsg = 'Reservation password is required to proceed.';
        $scope.canAccess = false;
        return { };
      });
    }

    if (!this.isNew) {
      $scope.getReservation($scope.reservationPassword);
    } else {
      $scope.reservation.flightId = flightId;
      $scope.reservation.state = 'NEW';
      $scope.canAccess = true;
    }

    this.updateReservation = function() {
      if (this.isNew) {
        $http.post(API_URL + RESERVATION_PATH, $scope.reservation).then(function(response) {
          console.log('create reservation OK');
          $location.path(FLIGHT_PATH);
        }, HandleHttpError);
      } else {
        $http.put(API_URL + RESERVATION_PATH + reservationId, 
          $scope.reservation,
          { headers: { 'X-Password': $scope.reservationPassword }})
            .then(function(response) {
              console.log('update reservation OK');
          $location.path(RESERVATION_PATH);
        }, HandleHttpError);
      }
    };

    $scope.cancel = function() {
      $scope.reservation.state = 'CANCELLED';
      updateReservation();
    }

    $scope.pay = function() {
      var cardNum = '234323424234234';
      $http.post(API_URL + RESERVATION_PATH + reservationId + '/' + cardNum, $scope.reservation).then(function(response) {
        console.log('update reservation OK');
        $location.path(RESERVATION_PATH);
      }, HandleHttpError);
    }

    $scope.print = function() {
      $http.get(API_URL + RESERVATION_PATH + reservationId 
          + '/' + 'print?email=' + $scope.email).then(function(response) {

        console.log('print reservation OK');
        $location.path(FLIGHT_PATH);
      }, HandleHttpError);
    }


  }]);

})();