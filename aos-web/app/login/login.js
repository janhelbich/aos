

(function() {
angular.module('aos.login', ['ngRoute', 'ngCookies', 'constants'])

  // ---------------------------------------------
  // ----------- APPLICATION CONFIG --------------
  // ---------------------------------------------
  .config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/login', {
      templateUrl: 'login/login.html',
      controller: 'LoginController',
      controllerAs: 'loginCtrl'
    });
  }])

  .factory('AuthenticationService', ['$http', '$cookies', '$rootScope', 'API_URL',
                                    function ($http, $cookies, $rootScope, API_URL) {
    var service = {};

    service.signUp = function (username, password, succ, err) {

        $http.post(API_URL + '/signUp', {}, {
          headers: {'Authorization': 'Basic ' + btoa(username + ':' + password)}
        }).then(succ, err);

    };

    service.setCredentials = function (username, password) {
        var authdata = btoa(username + ':' + password);

        $rootScope.globals = {
            currentUser: {
                username: username,
                authdata: authdata
            }
        };

        $http.defaults.headers.common['Authorization'] = 'Basic ' + authdata;
        $cookies.put('globals', $rootScope.globals);
    };

    service.clearCredentials = function () {
        $rootScope.globals = {};
        $cookies.remove('globals');
        $http.defaults.headers.common.Authorization = 'Basic ';
    };

    return service;
  }])

  // ---------------------------------------------
  // --------- APPLICATION CONTROLLERS -----------
  // ---------------------------------------------
  .controller('LoginController', ['$scope', '$rootScope', '$location', 'AuthenticationService',
                                  function ($scope, $rootScope, $location, AuthenticationService) {
    // reset login status
    AuthenticationService.clearCredentials();

    $scope.signUp = function () {
        AuthenticationService.signUp($scope.username, $scope.password, 
          function(succResp) {
            AuthenticationService.setCredentials($scope.username, $scope.password);
            $location.path('/');
          }, 
          function(errResp) {
            $scope.error = errResp.message;
          });
      };
  }]);

})();