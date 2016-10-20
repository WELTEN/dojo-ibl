angular.module('DojoIBL')

    .controller('SidebarController', function ($scope, $rootScope, $modal, Session, $state, $stateParams, GameService, RunService, Account, AccountService) {

        $scope.$on("$stateChangeSuccess", function updatePage() {
            $scope.phaseNumber = $state.params.phase;
            $scope.statePhase = $state.current.name;
        });

        if ($stateParams.runId) {

            RunService.getRunById($stateParams.runId).then(function(data){
                $scope.run = data;
                AccountService.myDetails().then(
                    function(data){
                        $scope.myAccount = data;
                        //console.log(data)
                        //loadAccessRules();
                    }
                );

            });
        }else{
            AccountService.myDetails().then(
                function(data){
                    $scope.myAccount = data;
                    //console.log(data)
                    //loadAccessRules();
                }
            );
        }

        $scope.createNewInquiry = function () {

            var modalInstance = $modal.open({
                templateUrl: '/src/components/common/new.inquiry.modal.html',
                controller: 'NewInqCtrl'
            });

            //modalInstance.result.then(function (result){
            //    angular.extend($scope.usersRun[$scope.runVar], result);
            //});
        };
    })
        .controller('NewInqCtrl', function ($scope, $modalInstance, GameService, ActivityService) {

            $scope.ok = function () {
                $scope.game.deleted = false;
                $scope.game.revoked = false;

                var __phases = ["Problem statement", "Plan the method", "Collect the data", "Analyse the data", "Interpret the findings", "Communicate the results"];

                if($scope.game.template){
                    $scope.game.phases = [];
                    angular.forEach(__phases, function(phase){
                        $scope.game.phases.push({
                            title: phase,
                            type: "org.celstec.arlearn2.beans.game.Phase"
                        });
                    });
                }

                GameService.newGame($scope.game).then(function(data){
                    $scope.game = data;
                    $scope.game.config.roles = [];
                    $scope.phases = $scope.game.phases;

                    var __activity = [{
                        "type": "org.celstec.arlearn2.beans.generalItem.AudioObject",
                        "gameId": $scope.game.gameId,
                        "deleted": false,
                        "sortKey": 1,
                        "scope": "user",
                        "name": "Research question",
                        "description": "",
                        "autoLaunch": false,
                        "section": "0",
                        "roles": ["null"],
                        "fileReferences": [],
                        "richText": "<p><span style=\"color: rgb(51, 51, 51);\">Individually you need to bring 3 relevant questions about your topic. One of them could be more generic and the other 2 can be sub-questions of the other one.<\/span><br\/><\/p><div class=\"alert alert-info ng-scope\" style=\"color: rgb(49, 112, 143);background-color: rgb(217, 237, 247);\"><strong>Goal:<\/strong><span class=\"Apple-converted-space\">Â This is an individual work. Everyone should bring his or her own research questions and later decide which ones are the most interesting ones in order to use them in the following steps of the inquiry.<\/span><\/div><div class=\"alert alert-warning ng-scope\" style=\"color: rgb(138, 109, 59);background-color: rgb(252, 248, 227);\"><strong>Tip:<\/strong><span class=\"Apple-converted-space\">Â Be aware of that the questions we are looking should be good enough. You don't want weak research questions. You need to find you <b>essential question<\/b>. Follow the link to know more about this:Â <a href=\"http:\/\/www.ascd.org\/publications\/books\/109004\/chapters\/What-Makes-a-Question-Essential%A2.aspx\" target=\"\">http:\/\/www.ascd.org\/publications\/books\/109004\/chapters\/What-Makes-a-Question-Essential%A2.aspx<\/a><\/span><\/div><div class=\"alert alert-warning ng-scope\" style=\"color: rgb(138, 109, 59);background-color: rgb(252, 248, 227);\">You do not understand what a <b>research questions <\/b>is? Ask google, <b>it is very important for you to know this.<\/b><\/div>",
                        "audioFeed": "example link"
                    }];

                    ActivityService.newActivity(__activity[0]);
                });
                $modalInstance.close();
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };

        })
;