var words = [ "car", "book", "truck", "boat", "cat", "dog",
		"pig", "tree", "chair", "hat", "snake", "elephant",
		"mouse", "house", "rat", "bat", "ball", "moon", "star",
		"door", "tiger", "lion", "bear", "crocodile", "fish",
		"snail", "duck", "frog", "egg", "bed", "ear", "nose",
		"hair", "hand", "water", "girl", "boy", "baby", "bird",
		"king", "queen", "teeth", "foot" ];




function imageName(word) {
	return "images/" + word + ".jpg";
}

function rndIndex() {
	return Math.floor(Math.random() * words.length);
}

function getRandomList(len,notNamed) {
	indexes = [ Math.floor(Math.random() * words.length),
				Math.floor(Math.random() * words.length),
				Math.floor(Math.random() * words.length),
				Math.floor(Math.random() * words.length),
				Math.floor(Math.random() * words.length),
				Math.floor(Math.random() * words.length) ];
		problemIndex = checkIndex(indexes, notNamed);
		while (problemIndex < len) {
			indexes[problemIndex] = Math.floor(Math.random()
					* words.length);
			problemIndex = checkIndex(indexes, notNamed);
		}
		return indexes;
}

function checkIndex(indexes, notNamed) {
	ind = 0;
	problem = false;
	while (!problem && ind < indexes.length) {
		if (words[indexes[ind]] == notNamed) {
			problem = true;
		} else {
			for (i = 0; i < ind; i++) {
				if (indexes[i] == indexes[ind]) {
					problem = true;
				}
			}
		}
		if (!problem)
			ind++;
	}
	return ind;
}