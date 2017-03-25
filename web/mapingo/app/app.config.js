angular.module('mapingoApp').config(['$locationProvider', '$routeProvider',
    function config($locationProvider, $routeProvider) {
        $locationProvider.hashPrefix('!');

        $routeProvider.when('/about', {
            template: '<about></about>'
        }).when('/manage-store', {
            template: '<manage-store></manage-store>'
        }).when('/manage-orders', {
            template: '<manage-orders></manage-orders>'
        }).when('/creepy-mode', {
            template: '<creepy-mode></creepy-mode>'
        }).when('/login', {
            template: '<login></login>'
        }).otherwise('/login');


        // use the HTML5 History API
        $locationProvider.html5Mode(true);
    }

]);