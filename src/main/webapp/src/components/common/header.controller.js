angular.module('DojoIBL')

    .controller('HeaderController', function ($scope, Session, $translate,$state, $location, config) {
        $scope.test = "foo";

        $scope.name = "your name";

        $scope.flags = [{
            icon: 'src/assets/img/i18n/Spain.png',
            name: 'Spanish',
            id: 'es'
        }, {
            icon: 'src/assets/img/i18n/United-States.png',
            name: 'English',
            id: 'en'
        }, {
            icon: 'src/assets/img/i18n/Netherlands.png',
            name: 'Dutch',
            id: 'nl'
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
        };

        $scope.signout = function() {
            Session.reset();
            document.cookie =  'arlearn.AccessToken=; expires=Thu, 01 Jan 1970 00:00:01 GMT;path=/';
            window.location.href=config.server+'/index.html';
        }
    }
);