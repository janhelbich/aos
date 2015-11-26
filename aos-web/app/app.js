'use strict';

// Declare app level module which depends on views, and components
  angular.module('aos', [
    'constants',
    'ngRoute',
    'aos.flight',
    'aos.destination',
    'aos.reservation'
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
  .service('sharedProps', [function() {
    var prop = '';

    return {
      getProperty: function() {
        return prop;
      },
      setProperty: function(value) {
        prop = value;
      }
    };

  }]);

