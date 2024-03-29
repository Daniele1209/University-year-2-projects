function open() {
    $('#box').show();
}

function close() {
    $('#box').hide();
}

$(document).ready(function () {
    $('#close').click(function () {
        close();
    });
    $('#open').click(function () {
        open();
    });

});

function handle_mousedown(e) {
    e.preventDefault()
    window.my_dragging = {};
    my_dragging.pageX0 = e.pageX;
    my_dragging.pageY0 = e.pageY;
    my_dragging.elem = this;
    my_dragging.offset0 = $(this).offset();


    function handle_dragging(e) {
        var left = my_dragging.offset0.left + (e.pageX - my_dragging.pageX0);
        var top = my_dragging.offset0.top + (e.pageY - my_dragging.pageY0);
        $(my_dragging.elem).offset({top: top, left: left});
    }

    function handle_mouseup(e) {
        $("#box").off('mousemove', handle_dragging)
            .off('mouseup', handle_mouseup);
    }

    $("#box").on('mouseup', handle_mouseup)
        .on('mousemove', handle_dragging);

}

$('#container').mousedown(handle_mousedown);


