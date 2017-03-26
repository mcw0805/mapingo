angular.module('creepyNavBar').component('creepyNavBar', {
    templateUrl: 'creepy-nav-bar/creepy-nav-bar.template.html',

    controller: ['$window', '$scope', '$location', function creepyNavBarController($window, $scope, $location) {
        var self = this;
        updateNavBar($location, self);

        $scope.$on('$routeChangeSuccess', function () {
            updateNavBar($location, self);
        });

        this.signOut = function () {
            console.log("BEFORE SIGNOUT:" + firebase.auth().currentUser.displayName);
            firebase.auth().signOut().then(function() {
                // Sign-out successful.
                console.log("SIGNED OUT");
                // $location.url('/manage-classes');
                $window.location.href = '/login';
                $location.path('/login');
            }, function(error) {
                // An error happened.
            });
        }
    }

    ]
});

function updateNavBar(location, self) {
    self.path = location.path();
    self.url = location.url();
    self.manageStore = /manage-store$/.test(self.path);
    self.manageOrders = /manage-orders$/.test(self.path);
    self.creepyMode = /creepy-mode$/.test(self.path);
    self.about = /about$/.test(self.path);
}