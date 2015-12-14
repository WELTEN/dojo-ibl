//window.GameListView = Backbone.View.extend({
//    tagName:  "div",
//    className: "col-md-4",
//    initialize:function () {
//
//        console.log("A game view has been initialized");
//
//        //console.log(this.options.model2);
//
//        this.template = _.template(tpl.get('game'));
//
//
//
//    },
//    events: {
//        'click #show-runs' : 'showRuns'
//
//    },
//    render: function () {
//        $(this.el).html(this.template(this.model));
//        return this;
//    },
//    showRuns: function(e){
//        e.preventDefault();
//        console.log(this.model);
//        console.log(_.template(tpl.get('run')));
//        var subject = $('#search').val() || 'NYC';
//        this.tweets.url = "http://localhost:8888/rest/myGames/gameId/"+ this.id;
//        this.tweets.fetch();
//        //$(this).append();
//    }
//
//    //,
//    //events:{
//    //    "click .save":"saveWine"
//    //},
//    //saveWine:function () {
//    //    //this.model.set({
//    //    //    name:$('#name').val(),
//    //    //    grapes:$('#grapes').val(),
//    //    //    country:$('#country').val(),
//    //    //    region:$('#region').val(),
//    //    //    year:$('#year').val(),
//    //    //    description:$('#description').val()
//    //    //});
//    //    if (this.model.isNew()) {
//    //        var self = this;
//    //        app.wineList.create(this.model, {
//    //            success:function () {
//    //                app.navigate('wines/' + self.model.id, false);
//    //            }
//    //        });
//    //    } else {
//    //        this.model.save();
//    //    }
//    //
//    //    return false;
//    //}
//});

//// Main
//window.MainView = Backbone.View.extend({
//    tagName: "div",
//    className: "row",
//    initialize:function () {
//        this.template = _.template(tpl.get('main'));
//    },
//    render:function () {
//        $(this.el).html(this.template(this.model));
//
//        return this;
//    }
//});

// Game
window.GameListView = Backbone.View.extend({
    tagName:  "div",
    className: "col-lg-3 animated fadeInUp",
    initialize:function (options) {
        //this.collection.bind('add', this.render);
        this.listenTo(app.GameList, 'add', this.show);
        if(options.v == 1){
            this.template = _.template(tpl.get('game_teacher'));
        }

        if(options.v == 2){
            this.template = _.template(tpl.get('game'));
        }
    },
    events: {
        'click .show-runs' : 'showRuns'
    },
    show: function(){
        console.log("render");
    },
    render: function () {
        $(this.el).html(this.template({
            model: this.model,
            time: new Date(this.model.lastModificationDate).toLocaleDateString(),
            timeago: jQuery.timeago(new Date(this.model.lastModificationDate).toISOString())
        }));
        return this;
    },
    showRuns: function(e){
        e.preventDefault();
        var _aux = $(this.el).find(".widget-text-box");

        console.log($(_aux).length, "Game Id: "+this.model.gameId);
        $(_aux).slideUp(200).html("");

        this.RunAccessList = new RunByGameCollection({ id: this.model.gameId });
        this.RunAccessList.fetch({
            beforeSend: setHeader,
            success: function(response, xhr) {
                if(xhr.runs.length == 0){
                    console.error("The game does not have runs. There have been a problem during the creation of the inquiry.")
                    //$(_aux).html("<li><a href=''>No inquiries</a></li>").slideDown(200);
                }else if(xhr.runs.length == 1){
                    _.each(xhr.runs, function(run){
                        $(_aux).html(new this.RunListView({ model: run }).render().el).slideDown(200);
                    });
                }else{
                    _.each(xhr.runs, function(run){
                        $(_aux).html(new this.RunListView({ model: run }).render().el).slideDown(200);
                    });
                }
            }
        });
    }
});

