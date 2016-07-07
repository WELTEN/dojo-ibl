angular.module('DojoIBL')
    .directive('response', function ($compile, ResponseService, AccountService, $stateParams, UserService, ActivityService) {
        return {
            restrict: "E",
            replace: true,
            scope: {
                response: '='
            },
            templateUrl: '/dist/templates/directives/response.html',
            link: function (scope, element, attrs) {

                AccountService.myDetails().then(
                    function(data){
                        scope.myAccount = data;
                    }
                );

                scope.getUser = function (response){
                    return UserService.getUserFromCache(response.userEmail.split(':')[1]).name;
                };
                scope.getAvatar = function (response){
                    return UserService.getUserFromCache(response.userEmail.split(':')[1]).picture;
                };

                scope.removeComment = function(data){
                    ResponseService.deleteResponse(data.responseId);
                };

                scope.share = function(data){

                    var act = ActivityService.getItemFromCache(data.generalItemId);
                    //console.log(data, act);

                    var controllerElement = document.querySelector('[ng-controller=InstantMessagingController]');
                    var controllerScope = angular.element(controllerElement).scope();
                    controllerScope.bodyMessage = act.name+"@"+UserService.getUserFromCache(data.userEmail.split(':')[1]).name+": "+data.responseValue;

                    //controllerScope.bodyMessage = $compile('<b>'+UserService.getUserFromCache(data.userEmail.split(':')[1]).name+'</b>')(scope).html();
                    //console.log($compile('<b>'+UserService.getUserFromCache(data.userEmail.split(':')[1]).name+'</b>')(scope).html())
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
                        });
                    });
                };

                $("div[data-item='"+scope.response.parentId+"']").parent().append(element);
            }
        };
    }
);