/*
 * Author: Angel Suarez
 * Date: 31 August 2015
 * Description:
 *      
 **/
"use strict";

$(function () {

  	//$(".knob").knob();

    ////////////////////////////////////
    // Extra information hovering phases
    ////////////////////////////////////
    $("#inquiry-explanation").children().hide();
    $( "#inquiry-explanation .general").show();
    $("#circlemenu .question, #circlemenu .method, #circlemenu .collection, #circlemenu .analysis, #circlemenu .interpretation, #circlemenu .communication").hover(
      function(e) {
        $("#inquiry-explanation .general").hide();
        $("#inquiry-explanation ."+$(e.toElement).attr('class')).show();
      }, function() {
        $( "#inquiry-explanation").children().hide();
        $( "#inquiry-explanation .general").show();
      }
    );
    $("a#show-runs").click(function(event){
        event.preventDefault();
    });
});