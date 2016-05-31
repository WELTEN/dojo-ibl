angular.module('DojoIBL')
    .directive("scroll", function ($window) {
        return function (scope, element, attrs) {
            angular.element($window).bind("scroll", function () {
                if (this.pageYOffset >= 165) {
                    scope.boolChangeClass = true;
                    console.log('Scrolled below header.');
                } else {
                    scope.boolChangeClass = false;
                    console.log('Header is in view.');
                }
                scope.$apply();
            });

            /////
            // When scrolling move videos in activity view to the right side


        };
    })
;