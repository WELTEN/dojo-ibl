// Game
//window.GameListView = Backbone.View.extend({
//    tagName:  "div",
//    className: "col-lg-3 game animated fadeInUp",
//    initialize:function (options) {
//        //this.collection.bind('add', this.render);
//        this.listenTo(app.GameList, 'add', this.show);
//        if(options.v == 1){
//            this.template = _.template(tpl.get('game_teacher'));
//        }
//
//        if(options.v == 2){
//            this.template = _.template(tpl.get('game'));
//        }
//    },
//    events: {
//        'click .show-runs' : 'showRuns',
//        'click .delete-inquiry': 'deleteInquiry'
//    },
//    show: function(){
//    },
//    render: function () {
//        $(this.el).html(this.template({
//            model: this.model,
//            time: new Date(this.model.lastModificationDate).toLocaleDateString(),
//            timeago: jQuery.timeago(new Date(this.model.lastModificationDate).toISOString()),
//            //description: jQuery.trim(this.model.description).substring(0, 50).split(" ").slice(0, -1).join(" ") + "..."
//            description: jQuery.trim(this.model.description)
//        }));
//
//        //this.el.$(".back").hide();
//
//        //$(".col-lg-3.animated.fadeInUp").flip();
//        return this;
//    },
//    showRuns: function(e){
//        e.preventDefault();
//
//        var _aux = $(this.el).find(".widget-text-box > tbody");
//
//        $(this.el).find(".front").toggle();
//        $(this.el).find(".back").toggle();
//
//        this.RunAccessList = new RunByGameCollection({ id: this.model.gameId });
//        this.RunAccessList.fetch({
//            beforeSend: setHeader,
//            success: function(response, xhr) {
//                if(xhr.runs.length == 0){
//                    console.error("The game does not have runs. There have been a problem during the creation of the inquiry.")
//                }else if(xhr.runs.length == 1){
//                    _.each(xhr.runs, function(run){
//                        $(_aux).html(new this.RunListView({ model: run }).render().el);
//                    });
//                }else{
//                    _.each(xhr.runs, function(run){
//                        $(_aux).html(new this.RunListView({ model: run }).render().el);
//                    });
//                }
//            }
//        });
//    },
//    deleteInquiry: function(e){
//        e.preventDefault();
//
//        swal({
//            title: "Are you sure?",
//            text: "You will not be able to recover this inquiry!",
//            type: "warning",
//            showCancelButton: true,
//            confirmButtonColor: "#DD6B55",
//            confirmButtonText: "Yes, delete it!",
//            closeOnConfirm: false
//        }, function () {
//            swal("Deleted!", "Your inquiry has been deleted.", "success");
//            //////////////
//            // Delete game
//            //////////////
//            var deleteGame = new GameDelete({ id: $(e.currentTarget).attr('data') });
//            deleteGame.gameId = $(e.currentTarget).attr('data');
//            deleteGame.destroy({
//                beforeSend:setHeader,
//                type: 'DELETE',
//                success: function(r, new_response){
//                    console.log(r, new_response);
//                }
//            });
//
//            $(e.currentTarget).closest('.col-lg-3.game.animated.fadeInUp').hide();
//        });
//    }
//});

window.GameListView = Backbone.View.extend({
    initialize:function (options) {
        _(this).bindAll('render');
        _(this).bindAll('add');
        this.collection.bind('add', this.add);
    },
    events: {
        'click .show-runs' : 'showRuns',
        'click .delete-inquiry': 'deleteInquiry'
    },
    add: function(game){
        $(this.el).append(new GameListItemView({ model: game.toJSON() }).render().el);
    },
    render: function () {
        _.each(this.collection.models, function(game){
            $(this.el).append(new GameListItemView({ model: game.toJSON() }).render().el);
        }, this);
        return this;
    }
});

window.GameListItemView = Backbone.View.extend({
    tagName:  "div",
    className: "col-lg-3 game animated fadeIn",
    initialize:function (options) {
        ////this.collection.bind('add', this.render);
        //this.listenTo(app.GameList, 'add', this.show);
        //if(options.v == 1){
        //    this.template = _.template(tpl.get('game_teacher'));
        //}
        //
        //if(options.v == 2){
            this.template = _.template(tpl.get('game'));
        //}
    },
    events: {
        'click .show-runs' : 'showRuns',
        'click .delete-inquiry': 'deleteInquiry'
    },
    show: function(){
    },
    render: function () {

        var desc = "Demo description";

        if(this.model.description.length > 150){
            desc = jQuery.trim(this.model.description.substring(0, 150) + '...')
        }else{
            desc = jQuery.trim(this.model.description)
        }

        $(this.el).html(this.template({
            model: this.model,
            time: new Date(this.model.lastModificationDate).toLocaleDateString(),
            timeago: new Date(this.model.lastModificationDate),
            description: desc
        }));

        return this;
    },
    showRuns: function(e){
        e.preventDefault();

        var _aux = $(this.el).find(".widget-text-box");

        $(this.el).find(".front").toggle();
        $(this.el).find(".back").toggle();

        //$(_aux).append(new RunListView({ collection: app.RunList }).render().el);
        //
        //if(app.RunList.length == 0) {
        //    app.RunAccessList.id = this.model.gameId;
        //    app.RunAccessList.fetch({
        //        beforeSend: setHeader,
        //        success: function (e, response) {
        //            //console.log(response);
        //            _.each(response.runs, function (run) {
        //                console.log(run);
        //                app.RunList.add(run);
        //            });
        //        }
        //    });
        //}

        this.RunAccessList = new RunByGameCollection({ id: this.model.gameId });
        this.RunAccessList.fetch({
            beforeSend: setHeader,
            success: function(response, xhr) {
                if(xhr.runs.length == 0){
                    console.error("The game does not have runs. There have been a problem during the creation of the inquiry.")
                }else if(xhr.runs.length == 1){
                    _.each(xhr.runs, function(run){
                        $(_aux).html(new this.RunListItemView({ model: run }).render().el);
                    });
                }else{
                    _.each(xhr.runs, function(run){
                        $(_aux).html(new this.RunListItemView({ model: run }).render().el);
                    });
                }
            }
        });
    },
    deleteInquiry: function(e){
        e.preventDefault();

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
            //////////////
            // Delete game
            //////////////
            var deleteGame = new GameDelete({ id: $(e.currentTarget).attr('data') });
            deleteGame.gameId = $(e.currentTarget).attr('data');
            deleteGame.destroy({
                beforeSend:setHeader,
                type: 'DELETE',
                success: function(r, new_response){
                    console.log(r, new_response);
                }
            });

            $(e.currentTarget).closest('.col-lg-3.game.animated.fadeIn').hide();
        });
    }
});

// Run
window.RunListView = Backbone.View.extend({
    tagName:  "tbody",
    initialize:function () {
        _(this).bindAll('render');
        _(this).bindAll('add');
        this.collection.bind('add', this.add);
    },
    add: function(run){
        console.log("add");
        $(this.el).append(new RunListItemView({ model: run.toJSON() }).render().el);
    },
    render: function () {

        //console.log("render");

        _.each(this.collection.models, function(run){
            console.log(run.toJSON());
            //$(this.el).append(new RunListItemView({ model: run.toJSON() }).render().el);
        }, this);
        return this;
    }
});
window.RunListItemView = Backbone.View.extend({
    tagName:  "tr",
    initialize:function () {
        this.template = _.template(tpl.get('run'));
    },
    render:function () {
        console.log(this.model);
        $(this.el).html(this.template(this.model));
        return this;
    }
});


// Run
window.ProfileView = Backbone.View.extend({
    tagName:  "div",
    className: "wrapper wrapper-content animated fadeInRight profile-personal",
    initialize:function () {
        this.template = _.template(tpl.get('profile'));
    },
    render:function () {
        $(this.el).html(this.template(this.model));
        return this;
    }
});

// Inquiries
window.InquiryView = Backbone.View.extend({
    tagName:  "div",
    id: "inquiry-content",
    className: "col-md-9 wrapper wrapper-content animated fadeIn",
    initialize:function (options) {
        this.template = _.template(tpl.get('inquiry'));
        //console.log(options.model);
        this.options = options;
    },
    render:function () {

        $(this.el).html(this.template(this.model));

        var container = $(this.el).find('#circlemenu');

        //console.log(this.model);

        var gameId = this.model.game.gameId;

        this.model.game.phases.forEach(function(phase){
            container.append('<li><input type="text" class="knob" data-readonly="true" value="0" data-width="50" data-height="50" data-fgcolor="#39CCCC" /><div class="knob-label"><a href="#inquiry/'+gameId+'/phase/'+phase.phaseId+'">'+phase.title+'</a></div></li>');
        });

        var radius = 170;
        var fields = $(this.el).find('#circlemenu li'),
            container = $(this.el).find('#circlemenu'),
            width = container.width(),
            height = container.height(),
            angle = 300,
            step = (2*Math.PI) / fields.length;

        //console.log(fields, container);

        fields.each(function() {

            //console.log(width, $(this).width());

            var x = Math.round(width/2 + radius * Math.cos(angle) - $(this).width()/2);
            var y = Math.round(height/2 + radius * Math.sin(angle) - $(this).height()/2);
            //console.log(x,y);
            if(window.console) {
                //console.log($(this).text(), x, y);
            }
            $(this).css({
                left: x + 'px',
                top: y + 'px'
            });
            angle += step;
        });

        return this;
    }
});

