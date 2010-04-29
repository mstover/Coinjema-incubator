

var itop = 100;
var ileft = 0;
var moveTop = 10;
var moveLeft = 10;

function bounce() {
	el = document.getElementById("happy_face");
	window.setTimeout('move(el)', 50);
}

function score() {
	score = window.opener.correct / window.opener.attempts;
	document.getElementById("score").innerHTML = "Score:"
			+ (100*score).toFixed(0) + "%";

}

function move(el) {
	width = el.offsetWidth;
	height = el.offsetHeight;
	if ((itop + height + 10) > window.innerHeight) {
		moveTop = -10;
	} else if (itop < 0) {
		moveTop = 10;
	}
	if ((width + ileft + 10) > window.innerWidth) {
		moveLeft = -10;
	} else if (ileft < 0) {
		moveLeft = 10;
	}
	ileft += moveLeft;
	itop += moveTop;
	el.style.left = ileft + "px";
	el.style.top = itop + "px";
	bounce();
}

function acknowledge() {
	window.close();
	window.opener.populatePage();
}