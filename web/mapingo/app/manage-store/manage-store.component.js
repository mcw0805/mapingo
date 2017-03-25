angular.module('manageStore').component('manageStore', {
    templateUrl: 'manage-store/manage-store.template.html',

    controller: ['$routeParams', '$route', '$firebaseObject', '$firebaseArray', function manageStoreController($routeParams, $route, $firebaseObject, $firebaseArray) {
        var self = this;
        var user = firebase.auth().currentUser;
        self.storeRef = firebase.database().ref().child("shops").child(user.uid);
        console.log(user.uid);
        self.storeName = "Loading...";
        self.newStoreName = "";

        self.nameRefObject = $firebaseObject(self.storeRef.child("name"));
        self.nameRefObject.$loaded(
            function(data) {
                val = data.$value;
                var defaultStoreName = user.displayName + "\'s store";
                self.storeName = defaultStoreName;
                if (val == null || val == "") {
                    // self.storeName = defaultStoreName;
                    self.storeRef.child("name").set(defaultStoreName);
                    $route.reload()
                } else {
                    self.storeName = val;
                }
            },
            function(error) {
                console.error("Error:", error);
            }
        );

        self.updateStoreName = function () {
            if (null != self.newStoreName && self.newStoreName != "") {
                self.storeRef.child("name").set(self.newStoreName);
                $route.reload();
            }
        }
    }]
});