window.InquiryNewView = Backbone.View.extend({
    tagName:  "div",
    className: "col-md-12 wrapper wrapper-content animated fadeIn",
    initialize:function () {
        this.template = _.template(tpl.get('new_inquiry'));
    },
    events: {
        "click .drag.external-event.navy-bg.remove": "loadActivity",
        "click .add-new-role": "createNewRole"
    },
    loadActivity: function(e) {

        $('.selected').removeClass('selected');
        $(e.toElement).addClass('selected');

        $(".activities-form .spiner-example").show(1000);

        var _gameId = 0;

        if($.cookie("dojoibl.game")){
            _gameId = $.cookie("dojoibl.game");
        }

        $(".activities-form").show();

        var act = new ActivityCollection({ });
        act.id = $(e.toElement).attr('id');
        act.gameId = _gameId;
        act.fetch({
            beforeSend: setHeader,
            success: function (response, xhr) {

                setTimeout(function(){
                    $(".activities-form .spiner-example").hide(1000);
                }, 2000);

                $.each(xhr, function(name, value){
                    if( name != 'videoFeed' && name != 'audioFeed' ){
                        $('[name="audioFeed"]').prev(".hr-line-dashed").hide();
                        $('[name="audioFeed"]').closest('.form-group').hide();
                        $('[name="resource"]').prev(".hr-line-dashed").hide();
                        $('[name="resource"]').closest('.form-group').hide();
                        $('[name="videoFeed"]').prev(".hr-line-dashed").hide();
                        $('[name="videoFeed"]').closest('.form-group').hide();
                    }else{
                        $('[name="audioFeed"]').prev(".hr-line-dashed").show();
                        $('[name="audioFeed"]').closest('.form-group').show();
                        $('[name="resource"]').prev(".hr-line-dashed").show();
                        $('[name="resource"]').closest('.form-group').show();
                        $('[name="videoFeed"]').prev(".hr-line-dashed").show();
                        $('[name="videoFeed"]').closest('.form-group').show();
                    }

                    if(name == 'videoFeed'){
                        $('[name="audioFeed"]').attr('name','videoFeed');
                        $('[name="resource"]').attr('name','videoFeed');
                    }

                    if(name == 'audioFeed'){
                        $('[name="videoFeed"]').attr('name','audioFeed');
                        $('[name="resource"]').attr('name','audioFeed');
                    }

                    var $el = $('[name="'+name+'"]'),
                        type = $el.attr('type');

                    switch(type){
                        case 'checkbox':
                            $el.attr('checked', 'checked');
                            break;
                        case 'radio':
                            $el.filter('[value="'+value+'"]').attr('checked', 'checked');
                            break;
                        default:
                            $el.val(value);
                    }

                });
            }
        });
    },
    render:function () {
        $(this.el).html(this.template(this.model));

        return this;
    },
    addPluginChosen: function(){
        var config = {
            '.chosen-select'           : {},
            '.chosen-select-deselect'  : {allow_single_deselect:true},
            '.chosen-select-no-single' : {disable_search_threshold:10},
            '.chosen-select-no-results': {no_results_text:'Oops, nothing found!'},
            '.chosen-select-width'     : {width:"95%"}
        }
        for (var selector in config) {
            $(selector).chosen(config[selector]);
        }
    },
    createNewRole: function(e){
        e.preventDefault();
        console.log("click");

        swal({
            title: "Add a new role",
            text: "Give a descriptive name for the new role",
            type: "input",
            showCancelButton: true,
            closeOnConfirm: false,
            animation: "slide-from-top",
            inputPlaceholder: "Example: Explainer"
        }, function (inputValue) {
            if (inputValue === false)
                return false;
            if (inputValue === "") {
                swal.showInputError("You need to write something!");
                return false
            }
            swal("Nice!", "A new role: " + inputValue + " has been created", "success");

            var new_role = new AddRole("hol");
            new_role.gameId = $.cookie("dojoibl.game");
            new_role.save({}, {
                beforeSend: setHeader,
                success: function (r, response) {

                }
            });
        });

    }
});

window.NewInquiryCode = Backbone.View.extend({
    tagName:  "div",
    className: "article col-md-6 col-md-offset-3 animated fadeIn",
    initialize:function (options) {
        this.code = options.code;
        this.template = _.template(tpl.get('new_inquiry_code'));

    },
    render:function () {
        $(this.el).html(this.template({ code: this.code }));

        return this;
    }
});

window.NewActivityNewInquiryView = Backbone.View.extend({
    tagName:  "tr",
    initialize:function () {
        this.template = _.template(tpl.get('new_activity_new_inquiry'));
    },
    events: {
        "click .drag.external-event.navy-bg.remove": "loadActivity",
        "change .type-activity": "changeSelect"
    },
    loadActivity: function() {
        $('.selected.remove').removeClass('select');
    },
    changeSelect: function() {
        this.$(".type-activity option:selected").each(function() {
            if($( this ).val() == "org.celstec.arlearn2.beans.generalItem.VideoObject"){
                console.log("a");
                $(this).closest("tr").find(".feed-activity").attr('type', 'file');
                $(this).closest("tr").find(".feed-activity").prop('disabled', false);
            }else if( $( this ).val() == "org.celstec.arlearn2.beans.generalItem.OpenBadge" ||
                $( this ).val() == "org.celstec.arlearn2.beans.generalItem.AudioObject" ){
                console.log("b");
                $(this).closest("tr").find(".feed-activity").attr('type', 'text');
                $(this).closest("tr").find(".feed-activity").prop('disabled', false);
            }else {
                console.log("c");
                $(this).closest("tr").find(".feed-activity").attr('type', 'file');
                $(this).closest("tr").find(".feed-activity").prop('disabled', true);
            }
        });
    },
    render:function () {
        $(this.el).html(this.template());

        return this;
    }
});

window.NewPhaseNewInquiryView = Backbone.View.extend({
    tagName: "div",
    className: "tab-pane",
    initialize:function () {
        this.template = _.template(tpl.get('new_phase_new_inquiry'));

    },
    events: {
        //"click .remove-phase": "removePhase",
        "focusout .activities-form form": "saveActivity"
    },
    saveActivity: function(e) {

        var frm = $(e.currentTarget).closest('form');

        var data = app.getDataForm(frm);

        console.log(data);

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
                console.info("Activity saved");
            },
            error: function(){
                console.error("Activity couldn't be saved due to en error");
            }
        });
    },
    //removePhase: function(){
    //    console.log("remove");
    //},
    render:function () {
        $(this.el).html(this.template());

        $( "#drag-list" ).sortable({
            connectWith: "div",
            remove: function(event, ui) {
                var _gameId = 0;

                //console.log(event, ui);

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
                        name: "",
                        description: "",
                        autoLaunch: false,
                        fileReferences: [],
                        sortKey: 1,
                        videoFeed: feed,
                        richText: ""
                    };
                }else if(type == "org.celstec.arlearn2.beans.generalItem.AudioObject") {
                    var data = {
                        type: type,
                        section: phase,
                        gameId: _gameId,
                        deleted: true,
                        name: "",
                        description: "",
                        autoLaunch: false,
                        fileReferences: [],
                        sortKey: 1,
                        audioFeed: feed,
                        richText: ""
                    };
                }else{
                    var data = {
                        type: type,
                        section: phase,
                        gameId: _gameId,
                        deleted: true,
                        name: "",
                        description: "",
                        autoLaunch: false,
                        fileReferences: [],
                        sortKey: 1,
                        richText: ""
                    };
                }

                //console.log(window);

                /////////////////////////////////////
                // Create the activity = General Item
                /////////////////////////////////////
                var newActivity = new Activity(data);
                newActivity.save({}, {
                    beforeSend:setHeader,
                    success: function(r, new_response){
                        $(_aux).attr("id", new_response.id);
                        //app.ActivityList.add(new_response);
                    },
                    error: function(e){
                        console.log(e);
                    }
                });

                var phases = [];

                $("ul.select-activities").children("li:not(.new-phase)").each(function () {
                    var item = {}
                    item ["title"] = $(this).find("a").text();
                    item ["phaseId"] = $(this).find("a").attr("href").substring(5,6);
                    item ["type"] = "org.celstec.arlearn2.beans.game.Phase";
                    phases.push(item);
                });

                ////////////////////////
                // Update phases in Game
                ////////////////////////
                var updateGame = new Game({ gameId: _gameId, phases: phases, title: $("#inquiry-title-value").val(), description: $("#inquiry-description-value").val()  });
                updateGame.save({},{ beforeSend:setHeader });
            }
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
                        console.warn("still need to remove responses from run");
                    }
                });
            },
            hoverClass: "remove-drag-hover",
            accept: '.remove'
        });

        $(this.el).find('.summernote').summernote();

        return this;
    }
});

