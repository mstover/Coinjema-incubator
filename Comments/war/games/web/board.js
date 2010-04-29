Board = function(columns, rows, hexSize, selectorToDrawTo) {
	return new Board.fn.init(columns, rows, hexSize,
			selectorToDrawTo);
};

Board.fn = Board.prototype = {
	init : function(columns, rows, hexSize, selectorToDrawTo) {
		this.cols = columns;
		this.rows = rows;
		this.hexSize = hexSize;
		this.selector = selectorToDrawTo;
		for (i = 0; i < columns; i++) {
			this.squares[i] = [ rows ];
		}
		return this;
	},

	cols : 0,
	rows : 0,
	hexSize : 0,
	selector : "",
	/**
	 * Records the actual pixel coordinates of the hexes
	 */
	squares : [],
	shapes : [],
	hexColors : [ "white", "green", "yellow" ],
	
	repaint : function() {
		$("#"+this.selector+" img").remove();
		this.drawHexes();
		for(var shape in this.shapes) {
			this.drawShape(this.shapes[shape]);
		}
	},

	drawHexes : function() {
		var hexColorIndex = 0;
		for (j = 0; j < this.rows; j++) {
			hexColorIndex = hexColorIndex
					% this.hexColors.length;
			var topBase = j * (34.5 / 40) * this.hexSize;
			for (i = 0; i < this.cols; i++) {
				var colorIndex = (hexColorIndex + (i % 2))
						% this.hexColors.length;
				var left = i * (29.5 / 40) * this.hexSize;
				var top = (i % 2 == 1) ? topBase + (16.75 / 40)
						* this.hexSize : topBase;
				this.squares[i][j] = new Hex(left, top);
				$(
						"<img src='images/"
								+ this.hexColors[colorIndex]
								+ "_hex.png' width='"
								+ this.hexSize
								+ "' style='position:absolute;top:"
								+ top + "px;left:" + left
								+ "px;'>").appendTo(
						this.selector);
			}
			$("<br/>").appendTo(this.selector);
			hexColorIndex += 2;
		}
	},

	isLegalCoordinates : function(hex) {
		return !(hex.x < 0 || hex.x >= this.squares.length
				|| hex.y < 0 || hex.y >= this.squares[0].length);
	},

	/**
	 * Returns a Point object with x and y properties yielding the pixel
	 * coordinates of the top-left corner of the desired hex.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	getHex : function(hex) {
		if (!this.isLegalCoordinates(hex)) {
			return new Hex(-100, -100);
		} else {
			return this.squares[hex.x][hex.y];
		}
	},

	moveShape : function(shape, direction, visible,
			legalityMatters) {
		var vis = (visible == undefined) ? true : visible;
		var legMat = (legalityMatters == undefined) ? true
				: legalityMatters;
		var copy = shape.copy();
		var thisBoard = this;
		shape.mapHexes(function(hex) {
			return thisBoard.getAdjacentHex(hex, direction);
		});
		if (vis) {
			if (!legMat || isLegal(shape)) {
				shape.moveTo(this, shape.hexes);
			} else {
				shape.resetTo(copy);
			}
		}
	},

	rotateDirection : function(dir, numRot, clockwise) {
		if (clockwise) {
			return (dir + numRot) % 6;
		} else {
			return (dir + (6 - numRot)) % 6;
		}
	},

	getPath : function(fromHex, toHex) {
		var inbetween = fromHex;
		var res = [];
		while (inbetween.y != toHex.y) {
			if (inbetween.y > toHex.y) {
				res.splice(res.length, 0, N);
				inbetween = this.getAdjacentHex(inbetween, N);
			} else {
				res.splice(res.length, 0, S);
				inbetween = this.getAdjacentHex(inbetween, S);
			}
		}
		while (inbetween.x != toHex.x) {
			if (inbetween.x > toHex.x) {
				var d = inbetween.x % 2 == 0 ? SW : NW;
				res.splice(res.length, 0, d);
				inbetween = this.getAdjacentHex(inbetween, d);
			} else {
				var d = inbetween.x % 2 == 0 ? SE : NE;
				res.splice(res.length, 0, d);
				inbetween = this.getAdjacentHex(inbetween, d);
			}
		}
		return res;
	},

	/**
	 * Rotate a piece around an apex point.
	 * 
	 * @param shape
	 * @param apex
	 * @param clockwise
	 * @param numRotations
	 * @return
	 */
	rotate : function(shape, apex, clockwise, numRotations,
			visible, legalityMatters) {
		if (numRotations < 1)
			return shape;
		var legMat = (legalityMatters == undefined) ? true
				: legalityMatters;
		var copy = shape.copy();
		var vis = (visible == undefined) ? true : visible;
		var numRot = (numRotations) ? numRotations : 1;
		var thisBoard = this;
		shape.mapDirections(function(dir) {
			return thisBoard.rotateDirection(dir, numRot,
					clockwise);
		});
		var path = this.getPath(apex, shape.hexes[0]);
		for ( var i in path) {
			path[i] = this.rotateDirection(path[i], numRot,
					clockwise);
		}
		var newHex = apex;
		for ( var i in path) {
			newHex = this.getAdjacentHex(newHex, path[i]);
		}
		shape.startHex = newHex;

		this.drawShape(shape, false);
		if (vis) {
			if (!legMat || this.isLegal(shape)) {
				shape.moveTo(this, shape.hexes);
			} else {
				shape.resetTo(copy);
			}
		}
		return shape;
	},

	hideShape : function(shape) {
		$("img." + shape.name).remove();
	},

	removeShape : function(shape) {
		this.hideShape(shape);
		for (s in this.shapes) {
			if (this.shapes[s] == shape) {
				this.shapes.splice(s, 1);
			}
		}
	},

	drawShape : function(shape, v) {
		shape.redefineShape();
		var visible = (v != undefined) ? v : true;
		var point = shape.startHex;
		if (visible) {
			var img = shape.drawPiece(this, point, 0);
			img.appendTo(this.selector);
			img.bind("click", {
				board : this
			}, this.selectShapeListener);
		}
		var x = point.x;
		var y = point.y;
		var thisBoard = this;
		var pieceCount = 1;
		shape.mapDirections(function(dir) {
			var newHex = thisBoard.getAdjacentHex(new Hex(x, y),
					dir);
			x = newHex.x;
			y = newHex.y;
			if (visible) {
				thisBoard.drawShapePart(newHex, shape,
						pieceCount++);
			}
			shape.nextHex(newHex);
			return dir;
		});
		return shape;
	},

	getAdjacentHex : function(hex, dir) {
		var newHex = new Hex(hex.x, hex.y);
		switch (dir) {
		case N:
			newHex.y--;
			break;
		case NE:
			if (newHex.x % 2 == 0)
				newHex.y--;
			newHex.x++;
			break;
		case SE:
			if (newHex.x % 2 == 1)
				newHex.y++;
			newHex.x++;
			break;
		case S:
			newHex.y++;
			break;
		case SW:
			if (newHex.x % 2 == 1)
				newHex.y++;
			newHex.x--;
			break;
		case NW:
			if (newHex.x % 2 == 0)
				newHex.y--;
			newHex.x--;
			break;
		}
		return newHex;
	},

	drawShapePart : function(hex, shape, id) {
		var img = shape.drawPiece(this, hex, id);
		img.appendTo(this.selector);
		img.bind("click", {
			board : this
		}, this.selectShapeListener);
	},

	writeToHex : function(hex, words) {
		point = this.getHex(hex);
		$(
				"<div style='font-weight:bold;padding:0px;margin:0px;position:absolute;top:"
						+ (point.y + this.hexSize / 2)
						+ "px;left:"
						+ (point.x + this.hexSize / 2)
						+ "px;'><img src='images/dot.png'></div>")
				.appendTo(this.selector);
	},

	/**
	 * Add a shape to the board. It will record it's presence on the board for
	 * determining collisions and it will immediately draw the shape.
	 * 
	 * @param shape
	 * @return
	 */
	addShape : function(shape) {
		this.drawShape(shape, false);
		if (!this.isLegal(shape)) {
			return false;
		}
		this.drawShape(shape);
		this.shapes.splice(this.shapes.length, 0, shape);
		return true;
	},

	isLegal : function(shape) {
		for (i in shape.hexes) {
			if (!this.isLegalCoordinates(shape.hexes[i])) {
				return false;
			}
		}
		for (s in this.shapes) {
			if (this.shapes[s] != shape
					&& this.shapes[s].occupies(shape)) {
				return false;
			}
		}
		return true;
	},

	rotator : undefined,
	grouper : undefined,

	getShapeFromName : function(name) {
		for (i in this.shapes) {
			if (this.shapes[i].name == name) {
				return this.shapes[i];
			}
		}
		return null;
	},

	getShapeHex : function(shape, num) {
		var index = parseInt(num);
		return shape.hexes[index];
	},

	mostRecentRelevantPiece : undefined,

	getShapeInHex : function(hex) {
		var rel = undefined;
		if (this.mostRecentRelevantPiece != undefined
				&& this.mostRecentRelevantPiece.occupiesHex(hex)) {
			return this.mostRecentRelevantPiece;
		}
		for ( var i in this.shapes) {
			if (this.shapes[i].occupiesHex(hex)) {
				rel = this.shapes[i];
			}
		}
		if (rel != undefined) {
			this.mostRecentRelevantPiece = rel;
		}
		return rel;
	},

	isSamePiece : function(hex, shape) {
		var rel = this.getShapeInHex(hex);
		if (rel != undefined) {
			if (rel.color == shape.color)
				return true;
		}
		return false;
	},

	isOpposingPiece : function(hex, shape) {
		var rel = this.getShapeInHex(hex);
		if (rel != undefined) {
			if (rel.color != shape.color)
				return true;
		}
		return false;
	},

	selectShapeListener : function(e, arg2) {
		this.focus();
		var res = false;
		var id = e.target.getAttribute("id");
		var name = id.substring(0, id.lastIndexOf("_"));
		var board = e.data.board;
		var shape = board.getShapeFromName(name);
		if (board.rotator == undefined
				&& board.grouper == undefined) {
			if (e.pageY < window.innerHeight / 2) {
				gameDialog
						.dialog('option', 'position', 'bottom');
			} else {
				gameDialog.dialog('option', 'position', 'top');
			}
			gameDialog.dialog('open');
			if (shape.isGeneral) {
				$("#dialog")
						.append(
								"<li id='general_adj_info'><b>This piece is a general.  Please select pieces to group with it</b></li>");
				board.grouper = new Grouper(board, shape);
				$("img." + name).css("opacity", "0.5");
			} else {
				board.rotator = new Rotator(board, shape, board
						.getShapeHex(shape, id.substring(id
								.lastIndexOf("_") + 1)));
				$("img." + name).css("opacity", "0.5");
				board.rotator.begin();
			}
		} else {
			res = true;
		}
		return res;
	}

};

