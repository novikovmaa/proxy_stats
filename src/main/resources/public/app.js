/**
 * Created by novikovma on 8/27/2017.
 */
var app = angular.module('proxyapp',['ngSanitize', 'ngRoute']);
app.config(function ($routeProvider) {
    $routeProvider.when('/', {
        templateUrl:'/views/table.html',
        controller: 'statisticsCtrlr'
    })
});
app.controller('statisticsCtrlr', function($scope, $http, $timeout) {
    $scope.hosts = [];
    $scope.load = function(){
        $scope.isLoading = true;
        $http.get('/stats').success(function(data){
            $scope.hosts = data;
            $scope.isLoading = false;
            $timeout($scope.load, 5000);
        }).error(function(data){
            console.log('Error',data);
            $scope.isLoading = false;
        })
    }
    $scope.load();
})