window.SideBarView = Backbone.View.extend({
    tagName:  "div",
    className: "col-lg-3 wrapper wrapper-content small-chat-float",
    initialize:function () {
        _.bindAll(this, 'cleanup');
        this.$el.on('destroyed', this.cleanup);
        this.template = _.template(tpl.get('inquiry_sidebar'));
    },
    render:function () {
        $(this.el).html(this.template());

        $(window).scroll(function(){
            if ($(this).scrollTop() > 153 && $(this).width() > 1200 ) {
                $(".ibox-chat-content").addClass('fixed-chat');
            } else {
                $(".ibox-chat-content").removeClass('fixed-chat');
            }
        });



        return this;
    },
    events: {
        "click #send-message": "clickMessage",
        "keypress #add-new-message": "pressKeyMessage"
    },
    clickMessage: function() {
        var newMessage = new Message({ runId: $.cookie("dojoibl.run"), threadId: 0, subject: "", body: $('input#add-new-message').val() });

        newMessage.save({}, {
            beforeSend:setHeader,
            success: function(response, message){
                app.MessagesList.add(message)
            }
        });
        $('input#add-new-message').val('');
    },
    pressKeyMessage: function(e){
        //console.log("pressKeyMessage called");
        var key = e.which;
        if(key == 13){
            console.log($(this));
            console.log("message send");
            var newMessage = new Message({ runId: $.cookie("dojoibl.run"), threadId: 0, subject: "", body: $('input#add-new-message').val() });

            newMessage.save({}, {
                beforeSend:setHeader,
                success: function(response, message){
                    app.MessagesList.add(message)
                }
            });
            $('input#add-new-message').val('');
        }
    },
    removeHandler: function(){
        // Your processing code here
        this.cleanup();
    },
    cleanup: function() {
        this.undelegateEvents();
        $(this.el).empty();
    }
});

// Phases
window.PhaseView = Backbone.View.extend({
    className: "animated fadeIn",
    initialize:function () {
        this.template = _.template(tpl.get('phase'));
    },
    render:function () {
        $(this.el).html(this.template());

        return this;
    }
});

// Activities
window.ActivityBulletView = Backbone.View.extend({
    tagName: "li",
    initialize:function (options) {
        this.phase = options.phase;
        this.template = _.template(tpl.get('activity'));
        $(this.el).attr("data", this.model.id);
    },
    render:function () {

        var icon;

        switch(this.model.type){
            case "org.celstec.arlearn2.beans.generalItem.AudioObject":
                icon = '<i class="fa fa-external-link"></i> ';
                break;
            case "org.celstec.arlearn2.beans.generalItem.NarratorItem":
                icon = '<i class="fa fa-file-text"></i> ';
                break;
            case "org.celstec.arlearn2.beans.generalItem.SingleChoiceImageTest":
                icon = '<i class="fa fa-sitemap"></i> ';
                break;
            case "org.celstec.arlearn2.beans.generalItem.VideoObject":
                icon = '<i class="fa fa-file-movie-o"></i> ';
                break;
            case "org.celstec.arlearn2.beans.generalItem.OpenBadge":
                icon = '<i class="fa fa-link"></i> ';
                break;
            case "org.celstec.arlearn2.beans.generalItem.ScanTag":
                icon = '<i class="fa fa-file-archive"></i> ';
                break;
            case "org.celstec.arlearn2.beans.generalItem.ResearchQuestion":
                icon = '<i class="fa fa-question"></i> ';
                break;
        }

        $(this.el).html(this.template({ model: this.model, phase: this.phase, icon: icon }));
        return this;
    }
});

window.ItemBreadcrumbView = Backbone.View.extend({
    tagName: "div",
    className: "move-right-breadcrumb",
    initialize:function () {
        this.template = _.template(tpl.get('item_breadcrumb_activity'));
    },
    render:function () {
        $(this.el).html(this.template({ }));
        return this;
    }
});

// Responses
window.ResponseListView = Backbone.View.extend({
    className: "tabs-container",
    tagName: "div",
    initialize: function(options){

        this.users = options.users;
        this.game = options.game;
        this.runId = options.run;

        console.log(options);

        this.template = _.template(tpl.get('activity_text'));
        this.emptyTemplate = _.template('<div class="alert alert-warning">' +
        'No comments here yet. <a class="alert-link" href="#"> Hurry up!</a> comment and be the first one!.' +
        '</div>');
        this.collection.on('add', this.addOne, this);
        this.collection.on('reset', this.addAll, this);

        this.childViews = [];
    },
    addAll: function(){
        if (this.collection.length == 0){
            this.$el.find('#list_answers').append(this.emptyTemplate);
        }else{
            this.collection.forEach(this.addOne, this);
        }

        $('.reply').click(function(e){
            console.log(e);
            e.preventDefault();
            //////////////////////////////////////////////////////////////////
            // Problem here is that we have more .responses now under one div
            // todo make it better
            /////////////////////////////////////////////////////////////////
            if($(this).parent().parent().find(".response").length == 0){
                if($(this).attr("data")){
                    var form = new ResponseReplyView({}).render().el;

                    var gItem = $(this).attr("gitem");

                    $(form).find("button.save").attr("responseid", $(this).attr("data"));
                    $(form).find("textarea.response").attr("responseid", $(this).attr("data"));

                    $(this).parent().parent().append(form);

                    ////////////////////////////////////////////////////////////////////////////////////
                    // This event should be captured here in order to capture input for future responses
                    ////////////////////////////////////////////////////////////////////////////////////
                    $(".save[responseid='"+$(this).attr("data")+"']").click(function(){

                        console.log($(this).attr("responseid"), $("textarea[responseid='"+$(this).attr("responseid")+"']").val());

                        if  ($("textarea[responseid='"+$(this).attr("responseid")+"']").val() != ""){

                            var newResponse = new Response({ generalItemId: gItem, responseValue: $("textarea[responseid='"+$(this).attr("responseid")+"']").val(), runId: $.cookie("dojoibl.run"), userEmail: 0, parentId: $(this).attr("responseid"), lastModificationDate: Date.now() });
                            newResponse.save({}, {
                                beforeSend:setHeader,
                                success: function(r, new_response){
                                }
                            });
                        }
                        $("textarea[responseid='"+$(this).attr("responseid")+"']").val("");
                    });
                }else{
                    console.error("Id of the response is missing")
                }
            }else{
                //console.log("remove",$(this).parent().siblings(".social-comment:last"));
                $(this).parent().siblings(".social-comment:last").remove();
            }


        });
    },
    addOne: function(response){
        var res = response.toJSON();
        var aux = response.toJSON().userEmail.split(':');
        var user = this.users.where({ 'localId': aux[1] });

        if(this.model.id == res.generalItemId){
            if(res.parentId != 0){

                var childView = new ResponseView({ model: response.toJSON(), user: user[0] });
                this.childViews.push(childView);
                childView = childView.render().el;

                if($("textarea[responseid='"+res.parentId+"']").parent().parent().length == 0){
                    $("div[data-item='"+res.parentId+"']").parent().append(childView);
                }else{
                    $(childView).insertBefore($("textarea[responseid='"+res.parentId+"']").parent().parent());
                }
                this.childViews.push(childView);
            }else{
                var view = new ResponseView({ model: response.toJSON(), user: user[0] });
                this.childViews.push(view);
                view = view.render().el;
                this.$el.find('#list_answers').append(view);
            }
        }
    },
    //emptyCollection: function(){
    //    this.$el.html(this.template());
    //    return this;
    //},
    render: function(){

        this.$el.html(this.template(this.model));

        return this;
    },
    onClose: function(){
        this.remove();
        this.unbind();
        this.collection.unbind("add", this.addOne);

        // handle other unbinding needs, here
        _.each(this.childViews, function(childView){
            if (childView.close){
                childView.close();
            }
        })
    }
});

