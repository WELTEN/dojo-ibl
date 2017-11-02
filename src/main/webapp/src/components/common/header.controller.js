angular.module('DojoIBL')

    .controller('HeaderController', function ($scope, Session, $translate,$state, $location, config, firebase, Account, NotificationService) {
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
        }, {
            icon: 'src/assets/img/i18n/Greece.png',
            name: 'Greek',
            id: 'gr'
        }];

        if (Session.getAccessToken()) {
            Account.accountDetails().$promise.then(
                function(data){
                    $scope.pictureUrl = data.picture;
                    $scope.name = data.name;

                    $scope.notifications = NotificationService.showNotifications(data.localId);
                }
            );
        }

        $scope.setLanguage = function (lang) {
            $translate.use(lang);
            localStorage.setItem('i18', lang);
        };

        $scope.notificationRead = function(not){

            Account.accountDetails().$promise.then(
                function(data){
                    NotificationService.notificationRead(data.localId, not);
                }
            );
        }

        $scope.signout = function() {

            var config = {
                apiKey: "AIzaSyBqdNjX3HNZStyuBklf3FQRGxRLTAbb2hc",
                authDomain: "dojo-ibl.firebaseapp.com",
                databaseURL: "https://dojo-ibl.firebaseio.com",
                projectId: "dojo-ibl",
                storageBucket: "dojo-ibl.appspot.com",
                messagingSenderId: "518897628174"
            };

            if (!firebase.apps.length) {
                firebase.initializeApp(config);
            }

            Session.reset();

            firebase.auth().signOut().then(function() {

            }).catch(function(error) {
                console.log(error)
            });




            document.cookie =  'arlearn.AccessToken=; expires=Thu, 01 Jan 1970 00:00:01 GMT;path=/';
            window.location.href='/#/login';
        }
    }
);