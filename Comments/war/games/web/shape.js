var N = 0, NE = 1, SE = 2, S = 3, SW = 4, NW = 5;

function General(color, startHex) {
	general = new Shape(color, startHex, []);
	general.drawPiece = __drawGeneralPiece;
	general.isGeneral = true;
	return general;
}

function Shape(color, startHex, directions) {
	this.directions = directions;
	this.startHex = startHex;
	this.hexes = [ 1 ];
	this.hexes[0] = this.startHex;
	this.color = color;
	this.drawShape = __drawShape;
	this.reverseShape = __reverseShape;
	this.getEndHex = __getEndHex;
	this.mapDirections = __mapDirections;
	this.nextHex = __nextHex;
	this.drawPiece = __drawPiece;
	this.occupies = __occupies;
	this.occupiesHex = __occupiesHex;
	this.mapHexes = __mapHexes;
	this.redefineShape = __redefineShape;
	this.isAdjacentToGeneral = __isAdjacentToGeneral;
	this.isAdjacentTo = __isAdjacentTo;
	this.isGeneral = false;
	this.moveTo = __moveTo;
	this.resetTo = __resetTo;
	this.copy = __copy;
	this.name = "piece_" + nextId();
	return this;
}

function __resetTo(shape) {
	for ( var i in this.directions)
		this.directions[i] = shape.directions[i];
	this.hexes = [ shape.hexes.length ];
	for ( var i in shape.hexes) {
		this.hexes[i] = shape.hexes[i];
	}
}

function __copy() {
	var dir = [ this.directions.length ];
	for ( var i in this.directions)
		dir[i] = this.directions[i];
	var newShape = new Shape(this.color, this.startHex, dir);
	newShape.hexes = [ this.hexes.length ];
	for ( var i in this.hexes) {
		newShape.hexes[i] = this.hexes[i];
	}
	return newShape;
}

function __drawShape(board) {
	board.drawShape(this);
}

function __reverseShape(apex) {
	var index = 0;
	while (index < this.hexes.length
			&& !apex.equals(this.hexes[index])) {
		index++;
	}
	var newDirections = this.directions.slice(0, index)
			.reverse();
	for ( var i = 0; i < newDirections.length; i++) {
		newDirections[i] = (newDirections[i] + 3) % 6;
	}
	return new Shape(this.color, apex, newDirections);
}

function __occupies(shape) {
	var occupies = false;
	this.mapHexes(function(shapeHex) {
		shape.mapHexes(function(testHex) {
			if (shapeHex.equals(testHex)) {
				occupies = true;
			}
			return testHex;
		});
		return shapeHex;
	});
	return occupies;
}

function __occupiesHex(testHex) {
	var occupies = false;
	this.mapHexes(function(shapeHex) {
		if (shapeHex.equals(testHex)) {
			occupies = true;
		}
		return shapeHex;
	});
	return occupies;
}

function __getEndHex() {
	return this.hexes[this.hexes.length - 1];
}

function __redefineShape() {
	this.hexes = [ 1 ];
	this.hexes[0] = this.startHex;
}

function __nextHex(hex) {
	this.hexes.splice(this.hexes.length, 0, hex);
}

function __mapHexes(executor) {
	for ( var hex in this.hexes) {
		this.hexes[hex] = executor(this.hexes[hex]);
	}
}

function __mapDirections(executor) {
	for ( var dir in this.directions) {
		this.directions[dir] = executor(this.directions[dir]);
	}
}

function __drawGeneralPiece(board, hex, id) {
	var point = board.getHex(hex);
	var img = $("<img class='piece " + this.color + " "
			+ this.name + "' src='images/" + this.color
			+ "_general_piece.png' width='" + board.hexSize
			+ "' style='position:absolute;top:" + point.y
			+ "px;left:" + point.x + "px;' id='" + this.name
			+ "_" + id + "'>");
	return img;
}

function __drawPiece(board, hex, id) {
	var point = board.getHex(hex);
	var img = $("<img class='piece " + this.color + " "
			+ this.name + "' src='images/" + this.color
			+ "_piece.png' width='" + board.hexSize
			+ "' style='position:absolute;top:" + point.y
			+ "px;left:" + point.x + "px;' id='" + this.name
			+ "_" + id + "'>");
	return img;
}

/**
 * Move a shape to a new set of hexes.
 * 
 * @param board
 * @param newHexes
 * @return
 */
function __moveTo(board, newHexes) {
	for ( var hex in this.hexes) {
		var piece = $("#" + this.name + "_" + hex);
		var newLoc = board.getHex(newHexes[hex]);
		piece.animate( {
			"left" : newLoc.x + "px",
			"top" : newLoc.y + "px"
		}, 400);
	}
}

function __isAdjacentTo(board,shape) {
	var isAdj = false;
	var thisShape = this;
	this.mapHexes(function(hex) {
		for ( var i = 0; i < 6; i++) {
			var adj = board.getAdjacentHex(hex, i);
			var adjShape = board.getShapeInHex(adj);
			if (adjShape != undefined && adjShape == shape) {
				isAdj = true;
			}
		}
		return hex;
	});
	return isAdj;
}

function __isAdjacentToGeneral(board) {
	var isGen = false;
	var thisShape = this;
	this.mapHexes(function(hex) {
		for ( var i = 0; i < 6; i++) {
			var adj = board.getAdjacentHex(hex, i);
			var adjShape = board.getShapeInHex(adj);
			if (adjShape != undefined && adjShape != thisShape
					&& adjShape.isGeneral
					&& adjShape.color == thisShape.color) {
				isGen = true;
			}
		}
		return hex;
	});
	return isGen;
}

var id_generator = 0;

function nextId() {
	return id_generator++;
}

LinkedShapeClass = {
	addShape : function(shape) {
		this.allShapes.splice(this.allShapes.length, 0, shape);
	}
};

function LinkedShape(general) {
	this.general = general;
	this.allShapes = [ 1 ];
	this.allShapes[0] = general;
	this.addShape = LinkedShapeClass.addShape;
	return this;
}
