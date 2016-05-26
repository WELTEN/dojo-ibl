angular.module('DojoIBL')

    .controller('InquiryEditGameController', function ($scope, $sce, $stateParams, $state, Session, RunService, ActivityService, GameService ) {

        GameService.getGameById($stateParams.gameId).then(function(data){
            if (data.error) {
                $scope.showNoAccess = true;
            } else {
                $scope.show = true;
            }

            $scope.game = data;

            if(!$scope.game.config.roles)
                $scope.game.config.roles = [];
            else{
                //$scope.game.config.roles = new JSONObject(data.config.roles);

            }

            if (data.lat) {
                $scope.coords.latitude = data.lat;
                $scope.coords.longitude = data.lng;
                $scope.map.center.latitude = data.lat;
                $scope.map.center.longitude = data.lng;
                $scope.showMap = true;
            }

            // Original list of activities
            $scope.list_original = [
                {'name': 'Google Resources', 'type': 'org.celstec.arlearn2.beans.generalItem.AudioObject', 'icon': 'fa-file-text'},
                {'name': 'Text activity', 'type': 'org.celstec.arlearn2.beans.generalItem.NarratorItem', 'icon': 'fa-file-text'},
                {'name': 'Youtube video', 'type': 'org.celstec.arlearn2.beans.generalItem.VideoObject', 'icon': 'fa-external-link'},
                {'name': 'Concept map', 'type': 'org.celstec.arlearn2.beans.generalItem.SingleChoiceImageTest', 'icon': 'fa-sitemap'},
                {'name': 'External widget', 'type': 'org.celstec.arlearn2.beans.generalItem.OpenBadge', 'icon': 'fa-link'},
                {'name': 'Research question', 'type': 'org.celstec.arlearn2.beans.generalItem.ResearchQuestion', 'icon': 'fa-question'}
            ];

            // Managing different tabs activities
            $scope.lists = [];

            angular.forEach($scope.game.phases, function(value, key) {
                $scope.lists[key] = [];
                ActivityService.getActivitiesForPhase($stateParams.gameId, key).then(function(data){
                    angular.forEach(data, function(i,a){
                        $scope.lists[key].push(i);
                    });
                });



            });

        });

        $scope.selected = false;
        $scope.activity = {};

        $scope.saveActivity = function(){
            ActivityService.newActivity($scope.activity);
        };


        $scope.selectActivity = function(activity){
            $scope.activity = activity;
            $scope.selected = true;

            $scope.removeActivity = function(){
                ActivityService.deleteActivity($scope.activity.gameId, $scope.activity.id);
            };

        };
        $scope.dragStartCallback = function(evnet, ui, item) {
            if (!$scope.dragItem) {
                console.debug('dragStartCallback');
                $scope.dragItem = item;
                //toggleDropArea(true);
                //if (!$scope.$$phase && !$scope.$root.$$phase) {
                //    $scope.$apply();
                //}
            }
        };

        $scope.dropCallback = function(event, ui, item) {

            if(ui.draggable.attr('data') == "org.celstec.arlearn2.beans.generalItem.VideoObject"){
                $scope.activity = ActivityService.newActivity({
                    type: ui.draggable.attr('data'),
                    section: $(event.target).attr('data'),
                    gameId: $stateParams.gameId,
                    deleted: true,
                    name: ui.draggable.attr('name'),
                    description: "",
                    autoLaunch: false,
                    fileReferences: [],
                    sortKey: 1,
                    videoFeed: "example link",
                    richText: ""
                });

            }else if(ui.draggable.attr('data') == "org.celstec.arlearn2.beans.generalItem.AudioObject") {
                $scope.activity = ActivityService.newActivity({
                    type: ui.draggable.attr('data'),
                    section: $(event.target).attr('data'),
                    gameId: $stateParams.gameId,
                    deleted: true,
                    name: ui.draggable.attr('name'),
                    description: "",
                    autoLaunch: false,
                    fileReferences: [],
                    sortKey: 1,
                    audioFeed: "example link",
                    richText: ""
                });

            }else{
                $scope.activity = ActivityService.newActivity({
                    type: ui.draggable.attr('data'),
                    section: $(event.target).attr('data'),
                    gameId: $stateParams.gameId,
                    deleted: true,
                    name: ui.draggable.attr('name'),
                    description: "",
                    autoLaunch: false,
                    fileReferences: [],
                    sortKey: 1,
                    richText: ""
                });
            }

            console.log($scope.activity);
        };

        $scope.dragStopCallback = function() {
            console.debug('dragStop');

            //$timeout(function() {
            //    if ($scope.dragItem) {
            //        $scope.dragItem = undefined;
            //        toggleDropArea(false);
            //        if (!$scope.$$phase && !$scope.$root.$$phase) {
            //            $scope.$apply();
            //        }
            //    }
            //}, 500);
        };


        $scope.addRole = function () {

            $scope.game.config.roles.push({
                name: $scope.roleName
            });

            GameService.newGame($scope.game);

            $scope.roleName = "";

        };

        $scope.selectRole = function(index){

            $scope.removeRole = function(){
                $scope.game.config.roles.splice(index, 1);
                GameService.newGame($scope.game);
            };

        };

        $scope.addPhase = function(){

            swal({
                title: "New phase",
                text: "Provide a new for the phase",
                type: "input",
                showCancelButton: true,
                closeOnConfirm: true,
                animation: "slide-from-top",
                inputPlaceholder: "Example: Data collection"
            }, function (inputValue) {
                if (inputValue === false)
                    return false;
                if (inputValue === "") {
                    swal.showInputError("You need to write something!");
                    return false
                }

                $scope.game.phases.push({
                    phaseId: 1,
                    title: inputValue,
                    type: "org.celstec.arlearn2.beans.game.Phase"
                });

                GameService.newGame($scope.game);
                //RunService.newRun($scope.run);

            });
        };

        $scope.removePhase = function(index){

            $scope.game.phases.splice(index, 1);

            GameService.newGame($scope.game);
            //RunService.newRun($scope.run);
        };

        $scope.ok = function(){
            GameService.newGame($scope.game);
            //$window.history.back();
        };


        //$scope.chat = true;
        //$scope.visualization = true;
        //$scope.state = $state.current.name;
        //
        //RunService.getRunById($stateParams.runId).then(function (data) {
        //    $scope.inqTitle = data.title;
        //    $scope.inqId = data.runId;
        //    $scope.phases = data.game.phases;
        //    console.log(data);
        //});

        //var radius = 170;
        //var fields = $(this.el).find('#circlemenu li'),
        //    container = $(this.el).find('#circlemenu'),
        //    width = container.width(),
        //    height = container.height(),
        //    angle = 300,
        //    step = (2*Math.PI) / fields.length;
        //
        ////console.log(fields, container);
        //
        //fields.each(function() {
        //
        //    //console.log(width, $(this).width());
        //
        //    var x = Math.round(width/2 + radius * Math.cos(angle) - $(this).width()/2);
        //    var y = Math.round(height/2 + radius * Math.sin(angle) - $(this).height()/2);
        //    //console.log(x,y);
        //    if(window.console) {
        //        //console.log($(this).text(), x, y);
        //    }
        //    $(this).css({
        //        left: x + 'px',
        //        top: y + 'px'
        //    });
        //    angle += step;
        //});

    }).controller('TabController', function ($scope, $stateParams, GameService, ActivityService) {
        this.tab = 0;


        GameService.getGameById($stateParams.gameId).then(function (data) {

            if (data.config.roles)
                $scope.roles = data.config.roles;

            $scope.game = data;

        });

        this.setTab = function (tabId) {
            this.tab = tabId;
            ActivityService.getActivitiesForPhase($scope.game.gameId, tabId).then(function (data) {
                $scope.activities = data;
            });
        };

        this.isSet = function (tabId) {
            return this.tab === tabId;
        };
    })
;