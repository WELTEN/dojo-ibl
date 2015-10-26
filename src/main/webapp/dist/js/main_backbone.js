Backbone.View.prototype.close = function () {
    console.log('Closing view ' + this);
    if (this.beforeClose) {
        this.beforeClose();
    }
    this.remove();
    this.unbind();
};

var AppRouter = Backbone.Router.extend({

    initialize: function() {
    },
    routes: {
        ""			: "landing",
        "inquiries"			: "inquiries",
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
    landing: function() {
        this.common();
    },
    inquiries: function() {
        this.common();
        this.initialGame();
        //this.initialRun();
        this.initializeChannelAPI();
    },
    initialGame: function(callback) {

        $('#page-wrapper > .row:last').html( new MainView({ }).render().el );

        //this.GameList = new GameCollection();
        //
        //this.GameAccessList = new GameAccessCollection();
        //this.GameAccessList.fetch({
        //    beforeSend: setHeader,
        //    success: successGameHandler
        //});
        //
        //this.GameParticipateList = new GameParticipateCollection();
        //this.GameParticipateList.fetch({
        //    beforeSend: setHeader,
        //    success: successGameParticipateHandler
        //});


        console.log("GameAccessList",app.GameAccessList);
        console.log("GameParticipateList", app.GameParticipateList);
        console.log("GameList", app.GameList);
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
                        this.RunList = new RunCollection({ });
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

        window.Run.global_identifier = id;

        var date = new Date();
        date.setTime(date.getTime() + (1 * 24 * 60 * 60 * 1000));
        var expires = "; expires=" + date.toGMTString();

        $.cookie("dojoibl.run", id, {expires: date, path: "/"});

        this.common();
        this.loadInquiryUsers(id);
        this.initializeChannelAPI();
        this.loadChat(id);

        $('#inquiries').html(new InquiryStructureView({ }).render().el);
        //$('aside#summary').hide();

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

        $('input#add-new-message').keypress(function (e) {
            var key = e.which;
            if(key == 13){
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

        this.RunList = new RunCollection({ });
        this.RunList.id = id;

        this.RunList.fetch({
            beforeSend: setHeader,
            success: function (response, results) {

                this.inquiryView = new InquiryView({ model: results });

                $('aside#summary').html(this.inquiryView.render().el);
                $(".knob").knob();
                $("#inquiry").hide();

            }
        });
    },
    showPhase: function(id, phase){
        var _self = this;
        var _id = id;
        var _phase = phase;

        var date = new Date();
        date.setTime(date.getTime() + (1 * 24 * 60 * 60 * 1000));
        var expires = "; expires=" + date.toGMTString();
        $.cookie("dojoibl.game", id, {expires: date, path: "/"});

        ///////////////////////////////////////////////////////////////////
        // If we refresh the in phase view we need to load everything again
        ///////////////////////////////////////////////////////////////////
        if ( $("#inquiry").length == 0 || $("aside#summary").length == 0 ){
            if($.cookie("dojoibl.run")){
                $('#inquiries').html(new InquiryStructureView({ }).render().el);

                this.RunList = new RunCollection({ });
                this.RunList.id = $.cookie("dojoibl.run");

                this.RunList.fetch({
                    beforeSend: setHeader,
                    success: function (response, results) {

                        this.inquiryView = new InquiryView({ model: results });

                        $('aside#summary').html(this.inquiryView.render().el);
                        $(".knob").knob();
                        $("#inquiry").hide();

                        _self.loadPhaseActivities(_id, _phase);
                        _self.common();
                        _self.loadChat($.cookie("dojoibl.run"));
                        _self.loadInquiryUsers($.cookie("dojoibl.run"));
                    }
                });
            }
        }

        this.loadPhaseActivities(id, phase);

    },
    showActivity: function(id, phase, activity){

        var _phase = phase;

        this.loadInquiryUsers($.cookie("dojoibl.run"));

        if ($(".phase-master").length == 0){
            this.showPhase(id, phase);
            console.log("show phase", $(".phase-master").html());
            app.navigate('inquiry/'+id+'/phase/'+phase);
        }else{
            if ($(".phase-detail").length != 0){
                console.info("Activity is already open");
                $(".phase-detail").remove();
            }

            $(".phase-master").animate({
                'marginLeft' : '-14em',
                'width': '167px',
                'top': '222px'
            }, 200, function() {
                $(this).find(".box-tools.pull-right").hide();
            });

            this.Activity = new ActivityCollection({ });
            this.Activity.id = activity;
            this.Activity.gameId = id;
            this.Activity.fetch({
                beforeSend: setHeader,
                success: function(response, xhr){

                    $('section.phase-master').after(new ActivityView({ model: xhr }).render().el);

                    app.Responses = new window.ResponseCollection();

                    if(xhr.type.indexOf("VideoObject") > -1){

                    }else if(xhr.type.indexOf("OpenBadge") > -1) {

                    }else if(xhr.type.indexOf("MultipleChoiceImageTest") > -1) {
                        new window.ResponseTreeView({ collection: app.Responses });
                    }else if(xhr.type.indexOf("AudioObject") > -1) {
                        new window.ResponseDiscussionListView({ collection: app.Responses, users: app.InquiryUsers });
                    }else{
                        new window.ResponseListView({ collection: app.Responses, users: app.InquiryUsers });
                    }

                    app.Responses.id = window.Run.global_identifier;
                    app.Responses.itemId = xhr.id;
                    app.Responses.fetch({
                        beforeSend: setHeader
                    });

                    ///////////////////////////////
                    // Listener to submit responses
                    ///////////////////////////////
                    $(".close-detail, .cancel").click(function(e){
                        //$(".phase-master").animate({
                        //    'marginLeft' : '+0%'
                        //});

                        $(".phase-master").animate({
                            'marginLeft' : '+0%',
                            'width': '100%',
                            'top': '0px'
                        });
                        $(".phase-master").find(".box-tools.pull-right").show();
                        $(".phase-master").find(".skill-tree-row.row-1").show();


                        //console.debug(xhr,"saving or closing...");

                        app.navigate('inquiry/'+xhr.gameId+'/phase/'+_phase);

                        $(".phase-detail").hide();
                        $(".phase-detail").remove();

                        e.preventDefault();
                    });

                    $('textarea').keyup(function(e){
                        //console.log(e);
                        if(e.keyCode == 51){
                            console.debug("[successActivityHandl]","Display other responses to link them.");
                            $(this).trigger('enter');
                        }
                    });

                    $(".save").click(function(){
                        $(".message-user").html("Saving..").show().delay(500).fadeOut();

                        if  ($('textarea.response').val() == ""){

                        }else{
                            var newResponse = new Response({ generalItemId: xhr.id, responseValue: $('textarea.response').val(), runId: window.Run.global_identifier, userEmail: 0 });
                            newResponse.save({}, {
                                beforeSend:setHeader,
                                success: function(r, new_response){
                                    app.Responses.add(new_response);
                                }
                            });
                        }
                    });

                    $(".save-close").click(function(){
                        $(".message-user").html("Saving..").show().delay(500).fadeOut();

                        if  ($('textarea.response').val() == ""){
                            //$(".phase-master").animate({
                            //    'marginLeft' : '+0%'
                            //});

                            $(".phase-master").animate({
                                'marginLeft' : '+0%',
                                'width': '100%',
                                'top': '0px'
                            });
                            $(".phase-master").find(".box-tools.pull-right").show();
                            $(".phase-master").find(".skill-tree-row.row-1").show();


                            //console.debug(xhr,"saving or closing...");

                            app.navigate('inquiry/'+xhr.gameId+'/phase/1');

                            $(".phase-detail").hide();
                            $(".phase-detail").remove();
                        }else{
                            var newResponse = new Response({ generalItemId: xhr.id, responseValue: $('textarea.response').val(), runId: window.Run.global_identifier, userEmail: 0 });
                            newResponse.save({}, {
                                beforeSend:setHeader,
                                success: function(r, s){

                                    //
                                    //$(".phase-master").animate({
                                    //    'marginLeft' : '+0%',
                                    //    'width': '100%',
                                    //    'top': '0px'
                                    //});
                                    //$(".phase-master").find(".box-tools.pull-right").show();
                                    //
                                    //console.debug(s, xhr,"saving or closing...");
                                    //
                                    //app.navigate('inquiry/'+xhr.gameId+'/phase/1');
                                    //
                                    //$(".phase-detail").hide();
                                    //$(".phase-detail").remove();


                                    e.preventDefault();
                                }
                            });

                            $(".phase-master").animate({
                                'marginLeft' : '+0%',
                                'width': '100%',
                                'top': '0px'
                            });
                            $(".phase-master").find(".box-tools.pull-right").show();
                            $(".phase-master").find(".skill-tree-row.row-1").show();


                            //console.debug(xhr,"saving or closing...");

                            app.navigate('inquiry/'+xhr.gameId+'/phase/1');

                            $(".phase-detail").hide();
                            $(".phase-detail").remove();
                        }
                    });

                    $('input.response').keypress(function (e) {
                        var key = e.which;
                        if(key == 13){
                            if  ($('input.response').val() == ""){

                            }else{
                                var newResponse = new Response({ generalItemId: xhr.id, responseValue: $('input.response').val(), runId: window.Run.global_identifier, userEmail: 0 });
                                newResponse.save({}, {
                                    beforeSend:setHeader,
                                    success: function(r, new_response){
                                        app.Responses.add(new_response);
                                    }
                                });
                            }
                            $('input.response').val("");
                        }
                    });

                    $("button.new-todo-task").click(function(e){
                        $("div.new-todo-task").removeClass("hide");
                    });

                    $('input.new-todo-tasks-value').keypress(function (e) {
                        var key = e.which;
                        if(key == 13){
                            if  ($('input.new-todo-tasks-value').val() == ""){

                            }else{
                                var newResponse = new Response({ generalItemId: xhr.id, responseValue: $('input.new-todo-tasks-value').val(), runId: window.Run.global_identifier, userEmail: 0 });
                                newResponse.save({}, {
                                    beforeSend:setHeader,
                                    success: function(r, new_response){
                                        app.Responses.add(new_response);
                                    }
                                });
                            }

                            $('input.new-todo-tasks-value').val("");
                            $("div.new-todo-task").addClass("hide");
                        }
                    });

                    $(".phase-detail").show();
                }
            });

            if($.cookie("dojoibl.run")){
                window.Run.global_identifier = $.cookie("dojoibl.run");
            }
        }


    },
    initializeChannelAPI: function(){
        console.debug("[initializeChannelAPI]", "Initializing the chat");
        //this.loadChat();

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
                    if(app.MessagesList.where({ id: a.messageId }).length == 0){
                        console.log("dentro");
                        var message = new Message({ id: a.messageId });
                        message.fetch({
                            beforeSend: setHeader
                        });
                        app.MessagesList.add(message);
                    }
                    console.log(app.MessagesList.get(a.messageId));
                    console.log(app.MessagesList);



                    console.log("Controling type of notification", a, a.type);

                    if(a.type == "org.celstec.arlearn2.beans.notification.MessageNotification") {
                        if ($('.direct-chat-messages').length){
                            console.log(a);
                            var aux = a.messageId;
                                $('.direct-chat-messages').append(new MessageOwnView({ model: a }).render().el);
                                //if (a.senderId == app.UserList.at(0).toJSON().localId){
                                //    $('.direct-chat-messages').append(new MessageLeftView({ model: a }).render().el);
                                //}else{
                                //    $('.direct-chat-messages').append(new MessageRightView({ model: a }).render().el);
                                //}

                            $('.direct-chat-messages').animate({
                                    scrollTop: $('.direct-chat-messages')[0].scrollHeight
                                }, 200);
                        }else{
                            ///////////////////////////////////////////////////////////////////////////////////////
                            // TODO We would need to check here if I have other windows open not nofifying myself again.
                            // TODO Changes in one collection modifies a view: http://stackoverflow.com/questions/10341141/backbone-multiple-views-subscribing-to-a-single-collections-event-is-this-a-b
                            ///////////////////////////////////////////////////////////////////////////////////////
                            $('.messages-menu > ul').append(new MessageNotificationView({ model: a }).render().el);

                            var new_floating_notification = new GeneralFloatingNotificationView({ model: a }).render().el;

                            var message_notifications = $('.messages-menu span.label-success').html();

                            if(message_notifications != "" && message_notifications > 0){
                                message_notifications = parseInt(message_notifications) + 1;
                                $('.messages-menu span.label-success').html(message_notifications);
                            }else{
                                $('.messages-menu span.label-success').html(1);
                            }

                            /////////////////////////
                            // Floating notifications
                            /////////////////////////
                            var top = $(new_floating_notification).position().top;

                            var max_top = 64;

                            var pile_notifications = $('body').find($(".ui-pnotify.stack-topleft")).length;

                            max_top = max_top + 100 * pile_notifications;

                            console.log("Adding notification: Pixels:",max_top, "Bubbles:"+pile_notifications);

                            $(new_floating_notification).css({top: max_top+'px'});

                            $('body').append(new_floating_notification);

                            setTimeout(showNotifications, 5000);
                            function showNotifications(){
                                $(new_floating_notification).remove();

                                $.each( $(".ui-pnotify.stack-topleft"), function( key, value ) {
                                    var top = $(this).position().top;

                                    if(top == "64"){
                                        $(this).remove();
                                    }

                                    var descrease_top = top - 100 * pile_notifications;
                                    //max_top = max_top - 100;

                                    console.log("Removing notification: Pixels:",max_top, "Bubbles:"+pile_notifications, "Decrementar pixels:"+descrease_top);


                                    $(this).css({top: descrease_top+'px'});
                                });
                            }

                        }
                    }

                    if(a.type == "org.celstec.arlearn2.beans.run.Response"){
                        /////////////////////////
                        // Sidebar notifications
                        /////////////////////////
                        $("ul.notifications-side-bar").prepend(new NotificationSideBarView({ model: a }).render().el)
                    }
                };

                socket.onerror = function() { $('#messages').append('<p>Connection Error!</p>'); };
                socket.onclose = function() { $('#messages').append('<p>Connection Closed!</p>'); };
            }
        });

    },
    loadChat: function(id){
        console.debug("[loadChat]", "Loading the chat content");
        this.MessagesList = new MessageCollection();
        this.MessagesList.id = id;
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
                        $('.direct-chat-messages').append(new MessageLeftView({ model: message }).render().el);
                    }else{
                        $('.direct-chat-messages').append(new MessageRightView({ model: message }).render().el);
                    }

                    $('.direct-chat-messages').animate({
                        scrollTop: $('.direct-chat-messages')[0].scrollHeight
                    }, 0);
                });
            }
        });

        //$('.direct-chat-messages').animate({
        //    scrollTop: $('.direct-chat-messages')[0].scrollHeight
        //}, 2000);
    },
    common: function(callback) {

        console.debug("[common]", "Checking user...");

        if (this.UserList) {
            if (callback)
                callback();
        } else {

            this.UserList = new UserCollection();
            this.UserList.fetch({
                beforeSend: setHeader,
                success: function(response, xhr) {

                    //console.log(xhr);

                    if(xhr.errorCode == 2){
                        window.location = "https://wespot-arlearn.appspot.com/Login.html?client_id=wespotClientId&redirect_uri=http://dojo-ibl.appspot.com/oauth/wespot&response_type=code&scope=profile+email";
                    }else{
                        //console.log(location.href.split("code=")[1]);
                        //v.ar accessToken = location.href.split("code=")[1];
                        //document.cookie="accessToken="+accessToken+"; expires=Thu, 18 Dec 2013 12:00:00 UTC";
                        $(".m-r-sm.text-muted.welcome-message").html("Welcome "+xhr.givenName+" "+xhr.familyName);
                        $('ul.nav.metismenu > li:eq(0)').html( new UserView({ model: xhr }).render().el );
                        //$( new UserSidebarView({ model: xhr }).render().el).insertBefore("form.sidebar-form");
                        if (callback)
                            callback();
                    }
                }
            });
        }
    },
    loadPhaseActivities: function(id, phase){
        var _phase = phase;
        $("aside#summary > div").switchClass( "col-md-9", "col-md-2", 500, function(){
            this.ActivityList = new ActivitiesCollection({ });
            this.ActivityList
            this.ActivityList.id = id;

            this.ActivityList.fetch({
                beforeSend: setHeader,
                success: function (response, results){
                    $('#inquiry').html( new PhaseView({ }).render().el );
                    $("#inquiry").show();

                    $("#circlemenu").children().hide();
                    $("#circlemenu li:nth-child("+phase+")").show();
                    $("ul#circlemenu").attr("id", "circlemenu2");

                    $("#summary .title-summary").show();
                    $("#summary ul.nav-tabs.box-header").hide();

                    $("#inquiry-explanation").hide();

                    $("ul.box-header.with-border.nav.nav-tabs > li").fadeOut(100);

                    _.each(results.generalItems, function(generalItem){
                        //console.log(generalItem);
                        ///////////////////////////////////////////////////////////////////////////////////////
                        // TODO Re-think how to display the activities
                        ///////////////////////////////////////////////////////////////////////////////////////
                        //if(typeof(generalItem.dependsOn) != "undefined" && generalItem.dependsOn !== null) {
                        //console.log("yes depen");
                        //$('section.phase-master > .box-body > ul').append(new ActivityDepencyView({ model: generalItem }).render().el);
                        //}else{
                        //    console.log("no depen");
                        $('section.phase-master > .box-body > ul').append(new ActivityBulletView({ model: generalItem, phase: _phase }).render().el);
                        //}
                    });
                }
            });
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
    }
});