window.ResponseListQuestionsView = Backbone.View.extend({
    className: "tabs-container",
    tagName: "div",
    initialize: function(options){

        this.users = options.users;
        this.game = options.game;
        this.runId = options.run;

        this.template = _.template(tpl.get('activity_research_question'));

        this.collection.on('add', this.addOne, this);
        this.collection.on('reset', this.addAll, this);

        this.number = 0;
        this.childViews = [];

    },
    addAll: function(){
        this.collection.forEach(this.addOne, this);
        $('.reply').click(function(e){

            //////////////////////////////////////////////////////////////////
            // Problem here is that we have more .responses now under one div
            // todo make it better
            /////////////////////////////////////////////////////////////////
            if($(this).parent().parent().find(".response").length == 0){
                if($(this).attr("data")){
                    var form = new ResponseReplyView({}).render().el;

                    var gItem = $(this).attr("gitem");

                    $(form).find("button.save").attr("responseid", $(this).attr("data"));
                    $(form).find("textarea.response").attr("responseid", $(this).attr("data"));

                    $(this).parent().parent().append(form);

                    ////////////////////////////////////////////////////////////////////////////////////
                    // This event should be captured here in order to capture input for future responses
                    ////////////////////////////////////////////////////////////////////////////////////
                    $(".save[responseid='"+$(this).attr("data")+"']").click(function(){

                        //console.log($(this).attr("responseid"), $("textarea[responseid='"+$(this).attr("responseid")+"']").val());

                        if  ($("textarea[responseid='"+$(this).attr("responseid")+"']").val() != ""){

                            var newResponse = new Response({ generalItemId: gItem, responseValue: $("textarea[responseid='"+$(this).attr("responseid")+"']").val(), runId: $.cookie("dojoibl.run"), userEmail: 0, parentId: $(this).attr("responseid"), lastModificationDate: Date.now() });
                            newResponse.save({}, {
                                beforeSend:setHeader,
                                success: function(r, new_response){
                                }
                            });
                        }
                        $("textarea[responseid='"+$(this).attr("responseid")+"']").val("");
                    });
                }else{
                    console.error("Id of the response is missing")
                }
            }else{
                //console.log("remove",$(this).parent().siblings(".social-comment:last"));
                $(this).parent().siblings(".social-comment:last").remove();
            }

            e.preventDefault();
        });
    },
    addOne: function(response){
        var res = response.toJSON();
        var aux = response.toJSON().userEmail.split(':');
        var user = this.users.where({ 'localId': aux[1] });

        if(this.model.id == res.generalItemId){
            if(res.parentId != 0){
            var childView = new ResponseView({ model: response.toJSON(), user: user[0] });
            this.childViews.push(childView);
            childView.render();
            if($(".faq-item[data-item='"+res.parentId+"']").length == 0){
                $(childView.el).insertAfter("div[data-item='"+res.parentId+"']")
            }else{
                $(".faq-item[data-item='"+res.parentId+"'").find(".faq-answer").append(childView.el);
            }
        }else {
                var view = new ResponseQuestionView({model: response.toJSON(), user: user[0], number: this.number++});
                this.childViews.push(view);
                view = view.render().el;
                this.$el.find('#list_answers').prepend(view);

                var _id = response.toJSON().responseId;

                $(".save[responseid=" + _id + "]").click(function () {
                    if ($("textarea[responseid=" + _id + "]").val() != "") {
                        console.log(response.toJSON(), "sending response...." + $("textarea[responseid=" + _id + "]").val());

                        var newResponse = new Response({
                            generalItemId: response.toJSON().generalItemId,
                            responseValue: $("textarea[responseid=" + _id + "]").val(),
                            runId: $.cookie("dojoibl.run"),
                            userEmail: 0,
                            parentId: _id
                        });
                        newResponse.save({}, {
                            beforeSend: setHeader
                        });
                    }
                    $("textarea[responseid=" + _id + "]").val("");
                });
            }
        }
    },
    render: function(){

        this.$el.html(this.template(this.model));

        //$(".form-control.input-sm.pull-right").keyup(function () {
        //    //split the current value of searchInput
        //    var data = this.value.split(" ");
        //    //create a jquery object of the rows
        //    var jo = $("#activity-responses > tbody").find("tr");
        //    if (this.value == "") {
        //        jo.show();
        //        return;
        //    }
        //    //hide all the rows
        //    jo.hide();
        //
        //    //Recusively filter the jquery object to get results.
        //    jo.filter(function (i, v) {
        //        var $t = $(this);
        //        for (var d = 0; d < data.length; ++d) {
        //            if ($t.is(":contains('" + data[d] + "')")) {
        //                return true;
        //            }
        //        }
        //        return false;
        //    })
        //        //show the rows that match.
        //        .show();
        //}).focus(function ()
        //{
        //    this.value = "";
        //    $(this).css({
        //        "color": "black"
        //    });
        //    $(this).unbind('focus');
        //}).css({
        //    "color": "#C0C0C0"
        //});

        return this;
    },
    onClose: function(){
        this.remove();
        this.unbind();
        this.collection.unbind("add", this.addOne);

        // handle other unbinding needs, here
        _.each(this.childViews, function(childView){
            if (childView.close){
                childView.close();
            }
        })
    }
});

window.VideoActivityView = Backbone.View.extend({
    className: "tabs-container",
    tagName: "div",
    initialize: function(options){

        this.users = options.users;
        this.game = options.game;
        this.runId = options.run;

        this.template = _.template(tpl.get('activity_video'));

        this.collection.on('add', this.addOne, this);
        this.collection.on('reset', this.addAll, this);

        this.childViews = [];
    },
    addAll: function(){

        this.collection.forEach(this.addOne, this);
    },
    addOne: function(response){
        var res = response.toJSON();
        var aux = response.toJSON().userEmail.split(':');
        var user = this.users.where({ 'localId': aux[1] });


        if(res.parentId != 0){

            var childView = new ResponseView({ model: response.toJSON(), user: user[0] });
            this.childViews.push(childView);
            childView = childView.render().el;

            if($("textarea[responseid='"+res.parentId+"']").parent().parent().length == 0){
                $("div[data-item='"+res.parentId+"']").parent().append(childView);
            }else{
                $(childView).insertBefore($("textarea[responseid='"+res.parentId+"']").parent().parent());
            }
            this.childViews.push(childView);
        }else{
            var view = new ResponseView({ model: response.toJSON(), user: user[0] });
            this.childViews.push(view);
            view = view.render().el;
            this.$el.find('#list_answers').append(view);
        }
    },
    render: function(){

        this.$el.html(this.template(this.model));

        $('.reply').click(function(e){

            //console.log($(this).parent().parent().find(".response").length);

            //////////////////////////////////////////////////////////////////
            // Problem here is that we have more .responses now under one div
            // todo make it better
            /////////////////////////////////////////////////////////////////
            if($(this).parent().parent().find(".response").length == 0){
                if($(this).attr("data")){
                    var form = new ResponseReplyView({}).render().el;

                    var gItem = $(this).attr("gitem");

                    $(form).find("button.save").attr("responseid", $(this).attr("data"));
                    $(form).find("textarea.response").attr("responseid", $(this).attr("data"));

                    $(this).parent().parent().append(form);

                    ////////////////////////////////////////////////////////////////////////////////////
                    // This event should be captured here in order to capture input for future responses
                    ////////////////////////////////////////////////////////////////////////////////////
                    $(".save[responseid='"+$(this).attr("data")+"']").click(function(){

                        console.log($(this).attr("responseid"), $("textarea[responseid='"+$(this).attr("responseid")+"']").val());

                        if  ($("textarea[responseid='"+$(this).attr("responseid")+"']").val() != ""){

                            var newResponse = new Response({ generalItemId: gItem, responseValue: $("textarea[responseid='"+$(this).attr("responseid")+"']").val(), runId: $.cookie("dojoibl.run"), userEmail: 0, parentId: $(this).attr("responseid"), lastModificationDate: Date.now() });
                            newResponse.save({}, {
                                beforeSend:setHeader,
                                success: function(r, new_response){
                                }
                            });
                        }
                        $("textarea[responseid='"+$(this).attr("responseid")+"']").val("");
                    });
                }else{
                    console.error("Id of the response is missing")
                }
            }else{
                //console.log("remove",$(this).parent().siblings(".social-comment:last"));
                $(this).parent().siblings(".social-comment:last").remove();
            }

            e.preventDefault();
        });

        $(".form-control.input-sm.pull-right").keyup(function () {
            //split the current value of searchInput
            var data = this.value.split(" ");
            //create a jquery object of the rows
            var jo = $("#activity-responses > tbody").find("tr");
            if (this.value == "") {
                jo.show();
                return;
            }
            //hide all the rows
            jo.hide();

            //Recusively filter the jquery object to get results.
            jo.filter(function (i, v) {
                var $t = $(this);
                for (var d = 0; d < data.length; ++d) {
                    if ($t.is(":contains('" + data[d] + "')")) {
                        return true;
                    }
                }
                return false;
            })
                //show the rows that match.
                .show();
        }).focus(function ()
        {
            this.value = "";
            $(this).css({
                "color": "black"
            });
            $(this).unbind('focus');
        }).css({
            "color": "#C0C0C0"
        });

        return this;
    },
    onClose: function(){
        this.remove();
        this.unbind();
        this.collection.unbind("add", this.addOne);

        // handle other unbinding needs, here
        _.each(this.childViews, function(childView){
            if (childView.close){
                childView.close();
            }
        })
    }
});


