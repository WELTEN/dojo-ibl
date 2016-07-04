angular.module('DojoIBL')

    .directive('responseQuestions', function() {
        return  {
            restrict: "E",
            replace: true,
            scope: {
                responses: '='
            },
            template: "<response-question ng-repeat='response in responses | orderByDayNumber:\"lastModificationDate\"' response='response'></response-question>"
        };
    })

;