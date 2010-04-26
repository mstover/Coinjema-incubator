var hexSize = 36;
var gameBoard = new Board(21, 16, hexSize, "#game");
var gameDialog = $("<div id='dialog'><li><b>q</b> cancel move</li><li><b>m</b> toggle rotational or translational movement</li></div>");
gameDialog.css("font-size", "8pt");
$(function() {
	$("#controlPanel").css( {
		"position" : "absolute",
		"top" : "0px",
		"left" : ((((hexSize* (29.5 / 40)) * 20)+20+hexSize) + "px")
	});
	$("#zoom_in").bind("click",function(e,args){
		hexSize += 4;
		gameBoard.hexSize = hexSize;
		$("#controlPanel").css("left",((((hexSize* (29.5 / 40)) * 20)+20+hexSize) + "px"));
		gameBoard.repaint();
	});
	$("#zoom_out").bind("click",function(e,args){
		hexSize -= 4;
		gameBoard.hexSize = hexSize;
		$("#controlPanel").css("left",((((hexSize* (29.5 / 40)) * 20)+20+hexSize) + "px"));
		gameBoard.repaint();
	});
	gameDialog.appendTo("body");
	gameDialog.dialog( {
		autoOpen : false,
		width : 150,
		height : 75,
		title : "Move Help",
		position : 'top'
	});
	gameDialog.dialog("open");
	gameDialog.dialog("close");
	gameBoard.drawHexes();
	gameBoard.addShape(new Shape("red", new Hex(6, 1), [ S ]));
	gameBoard.addShape(new Shape("red", new Hex(6, 4), [ S ]));
	gameBoard.addShape(new Shape("red", new Hex(6, 7), [ S ]));
	gameBoard.addShape(new Shape("red", new Hex(6, 10), [ S ]));
	gameBoard.addShape(new Shape("red", new Hex(6, 13), [ S ]));
	gameBoard.addShape(new General("red", new Hex(2, 3)));
	gameBoard.addShape(new General("red", new Hex(2, 12)));
	gameBoard
			.addShape(new Shape("red", new Hex(1, 3), [ NW, N ]));
	gameBoard.addShape(new Shape("red", new Hex(1, 11),
			[ SW, S ]));
	gameBoard.addShape(new Shape("red", new Hex(0, 0), [ SE, SE,
			SE ]));
	gameBoard.addShape(new Shape("red", new Hex(0, 15), [ NE,
			NE, NE ]));
	gameBoard
			.addShape(new Shape("red", new Hex(4, 4), [ S, SW ]));
	gameBoard.addShape(new Shape("red", new Hex(4, 11),
			[ N, NW ]));
	gameBoard.addShape(new Shape("red", new Hex(2, 7), [ S, NE,
			NE, S ]));
	gameBoard
			.addShape(new Shape("red", new Hex(0, 7), [ N, NE ]));
	gameBoard
			.addShape(new Shape("red", new Hex(0, 8), [ S, SE ]));

	addGreenPieces();
});

function addGreenPieces() {

	gameBoard
			.addShape(new Shape("green", new Hex(14, 1), [ S ]));
	gameBoard
			.addShape(new Shape("green", new Hex(14, 4), [ S ]));
	gameBoard
			.addShape(new Shape("green", new Hex(14, 7), [ S ]));
	gameBoard
			.addShape(new Shape("green", new Hex(14, 10), [ S ]));
	gameBoard
			.addShape(new Shape("green", new Hex(14, 13), [ S ]));
	gameBoard.addShape(new General("green", new Hex(18, 3)));
	gameBoard.addShape(new General("green", new Hex(18, 12)));
	gameBoard.addShape(new Shape("green", new Hex(19, 3), [ NE,
			N ]));
	gameBoard.addShape(new Shape("green", new Hex(19, 11), [ SE,
			S ]));
	gameBoard.addShape(new Shape("green", new Hex(20, 0), [ SW,
			SW, SW ]));
	gameBoard.addShape(new Shape("green", new Hex(20, 15), [ NW,
			NW, NW ]));
	gameBoard.addShape(new Shape("green", new Hex(16, 11), [ N,
			NE ]));
	gameBoard.addShape(new Shape("green", new Hex(16, 4), [ S,
			SE ]));
	gameBoard.addShape(new Shape("green", new Hex(16, 7), [ S,
			NE, NE, S ]));
	gameBoard.addShape(new Shape("green", new Hex(20, 7), [ N,
			NW ]));
	gameBoard.addShape(new Shape("green", new Hex(20, 8), [ S,
			SW ]));
}

function checkForCapture(board, shapes) {
	var capturedShapes = {};
	for ( var i in shapes) {
		var shape = shapes[i];
		shape
				.mapHexes(function(hex) {
					for ( var i = 0; i < 6; i++) {
						var adj = board.getAdjacentHex(hex, i);
						var hexes = [];
						while (board.isOpposingPiece(adj, shape)) {
							hexes.splice(hexes.length, 0, adj);
							adj = board.getAdjacentHex(adj, i);
						}
						if (hexes.length > 0
								&& (board
										.isSamePiece(adj, shape) || !board
										.isLegalCoordinates(adj))) {
							for ( var h in hexes) {
								var s = board
										.getShapeInHex(hexes[h]);
								capturedShapes[s.name] = s;
							}
						}
						hexes = [];
					}
					return hex;
				});
	}
	for ( var c in capturedShapes) {
		alert("# captured shapes = " + capturedShapes[c].name);
		board.removeShape(capturedShapes[c]);
	}
	
	
}

GameClass = {		
				
}

function Game(redPlayer,greenPlayer) {
	this.redPlayer = redPlayer;
	this.greenPlayer = greenPlayer;
	this.currentMover = "red";
}