var successInquiryUsers = function(response, xhr){
    $('span.label.label-success.number-participants').html((xhr.users.length == 1) ? xhr.users.length+" user" :xhr.users.length+" users" );
    _.each(xhr.users, function(participant){
        $('ul.users-list').append( new UsersInquiryView({ model: participant}).render().el );
    });
};

var successGameHandler = function(response, xhr){

    _.each(xhr.gamesAccess, function(e){
        if(app.GameList.where({ id: e.gameId}).length == 0){
            var game = new Game({ id: e.gameId });
            game.fetch({
                beforeSend: setHeader,
                success: function (response, game) {
                    $('.content').append( new GameListView({ model: game, v: 1 }).render().el );

                }
            });
            app.GameList.add(game);
        }
    });
};

var successGameParticipateHandler = function(response, xhr){
    _.each(xhr.games, function(game){
        if(app.GameList.where({ id: game.gameId}).length == 0){
            app.GameList.add(game);
            $('.content').append( new GameListView({ model: game, v: 2 }).render().el );
        }
    });
};

tpl.loadTemplates(['main', 'game','game_teacher', 'inquiry', 'run', 'user', 'user_sidebar', 'phase', 'activity', 'activity_detail','activity_details', 'inquiry_structure',
    'inquiry_sidebar', 'activityDependency', 'message', 'message_right', 'inquiry_left_sidebar','message_own', 'response', 'response_discussion', 'response_treeview','response_author', 'response_discussion_author',
    'message_notification','notification_floating', 'activity_video', 'activity_widget', 'activity_discussion', 'notification_sidebar', 'user_inquiry','activity_tree_view'], function() {
    app = new AppRouter();
    Backbone.history.start();
});
