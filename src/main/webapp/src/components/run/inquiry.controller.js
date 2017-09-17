angular.module('DojoIBL')

    .controller('InquiryController', function ($scope, $sce, $location, $stateParams, $state, Session, MessageService,
                                               ActivityService, AccountService, ChannelService, RunService, ActivityStatusService) {

        if(!Session.getAccessToken()){
            window.location.href='/#/login';
        }

        $scope.chat = true;
        $scope.visualization = true;
        $scope.state = $state.current.name;
        $scope.inqTitle = "";

        $scope.disableInquiryLoading = false;

        RunService.getRunById($stateParams.runId).then(function (data) {
            $scope.inqTitle = data.title;
            $scope.inqTempTitle = data.game.title;
            $scope.inqDescription = data.game.description;
            $scope.inqId = data.runId;
            $scope.phases = data.game.phases;
            $scope.code = data.code;
            $scope.serverCreationTime = data.serverCreationTime;
            $scope.disableInquiryLoading = true;

            angular.forEach(data.game.phases, function(value, key) {
                ActivityStatusService.getActivitiesServerStatus(data.game.gameId, $stateParams.runId, key);
            });

        });

        $scope.activities = ActivityStatusService.getActivitiesStatus();

        $scope.goToPhase = function(inqId, index) {
            $location.path('inquiry/'+inqId+'/phase/'+ index);
        };

        $scope.goToActivity = function(inqId, index, activity) {
            $location.path('inquiry/'+inqId+'/phase/'+ index + '/activity/' +activity);
        };




        $scope.getRoleName = function(roles){
            if(!angular.isUndefined(roles)) {
                try{
                    if (!angular.isUndefined(roles[0])) {
                        return roles[0].name;
                    }
                }catch(e){
                    return "-";
                }
            }
            return "-";
        };

        $scope.getRoleColor = function(roles){

            if(!angular.isUndefined(roles)) {
                try{
                    if (!angular.isUndefined(roles[0])) {
                        return {
                            "border-left": "3px solid "+roles[0].color
                        };
                    }
                }catch(e){
                    return {
                        "border-left": "3px solid #2f4050"
                    };
                }
            }
            return {
                "border-left": "3px solid #2f4050"
            };
        };

        var fields = $(this.el).find('#circlemenu li'),
            container = $('#circlemenu'),
            width = container.width(),
            height = 200,
            angle = 300,
            radius = 100;

        $scope.calculateLeft = function(i) {

            angle += (2*Math.PI) / $scope.phases.length;

            angle *= i;
            //console.log(angle, width, height)


            return Math.round(width/2 + radius * Math.cos(angle) - width/2);
        };

        $scope.calculateTop = function(phases, i) {
            angle += (2*Math.PI) / phases.length;

            angle *= i;

            //console.log(angle, height)

            return Math.round(height/2 + radius * Math.sin(angle) - height/2);
        };

    }
);