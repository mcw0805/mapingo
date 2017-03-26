angular.module('creepyMode').component('creepyMode', {
    templateUrl: 'creepy-mode/creepy-mode.template.html',

    controller: ['$routeParams', '$route', '$firebaseObject', '$firebaseArray', function creepyModeController($routeParams, $route, $firebaseObject, $firebaseArray) {
        var self = this;
        var user = firebase.auth().currentUser;
        self.ordersRef = firebase.database().ref().child("shops").child(user.uid).child("orders");
        self.ordersArray = $firebaseArray(self.ordersRef);

        self.deleteOrder = function (orderKey) {
            self.ordersRef.child(orderKey).set(null);
        }
    }]
});