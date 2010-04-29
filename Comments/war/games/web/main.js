var attempts = 0;
var correct = 0;

function populatePage() {
	window.name = "main";
	div = document.getElementById("word_1");
	wordIndex = Math.floor(Math.random() * words.length);
	div.innerHTML = words[wordIndex];

	images = setupImageNames(words[wordIndex]);
	document.getElementById("img_1").src = images[0];
	document.getElementById("img_2").src = images[1];
	document.getElementById("img_3").src = images[2];
	document.getElementById("img_4").src = images[3];
	document.getElementById("img_5").src = images[4];
	document.getElementById("img_6").src = images[5];
}

function setupImageNames(notNamed) {
	indexes = getRandomList(6,notNamed);
	images = [ imageName(words[indexes[0]]),
			imageName(words[indexes[1]]),
			imageName(words[indexes[2]]),
			imageName(words[indexes[3]]),
			imageName(words[indexes[4]]),
			imageName(words[indexes[5]]) ];

	images[Math.floor(Math.random() * images.length)] = imageName(notNamed);
	return images;
}

function selection(img) {
	attempts++;
	if (img.src.substring(img.src.lastIndexOf("/") + 1) == document
			.getElementById("word_1").innerHTML
			+ ".jpg") {
		correct++;
		window.open("correct.html", "_blank", "", "", true);
	} else
		window.open("incorrect.html", "_blank", "", "", true);
}