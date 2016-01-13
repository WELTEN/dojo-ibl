Backbone.View.prototype.close = function () {
    //console.log('Closing view ', this);
    if (this.beforeClose) {
        this.beforeClose();
    }
    this.undelegateEvents();
    this.remove();
    this.unbind();
};

var AppRouter = Backbone.Router.extend({
    initialize: function() {
        this.GameList = new GameCollection();
        this.RunList = new RunCollection({ });
        this.UsersList = new UserCollection({ });
        this.Response = new ResponseCollection();
        this.ActivityList = new ActivityCollection({ });
    },
    routes: {
        ""			                                        : "showInquiries",
        "logout"			                                : "logout",
        "inquiry/:id"	                                    : "showInquiry",
        "inquiry/new/"	                                    : "createInquiry",
        "inquiry/:id/phase/:phase"                          : "showPhase",
        "inquiry/:id/phase/:phase/activity/:activity"       : "showActivity"
    },

    // main views
    showInquiries: function() {

        this.isAuthenticated();
        this.common();
        this.initialGame();
        this.initializeChannelAPI();
        //this.breadcrumbManagerSmall("","List of inquiries");
        this.changeTitle("List of inquiries");

        $(".join-inquiry").click(function(){

        });
    },

    showInquiry:function (id) {
        this.isAuthenticated();
        $(".phases-breadcrumb").hide();

        this.createCookie("dojoibl.run", id);
        this.changeTitle("List of inquiries");

        this.common();
        this.breadcrumbManager(0, "");

        this.initializeChannelAPI();
        this.loadChat();

        this.RunList = new RunCollection({ });
        this.RunList.id = id;

        this.RunList.fetch({
            beforeSend: setHeader,
            success: function (response, results) {

                console.log("load run info and add sidebarview");

                app.breadcrumbManagerSmall("","list of inquiries");
                app.changeTitle(results.title);

                console.log(results);

                $('.row.inquiry').html(new InquiryView({ model: results }).render().el);
                $('.row.inquiry').append(new SideBarView({ }).render().el);

                $(".knob").knob();

                $('.chat-discussion').slimScroll({
                    height: '400px',
                    start: 'bottom',
                    railOpacity: 0.4
                });
            }
        });

        $('.tooltip-demo').tooltip({
            selector: "[data-toggle=tooltip]",
            container: "body"
        });

        /////////////////////////////////////////////////////////////////////////////
        // Hide scroll bar while hovering the chat to make the chat experience easier
        // Avoid undesirable scrolling movements
        /////////////////////////////////////////////////////////////////////////////
        //var b = $('html');
        //$('.direct-chat').hover(function() {
        //    var s = b.scrollTop();
        //    b.css('overflow', 'hidden');
        //    b.scrollTop(s);
        //}, function() {
        //    var s = b.scrollTop();
        //    b.css('overflow', 'auto');
        //    b.scrollTop(s);
        //});


    },
    showPhase: function(id, phase){
        this.isAuthenticated();

        var _self = this;
        var _gameId = id;
        var _phase = phase;
        var _runId = 0;

        if($.cookie("dojoibl.run")){
            _runId = $.cookie("dojoibl.run");
        }

        this.createCookie("dojoibl.game", _gameId);

        $("#circlemenu li:nth-child("+phase+")").addClass("animated bounceOutUp");
        window.setTimeout(function () {
            app.breadcrumbManager(1, "Data collection");

            app.breadcrumbManagerSmall("#inquiry/"+_runId,"the list of phases");

            app.changeTitle(_phase);

            $(this).addClass("animated fadeOutUpBig");
            this.ActivityList = new ActivitiesCollection({ });
            this.ActivityList
            this.ActivityList.id = _gameId;
            this.ActivityList.section = phase;

            if($(".col-md-9.wrapper.wrapper-content.animated.fadeInUp").length == 0){
                $(".row.inquiry").append($('<div />', {
                    "class": 'col-md-9 wrapper wrapper-content animated fadeInUp',
                    id: "inquiry-content"
                }));
                $('.row.inquiry').append(new SideBarView({ }).render().el);
                app.initializeChannelAPI();
                app.loadChat();
            }

            $("#inquiry-content").html(new PhaseView().render().el);

            this.ActivityList.fetch({
                beforeSend: setHeader,
                success: function (response, results) {
                    _.each(results.generalItems, function(generalItem){
                        $('.ibox-content > .row.m-t-sm > .list_activities').append(new ActivityBulletView({
                            model: generalItem,
                            phase: _phase
                        }).render().el);
                    });
                    $(".knob").knob();
                }
            });
        }, 500);
    },
    showActivity: function(id, phase, activity){
        this.isAuthenticated();
        this.common();

        var _phase = phase;
        var _gameId = id;
        var _runId = 0;

        if($.cookie("dojoibl.run")){
            _runId = $.cookie("dojoibl.run");
        }

        this.createCookie("dojoibl.activity", activity);

        this.loadInquiryUsers(_runId);

        this.breadcrumbManagerSmall("#inquiry/"+_gameId+"/phase/"+_phase,"the list of activities");


        $( ".list_activities li[data='"+activity+"']").addClass("animated bounceOutUp");
        window.setTimeout(function () {

            this.Activity = new ActivityCollection({ });
            this.Activity.id = activity;
            this.Activity.gameId = id;
            this.Activity.section = phase;
            this.Activity.fetch({
                beforeSend: setHeader,
                success: function (response, xhr) {
                    app.breadcrumbManager(2, "Data Collection", xhr.name );
                    app.changeTitle(xhr.name);

                    if($(".col-md-9.wrapper.wrapper-content.animated.fadeInUp").length == 0){
                        $(".row.inquiry").append($('<div />', {
                            "class": 'col-md-9 wrapper wrapper-content animated fadeInUp',
                            id: "inquiry-content"
                            }
                        ));

                        if($(".col-lg-3.wrapper.wrapper-content.small-chat-float").length == 0){
                            $('.row.inquiry').append(new SideBarView({ }).render().el);
                            app.initializeChannelAPI();
                            app.loadChat();
                        }
                    }

                    $("#inquiry-content").html(new ActivityView({ model: xhr }).render().el);

                    $(".knob").knob();

                    if(xhr.type.indexOf("SingleChoiceImageTest") > -1) {
                        //var responses = new ResponseCollection();
                        app.Response.id = _runId;
                        app.Response.itemId = xhr.id;
                        app.Response.fetch({
                            beforeSend: setHeader,
                            success: function(response, xhr){
                                //console.log(xhr, response)
                                new ConceptMapView({ model: xhr }).render().el;
                            }
                        });
                    }else{
                        //app.Responses = new ResponseCollection();

                        if(xhr.type.indexOf("VideoObject") > -1){
                            //
                            //}else if(xhr.type.indexOf("OpenBadge") > -1) {
                            //
                            //}else if(xhr.type.indexOf("MultipleChoiceImageTest") > -1) {
                            //    new window.ResponseTreeView({ collection: app.Responses });
                        }else{
                            new window.ResponseListView({ collection: app.Response, users: app.InquiryUsers, game: _gameId, run: _runId });
                            //new window.ResponseListView({ collection: app.Responses });
                        }

                        app.Response.id = _runId;
                        app.Response.itemId = xhr.id;
                        app.Response.fetch({
                            beforeSend: setHeader
                        });

                        $('#list_answers').addClass("social-footer");
                        $('#list_answers').append(new ResponseReplyView({}).render().el);

                        $(".previous-activity").click(function(){
                            console.log("previous activity");
                        });

                        $(".next-activity").click(function(){
                            console.log("next activity");
                        });

                        ///////////////////////////////////////////////////////////////////////////
                        // This event should be captured outside to manage the comments in the main
                        // discussion thread
                        ///////////////////////////////////////////////////////////////////////////
                        $(".save[responseid='0']").click(function(){

                            if  ($("textarea[responseid='0']").val() != ""){

                                var newResponse = new Response({ generalItemId: xhr.id, responseValue: $("textarea[responseid='0']").val(), runId: $.cookie("dojoibl.run"), userEmail: 0, parentId: 0 });
                                newResponse.save({}, {
                                    beforeSend:setHeader,
                                    success: function(r, new_response){
                                        app.Response.add(new_response);
                                        //$(".m-r-sm.text-muted.notification-text").addClass("animated fadeInUp").html("Comment sent").fadeOut(5000);
                                        app.showNotification("success", "Discussion thread", "Your comment has been sent");
                                    }
                                });
                            }
                            $("textarea[responseid='0']").val("");
                        });
                    }
                }
            });
        }, 500);
    },
    createInquiry: function(){
        this.isAuthenticated();
        app.showView(".row.inquiry", new InquiryNewView());

        app.changeTitle("New inquiry");
        app.breadcrumbManager(0, "");


        var _gameId = 0;

        $("#wizard").steps({
            bodyTag: "div.step-content",
            onStepChanging: function (event, currentIndex, newIndex){
                console.log(_gameId);

                // Suppress (skip) "Warning" step if the user is old enough.
                if (currentIndex === 1){

                    if(_gameId != 0){
                        var newGame = new Game({ gameId: _gameId, title: $("#inquiry-title-value").val(), description: $("#inquiry-description-value").val() });
                        newGame.save({}, { beforeSend:setHeader });
                    }else{
                        var newGame = new Game({ title: $("#inquiry-title-value").val(), description: $("#inquiry-description-value").val() });
                        newGame.save({}, {
                            beforeSend:setHeader,
                            success: function(r, new_response){
                                _gameId = new_response.gameId;
                                var _name = new_response.title;
                                var _description = new_response.description;

                                app.createCookie("dojoibl.game", _gameId);


                                //////////////////////
                                // Give access to Game
                                //////////////////////
                                var newAccessGame = new GiveAccessToGame();
                                newAccessGame.gameId = new_response.gameId;
                                newAccessGame.accoundId = $.cookie("dojoibl.accountType")+":"+$.cookie("dojoibl.localId");
                                newAccessGame.accessRight = 1;
                                newAccessGame.fetch({}, { beforeSend:setHeader });

                                app.GameList.add(new_response);

                                /////////////////////////////////
                                // Create the run for the inquiry
                                /////////////////////////////////
                                var newRun = new Run({
                                    title: _name,
                                    description: _description,
                                    gameId: _gameId
                                });
                                newRun.save({}, {
                                    beforeSend:setHeader,
                                    success: function(r, new_response){
                                        console.log(new_response);
                                        app.RunList.add(new_response);

                                        app.createCookie("dojoibl.code", new_response.code);

                                        /////////////////////
                                        // Give access to Run
                                        /////////////////////
                                        var newAccessRun = new GiveAccessToRun();
                                        newAccessRun.runId = new_response.runId;
                                        newAccessRun.accoundId = $.cookie("dojoibl.accountType")+":"+$.cookie("dojoibl.localId");
                                        newAccessRun.accessRight = 1;
                                        newAccessRun.fetch({}, { beforeSend:setHeader });

                                        ////////////////////
                                        // Add user to a run
                                        ////////////////////
                                        var newUserForRun = new AddUserToRun({
                                            runId: new_response.runId,
                                            email: $.cookie("dojoibl.accountType")+":"+$.cookie("dojoibl.localId"),
                                            accountType: $.cookie("dojoibl.accountType"),
                                            localId: $.cookie("dojoibl.localId"),
                                            gameId: $.cookie("dojoibl.game") });
                                        newUserForRun.save({}, { beforeSend:setHeader });

                                        app.showNotification("success", "Inquiry created successfully", "You can find "+_name+" in your inquiry list.");

                                    }
                                });
                            }
                        });
                    }
                }

                if (currentIndex === 5){
                    console.log("Create roles and assign them to the game");
                }

                var form = $(this).find("form");

                // Clean up if user went backward before
                if (currentIndex < newIndex){
                    // To remove error styles
                    $(".body:eq(" + newIndex + ") label.error", form).remove();
                    $(".body:eq(" + newIndex + ") .error", form).removeClass("error");
                }

                // Disable validation on fields that are disabled or hidden.
                form.validate().settings.ignore = ":disabled,:hidden";

                // Start validation; Prevent going forward if false
                return form.valid();
            },
            onStepChanged: function (event, currentIndex, priorIndex){
            },
            onFinishing: function (event, currentIndex){
                // Disable validation on fields that are disabled.
                // At this point it's recommended to do an overall check (mean ignoring only disabled fields)
                //form.validate().settings.ignore = ":disabled";
                //
                //var avatar = new PictureUrlGame({ });
                //avatar.gameId = $.cookie("dojoibl.game");
                //avatar.fetch({
                //    beforeSend:setHeader,
                //    success: function(e , r) {
                //        console.log(r.uploadUrl);
                //        $("#my-awesome-dropzone").attr('action', r.uploadUrl);
                //        $("input#submit").click(function(e) {
                //            e.preventDefault();
                //           console.log(e);
                //        });
                //    }

                //
                //        var _url = r.uploadUrl;
                //
                //        //new Dropzone("#my-awesome-dropzone", {
                //        //    url: _url,
                //        //    paramName: "file"
                //        //
                //        //});
                //
                //        //$("#my-awesome-dropzone").attr('action', );
                //        //$("").submit();
                //
                //        //$.ajax({
                //        //    type: "post",
                //        //    url: r.uploadUrl,
                //        //    contentType:"multipart/form-data" ,
                //        //    data: $("#my-awesome-dropzone :file"), // serializes the form's elements.
                //        //    success: function(data){
                //        //        alert(data); // show response from the php script.
                //        //    },
                //        //    error: function(e){
                //        //        console.log(e);
                //        //    }
                //        //});
                //
                //        //var $form = $( "#my-awesome-dropzone" ),
                //        //    term = this.$("form :file"),
                //        //    url = r.uploadUrl;
                //        //
                //        //console.log(term);
                //        //
                //        //// Send the data using post
                //        //var posting = $.post( url, term );
                //        //
                //        //// Put the results in a div
                //        //posting.done(function( data ) {
                //        //    console.log(data);
                //        //});
                //
                //    }
                //});

                $('.row.inquiry').html(new NewInquiryCode({ code: $.cookie("dojoibl.code") }).render().el);


                return true;
            },
            onFinished: function (event, currentIndex) {
            },
            afterSync: function(event){

            }
        }).validate({
            errorPlacement: function (error, element)
            {
                element.before(error);
            },
            rules: {
                inquiry_title: "required",
                inquiry_description: "required"
            }
        });

        this.addDragDrop();

        var pageNum = 1;

        ////////////////////
        // Manage activities
        ////////////////////
        this.newActivityNewInquiry(pageNum);
        this.removeActivity();

        ////////////////
        // Manage phases
        ////////////////
        this.newPhaseNewInquiry();
        this.removePhase();
    },

    // util functions
    initialGame: function(callback) {
        //this.changeTitle("Inquiries");

        this.cleanMainView();

        this.GameParticipateList = new GameParticipateCollection();
        this.GameParticipateList.fetch({
            beforeSend: setHeader,
            success: successGameParticipateHandler
        });

        //$(".confirm-new-inquiry").click(function(e){
        //    var new_game = new MyGame({ title: $("#new_inquiry input").val(), description: $("#new_inquiry textarea").val() });
        //    new_game.save({}, {
        //        beforeSend:setHeader,
        //        success: function(r, game){
        //            app.GameList.add(game);
        //            app.navigate('inquiry/new/');
        //         }
        //    });
        //    $('#new_inquiry').modal('toggle');
        //    e.preventDefault();
        //});

        //console.log("GameAccessList",app.GameAccessList);
        //console.log("GameParticipateList", app.GameParticipateList);
        //console.log("GameList", app.GameList);
    },
    initialRun: function(callback) {
        if (this.RunList) {
            if (callback)
                callback();
        } else {
            this.RunAccessList = new RunAccessCollection();
            this.RunAccessList.fetch({
                beforeSend: setHeader,
                success: function(response, xhr) {

                    _.each(xhr.runAccess, function(e){

                        this.RunList.id = e.runId;

                        this.RunList.fetch({
                            beforeSend: setHeader,
                            success: function (response, results) {

                                $('#inquiries > div > div.box-body').append( new RunListView({ model: results }).render().el );
                                if (callback)
                                    callback();
                            }
                        });
                    });
                }
            });
        }
    },
    newPhaseNewInquiry: function(){
        $("li.new-phase").click(function(e){
            e.preventDefault();
            var _phase = new NewPhaseNewInquiryView({}).render().el;

            var _number_phase = $("div[id*='tab']").length;
            _number_phase += 1;

            _phase.id = "tab-"+_number_phase;
            $(".tab-content").append(_phase);

            $('<li><a data-toggle="tab" href="#tab-'+_number_phase+'" > Phase '+_number_phase+'<i class="fa fa-remove remove-phase"></i></a></li>')
                .insertBefore($("ul.select-activities > li.new-phase"));

            app.newActivityNewInquiry(_number_phase);
            app.removePhase();

            app.addDragDrop();
        });
    },
    newActivityNewInquiry: function(tab){
        $("#tab-"+tab+" button.new-activity").click(function(e){
            e.preventDefault();
            $("div[id*='tab'].active tbody").append(new NewActivityNewInquiryView({}).render().el);

            ///////////////////////////////////////////////////////////////////
            // We need to put it also here for those divs generated with jQuery
            ///////////////////////////////////////////////////////////////////
            app.removeActivity();
        });
    },
    addDragDrop: function(){

        $( "#drag-list" ).sortable({
            connectWith: "div",
            remove: function(event, ui) {
                var _gameId = 0;

                if($.cookie("dojoibl.game")){
                    _gameId = $.cookie("dojoibl.game");
                }


                ui.item.clone().appendTo(this);
                $('.selected').removeClass('selected');
                ui.item.addClass('remove').addClass('selected');

                var _aux = ui.item;

                var type = $(ui.item).attr('data');
                var phase = $(ui.item).closest("div[id*='tab']").attr("id").substring(4,5);
                var feed = "";

                if(type == "org.celstec.arlearn2.beans.generalItem.VideoObject"){
                    var data = {
                        type: type,
                        section: phase,
                        gameId: _gameId,
                        deleted: true,
                        name: "Dummy title",
                        description: "Dummy description",
                        autoLaunch: false,
                        fileReferences: [],
                        sortKey: 1,
                        videoFeed: feed
                    };
                }else if(type == "org.celstec.arlearn2.beans.generalItem.AudioObject") {
                    var data = {
                        type: type,
                        section: phase,
                        gameId: _gameId,
                        deleted: true,
                        name: "Dummy title",
                        description: "Dummy description",
                        autoLaunch: false,
                        fileReferences: [],
                        sortKey: 1,
                        audioFeed: feed
                    };
                }else{
                    var data = {
                        type: type,
                        section: phase,
                        gameId: _gameId,
                        deleted: true,
                        name: "Dummy title",
                        description: "Dummy description",
                        autoLaunch: false,
                        fileReferences: [],
                        sortKey: 1
                    };
                }

                /////////////////////////////////////
                // Create the activity = General Item
                /////////////////////////////////////
                var newActivity = new Activity(data);
                newActivity.save({}, {
                    beforeSend:setHeader,
                    success: function(r, new_response){
                        $(_aux).attr("id", new_response.id);
                        app.ActivityList.add(new_response);
                    }
                });
            }
        });

        $("#wizard-p-2 form .form-control").focusout(function(e, i){

            var frm = $('.tab-pane.active .form-horizontal .form-control');

            var data = app.getDataForm(frm);

            /////////////////////////////////////
            // Create the activity = General Item
            /////////////////////////////////////
            var newActivity = new ActivityUpdate(data);
            newActivity.id = data.id;
            newActivity.gameId = data.gameId;
            newActivity.save({}, {
                beforeSend:setHeader,
                type: 'POST',
                success: function(r, new_response){

                }
            });
        });

        $( ".activities" ).sortable({
            connectWith: "div.activities"
        });

        $("#remove-drag").droppable({
            drop: function (event, ui) {
                $(ui.draggable).remove();

                var _gameId = 0;

                if($.cookie("dojoibl.game")){
                    _gameId = $.cookie("dojoibl.game");
                }

                /////////////////////////////////////
                // Create the activity = General Item
                /////////////////////////////////////
                var newActivity = new ActivityDelete({id: $(ui.draggable).attr("id")});
                newActivity.id = $(ui.draggable).attr("id");
                newActivity.gameId = _gameId;
                newActivity.destroy({
                    beforeSend:setHeader,
                    type: 'DELETE',
                    success: function(r, new_response){
                        console.log(r);
                    }
                });
            },
            hoverClass: "remove-drag-hover",
            accept: '.remove'
        });


    },
    removeActivity: function(){
        $("button.remove-activity").click(function(){
            console.log($(this));
            $(this).parent().parent().remove();
        });

    },
    removePhase: function(){
        $(".remove-phase").click(function(){
            console.log("Delete phase. Delete all the general items under it");
            var tabId = $(this).parents('li').children('a').attr('href');
            $(this).parents('li').remove('li');
            $(tabId).remove();
            $(".tabs-container tab-content").addClass("active");
            ////reNumberPages();
            //$('#pageTab a:first').tab('show');
        });
    },
    showView: function(selector, view) {
        if (this.currentView) {
            this.currentView.close();
        }
        $(selector).html(view.render().el);
        this.currentView = view;
        return view;
    },
    createCookie: function (name, value) {
        var date = new Date();
        date.setTime(date.getTime() + (1 * 24 * 60 * 60 * 1000));
        var expires = "; expires=" + date.toGMTString();

        $.cookie(name, value, {expires: date, path: "/"});
    },
    showNotification: function(type, title, message){
        toastr.options = {
            "closeButton": true,
            "debug": false,
            "progressBar": true,
            "preventDuplicates": false,
            "positionClass": "toast-top-right",
            "onclick": null,
            "showDuration": "10000",
            "hideDuration": "10000",
            "timeOut": "10000",
            "extendedTimeOut": "10000",
            "showEasing": "swing",
            "hideEasing": "linear",
            "showMethod": "fadeIn",
            "hideMethod": "fadeOut"
        }
        //if ($('#addBehaviorOnToastClick').prop('checked')) {
        //    toastr.options.onclick = function () {
        //        alert('You can perform some custom action after a toast goes away');
        //    };
        //}
        //$("#toastrOptions").text("Command: toastr["
        //    + shortCutFunction
        //    + "](\""
        //    + msg
        //    + (title ? "\", \"" + title : '')
        //    + "\")\n\ntoastr.options = "
        //    + JSON.stringify(toastr.options, null, 2)
        //);
        var $toast = toastr[type](message, title); // Wire up an event handler to a button in the toast, if it exists
    },
    getDataForm: function($form){
        var unindexed_array = $form.serializeArray();
        var indexed_array = {};

        $.map(unindexed_array, function(n, i){
            indexed_array[n['name']] = n['value'];
        });

        return indexed_array;
    },

    // chat
    initializeChannelAPI: function(){
        if(!this.ChannelAPI){
            console.debug("[initializeChannelAPI]", "Initializing the chat");

            this.ChannelAPI = new ChannelAPICollection();
            this.ChannelAPI.fetch({
                beforeSend: setHeader,
                success: function(response, xhr){

                    channel = new goog.appengine.Channel(xhr.token);
                    socket = channel.open();

                    socket.onopen = function() {
                        ///////////////////////////////////////////////////////////////////////////////////////
                        // TODO Block the chat until is connected
                        ///////////////////////////////////////////////////////////////////////////////////////
                        $('#messages').append('<p>Connected!</p>');
                    };

                    socket.onmessage = function(message) {
                        var a = $.parseJSON(message.data);

                        // TODO create a getMessage service on the API
                        //if(app.MessagesList.where({ id: a.messageId }).length == 0){
                        //    console.log("dentro");
                        //    var message = new Message({ id: a.messageId });
                        //    message.fetch({
                        //        beforeSend: setHeader
                        //    });
                        //    app.MessagesList.add(message);
                        //}

                        //console.log(app.MessagesList);

                        if (a.type == "org.celstec.arlearn2.beans.notification.MessageNotification") {
                            if ($('.chat-discussion').length) {
                                console.log(a);
                                var aux = a.messageId;
                                $('.chat-discussion').append(new MessageFromNotificationView({model: a}).render().el);
                                //if (a.senderId == app.UserList.at(0).toJSON().localId){
                                //    $('.direct-chat-messages').append(new MessageLeftView({ model: a }).render().el);
                                //}else{
                                //    $('.direct-chat-messages').append(new MessageRightView({ model: a }).render().el);
                                //}

                                $('.chat-discussion').animate({
                                    scrollTop: $('.chat-discussion')[0].scrollHeight
                                }, 200);
                            }
                            //else {
                            //    ///////////////////////////////////////////////////////////////////////////////////////
                            //    // TODO We would need to check here if I have other windows open not nofifying myself again.
                            //    // TODO Changes in one collection modifies a view: http://stackoverflow.com/questions/10341141/backbone-multiple-views-subscribing-to-a-single-collections-event-is-this-a-b
                            //    ///////////////////////////////////////////////////////////////////////////////////////
                            //    $('.messages-menu > ul').append(new MessageNotificationView({model: a}).render().el);
                            //
                            //    var new_floating_notification = new GeneralFloatingNotificationView({model: a}).render().el;
                            //
                            //    var message_notifications = $('.messages-menu span.label-success').html();
                            //
                            //    if (message_notifications != "" && message_notifications > 0) {
                            //        message_notifications = parseInt(message_notifications) + 1;
                            //        $('.messages-menu span.label-success').html(message_notifications);
                            //    } else {
                            //        $('.messages-menu span.label-success').html(1);
                            //    }
                            //
                            //    /////////////////////////
                            //    // Floating notifications
                            //    /////////////////////////
                            //    var top = $(new_floating_notification).position().top;
                            //
                            //    var max_top = 64;
                            //
                            //    var pile_notifications = $('body').find($(".ui-pnotify.stack-topleft")).length;
                            //
                            //    max_top = max_top + 100 * pile_notifications;
                            //
                            //    console.log("Adding notification: Pixels:", max_top, "Bubbles:" + pile_notifications);
                            //
                            //    $(new_floating_notification).css({top: max_top + 'px'});
                            //
                            //    $('body').append(new_floating_notification);
                            //
                            //    setTimeout(showNotifications, 5000);
                            //    function showNotifications() {
                            //        $(new_floating_notification).remove();
                            //
                            //        $.each($(".ui-pnotify.stack-topleft"), function (key, value) {
                            //            var top = $(this).position().top;
                            //
                            //            if (top == "64") {
                            //                $(this).remove();
                            //            }
                            //
                            //            var descrease_top = top - 100 * pile_notifications;
                            //            //max_top = max_top - 100;
                            //
                            //            console.log("Removing notification: Pixels:", max_top, "Bubbles:" + pile_notifications, "Decrementar pixels:" + descrease_top);
                            //
                            //
                            //            $(this).css({top: descrease_top + 'px'});
                            //        });
                            //    }
                            //
                            //}
                        }

                        //if (a.type == "org.celstec.arlearn2.beans.run.Response") {
                        //    /////////////////////////
                        //    // Sidebar notifications
                        //    /////////////////////////
                        //    $("ul.notifications-side-bar").prepend(new NotificationSideBarView({model: a}).render().el)
                        //}
                    };

                    socket.onerror = function() { $('#messages').append('<p>Connection Error!</p>'); };
                    socket.onclose = function() { $('#messages').append('<p>Connection Closed!</p>'); };
                }
            });
        }
    },
    loadChat: function(){

        if($(".chat-discussion").length != 0){
            console.log("hay chat");
        }

        console.debug("[loadChat]", "Loading the chat content");
        this.MessagesList = new MessageCollection();
        this.MessagesList.id = $.cookie("dojoibl.run");
        this.MessagesList.fetch({
            beforeSend: setHeader,
            success: function (response, xhr) {

                console.info("TODO: retrieve messages per blocks");

                _.each(xhr.messages, function(message){
                    //console.log(message);
                    //////////////////////////////////////////////////////////////
                    // TODO make different type of message if it is not my message
                    // TODO place the focus at the end of the chat box
                    //////////////////////////////////////////////////////////////
                    if (message.senderId == app.UsersList.at(0).toJSON().localId){
                        $('.chat-discussion').append(new MessageRightView({ model: message }).render().el);
                    }else{
                        $('.chat-discussion').append(new MessageLeftView({ model: message }).render().el);
                    }

                    //if($('.chat-discussion').length != 0){
                    //    console.log($('.chat-discussion')[0])
                    //    $('.chat-discussion').animate({
                    //        scrollTop: $('.chat-discussion')[0].scrollHeight
                    //    }, 0);
                    //}else{
                    //    console.log($('.chat-discussion'));
                    //}

                });
            }
        });

        //$('.direct-chat-messages').animate({
        //    scrollTop: $('.direct-chat-messages')[0].scrollHeight
        //}, 2000);
    },

    // common
    breadcrumbManager: function(level, title_1, title_2){
        $(".col-sm-7.breadcrumb-flow.tooltip-demo").html("");

        for (var i = 0; i < level; i++) {
            $(".col-sm-7.breadcrumb-flow.tooltip-demo").append(new ItemBreadcrumbView({ }).render().el);
            switch(i){
                case 0:
                    // Inquiry
                    $(".col-sm-7.breadcrumb-flow.tooltip-demo > div:nth-child("+1+")").addClass("phase ");
                    $(".col-sm-7.breadcrumb-flow.tooltip-demo > div:nth-child("+1+") > .phases-breadcrumb").attr({ "title": title_1 });

                    break;
                case 1:
                    // Phase
                    $(".col-sm-7.breadcrumb-flow.tooltip-demo > div:nth-child("+2+")").addClass("activity animated tada");
                    $(".col-sm-7.breadcrumb-flow.tooltip-demo > div:nth-child("+2+") > .phases-breadcrumb").attr({ "title": title_2 });
                    $(".col-sm-7.breadcrumb-flow.tooltip-demo > div:nth-child("+2+") > .phases-breadcrumb > input").attr({ "value": 23 });
                    break;
            }
            $(".col-sm-7.breadcrumb-flow.tooltip-demo > div").removeClass("animated tada");
            $(".col-sm-7.breadcrumb-flow.tooltip-demo > div:last-child").addClass("animated tada");
        }
    },
    breadcrumbManagerSmall: function(routing, label){
        $("ol.breadcrumb").html('<li><a href="'+routing+'"><i class="fa fa-angle-left"></i> <strong>Back to '+label+'</strong></a></li>');
    },
    changeTitle: function(title) {
        $('.row.wrapper.border-bottom.white-bg.page-heading > .col-sm-3 > h2').html(title);
    },
    cleanMainView: function(){
        $(".inquiry").html("");
    },
    common: function(callback) {
        app.UsersList.fetch({
            beforeSend: setHeader,
            success: function(response, xhr) {
                $(".m-r-sm.text-muted.notification-text").html(xhr.givenName+" "+xhr.familyName);
                app.showView('ul.nav.metismenu > li:eq(0)', new UserView({ model: xhr }));

                var date = new Date();
                date.setTime(date.getTime() + (1 * 24 * 60 * 60 * 1000));
                var expires = "; expires=" + date.toGMTString();

                $.cookie("dojoibl.accountType", xhr.accountType , {expires: date, path: "/"});
                $.cookie("dojoibl.localId", xhr.localId , {expires: date, path: "/"});
            }
        });
    },
    loadInquiryUsers: function(id){
        if(!this.InquiryUsers){
            // Load users of the Inquiry
            this.InquiryUsers = new UserRunCollection({});
            this.InquiryUsers.runId = id;
            this.InquiryUsers.fetch({
                beforeSend: setHeader,
                success: successInquiryUsers
            });
        }
    },

    // authentication
    isAuthenticated: function (){
        app.UsersList.fetch({
            beforeSend: setHeader,
            success: function(response, xhr) {
                if(typeof xhr == 'undefined' || xhr.errorCode == 2){
                    if(window.location.hostname.toLowerCase().indexOf("localhost") >= 0){
                        window.location = "https://wespot-arlearn.appspot.com/Login.html?client_id=wespotClientId&redirect_uri=http://localhost:8888/oauth/wespot&response_type=code&scope=profile+email";
                    }else{
                        window.location = "https://wespot-arlearn.appspot.com/Login.html?client_id=wespotClientId&redirect_uri=http://dojo-ibl.appspot.com/oauth/wespot&response_type=code&scope=profile+email";
                    }
                }
            }
        });
    },
    logout: function() {
        $.cookie("dojoibl.run", null, { path: '/' });
        $.cookie("dojoibl.game", null, { path: '/' });
        $.cookie("arlearn.AccessToken", null, { path: '/' });
        $.cookie("arlearn.OauthType", null, { path: '/' });
        app.navigate('');
        window.location.replace("/");
    }
});

