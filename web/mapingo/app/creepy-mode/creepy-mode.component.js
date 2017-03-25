angular.module('creepyMode').component('creepyMode', {
    templateUrl: 'creepy-mode/creepy-mode.template.html',

    controller: ['$routeParams', '$route', '$firebaseObject', '$firebaseArray', function creepyModeController($routeParams, $route, $firebaseObject, $firebaseArray) {
        var self = this;
        var user = firebase.auth().currentUser;
        self.creepyModeRef = firebase.database().ref().child("users").child(user.uid).child("creepy-mode");
    }]
});