////
//window.ResponseListQuestionsView = Backbone.View.extend({
//    initialize: function(options){
//        _(this).bindAll('render');
//        this.collection.bind('add', this.render);
//        this.users = options.users;
//        this.game = options.game;
//        this.runId = options.run;
//
//        //this.collection.on('add', function(){
//        //    console.log("add");
//        //});
//        //this.collection.on('reset', function(){
//        //    console.log("reset")
//        //});
//    },
//    render: function(){
//        var number = 0;
//
//        //var styles = {
//        //    visibility : "hidden",
//        //    display: "none"
//        //};
//        //
//        //$(".spiner-example, .sk-spinner .sk-spinner-chasing-dots").css(styles)
//
//        _.each(this.collection.models, function(response){
//            var res = response.toJSON();
//            var aux = response.toJSON().userEmail.split(':');
//            var user = this.users.where({ 'localId': aux[1] });
//
//            //console.log(res);
//
//            if(res.parentId != 0){
//                if($(".faq-answer div[data-item='"+res.parentId+"']").length == 0){
//                    $("div[data-item='"+res.parentId+"'] .faq-answer")
//                        .append(new ResponseView({ model: response.toJSON(), user: user[0] }).render().el);
//                }else{
//                    $("div[data-item='"+res.parentId+"']")
//                        .append(new ResponseView({ model: response.toJSON(), user: user[0] }).render().el);
//                }
//            }else{
//                // Questions that we want to show for now
//                var view = new ResponseQuestionView({ model: response.toJSON(), user: user[0], number: number++ }).render().el;
//                $('#list_answers').prepend(view)
//            }
//        }, this);
//
//        this.collection.reset();
//
//        $('.reply').click(function(e){
//
//            //console.log($(this).parent().parent().find(".response").length);
//
//            if($(this).parent().parent().find(".response").length == 0){
//                if($(this).attr("data")){
//                    var form = new ResponseReplyView({}).render().el;
//
//                    var gItem = $(this).attr("gitem");
//
//                    $(form).find("button.save").attr("responseid", $(this).attr("data"));
//                    $(form).find("textarea.response").attr("responseid", $(this).attr("data"));
//
//                    $(this).parent().parent().append(form);
//
//                    ////////////////////////////////////////////////////////////////////////////////////
//                    // This event should be captured here in order to capture input for future responses
//                    ////////////////////////////////////////////////////////////////////////////////////
//                    $(".save[responseid='"+$(this).attr("data")+"']").click(function(){
//
//                        //console.log($(this).attr("responseid"), $("textarea[responseid='"+$(this).attr("responseid")+"']").val());
//
//                        if  ($("textarea[responseid='"+$(this).attr("responseid")+"']").val() != ""){
//
//                            var newResponse = new Response({ generalItemId: gItem, responseValue: $("textarea[responseid='"+$(this).attr("responseid")+"']").val(), runId: $.cookie("dojoibl.run"), userEmail: 0, parentId: $(this).attr("responseid") });
//                            newResponse.save({}, {
//                                beforeSend:setHeader,
//                                success: function(r, new_response){
//                                }
//                            });
//                        }
//                        $("textarea[responseid='"+$(this).attr("responseid")+"']").val("");
//                    });
//                }else{
//                    console.error("Id of the response is missing")
//                }
//            }else{
//                //console.log("remove",$(this).parent().siblings(".social-comment:last"));
//                $(this).parent().siblings(".social-comment:last").remove();
//            }
//
//            e.preventDefault();
//        });
//
//        $(".form-control.input-sm.pull-right").keyup(function () {
//            //split the current value of searchInput
//            var data = this.value.split(" ");
//            //create a jquery object of the rows
//            var jo = $("#activity-responses > tbody").find("tr");
//            if (this.value == "") {
//                jo.show();
//                return;
//            }
//            //hide all the rows
//            jo.hide();
//
//            //Recusively filter the jquery object to get results.
//            jo.filter(function (i, v) {
//                var $t = $(this);
//                for (var d = 0; d < data.length; ++d) {
//                    if ($t.is(":contains('" + data[d] + "')")) {
//                        return true;
//                    }
//                }
//                return false;
//            })
//                //show the rows that match.
//                .show();
//        }).focus(function ()
//        {
//            this.value = "";
//            $(this).css({
//                "color": "black"
//            });
//            $(this).unbind('focus');
//        }).css({
//            "color": "#C0C0C0"
//        });
//    }
//});

window.ResponseDataCollectionListView = Backbone.View.extend({
    initialize: function(options){
        _(this).bindAll('render');
        this.collection.bind('add', this.render);
        this.users = options.users;
        this.game = options.game;
        this.runId = options.run;
    },
    render: function(){

        //console.log(this.collection.models.length);

        _.each(this.collection.models, function(response){
            var res = response.toJSON();
            var aux = response.toJSON().userEmail.split(':');
            var user = this.users.where({ 'localId': aux[1] });

            $('#list_answers.data-collection').append(new DataCollectionGridItemView({ model: response.toJSON(), user: user[0] }).render().el);

        }, this);

        document.getElementById('list_answers').onclick = function (event) {
            event = event || window.event;
            var target = event.target || event.srcElement,
                link = target.src ? target.parentNode : target,
                options = {index: link, event: event},
                    links = this.getElementsByTagName('a');
            blueimp.Gallery(links, options);
        };

        $(".add-new-tag").click(function(e){
            e.preventDefault();
            $(".tag-list").append('<li><a href="">Film</a></li>');
        });

        this.collection.reset();
    }
});

window.ResponseView = Backbone.View.extend({
    tagName: "div",
    className: "social-comment",
    model: Response,
    initialize: function(options) {
        this.template = _.template(tpl.get('response'));
        this.user = options.user;
        //console.log(1,this.el);
    },
    render:function () {
        if(this.model.lastModificationDate == 0){
            $(this.el).find(".username > .text-muted.pull-right").prepend("Now");
            $(this.el).append(this.template({ model: this.model, author: this.user.toJSON(), time: "Now" }));
        }else{
            $(this.el).append(this.template({ model: this.model, author: this.user.toJSON(), time: jQuery.timeago(new Date(this.model.lastModificationDate).toISOString()) }));
        }
        return this;
    },
    onclose: function(){
        console.log("close child")
        this.undelegateEvents();
        this.remove();
        this.unbind();
    }
});

window.ResponseQuestionView = Backbone.View.extend({
    tagName: "div",
    className: "social-comment",
    model: Response,
    initialize: function(options) {
        this.template = _.template(tpl.get('response_question'));
        this.user = options.user;
        this.number = options.number;
        //console.log(2,this.el);
    },
    render:function () {
        if(this.model.lastModificationDate == 0){
            $(this.el).find(".username > .text-muted.pull-right").prepend("Now");
            $(this.el).append(this.template({ model: this.model, author: this.user.toJSON(), time: "Now", number: this.number }));
        }else{
            $(this.el).append(this.template({ model: this.model, author: this.user.toJSON(), time: jQuery.timeago(new Date(this.model.lastModificationDate).toISOString()), number: this.number }));
        }
        return this;
    },
    onclose: function(){
        console.log("close child")
        this.undelegateEvents();
        this.remove();
        this.unbind();
    }
});

window.ResponseReplyView = Backbone.View.extend({
    tagName: "div",
    className: "social-comment reply-box",
    model: Response,
    initialize: function() {
        this.template = _.template(tpl.get('response_reply'));
    },
    render:function () {
        $(this.el).append(this.template());

        return this;
    }
});

window.ResponseReplyQuestionView = Backbone.View.extend({
    tagName: "div",
    className: "input-group create-question",
    model: Response,
    initialize: function() {
        this.template = _.template(tpl.get('response_reply_question'));
    },
    render:function () {
        $(this.el).append(this.template());

        return this;
    }
});

window.ActivityView = Backbone.View.extend({
    initialize:function (xhr) {
        if(xhr.model.type.indexOf("VideoObject") > -1){
            this.template = _.template(tpl.get('activity_video'));
        }else if(xhr.model.type.indexOf("OpenBadge") > -1) {
            this.template = _.template(tpl.get('activity_widget'));
        }else if(xhr.model.type.indexOf("AudioObject") > -1) {
            this.template = _.template(tpl.get('activity_html'));
        }else if(xhr.model.type.indexOf("SingleChoiceImageTest") > -1) {
            this.template = _.template(tpl.get('activity_concept_map'));
        }else if(xhr.model.type.indexOf("ScanTag") > -1) {
            this.template = _.template(tpl.get('activity_data_collection'));
        }else if(xhr.model.type.indexOf("ResearchQuestion") > -1) {
            this.template = _.template(tpl.get('activity_research_question'));
        }else{
            //this.template = _.template(tpl.get('activity_text'));
        }
    },
    render:function () {
        $(this.el).html(this.template(this.model));
        //$(this.el).find('#nestable3').nestable();

        //$(this.el).find('.summernote').code(); //save HTML If you need(aHTML: array).
        //$(this.el).find('.summernote').destroy();
        return this;
    }
});

window.DataCollectionGridItemView = Backbone.View.extend({
    tagName: "div",
    className: "file-box",
    model: Response,
    initialize: function() {
        this.template = _.template(tpl.get('response_grid_item'));
    },
    render:function () {
        $(this.el).append(this.template({ model: this.model, modified: jQuery.timeago(new Date(this.model.lastModificationDate).toISOString()), created: jQuery.timeago(new Date(this.model.timestamp).toISOString())}));

        return this;
    }
});

