/**
 *
 */

var imgAr = [
    "/img/404-01.jpg",
    "/img/404-02.jpg",
    "/img/404-03.jpg",
    "/img/404-04.jpg",
    "/img/404-05.jpg",
    "/img/404-06.jpg",
    "/img/404-07.jpg",
    "/img/404-08.jpg",
    "/img/404-09.jpg",
    "/img/404-10.jpg",
    "/img/404-11.jpg",
    "/img/404-12.jpg",
    "/img/404-13.jpg",
    "/img/404-14.jpg",
    "/img/404-15.jpg",
    "/img/404-16.jpg",
    "/img/404-17.jpg"];

/*function test()
{console.log('Test');}*/

function getRandomImage() {
    var num = Math.floor( Math.random() * imgAr.length );
    document.getElementById("randImage").innerHTML = ('<img src="' + imgAr[num] + '" />')
};