var messageNotifications = function (r, a, message){
    console.log(message);
};

var successInquiryUsers = function(response, xhr){
    $('span.label.label-success.number-participants').html((xhr.users.length == 1) ? xhr.users.length+" user" :xhr.users.length+" users" );
    _.each(xhr.users, function(participant){
        $('ul.users-list').append( new UsersInquiryView({ model: participant}).render().el );
    });
};

var successGameParticipateHandler = function(response, xhr){
    console.log("Games I participate:");
    _.each(xhr.games, function(game){
        console.log(game.gameId);
        var _gl = app.GameList.get(game.gameId);
        if(!_gl) {
            app.GameList.add(game);
        }
    });

    app.GameAccessList = new GameAccessCollection();
    app.GameAccessList.fetch({
        beforeSend: setHeader,
        success: successGameHandler
    });
};

var successGameHandler = function(response, xhr){
    console.log("Games I have access / I have created:");
    _.each(xhr.gamesAccess, function(e){
        console.log(e);
        var _gl = app.GameList.get(e.gameId);
        if(!_gl) {



            var game = new GameCollection({  });
            game.gameId = e.gameId;
            game.fetch({
                beforeSend: setHeader,
                success: function (response, game) {
                    $('.inquiry').append( new GameListView({ model: game, v: 2 }).render().el );
                }
            });
            console.log(game);
            app.GameList.add(game);
        }else{
            var game = _gl;
            $('.inquiry').append( new GameListView({ model: game.toJSON(), v: 1 }).render().el );
        }
    });
};

tpl.loadTemplates(['main', 'game','game_teacher', 'inquiry', 'run', 'user', 'user_sidebar', 'phase', 'activity', 'activity_detail','activity_details', 'inquiry_structure',
    'inquiry_sidebar', 'activityDependency', 'message', 'message_right', 'inquiry_left_sidebar','message_own', 'response', 'response_discussion', 'response_treeview','response_author', 'response_discussion_author',
    'message_notification','notification_floating', 'activity_video', 'activity_widget', 'activity_discussion', 'notification_sidebar', 'user_inquiry','activity_tree_view',
    'item_breadcrumb_phase'
    , 'item_breadcrumb_activity','new_inquiry_code','activity_html', 'response_reply', 'new_inquiry', 'activity_concept_map', 'new_activity_new_inquiry', 'new_phase_new_inquiry'], function() {
    app = new AppRouter();
    Backbone.history.start();
});
