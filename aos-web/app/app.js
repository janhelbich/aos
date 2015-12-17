'use strict';

// Declare app level module which depends on views, and components
  angular.module('aos', [
    'constants',
    'ngRoute',
    'ngCookies',
    'aos.flight',
    'aos.destination',
    'aos.reservation',
    'aos.login'
  ]).
  config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/', {
      templateUrl: 'resource-list.html',
      controller: 'ResourcesController'
    })
    .otherwise({
      controller: '404Controller',
      template: '<div></div>'
    });
  }])
  .config(['$httpProvider', function ($httpProvider) {
    $httpProvider.defaults.headers.common = { 
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      };
  }])

  .controller('ResourcesController', [function() {

  }])
  .controller('404Controller', function() {
    window.location.href = '/not-found.html';
  })
  .run(['$rootScope', '$cookies', '$http',
        function ($rootScope, $cookies, $http) {

      $rootScope.globals = $cookies.get('globals') || {};
      if ($rootScope.globals.currentUser) {
          $http.defaults.headers.common['Authorization'] = 'Basic ' + $rootScope.globals.currentUser.authdata;
      }
  }]);

