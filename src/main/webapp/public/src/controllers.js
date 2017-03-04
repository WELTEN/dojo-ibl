angular.module('DojoIBL')

    .controller('ProfileController', ["$scope", "$sce", "$stateParams", "$state", "Session", "AccountService", "Upload", "config", function ($scope, $sce, $stateParams, $state, Session, AccountService, Upload, config) {
        AccountService.myDetails().then(function(data){
            $scope.myAccount = data;
            $scope.nameValue = $scope.myAccount.familyName+" "+$scope.myAccount.givenName;
        });

        AccountService.accountDetailsById($stateParams.fullId).then(function(data){
            $scope.user = data;
        });

        $scope.ok = function(){
            console.log($scope.user)
            AccountService.update({
                "type": "org.celstec.arlearn2.beans.account.Account",
                "accountType": $scope.myAccount.accountType,
                "localId": $scope.myAccount.localId,
                "email": $scope.myAccount.accountType+":"+$scope.myAccount.localId,
                "picture": $scope.myAccount.picture,
                "name": $scope.user.name
            });
        };

        $scope.upload = function (file) {
            $scope.progressPercentage = 0;

            AccountService.uploadUrl($scope.myAccount.accountType+":"+$scope.myAccount.localId, file.name).$promise.then(function(url){
                console.log(url);
                Upload.upload({
                    url: url.uploadUrl,
                    data: {file: file, 'username': $scope.username}
                }).then(function (resp) {
                    AccountService.update({
                        "type": "org.celstec.arlearn2.beans.account.Account",
                        "accountType": $scope.myAccount.accountType,
                        "localId": $scope.myAccount.localId,
                        "email": $scope.myAccount.accountType+":"+$scope.myAccount.localId,
                        "name": $scope.myAccount.familyName+" "+$scope.myAccount.givenName,
                        "picture": config.server+"/uploadUserContent/"+resp.config.data.file.name+"?account="+$scope.myAccount.accountType+":"+$scope.myAccount.localId
                    });

                    console.log(resp)
                    console.log('Success ' + resp.config.data.file.name + 'uploaded. Response: ' + resp.data);

                }, function (resp) {
                    console.log('Error status: ' + resp.status);
                }, function (evt) {
                    var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                    $scope.progressPercentage = progressPercentage;
                    console.log('progress: ' + progressPercentage + '% ' + evt.config.data.file.name);
                });
            });
        };

    }]
);;angular.module('DojoIBL')

    .controller('ProfilesController', ["$scope", "$sce", "$stateParams", "$state", "Session", "AccountService", function ($scope, $sce, $stateParams, $state, Session, AccountService) {
        AccountService.myDetails().then(function(data){
            $scope.user = data;
        });
    }]
);;angular.module('DojoIBL')

    .controller('ActivityController', ["$scope", "$sce", "$stateParams", "Session", "ActivityService", "UserService", "AccountService", "Response", "ResponseService", "RunService", "ChannelService", "Upload", "config", "toaster", "GameService", function ($scope, $sce, $stateParams, Session, ActivityService, UserService, AccountService,
                                                Response, ResponseService, RunService, ChannelService, Upload, config, toaster, GameService) {

        ChannelService.register('org.celstec.arlearn2.beans.run.Response', function (data) {

            console.log(data);

            if($stateParams.activityId == data.generalItemId && $stateParams.runId == data.runId){
                ResponseService.refreshResponse(data, $stateParams.runId, $stateParams.activityId);
            }
            if($stateParams.runId == data.runId){
                //console.log(data.userEmail, AccountService.myDetailsCache().fullId);
                //
                //if(data.userEmail == AccountService.myDetailsCache().fullId){
                //    toaster.success({
                //        title: 'You added a response',
                //        body: 'You have contributed to an activity.'
                //    });
                //}else{

                    toaster.success({
                        title: UserService.getUser($stateParams.runId, data.userEmail).name+' added a response',
                        body: UserService.getUser($stateParams.runId, data.userEmail).name+' has contributed to an activity.'
                    });
                //}

            }
        });

        ChannelService.register('org.celstec.arlearn2.beans.notification.GeneralItemModification', function (notification) {
            ActivityService.refreshActivity(notification.itemId, notification.gameId).then(function (data) {
                $scope.activity = data;
            });
            toaster.success({
                title: 'Activity modified',
                body: 'The structure of the activity has been modified.'
            });
        });

        $scope.responses = {};

        $scope.loadMoreButton = false;

        function loadResponses() {
            ResponseService.resumeLoadingResponses($stateParams.runId, $stateParams.activityId).then(function (data) {
                if (data.error) {
                    $scope.showNoAccess = true;
                } else {
                    $scope.show = true;
                    if (data.resumptionToken) {
                        loadResponses();
                    }
                }
            });
        }

        loadResponses();

        $scope.responses = ResponseService.getResponsesByInquiryActivity($stateParams.runId, $stateParams.activityId);
        //console.log($scope.responses);
        //$scope.responses = ResponseService.getResponses($stateParams.runId, $stateParams.activityId);

        RunService.getRunById($stateParams.runId).then(function(data){

            ActivityService.getActivityById($stateParams.activityId, data.game.gameId).then(function (data) {
                $scope.activity = data;
            });

            GameService.getGameAssets(data.game.gameId).then(function(data){
                $scope.assets = data;
            });
        });

        AccountService.myDetails().then(function(data){
            $scope.myAccount = data;
        });

        $scope.sendComment = function(){
            if($scope.response != null && $scope.response.length > 0){

                if(angular.isUndefined($scope.response.id)){
                    ResponseService.removeCachedResponse($stateParams.activityId);
                }

                AccountService.myDetails().then(function(data){
                    ResponseService.newResponse({
                        "type": "org.celstec.arlearn2.beans.run.Response",
                        "runId": $stateParams.runId,
                        "deleted": false,
                        "generalItemId": $stateParams.activityId,
                        "userEmail": data.accountType+":"+data.localId,
                        "responseValue": $scope.response,
                        "parentId": 0,
                        "revoked": false,
                        "lastModificationDate": new Date().getTime()
                    }).then(function(data){
                        $scope.response = null;
                    });
                });
            }
        };

        $scope.sendChildComment = function(responseParent, responseChildren) {

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
                    $scope.responseChildren = null;
                });
            });
        };

        $scope.removeComment = function(data){

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

        $scope.trustSrc = function(src) {
            return $sce.trustAsResourceUrl(src);
        };

        ////////////////////////////
        // Response cache management
        ////////////////////////////

        if(ResponseService.getResponseInCached($stateParams.activityId)){
            $scope.response = ResponseService.getResponseInCached($stateParams.activityId);
        }

        $scope.saveResponse = function (){
            if($scope.response != null && $scope.response.length > 0){
                if(angular.isUndefined($scope.response.id)){
                    ResponseService.saveResponseInCache($scope.response, $stateParams.activityId);
                }else{
                    ResponseService.newResponse({
                        "type": "org.celstec.arlearn2.beans.run.Response",
                        "runId": $stateParams.runId,
                        "deleted": false,
                        "generalItemId": $stateParams.activityId,
                        "userEmail": data.accountType+":"+data.localId,
                        "responseValue": $scope.response,
                        "parentId": 0,
                        "revoked": false,
                        "lastModificationDate": new Date().getTime()
                    }).then(function(data){
                        $scope.response = null;
                    });
                }
            }
        };

        // upload on file select or drop
        $scope.upload = function (file) {
            $scope.progressPercentage = 0;
            if(file){

                file.name = (file.name).replace(/\s+/g, '_');

                ResponseService.uploadUrl($stateParams.runId, $scope.myAccount.accountType+":"+$scope.myAccount.localId, file.name.replace(/\s+/g, '_')).$promise.then(function(url){
                    console.log(url, url.uploadUrl);
                    Upload.rename(file, file.name.replace(/\s+/g, '_'));
                    Upload.upload({
                        url: url.uploadUrl,
                        data: {file: file, 'username': $scope.myAccount.accountType+":"+$scope.myAccount.localId}
                    }).then(function (resp) {

                        switch (true) {
                            case /video/.test(resp.config.data.file.type):
                                ResponseService.newResponse({
                                    "type": "org.celstec.arlearn2.beans.run.Response",
                                    "runId": $stateParams.runId,
                                    "deleted": false,
                                    "generalItemId": $stateParams.activityId,
                                    "userEmail": $scope.myAccount.accountType+":"+$scope.myAccount.localId,
                                    "responseValue": {
                                        "videoUrl": config.server +"/uploadService/"+$stateParams.runId+"/"+$scope.myAccount.accountType+":"+$scope.myAccount.localId+"/"+file.name.replace(/\s+/g, '_'),
                                        "fileName": file.name,
                                        "fileType": resp.config.data.file.type,
                                        "width": 3264,
                                        "height": 1840
                                    },
                                    "parentId": 0,
                                    "revoked": false,
                                    "lastModificationDate": new Date().getTime()
                                }).then(function(data){

                                });
                                break;
                            case /image/.test(resp.config.data.file.type):
                                ResponseService.newResponse({
                                    "type": "org.celstec.arlearn2.beans.run.Response",
                                    "runId": $stateParams.runId,
                                    "deleted": false,
                                    "generalItemId": $stateParams.activityId,
                                    "userEmail": $scope.myAccount.accountType+":"+$scope.myAccount.localId,
                                    "responseValue": {
                                        "imageUrl": config.server +"/uploadService/"+$stateParams.runId+"/"+$scope.myAccount.accountType+":"+$scope.myAccount.localId+"/"+file.name.replace(/\s+/g, '_'),
                                        "fileName": file.name,
                                        "fileType": resp.config.data.file.type,
                                        "width": 3264,
                                        "height": 1840
                                    },
                                    "parentId": 0,
                                    "revoked": false,
                                    "lastModificationDate": new Date().getTime()
                                }).then(function(data){

                                });
                                break;
                            case /pdf/.test(resp.config.data.file.type):
                                ResponseService.newResponse({
                                    "type": "org.celstec.arlearn2.beans.run.Response",
                                    "runId": $stateParams.runId,
                                    "deleted": false,
                                    "generalItemId": $stateParams.activityId,
                                    "userEmail": $scope.myAccount.accountType+":"+$scope.myAccount.localId,
                                    "responseValue": {
                                        "pdfUrl": config.server +"/uploadService/"+$stateParams.runId+"/"+$scope.myAccount.accountType+":"+$scope.myAccount.localId+"/"+file.name.replace(/\s+/g, '_'),
                                        "fileName": file.name,
                                        "fileType": resp.config.data.file.type,
                                        "width": 3264,
                                        "height": 1840
                                    },
                                    "parentId": 0,
                                    "revoked": false,
                                    "lastModificationDate": new Date().getTime()
                                }).then(function(data){

                                });
                                break;
                            case /audio/.test(resp.config.data.file.type):
                                ResponseService.newResponse({
                                    "type": "org.celstec.arlearn2.beans.run.Response",
                                    "runId": $stateParams.runId,
                                    "deleted": false,
                                    "generalItemId": $stateParams.activityId,
                                    "userEmail": $scope.myAccount.accountType+":"+$scope.myAccount.localId,
                                    "responseValue": {
                                        "audioUrl": config.server +"/uploadService/"+$stateParams.runId+"/"+$scope.myAccount.accountType+":"+$scope.myAccount.localId+"/"+file.name.replace(/\s+/g, '_'),
                                        "fileName": file.name,
                                        "fileType": resp.config.data.file.type,
                                        "width": 3264,
                                        "height": 1840
                                    },
                                    "parentId": 0,
                                    "revoked": false,
                                    "lastModificationDate": new Date().getTime()
                                }).then(function(data){

                                });
                                break;
                            case /wordprocessingml|msword/.test(resp.config.data.file.type):
                                console.log(resp.config.data.file.type);
                                ResponseService.newResponse({
                                    "type": "org.celstec.arlearn2.beans.run.Response",
                                    "runId": $stateParams.runId,
                                    "deleted": false,
                                    "generalItemId": $stateParams.activityId,
                                    "userEmail": $scope.myAccount.accountType+":"+$scope.myAccount.localId,
                                    "responseValue": {
                                        "documentUrl": config.server +"/uploadService/"+$stateParams.runId+"/"+$scope.myAccount.accountType+":"+$scope.myAccount.localId+"/"+file.name.replace(/\s+/g, '_'),
                                        "fileName": file.name,
                                        "fileType": resp.config.data.file.type,
                                        "width": 3264,
                                        "height": 1840
                                    },
                                    "parentId": 0,
                                    "revoked": false,
                                    "lastModificationDate": new Date().getTime()
                                }).then(function(data){

                                });
                                break;
                            case /vnd.ms-excel|spreadsheetml/.test(resp.config.data.file.type):
                                console.log(resp.config.data.file.type);
                                ResponseService.newResponse({
                                    "type": "org.celstec.arlearn2.beans.run.Response",
                                    "runId": $stateParams.runId,
                                    "deleted": false,
                                    "generalItemId": $stateParams.activityId,
                                    "userEmail": $scope.myAccount.accountType+":"+$scope.myAccount.localId,
                                    "responseValue": {
                                        "excelUrl": config.server +"/uploadService/"+$stateParams.runId+"/"+$scope.myAccount.accountType+":"+$scope.myAccount.localId+"/"+file.name.replace(/\s+/g, '_'),
                                        "fileName": file.name,
                                        "fileType": resp.config.data.file.type,
                                        "width": 3264,
                                        "height": 1840
                                    },
                                    "parentId": 0,
                                    "revoked": false,
                                    "lastModificationDate": new Date().getTime()
                                }).then(function(data){

                                });
                                break;
                            default:
                                ResponseService.newResponse({
                                    "type": "org.celstec.arlearn2.beans.run.Response",
                                    "runId": $stateParams.runId,
                                    "deleted": false,
                                    "generalItemId": $stateParams.activityId,
                                    "userEmail": $scope.myAccount.accountType+":"+$scope.myAccount.localId,
                                    "responseValue": file.name,
                                    "parentId": 0,
                                    "revoked": false,
                                    "lastModificationDate": new Date().getTime()
                                }).then(function(data){

                                });
                                break;

                        }

                        console.log(resp)
                        console.log('Success ' + resp.config.data.file.name + ' uploaded by: ' + resp.config.data.username);

                    }, function (resp) {
                        console.log(resp)
                        console.log('Error ' + resp.config.data.file.name + ' from: ' + resp.config.data.username);
                        console.log('Error status: ' + resp.status);
                    }, function (evt) {
                        var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                        $scope.progressPercentage = progressPercentage;
                        console.log('progress: ' + progressPercentage + '% ' + evt.config.data.file.name);
                    });
                });

            }
        };

        $scope.getUser = function (response){
            return UserService.getUserFromCache(response.userEmail.split(':')[1]).name;
        };
        $scope.getAvatar = function (response){
            return UserService.getUserFromCache(response.userEmail.split(':')[1]).picture;
        };

        $scope.getRoleColor = function(roles){
            if(!angular.isUndefined(roles)) {
                try{
                    if (!angular.isUndefined(angular.fromJson(roles[0]))) {
                        if (!angular.isObject(roles[0])) {
                            return {
                                "color": angular.fromJson(roles[0]).color
                            };
                        }
                    }
                }catch(e){
                    return {
                        "color": "#f8ac59"
                    };
                }
            }
            return {
                "color": "#f8ac59"
            };
        };

        $scope.trustSrc = function(src) {
            return $sce.trustAsResourceUrl(src);
        };

    }]
);;angular.module('DojoIBL')

    .controller('PhaseController', ["$scope", "$sce", "$location", "$stateParams", "$state", "toaster", "Session", "ActivityStatusService", "RunService", "ChannelService", function ($scope, $sce, $location, $stateParams, $state, toaster, Session, ActivityStatusService, RunService, ChannelService) {
        $scope.runId = $stateParams.runId;

        ChannelService.register('org.celstec.arlearn2.beans.run.GeneralItemsStatus', function (notification) {
            //
            //console.log(notification)
            //
            ActivityStatusService.refreshActivityStatus(notification.generalItemId, $scope.gameId, $stateParams.runId, $stateParams.phase);
            toaster.success({
                title: 'Activity modified',
                body: 'The structure of the activity has been modified.'
            });
        });

        $scope.phase = "holaaaaaaa";


        RunService.getRunById($stateParams.runId).then(function(data){
            $scope.phase = data.game.phases[$stateParams.phase];
            $scope.phase.num = $stateParams.phase;
            $scope.gameId = data.game.gameId;
            ActivityStatusService.getActivitiesServerStatus(data.game.gameId, $stateParams.runId, $stateParams.phase);
        });

        $scope.activities = ActivityStatusService.getActivitiesStatus();


        $scope.sortableOptions = {
            connectWith: ".connectList",
            scroll: false,
            receive: function(event, ui) {
                var item = ui.item.scope().activity;
                var group = event.target;
                ActivityStatusService.changeActivityStatus($stateParams.runId, item.id,group.id, $stateParams.phase).then(function (data) {
                    switch(data.status){
                        case 0:
                            toaster.success({
                                title: 'Moved to ToDo list',
                                body: 'The activity has been successfully moved to the ToDo list.'
                            });
                            break;
                        case 1:
                            toaster.success({
                                title: 'Moved to In Progress list',
                                body: 'The activity has been successfully moved to the In Progress list.'
                            });
                            break;
                        case 2:
                            toaster.success({
                                title: 'Moved to Completed list',
                                body: 'The activity has been successfully moved to the Completed list.'
                            });
                            break;
                    }

                });
            },
            'ui-floating': 'auto',
            'start': function (event, ui) {
                if($scope.sortableFirst){
                    $scope.wscrolltop = $(window).scrollTop();
                }
                $scope.sortableFirst = true;
            },
            'sort': function (event, ui) {
                ui.helper.css({'top': ui.position.top + $scope.wscrolltop + 'px'});
            }
        };

        $scope.goToActivity = function(runId, section, id) {
            $location.path('inquiry/'+runId+'/phase/'+section+'/activity/'+id);
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


        function isEmpty(obj) {
            for(var prop in obj) {
                if(obj.hasOwnProperty(prop))
                    return false;
            }
            return true;
        }
    }]
);;angular.module('DojoIBL')

    .controller('FooterController', ["$scope", "Session", function ($scope, Session) {
        $scope.test = "foo";

        $scope.name = "your name";

        if (Session.getAccessToken() ) {
            //Account.accountDetails().$promise.then(
            //    function(data){
            //        $scope.pictureUrl = data.picture;
            //        $scope.name = data.name;
            //    }
            //);
        }
    }]
);;angular.module('DojoIBL')

    .controller('HeaderController', ["$scope", "Session", "$translate", "$state", "$location", "config", function ($scope, Session, $translate,$state, $location, config) {
        $scope.test = "foo";

        $scope.name = "your name";

        $scope.flags = [{
            icon: 'src/assets/img/i18n/Spain.png',
            name: 'Spanish',
            id: 'es'
        }, {
            icon: 'src/assets/img/i18n/United-States.png',
            name: 'English',
            id: 'en'
        }, {
            icon: 'src/assets/img/i18n/Netherlands.png',
            name: 'Dutch',
            id: 'nl'
        }];

        if (Session.getAccessToken()) {
            //Account.accountDetails().$promise.then(
            //    function(data){
            //        $scope.pictureUrl = data.picture;
            //        $scope.name = data.name;
            //    }
            //);
        }

        $scope.setLanguage = function (lang) {
            $translate.use(lang);
        };

        $scope.signout = function() {
            Session.reset();
            document.cookie =  'arlearn.AccessToken=; expires=Thu, 01 Jan 1970 00:00:01 GMT;path=/';
            window.location.href=config.server+'/index.html';
        }
    }]
);;angular.module('DojoIBL')

    .controller('SidebarController', ["$scope", "$rootScope", "$modal", "Session", "$state", "$stateParams", "GameService", "RunService", "Account", "AccountService", function ($scope, $rootScope, $modal, Session, $state, $stateParams, GameService, RunService, Account, AccountService) {

        $scope.$on("$stateChangeSuccess", function updatePage() {
            $scope.phaseNumber = $state.params.phase;
            $scope.statePhase = $state.current.name;
        });

        $scope.$on('inquiry-run', function(event, args) {
            RunService.getRunById(args.runId).then(function(data){
                $scope.run = data;
                AccountService.myDetails();
            });
        });

        if ($stateParams.runId) {

            RunService.getRunById($stateParams.runId).then(function(data){
                $scope.run = data;
                AccountService.myDetails();
            });

        }else if($stateParams.gameId){
            GameService.getGameById($stateParams.gameId).then(function(data){
                if (data.error) {
                    $scope.showNoAccess = true;
                } else {
                    $scope.show = true;
                }

                $scope.game = data;
            });
        }
        else{
            AccountService.myDetails();
        }

        $scope.myAccount = AccountService.myDetailsCache();

        $scope.createNewInquiry = function () {

            var modalInstance = $modal.open({
                templateUrl: '/src/components/common/new.inquiry.modal.html',
                controller: 'NewInqCtrl'
            });

            //modalInstance.result.then(function (result){
            //    angular.extend($scope.usersRun[$scope.runVar], result);
            //});
        };
    }])
        .controller('NewInqCtrl', ["$scope", "$modalInstance", "GameService", "ActivityService", function ($scope, $modalInstance, GameService, ActivityService) {

            $scope.ok = function () {
                $scope.game.deleted = false;
                $scope.game.revoked = false;

                var __phases = ["Problem statement", "Plan the method", "Collect the data", "Analyse the data", "Interpret the findings", "Communicate the results"];

                if($scope.game.template){
                    $scope.game.phases = [];
                    angular.forEach(__phases, function(phase){
                        $scope.game.phases.push({
                            title: phase,
                            type: "org.celstec.arlearn2.beans.game.Phase"
                        });
                    });
                }

                GameService.newGame($scope.game).then(function(data){
                    $scope.game = data;
                    $scope.game.config.roles = [];
                    //$scope.phases = $scope.game.phases;
                    //
                    //var __activity = [{
                    //    "type": "org.celstec.arlearn2.beans.generalItem.AudioObject",
                    //    "gameId": $scope.game.gameId,
                    //    "deleted": false,
                    //    "sortKey": 1,
                    //    "scope": "user",
                    //    "name": "Research question",
                    //    "description": "",
                    //    "autoLaunch": false,
                    //    "section": "0",
                    //    "roles": ["null"],
                    //    "fileReferences": [],
                    //    "richText": "<p><span style=\"color: rgb(51, 51, 51);\">Individually you need to bring 3 relevant questions about your topic. One of them could be more generic and the other 2 can be sub-questions of the other one.<\/span><br\/><\/p><div class=\"alert alert-info ng-scope\" style=\"color: rgb(49, 112, 143);background-color: rgb(217, 237, 247);\"><strong>Goal:<\/strong><span class=\"Apple-converted-space\">Â This is an individual work. Everyone should bring his or her own research questions and later decide which ones are the most interesting ones in order to use them in the following steps of the inquiry.<\/span><\/div><div class=\"alert alert-warning ng-scope\" style=\"color: rgb(138, 109, 59);background-color: rgb(252, 248, 227);\"><strong>Tip:<\/strong><span class=\"Apple-converted-space\">Â Be aware of that the questions we are looking should be good enough. You don't want weak research questions. You need to find you <b>essential question<\/b>. Follow the link to know more about this:Â <a href=\"http:\/\/www.ascd.org\/publications\/books\/109004\/chapters\/What-Makes-a-Question-Essential%A2.aspx\" target=\"\">http:\/\/www.ascd.org\/publications\/books\/109004\/chapters\/What-Makes-a-Question-Essential%A2.aspx<\/a><\/span><\/div><div class=\"alert alert-warning ng-scope\" style=\"color: rgb(138, 109, 59);background-color: rgb(252, 248, 227);\">You do not understand what a <b>research questions <\/b>is? Ask google, <b>it is very important for you to know this.<\/b><\/div>",
                    //    "audioFeed": "example link"
                    //}];
                    //
                    //ActivityService.newActivity(__activity[0]);
                });
                $modalInstance.close();
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };

        }])
;;angular.module('DojoIBL')

    .controller('ToolbarController', ["$scope", "$rootScope", "$state", "$location", "$stateParams", "RunService", "ActivityService", "Session", "Game", "GameService", "AccountService", "config", "ChannelService", "UserService", function ($scope, $rootScope, $state, $location, $stateParams,RunService, ActivityService, Session, Game, GameService, AccountService, config, ChannelService, UserService) {
        $scope.test = "foo";

        $scope.name = "your name";
        $scope.inquiryCode;

        $scope.leftDisabled = false;

        $scope.state = $state;

        if ($stateParams.runId) {
            $scope.$on('inquiry-run', function(event, args) {
                RunService.getRunById(args.runId).then(function(run) {
                    $scope.run = run;
                });
            });

            RunService.getRunById($stateParams.runId).then(function(run){
                $scope.run = run;

                Game.access({ }).$promise.then(function (data) {

                    angular.forEach(data.gamesAccess, function (gameAccess) {

                        if(run.gameId == gameAccess.gameId){
                            GameService.getGameById(gameAccess.gameId).then(function (aux) {

                                var data_extended = angular.extend({}, aux, gameAccess);

                                $scope.game = data_extended;

                                //console.log(data_extended);

                                if ($stateParams.phase) {
                                    $scope.namePhase = aux.phases[$stateParams.phase];
                                }
                            });
                        }
                    });
                });

                RunService.getParticipateRunsForGame(run.gameId).then(function(data){
                    var aux = []
                    $scope.itemArray = aux;
                    angular.forEach(data, function(_b){
                        aux.push(_b)
                    });
                    $scope.selected = { value: $scope.itemArray[0] };
                });

                $scope.selectChange = function(run) {

                    $rootScope.$broadcast('inquiry-run', { 'runId':run.runId });

                    switch($state.current.name){
                        case "inquiry.home":
                            $state.go('inquiry.home', { 'runId':run.runId });
                            break;
                        case "inquiry.timeline":
                            $state.go('inquiry.timeline', { 'runId':run.runId });
                            break;
                        case "inquiry.phase":
                            $state.go('inquiry.phase', { 'runId':run.runId, phase: $state.params.phase });
                            break;
                        case "inquiry.activity":
                            $state.go('inquiry.activity', { 'runId':run.runId, phase: $state.params.phase, activityId: $state.params.activityId  });
                            break;
                    }
                };

                if ($stateParams.activityId) {

                    ActivityService.getActivityById($stateParams.activityId, run.gameId).then(function (data) {
                        $scope.activity = data;
                    });
                }

                $scope.goLeft = function(){
                    switch($state.current.name){
                        case "inquiry.home":

                            break;
                        case "inquiry.phase":

                            if($state.params.phase > 0){
                                var prevPhase = $state.params.phase - 1;
                                $state.go('inquiry.phase', { 'runId':run.runId, phase: prevPhase });
                            }
                            break;
                        case "inquiry.activity":
                            var prevActivity = ActivityService.getPrevActivity($stateParams.activityId, run.gameId, $state.params.phase);
                            if(!angular.isUndefined(prevActivity)){
                                $state.go('inquiry.activity', { 'runId':run.runId, phase: $state.params.phase, activityId: prevActivity.id  });
                            }
                            break;
                    }
                };

                $scope.goUp = function(){
                    switch($state.current.name){
                        case "inquiry.home":
                            $state.go('home', { });
                            break;
                        case "inquiry.phase":
                            $state.go('inquiry.home', { 'runId':run.runId });
                            break;
                        case "inquiry.activity":
                            $state.go('inquiry.phase', { 'runId':run.runId, phase: $state.params.phase });
                            break;
                    }
                };

                $scope.goRight = function(){
                    switch($state.current.name){
                        case "inquiry.home":

                            break;
                        case "inquiry.phase":
                            if($state.params.phase < ActivityService.getLengthPhase($stateParams.activityId, run.gameId) - 1){
                                var prevPhase = parseInt($state.params.phase) + 1;
                                $state.go('inquiry.phase', { 'runId':run.runId, phase: prevPhase });
                            }
                            break;
                        case "inquiry.activity":
                            var nextActivity = ActivityService.getNextActivity($stateParams.activityId, run.gameId, $state.params.phase);
                            if(!angular.isUndefined(nextActivity)){
                                $state.go('inquiry.activity', { 'runId':run.runId, phase: $state.params.phase, activityId: nextActivity.id  });
                            }
                            break;
                    }
                };



            });


        }

        $scope.findAndJoin = function(){

            RunService.getRunByCode($scope.inquiryCode).then(function(run){

                AccountService.myDetails().then(function(user){

                    //////
                    // AccessRight explanation
                    // 1: Editor
                    // 2: User

                    console.log(user);

                    GameService.giveAccess(run.game.gameId, user.accountType+":"+user.localId,2);

                    RunService.giveAccess(run.runId, user.accountType+":"+user.localId,2);

                    RunService.addUserToRun({
                        runId: run.runId,
                        email: user.accountType+":"+user.localId,
                        accountType: user.accountType,
                        localId: user.localId,
                        gameId: run.game.gameId });

                    window.location.href=config.server+'/main.html#/inquiry/'+run.runId;
                });
            });

            $scope.inquiryCode = null;
        };

        ChannelService.register('org.celstec.arlearn2.beans.run.User', function (notification) {
            console.info("[Notification][User]", notification);
            UserService.getUserByAccount(notification.runId, notification.accountType+":"+notification.localId);
        });
    }]
);;angular.module('DojoIBL')

    .controller('InquiryEditGameController', ["$scope", "$sce", "$stateParams", "$state", "$modal", "Session", "RunService", "ActivityService", "AccountService", "ChannelService", "GameService", "UserService", "toaster", "$interval", "uiCalendarConfig", "$anchorScroll", "$location", function ($scope, $sce, $stateParams, $state, $modal, Session, RunService, ActivityService,
                                                       AccountService, ChannelService, GameService, UserService, toaster, $interval,
                                                       uiCalendarConfig, $anchorScroll, $location) {



        ChannelService.register('org.celstec.arlearn2.beans.game.Game', function (notification) {
            GameService.refreshGame(notification.gameId).then(function (data) {
                $scope.game = data;
                $scope.phases = data.phases;
            });
            toaster.success({
                title: 'Inquiry template modified',
                body: 'The inquiry "'+$scope.game.title+'" has been modified.'
            });
        });

        ChannelService.register('org.celstec.arlearn2.beans.notification.GeneralItemModification', function (notification) {
            ActivityService.refreshActivity(notification.itemId, notification.gameId);
            toaster.success({
                title: 'Activity modified',
                body: 'The structure of the activity has been modified.'
            });
        });

        // Managing different tabs activities
        $scope.lists = [];
        $scope.selection = [];

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

            if(!$scope.game.config.roles2)
                $scope.game.config.roles2 = [];
            else{
                //$scope.game.config.roles = new JSONObject(data.config.roles);
            }

            $scope.phases = $scope.game.phases;

            if (data.lat) {
                $scope.coords.latitude = data.lat;
                $scope.coords.longitude = data.lng;
                $scope.map.center.latitude = data.lat;
                $scope.map.center.longitude = data.lng;
                $scope.showMap = true;
            }

            // Original list of activities
            $scope.list_original = [
                //{'name': 'Google Resources', 'type': 'org.celstec.arlearn2.beans.generalItem.AudioObject', 'icon': 'fa-file-text'},
                {'name': 'Add text activity', 'type': 'org.celstec.arlearn2.beans.generalItem.NarratorItem', 'icon': 'fa-file-text'},
                {'name': 'Add external resource', 'type': 'org.celstec.arlearn2.beans.generalItem.VideoObject', 'icon': 'fa-external-link'},
                //{'name': 'Concept map', 'type': 'org.celstec.arlearn2.beans.generalItem.SingleChoiceImageTest', 'icon': 'fa-sitemap'},
                //{'name': 'External widget', 'type': 'org.celstec.arlearn2.beans.generalItem.OpenBadge', 'icon': 'fa-link'},
                //{'name': 'Research question', 'type': 'org.celstec.arlearn2.beans.generalItem.ResearchQuestion', 'icon': 'fa-question'}
                {'name': 'Add list activity', 'type': 'org.celstec.arlearn2.beans.generalItem.AudioObject', 'icon': 'fa-tasks'},
                {'name': 'Add data collection', 'type': 'org.celstec.arlearn2.beans.generalItem.ScanTag', 'icon': 'fa-picture-o'}
            ];

            ActivityService.getActivitiesServer($stateParams.gameId);
        });

        $scope.activities = ActivityService.getActivities();

        /********
         Calendar
         *******/
        $scope.events = [];

        $scope.events = ActivityService.getCalendarActivities()[$stateParams.gameId];

        if(angular.isUndefined($scope.events)){
            $scope.events = ActivityService.getCalendarActivities();
        }

        $scope.updateCalendar = function(){
        };

        $scope.eventSources = [$scope.events];

        $scope.alertOnEventClick = function( event, allDay, jsEvent, view ){
            $scope.editActivity(event.activity, event.activity.gameId)
        };

        $scope.alertOnDrop = function(event, dayDelta, minuteDelta, allDay, revertFunc, jsEvent, ui, view){
            event.activity.timestamp += (60*60*24*dayDelta*1000);
            event.activity.timestamp = new Date(event.activity.timestamp)
            ActivityService.newActivity(event.activity).then(function(data){
                ActivityService.refreshActivity(data.id, data.gameId);
            });
        };

        $scope.alertOnResize = function(event, dayDelta, minuteDelta, revertFunc, jsEvent, ui, view ){
            $scope.alertMessage = (event.title +': Resized to make dayDelta ' + minuteDelta);
        };

        $scope.uiConfig = {
            calendar:{
                firstDay:1,
                height: 450,
                editable: true,
                header: {
                    left: 'prev,next,today',
                    center: 'title',
                    right: 'month,agendaWeek,agendaDay'
                },
                eventClick: $scope.alertOnEventClick,
                eventDrop: $scope.alertOnDrop,
                eventResize: $scope.alertOnResize
            }
        };

        $scope.ok = function(){
            GameService.newGame($scope.game);

            toaster.success({
                title: 'Inquiry template modified',
                body: 'The inquiry "'+$scope.game.title+'" has been modified.'
            });
        };

        //////////////////////
        // Scrolling functions
        //////////////////////
        $scope.gotoAnchor = function(x) {
            var newHash = 'anchor' + x;
            if ($location.hash() !== newHash) {
                // set the $location.hash to `newHash` and
                // $anchorScroll will automatically scroll to it
                $location.hash(x);
            } else {
                // call $anchorScroll() explicitly,
                // since $location.hash hasn't changed
                $anchorScroll();
            }
        };

        ///////////////
        // Manage roles
        ///////////////
        $scope.addRole = function () {
            //$scope.game.config.roles.push($scope.role);
            GameService.addRole($scope.game.gameId, $scope.role).then(function(data){
                $scope.game = data;
            });

            toaster.success({
                title: 'Role added to inquiry',
                body: 'The role "'+$scope.role.name+'" has been added.'
            });

            $scope.role = null;
        };

        $scope.editRole = function (index) {

            GameService.editRole($scope.game.gameId, $scope.role, $scope.index).then(function(data){
                $scope.game = data;
            });

            toaster.success({
                title: 'Role modified',
                body: 'The role "'+$scope.role.name+'" has been edited.'
            });

            $scope.role = null;
        };

        $scope.clearRole = function (index) {
            $scope.role = null;
        };

        $scope.selectRole = function(index){
            $scope.index = index;
            $scope.role = $scope.game.config.roles2[index];

            $scope.removeRole = function(){
                GameService.removeRole($scope.game.gameId, $scope.role).then(function(data){
                    $scope.game = data;
                });

                toaster.warning({
                    title: 'Role deleted',
                    body: 'The role "'+$scope.role.name+'" has been removed.'
                });

                $scope.role = null;
            };
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

        ////////////////
        // Manage phases
        ////////////////
        $scope.currentPhase = 0;

        $scope.addPhase = function(){
            swal({
                title: "New phase",
                text: "Are you sure you want to add a phase?",
                type: "input",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "Yes, create it!",
                inputPlaceholder: "New phase name",
                closeOnConfirm: false
            }, function (inputValue) {

                if (inputValue === false)
                    return false;
                if (inputValue === "") {
                    swal.showInputError("You need to write something!");
                    return false
                }

                swal("Well done!", "New phase created", "success");


                $scope.phases.push({
                    //phaseId: $scope.phases.length,
                    title: inputValue,
                    type: "org.celstec.arlearn2.beans.game.Phase"
                });

                //$scope.lists.push($scope.phases.length);
                //$scope.lists = [];

                toaster.success({
                    title: 'Phase added',
                    body: 'The phase "' + $scope.phaseName + '" has been added to the inquiry template.'
                });

                $scope.phaseName = "";

                $scope.game.phases = $scope.phases;

                GameService.newGame($scope.game).then(function (updatedGame) {

                    angular.forEach($scope.gameRuns, function (value, key) {

                        value.game = updatedGame;

                        RunService.storeInCache(value);
                    });
                });
            });
        };

        $scope.removePhase = function(index){

            swal({
                title: "Are you sure you want to delete the phase?",
                text: "You will not be able to recover this phase!",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "Yes, delete the phase!",
                closeOnConfirm: false
            }, function () {
                swal("Phase deleted!", "The phase has been removed from the inquiry structure.", "success");

                toaster.warning({
                    title: 'Phase removed',
                    body: 'The phase "'+$scope.phases[index].title+'" has been removed from the inquiry template.'
                });

                $scope.phases.splice(index, 1);
                $scope.game.phases = $scope.phases;
                GameService.newGame($scope.game).then(function(updatedGame){
                    angular.forEach($scope.gameRuns, function(value, key) {
                        value.game = updatedGame;
                        RunService.storeInCache(value);
                    });
                });

                ActivityService.getActivitiesForPhase($scope.game.gameId, index).then(function(data){
                    angular.forEach(data, function(i, a){
                        ActivityService.deleteActivity(i.gameId, i.id);
                    });
                });
            });

        };

        $scope.movePhase = function(x, y){

            swal({
                title: "Moving phase "+x+" to phase "+y,
                text: "You will switch phase positions. Current phase "+x+" will be "+y+" and phase "+y+" will be phase "+x,
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "Yes, move them!",
                closeOnConfirm: false
            }, function () {
                swal("Phase moved!", "Phases have been reordered", "success");

                var b = $scope.phases[y];
                var acts = $scope.lists[y];

                $scope.phases[y] = $scope.phases[x];
                $scope.lists[y] = $scope.lists[x];

                $scope.phases[x] = b;
                $scope.lists[x] = acts;

                angular.forEach($scope.lists[x], function(i, a){
                    i.section = x;
                    ActivityService.newActivity(i).then(function(data){
                    });
                });

                angular.forEach($scope.lists[y], function(i,a){
                    i.section = y;
                    ActivityService.newActivity(i).then(function(data){
                    });
                });

                $scope.game.phases = $scope.phases;

                GameService.newGame($scope.game).then(function(updatedGame){

                    angular.forEach($scope.gameRuns, function(value, key) {

                        value.game = updatedGame;

                        RunService.storeInCache(value);
                    });
                });

                toaster.success({
                    title: 'Phase moved',
                    body: 'The phase has been moved successfully.'
                });
            });
        };

        $scope.renamePhase = function(index){
            swal({
                title: "Rename phase "+index+" '"+$scope.phases[index].title+"'?",
                text: "Are you sure you want to rename the phase?",
                type: "input",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "Yes, rename it!",
                inputPlaceholder: "New phase name",
                closeOnConfirm: false
            }, function (inputValue) {

                if (inputValue === false)
                    return false;
                if (inputValue === "") {
                    swal.showInputError("You need to write something!");
                    return false
                }

                swal("Well done!", "You renamed the phase, now it's called: " + inputValue, "success");

                $scope.phases[index].title = inputValue;

                $scope.game.phases = $scope.phases;

                GameService.newGame($scope.game).then(function(updatedGame){

                    angular.forEach($scope.gameRuns, function(value, key) {
                        value.game = updatedGame;

                        RunService.storeInCache(value);
                    });
                });

                toaster.success({
                    title: 'Phase renamed',
                    body: 'The phase has been renamed successfully.'
                });

            });
        };

        //////////////////////
        // Manage inquiry runs
        //////////////////////
        RunService.getParticipateRunsForGame($stateParams.gameId).then(function(data){
            if (data.error) {
                $scope.showNoAccess = true;
            } else {
                $scope.show = true;
            }

            $scope.gameRuns = data;


            $scope.usersRun = [];

            angular.forEach($scope.gameRuns, function(value, key) {
                $scope.usersRun[value.runId] = [];
                UserService.getUsersForRun(value.runId).then(function(data){

                    $scope.usersRun[value.runId] = data;

                });
            });
        });

        $scope.removeRun = function(run){

            swal({
                title: "Are you sure you want to delete the inquiry run?",
                text: "You will not be able to recover this inquiry run!",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "Yes, delete the run!",
                closeOnConfirm: false
            }, function () {
                swal("Inquiry run deleted!", "The Inquiry Run has been removed from the list of inquiry runs.", "success");

                var idx = arrayObjectIndexOf($scope.gameRuns, run.runId, "runId");

                // is currently selected
                if (idx > -1) {
                    $scope.gameRuns.splice(idx, 1);
                }

                var updatedRun = RunService.deleteRun(run.runId);
            });
        };

        $scope.createInquiryRun = function(){
            // Add the link between run and game
            $scope.run.gameId = $stateParams.gameId;
            $scope.run.game = $scope.game;

            RunService.newRun($scope.run).then(function(run){
                // Update run with data from the server
                $scope.run = run;

                if(angular.isUndefined($scope.gameRuns)){
                    $scope.gameRuns = []
                }
                if(angular.isUndefined($scope.usersRun)){
                    $scope.usersRun = []
                }


                angular.forEach($scope.accountsAccesGame, function (gameAccess) {
                    if(gameAccess.accessRights == 1){
                        RunService.giveAccess(run.runId, gameAccess.account, 2);

                        RunService.addUserToRun({
                            runId: run.runId,
                            email: gameAccess.account,
                            accountType: gameAccess.accountType,
                            localId: gameAccess.localId,
                            gameId: run.game.gameId });
                    }
                });

                //$scope.gameRuns.push(run);
                AccountService.myDetails().then(function(data){

                    $scope.me = data;

                    // Grant me access to the run
                    RunService.giveAccess($scope.run.runId, data.accountType+":"+data.localId,1);

                    if(angular.isUndefined($scope.usersRun[$scope.run.runId])){
                        $scope.usersRun[$scope.run.runId] = []
                    }

                    var newUsers = [];

                    // Add me as a user to the run
                    var user = RunService.addUserToRun({
                        runId: $scope.run.runId,
                        email: data.accountType+":"+data.localId,
                        accountType: data.accountType,
                        localId: data.localId,
                        gameId: $stateParams.gameId
                    });

                    newUsers.push(user);

                    $scope.gameRuns[$scope.run.runId] = $scope.run;
                    angular.extend($scope.usersRun[$scope.run.runId], newUsers);

                    // Reset the run variable to create new ones
                    $scope.run = null;
                });
            });
        };

        $scope.addUsersToRun = function (run, game) {

            $scope.runVar = run;
            $scope.gameVar = game;

            var modalInstance = $modal.open({
                templateUrl: '/src/components/home/add.user.modal.html',
                controller: 'AddUserCtrl',
                resolve: {
                    run: function () {
                        return $scope.runVar;
                    },
                    game: function () {
                        return $scope.gameVar;
                    }
                }
            });

            modalInstance.result.then(function (result){
                angular.extend($scope.usersRun[$scope.runVar], result);
            });

        };

        ///////////////////////
        // Manage access rights
        ///////////////////////
        AccountService.myDetails().then(function(data){
           $scope.me = data;
        });

        GameService.getGameAccesses($stateParams.gameId).then(function(data){
            $scope.accountsAccesGame = {};
            angular.forEach(data.gamesAccess, function (gameAccess) {
                AccountService.accountDetailsById(gameAccess.account).then(function(data){
                    var data_extended = angular.extend({}, data, gameAccess);
                    $scope.accountsAccesGame[gameAccess.account] = data_extended;
                });
            });
        });

        $scope.grantEditorAccess = function (game, user){
            GameService.giveAccess(game.gameId, user.accountType+":"+user.localId,1);
            $scope.accountsAccesGame[user.account].accessRights = 1;

            angular.forEach($scope.gameRuns, function(run){
                RunService.giveAccess(run.runId, user.account, 2);

                RunService.addUserToRun({
                    runId: run.runId,
                    email: user.account,
                    accountType: user.accountType,
                    localId: user.localId,
                    gameId: run.game.gameId });
            });
        };

        $scope.revokeEditorAccess = function (game, user){
            GameService.giveAccess(game.gameId, user.accountType+":"+user.localId,2);
            $scope.accountsAccesGame[user.account].accessRights = 2;
        };

        //////////////////
        // Extra functions
        //////////////////
        function arrayObjectIndexOf(myArray, searchTerm, property) {
            for(var i = 0, len = myArray.length; i < len; i++) {
                if (myArray[i][property] === searchTerm) return i;
            }
            return -1;
        }


        ////////////////////
        // Manage activities
        ////////////////////
        $scope.addNewActivity = function (phase, game) {
            $scope.activity = {};

            if(ActivityService.getActivityInCached()){
                $scope.activity = ActivityService.getActivityInCached();
            }

            $scope.activity.section = phase;

            var modalInstance = $modal.open({
                templateUrl: '/src/components/home/new.activity.modal.html',
                controller: 'NewActivityController',
                resolve: {
                    activity: function () { return $scope.activity; },
                    game: function () { return game; }
                }
            });

            modalInstance.result.then(function (result){

                if(angular.isUndefined($scope.lists[result.section])){
                    $scope.lists[result.section] = []
                }

                ActivityService.newActivity(result).then(function(data){
                     ActivityService.getActivityById(data.id, $stateParams.gameId).then(function(data){
                        if(!angular.isUndefined(result.roles2)){
                        }else{
                            $scope.lists[result.section].push(data);
                        }

                    });
                });
                console.log("Modal Accepted!!!");
                //if (angular.isDefined(stop)) {
                //    $interval.cancel(stop);
                //    stop = undefined;
                //}
            }, function(){
                console.log("Modal Dismissed!!!");
                //if (angular.isDefined(stop)) {
                //    $interval.cancel(stop);
                //    stop = undefined;
                //}
                ActivityService.saveActivityInCache($scope.activity);
            });
        };

        $scope.editActivity = function (activity, game) {
            var modalInstance = $modal.open({
                templateUrl: '/src/components/home/new.activity.modal.html',
                controller: 'NewActivityController',
                resolve: {
                    activity: function () { return activity; },
                    game: function () { return game; }
                }
            });

            modalInstance.result.then(function (result){

                console.log(result);

                ActivityService.newActivity(result).then(function(data){
                    ActivityService.refreshActivity(data.id, data.gameId);
                });
            });
        };

        $scope.wscrolltop = '';
        $scope.sortableFirst = false;

        $scope.sortableOptions = {
            //connectWith: ".connectList",
            'scroll': false,
            'ui-floating': 'auto',
            'start': function (event, ui) {
                if($scope.sortableFirst){
                    $scope.wscrolltop = $(window).scrollTop();
                }
                $scope.sortableFirst = true;
            },
            'sort': function (event, ui) {
                ui.helper.css({'top': ui.position.top + $scope.wscrolltop + 'px'});
            },
            stop: function(e, ui) {
                var item = ui.item.scope().activity;
                var group = event.target;
                //console.log(e.target.id);
                $.map($(this).find('li'), function(el) {
                    var sortKey = $(el).index();
                    el = angular.fromJson(el.id);

                    //console.log(e.target.id, el.section);

                    el.sortKey = sortKey;
                    ActivityService.newActivity(el).then(function(data){
                        //console.log(data);
                    });
                });
            }
        };
    }])
    .controller('NewActivityController', ["$scope", "$stateParams", "$modalInstance", "GameService", "ActivityService", "Upload", "activity", "game", "config", "$interval", function ($scope, $stateParams, $modalInstance,
                                                   GameService, ActivityService, Upload,
                                                   activity, game, config, $interval) {

        $scope.saveActivity = function (){
            if(angular.isUndefined($scope.activity.id)){
                ActivityService.saveActivityInCache($scope.activity);
            }else{
                ActivityService.newActivity($scope.activity).then(function(data){
                    ActivityService.refreshActivity(data.id, data.gameId);
                });
            }
        };

        $scope.list_original = [
            //{'name': 'Google Resources', 'type': 'org.celstec.arlearn2.beans.generalItem.AudioObject', 'icon': 'fa-file-text'},
            {'name': 'Discussion activity', 'type': 'org.celstec.arlearn2.beans.generalItem.NarratorItem', 'icon': 'fa-file-text'},
            {'name': 'External resource', 'type': 'org.celstec.arlearn2.beans.generalItem.VideoObject', 'icon': 'fa-external-link'},
            //{'name': 'Concept map', 'type': 'org.celstec.arlearn2.beans.generalItem.SingleChoiceImageTest', 'icon': 'fa-sitemap'},
            //{'name': 'External widget', 'type': 'org.celstec.arlearn2.beans.generalItem.OpenBadge', 'icon': 'fa-link'},
            //{'name': 'Research question', 'type': 'org.celstec.arlearn2.beans.generalItem.ResearchQuestion', 'icon': 'fa-question'}
            {'name': 'List activity', 'type': 'org.celstec.arlearn2.beans.generalItem.AudioObject', 'icon': 'fa-tasks'},
            {'name': 'Data collection', 'type': 'org.celstec.arlearn2.beans.generalItem.ScanTag', 'icon': 'fa-picture-o'}
        ];

        $scope.game = game;
        $scope.activity = activity;

        $scope.activity.fileReferences = [];
        $scope.activity.gameId = $stateParams.gameId;

        $scope.dateOptions = {format: 'dd/mm/yyyy'}

        if(activity.type == null){
            activity.type = $scope.list_original[0].type;
        }

        GameService.getGameAssets($stateParams.gameId).then(function(data){
            $scope.assets = data;
        });

        // upload on file select or drop
        $scope.upload = function (file) {
            $scope.progressPercentage = 0;
            if(file){

                file.name = (file.name).replace(/\s+/g, '_');

                console.log(activity, file);

                ActivityService.uploadUrl($stateParams.gameId, activity.id, file.name.replace(/\s+/g, '_')).$promise
                    .then(function(url){

                        Upload.rename(file, file.name.replace(/\s+/g, '_'));
                        Upload.upload({
                            url: url.uploadUrl,
                            data: {file: file}
                        }).then(function (resp) {

                            switch (true) {
                                case /video/.test(resp.config.data.file.type):
                                    //ResponseService.newResponse({
                                    //    "type": "org.celstec.arlearn2.beans.run.Response",
                                    //    "runId": $stateParams.runId,
                                    //    "deleted": false,
                                    //    "generalItemId": $stateParams.activityId,
                                    //    "userEmail": $scope.myAccount.accountType+":"+$scope.myAccount.localId,
                                    //    "responseValue": {
                                    //        "videoUrl": config.server +"/uploadService/"+$stateParams.runId+"/"+$scope.myAccount.accountType+":"+$scope.myAccount.localId+"/"+file.name.replace(/\s+/g, '_'),
                                    //        "fileName": file.name,
                                    //        "fileType": resp.config.data.file.type,
                                    //        "width": 3264,
                                    //        "height": 1840
                                    //    },
                                    //    "parentId": 0,
                                    //    "revoked": false,
                                    //    "lastModificationDate": new Date().getTime()
                                    //}).then(function(data){
                                    //
                                    //});
                                    break;
                                case /image/.test(resp.config.data.file.type):

                                    console.log(resp)
                                    console.log(config)
                                    console.log(config.server +"/generalItems/"+activity.gameId+"/"+file.name.replace(/\s+/g, '_'))
                                    ///generalItems/5784321700921344/requst-a-demo.jpg

                                    //ResponseService.newResponse({
                                    //    "type": "org.celstec.arlearn2.beans.run.Response",
                                    //    "runId": $stateParams.runId,
                                    //    "deleted": false,
                                    //    "generalItemId": $stateParams.activityId,
                                    //    "userEmail": $scope.myAccount.accountType+":"+$scope.myAccount.localId,
                                    //    "responseValue": {
                                    //        "imageUrl": config.server +"/uploadService/"+$stateParams.runId+"/"+$scope.myAccount.accountType+":"+$scope.myAccount.localId+"/"+file.name.replace(/\s+/g, '_'),
                                    //        "fileName": file.name,
                                    //        "fileType": resp.config.data.file.type,
                                    //        "width": 3264,
                                    //        "height": 1840
                                    //    },
                                    //    "parentId": 0,
                                    //    "revoked": false,
                                    //    "lastModificationDate": new Date().getTime()
                                    //}).then(function(data){
                                    //
                                    //});
                                    break;
                                case /pdf/.test(resp.config.data.file.type):
                                    //ResponseService.newResponse({
                                    //    "type": "org.celstec.arlearn2.beans.run.Response",
                                    //    "runId": $stateParams.runId,
                                    //    "deleted": false,
                                    //    "generalItemId": $stateParams.activityId,
                                    //    "userEmail": $scope.myAccount.accountType+":"+$scope.myAccount.localId,
                                    //    "responseValue": {
                                    //        "pdfUrl": config.server +"/uploadService/"+$stateParams.runId+"/"+$scope.myAccount.accountType+":"+$scope.myAccount.localId+"/"+file.name.replace(/\s+/g, '_'),
                                    //        "fileName": file.name,
                                    //        "fileType": resp.config.data.file.type,
                                    //        "width": 3264,
                                    //        "height": 1840
                                    //    },
                                    //    "parentId": 0,
                                    //    "revoked": false,
                                    //    "lastModificationDate": new Date().getTime()
                                    //}).then(function(data){
                                    //
                                    //});
                                    break;
                                case /audio/.test(resp.config.data.file.type):
                                    //ResponseService.newResponse({
                                    //    "type": "org.celstec.arlearn2.beans.run.Response",
                                    //    "runId": $stateParams.runId,
                                    //    "deleted": false,
                                    //    "generalItemId": $stateParams.activityId,
                                    //    "userEmail": $scope.myAccount.accountType+":"+$scope.myAccount.localId,
                                    //    "responseValue": {
                                    //        "audioUrl": config.server +"/uploadService/"+$stateParams.runId+"/"+$scope.myAccount.accountType+":"+$scope.myAccount.localId+"/"+file.name.replace(/\s+/g, '_'),
                                    //        "fileName": file.name,
                                    //        "fileType": resp.config.data.file.type,
                                    //        "width": 3264,
                                    //        "height": 1840
                                    //    },
                                    //    "parentId": 0,
                                    //    "revoked": false,
                                    //    "lastModificationDate": new Date().getTime()
                                    //}).then(function(data){
                                    //
                                    //});
                                    break;
                                case /wordprocessingml|msword/.test(resp.config.data.file.type):
                                    console.log(resp.config.data.file.type);
                                    //ResponseService.newResponse({
                                    //    "type": "org.celstec.arlearn2.beans.run.Response",
                                    //    "runId": $stateParams.runId,
                                    //    "deleted": false,
                                    //    "generalItemId": $stateParams.activityId,
                                    //    "userEmail": $scope.myAccount.accountType+":"+$scope.myAccount.localId,
                                    //    "responseValue": {
                                    //        "documentUrl": config.server +"/uploadService/"+$stateParams.runId+"/"+$scope.myAccount.accountType+":"+$scope.myAccount.localId+"/"+file.name.replace(/\s+/g, '_'),
                                    //        "fileName": file.name,
                                    //        "fileType": resp.config.data.file.type,
                                    //        "width": 3264,
                                    //        "height": 1840
                                    //    },
                                    //    "parentId": 0,
                                    //    "revoked": false,
                                    //    "lastModificationDate": new Date().getTime()
                                    //}).then(function(data){
                                    //
                                    //});
                                    break;
                                case /vnd.ms-excel|spreadsheetml/.test(resp.config.data.file.type):
                                    console.log(resp.config.data.file.type);
                                    //ResponseService.newResponse({
                                    //    "type": "org.celstec.arlearn2.beans.run.Response",
                                    //    "runId": $stateParams.runId,
                                    //    "deleted": false,
                                    //    "generalItemId": $stateParams.activityId,
                                    //    "userEmail": $scope.myAccount.accountType+":"+$scope.myAccount.localId,
                                    //    "responseValue": {
                                    //        "excelUrl": config.server +"/uploadService/"+$stateParams.runId+"/"+$scope.myAccount.accountType+":"+$scope.myAccount.localId+"/"+file.name.replace(/\s+/g, '_'),
                                    //        "fileName": file.name,
                                    //        "fileType": resp.config.data.file.type,
                                    //        "width": 3264,
                                    //        "height": 1840
                                    //    },
                                    //    "parentId": 0,
                                    //    "revoked": false,
                                    //    "lastModificationDate": new Date().getTime()
                                    //}).then(function(data){
                                    //
                                    //});
                                    break;
                                default:
                                    //ResponseService.newResponse({
                                    //    "type": "org.celstec.arlearn2.beans.run.Response",
                                    //    "runId": $stateParams.runId,
                                    //    "deleted": false,
                                    //    "generalItemId": $stateParams.activityId,
                                    //    "userEmail": $scope.myAccount.accountType+":"+$scope.myAccount.localId,
                                    //    "responseValue": file.name,
                                    //    "parentId": 0,
                                    //    "revoked": false,
                                    //    "lastModificationDate": new Date().getTime()
                                    //}).then(function(data){
                                    //
                                    //});
                                    break;
                            }


                            GameService.getGameAssets($stateParams.gameId).then(function(data){
                                $scope.assets = data;
                            });

                            console.log(resp)
                            console.log('Success ' + resp.config.data.file.name + ' uploaded by: ' + resp.config.data);

                        }, function (resp) {
                            console.log(resp)
                            console.log('Error ' + resp.config.data.file.name + ' from: ' + resp.config.data.username);
                            console.log('Error status: ' + resp.status);
                        }, function (evt) {
                            var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                            $scope.progressPercentage = progressPercentage;
                            console.log('progress: ' + progressPercentage + '% ' + evt.config.data.file.name);
                        });
                });

            }
        };

        $scope.ok = function () {

            if(angular.isUndefined($scope.activity.id)){
                ActivityService.removeCachedActivity();
            }

            var _aux = [];

            angular.forEach($scope.activity.roles2, function(role){
                _aux.push(angular.fromJson(role));
            });
            $scope.activity.roles2 = _aux;

            if($scope.activity.type == "org.celstec.arlearn2.beans.generalItem.AudioObject"){
                if($scope.activity.audioFeed == ""){
                    $scope.activity.audioFeed = "-";
                }else if(angular.isUndefined($scope.activity.audioFeed)){
                    $scope.activity.audioFeed = "-";
                }else{
                    // nothing
                }
            }

            if($scope.activity.type == "org.celstec.arlearn2.beans.generalItem.VideoObject"){
                if($scope.activity.videoFeed == ""){
                    $scope.activity.videoFeed = "-";
                }else if(angular.isUndefined($scope.activity.videoFeed)){
                    $scope.activity.videoFeed = "-";
                }else{
                    // nothing
                }
            }

            $modalInstance.close($scope.activity);
        };

        $scope.removeActivity = function(data){
            swal({
                title: "Are you sure?",
                text: "You will not be able to recover this activity!",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "Yes, delete it!",
                closeOnConfirm: false
            }, function () {
                swal("Deleted!", "The activity has been removed from the inquiry structure.", "success");
                ActivityService.deleteActivity(data.gameId, data);
                $modalInstance.dismiss('cancel');
            });
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };

    }])

    .controller('AddUserCtrl', ["$scope", "$modalInstance", "AccountService", "RunService", "run", "game", function ($scope, $modalInstance, AccountService, RunService, run, game) {

        $scope.refreshAccounts = function(query) {

            AccountService.searchAccount(query).then(function(data){
                $scope.availableColors = data.accountList;
            });
        };

        $scope.multipleDemo = {};
        $scope.multipleDemo.colors = [];

        $scope.ok = function () {

            var newUsers = [];

            angular.forEach($scope.multipleDemo.colors, function(value, key) {

                // Grant me access to the run
                RunService.giveAccess(run, value.accountType+":"+value.localId,1);
                // Add me as a user to the run
                var a = RunService.addUserToRun({
                    runId: run,
                    email: value.accountType+":"+value.localId,
                    accountType: value.accountType,
                    localId: value.localId,
                    gameId: game
                });
                newUsers.push(a);
            });

            $modalInstance.close(newUsers);
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };

    }])

    .controller('TabController', ["$scope", "$stateParams", "GameService", "ActivityService", function ($scope, $stateParams, GameService, ActivityService) {
        this.tab = 0;

        GameService.getGameById($stateParams.gameId).then(function (data) {

            if (data.config.roles)
                $scope.roles = data.config.roles;

            $scope.game = data;

        });

        this.setTab = function (tabId) {

            this.tab = tabId;

        };

        this.isSet = function (tabId) {
            return this.tab === tabId;
        };
    }])

