var attempts = 0;
var correct = 0;

function populatePage() {
	window.name = "imgtoword";
	image = document.getElementById("test_image_1");
	wordIndex = Math.floor(Math.random() * words.length);
	image.src = imageName(words[wordIndex]);

	var testWords = setupWords(words[wordIndex]);
	document.getElementById("word_1").innerHTML = testWords[0];
	document.getElementById("word_2").innerHTML = testWords[1];
	document.getElementById("word_3").innerHTML = testWords[2];
	document.getElementById("word_4").innerHTML = testWords[3];
	document.getElementById("word_5").innerHTML = testWords[4];
	document.getElementById("word_6").innerHTML = testWords[5];
}

function setupWords(notNamed) {
	indexes = getRandomList(6,notNamed);
	var testWords = [ words[indexes[0]],
			words[indexes[1]],
			words[indexes[2]],
			words[indexes[3]],
			words[indexes[4]],
			words[indexes[5]] ];

	testWords[Math.floor(Math.random() * testWords.length)] = notNamed;
	return testWords;
}

function selection(img) {
	attempts++;
	src = document
	.getElementById("test_image_1").src;
	src = src.substring(src.lastIndexOf("/")+1);
	if (img.innerHTML+".jpg" == src) {
		correct++;
		window.open("correct.html", "_blank", "", "", true);
	} else
		window.open("incorrect.html", "_blank", "", "", true);
}