window.ConceptMapView = Backbone.View.extend({
    initialize: function(options){
        this.gItem = options.gItem;

    },
    render: function() {

        var jsonObj = [];
        var nodes = [];
        var links = [];

        _.each(this.model.responses, function(response){
            if(!response.revoked){
                if(response.parentId != -1){
                    var item_nodes = {}
                    item_nodes ["id"] = response.responseId;
                    item_nodes ["name"] = response.responseValue;
                    var min = 50;
                    var max = 550;
                    var random = Math.floor(Math.random() * (max - min + 1)) + min;
                    item_nodes ["x"] = random;
                    var min = 50;
                    var max = 150;
                    var random = Math.floor(Math.random() * (max - min + 1)) + min;
                    item_nodes ["y"] = random;
                    item_nodes ["gItem"] = response.generalItemId;
                    item_nodes ["runId"] = response.runId;
                    nodes.push(item_nodes);
                }else{
                    var item_links = {};

                    var str = response.responseValue;
                    var arr_str = str.split(",");

                    item_links["source"] = arr_str[0];
                    item_links["target"] = arr_str[1];
                    item_links["id"] = response.responseId;

                    links.push(item_links);
                }
            }
        }, this);

        var item2 = {};
        item2 ["nodes"] = nodes;
        item2 ["links"] = links;
        jsonObj.push(item2);

        this.render5(JSON.stringify(jsonObj));

        return this;
    },
    render5: function(jsonObj) {

        // Manage actions
        $(".add-concept").click(function (e) {
            e.preventDefault();

            createNode();
        });

        var self = this;

        this.width  = $("#concept-map").width()-25,
            this.height = 500;
        this.colors = d3.scale.category10();


        this.svg = d3.select("#concept-map")
            .append('svg')
            .attr('oncontextmenu', 'return false;')
            .attr('width', this.width)
            .attr('height', this.height);

        // set up initial nodes and links
        //  - nodes are known by 'id', not by index in array.
        //  - reflexive edges are indicated on the node (as a bold black circle).
        //  - links are always source < target; edge directions are set by 'left' and 'right'.
        // Create nodes and links from jsonObj
        var graph = JSON.parse(jsonObj)[0];

        this.nodes = [], this.links = [], lastNodeId = 2;
        graph.nodes.forEach(function (d) {
            d.reflexive = false;
            self.nodes.push(d);
        });

        if (graph.nodes.length > 1) {
            graph.links.forEach(function (d) {
                d.source = graph.nodes[d.source];
                d.target = graph.nodes[d.target];
                var _link = { source: d.source, target: d.target, left: true, right: false, id: d.id };
                self.links.push(_link);
            });
        }

        // init D3 force layout
        this.force = d3.layout.force()
            .nodes(this.nodes)
            .links(this.links)
            .size([this.width, this.height])
            .linkDistance(250)
            .charge(-500)
            .on('tick', tick);

        // define arrow markers for graph links
        this.svg.append('svg:defs').append('svg:marker')
            .attr('id', 'end-arrow')
            .attr('viewBox', '0 -5 10 10')
            .attr('refX', 6)
            .attr('markerWidth', 3)
            .attr('markerHeight', 3)
            .attr('orient', 'auto')
            .append('svg:path')
            .attr('d', 'M0,-5L10,0L0,5')
            .attr('fill', '#000');

        this.svg.append('svg:defs').append('svg:marker')
            .attr('id', 'start-arrow')
            .attr('viewBox', '0 -5 10 10')
            .attr('refX', 4)
            .attr('markerWidth', 3)
            .attr('markerHeight', 3)
            .attr('orient', 'auto')
            .append('svg:path')
            .attr('d', 'M10,-5L0,0L10,5')
            .attr('fill', '#000');

        // line displayed when dragging new nodes
        var drag_line = this.svg.append('svg:path')
            .attr('class', 'link dragline hidden')
            .attr('d', 'M0,0L0,0');
        this.drag_line = drag_line;

        // handles to link and node element groups
        var path = this.svg.append('svg:g').selectAll('path');
        this.path = path;
        var circle = this.svg.append('svg:g').selectAll('g');
        this.circle = circle;

        // mouse event vars
        var selected_node = null,
            selected_link = null,
            mousedown_link = null,
            mousedown_node = null,
            mouseup_node = null;

        this.selected_node = null,
            this.selected_link = null,
            this.mousedown_link = null,
            this.mousedown_node = null,
            this.mouseup_node = null;

        // update force layout (called automatically each iteration)
        function tick() {
            // draw directed edges with proper padding from node centers
            self.path.attr('d', function(d) {
                var deltaX = d.target.x - d.source.x,
                    deltaY = d.target.y - d.source.y,
                    dist = Math.sqrt(deltaX * deltaX + deltaY * deltaY),
                    normX = deltaX / dist,
                    normY = deltaY / dist,
                    sourcePadding = d.left ? 17 : 12,
                    targetPadding = d.right ? 17 : 12,
                    sourceX = d.source.x + (sourcePadding * normX),
                    sourceY = d.source.y + (sourcePadding * normY),
                    targetX = d.target.x - (targetPadding * normX),
                    targetY = d.target.y - (targetPadding * normY);
                return 'M' + sourceX + ',' + sourceY + 'L' + targetX + ',' + targetY;
            });

            self.circle.attr('transform', function(d) {
                return 'translate(' + d.x + ',' + d.y + ')';
            });
        }

        function mousedown() {
            // prevent I-bar on drag
            //d3.event.preventDefault();

            // because :active only works in WebKit?
            self.svg.classed('active', true);

            if(d3.event.ctrlKey || mousedown_node || mousedown_link) return;

            self.restart();
        }

        function mousemove() {
            if(!mousedown_node) return;

            // update drag line
            self.drag_line.attr('d', 'M' + mousedown_node.x + ',' + mousedown_node.y + 'L' + d3.mouse(self)[0] + ',' + d3.mouse(self)[1]);

            self.restart();
        }

        function mouseup() {
            if(mousedown_node) {
                // hide drag line
                self.drag_line
                    .classed('hidden', true)
                    .style('marker-end', '');
            }

            // because :active only works in WebKit?
            self.svg.classed('active', false);

            // clear mouse event vars
            self.resetMouseVars();
        }

        function spliceLinksForNode(node) {
            var toSplice = self.links.filter(function(l) {
                return (l.source === node || l.target === node);
            });

            console.log(toSplice);


            toSplice.map(function(l) {
                console.log(l);
                deleteResponse(l);
                self.links.splice(self.links.indexOf(l), 1);
            });
        }

        // only respond once per keydown
        var lastKeyDown = -1;

        function keydown() {

            if(lastKeyDown !== -1) return;
            lastKeyDown = d3.event.keyCode;

            // ctrl
            if(d3.event.keyCode === 17) {
                self.circle.call(self.force.drag);
                self.svg.classed('ctrl', true);
            }

            if(!selected_node && !selected_link) return;
            switch(d3.event.keyCode) {
                case 8: // backspace
                case 46: // delete
                    d3.event.preventDefault();

                    if(selected_node) {
                        self.nodes.splice(self.nodes.indexOf(selected_node), 1);
                        spliceLinksForNode(selected_node);

                        deleteResponse(selected_node);

                    } else if(selected_link) {

                        console.log(selected_link);

                        deleteResponse(selected_link);

                        self.links.splice(self.links.indexOf(selected_link), 1);
                    }

                    selected_link = null;
                    selected_node = null;
                    self.restart();
                    break;
                default:
                    break;
            }
        }

        function keyup() {
            lastKeyDown = -1;

            // ctrl
            if(d3.event.keyCode === 17) {
                self.circle
                    .on('mousedown.drag', null)
                    .on('touchstart.drag', null);
                self.svg.classed('ctrl', false);
            }
        }

        function createNode() {
            swal({
                title: "Add new concept",
                text: "Provide a general idea or understanding about the topic of the concept map",
                type: "input",
                showCancelButton: true,
                closeOnConfirm: false,
                animation: "slide-from-top",
                inputPlaceholder: "Example: Gravity"
            }, function (inputValue) {
                if (inputValue === false)
                    return false;
                if (inputValue === "") {
                    swal.showInputError("You need to write something!");
                    return false
                }
                swal("Nice!", "Your new node: " + inputValue + " has been created", "success");

                var newResponse = new Response({
                    generalItemId: $.cookie("dojoibl.activity"),
                    responseValue: inputValue,
                    runId: $.cookie("dojoibl.run"),
                    userEmail: 0
                });
                newResponse.save({}, {
                    beforeSend: setHeader,
                    success: function (r, response) {

                        var item_nodes = {};
                        item_nodes ["id"] = response.responseId;
                        item_nodes ["name"] = response.responseValue;
                        var min = 50;
                        var max = 550;
                        var random = Math.floor(Math.random() * (max - min + 1)) + min;
                        item_nodes ["x"] = random;
                        var min = 50;
                        var max = 150;
                        var random = Math.floor(Math.random() * (max - min + 1)) + min;
                        item_nodes ["y"] = random;
                        item_nodes ["gItem"] = response.generalItemId;
                        item_nodes ["runId"] = response.runId;

                        self.nodes.push(item_nodes);

                        self.restart();
                    }
                });
            });
        }

        function deleteResponse(response) {

            console.log(response);

            var responseDelete = new ResponseDelete({ id: response.id });
            responseDelete.id = response.id;
            responseDelete.destroy({
                beforeSend:setHeader,
                success: function(r, new_response){
                }
            });
        }

        // app starts here
        self.svg.on('mousedown', mousedown)
            .on('mousemove', mousemove)
            .on('mouseup', mouseup);
        d3.select(window)
            .on('keydown', keydown)
            .on('keyup', keyup);


        var styles = {
            visibility : "hidden",
            display: "none"
        };

        $(".spiner-example, .sk-spinner .sk-spinner-chasing-dots").css(styles)

        self.restart();
    },
    resetMouseVars: function () {
        var self = this;
        self.mousedown_node = null;
        self.mouseup_node = null;
        self.mousedown_link = null;
    },
    createLink: function (pointA, pointB) {

        var self = this;

        var newResponse = new Response({
            generalItemId: $.cookie("dojoibl.activity"),
            responseValue: pointA + "," + pointB,
            runId: $.cookie("dojoibl.run"),
            userEmail: 0,
            parentId: -1
        });
        newResponse.save({}, {
            beforeSend: setHeader,
            success: function (r, response) {

                var item_links = {};

                item_links["id"] = response.id;
                item_links["source"] = pointA;
                item_links["target"] = pointB;

                self.links.push(item_links);

                self.restart();
            }
        });
    }
    ,
    restart: function(){
        var self = this;

        self.path = self.path.data(self.links);

        // update existing links
        self.path.classed('selected', function(d) { return d === self.selected_link; })
            .style('marker-start', function(d) { return d.left ? 'url(#start-arrow)' : ''; })
            .style('marker-end', function(d) { return d.right ? 'url(#end-arrow)' : ''; });

        // add new links
        self.path.enter().append('svg:path')
            .attr('class', 'link')
            .classed('selected', function(d) { return d === self.selected_link; })
            .style('marker-start', function(d) { return d.left ? 'url(#start-arrow)' : ''; })
            .style('marker-end', function(d) { return d.right ? 'url(#end-arrow)' : ''; })
            .on('mousedown', function(d) {
                if(d3.event.ctrlKey) return;

                console.log(d);

                // select link
                self.mousedown_link = d;
                if(self.mousedown_link === self.selected_link) self.selected_link = null;
                else self.selected_link = self.mousedown_link;
                self.selected_node = null;
                self.restart();
            });

        // remove old links
        self.path.exit().remove();

        // circle (node) group
        // NB: the function arg is crucial here! nodes are known by id, not by index!
        self.circle = self.circle.data(self.nodes, function(d) { return d.id; });

        // update existing nodes (reflexive & selected visual states)
        self.circle.selectAll('circle')
            .style('fill', function(d) { return (d === self.selected_node) ? d3.rgb(self.colors(d.id)).brighter().toString() : self.colors(d.id); })
            .classed('reflexive', function(d) { return d.reflexive; });

        // add new nodes
        var g = self.circle.enter().append('svg:g');

        g.append('svg:circle')
            .attr('class', 'node')
            .attr('r', 30)
            .style('fill', function(d) { return (d === self.selected_node) ? d3.rgb(self.colors(d.id)).brighter().toString() : self.colors(d.id); })
            .style('stroke', function(d) { return d3.rgb(self.colors(d.id)).darker().toString(); })
            .on('mouseover', function(d) {
                if(!self.mousedown_node || d === self.mousedown_node) return;
                // enlarge target node
                d3.select(this).attr('transform', 'scale(1.1)');
            })
            .on('mouseout', function(d) {
                if(!self.mousedown_node || d === self.mousedown_node) return;
                // unenlarge target node
                d3.select(this).attr('transform', '');
            })
            .on('mousedown', function(d) {
                if(d3.event.ctrlKey) return;

                // select node
                self.mousedown_node = d;
                if(self.mousedown_node === self.selected_node) self.selected_node = null;
                else self.selected_node = self.mousedown_node;
                self.selected_link = null;

                // reposition drag line
                self.drag_line
                    .style('marker-end', 'url(#end-arrow)')
                    .classed('hidden', false)
                    .attr('d', 'M' + self.mousedown_node.x + ',' + self.mousedown_node.y + 'L' + self.mousedown_node.x + ',' + self.mousedown_node.y);

                self.restart();
            })
            .on('mouseup', function(d) {
                if(!self.mousedown_node) return;

                // needed by FF
                self.drag_line
                    .classed('hidden', true)
                    .style('marker-end', '');

                // check for drag-to-self
                self.mouseup_node = d;
                if(self.mouseup_node === self.mousedown_node) { self.resetMouseVars(); return; }

                // unenlarge target node
                d3.select(this).attr('transform', '');

                // add link to graph (update if exists)
                // NB: links are strictly source < target; arrows separately specified by booleans
                var source, target, direction;
                if(self.mousedown_node.id < self.mouseup_node.id) {
                    source = self.mousedown_node;
                    target = self.mouseup_node;
                    direction = 'right';
                } else {
                    source = self.mouseup_node;
                    target = self.mousedown_node;
                    direction = 'left';
                }

                var link;
                link = self.links.filter(function(l) {
                    return (l.source === source && l.target === target);
                })[0];

                if(link) {
                    link[direction] = true;
                } else {
                    link = {source: source, target: target, left: false, right: false};
                    link[direction] = true;
                    self.links.push(link);
                }

                self.createLink(source.index, target.index);

                // select new link
                self.selected_link = link;
                self.selected_node = null;
                self.restart();
            });
        // show node IDs
        g.append('svg:text')
            .attr('x', 0)
            .attr('y', 4)
            .attr('class', 'id')
            .text(function(d) { return d.name; });

        // remove old nodes
        self.circle.exit().remove();

        // set the graph in motion
        self.force.start();
    }
});

