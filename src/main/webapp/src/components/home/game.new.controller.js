angular.module('DojoIBL')

    .controller('InquiryNewGameController', function ($scope, $sce, $stateParams, GameService, $http, $modal) {


        $http.get('/src/assets/resources/catalogue.json')
            .then(function(res){
                $scope.catalogue = res.data;
            });


        $scope.createNewSelInquiry = function (template) {

            var modalInstance = $modal.open({
                templateUrl: '/src/components/common/new.inquiry.modal.html',
                controller: 'SelInqCtrl',
                resolve: {
                    structure: function(){
                        return template;
                    }
                }
            });
        };
    })

    .controller('SelInqCtrl', function ($scope, $modalInstance, GameService, ActivityService, config, structure ) {

        $scope.ok = function () {
            $scope.game.deleted = false;
            $scope.game.revoked = false;

            $scope.game.phases = [];
            angular.forEach(structure.phases, function(phase){
                $scope.game.phases.push({
                    title: phase.phase,
                    type: "org.celstec.arlearn2.beans.game.Phase"
                });
            });

            GameService.newGame($scope.game).then(function(data){
                $scope.game = data;
                $scope.game.config.roles = [];
                window.location.href=config.server+'/#/inquiry/'+data.gameId+'/edit';
            });
            $modalInstance.close();
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');


        };

    })

;