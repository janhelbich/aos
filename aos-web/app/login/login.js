

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

    service.signUp = function (username, password, callback) {

        $http.post(API_URL + '/login', {})
            .success(function (response) {
                callback(response);
            });

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
    AuthenticationService.ClearCredentials();

    $scope.signUp = function () {
        $scope.dataLoading = true;
        AuthenticationService.Login($scope.username, $scope.password, function(response) {
            if(response.success) {
                AuthenticationService.SetCredentials($scope.username, $scope.password);
                $location.path('/');
            } else {
                $scope.error = response.message;
                $scope.dataLoading = false;
            }
        });
      };
  }]);

})();