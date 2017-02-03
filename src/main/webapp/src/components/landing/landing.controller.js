angular.module('DojoIBL')

    .controller('LandingController', function ($scope, Session, Oauth) {
        $scope.oauth = Session.getOauthType;
        $scope.accessToken = Session.getAccessToken();
        $scope.isLoggedIn = function () {
            if ($scope.accessToken() && $scope.oauth()) {

                return true;
            }
            else {

                return false;
            }
        };

        $scope.loading = true;

        var providers = {};
        Oauth.info().$promise.then(
            function (data) {
                for (var i = 0; i < data.oauthInfoList.length; i++) {
                    providers['prov' + data.oauthInfoList[i].providerId] = data.oauthInfoList[i];

                    if (data.oauthInfoList[i].providerId == 5){
                        $scope.wespotUrl = "https://wespot-arlearn.appspot.com/Login.html?client_id="+providers.prov5.clientId
                        +"&redirect_uri="+providers.prov5.redirectUri+"&response_type=code&scope=profile+email"
                    }
                }
                $scope.loading = false;
                console.log(providers);
            }
        );

        $scope.providerExists = function(providerId) {
            return (providers['prov'+providerId]!= null);
            //return true;
        };

        $scope.fbUrl = function(){
            if (!providers.prov1) return "";
            return "https://graph.facebook.com/oauth/authorize?client_id="+providers.prov1.clientId+
                "&display=page&redirect_uri="+providers.prov1.redirectUri+"&scope=email";
        };

        $scope.googleUrl = function(){
            return "https://accounts.google.com/o/oauth2/auth?redirect_uri="+providers.prov2.redirectUri
                +"&response_type=code&client_id="+providers.prov2.clientId+
                "&approval_prompt=force&scope=https://www.googleapis.com/auth/userinfo.profile%20%20https://www.googleapis.com/auth/userinfo.email";
        };

        $scope.wespotUrl = function(){
            return "https://wespot-arlearn.appspot.com/Login.html?client_id="+providers.prov5.clientId
                +"&redirect_uri="+providers.prov5.redirectUri+"&response_type=code&scope=profile+email";
        };
    }
);
