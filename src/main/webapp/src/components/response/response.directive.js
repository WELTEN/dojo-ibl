angular.module('DojoIBL')
    .directive('response', function ($compile, ResponseService, AccountService, $stateParams, UserService, ActivityService,RunService) {
        return {
            restrict: "E",
            replace: true,
            scope: {
                response: '='
            },
            templateUrl: '/src/components/response/response.template.html',
            link: function (scope, element, attrs) {

                AccountService.myDetails().then(
                    function(data){
                        scope.myAccount = data;
                    }
                );

                UserService.getUserByAccount(scope.response.runId, scope.response.userEmail.split(':')[1]).then(function(data){
                    scope.user = data;
                });

                scope.removeComment = function(data){
                    swal({
                        title: "Are you sure?",
                        text: "You will not be able to recover it in the future!",
                        type: "warning",
                        showCancelButton: true,
                        confirmButtonColor: "#DD6B55",
                        confirmButtonText: "Yes, remove it!",
                        closeOnConfirm: false
                    }, function () {
                        swal("Removed!", "Your contribution has been removed from the inquiry", "success");
                        ResponseService.deleteResponse(data.responseId);
                    });
                };

                scope.share = function(data){

                    RunService.getRunById(data.runId).then(function (run) {
                        ActivityService.getActivityById(data.generalItemId, run.gameId).then(function(act){
                            var controllerElement = document.querySelector('[ng-controller=InstantMessagingController]');
                            var controllerScope = angular.element(controllerElement).scope();
                            controllerScope.bodyMessage = act.name+"@"+UserService.getUserFromCache(data.userEmail.split(':')[1]).name+": "+data.responseValue;
                        });
                    });
                };

                scope.sendChildComment = function(responseParent, responseChildren) {

                    AccountService.myDetails().then(function(data){
                        ResponseService.newResponse({
                            "type": "org.celstec.arlearn2.beans.run.Response",
                            "runId": $stateParams.runId,
                            "deleted": false,
                            "generalItemId": $stateParams.activityId,
                            "userEmail": data.accountType+":"+data.localId,
                            "responseValue": responseChildren,
                            "parentId": responseParent.responseId,
                            "revoked": false,
                            "lastModificationDate": new Date().getTime()
                        }).then(function(childComment){
                            scope.responseChildren = null;
                            scope.hiddenDiv = false;
                        });
                    });
                };

                //console.log(scope.response);

                $("div[data-item='"+scope.response.parentId+"']").parent().append(element);
            }
        };
    }
);