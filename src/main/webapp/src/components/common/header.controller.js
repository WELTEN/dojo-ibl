angular.module('DojoIBL')

    .controller('HeaderController', function ($scope, Session, $translate,$state, $location, config, firebase) {
        $scope.test = "foo";

        $scope.name = "your name";

        $scope.flags = [{
            icon: 'src/assets/img/i18n/Spain.png',
            name: 'Spanish',
            id: 'es'
        }, {
            icon: 'src/assets/img/i18n/United-States.png',
            name: 'English',
            id: 'en-US'
        }, {
            icon: 'src/assets/img/i18n/Netherlands.png',
            name: 'Dutch',
            id: 'nl'
        }, {
            icon: 'src/assets/img/i18n/Bulgaria.png',
            name: 'Bulgarian',
            id: 'bg'
        }];

        if (Session.getAccessToken()) {
            //Account.accountDetails().$promise.then(
            //    function(data){
            //        $scope.pictureUrl = data.picture;
            //        $scope.name = data.name;
            //    }
            //);
        }

        $scope.setLanguage = function (lang) {
            $translate.use(lang);
            localStorage.setItem('i18', lang);
        };



        $scope.signout = function() {

            firebase.auth().signOut().then(function() {

                console.log("logout")


            }).catch(function(error) {
                // An error happened.
            });

            Session.reset();
            document.cookie =  'arlearn.AccessToken=; expires=Thu, 01 Jan 1970 00:00:01 GMT;path=/';
            window.location.href=config.server+'/#/login';
        }
    }
);