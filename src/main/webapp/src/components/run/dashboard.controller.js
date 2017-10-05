angular.module('DojoIBL')

    .controller('DashboardController', function ($scope, $sce, $stateParams, $state, Response,
                                                ActivityService, UserService, GameService, RunService, AccountService,
                                                ChannelService, $location) {

        $scope.resizeIframe = function (obj) {
            obj.style.height = obj.contentWindow.document.body.scrollHeight + 'px';

        }
    }
);