angular.module('manageStore').component('manageStore', {
    templateUrl: 'manage-store/manage-store.template.html',

    controller: ['$routeParams', '$route', '$firebaseObject', '$firebaseArray', function manageStoreController($routeParams, $route, $firebaseObject, $firebaseArray) {
        var self = this;
        var user = firebase.auth().currentUser;
        console.log(user.uid);
        self.storeRef = firebase.database().ref().child("shops").child(user.uid);

        self.storeName = "Loading...";
        self.newStoreName = "";

        self.nameRefObject = $firebaseObject(self.storeRef.child("name"));
        self.nameRefObject.$loaded(
            function (data) {
                var val = data.$value;
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
            function (error) {
                console.error("Error:", error);
            }
        );

        self.updateStoreName = function () {
            if (null != self.newStoreName && self.newStoreName != "") {
                self.storeRef.child("name").set(self.newStoreName);
                $route.reload();
            }
        };

        self.latitude = "Loading...";
        self.longitude = "Loading...";
        self.newLatitude = null;
        self.newLongitude = null;

        self.latitudeRefObject = $firebaseObject(self.storeRef.child("latitude"));
        self.latitudeRefObject.$loaded(
            function (data) {
                var val = data.$value;
                var defaultLatitude = 33.7925;
                self.latitude = defaultLatitude;
                if (val == null || val == "") {
                    self.storeRef.child("latitude").set(defaultLatitude);
                    $route.reload()
                } else {
                    self.latitude = val;
                }
            },
            function (error) {
                console.error("Error:", error);
            }
        );

        self.longitudeRefObject = $firebaseObject(self.storeRef.child("longitude"));
        self.longitudeRefObject.$loaded(
            function (data) {
                var val = data.$value;
                var defaultlongitude = 84.3240;
                self.longitude = defaultlongitude;
                if (val == null || val == "") {
                    self.storeRef.child("longitude").set(defaultlongitude);
                    $route.reload()
                } else {
                    self.longitude = val;
                }
            },
            function (error) {
                console.error("Error:", error);
            }
        );

        self.updateStoreLocation = function () {
            if (null != self.newLatitude && self.newLatitude != "" && null != self.newLongitude && self.newLongitude != "") {
                self.storeRef.child("latitude").set(self.newLatitude);
                self.storeRef.child("longitude").set(self.newLongitude);
                $route.reload();
            }
        };

        self.numOrders = "Loading...";
        self.newNumOrders = "";

        self.numOrdersObject = $firebaseObject(self.storeRef.child("num-orders"));
        self.numOrdersObject.$loaded(
            function (data) {
                var val = data.$value;
                var defaultNumOrders = 90;
                self.numOrders = defaultNumOrders;
                // if (val == null || val == "") {
                //     self.storeRef.child("num-orders").set(defaultNumOrders);
                //     $route.reload()
                // } else {
                //     self.numOrders = val;
                // }
                self.numOrders = val;
                // $route.reload()
            },
            function (error) {
                console.error("Error:", error);
            }
        );

        self.resetNumOrders = function () {
            self.storeRef.child("num-orders").set(0);
            $route.reload()
        };

        self.menuRef = self.storeRef.child("menu");
        self.menuItemsArray = $firebaseArray(self.menuRef);
    }]
});