Backbone.View.prototype.close = function () {
    //console.log('Closing view ', this);
    if (this.beforeClose) {
        this.beforeClose();
    }
    this.remove();
    this.unbind();
};

var AppRouter = Backbone.Router.extend({
    initialize: function() {
        this.RunList = new RunCollection({ });
    },
    routes: {
        ""			: "inquiries",
        "logout"			: "logout",
        "inquiry/:id"	: "showInquiry",
        "inquiry/:id/phase/:phase" : "showPhase",
        "inquiry/:id/phase/:phase/activity/:activity" : "showActivity"
    },
    logout: function() {
        $.cookie("dojoibl.run", null, { path: '/' });
        $.cookie("dojoibl.game", null, { path: '/' });
        $.cookie("arlearn.AccessToken", null, { path: '/' });
        $.cookie("arlearn.OauthType", null, { path: '/' });
        app.navigate('');
        window.location.replace("/");
    },
    inquiries: function() {
        this.isAuthenticated();
        this.common();
        this.initialGame();
    },
    initialGame: function(callback) {
        this.changeTitle("Inquiries");

        this.cleanMainView();

        this.GameList = new GameCollection();

        this.GameParticipateList = new GameParticipateCollection();
        this.GameParticipateList.fetch({
            beforeSend: setHeader,
            success: successGameParticipateHandler
        });

        $(".confirm-new-inquiry").click(function(e){
            var new_game = new MyGame({ title: $("#new_inquiry input").val(), description: $("#new_inquiry textarea").val() });
            new_game.save({}, {
                beforeSend:setHeader,
                success: function(r, game){
                    app.GameList.add(game);
                    var new_run = new MyRun({ title: $("#new_inquiry input").val(), gameId: game.gameId, description: $("#new_inquiry textarea").val() });
                    new_run.save({}, {
                        beforeSend:setHeader,
                        success: function(r, run){
                            app.RunList.add(run);
                        }
                    });
                }
            });
            $(this).hide();
            e.preventDefault();
        });

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
    showInquiry:function (id) {
        this.isAuthenticated();

        $(".phases-breadcrumb").hide();
        window.Run.global_identifier = id;

        var date = new Date();
        date.setTime(date.getTime() + (1 * 24 * 60 * 60 * 1000));
        var expires = "; expires=" + date.toGMTString();

        $.cookie("dojoibl.run", id, {expires: date, path: "/"});

        this.common();
        this.changeTitle("Inquiry view");
        this.breadcrumbManager(0, "");
        this.initializeChannelAPI();

        this.RunList = new RunCollection({ });
        this.RunList.id = id;

        this.RunList.fetch({
            beforeSend: setHeader,
            success: function (response, results) {
                console.log(results);
                app.showView('.row.inquiry', new InquiryView({ model: results }));
                $('.row.inquiry').append(new SideBarView({ }).render().el);
                $(".knob").knob();

                $('.chat-discussion').slimScroll({
                    height: '400px',
                    start: 'bottom',
                    railOpacity: 0.4
                });

                $('input#add-new-message').keypress(function (e) {
                    var key = e.which;
                    if(key == 13){
                        console.log("message send");
                        var newMessage = new Message({ runId: id, threadId: 0, subject: "", body: $('input#add-new-message').val() });

                        newMessage.save({}, {
                            beforeSend:setHeader
                        });
                        $('input#add-new-message').val('');
                    }
                });

                $('button#send-message').click(function(){

                    var newMessage = new Message({ runId: id, threadId: 0, subject: "", body: $('input#add-new-message').val() });

                    newMessage.save({}, {
                        beforeSend:setHeader
                    });
                    $('input#add-new-message').val('');
                });
            }
        });

        $('.tooltip-demo').tooltip({
            selector: "[data-toggle=tooltip]",
            container: "body"
        });


        //$("#circlemenu li").click(function(){
        //
        //});
        ////this.loadInquiryUsers(id);
        //this.initializeChannelAPI();
        //this.loadChat(id);

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

        var date = new Date();
        date.setTime(date.getTime() + (1 * 24 * 60 * 60 * 1000));
        var expires = "; expires=" + date.toGMTString();

        $.cookie("dojoibl.game", _gameId, {expires: date, path: "/"});

        $("#circlemenu li:nth-child("+phase+")").addClass("animated bounceOutUp");
        window.setTimeout(function () {
            app.breadcrumbManager(1, "Data collection");
            app.changeTitle("Data collection");

            $(this).addClass("animated fadeOutUpBig");
            this.ActivityList = new ActivitiesCollection({ });
            this.ActivityList
            this.ActivityList.id = _gameId;

            if($(".col-md-9.wrapper.wrapper-content.animated.fadeInUp").length == 0){
                $(".row.inquiry").append($('<div />', {
                    "class": 'col-md-9 wrapper wrapper-content animated fadeInUp',
                    id: "inquiry-content"
                }));
                $('.row.inquiry').append(new SideBarView({ }).render().el);
            }

            $("#inquiry-content").html(new PhaseView().render().el);

            this.ActivityList.fetch({
                beforeSend: setHeader,
                success: function (response, results) {
                    _.each(results.generalItems, function(generalItem){
                        $('.ibox-content > .row.m-t-sm > .list_activities').append(new ActivityBulletView({ model: generalItem, phase: _phase }).render().el);
                    });
                    $(".knob").knob();
                }
            });
        }, 500);

        //var date = new Date();
        //date.setTime(date.getTime() + (1 * 24 * 60 * 60 * 1000));
        //var expires = "; expires=" + date.toGMTString();
        //$.cookie("dojoibl.game", id, {expires: date, path: "/"});
        //
        /////////////////////////////////////////////////////////////////////
        //// If we refresh the in phase view we need to load everything again
        /////////////////////////////////////////////////////////////////////
        //if ( $("#inquiry").length == 0 || $("aside#summary").length == 0 ){
        //    if($.cookie("dojoibl.run")){
        //        $('#inquiries').html(new InquiryStructureView({ }).render().el);
        //
        //        this.RunList = new RunCollection({ });
        //        this.RunList.id = $.cookie("dojoibl.run");
        //
        //        this.RunList.fetch({
        //            beforeSend: setHeader,
        //            success: function (response, results) {
        //
        //                this.inquiryView = new InquiryView({ model: results });
        //
        //                $('aside#summary').html(this.inquiryView.render().el);
        //                $(".knob").knob();
        //                $("#inquiry").hide();
        //
        //                _self.loadPhaseActivities(_id, _phase);
        //                _self.common();
        //                _self.loadChat($.cookie("dojoibl.run"));
        //                _self.loadInquiryUsers($.cookie("dojoibl.run"));
        //            }
        //        });
        //    }
        //}
        //
        //this.loadPhaseActivities(id, phase);

    },
    showActivity: function(id, phase, activity){
        this.isAuthenticated();

        var _phase = phase;
        var _gameId = id;
        var _runId = 0;

        if($.cookie("dojoibl.run")){
            _runId = $.cookie("dojoibl.run");
        }

        this.loadInquiryUsers(_runId);

        $( ".list_activities li[data='"+activity+"']").addClass("animated bounceOutUp");
        window.setTimeout(function () {

            this.Activity = new ActivityCollection({ });
            this.Activity.id = activity;
            this.Activity.gameId = id;
            this.Activity.fetch({
                beforeSend: setHeader,
                success: function (response, xhr) {
                    if($(".col-md-9.wrapper.wrapper-content.animated.fadeInUp").length == 0){
                        $(".row.inquiry").append($('<div />', {
                            "class": 'col-md-9 wrapper wrapper-content animated fadeInUp',
                            id: "inquiry-content"
                            }
                        ));
                        $('.row.inquiry').append(new SideBarView({ }).render().el);
                    }
                    app.breadcrumbManager(2, "Data Collection", xhr.name );
                    app.changeTitle("Phase name");

                    $("#inquiry-content").html(new ActivityView({ model: xhr }).render().el);
                    $(".knob").knob();

                    app.Responses = new ResponseCollection();

                    if(xhr.type.indexOf("VideoObject") > -1){
                    //
                    //}else if(xhr.type.indexOf("OpenBadge") > -1) {
                    //
                    //}else if(xhr.type.indexOf("MultipleChoiceImageTest") > -1) {
                    //    new window.ResponseTreeView({ collection: app.Responses });
                    //}else if(xhr.type.indexOf("AudioObject") > -1) {
                    //    new window.ResponseDiscussionListView({ collection: app.Responses, users: app.InquiryUsers });
                    }else{
                        new window.ResponseListView({ collection: app.Responses, users: app.InquiryUsers, game: _gameId, run: _runId });
                        //new window.ResponseListView({ collection: app.Responses });
                    }

                    app.Responses.id = _runId;
                    app.Responses.itemId = xhr.id;
                    app.Responses.fetch({
                        beforeSend: setHeader
                    });

                    $('#list_answers').addClass("social-footer");
                    $('#list_answers').append(new ResponseReplyView({}).render().el);

                    $('.save').click(function(){
                        if  ($('#list_answers textarea').val() != ""){
                            console.log($('#list_answers textarea').val());
                            var newResponse = new Response({ generalItemId: xhr.id, responseValue: $('#list_answers textarea').val(), runId: $.cookie("dojoibl.run"), userEmail: 0 });
                            newResponse.save({}, {
                                beforeSend:setHeader,
                                success: function(r, new_response){
                                    app.Responses.add(new_response);
                                }
                            });
                        }
                        $('#list_answers textarea').val("");
                    });
                }
            });
        }, 500);

        // ============================================

        //this.loadInquiryUsers($.cookie("dojoibl.run"));

        //if ($(".phase-master").length == 0){
        //    this.showPhase(id, phase);
        //    console.log("show phase", $(".phase-master").html());
        //    app.navigate('inquiry/'+id+'/phase/'+phase);
        //}else{
        //    if ($(".phase-detail").length != 0){
        //        console.info("Activity is already open");
        //        $(".phase-detail").remove();
        //    }
        //
        //    $(".phase-master").animate({
        //        'marginLeft' : '-14em',
        //        'width': '167px',
        //        'top': '222px'
        //    }, 200, function() {
        //        $(this).find(".box-tools.pull-right").hide();
        //    });
        //
        //    this.Activity = new ActivityCollection({ });
        //    this.Activity.id = activity;
        //    this.Activity.gameId = id;
        //    this.Activity.fetch({
        //        beforeSend: setHeader,
        //        success: function(response, xhr){
        //
        //            $('section.phase-master').after(new ActivityView({ model: xhr }).render().el);
        //
        //            app.Responses = new window.ResponseCollection();
        //
        //            if(xhr.type.indexOf("VideoObject") > -1){
        //
        //            }else if(xhr.type.indexOf("OpenBadge") > -1) {
        //
        //            }else if(xhr.type.indexOf("MultipleChoiceImageTest") > -1) {
        //                new window.ResponseTreeView({ collection: app.Responses });
        //            }else if(xhr.type.indexOf("AudioObject") > -1) {
        //                new window.ResponseDiscussionListView({ collection: app.Responses, users: app.InquiryUsers });
        //            }else{
        //                new window.ResponseListView({ collection: app.Responses, users: app.InquiryUsers });
        //            }
        //
        //            app.Responses.id = window.Run.global_identifier;
        //            app.Responses.itemId = xhr.id;
        //            app.Responses.fetch({
        //                beforeSend: setHeader
        //            });
        //
        //            ///////////////////////////////
        //            // Listener to submit responses
        //            ///////////////////////////////
        //            $(".close-detail, .cancel").click(function(e){
        //                //$(".phase-master").animate({
        //                //    'marginLeft' : '+0%'
        //                //});
        //
        //                $(".phase-master").animate({
        //                    'marginLeft' : '+0%',
        //                    'width': '100%',
        //                    'top': '0px'
        //                });
        //                $(".phase-master").find(".box-tools.pull-right").show();
        //                $(".phase-master").find(".skill-tree-row.row-1").show();
        //
        //
        //                //console.debug(xhr,"saving or closing...");
        //
        //                app.navigate('inquiry/'+xhr.gameId+'/phase/'+_phase);
        //
        //                $(".phase-detail").hide();
        //                $(".phase-detail").remove();
        //
        //                e.preventDefault();
        //            });
        //
        //            $('textarea').keyup(function(e){
        //                //console.log(e);
        //                if(e.keyCode == 51){
        //                    console.debug("[successActivityHandl]","Display other responses to link them.");
        //                    $(this).trigger('enter');
        //                }
        //            });
        //
        //            $(".save").click(function(){
        //                $(".message-user").html("Saving..").show().delay(500).fadeOut();
        //
        //                if  ($('textarea.response').val() == ""){
        //
        //                }else{
        //                    var newResponse = new Response({ generalItemId: xhr.id, responseValue: $('textarea.response').val(), runId: window.Run.global_identifier, userEmail: 0 });
        //                    newResponse.save({}, {
        //                        beforeSend:setHeader,
        //                        success: function(r, new_response){
        //                            app.Responses.add(new_response);
        //                        }
        //                    });
        //                }
        //            });
        //
        //            $(".save-close").click(function(){
        //                $(".message-user").html("Saving..").show().delay(500).fadeOut();
        //
        //                if  ($('textarea.response').val() == ""){
        //                    //$(".phase-master").animate({
        //                    //    'marginLeft' : '+0%'
        //                    //});
        //
        //                    $(".phase-master").animate({
        //                        'marginLeft' : '+0%',
        //                        'width': '100%',
        //                        'top': '0px'
        //                    });
        //                    $(".phase-master").find(".box-tools.pull-right").show();
        //                    $(".phase-master").find(".skill-tree-row.row-1").show();
        //
        //
        //                    //console.debug(xhr,"saving or closing...");
        //
        //                    app.navigate('inquiry/'+xhr.gameId+'/phase/1');
        //
        //                    $(".phase-detail").hide();
        //                    $(".phase-detail").remove();
        //                }else{
        //                    var newResponse = new Response({ generalItemId: xhr.id, responseValue: $('textarea.response').val(), runId: window.Run.global_identifier, userEmail: 0 });
        //                    newResponse.save({}, {
        //                        beforeSend:setHeader,
        //                        success: function(r, s){
        //
        //                            //
        //                            //$(".phase-master").animate({
        //                            //    'marginLeft' : '+0%',
        //                            //    'width': '100%',
        //                            //    'top': '0px'
        //                            //});
        //                            //$(".phase-master").find(".box-tools.pull-right").show();
        //                            //
        //                            //console.debug(s, xhr,"saving or closing...");
        //                            //
        //                            //app.navigate('inquiry/'+xhr.gameId+'/phase/1');
        //                            //
        //                            //$(".phase-detail").hide();
        //                            //$(".phase-detail").remove();
        //
        //
        //                            e.preventDefault();
        //                        }
        //                    });
        //
        //                    $(".phase-master").animate({
        //                        'marginLeft' : '+0%',
        //                        'width': '100%',
        //                        'top': '0px'
        //                    });
        //                    $(".phase-master").find(".box-tools.pull-right").show();
        //                    $(".phase-master").find(".skill-tree-row.row-1").show();
        //
        //
        //                    //console.debug(xhr,"saving or closing...");
        //
        //                    app.navigate('inquiry/'+xhr.gameId+'/phase/1');
        //
        //                    $(".phase-detail").hide();
        //                    $(".phase-detail").remove();
        //                }
        //            });
        //
        //            $('input.response').keypress(function (e) {
        //                var key = e.which;
        //                if(key == 13){
        //                    if  ($('input.response').val() == ""){
        //
        //                    }else{
        //                        var newResponse = new Response({ generalItemId: xhr.id, responseValue: $('input.response').val(), runId: window.Run.global_identifier, userEmail: 0 });
        //                        newResponse.save({}, {
        //                            beforeSend:setHeader,
        //                            success: function(r, new_response){
        //                                app.Responses.add(new_response);
        //                            }
        //                        });
        //                    }
        //                    $('input.response').val("");
        //                }
        //            });
        //
        //            $("button.new-todo-task").click(function(e){
        //                $("div.new-todo-task").removeClass("hide");
        //            });
        //
        //            $('input.new-todo-tasks-value').keypress(function (e) {
        //                var key = e.which;
        //                if(key == 13){
        //                    if  ($('input.new-todo-tasks-value').val() == ""){
        //
        //                    }else{
        //                        var newResponse = new Response({ generalItemId: xhr.id, responseValue: $('input.new-todo-tasks-value').val(), runId: window.Run.global_identifier, userEmail: 0 });
        //                        newResponse.save({}, {
        //                            beforeSend:setHeader,
        //                            success: function(r, new_response){
        //                                app.Responses.add(new_response);
        //                            }
        //                        });
        //                    }
        //
        //                    $('input.new-todo-tasks-value').val("");
        //                    $("div.new-todo-task").addClass("hide");
        //                }
        //            });
        //
        //            $(".phase-detail").show();
        //        }
        //    });
        //
        //    if($.cookie("dojoibl.run")){
        //        window.Run.global_identifier = $.cookie("dojoibl.run");
        //    }
        //}


    },
    newInquiry: function(){



        //var new_game = new MyGame({ title:  });
        //new_game.save({}, {
        //    beforeSend:setHeader,
        //    success: function(r, game){
        //        app.GameList.add(game);
        //    }
        //});
    },
    // util functions
    initializeChannelAPI: function(){
        console.debug("[initializeChannelAPI]", "Initializing the chat");
        this.loadChat();

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

    },
    loadChat: function(id){
        console.debug("[loadChat]", "Loading the chat content");
        this.MessagesList = new MessageCollection();
        this.MessagesList.id = $.cookie("dojoibl.run");
        this.MessagesList.fetch({
            beforeSend: setHeader,
            success: function (response, xhr) {

                console.info("TODO: retrieve messages per blocks");

                _.each(xhr.messages, function(message){
                    //////////////////////////////////////////////////////////////
                    // TODO make different type of message if it is not my message
                    // TODO place the focus at the end of the chat box
                    //////////////////////////////////////////////////////////////
                    if (message.senderId == app.UserList.at(0).toJSON().localId){
                        $('.chat-discussion').append(new MessageRightView({ model: message }).render().el);
                    }else{
                        $('.chat-discussion').append(new MessageLeftView({ model: message }).render().el);
                    }

                    $('.chat-discussion').animate({
                        scrollTop: $('.chat-discussion')[0].scrollHeight
                    }, 0);
                });
            }
        });

        //$('.direct-chat-messages').animate({
        //    scrollTop: $('.direct-chat-messages')[0].scrollHeight
        //}, 2000);
    },
    common: function(callback) {
        // Load user information
        this.UserList = new UserCollection();
        this.UserList.fetch({
            beforeSend: setHeader,
            success: function(response, xhr) {
                $(".m-r-sm.text-muted.welcome-message").html("Welcome "+xhr.givenName+" "+xhr.familyName);
                app.showView('ul.nav.metismenu > li:eq(0)', new UserView({ model: xhr }));
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
    showView: function(selector, view) {
        if (this.currentView) {
            this.currentView.close();
            console.log(this.currentView);
        }
        $(selector).html(view.render().el);
        this.currentView = view;
        return view;
    },
    isAuthenticated: function (){
        if($.cookie("arlearn.AccessToken") === "null"){
            if(window.location.hostname.toLowerCase().indexOf("localhost") >= 0){
                window.location = "https://wespot-arlearn.appspot.com/Login.html?client_id=wespotClientId&redirect_uri=http://localhost:8888/oauth/wespot&response_type=code&scope=profile+email";
            }else{
                window.location = "https://wespot-arlearn.appspot.com/Login.html?client_id=wespotClientId&redirect_uri=http://dojo-ibl.appspot.com/oauth/wespot&response_type=code&scope=profile+email";
            }
        }
    },
    changeTitle: function(title) {
        $('.row.wrapper.border-bottom.white-bg.page-heading > .col-sm-3 > h2').html(title);
    },
    cleanMainView: function(){
        $(".inquiry").html("");
    },
    breadcrumbManager: function(level, title_1, title_2){
        $(".col-sm-7.breadcrumb-flow.tooltip-demo").html("");
        for (var i = 0; i < level; i++) {
            $(".col-sm-7.breadcrumb-flow.tooltip-demo").append(new ItemBreadcrumbView({ }).render().el);
            switch(i){
                case 0:
                    $(".col-sm-7.breadcrumb-flow.tooltip-demo > div:nth-child("+1+")").addClass("phase ");
                    $(".col-sm-7.breadcrumb-flow.tooltip-demo > div:nth-child("+1+") > .phases-breadcrumb").attr({ "title": title_1 });
                    break;
                case 1:
                    $(".col-sm-7.breadcrumb-flow.tooltip-demo > div:nth-child("+2+")").addClass("activity animated tada");
                    $(".col-sm-7.breadcrumb-flow.tooltip-demo > div:nth-child("+2+") > .phases-breadcrumb").attr({ "title": title_2 });
                    $(".col-sm-7.breadcrumb-flow.tooltip-demo > div:nth-child("+2+") > .phases-breadcrumb > input").attr({ "value": 23 });
                    break;
            }
            $(".col-sm-7.breadcrumb-flow.tooltip-demo > div").removeClass("animated tada");
            $(".col-sm-7.breadcrumb-flow.tooltip-demo > div:last-child").addClass("animated tada");
        }
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
        console.log(e.gameId);
        var _gl = app.GameList.get(e.gameId);
        if(!_gl) {
            var game = new Game({ gameId: e.gameId });
            game.fetch({
                beforeSend: setHeader,
                success: function (response, game) {
                    $('.inquiry').append( new GameListView({ model: game, v: 2 }).render().el );
                }
            });
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
    , 'item_breadcrumb_activity', 'response_reply'], function() {
    app = new AppRouter();
    Backbone.history.start();
});