;;angular.module('DojoIBL')

    .controller('HomeController', ["$scope", "$sce", "Game", "GameService", "ActivityService", "config", "Session", "RunService", "ChannelService", "AccountService", function ($scope, $sce, Game, GameService, ActivityService, config, Session, RunService, ChannelService, AccountService) {

        $scope.games = {};
        $scope.runs = {};

        function loadGames() {
            GameService.resumeLoadingGames().then(function (data) {
                if (data.error) {
                    $scope.showNoAccess = true;
                } else {
                    $scope.show = true;
                    if (data.resumptionToken) {
                        loadGames();
                    }
                }
            });
        }

        //if(isEmpty($scope.games)){
        loadGames();
        //}

        $scope.games = GameService.getGames();

        $scope.thumbnailUrl = function(gameId) {

            return config.server+'/game/'+gameId+'/gameThumbnail';
        };

        $scope.showRunsValue = true;

        $scope.showRuns = function (id) {
            $scope.gameSelected = id;
            RunService.getParticipateRunsForGame(id).then(function(data){
                $scope.runs[id] = {};
                $scope.runs[id] = data;
            });
        };

        AccountService.myDetails().then(
            function(data){
                $scope.myAccount = data;
            }
        );

        $scope.cloneInquiry = function(id){
            swal({
                title: "Clone inquiry",
                text: "Are you sure you want to clone the inquiry?",
                type: "input",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "Yes, clone it!",
                inputPlaceholder: "Title of the cloned inquiry",
                closeOnConfirm: false
            }, function (inputValue) {

                if (inputValue === false)
                    return false;
                if (inputValue === "") {
                    swal.showInputError("You need to write something!");
                    return false
                }

                swal("Well done!", "You have cloned the inquiry", "success");

                GameService.cloneInquiry(id, inputValue);

            });
        };



        $scope.deleteInquiry = function (id) {

            swal({
                title: "Are you sure?",
                text: "You will not be able to recover this inquiry!",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "Yes, delete it!",
                closeOnConfirm: false
            }, function () {
                swal("Deleted!", "Your inquiry has been deleted.", "success");

                var idx = arrayObjectIndexOf($scope.games, GameService.getGameFromCache(id).gameId, "gameId");

                // is currently selected
                if (idx > -1) {
                    $scope.games.splice(idx, 1);
                }

                GameService.deleteGame(id);

            });

        };

        $scope.config = {
            itemsPerPage: 5,
            fillLastPage: true
        };

        $scope.isLoggedIn = function () {
            if (Session.getAccessToken() ) return true;
            return false;
        };

        ChannelService.register('org.celstec.arlearn2.beans.game.Game', function (notification) {
            //console.info("[Notification][Game]", notification);

            if(!angular.isUndefined(notification.gameId)){
                GameService.refreshGame(notification.gameId);
            }
        });

        $scope.findAndJoin = function(){

            RunService.getRunByCode($scope.inquiryCode).then(function(run){

                AccountService.myDetails().then(function(user){

                    //////
                    // AccessRight explanation
                    // 1: Editor
                    // 2: User

                    console.log(user);

                    GameService.giveAccess(run.game.gameId, user.accountType+":"+user.localId,2);

                    RunService.giveAccess(run.runId, user.accountType+":"+user.localId,2);

                    RunService.addUserToRun({
                        runId: run.runId,
                        email: user.accountType+":"+user.localId,
                        accountType: user.accountType,
                        localId: user.localId,
                        gameId: run.game.gameId });

                    window.location.href=config.server+'/main.html#/inquiry/'+run.runId;
                });
            });

            $scope.inquiryCode = null;
        }


        // New home page
        $scope.gameSelected;

        function arrayObjectIndexOf(myArray, searchTerm, property) {
            for(var i = 0, len = myArray.length; i < len; i++) {
                if (myArray[i][property] === searchTerm) return i;
            }
            return -1;
        }
    }]
);;angular.module('DojoIBL')

    .controller('LandingController', ["$scope", "Session", "Oauth", function ($scope, Session, Oauth) {
        $scope.oauth = Session.getOauthType;
        $scope.accessToken = Session.getAccessToken();
        $scope.isLoggedIn = function () {
            if ($scope.accessToken() && $scope.oauth()) {

                return true;
            }
            else {

                return false;
            }
        };

        $scope.loading = true;

        var providers = {};
        Oauth.info().$promise.then(
            function (data) {
                for (var i = 0; i < data.oauthInfoList.length; i++) {
                    providers['prov' + data.oauthInfoList[i].providerId] = data.oauthInfoList[i];

                    if (data.oauthInfoList[i].providerId == 5){
                        $scope.wespotUrl = "https://wespot-arlearn.appspot.com/Login.html?client_id="+providers.prov5.clientId
                        +"&redirect_uri="+providers.prov5.redirectUri+"&response_type=code&scope=profile+email"
                    }
                }
                $scope.loading = false;
                console.log(providers);
            }
        );

        $scope.providerExists = function(providerId) {
            return (providers['prov'+providerId]!= null);
            //return true;
        };

        $scope.fbUrl = function(){
            if (!providers.prov1) return "";
            return "https://graph.facebook.com/oauth/authorize?client_id="+providers.prov1.clientId+
                "&display=page&redirect_uri="+providers.prov1.redirectUri+"&scope=email";
        };

        $scope.googleUrl = function(){
            return "https://accounts.google.com/o/oauth2/auth?redirect_uri="+providers.prov2.redirectUri
                +"&response_type=code&client_id="+providers.prov2.clientId+
                "&approval_prompt=force&scope=https://www.googleapis.com/auth/userinfo.profile%20%20https://www.googleapis.com/auth/userinfo.email";
        };

        $scope.wespotUrl = function(){
            return "https://wespot-arlearn.appspot.com/Login.html?client_id="+providers.prov5.clientId
                +"&redirect_uri="+providers.prov5.redirectUri+"&response_type=code&scope=profile+email";
        };
    }]
);
;angular.module('DojoIBL')

    .controller('InstantMessagingController', ["$window", "$scope", "$stateParams", "Message", "MessageService", "ChannelService", "AccountService", "UserService", "ngAudio", function ($window, $scope, $stateParams, Message, MessageService,
                                                        ChannelService, AccountService, UserService, ngAudio) {

        var loadMessages = function(anyThing){
            UserService.getUsersForRun(anyThing).then(function(data){
                $scope.usersRun = data;
            }).then(function(){
                $scope.messages = {};
                $scope.messages.messages = [];

                $scope.loadMoreButton = false;
                $scope.disableMessagesLoading = false;

                // Option 1
                $scope.messages.messages = MessageService.getMessages($stateParams.runId);

                // Option 2 - new
                //$scope.loadMoreMessages = function () {
                //
                //    $scope.disableMessagesLoading = true;
                //
                //    Message.resume({resumptionToken: $scope.messages.resumptionToken, runId: $stateParams.runId, from: 0 })
                //        .$promise.then(function (data) {
                //
                //            var messages = [];
                //
                //            angular.forEach(data.messages, function(message){
                //                messages.push(message);
                //            });
                //
                //            $scope.messages.messages = $scope.messages.messages.concat(messages);
                //            $scope.messages.resumptionToken = data.resumptionToken;
                //            $scope.messages.serverTime = data.serverTime;
                //
                //            if (data.resumptionToken) {
                //                $scope.disableMessagesLoading = false
                //            } else {
                //                $scope.disableMessagesLoading = true
                //            }
                //        });
                //};
            });
        }
        $scope.scroll = 0;
        AccountService.myDetails().then(
            function(data){
                $scope.myAccount = data;
            }
        );



        $scope.$on('inquiry-run', function(event, args) {
            $scope.disableMessagesLoading = true;
            loadMessages(args.runId)
        });

        $scope.account = AccountService.myDetailsCache();

        loadMessages($stateParams.runId);



        $scope.bodyMessage;
        $scope.glued = true;

        $scope.sendMessage = function() {
            var m = $scope.bodyMessage;
            $scope.bodyMessage = null;
            AccountService.myDetails().then(function(data){
                if(m){
                    MessageService.newMessage({
                        runId: $stateParams.runId,
                        threadId: data.accountType + ":" + data.localId,
                        subject: "empty",
                        body: m
                    }).then(function(data){
                    });
                }
            });
        };

        $scope.numberMessages = 0;

        ChannelService.register('org.celstec.arlearn2.beans.run.Message', function (notification) {
            if (notification.runId == $stateParams.runId) {
                $scope.numberMessages += 1;

                MessageService.getMessageById(notification.messageId).then(function (data) {
                    if($scope.account.localId != data.senderId){
                        console.info("[Notification][Message]", notification, $scope.account.localId, data.senderId);

                        notifyMe(notification);
                        //$scope.sound = ngAudio.load("/src/assets/beep.m4a");
                        //$scope.sound.play();
                    }


                });



            }
        });

        $scope.notifications = [];
        $scope.waitingForData = function () {
            $scope.notifications.length == 0;
        };

        function notifyMe(message) {

            var not = {};
            not.body = message.body;

            AccountService.accountDetailsById(message.senderProviderId+":"+message.senderId).then(function(account){
                not.icon = account.picture;
                not.title = account.name;
                console.log(not)



                // Let's check if the browser supports notifications
                if (!("Notification" in window)) {
                    alert("This browser does not support desktop notification");
                }

                // Let's check whether notification permissions have already been granted
                else if (Notification.permission === "granted") {
                    // If it's okay let's create a notification
                    var notification = new Notification(not.title, {
                        body: not.body,
                        icon:not.icon
                    });
                }

                // Otherwise, we need to ask the user for permission
                else if (Notification.permission !== 'denied') {
                    Notification.requestPermission(function (permission) {
                        // If the user accepts, let's create a notification
                        if (permission === "granted") {
                            var notification = new Notification(not.title, {
                                body: not.body,
                                icon:not.icon
                            });
                        }
                    });
                }


            });


            // At last, if the user has denied notifications, and you
            // want to be respectful there is no need to bother them any more.
        }
    }]
);;angular.module('DojoIBL')

    .controller('OauthController', ["$scope", "$stateParams", "Session", "$location", function ($scope, $stateParams, Session, $location) {
        $scope.token = $stateParams.accessToken;
        $scope.type = $stateParams.type;
        $scope.expires = $stateParams.expires;
        Session.setAccessToken($stateParams.accessToken);

        $location.path('/home');

    }]
);;angular.module('DojoIBL')

    .controller('CalendarController', ["$scope", "$sce", "$stateParams", "$state", "Response", "ActivityService", "UserService", "GameService", "RunService", "AccountService", "ChannelService", "$location", function ($scope, $sce, $stateParams, $state, Response,
                                                ActivityService, UserService, GameService, RunService, AccountService,
                                                ChannelService, $location) {


        ChannelService.register('org.celstec.arlearn2.beans.notification.GeneralItemModification', function (notification) {
            ActivityService.refreshActivity(notification.itemId, notification.gameId);
            toaster.success({
                title: 'Activity modified',
                body: 'The structure of the activity has been modified.'
            });
        });

        $scope.events = [];

        RunService.getRunById($stateParams.runId).then(function (run) {
            ActivityService.getActivitiesServer(run.gameId);
        });

        $scope.events = ActivityService.getCalendarActivities()[$stateParams.gameId]
        $scope.eventSources = [$scope.events];


        /* message on eventClick */
        $scope.alertOnEventClick = function( event, allDay, jsEvent, view ){
            $scope.goToActivity($stateParams.runId, event.activity.section, event.activity.id)
        };

        /* config object */
        $scope.uiConfig = {
            calendar:{
                firstDay:1,
                height: 450,
                editable: true,
                header: {
                    left: 'prev,next,today',
                    center: 'title',
                    right: 'month,agendaWeek,agendaDay'
                },
                eventClick: $scope.alertOnEventClick,
                disableDragging :true,
                views: {
                    basic: {
                        // options apply to basicWeek and basicDay views
                    },
                    agenda: {
                        // options apply to agendaWeek and agendaDay views
                    },
                    week: {
                        // options apply to basicWeek and agendaWeek views
                    },
                    day: {
                        // options apply to basicDay and agendaDay views
                    }
                }
            }
        };

        $scope.goToActivity = function(inqId, index, activity) {
            $location.path('inquiry/'+inqId+'/phase/'+ index + '/activity/' +activity);
        };
    }]
);;angular.module('DojoIBL')

    .controller('InquiryController', ["$scope", "$sce", "$location", "$stateParams", "$state", "Session", "MessageService", "ActivityService", "AccountService", "ChannelService", "RunService", function ($scope, $sce, $location, $stateParams, $state, Session, MessageService,
                                               ActivityService, AccountService, ChannelService, RunService) {

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

            ActivityService.getActivitiesServer(data.gameId);
        });

        $scope.activities = ActivityService.getActivities();

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

        //
        //$scope.arrangeInCircle = function(index){
        //    var radius = 170;
        //    var fields = $(this.el).find('#circlemenu li'),
        //        container = $(this.el).find('#circlemenu'),
        //        width = container.width(),
        //        height = container.height(),
        //        angle = 300,
        //        step = (2*Math.PI) / fields.length,
        //        x_initial = -4,
        //        y_initial = ;
        //
        //
        //
        //    //console.log(fields, container);
        //
        //    fields.each(function() {
        //
        //        //console.log(width, $(this).width());
        //
        //        var x = Math.round(width/2 + radius * Math.cos(angle) - $(this).width()/2);
        //        var y = Math.round(height/2 + radius * Math.sin(angle) - $(this).height()/2);
        //        //console.log(x,y);
        //        if(window.console) {
        //            //console.log($(this).text(), x, y);
        //        }
        //        $(this).css({
        //            left: x + 'px',
        //            top: y + 'px'
        //        });
        //        angle += step;
        //    });
        //};

        //

    }]
);;angular.module('DojoIBL')

    .controller('InquiryEditRunController', ["$scope", "$sce", "$stateParams", "$state", "Session", "RunService", "UserService", "Contacts", function ($scope, $sce, $stateParams, $state, Session, RunService, UserService, Contacts ) {

        $scope.roles = [];

        RunService.getRunById($stateParams.runId).then(function(data){
            if (data.error) {
                $scope.showNoAccess = true;
            } else {
                $scope.show = true;
            }

            $scope.game = data.game;

            if(!$scope.game.config.roles)
                $scope.game.config.roles = [];

            $scope.run = data;

            if (data.lat) {
                $scope.coords.latitude = data.lat;
                $scope.coords.longitude = data.lng;
                $scope.map.center.latitude = data.lat;
                $scope.map.center.longitude = data.lng;
                $scope.showMap = true;
            }
        });

        UserService.getUsersForRun($stateParams.runId).then(function(data){
            $scope.usersRun = data;
        });

        $scope.ok = function(){
            if($scope.run.runId){
                RunService.updateRun($scope.run);
            }else{
                RunService.newRun($scope.run);
            }
        };

        $scope.removeAccess = function(runId, account){
            swal({
                title: "Remove user from inquiry",
                text: "Are you sure you want to remove this student from the inquiry?",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "Yes, remove the student!",
                closeOnConfirm: false
            }, function () {
                swal("Student removed!", "The student has been removed from the list of participants in this inquiry.", "success");

                console.log($scope.usersRun, runId, account.email);

                var idx = arrayObjectIndexOf($scope.usersRun, account.email, "email");

                if (idx > -1) {
                    $scope.usersRun.splice(idx, 1);
                }

                RunService.removeAccessToInquiryRun(runId, account)

            });
        };

        $scope.friends = [];

        Contacts.getContacts({date:0}).$promise.then(
            function(data){
                for(var i=0;i<data.accountList.length;i++){
                    $scope.friends.push(data.accountList[i]);
                }
            }
        );

        //////////////////
        // Extra functions
        //////////////////
        function arrayObjectIndexOf(myArray, searchTerm, property) {
            for(var i = 0, len = myArray.length; i < len; i++) {
                if (myArray[i][property] === searchTerm) return i;
            }
            return -1;
        }
    }]);;angular.module('DojoIBL')

    .controller('TimelineController', ["$scope", "$sce", "$stateParams", "$state", "Response", "ActivityService", "UserService", "GameService", "RunService", "AccountService", "ChannelService", function ($scope, $sce, $stateParams, $state, Response,
                                                ActivityService, UserService, GameService, RunService, AccountService,
                                                ChannelService) {

        $scope.games = {};
        $scope.games.games = [];

        $scope.disableGameLoading = false;

        var old_item_id = 0;
        var old_side_left = true;

        ChannelService.register('org.celstec.arlearn2.beans.run.Response', function (data) {
            if(data.runId == $stateParams.runId){
                var responses = [];

                if(data.generalItemId != old_item_id){
                    if(old_side_left){
                        data.move_right = false;
                    }else{
                        data.move_right = true;
                    }
                }

                old_item_id = data.generalItemId;
                old_side_left = data.move_right;

                RunService.getRunById($stateParams.runId).then(function (run) {
                    ActivityService.getActivityById(data.generalItemId, run.gameId).then(function(act){
                        data.activity = act;
                    });
                });

                //UserService.getUserByAccount($stateParams.runId, data.userEmail.split(':')[1]).then(function(user){
                //    data.user = user;
                //});
                data.user = UserService.getUser($stateParams.runId, data.userEmail);


                responses.push(data);
                $scope.games.games = responses.concat($scope.games.games)
                //$scope.responses.responses = ResponseService.getResponses($stateParams.runId, $stateParams.activityId);
                //if((user.localId != data.userEmail.split(':')[1]) && data.generalItemId == $stateParams.activityId && data.runId == $stateParams.runId)
                //    //ResponseService.addResponse(data, $stateParams.runId, $stateParams.activityId);
                //console.info("[Notification][Response]", data, $stateParams.runId, $stateParams.activityId);
            }
        });


        $scope.loadMoreGames = function () {

            $scope.disableGameLoading = true;

            Response.resume({resumptionToken: $scope.games.resumptionToken, runId: $stateParams.runId, from: 0 })
                .$promise.then(function (data) {

                    var responses = [];

                    angular.forEach(data.responses, function(resp){

                        // TODO check this is not working well
                        if(resp.generalItemId != old_item_id){
                            if(old_side_left){
                                resp.move_right = false;
                            }else{
                                resp.move_right = true;
                            }
                        }

                        old_item_id = resp.generalItemId;
                        old_side_left = resp.move_right;

                        RunService.getRunById($stateParams.runId).then(function (run) {
                            ActivityService.getActivityById(resp.generalItemId, run.gameId).then(function(data){
                                resp.activity = data;
                            });
                        });

                        //UserService.getUserByAccount($stateParams.runId, resp.userEmail.split(':')[1]).then(function(data){
                        //    resp.user = data;
                        //});

                        resp.user = UserService.getUser($stateParams.runId, resp.userEmail);

                        responses.push(resp);
                    });

                    $scope.games.games = $scope.games.games.concat(responses);
                    $scope.games.resumptionToken = data.resumptionToken;
                    $scope.games.serverTime = data.serverTime;

                    if (data.resumptionToken) {
                        $scope.disableGameLoading = false
                    } else {
                        $scope.disableGameLoading = true
                    }
                });
        };


        UserService.getUsersForRun($stateParams.runId).then(function(data){

            $scope.usersRun = data;


        });

        $scope.filter = {};

        // Functions - Definitions
        $scope.filterByCategory = function(response) {
            return $scope.filter[response.user.localId] || noFilter($scope.filter);
        };

        function noFilter(filterObj) {
            return Object.
                keys(filterObj).
                every(function (key) { return !filterObj[key]; });
        }

    }]
);