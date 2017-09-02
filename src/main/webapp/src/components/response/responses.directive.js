angular.module('DojoIBL')

    .directive('responses', function(AccountService) {
        return  {
            restrict: "E",
            replace: true,
            scope: {
                response: '='
            },
            templateUrl: '/src/components/response/response.template.html',
            link: function (scope, element, attrs) {

                //console.log(scope);

                //scope.sendComment = function(responseParent, responseChildren) {
                //
                //    AccountService.myDetails().then(function(data){
                //
                //        scope.responses.$add({
                //            "type": "org.celstec.arlearn2.beans.run.Response",
                //            "runId": scope.responseChildren.runId,
                //            "deleted": false,
                //            "generalItemId": scope.generalItemId.activityId,
                //            "userAccountType": data.accountType,
                //            "userLocalId": data.localId,
                //            "userName": data.name,
                //            "userProfile": data.picture,
                //            "responseValue": $scope.responseText,
                //            "parentId": scope.responseParent.$id,
                //            "revoked": false,
                //            "lastModificationDate": firebase.database.ServerValue.TIMESTAMP
                //        });
                //
                //    });
                //};

                //if (angular.isArray(scope.response.children)) {
                //    element.append("<collection collection='member.children'></collection>");
                //    $compile(element.contents())(scope)
                //}
            }
        };
    })

;