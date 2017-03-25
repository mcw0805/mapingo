angular.module('manageOrders').component('manageOrders', {
    templateUrl: 'manage-orders/manage-orders.template.html',

    controller: ['$routeParams', '$route', '$firebaseObject', '$firebaseArray', function manageOrdersController($routeParams, $route, $firebaseObject, $firebaseArray) {
        var self = this;
        var user = firebase.auth().currentUser;
        self.manageOrdersRef = firebase.database().ref().child("users").child(user.uid).child("manage-orders");
    }]
});