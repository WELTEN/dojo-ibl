angular.module('DojoIBL')

    .directive('responses', function() {
        return  {
            restrict: "E",
            replace: true,
            scope: {
                responses: '='
            },
            template: "<response ng-repeat='response in responses | orderByDayNumber:\"lastModificationDate\"' response='response'></response>"
        };
    })

;