Board.fn.init.prototype = Board.fn;

function Rotator(board, shapes, hex, rotationDoneListener) {
	if (shapes.length != undefined) {
		this.shapes = shapes;
	} else {
		this.shapes = [ 1 ];
		this.shapes[0] = shapes;
	}
	this.shapeCopies = [ this.shapes.length ];
	for ( var i in this.shapes)
		this.shapeCopies[i] = this.shapes[i].copy();
	this.hex = hex;
	this.board = board;
	this.currentRotationLevel = 0;
	this.done = false;
	this.basePoint = new Hex(board.getHex(hex).x
			+ (board.hexSize / 2), board.getHex(hex).y
			+ (board.hexSize / 2));
	this.moveX = this.basePoint.x;
	this.moveY = this.basePoint.y;
	this.currentMove = -1;
	this.rotateMode = this.shapes.length > 1
			|| this.shapes[0].hexes.length > 1 ? true : false;
	this.currentlyRotating = false;
	this.rotationDoneListener = rotationDoneListener;

	/**
	 * 
	 * @param e
	 * @param arg
	 * @return
	 */
	this.rotationListener = function(e, arg) {
		if (this.done)
			return;
		var board = e.data.board;
		var rotator = e.data.rotator;
		if (rotator.currentlyRotating)
			return;
		rotator.currentlyRotating = true;
		if (rotator.rotateMode) {
			var distance = Math.sqrt(Math.pow(
					(e.pageX - rotator.basePoint.x), 2)
					+ Math.pow((e.pageY - rotator.basePoint.y),
							2));
			var factor = Math.floor(distance / board.hexSize);
			if (factor != rotator.currentRotationLevel) {
				if (factor > rotator.currentRotationLevel) {
					for ( var i in rotator.shapes) {
						board
								.rotate(
										rotator.shapes[i],
										rotator.hex,
										true,
										(factor - rotator.currentRotationLevel),
										true, false);
					}
				} else {
					for ( var i in rotator.shapes) {
						board
								.rotate(
										rotator.shapes[i],
										rotator.hex,
										false,
										(rotator.currentRotationLevel - factor),
										true, false);
					}
				}
				rotator.currentRotationLevel = factor;
			}
		} else {
			var diffX = e.pageX - rotator.basePoint.x;
			var diffY = e.pageY - rotator.basePoint.y;
			if ((Math.abs(e.pageX - rotator.moveX) > board.hexSize / 2 || Math
					.abs(e.pageY - rotator.moveY) > board.hexSize / 2)) {
				var moved = false;
				var distance = Math.sqrt(Math.pow(diffY, 2)
						+ Math.pow(diffX, 2));
				if (distance > board.hexSize) {
					if (e.pageY < rotator.basePoint.y) {
						if (Math.abs(diffX) < distance / 2) {
							moved = rotator.translateShape(N);
						} else if (diffX < 0) {
							moved = rotator.translateShape(NW);
						} else {
							moved = rotator.translateShape(NE);
						}
					} else {
						if (Math.abs(diffX) < distance / 2) {
							moved = rotator.translateShape(S);
						} else if (diffX < 0) {
							moved = rotator.translateShape(SW);
						} else {
							moved = rotator.translateShape(SE);
						}
					}
				} else {
					moved = rotator.translateShape(-1);
				}
				if (moved) {
					rotator.moveX = e.pageX;
					rotator.moveY = e.pageY;
				}
			}
		}
		rotator.currentlyRotating = false;
	};

	/**
	 * 
	 * @param e
	 * @param arg
	 * @return
	 */
	this.rotationStopper = function(e, arg) {
		if (this.done)
			return;
		var board = e.data.board;
		var rotator = e.data.rotator;
		if (e.shiftKey) {
			alert("Shift key held down");
		}
		rotator.stop();
		checkForCapture(board, rotator.shapes);
	};

	/**
	 * 
	 * @param e
	 * @param arg
	 * @return
	 */
	this.keyPress = function(e, arg) {
		if (this.done)
			return;
		var board = e.data.board;
		var rotator = e.data.rotator;
		var char = String.fromCharCode(e.charCode);
		if (char == 'q') {
			for ( var i in rotator.shapes) {
				rotator.shapes[i]
						.resetTo(rotator.shapeCopies[i]);
				rotator.shapes[i].moveTo(board,
						rotator.shapes[i].hexes);
			}
			rotator.stop();
		} else {
			if (char == 'm') {
				if (rotator.rotateMode) {
					for ( var i in rotator.shapes) {
						rotator.shapes[i]
								.resetTo(rotator.shapeCopies[i]);
						rotator.shapes[i].moveTo(board,
								rotator.shapes[i].hexes);
					}
					rotator.rotateMode = false;
				} else {
					for ( var i in rotator.shapes) {
						rotator.shapes[i]
								.resetTo(rotator.shapeCopies[i]);
						rotator.shapes[i].moveTo(board,
								rotator.shapes[i].hexes);
					}
					rotator.rotateMode = true;
				}
			}
		}
	};

	/**
	 * 
	 * @param dir
	 * @return
	 */
	this.translateShape = function(dir) {
		if (this.currentMove != dir) {
			for ( var i in this.shapes) {
				this.shapes[i].resetTo(this.shapeCopies[i]);
				this.board.moveShape(this.shapes[i], dir, true,
						false);
				this.currentMove = dir;
			}
			return true;
		}
		return false;
	};

	/**
	 * 
	 * @return
	 */
	this.begin = function() {
		$().bind("mousemove", {
			board : this.board,
			rotator : this
		}, this.rotationListener);
		$(this.board.selector).bind("click", {
			board : this.board,
			rotator : this
		}, this.rotationStopper);
		$().bind("keypress", {
			board : this.board,
			rotator : this
		}, this.keyPress);
	};

	this.stop = /**
				 * Stop the rotating
				 * 
				 * @return
				 */
	function() {
		if (this.done)
			return;
		$("#general_adj_info").remove();
		gameDialog.dialog("close");
		this.done = true;
		var isLegal = true;
		var board = this.board;
		jQuery.each(this.shapes, function() {
			if (!board.isLegal(this)) {
				isLegal = false;
			}
		});
		if (!isLegal) {
			alert("Not legal move!");
			for ( var i in this.shapes) {
				this.shapes[i].resetTo(this.shapeCopies[i]);
				this.shapes[i].moveTo(this.board,
						this.shapes[i].hexes);
			}
		}
		for ( var i in this.shapes) {
			$("img." + this.shapes[i].name).css("opacity", "1");
		}
		this.board.rotator = undefined;
		$().unbind("mousemove", this.rotationListener);
		$(this.board.selector).unbind("click",
				this.rotationStopper);
		$().unbind("keypress", this.keyPress);
		if (this.rotationDoneListener) {
			this.rotationDoneListener.stop();
		}
	};
}