// Run
window.RunListView = Backbone.View.extend({
    tagName:  "li",
    initialize:function () {
        this.template = _.template(tpl.get('run'));
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
    className: "col-md-9 wrapper wrapper-content animated fadeInUp",
    initialize:function () {
        this.template = _.template(tpl.get('inquiry'));
    },
    render:function () {
        $(this.el).html(this.template(this.model));

        return this;
    }
});

window.InquiryNewView = Backbone.View.extend({
    tagName:  "div",
    className: "col-md-12 wrapper wrapper-content animated fadeInUp",
    initialize:function () {
        this.template = _.template(tpl.get('inquiry_new'));

    },
    render:function () {
        $(this.el).html(this.template(this.model));

        return this;
    }
});

window.NewActivityNewInquiryView = Backbone.View.extend({
    tagName:  "tr",
    initialize:function () {
        this.template = _.template(tpl.get('new_activity_new_inquiry'));

    },
    events: {
        "change .type-activity": "changeSelect"
    },
    changeSelect: function() {
        this.$(".type-activity option:selected").each(function() {
            if($( this ).val() == "org.celstec.arlearn2.beans.generalItem.VideoObject" ||
                $( this ).val() == "org.celstec.arlearn2.beans.generalItem.OpenBadge"){
                    $(this).closest("tr").find(".feed-activity").prop('disabled', true);
                console.log($(this).closest("tr").find(".feed-activity").prop('disabled', false));
            }else{
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
    render:function () {
        $(this.el).html(this.template());

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

        return this;
    },
    events: {
        "click #send-message": "clickMessage",
        "keypress #add-new-message": "pressKeyMessage"
    },
    clickMessage: function() {
        var newMessage = new Message({ runId: $.cookie("dojoibl.run"), threadId: 0, subject: "", body: $('input#add-new-message').val() });

        newMessage.save({}, {
            beforeSend:setHeader
        });
        $('input#add-new-message').val('');
    },
    pressKeyMessage: function(e){
        var key = e.which;
        if(key == 13){
            console.log($(this));
            console.log("message send");
            var newMessage = new Message({ runId: $.cookie("dojoibl.run"), threadId: 0, subject: "", body: $('input#add-new-message').val() });

            newMessage.save({}, {
                beforeSend:setHeader
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
        console.log(this);
        $(this.el).empty();
    }
});

// Phases
window.PhaseView = Backbone.View.extend({
    className: "animated fadeInUp",
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
        $(this.el).html(this.template({model: this.model, phase: this.phase}));
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
    initialize: function(options){
        _(this).bindAll('render');
        this.collection.bind('add', this.render);
        this.users = options.users;
        this.game = options.game;
        this.runId = options.run;
    },
    render: function(){

        _.each(this.collection.models, function(response){
            var res = response.toJSON();
            var aux = response.toJSON().userEmail.split(':');
            var user = this.users.where({ 'localId': aux[1] });

            if(res.parentId != 0){
                $(new ResponseView({ model: response.toJSON(), user: user[0] }).render().el).insertBefore($("textarea[responseid='"+res.parentId+"']").parent().parent());
            }else{
                $(new ResponseView({ model: response.toJSON(), user: user[0] }).render().el).insertBefore($('#list_answers > .social-comment:last-child'));
            }


        }, this);

        this.collection.reset();

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

                        console.log($(this).attr("responseid"), $("textarea[responseid='"+$(this).attr("responseid")+"']").val());

                        if  ($("textarea[responseid='"+$(this).attr("responseid")+"']").val() != ""){

                            var newResponse = new Response({ generalItemId: gItem, responseValue: $("textarea[responseid='"+$(this).attr("responseid")+"']").val(), runId: $.cookie("dojoibl.run"), userEmail: 0, parentId: $(this).attr("responseid") });
                            newResponse.save({}, {
                                beforeSend:setHeader,
                                success: function(r, new_response){
                                    app.Response.add(new_response);
                                }
                            });
                        }
                        $("textarea[responseid='"+$(this).attr("responseid")+"']").val("");
                    });
                }else{
                    console.error("Id of the response is missing")
                }
            }else{
                console.log("remove");
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
        }).focus(function () {
            this.value = "";
            $(this).css({
                "color": "black"
            });
            $(this).unbind('focus');
        }).css({
            "color": "#C0C0C0"
        });
    }
});

window.ResponseView = Backbone.View.extend({
    tagName: "div",
    className: "social-comment",
    model: Response,
    initialize: function(options) {
        this.template = _.template(tpl.get('response'));
        this.user = options.user;
    },
    render:function () {
        if(this.model.lastModificationDate == 0){
            $(this.el).find(".username > .text-muted.pull-right").prepend("Now");
            $(this.el).append(this.template({ model: this.model, author: this.user.toJSON(), time: "Now" }));
        }else{
            $(this.el).append(this.template({ model: this.model, author: this.user.toJSON(), time: jQuery.timeago(new Date(this.model.lastModificationDate).toISOString()) }));
        }
        return this;
    }
});

window.ResponseReplyView = Backbone.View.extend({
    tagName: "div",
    className: "social-comment",
    model: Response,
    initialize: function() {
        this.template = _.template(tpl.get('response_reply'));
    },
    render:function () {
        $(this.el).append(this.template());

        return this;
    }
});


window.ActivityView = Backbone.View.extend({
    initialize:function (xhr) {
        if(xhr.model.type.indexOf("VideoObject") > -1){
            //console.debug("VideoObject");
            this.template = _.template(tpl.get('activity_video'));
        }else if(xhr.model.type.indexOf("OpenBadge") > -1) {
            //console.debug("OpenBadge");
            this.template = _.template(tpl.get('activity_widget'));
        }else if(xhr.model.type.indexOf("AudioObject") > -1) {
            console.log("Insert html");
            this.template = _.template(tpl.get('activity_discussion'));
        }else if(xhr.model.type.indexOf("MultipleChoiceImageTest") > -1) {
            this.template = _.template(tpl.get('activity_tree_view'));
        }else if(xhr.model.type.indexOf("SingleChoiceImageTest") > -1) {
            this.template = _.template(tpl.get('activity_concept_map'));
        }else{
            console.debug("Discussion");
            this.template = _.template(tpl.get('activity_detail'));
        }
    },
    render:function () {
        $(this.el).html(this.template(this.model));
        $(this.el).find('#nestable3').nestable();
        return this;
    }
});


window.ConceptMapView = Backbone.View.extend({
    initialize: function(){
    },
    render: function(){

        var jsonObj = [];
        var nodes = [];
        var links = [];

        _.each(this.model.responses, function(response){
            //console.log(response);
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
        }, this);

        var item2 = {};
        item2 ["nodes"] = nodes;

        var item_links = {};

        item_links["source"] = 0;
        item_links["target"] = 1;

        links.push(item_links);

        item2 ["links"] = links;
        jsonObj.push(item2);

        this.render3(JSON.stringify(jsonObj));

        return this;
    },
    render2: function(jsonObj){

        var width = $("#concept-map").width(),
            height = 400, shiftKey;

        var svg = d3.select("#concept-map")
            .each(function() { this.focus(); })
            .append("svg")
            .attr("width", width)
            .attr("height", height)
            .on("contextmenu", createNew);

        var force = d3.layout.force()
            .size([width, height])
            .charge(-400)
            .linkDistance(40)
            .on("tick", tick);

        var drag = force.drag()
            .on("dragstart", dragstart);

        var brush = svg.append("g")
            .datum(function() { return {selected: false, previouslySelected: false}; })
            .attr("class", "cp-brush");

        var labels = svg.append("g")
            .attr("class", "labels")
            .selectAll("text");

        var graph = JSON.parse(jsonObj)[0];

        var nodes = force.nodes();
        var links = force.links();

        graph.links.forEach(function(d) {
            d.source = graph.nodes[d.source];
            d.target = graph.nodes[d.target];
        });

        graph.nodes.forEach(function(d){
            nodes.push(d);
        });

        var node = svg.append("g")
            .attr("class", "node")
            .selectAll("rect");

        var link = svg.append("g")
            .attr("class", "cp-link")
            .selectAll("line");

        force.start();

        //function restart() {
        //    link = link.data(links);
        //
        //    link.enter().insert("line", ".node")
        //        .attr("class", "link");
        //
        //    node = node.data(nodes);
        //
        //    node.enter().insert("circle", ".cursor")
        //        .attr("class", "node")
        //        .attr("r", 5)
        //        .call(force.drag);
        //
        //    force.start();
        //}

        link = link.data(graph.links)
            .enter().append("line")
            .attr("class", "link");

        node = node.data(graph.nodes)
            .enter().append("circle")
            .attr("class", "node")
            .attr("r", 10)
            .on("dblclick", dblclick)
            .call(drag)
            .on("mousedown", function(d) {
                if (!d.selected) { // Don't deselect on shift-drag.
                    if (!shiftKey){
                        node.classed("selected", function(p) { return p.selected = d === p; });
                        labels.classed("selected", function(p) { return p.selected = d === p; });
                    }
                    else{
                        d3.select(this).classed("selected", d.selected = true);
                    }
                }
            });

        console.log(node);

        labels = labels.data(graph.nodes).enter().append("text")
            .attr("y", function(d) {
                return d.y+20;
            })
            .attr("x", function(d) {
                return d.x+10;
            })
            .text(function(d){
                return d.name
            });

        function tick() {
            link.attr("x1", function(d) { return d.source.x; })
                .attr("y1", function(d) { return d.source.y; })
                .attr("x2", function(d) { return d.target.x; })
                .attr("y2", function(d) { return d.target.y; });

            node.attr("cx", function(d) { return d.x; })
                .attr("cy", function(d) { return d.y; });

            labels.attr("x", function(d) { return d.x+10; })
                .attr("y", function(d) { return d.y+20; });
        }

        function dblclick(d) {
            d3.select(this).classed("fixed", d.fixed = false);
        }

        function dragstart(d) {
            d3.select(this).classed("fixed", d.fixed = true);
        }

        function update(jsonObj) {

            // DATA JOIN
            // Join new data with old elements, if any.
            //console.log(jsonObj);
            //var graph = JSON.parse(jsonObj)[0];
            //
            //graph.links.forEach(function(d) {
            //    d.source = graph.nodes[d.source];
            //    d.target = graph.nodes[d.target];
            //});

            console.log(jsonObj);

            var node = svg.selectAll("rect").data(jsonObj);

            console.log(node);
            console.log(svg);

            //node.attr("class", "node")
            //    .attr("r", 10)
            //    .on("dblclick", dblclick)
            //    .call(drag)

            // UPDATE
            // Update old elements as needed.
            //text.attr("class", "update");

            // ENTER
            // Create new elements as needed.
            node.enter().append("circle")
                .attr("class", "node")
                .attr("r", 10)
                .on("dblclick", dblclick)
                .call(drag);

            // ENTER + UPDATE
            // Appending to the enter selection expands the update selection to include
            // entering elements; so, operations on the update selection after appending to
            // the enter selection will apply to both entering and updating nodes.
            //labels = labels.data(jsonObj).enter().append("text")
            //    .attr("y", function(d) {
            //        return d.y+20;
            //    })
            //    .attr("x", function(d) {
            //        return d.x+10;
            //    })
            //    .text(function(d){
            //        return d.name
            //    });

            // EXIT
            // Remove old elements as needed.
            node.exit().remove();
            //labels.exit().remove();
        }

        function createNew() {
            d3.event.preventDefault();

            console.log(nodes, node);

            var point = d3.mouse(this),
                node = {x: point[0], y: point[1]},
                n = nodes.push(node);
            node.filter(function(d) {
                var newResponse = new Response({
                    generalItemId: d.gItem,
                    responseValue: "nuevoooo",
                    runId: d.runId,
                    userEmail: 0
                });
                newResponse.save({}, {
                    beforeSend: setHeader,
                    success: function (r, response) {
                        // Add and remove elements on the graph object
                        node.push({"id": response.responseId});

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
                    }
                });
            });

            //link = link.data(links);
            //
            //link.enter().insert("line", ".node")
            //    .attr("class", "link");

            node = node.data(nodes);

            node.enter().insert("circle", ".cursor")
                .attr("class", "node")
                .attr("r", 5)
                .call(force.drag);

            force.start();

            //// add links to any nearby nodes
            //nodes.forEach(function(target) {
            //    var x = target.x - node.x,
            //        y = target.y - node.y;
            //    if (Math.sqrt(x * x + y * y) < 30) {
            //        links.push({source: node, target: target});
            //    }
            //});

        }

        $(".add-concept").click(function(e){
            e.preventDefault();
            node.filter(function(d) {



                //// add links to any nearby nodes
                //nodes.forEach(function(target) {
                //    var x = target.x - node.x,
                //        y = target.y - node.y;
                //    if (Math.sqrt(x * x + y * y) < 30) {
                //        links.push({source: node, target: target});
                //    }
                //});

                //if(d.selected){ $("input.new-concept").val()
                    var newResponse = new Response({ generalItemId: d.gItem, responseValue: "nuevoooo" , runId: d.runId, userEmail: 0 });
                    newResponse.save({}, {
                        beforeSend:setHeader,
                        success: function(r, response){
                            // Add and remove elements on the graph object
                            node.push({"id":response.responseId});

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
                            console.log(nodes);
                            nodes.push(item_nodes);
                        }
                    });
                //}

                return d.selected;
            })
        });
    },
    render3: function(jsonObj){

        var width = $("#concept-map").width(),
            height = 400, shiftKey;

        var fill = d3.scale.category20();



        var force = d3.layout.force()
            .size([width, height])
            //.nodes([{}]) // initialize with a single node
            .linkDistance(200)
            .charge(-200)
            .on("tick", tick);

        var drag = force.drag()
            .on("dragstart", dragstart);

        var svg = d3.select("#concept-map").append("svg")
            .attr("width", width)
            .attr("height", height)
            .on('mousemove', mousemove)
            //.on("keydown", keydown)
            //.on("mousedown", mousedown);


        var nodes = force.nodes(),
            links = force.links(),
            //labels = force.labels(),
            node = svg.selectAll(".node"),
            link = svg.selectAll(".link"),
            label = svg.selectAll(".labels");

        // options
        var movement_allowed = null;

        // mouse event vars
        var selected_node = null,
            selected_link = null,
            mousedown_link = null,
            mousedown_node = null,
            mouseup_node = null;


        // define arrow markers for graph links
        svg.append('svg:defs').append('svg:marker')
            .attr('id', 'end-arrow')
            .attr('viewBox', '0 -5 10 10')
            .attr('refX', 6)
            .attr('markerWidth', 3)
            .attr('markerHeight', 3)
            .attr('orient', 'auto')
            .append('svg:path')
            .attr('d', 'M0,-5L10,0L0,5')
            .attr('fill', '#000');

        svg.append('svg:defs').append('svg:marker')
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
        var drag_line = svg.append('svg:path')
            .attr('class', 'link dragline hidden')
            .attr('d', 'M0,0L0,0');

        function resetMouseVars() {
            mousedown_node = null;
            mouseup_node = null;
            mousedown_link = null;
        }

        start(jsonObj);
        restart();

        //brush.call(d3.svg.brush()
        //    .x(d3.scale.identity().domain([0, width]))
        //    .y(d3.scale.identity().domain([0, height]))
        //    .on("brushstart", function(d) {
        //        console.log("brushstart");
        //        node.each(function(d) { d.previouslySelected = shiftKey && d.selected; });
        //    })
        //    .on("brush", function() {
        //        var extent = d3.event.target.extent();
        //        node.classed("selected", function(d) {
        //            return d.selected = d.previouslySelected ^
        //            (extent[0][0] <= d.x && d.x < extent[1][0]
        //            && extent[0][1] <= d.y && d.y < extent[1][1]);
        //        });
        //    })
        //    .on("brushend", function() {
        //        d3.event.target.clear();
        //        d3.select(this).call(d3.event.target);
        //    }));

        function dblclick(d) {
            d3.select(this).classed("fixed", d.fixed = false);
        }

        function dblclickLabel() {
            var _old_value = d3.select(this).html();
            var _old_label = d3.select(this),
                _x_pos = d3.select(this).attr("x"),
                _y_pos = d3.select(this).attr("y")-15;

            d3.select(this).html("");

            var new_input = svg.append("foreignObject");
            new_input.attr("x",_x_pos)
                .attr("y",_y_pos)
                .append("xhtml:form")
                .append("input")
                .attr("value", function() {
                    this.focus();
                    $(this).css("width","200px");
                    $(this).css("outline","none");
                    $(this).css("border","0px");
                    $(this).css("background","transparent");
                    $(this).addClass("edit-value-concept","transparent");
                    return _old_value;
                })
                .on("keydown", function(d){
                    d3.event.stopPropagation();
                    if (d3.event.keyCode == 13 && !d3.event.shiftKey){
                        this.blur();
                    }
                })
                .on("blur", function() {
                    _old_label.text(function(d) { return $(".edit-value-concept").val(); });
                    svg.select("foreignObject").remove();
                });
        }

        function dragstart(d) {
            d3.select(this).classed("fixed", d.fixed = true);
        }

        function tick() {
            link.attr("x1", function(d) { return d.source.x; })
                .attr("y1", function(d) { return d.source.y; })
                .attr("x2", function(d) { return d.target.x; })
                .attr("y2", function(d) { return d.target.y; });

            node.attr("cx", function(d) { return d.x; })
                .attr("cy", function(d) { return d.y; });

            label.attr("x", function(d) { return d.x+15; })
                .attr("y", function(d) { return d.y+5; });
        }

        function restart() {
            link = link.data(links);

            link.enter().insert("line", ".node")
                .attr("class", "link");

            node = node.data(nodes);

            node.enter().insert("circle", ".cursor")
                .attr("class", "node")
                .attr("r", 10)
                //.call(force.drag)
                .on("dblclick", dblclick)
                //.on("mousedown", function(d) {
                //
                //    if (!d.selected) { // Don't deselect on shift-drag.
                //        if (!shiftKey){
                //            node.classed("selected", function(p) { return p.selected = d === p; });
                //            label.classed("selected", function(p) { return p.selected = d === p; });
                //        }
                //        else{
                //            d3.select(this).classed("selected", d.selected = true);
                //        }
                //    }
                //
                //    mousedown_node = d;
                //    if(mousedown_node === selected_node) selected_node = null;
                //    else selected_node = mousedown_node;
                //    selected_link = null;
                //
                //    drag_line
                //        .style('marker-end', 'url(#end-arrow)')
                //        .classed('hidden', false)
                //        .attr('d', 'M' + mousedown_node.x + ',' + mousedown_node.y + 'L' + mousedown_node.x + ',' + mousedown_node.y);
                //
                //    restart();
                //
                //})
                .on('mousedown', function(d) {

                    if (!d.selected) { // Don't deselect on shift-drag.
                        if (!shiftKey){
                            node.classed("selected", function(p) { return p.selected = d === p; });
                            label.classed("selected", function(p) { return p.selected = d === p; });
                        }
                        else{
                            d3.select(this).classed("selected", d.selected = true);
                        }
                    }

                    console.log(d3.select(this).classed("selected"));

                    if(movement_allowed) return;

                    // select node
                    mousedown_node = d;
                    if(mousedown_node === selected_node) selected_node = null;
                    else selected_node = mousedown_node;
                    selected_link = null;

                    // reposition drag line
                    drag_line
                        .style('marker-end', 'url(#end-arrow)')
                        .classed('hidden', false)
                        .attr('d', 'M' + mousedown_node.x + ',' + mousedown_node.y + 'L' + mousedown_node.x + ',' + mousedown_node.y);

                    restart();
                })
                .on('mouseup', function(d) {
                    if(!mousedown_node) return;

                    // needed by FF
                    drag_line
                        .classed('hidden', true)
                        .style('marker-end', '');

                    // check for drag-to-self
                    mouseup_node = d;
                    if(mouseup_node === mousedown_node) { resetMouseVars(); return; }

                    // unenlarge target node
                    d3.select(this).attr('transform', '');

                    // add link to graph (update if exists)
                    // NB: links are strictly source < target; arrows separately specified by booleans
                    var source, target, direction;
                    if(mousedown_node.id < mouseup_node.id) {
                        source = mousedown_node;
                        target = mouseup_node;
                        direction = 'right';
                    } else {
                        source = mouseup_node;
                        target = mousedown_node;
                        direction = 'left';
                    }

                    var link;
                    link = links.filter(function(l) {
                        return (l.source === source && l.target === target);
                    })[0];

                    if(link) {
                        link[direction] = true;
                    } else {
                        link = {source: source, target: target, left: false, right: false};
                        link[direction] = true;
                        links.push(link);
                    }

                    // select new link
                    selected_link = link;
                    selected_node = null;
                    restart();
                })
                .on("mouseover", function(d){
                    d3.select(this).attr("r", "11");
                })
                .on("mouseout", function(d){
                    d3.select(this).attr("r", "10")
                });

            node.exit().remove();

            label = label.data(nodes);
            label.enter().append("text")
                //.call(force.drag)
                .on("dblclick", dblclickLabel)
                .attr("y", function(d) {
                    return d.y+20;
                })
                .attr("x", function(d) {
                    return d.x+10;
                })
                .text(function(d){
                    return d.name
                });

            label.exit().remove();

            force.start();
        }

        function mousemove() {
            if(!mousedown_node) return;

            // update drag line
            drag_line.attr('d', 'M' + mousedown_node.x + ',' + mousedown_node.y + 'L' + d3.mouse(this)[0] + ',' + d3.mouse(this)[1]);

            restart();
        }

        function start(jsonObj){
            var graph = JSON.parse(jsonObj)[0];

            graph.nodes.forEach(function(d){
                nodes.push(d);
            });

            if(graph.nodes.length > 1){
                graph.links.forEach(function(d) {
                    d.source = graph.nodes[d.source];
                    d.target = graph.nodes[d.target];
                    links.push({source: d.source, target: d.target})
                });

                link = link.data(links);
            }

            node = node.data(nodes);
            label = label.data(nodes)
                .enter().append("text")
                //.call(force.drag)
                .on("dblclick", dblclickLabel)
                .attr("y", function(d) {
                    return d.y+20;
                })
                .attr("x", function(d) {
                    return d.x+10;
                })
                .text(function(d){
                    return d.name
                });

        }

        $(".move-concept, .link-concept").click(function(e){
            e.preventDefault();
            if(!movement_allowed){
                $(".move-concept").addClass("btn-warning");
                $(".move-concept").removeClass("btn-primary");
                $(".link-concept").addClass("btn-primary");
                $(".link-concept").removeClass("btn-warning");

                movement_allowed = true;

                label.call(force.drag);
                node.call(force.drag);

            }else{
                $(".move-concept").addClass("btn-primary");
                $(".move-concept").removeClass("btn-warning");
                $(".link-concept").addClass("btn-warning");
                $(".link-concept").removeClass("btn-primary");

                movement_allowed = false;

                label.on("mousedown.drag", null);
                node.on("mousedown.drag", null);

            }
            console.log(movement_allowed);
        });

        $(".add-concept").click(function(e){
            e.preventDefault();

            $(this).parent().append('<input type="email" placeholder="New concept text here..." class="btn m-xs has-error pull-right .form-control add-concept add-concept-value">');
            $('input.add-concept-value').keypress(function (e) {
                var key = e.which;
                if(key == 13){
                    createNode();
                    $('input.add-concept-value').remove();
                }
            });

        });

        $(".remove-concept").click(function(e){
            e.preventDefault();
            //console.log(node.filter(function(d) { return d.selected; }));

            //var a = node.filter(function(d) { return console.log(d); d.selected; });

            console.log(node.filter(function(d) { return d.selected; }).length);

            //a.forEach(function(node){
            //
            //    console.log(node);
            //    console.log(nodes.indexOf(node), nodes, node);
            //    nodes.splice(nodes.indexOf(node), 1);
            //    var toSplice = links.filter(function(l) {
            //        return (l.source === node || l.target === node);
            //    });
            //    toSplice.map(function(l) {
            //        links.splice(links.indexOf(l), 1);
            //    });
            //});

            //if(!selected_node && !selected_link) return;
            //
            //if (selected_node) {
            //    nodes.splice(nodes.indexOf(selected_node), 1);
            //    spliceLinksForNode(selected_node);
            //} else if (selected_link) {
            //    links.splice(links.indexOf(selected_link), 1);
            //}
            //selected_link = null;
            //selected_node = null;
            restart();

        });

        function spliceLinksForNode(node) {

        }

        function createNode(){
            node.filter(function(d,i) {
                // We only need to information of one of them
                // todo What happen when there is no nodes
                if(i==0){
                    var newResponse = new Response({ generalItemId: d.gItem, responseValue: $('input.add-concept-value').val() , runId: d.runId, userEmail: 0 });
                    newResponse.save({}, {
                        beforeSend:setHeader,
                        success: function(r, response){

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
                            console.log(nodes);

                            restart();
                        }
                    });
                }

                return d.selected;
            });
        }

    }
});

// =====================================================

window.InquiryLeftSidebarView = Backbone.View.extend({
    tagName: "div",
    className: "col-md-2 pull-left",
    initialize:function () {
        this.template = _.template(tpl.get('inquiry_left_sidebar'));
        console.log("load inquiry left sidebar");
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
window.MessageFromNotificationView = Backbone.View.extend({
    tagName:  "div",
    className: "right",
    initialize:function (a) {
        this.template = _.template(tpl.get('message_own'));
    },
    render:function () {
        $(this.el).html(this.template(this.model));
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
        $(this.el).html(this.template(this.model));
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
        $(this.el).html(this.template(this.model));
        return this;
    }
});
//
//
//window.ActivityView = Backbone.View.extend({
//    tagName: 'section',
//    className: 'phase-detail box box-success box-solid',
//    initialize:function (xhr) {
//        if(xhr.model.type.indexOf("VideoObject") > -1){
//            this.template = _.template(tpl.get('activity_video'));
//        }else if(xhr.model.type.indexOf("OpenBadge") > -1) {
//            this.template = _.template(tpl.get('activity_widget'));
//        }else if(xhr.model.type.indexOf("AudioObject") > -1) {
//            this.template = _.template(tpl.get('activity_discussion'));
//        }else if(xhr.model.type.indexOf("MultipleChoiceImageTest") > -1) {
//            this.template = _.template(tpl.get('activity_tree_view'));
//        }else{
//            this.template = _.template(tpl.get('activity_detail'));
//        }
//    },
//
//    render:function () {
//        $(this.el).html(this.template(this.model));
//        $(this.el).find('#nestable3').nestable();
//        return this;
//    }
//});

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

window.NotificationSideBarView = Backbone.View.extend({
    tagName:  "li",
    initialize:function () {
        this.template = _.template(tpl.get('notification_sidebar'));
    },
    render:function () {
        $(this.el).html(this.template(this.model));
        return this;
    }
});
