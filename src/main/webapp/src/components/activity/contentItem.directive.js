angular.module('DojoIBL')

    .directive('contentItem', function($compile) {

        var template = {
            "imageTemplate": "<div class='entry-photo'><h2>&nbsp;</h2><div class='entry-img'><span><a href='{{rootDirectory}}{{content.data}}'><img ng-src='{{rootDirectory}}{{content.data}}' alt='entry photo'></a></span></div><div class='entry-text'><div class='entry-title'>{{content.title}}</div><div class='entry-copy'>{{content.description}}</div></div></div>",
            "videoTemplate": "<div class='entry-video'><h2>&nbsp;</h2><div class='entry-vid'><iframe ng-src='{{content.data}}' width='280' height='200' frameborder='0' webkitAllowFullScreen mozallowfullscreen allowFullScreen></iframe></div><div class='entry-text'><div class='entry-title'>{{content.title}}</div><div class='entry-copy'>{{content.description}}</div></div></div>",
            "noteTemplate": "<div class='entry-note'><h2>&nbsp;</h2><div class='entry-text'><div class='entry-title'>{{content.title}}</div><div class='entry-copy'>{{content.data}}</div></div></div>"
        }

        var getTemplate = function (templates, contentType) {
            var template = '';

            switch (contentType) {
                case 'image/png':
                    template = '<img ngf-src="multimedia" ng-repeat="" class="thumb">';
                    break;
                case 'video':
                    template = '<video ngf-src="multimedia" ng-repeat="" class="thumb"/>';
                    break;
            }

            return template;
        };

        var linker = function(scope, element, attrs){
            element.html(getTemplate(scope.content, scope.content.type));

            $compile(element.contents())(scope);
        }

        return  {
            restrict: 'E',
            link: linker,
            scope: {
                content: '='
            }
        };
    }

);