/*******************************************************************************
 * Grouper Object
 ******************************************************************************/
GrouperClass = {
	selectShape : function(e, arg) {
		var id = e.target.getAttribute("id");
		var name = id.substring(0, id.lastIndexOf("_"));
		var board = e.data.board;
		var grouper = e.data.grouper;
		var shape = board.getShapeFromName(name);
		if (shape.isAdjacentTo(board, grouper.general)) {
			$("img." + name).css("opacity", "0.5");
			grouper.linkedShape.addShape(shape);
		}
	},

	keyPress : function(e, arg) {
		var board = e.data.board;
		var grouper = e.data.grouper;
		var char = String.fromCharCode(e.charCode);
		if (char == 'd') {
			$("#general_adj_info").remove();
			$("#dialog")
					.append(
							"<li id='grouper_info'>Choose Hex to rotate around - must be a hex within a chosen shape</li>");

			$(board.selector).unbind("click",
					grouper.selectShape);
			for ( var i in grouper.linkedShape.allShapes) {
				$("img." + grouper.linkedShape.allShapes[i].name)
						.bind("click", {
							board : board,
							grouper : grouper
						}, grouper.selectFulcrum);
			}
		} else {
			e.data.grouper.stop();
		}
	},

	selectFulcrum : function(e, arg) {
		var board = e.data.board;
		var grouper = e.data.grouper;
		var id = e.target.getAttribute("id");
		var name = id.substring(0, id.lastIndexOf("_"));
		var shape = board.getShapeFromName(name);
		var hex = board.getShapeHex(shape, id.substring(id
				.lastIndexOf("_") + 1));
		$("#grouper_info").remove();
		$("#dialog")
				.append(
						"<li id='grouper_info'>Now rotate entire linked shape normally");
		$().unbind("keypress", this.keyPress);
		for ( var i in grouper.linkedShape.allShapes) {
			$("img." + grouper.linkedShape.allShapes[i].name)
					.unbind("click", grouper.selectFulcrum);
		}
		var rotator = new Rotator(board,
				grouper.linkedShape.allShapes, hex, grouper);
		rotator.begin();
		return false;
	},

	stop : function() {
		$("#general_adj_info").remove();
		$("#grouper_info").remove();
		gameDialog.dialog("close");
		$(this.board.selector).unbind("click", this.selectShape);
		$().unbind("keypress", this.keyPress);
		for ( var i in this.linkedShape.allShapes) {
			$("img." + this.linkedShape.allShapes[i].name)
					.unbind("click", this.selectFulcrum);
		}
		$("img.piece").css("opacity", "1");
		this.board.grouper = undefined;
	},

	begin : function() {
		$(this.board.selector).bind("click", {
			board : this.board,
			grouper : this
		}, this.selectShape);
		$().bind("keypress", {
			board : this.board,
			grouper : this
		}, this.keyPress);
	}
};
function Grouper(board, shape) {
	this.board = board;
	this.general = shape;
	this.begin = GrouperClass.begin;
	this.stop = GrouperClass.stop;
	this.keyPress = GrouperClass.keyPress;
	this.selectShape = GrouperClass.selectShape;
	this.selectFulcrum = GrouperClass.selectFulcrum;
	this.linkedShape = new LinkedShape(this.general);
	this.begin();
	return this;
}

HexClass = {

	equals : function(h) {
		if (h.x == this.x && h.y == this.y)
			return true;
		else
			return false;
	},

	toString : function() {
		return "hex(" + this.x + "," + this.y + ")";
	}
};

function Hex(x, y) {
	this.x = x;
	this.y = y;
	this.equals = HexClass.equals;
	this.toString = HexClass.toString;
}