// =====================================================

window.InquiryLeftSidebarView = Backbone.View.extend({
    tagName: "div",
    className: "col-md-2 pull-left",
    initialize:function () {
        this.template = _.template(tpl.get('inquiry_left_sidebar'));
        //console.log("load inquiry left sidebar");
    },
    render:function () {
        $(this.el).html(this.template(this.model));

        return this;
    }
});

window.InquirySidebarView = Backbone.View.extend({
    initialize:function () {
        this.template = _.template(tpl.get('inquiry_sidebar'));

    },
    render:function () {
        $(this.el).html(this.template(this.model));

        return this;
    },
    cleanup: function() {
        this.undelegateEvents();
        $(this.el).empty();
    }
});

window.InquiryStructureView = Backbone.View.extend({
    initialize:function () {
        this.template = _.template(tpl.get('inquiry_structure'));
        console.log("hola");
    },
    events: {
        'click ul#circlemenu > li > div > a': 'open_phase'
    },
    open_phase: function(e){
        console.debug("Access phase");

        //$(e.currentTarget).parent().parent().siblings().hide();

        //$("ul#circlemenu").attr("id", "circlemenu2");

        //$("#summary .title-summary").show();
        //$("#summary ul.nav-tabs.box-header").hide();
        //
        //$("#inquiry-explanation").hide();
        //
        //$("ul.box-header.with-border.nav.nav-tabs > li").fadeOut(100);

    },
    add: function(ev){
        console.debug("[Add event InquiryStructureView]","Click in activity. Hiding siblings...");
    },
    render:function () {
        $(this.el).html(this.template(this.model));
        return this;
    }
});

// Messages

window.MessagesListView = Backbone.View.extend({
    el: ".chat-discussion",
    initialize:function (options) {

        this.collection.on('add', this.addOne, this);
        this.collection.on('reset', this.addAll, this);

        this.number = 0;
        this.childViews = [];

    },
    addAll: function(){
        this.collection.forEach(this.addOne, this);
    },
    addOne: function(message){
        var view;
        if(message.toJSON().senderId == $.cookie("dojoibl.localId")){
            view = new MessageRightView({ model: message.toJSON() });
        }else{
            view = new MessageLeftView({ model: message.toJSON() });
        }
        this.childViews.push(view);
        console.log("hol");
        view = view.render().el;
        this.$el.append(view);
    },
    render: function () {
        this.collection.forEach(this.addOne, this);
        return this;
    }
});

window.MessageLeftView = Backbone.View.extend({
    tagName:  "div",
    className: "left",
    initialize:function () {
        this.template = _.template(tpl.get('message_right'));
    },
    render:function () {
        $(this.el).html(this.template({ model: this.model, time: jQuery.timeago(new Date(this.model.date))} ));
        return this;
    }
});

window.MessageRightView = Backbone.View.extend({
    tagName:  "div",
    className: "right",
    initialize:function () {
        this.template = _.template(tpl.get('message'));
    },
    render:function () {
        $(this.el).html(this.template({ model: this.model, time: jQuery.timeago(new Date(this.model.date))}));
        return this;
    }
});

window.ActivityDepencyView = window.ActivityView.extend({
    tagName:  "span",
    id: "skill-1-2",
    //className: "skill-tree-row row-1",
    initialize:function () {
        this.template = _.template(tpl.get('activityDependency'));
    }
});

window.ResponseDiscussionListView = Backbone.View.extend({
    el: $(".box-footer.box-comments"),
    initialize: function(options){

        _(this).bindAll('render');

        this.collection.bind('add', this.render);

        this.users = options.users;
    },
    render: function(){
        _.each(this.collection.models, function(response){

            var aux = response.toJSON().userEmail.split(':');
            var user = this.users.where({ 'localId': aux[1] });
            $(".box-footer.box-comments").append(new ResponseDiscussionView({ model: response.toJSON(), user: user[0] }).render().el);
        }, this);

        $(".number-comments").html(this.collection.models.length == 1) ?
        this.collection.models.length+" comment" :
        this.collection.models.length+" comments"

        this.collection.reset();
    }
});

