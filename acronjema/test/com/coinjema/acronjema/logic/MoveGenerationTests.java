package com.coinjema.acronjema.logic;

import static com.coinjema.acronjema.logic.SquareDesignation.a1;
import static com.coinjema.acronjema.logic.SquareDesignation.a2;
import static com.coinjema.acronjema.logic.SquareDesignation.a3;
import static com.coinjema.acronjema.logic.SquareDesignation.a7;
import static com.coinjema.acronjema.logic.SquareDesignation.a8;
import static com.coinjema.acronjema.logic.SquareDesignation.b1;
import static com.coinjema.acronjema.logic.SquareDesignation.b3;
import static com.coinjema.acronjema.logic.SquareDesignation.b4;
import static com.coinjema.acronjema.logic.SquareDesignation.b5;
import static com.coinjema.acronjema.logic.SquareDesignation.b6;
import static com.coinjema.acronjema.logic.SquareDesignation.b8;
import static com.coinjema.acronjema.logic.SquareDesignation.c1;
import static com.coinjema.acronjema.logic.SquareDesignation.c2;
import static com.coinjema.acronjema.logic.SquareDesignation.c3;
import static com.coinjema.acronjema.logic.SquareDesignation.c5;
import static com.coinjema.acronjema.logic.SquareDesignation.c7;
import static com.coinjema.acronjema.logic.SquareDesignation.c8;
import static com.coinjema.acronjema.logic.SquareDesignation.d1;
import static com.coinjema.acronjema.logic.SquareDesignation.d2;
import static com.coinjema.acronjema.logic.SquareDesignation.d3;
import static com.coinjema.acronjema.logic.SquareDesignation.d5;
import static com.coinjema.acronjema.logic.SquareDesignation.d6;
import static com.coinjema.acronjema.logic.SquareDesignation.d7;
import static com.coinjema.acronjema.logic.SquareDesignation.e1;
import static com.coinjema.acronjema.logic.SquareDesignation.e2;
import static com.coinjema.acronjema.logic.SquareDesignation.e3;
import static com.coinjema.acronjema.logic.SquareDesignation.e4;
import static com.coinjema.acronjema.logic.SquareDesignation.e6;
import static com.coinjema.acronjema.logic.SquareDesignation.e7;
import static com.coinjema.acronjema.logic.SquareDesignation.e8;
import static com.coinjema.acronjema.logic.SquareDesignation.f1;
import static com.coinjema.acronjema.logic.SquareDesignation.f2;
import static com.coinjema.acronjema.logic.SquareDesignation.f4;
import static com.coinjema.acronjema.logic.SquareDesignation.f7;
import static com.coinjema.acronjema.logic.SquareDesignation.f8;
import static com.coinjema.acronjema.logic.SquareDesignation.g1;
import static com.coinjema.acronjema.logic.SquareDesignation.g3;
import static com.coinjema.acronjema.logic.SquareDesignation.g6;
import static com.coinjema.acronjema.logic.SquareDesignation.g7;
import static com.coinjema.acronjema.logic.SquareDesignation.g8;
import static com.coinjema.acronjema.logic.SquareDesignation.h1;
import static com.coinjema.acronjema.logic.SquareDesignation.h2;
import static com.coinjema.acronjema.logic.SquareDesignation.h7;
import static com.coinjema.acronjema.logic.SquareDesignation.xx;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MoveGenerationTests {
	static Board[] boards = {
			new Board(a1, a2, b1, c1, f1, g1, h1, h2, c2, f2, d1, g3, b3, xx,
					e1, c5, a8, a7, b8, c8, f8, g8, g7, h7, d7, e7, b6, e6, f7,
					xx, c7, e3),
			new Board(b3, xx, xx, xx, xx, xx, xx, xx, xx, xx, xx, xx, xx, xx,
					xx, d5, xx, xx, xx, xx, xx, xx, xx, xx, xx, xx, xx, xx, xx,
					xx, xx, c8),
			new Board(a1, xx, xx, xx, xx, xx, xx, xx, xx, xx, xx, xx, xx, xx,
					xx, b1, a8, xx, xx, xx, xx, xx, xx, xx, xx, xx, xx, xx, xx,
					xx, xx, b8),
			new Board(a1, a2, b1, c1, f1, g1, h1, h2, c2, e2, g3, xx, b3, xx,
					e1, d6, a8, a7, b8, c8, e8, f7, h7, b5, e7, xx, b6, g6, d7,
					xx, c7, e6),
			new Board(xx, xx, xx, xx, xx, xx, xx, xx, xx, xx, xx, xx, xx, xx,
					xx, e3, xx, xx, xx, xx, xx, xx, xx, xx, xx, xx, xx, xx, xx,
					xx, d3, xx),
			new Board(a3, c1, d1, e1, f1, g1, h2, xx, b4, xx, c2, xx, xx, xx,
					e4, d3, d2, e2, f2, f4, c3, xx, xx, xx, xx, xx, xx, xx, xx,
					xx, xx, b3) };
	// String text[] = {
	// "10w "+
	// " +-----------------+"+
	// "8| r r r     r r   |"+
	// "7| r   m c c h r r |"+
	// "6|   d     d       |"+
	// "5|     E           |"+
	// "4|                 |"+
	// "3|   H     e   D   |"+
	// "2| R   C     C   R |"+
	// "1| R R R D M R R R |"+
	// " +-----------------+"+
	// "   a b c d e f g h"+
	// "TS: 80"+
	// "",
	// "21w "+
	// " +-----------------+"+
	// "8| r r r  r   |"+
	// "7| r   m h c r   r |"+
	// "6|   d   E     d   |"+
	// "5|   r   |"+
	// "4|       |"+
	// "3|   H     e   D   |"+
	// "2| R   C   C     R |"+
	// "1| R R R   M R R R |"+
	// " +-----------------+"+
	// "   a b c d e f g h"+
	// "TS: 168"+
	// "",
	// "22w "+
	// " +-----------------+"+
	// "8| r r r  r   |"+
	// "7| r   m h c r   r |"+
	// "6|   d   E     d   |"+
	// "5| r     |"+
	// "4|       |"+
	// "3|   H     e   D   |"+
	// "2| R   C   C     R |"+
	// "1| R R R M   R R R |"+
	// " +-----------------+"+
	// "   a b c d e f g h"+
	// "TS: 176"+
	// "",
	// "6b "+
	// " +-----------------+"+
	// "8| r r r m   r r r |"+
	// "7| r   c     d   r |"+
	// "6|   h     c   h   |"+
	// "5|  d E  |"+
	// "4|       |"+
	// "3| H H     e   M   |"+
	// "2| D D R     C C   |"+
	// "1| R R R R   R R R |"+
	// " +-----------------+"+
	// "   a b c d e f g h"+
	// "TS: 52"+
	// "",
	// "23b "+
	// " +-----------------+"+
	// "8| r r r   m r r r |"+
	// "7| r   d     c   r |"+
	// "6| D   d E     h   |"+
	// "5| h   H c    |"+
	// "4|  C     H   |"+
	// "3|   R   e     M D |"+
	// "2|     R     C   R |"+
	// "1| R R    R R |"+
	// " +-----------------+"+
	// "   a b c d e f g h"+
	// "TS: 188"+
	// "",
	// "11w "+
	// " +-----------------+"+
	// "8| r r r     r r r |"+
	// "7|     c   r c     |"+
	// "6|   d    h   |"+
	// "5| h     |"+
	// "4|    E   d   |"+
	// "3|   C     e   H   |"+
	// "2| R   D D   C   R |"+
	// "1| R R R   M R R R |"+
	// " +-----------------+"+
	// "   a b c d e f g h"+
	// "TS: 88"+
	// "",
	// "18b "+
	// " +-----------------+"+
	// "8| r r r   m r r r |"+
	// "7| r   c d c d   r |"+
	// "6|   h    h   |"+
	// "5|       |"+
	// "4| R     E    |"+
	// "3|   H   e     H   |"+
	// "2| M   D C C D   R |"+
	// "1| R R R     R R R |"+
	// " +-----------------+"+
	// "   a b c d e f g h"+
	// "TS: 148"+
	// "",
	// "25w "+
	// " +-----------------+"+
	// "8| r r   m r   r r |"+
	// "7| r c d c   d r   |"+
	// "6|   h   e E   h   |"+
	// "5|       |"+
	// "4|     R |"+
	// "3|   H    H   |"+
	// "2|     D   D C C   |"+
	// "1| R R R   M   R R |"+
	// " +-----------------+"+
	// "   a b c d e f g h"+
	// "TS: 200"+
	// "",
	// "40w "+
	// " +-----------------+"+
	// "8| r r     m r   r |"+
	// "7| r c d h   d r R |"+
	// "6|  e E   r   |"+
	// "5|       |"+
	// "4|       |"+
	// "3|    D   H   |"+
	// "2|  D   C C   |"+
	// "1| R R R M     R R |"+
	// " +-----------------+"+
	// "   a b c d e f g h"+
	// "TS: 320"+
	// "",
	// "14b "+
	// " +-----------------+"+
	// "8| r r r m   r r r |"+
	// "7| r d d c     c r |"+
	// "6|   h      h |"+
	// "5|       |"+
	// "4|       |"+
	// "3|   H   e     H E |"+
	// "2| R D D C M C   R |"+
	// "1| R R R   R   R R |"+
	// " +-----------------+"+
	// "   a b c d e f g h"+
	// "TS: 116"+
	// "",
	// "24b "+
	// " +-----------------+"+
	// "8| r r r   m r r r |"+
	// "7| r d d c     c r |"+
	// "6|   h    h   |"+
	// "5|       |"+
	// "4|       |"+
	// "3| C H   e     R H |"+
	// "2| R D D E   C R R |"+
	// "1| R R R   M     R |"+
	// " +-----------------+"+
	// "   a b c d e f g h"+
	// "TS: 196"+
	// "",
	// "12w "+
	// " +-----------------+"+
	// "8| r e   |"+
	// "7|       |"+
	// "6|       |"+
	// "5|       |"+
	// "4|       |"+
	// "3|       |"+
	// "2|       |"+
	// "1| R E   |"+
	// " +-----------------+"+
	// "   a b c d e f g h"+
	// "TS: 44"+
	// "",
	// "48b "+
	// " +-----------------+"+
	// "8| r    D r   |"+
	// "7| r     r    |"+
	// "6|   c   |"+
	// "5|      c     |"+
	// "4| R r h      |"+
	// "3|     D r     E   |"+
	// "2|     C d R R d R |"+
	// "1|  R  R |"+
	// " +-----------------+"+
	// "   a b c d e f g h"+
	// "",
	// "48b "+
	// " +-----------------+"+
	// "8| r    D r   |"+
	// "7| r     r    |"+
	// "6|   c   |"+
	// "5|      c     |"+
	// "4| R r h      |"+
	// "3|    r e E   |"+
	// "2|   D C d R R d R |"+
	// "1|    R     R |"+
	// " +-----------------+"+
	// "   a b c d e f g h"+
	// "",
	// "20w "+
	// " +-----------------+"+
	// "8| r r   m h   r   |"+
	// "7| r   M     r   r |"+
	// "6| r     E    |"+
	// "5|   R      H |"+
	// "4|     e |"+
	// "3| D  C     R |"+
	// "2|     H C   R     |"+
	// "1| R R R  R R |"+
	// " +-----------------+"+
	// "   a b c d e f g h"+
	// "",
	// "16w "+
	// " +-----------------+"+
	// "8|     e      |"+
	// "7| R     |"+
	// "6|       |"+
	// "5|      E     |"+
	// "4|       |"+
	// "3|  R    |"+
	// "2|       |"+
	// "1|       |"+
	// " +-----------------+"+
	// "   a b c d e f g h"+
	// "", // Endgame
	// "16b "+
	// " +-----------------+"+
	// "8|     e      |"+
	// "7|       |"+
	// "6|       |"+
	// "5|      E     |"+
	// "4|       |"+
	// "3|  R    |"+
	// "2|       |"+
	// "1|       |"+
	// " +-----------------+"+
	// "   a b c d e f g h"+
	// "", // Endgame
	// "16w "+
	// " +-----------------+"+
	// "8|     e      |"+
	// "7|       |"+
	// "6|       |"+
	// "5|      E     |"+
	// "4|       |"+
	// "3|  R    |"+
	// "2|       |"+
	// "1|       |"+
	// " +-----------------+"+
	// "   a b c d e f g h"+
	// "", // Endgame
	// "26b "+
	// " +-----------------+"+
	// "8| r r r r r   r r |"+
	// "7| M E d h    |"+
	// "6| h D C c c   R H |"+
	// "5| R R R R r m   e |"+
	// "4|   H R |"+
	// "3|       |"+
	// "2|    R  |"+
	// "1|       |"+
	// " +-----------------+"+
	// "   a b c d e f g h"+
	// "",
	// "46b "+
	// " +-----------------+"+
	// "8|       |"+
	// "7|       |"+
	// "6|       |"+
	// "5|       |"+
	// "4|    r   r   |"+
	// "3| r   r   E   D r |"+
	// "2| R M e R m     R |"+
	// "1|   h R      |"+
	// " +-----------------+"+
	// "   a b c d e f g h"+
	// "",
	// "61w "+
	// " +-----------------+"+
	// "8| r H r      |"+
	// "7|   m E      |"+
	// "6|   r R d c   r   |"+
	// "5|   R   R R   R r |"+
	// "4| R D e R   r C r |"+
	// "3|       |"+
	// "2|       |"+
	// "1|       |"+
	// " +-----------------+"+
	// "   a b c d e f g h"+
	// "",
	// "58b "+
	// " +-----------------+"+
	// "8|       |"+
	// "7|       |"+
	// "6|       |"+
	// "5|       |"+
	// "4|   C    M r |"+
	// "3| R e r E    |"+
	// "2|     D r r r   R |"+
	// "1|     R R R R R   |"+
	// " +-----------------+"+
	// "   a b c d e f g h"+
	// "",
	// "40b "+
	// " +-----------------+"+
	// "8|    r   r r |"+
	// "7| r   c |"+
	// "6|    D  |"+
	// "5|   H   R    |"+
	// "4|      M     |"+
	// "3|   m      H |"+
	// "2|   E e R  r |"+
	// "1|   R R |"+
	// " +-----------------+"+
	// "   a b c d e f g h"+
	// "",
	// "63w "+
	// " +-----------------+"+
	// "8| r r     r   H   |"+
	// "7|     c r R h   r |"+
	// "6|   d  H R   |"+
	// "5|   e   |"+
	// "4|   h E  r C |"+
	// "3|   d R c     R   |"+
	// "2|     R      |"+
	// "1|       |"+
	// " +-----------------+"+
	// "   a b c d e f g h"+
	// "",
	// "63w "+
	// " +-----------------+"+
	// "8|     r |"+
	// "7|   E   M   C     |"+
	// "6|       |"+
	// "5|     D   H   D   |"+
	// "4|  H   C     |"+
	// "3|   R   |"+
	// "2|     R   R   R   |"+
	// "1|   R   R   R   R |"+
	// " +-----------------+"+
	// "   a b c d e f g h"+
	// "",
	// "63w "+
	// " +-----------------+"+
	// "8|     r |"+
	// "7|   E   H   C     |"+
	// "6|       |"+
	// "5|     D   H   D   |"+
	// "4|  M   C     |"+
	// "3|   R   |"+
	// "2|     R   R   R   |"+
	// "1|   R   R   R   R |"+
	// " +-----------------+"+
	// "   a b c d e f g h"+
	// "",
	// "63w "+
	// " +-----------------+"+
	// "8|     r |"+
	// "7|   C   H   E     |"+
	// "6|       |"+
	// "5|     D   H   D   |"+
	// "4|  M   C     |"+
	// "3|   R   |"+
	// "2|     R   R   R   |"+
	// "1|   R   R   R   R |"+
	// " +-----------------+"+
	// "   a b c d e f g h"+
	// "",
	// "63w "+
	// " +-----------------+"+
	// "8|     r |"+
	// "7|   C   H   E     |"+
	// "6|       |"+
	// "5|     D   H   D   |"+
	// "4|  M   C     |"+
	// "3|   R   |"+
	// "2|     R   R   R   |"+
	// "1|   R   R   R   R |"+
	// " +-----------------+"+
	// "   a b c d e f g h"+
	// "",
	// "63w "+
	// " +-----------------+"+
	// "8|     r |"+
	// "7|   C   H   E     |"+
	// "6|       |"+
	// "5|     D   M   D   |"+
	// "4|  H   C     |"+
	// "3|   R   |"+
	// "2|     R   R   R   |"+
	// "1|   R   R   R   R |"+
	// " +-----------------+"+
	// "   a b c d e f g h"+
	// "",
	// "63w "+
	// " +-----------------+"+
	// "8|     r |"+
	// "7|   C   H   E     |"+
	// "6|       |"+
	// "5|     D   M   D   |"+
	// "4|  C   H     |"+
	// "3|   R   |"+
	// "2|     R   R   R   |"+
	// "1|   R   R   R   R |"+
	// " +-----------------+"+
	// "   a b c d e f g h"+
	// "",
	// "63w "+
	// " +-----------------+"+
	// "8|     r |"+
	// "7|   E   H   C     |"+
	// "6|       |"+
	// "5|     D   M   D   |"+
	// "4|  C   H     |"+
	// "3|   R   |"+
	// "2|     R   R   R   |"+
	// "1|   R   R   R   R |"+
	// " +-----------------+"+
	// "   a b c d e f g h"+
	// "",
	// "63w "+
	// " +-----------------+"+
	// "8|     r |"+
	// "7| E     H   C     |"+
	// "6|       |"+
	// "5|     D   M   D   |"+
	// "4|  C   H     |"+
	// "3|   R   |"+
	// "2|     R   R   R   |"+
	// "1|   R   R   R   R |"+
	// " +-----------------+"+
	// "   a b c d e f g h"
	// };

	long correctMoveCount[] = { 20229, 18, 79, 20203, 91, 141, 18569, 25293,
			17386, 23695, 22409, 31866, 17622, 10232, 14361, 8596, 16477,
			48665, 358, 150, 3598, 161, 2549, 2030, 3045, 336488, 337916,
			337922, 337922, 338030, 338034, 338034, 315908, };

	@Test
	public void moveGen1() throws Exception {

		MoveTree tree = new MoveTree(boards[1]);
		StepTree stepBuffer = new StepTree(boards[1], tree);

		boards[1].print(System.out);
		stepBuffer.searchForSteps(false, 4, Move.EMPTY_MOVE);
		int numMovesGenerated = tree.getFirstNumber();
		assertEquals(correctMoveCount[1], numMovesGenerated);
	}

	@Test
	public void moveGen2() throws Exception {
		MoveTree tree = new MoveTree(boards[2]);
		StepTree stepBuffer = new StepTree(boards[2], tree);
		boards[2].print(System.out);
		stepBuffer.searchForSteps(true, 4, Move.EMPTY_MOVE);
		int numMovesGenerated = tree.getFirstNumber();
		assertEquals(correctMoveCount[2], numMovesGenerated);
	}

	@Test
	public void moveGen4() throws Exception {
		MoveTree tree = new MoveTree(boards[4]);
		StepTree stepBuffer = new StepTree(boards[4], tree);
		boards[4].print(System.out);
		stepBuffer.searchForSteps(true, 4, Move.EMPTY_MOVE);
		int numMovesGenerated = tree.getFirstNumber();
		assertEquals(correctMoveCount[4], numMovesGenerated);
	}

	@Test
	public void moveGen5() throws Exception {
		MoveTree tree = new MoveTree(boards[5]);
		StepTree stepBuffer = new StepTree(boards[5], tree);
		boards[5].print(System.out);
		stepBuffer.searchForSteps(false, 4, Move.EMPTY_MOVE);
		int numMovesGenerated = tree.getFirstNumber();
		assertEquals(correctMoveCount[5], numMovesGenerated);
	}

	@Test
	public void moveGen0() throws Exception {
		MoveTree tree = new MoveTree(boards[0]);
		StepTree stepBuffer = new StepTree(boards[0], tree);
		boards[0].print(System.out);
		stepBuffer.searchForSteps(true, 4, Move.EMPTY_MOVE);
		int numMovesGenerated = tree.getFirstNumber();
		assertEquals(correctMoveCount[0], numMovesGenerated);
	}
}
