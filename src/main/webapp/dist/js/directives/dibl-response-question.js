angular.module('DojoIBL')

    .directive('responseQuestion', function ($compile, ResponseService, AccountService, $stateParams, UserService, $templateRequest) {

        var getTemplate = function(contentType){
            var template = '';

            switch(contentType){
                case true:
                    template = '/dist/templates/directives/responseQuestion.html';
                    break;
                case false:
                    template = '/dist/templates/directives/response.html';
                    break;
            }
            return template;
        };

        var linker = function (scope, element, attrs) {

            AccountService.myDetails().then(
                function(data){
                    scope.myAccount = data;
                }
            );

            UserService.getUserByAccount(scope.response.runId, scope.response.userEmail.split(':')[1]).then(function(data){
                scope.user = data;
            });

            scope.removeComment = function(data){
                ResponseService.deleteResponse(data.responseId);
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

            $templateRequest(getTemplate(scope.response.parentId == 0)).then(function(html){
                element.html(html).show();

                console.log(element.html(html))

                if(scope.response.parentId != 0){
                    console.log(scope.response.parentId)
                    $("div[data-item='"+scope.response.parentId+"']").append(element);

                }
                $compile(element.contents())(scope);
            });
        };

        return {
            restrict: "E",
            replace: true,
            scope: {
                response: '='
            },
            link: linker
        };
    }
);