window.ResponseDiscussionView = Backbone.View.extend({
    tagName: "div",
    className: "box-comment",
    model: Response,
    initialize: function(options) {
        this.template = _.template(tpl.get('response_discussion'));
        this.template_author = _.template(tpl.get('response_discussion_author'));

        this.user = options.user;
    },
    render:function () {
        // TODO sometimes JSON error. I need to check this.
        $(this.el).append(this.template_author(this.user.toJSON()));

        $(this.el).append(this.template(this.model));
        $(this.el).find(".username").prepend(this.user.toJSON().name);
        if(this.model.lastModificationDate == 0){
            $(this.el).find(".username > .text-muted.pull-right").prepend("Now");
        }else{
            $(this.el).find(".username > .text-muted.pull-right").prepend(jQuery.timeago(new Date(this.model.lastModificationDate).toISOString()));
        }
        return this;
    }
});

window.ResponseTreeView = Backbone.View.extend({
    initialize: function(){

        _(this).bindAll('render');

        this.collection.bind('add', this.render);
    },
    render: function(){
        _.each(this.collection.models, function(response){
            $("#nestable3 ol.dd-list").append(new ResponseTreeviewItemView({ model: response.toJSON() }).render().el);
        }, this);

        this.collection.reset();
    }
});

window.ResponseTreeviewItemView = Backbone.View.extend({
    tagName: "li",
    className: "dd-item dd3-item",
    model: Response,
    initialize: function(options) {
        this.template = _.template(tpl.get('response_treeview'));
    },
    render:function () {

        $(this.el).append(this.template(this.model));

        return this;
    }
});

// Users
window.UserView = Backbone.View.extend({
    tagName:  "li",
    className: "dropdown user user-menu",
    initialize:function () {
        this.template = _.template(tpl.get('user'));
    },

    render:function () {
        $(this.el).html(this.template(this.model));
        return this;
    }
});

window.UserSidebarView = Backbone.View.extend({
    tagName:  "div",
    className: "user-panel",
    initialize:function () {
        this.template = _.template(tpl.get('user_sidebar'));
    },

    render:function () {
        $(this.el).html(this.template(this.model));
        return this;
    }
});

window.UsersInquiryView = Backbone.View.extend({
    tagName:  "li",
    initialize:function () {
        this.template = _.template(tpl.get('user_inquiry'));
    },

    render:function () {
        $(this.el).html(this.template(this.model));
        return this;
    }
});

// Notifications
window.MessageNotificationView = Backbone.View.extend({
    tagName:  "li",
    initialize:function () {
        this.template = _.template(tpl.get('message_notification'));
    },
    render:function () {
        $(this.el).html(this.template(this.model));
        return this;
    }
});

window.GeneralFloatingNotificationView = Backbone.View.extend({
    tagName:  "div",
    className: "ui-pnotify stack-topleft",
    initialize:function () {
        this.template = _.template(tpl.get('notification_floating'));
    },
    render:function () {
        $(this.el).html(this.template(this.model));
        return this;
    }
});

//window.NotificationSideBarView = Backbone.View.extend({
//    tagName:  "li",
//    initialize:function () {
//        this.template = _.template(tpl.get('notification_sidebar'));
//    },
//    render:function () {
//        $(this.el).html(this.template(this.model));
//        return this;
//    }
//});

window.InquiryToolbarView = Backbone.View.extend({
    className: "row wrapper white-bg page-heading toolbar",
    initialize:function (options) {
        this.template = _.template(tpl.get('inquiry_toolbar'));
        this.runId = options.runId;

    },
    render:function () {
        $(this.el).html(this.template({ run: this.runId }));

        return this;
    }
});

// Timeline
//window.TimelineView = Backbone.View.extend({
//    el:  "#vertical-timeline",
//    initialize:function (options) {
//
//        _(this).bindAll('render');
//        _(this).bindAll('hidebutton');
//        _(this).bindAll('add');
//        this.collection.bind('add', this.add);
//        this.collection.bind('add', this.hidebutton);
//
//        this.left = true;
//        this.style = "float: left";
//        this.style1 = "float: right";
//        this.style2 = "float: left";
//        this.right = "";
//
//    },
//    add: function(response){
//        console.log(response.toJSON());
//        //if(!response.toJSON().revoked){
//
//            if(response.toJSON().generalItemId != this.right){
//                if(this.left == true){
//                    this.left =  false;
//                    this.style =  this.style2;
//                }else{
//                    this.left =  true;
//                    this.style =  this.style1;
//                }
//            }
//
//            this.$el.append(new TimelineItemView({ model: response.toJSON(), right: this.left } ).render().el);
//
//            this.right = response.toJSON().generalItemId;
//        //}
//    },
//    hidebutton: function(response){
//      if(!this.collection.resumptionToken){
//        $(".show-more-responses").hide();
//      }
//    }
//    ,
//    render:function () {
//        //console.log("render foreach "+this.collection.models.length);
//        //if(this.collection.models.length == 0){
//        //    this.$el.html('<div class="alert alert-warning">' +
//        //    'The timeline is empty. Be the first contributing to this inquiry to see some activities here.' +
//        //    '</div>')
//        //    $("#vertical-timeline::before").css('background', '#fff');
//        //}else{
//            var right;
//            _.each(this.collection.models, function(response){
//                if(!response.toJSON().revoked) {
//                    if(response.toJSON().generalItemId != this.right){
//                        if(this.left == true){
//                            this.left =  false;
//                            this.style =  this.style2;
//                        }else{
//                            this.left =  true;
//                            this.style =  this.style1;
//                        }
//                    }
//
//                    this.$el.prepend(new TimelineItemView({ model: response.toJSON(), right: this.left } ).render().el);
//
//                    this.right = response.toJSON().generalItemId;
//                }
//            }, this);
//        //}
//
//        //this.collection.reset();
//
//        return this;
//    }
//});

// Responses
window.TimelineView = Backbone.View.extend({
    el: "#vertical-timeline",
    initialize: function(options){

        this.collection.on('add', this.addOne, this);
        this.collection.on('add', this.hidebutton, this);
        this.collection.on('reset', this.render, this);

        this.childViews = [];

        this.left = true;
        this.style = "float: left";
        this.style1 = "float: right";
        this.style2 = "float: left";
        this.right = "";

    },
    render: function(){
        //console.log("render")
        this.$el.empty();
        this.collection.forEach(this.addOne, this);
        return this;
    },
    addAll: function(){
        //console.log("addAll")
        this.collection.forEach(this.addOne, this);
    },
    hidebutton: function(response){
        if(!this.collection.resumptionToken){
            $(".show-more-responses").hide();
        }
    },
    addOne: function(response){
        //console.log("addOne", response.toJSON())
        if(response.toJSON().generalItemId != this.right){
            if(this.left == true){
                this.left =  false;
                this.style =  this.style2;
            }else{
                this.left =  true;
                this.style =  this.style1;
            }
        }

        var childView = new TimelineItemView({ model: response.toJSON(), right: this.left } );

        this.childViews.push(childView);

        childView = childView.render();

        this.$el.prepend(childView.el);

        this.right = response.toJSON().generalItemId;
    },
    onClose: function(){
        this.remove();
        this.unbind();
        this.collection.unbind("add", this.addOne);

        //console.log("remove timelineview");

        // handle other unbinding needs, here
        _.each(this.childViews, function(childView){
            if (childView.close){
                childView.close();
            }
        })
    }
});

window.TimelineItemView = Backbone.View.extend({
    tagName:  "div",
    className: "feed-element",
    initialize:function (options) {
        this.right = options.right;
        this.template = _.template(tpl.get('timeline_item'));
        _(this).bindAll('render');
    },
    render:function () {

        var _self = this;

        if (!app.ActivityList.get(_self.model.generalItemId)) {
            var act = new ActivityUpdate();
            act.id = _self.model.generalItemId;
            act.gameId = $.cookie("dojoibl.game");
            act.fetch({
                beforeSend: setHeader,
                success: function (a, r) {

                    ////////////////////////////////////////////////////////////////////////////////////////////////                    // We need to check both. If you delete a GeneralItem all its responses are still deleted: false
                    // so they appear here.
                    ////////////////////////////////////////////////////////////////////////////////////////////////
                    if(r.deleted != true && ! r.hasOwnProperty('error') ){
                        app.ActivityList.add(r);
                        //$(_self.el).html("<p>hola</p>")
                        $(_self.el).html(_self.template({
                            model: _self.model,
                            author: _self.model.userEmail.split(':')[1],
                            time: jQuery.timeago(new Date(_self.model.lastModificationDate).toISOString()),
                            timeDate: new Date(_self.model.lastModificationDate).toLocaleDateString('en-GB'),
                            activity: r,
                            right: _self.right
                        }));
                    }
                }
            });
        } else {
            $(_self.el).html(_self.template({
                model: _self.model,
                author: _self.model.userEmail.split(':')[1],
                time: jQuery.timeago(new Date(_self.model.lastModificationDate).toISOString()),
                timeDate: new Date(_self.model.lastModificationDate).toLocaleDateString('en-GB'),
                activity: app.ActivityList.get(_self.model.generalItemId).toJSON(),
                right: _self.right
            }));
        }
        return _self;
    },
    onclose: function(){
        console.log("close child")
        this.undelegateEvents();
        this.remove();
        this.unbind();
    }
});