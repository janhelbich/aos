'use strict';

// Declare app level module which depends on views, and components
angular.module('aos', [
  'ngRoute',
  'aos.flight'
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
.controller('ResourcesController', [function() {

}])
.controller('404Controller', function() {
  window.location.href = '/error.html';
});
