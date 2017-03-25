angular.module('manageStore').component('manageStore', {
    templateUrl: 'manage-store/manage-store.template.html',

    controller: ['$routeParams', '$route', '$firebaseObject', '$firebaseArray', function manageStoreController($routeParams, $route, $firebaseObject, $firebaseArray) {
        var self = this;
        var user = firebase.auth().currentUser;
        self.manageStoreRef = firebase.database().ref().child("users").child(user.uid).child("manage-store");
    }]
});