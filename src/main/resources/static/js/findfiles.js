$(document).ready(function(){

    $( "#tabs" ).tabs();
    $('#i_file').change( function(event) {
        var tmppath = URL.createObjectURL(event.target.files[0]);
        $("img").fadeIn("fast").attr('src',URL.createObjectURL(event.target.files[0]));

        $("#disp_tmp_path").html("Temporary Path(Copy it and try pasting it in browser address bar) --> <strong>["+tmppath+"]</strong>");
    });

});

function getfolder(e) {
    var files = e.target.files;
    console.log(files[0]);
    var path = files[0].webkitRelativePath;
    console.log(path);
    var Folder = path.split("/");
    console.log(Folder[0]);
}