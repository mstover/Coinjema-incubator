package com.coinjema.acronjema.logic;

import static com.coinjema.acronjema.logic.SquareDesignation.a1;
import static com.coinjema.acronjema.logic.SquareDesignation.a2;
import static com.coinjema.acronjema.logic.SquareDesignation.a3;
import static com.coinjema.acronjema.logic.SquareDesignation.a4;
import static com.coinjema.acronjema.logic.SquareDesignation.a5;
import static com.coinjema.acronjema.logic.SquareDesignation.a6;
import static com.coinjema.acronjema.logic.SquareDesignation.a7;
import static com.coinjema.acronjema.logic.SquareDesignation.a8;
import static com.coinjema.acronjema.logic.SquareDesignation.b1;
import static com.coinjema.acronjema.logic.SquareDesignation.b2;
import static com.coinjema.acronjema.logic.SquareDesignation.b3;
import static com.coinjema.acronjema.logic.SquareDesignation.b4;
import static com.coinjema.acronjema.logic.SquareDesignation.b5;
import static com.coinjema.acronjema.logic.SquareDesignation.b6;
import static com.coinjema.acronjema.logic.SquareDesignation.b7;
import static com.coinjema.acronjema.logic.SquareDesignation.b8;
import static com.coinjema.acronjema.logic.SquareDesignation.c1;
import static com.coinjema.acronjema.logic.SquareDesignation.c2;
import static com.coinjema.acronjema.logic.SquareDesignation.c3;
import static com.coinjema.acronjema.logic.SquareDesignation.c4;
import static com.coinjema.acronjema.logic.SquareDesignation.c5;
import static com.coinjema.acronjema.logic.SquareDesignation.c6;
import static com.coinjema.acronjema.logic.SquareDesignation.c7;
import static com.coinjema.acronjema.logic.SquareDesignation.c8;
import static com.coinjema.acronjema.logic.SquareDesignation.d1;
import static com.coinjema.acronjema.logic.SquareDesignation.d2;
import static com.coinjema.acronjema.logic.SquareDesignation.d3;
import static com.coinjema.acronjema.logic.SquareDesignation.d4;
import static com.coinjema.acronjema.logic.SquareDesignation.d5;
import static com.coinjema.acronjema.logic.SquareDesignation.d6;
import static com.coinjema.acronjema.logic.SquareDesignation.d7;
import static com.coinjema.acronjema.logic.SquareDesignation.d8;
import static com.coinjema.acronjema.logic.SquareDesignation.e1;
import static com.coinjema.acronjema.logic.SquareDesignation.e2;
import static com.coinjema.acronjema.logic.SquareDesignation.e3;
import static com.coinjema.acronjema.logic.SquareDesignation.e4;
import static com.coinjema.acronjema.logic.SquareDesignation.e5;
import static com.coinjema.acronjema.logic.SquareDesignation.e6;
import static com.coinjema.acronjema.logic.SquareDesignation.e7;
import static com.coinjema.acronjema.logic.SquareDesignation.e8;
import static com.coinjema.acronjema.logic.SquareDesignation.f1;
import static com.coinjema.acronjema.logic.SquareDesignation.f2;
import static com.coinjema.acronjema.logic.SquareDesignation.f3;
import static com.coinjema.acronjema.logic.SquareDesignation.f4;
import static com.coinjema.acronjema.logic.SquareDesignation.f5;
import static com.coinjema.acronjema.logic.SquareDesignation.f7;
import static com.coinjema.acronjema.logic.SquareDesignation.f8;
import static com.coinjema.acronjema.logic.SquareDesignation.g1;
import static com.coinjema.acronjema.logic.SquareDesignation.g2;
import static com.coinjema.acronjema.logic.SquareDesignation.g3;
import static com.coinjema.acronjema.logic.SquareDesignation.g4;
import static com.coinjema.acronjema.logic.SquareDesignation.g5;
import static com.coinjema.acronjema.logic.SquareDesignation.g6;
import static com.coinjema.acronjema.logic.SquareDesignation.g7;
import static com.coinjema.acronjema.logic.SquareDesignation.g8;
import static com.coinjema.acronjema.logic.SquareDesignation.h1;
import static com.coinjema.acronjema.logic.SquareDesignation.h2;
import static com.coinjema.acronjema.logic.SquareDesignation.h3;
import static com.coinjema.acronjema.logic.SquareDesignation.h4;
import static com.coinjema.acronjema.logic.SquareDesignation.h5;
import static com.coinjema.acronjema.logic.SquareDesignation.h6;
import static com.coinjema.acronjema.logic.SquareDesignation.h7;
import static com.coinjema.acronjema.logic.SquareDesignation.h8;
import static com.coinjema.acronjema.logic.SquareDesignation.xx;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Test;

public class MoveGenerationTests {
	static Board[] boards = {
			new Board(a1, a2, b1, c1, f1, g1, h1, h2, c2, f2, d1, g3, b3, xx,
					e1, e5, a8, a7, b8, c8, f8, g8, g7, h7, d7, e7, b6, g6, f7,
					xx, c7, e3),
			new Board(a1, a2, b1, c1, f1, g1, h1, h2, c2, e2, xx, g3, b3, xx,
					e1, d6, a8, a7, b8, c8, f7, g8, g7, b5, xx, e7, b6, g6, d7,
					xx, c7, e3),
			new Board(a2, a1, b1, c1, f1, g1, h1, h2, c2, e2, g3, xx, b3, xx,
					d1, d6, a5, a7, a8, b8, c8, f7, g8, h7, e7, xx, b6, g6, d7,
					xx, c7, e3),

			// String text[] = {
			// "10w "+
			// " +-----------------+"+
			// "8| r r r Ê Ê r r Ê |"+
			// "7| r Ê m c c h r r |"+
			// "6| Ê d Ê Ê Ê Ê d Ê |"+
			// "5| Ê Ê Ê Ê E Ê Ê Ê |"+
			// "4| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// "3| Ê H Ê Ê e Ê D Ê |"+
			// "2| R Ê C Ê Ê C Ê R |"+
			// "1| R R R D M R R R |"+
			// " +-----------------+"+
			// " Ê a b c d e f g h"+
			// "TS: 80"+
			// "",
			// Ê Ê Ê Ê Ê Ê Ê Ê Ê"21w "+
			// " +-----------------+"+
			// "8| r r r Ê Ê Ê r Ê |"+
			// "7| r Ê m h c r Ê r |"+
			// "6| Ê d Ê E Ê Ê d Ê |"+
			// "5| Ê r Ê Ê Ê Ê Ê Ê |"+
			// "4| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// "3| Ê H Ê Ê e Ê D Ê |"+
			// "2| R Ê C Ê C Ê Ê R |"+
			// "1| R R R Ê M R R R |"+
			// " +-----------------+"+
			// " Ê a b c d e f g h"+
			// "TS: 168"+
			// "",
			// Ê Ê Ê Ê Ê Ê Ê Ê Ê"22w "+
			// " +-----------------+"+
			// "8| r r r Ê Ê Ê r Ê |"+
			// "7| r Ê m h c r Ê r |"+
			// "6| Ê d Ê E Ê Ê d Ê |"+
			// "5| r Ê Ê Ê Ê Ê Ê Ê |"+
			// "4| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// "3| Ê H Ê Ê e Ê D Ê |"+
			// "2| R Ê C Ê C Ê Ê R |"+
			// "1| R R R M Ê R R R |"+
			// " +-----------------+"+
			// " Ê a b c d e f g h"+
			// "TS: 176"+
			// "",
			// Ê Ê Ê Ê Ê Ê Ê Ê Ê"6b "+
			// " +-----------------+"+
			// "8| r r r m Ê r r r |"+
			// "7| r Ê c Ê Ê d Ê r |"+
			// "6| Ê h Ê Ê c Ê h Ê |"+
			// "5| Ê Ê Ê d E Ê Ê Ê |"+
			// "4| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// "3| H H Ê Ê e Ê M Ê |"+
			// "2| D D R Ê Ê C C Ê |"+
			// "1| R R R R Ê R R R |"+
			// " +-----------------+"+
			// " Ê a b c d e f g h"+
			// "TS: 52"+
			new Board(a1, b1, c1, c2, d1, f1, g1, h1, f2, g2, a2, b2, a3, b3,
					g3, e5, a7, a8, b8, c8, f8, g8, h8, h7, c7, e6, f7, d5, b6,
					g6, d8, e3),
			// "",
			// Ê Ê Ê Ê Ê Ê Ê Ê Ê"23b "+
			// " +-----------------+"+
			// "8| r r r Ê m r r r |"+
			// "7| r Ê d Ê Ê c Ê r |"+
			// "6| D Ê d E Ê Ê h Ê |"+
			// "5| h Ê H c Ê Ê Ê Ê |"+
			// "4| Ê Ê Ê C Ê Ê H Ê |"+
			// "3| Ê R Ê e Ê Ê M D |"+
			// "2| Ê Ê R Ê Ê C Ê R |"+
			// "1| R R Ê Ê Ê Ê R R |"+
			// " +-----------------+"+
			// " Ê a b c d e f g h"+
			// "TS: 188"+
			new Board(a1, b1, b3, c2, g1, h1, h2, xx, f2, d4, h3, a6, c5, g4,
					g3, d6, a7, a8, b8, c8, f8, g8, h8, h7, f7, d5, c7, c6, a5,
					g6, e8, d3),
			// "",
			// Ê Ê Ê Ê Ê Ê Ê Ê Ê"11w "+
			// " +-----------------+"+
			// "8| r r r Ê Ê r r r |"+
			// "7| Ê Ê c Ê r c Ê Ê |"+
			// "6| Ê d Ê Ê Ê Ê h Ê |"+
			// "5| h Ê Ê Ê Ê Ê Ê Ê |"+
			// "4| Ê Ê Ê Ê E Ê d Ê |"+
			// "3| Ê C Ê Ê e Ê H Ê |"+
			// "2| R Ê D D Ê C Ê R |"+
			// "1| R R R Ê M R R R |"+
			// " +-----------------+"+
			// " Ê a b c d e f g h"+
			// "TS: 88"+
			new Board(a1, a2, b1, c1, f1, g1, h1, h2, f2, b3, c2, d2, g3, xx,
					e1, e4, a8, b8, c8, f8, g8, h8, e7, xx, c7, f7, b6, g4, a5,
					g6, xx, e3),
			// "",
			// Ê Ê Ê Ê Ê Ê Ê Ê Ê"18b "+
			// " +-----------------+"+
			// "8| r r r Ê m r r r |"+
			// "7| r Ê c d c d Ê r |"+
			// "6| Ê h Ê Ê Ê Ê h Ê |"+
			// "5| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// "4| R Ê Ê E Ê Ê Ê Ê |"+
			// "3| Ê H Ê e Ê Ê H Ê |"+
			// "2| M Ê D C C D Ê R |"+
			// "1| R R R Ê Ê R R R |"+
			// " +-----------------+"+
			// " Ê a b c d e f g h"+
			// "TS: 148"+
			new Board(a1, b1, c1, a4, f1, g1, h1, h2, d2, e2, c2, f2, b3, g3,
					a2, d4, a7, a8, b8, c8, f8, g8, h8, h7, c7, e7, d7, f7, b6,
					g6, e8, d3),
			// "",
			// Ê Ê Ê Ê Ê Ê Ê Ê Ê"25w "+
			// " +-----------------+"+
			// "8| r r Ê m r Ê r r |"+
			// "7| r c d c Ê d r Ê |"+
			// "6| Ê h Ê e E Ê h Ê |"+
			// "5| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// "4| Ê Ê Ê Ê Ê Ê Ê R |"+
			// "3| Ê H Ê Ê Ê Ê H Ê |"+
			// "2| Ê Ê D Ê D C C Ê |"+
			// "1| R R R Ê M Ê R R |"+
			// " +-----------------+"+
			// " Ê a b c d e f g h"+
			// "TS: 200"+
			new Board(a1, b1, c1, g1, h1, h4, xx, xx, f2, g2, c2, e2, b3, g3,
					e1, e6, a7, a8, b8, e8, g8, g7, h8, xx, b7, d7, c7, f7, b6,
					g6, d8, d6),
			// "",
			// Ê Ê Ê Ê Ê Ê Ê Ê Ê"40w "+
			// " +-----------------+"+
			// "8| r r Ê Ê m r Ê r |"+
			// "7| r c d h Ê d r R |"+
			// "6| Ê Ê Ê e E Ê r Ê |"+
			// "5| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// "4| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// "3| Ê Ê Ê Ê D Ê H Ê |"+
			// "2| Ê Ê Ê D Ê C C Ê |"+
			// "1| R R R M Ê Ê R R |"+
			// " +-----------------+"+
			// " Ê a b c d e f g h"+
			// "TS: 320"+
			new Board(a1, b1, c1, g1, h1, h7, xx, xx, f2, g2, d2, e3, g3, xx,
					d1, e6, a7, a8, b8, f8, g7, g6, h8, xx, b7, xx, c7, f7, d7,
					xx, e8, d6),
			// "",
			// Ê Ê Ê Ê Ê Ê Ê Ê Ê"14b "+
			// " +-----------------+"+
			// "8| r r r m Ê r r r |"+
			// "7| r d d c Ê Ê c r |"+
			// "6| Ê h Ê Ê Ê Ê Ê h |"+
			// "5| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// "4| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// "3| Ê H Ê e Ê Ê H E |"+
			// "2| R D D C M C Ê R |"+
			// "1| R R R Ê R Ê R R |"+
			// " +-----------------+"+
			// " Ê a b c d e f g h"+
			// "TS: 116"+
			new Board(a1, a2, b1, c1, e1, g1, h1, h2, d2, f2, b2, c2, b3, g3,
					e2, h3, a7, a8, b8, c8, f8, g8, h8, h7, d7, g7, b7, c7, b6,
					h6, d8, d3),
			// "",
			// Ê Ê Ê Ê Ê Ê Ê Ê Ê"24b "+
			// " +-----------------+"+
			// "8| r r r Ê m r r r |"+
			// "7| r d d c Ê Ê c r |"+
			// "6| Ê h Ê Ê Ê Ê h Ê |"+
			// "5| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// "4| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// "3| C H Ê e Ê Ê R H |"+
			// "2| R D D E Ê C R R |"+
			// "1| R R R Ê M Ê Ê R |"+
			// " +-----------------+"+
			// " Ê a b c d e f g h"+
			// "TS: 196"+
			new Board(a1, a2, b1, c1, h1, h2, g2, g3, f2, a3, b2, c2, b3, h3,
					e1, d2, a7, a8, b8, c8, f8, g8, h8, h7, d7, g7, b7, c7, b6,
					g6, e8, d3),
			// "",
			// Ê Ê Ê Ê Ê Ê Ê Ê Ê"12w "+
			// " +-----------------+"+
			// "8| r e Ê Ê Ê Ê Ê Ê |"+
			// "7| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// "6| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// "5| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// "4| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// "3| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// "2| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// "1| R E Ê Ê Ê Ê Ê Ê |"+
			// " +-----------------+"+
			// " Ê a b c d e f g h"+
			// "TS: 44"+
			new Board(a1, xx, xx, xx, xx, xx, xx, xx, xx, xx, xx, xx, xx, xx,
					xx, b1, a8, xx, xx, xx, xx, xx, xx, xx, xx, xx, xx, xx, xx,
					xx, xx, b8),
			// "",
			// Ê Ê Ê Ê Ê Ê Ê Ê Ê"48b "+
			// " +-----------------+"+
			// "8| r Ê Ê Ê Ê D r Ê |"+
			// "7| r Ê Ê r Ê Ê Ê Ê |"+
			// "6| Ê c Ê Ê Ê Ê Ê Ê |"+
			// "5| Ê Ê Ê Ê Ê c Ê Ê |"+
			// "4| R r h Ê Ê Ê Ê Ê |"+
			// "3| Ê Ê D r Ê Ê E Ê |"+
			// "2| Ê Ê C d R R d R |"+
			// "1| Ê Ê Ê R Ê Ê Ê R |"+
			// " +-----------------+"+
			// " Ê a b c d e f g h"+
			// "",
			new Board(a4, d1, e2, f2, h2, h1, xx, xx, c2, xx, f8, c3, xx, xx,
					xx, g3, a8, a7, g8, d7, b4, d3, xx, xx, b6, f5, d2, g2, c4,
					xx, xx, xx),
			// Ê Ê Ê Ê Ê Ê Ê Ê Ê"48b "+
			// " +-----------------+"+
			// "8| r Ê Ê Ê Ê D r Ê |"+
			// "7| r Ê Ê r Ê Ê Ê Ê |"+
			// "6| Ê c Ê Ê Ê Ê Ê Ê |"+
			// "5| Ê Ê Ê Ê Ê c Ê Ê |"+
			// "4| R r h Ê Ê Ê Ê Ê |"+
			// "3| Ê Ê Ê Ê r e E Ê |"+
			// "2| Ê D C d R R d R |"+
			// "1| Ê Ê Ê Ê R Ê Ê R |"+
			// " +-----------------+"+
			// " Ê a b c d e f g h"+
			// "",
			new Board(a4, e1, e2, f2, h1, h2, xx, xx, c2, xx, b2, f8, xx, xx,
					xx, g3, a7, a8, b4, e3, g8, d7, xx, xx, b6, f5, g2, d2, c4,
					xx, xx, f3),
			// Ê Ê Ê Ê Ê Ê Ê Ê Ê"20w "+
			// " +-----------------+"+
			// "8| r r Ê m h Ê r Ê |"+
			// "7| r Ê M Ê Ê r Ê r |"+
			// "6| r Ê Ê E Ê Ê Ê Ê |"+
			// "5| Ê R Ê Ê Ê Ê Ê H |"+
			// "4| Ê Ê Ê Ê Ê Ê Ê e |"+
			// "3| D Ê Ê Ê C Ê Ê R |"+
			// "2| Ê Ê H C Ê R Ê Ê |"+
			// "1| R R R Ê Ê Ê R R |"+
			// " +-----------------+"+
			// " Ê a b c d e f g h"+
			// "",
			new Board(a1, b1, c1, b5, f2, g1, h1, h3, d2, e3, a3, xx, c2, h5,
					c7, d6, a6, a7, a8, b8, f7, g8, h7, xx, xx, xx, xx, xx, e8,
					xx, d8, h4),
			// Ê Ê Ê Ê Ê Ê Ê Ê Ê"16w "+
			// " +-----------------+"+
			// "8| Ê Ê e Ê Ê Ê Ê Ê |"+
			// "7| R Ê Ê Ê Ê Ê Ê Ê |"+
			// "6| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// "5| Ê Ê Ê Ê Ê E Ê Ê |"+
			// "4| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// "3| Ê Ê Ê R Ê Ê Ê Ê |"+
			// "2| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// "1| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// " +-----------------+"+
			// " Ê a b c d e f g h"+
			// "", // Endgame
			// Ê Ê Ê Ê Ê Ê Ê Ê Ê"16b "+
			// " +-----------------+"+
			// "8| Ê Ê e Ê Ê Ê Ê Ê |"+
			// "7| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// "6| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// "5| Ê Ê Ê Ê Ê E Ê Ê |"+
			// "4| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// "3| Ê Ê Ê R Ê Ê Ê Ê |"+
			// "2| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// "1| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// " +-----------------+"+
			// " Ê a b c d e f g h"+
			// "", // Endgame
			// Ê Ê Ê Ê Ê Ê Ê Ê Ê"16w "+
			// " +-----------------+"+
			// "8| Ê Ê e Ê Ê Ê Ê Ê |"+
			// "7| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// "6| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// "5| Ê Ê Ê Ê Ê E Ê Ê |"+
			// "4| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// "3| Ê Ê Ê R Ê Ê Ê Ê |"+
			// "2| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// "1| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// " +-----------------+"+
			// " Ê a b c d e f g h"+
			// "", // Endgame
			// Ê Ê Ê Ê Ê Ê Ê Ê Ê"26b "+
			// " +-----------------+"+
			// "8| r r r r r Ê r r |"+
			// "7| M E d h Ê Ê Ê Ê |"+
			// "6| h D C c c Ê R H |"+
			// "5| R R R R r m Ê e |"+
			// "4| Ê Ê Ê Ê Ê Ê H R |"+
			// "3| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// "2| Ê Ê Ê Ê R Ê Ê Ê |"+
			// "1| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// " +-----------------+"+
			// " Ê a b c d e f g h"+
			// "",
			// Ê Ê Ê Ê Ê Ê Ê Ê Ê"46b "+
			// " +-----------------+"+
			// "8| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// "7| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// "6| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// "5| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// "4| Ê Ê Ê Ê r Ê r Ê |"+
			// "3| r Ê r Ê E Ê D r |"+
			// "2| R M e R m Ê Ê R |"+
			// "1| Ê h R Ê Ê Ê Ê Ê |"+
			// " +-----------------+"+
			// " Ê a b c d e f g h"+
			// "",
			// Ê Ê Ê Ê Ê Ê Ê Ê Ê"61w "+
			// " +-----------------+"+
			// "8| r H r Ê Ê Ê Ê Ê |"+
			// "7| Ê m E Ê Ê Ê Ê Ê |"+
			// "6| Ê r R d c Ê r Ê |"+
			// "5| Ê R Ê R R Ê R r |"+
			// "4| R D e R Ê r C r |"+
			// "3| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// "2| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// "1| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// " +-----------------+"+
			// " Ê a b c d e f g h"+
			// "",
			// Ê Ê Ê Ê Ê Ê Ê Ê Ê"58b "+
			// " +-----------------+"+
			// "8| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// "7| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// "6| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// "5| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// "4| Ê C Ê Ê Ê Ê M r |"+
			// "3| R e r E Ê Ê Ê Ê |"+
			// "2| Ê Ê D r r r Ê R |"+
			// "1| Ê Ê R R R R R Ê |"+
			// " +-----------------+"+
			// " Ê a b c d e f g h"+
			// "",
			// Ê Ê Ê Ê Ê Ê Ê Ê Ê"40b "+
			// " +-----------------+"+
			// "8| Ê Ê Ê Ê r Ê r r |"+
			// "7| r Ê Ê Ê Ê Ê Ê c |"+
			// "6| Ê Ê Ê Ê D Ê Ê Ê |"+
			// "5| Ê H Ê R Ê Ê Ê Ê |"+
			// "4| Ê Ê Ê Ê Ê M Ê Ê |"+
			// "3| Ê m Ê Ê Ê Ê Ê H |"+
			// "2| Ê E e R Ê Ê Ê r |"+
			// "1| Ê Ê Ê Ê Ê Ê R R |"+
			// " +-----------------+"+
			// " Ê a b c d e f g h"+
			// "",
			// Ê Ê Ê Ê Ê Ê Ê Ê Ê"63w "+
			// " +-----------------+"+
			// "8| r r Ê Ê r Ê H Ê |"+
			// "7| Ê Ê c r R h Ê r |"+
			// "6| Ê d Ê Ê Ê H R Ê |"+
			// "5| Ê Ê Ê Ê Ê Ê e Ê |"+
			// "4| Ê h E Ê Ê Ê r C |"+
			// "3| Ê d R c Ê Ê R Ê |"+
			// "2| Ê Ê R Ê Ê Ê Ê Ê |"+
			// "1| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// " +-----------------+"+
			// " Ê a b c d e f g h"+
			// "",
			// Ê Ê Ê Ê Ê Ê Ê Ê Ê"63w "+
			// " +-----------------+"+
			// "8| Ê Ê Ê Ê Ê Ê Ê r |"+
			// "7| Ê E Ê M Ê C Ê Ê |"+
			// "6| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// "5| Ê Ê D Ê H Ê D Ê |"+
			// "4| Ê Ê Ê H Ê C Ê Ê |"+
			// "3| Ê R Ê Ê Ê Ê Ê Ê |"+
			// "2| Ê Ê R Ê R Ê R Ê |"+
			// "1| Ê R Ê R Ê R Ê R |"+
			// " +-----------------+"+
			// " Ê a b c d e f g h"+
			// "",
			// Ê Ê Ê Ê Ê Ê Ê Ê Ê"63w "+
			// " +-----------------+"+
			// "8| Ê Ê Ê Ê Ê Ê Ê r |"+
			// "7| Ê E Ê H Ê C Ê Ê |"+
			// "6| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// "5| Ê Ê D Ê H Ê D Ê |"+
			// "4| Ê Ê Ê M Ê C Ê Ê |"+
			// "3| Ê R Ê Ê Ê Ê Ê Ê |"+
			// "2| Ê Ê R Ê R Ê R Ê |"+
			// "1| Ê R Ê R Ê R Ê R |"+
			// " +-----------------+"+
			// " Ê a b c d e f g h"+
			// "",
			// Ê Ê Ê Ê Ê Ê Ê Ê Ê"63w "+
			// " +-----------------+"+
			// "8| Ê Ê Ê Ê Ê Ê Ê r |"+
			// "7| Ê C Ê H Ê E Ê Ê |"+
			// "6| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// "5| Ê Ê D Ê H Ê D Ê |"+
			// "4| Ê Ê Ê M Ê C Ê Ê |"+
			// "3| Ê R Ê Ê Ê Ê Ê Ê |"+
			// "2| Ê Ê R Ê R Ê R Ê |"+
			// "1| Ê R Ê R Ê R Ê R |"+
			// " +-----------------+"+
			// " Ê a b c d e f g h"+
			// "",
			// Ê Ê Ê Ê Ê Ê Ê Ê Ê"63w "+
			// " +-----------------+"+
			// "8| Ê Ê Ê Ê Ê Ê Ê r |"+
			// "7| Ê C Ê H Ê E Ê Ê |"+
			// "6| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// "5| Ê Ê D Ê H Ê D Ê |"+
			// "4| Ê Ê Ê M Ê C Ê Ê |"+
			// "3| Ê R Ê Ê Ê Ê Ê Ê |"+
			// "2| Ê Ê R Ê R Ê R Ê |"+
			// "1| Ê R Ê R Ê R Ê R |"+
			// " +-----------------+"+
			// " Ê a b c d e f g h"+
			// "",
			// Ê Ê Ê Ê Ê Ê Ê Ê Ê"63w "+
			// " +-----------------+"+
			// "8| Ê Ê Ê Ê Ê Ê Ê r |"+
			// "7| Ê C Ê H Ê E Ê Ê |"+
			// "6| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// "5| Ê Ê D Ê M Ê D Ê |"+
			// "4| Ê Ê Ê H Ê C Ê Ê |"+
			// "3| Ê R Ê Ê Ê Ê Ê Ê |"+
			// "2| Ê Ê R Ê R Ê R Ê |"+
			// "1| Ê R Ê R Ê R Ê R |"+
			// " +-----------------+"+
			// " Ê a b c d e f g h"+
			// "",
			// Ê Ê Ê Ê Ê Ê Ê Ê Ê"63w "+
			// " +-----------------+"+
			// "8| Ê Ê Ê Ê Ê Ê Ê r |"+
			// "7| Ê C Ê H Ê E Ê Ê |"+
			// "6| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// "5| Ê Ê D Ê M Ê D Ê |"+
			// "4| Ê Ê Ê C Ê H Ê Ê |"+
			// "3| Ê R Ê Ê Ê Ê Ê Ê |"+
			// "2| Ê Ê R Ê R Ê R Ê |"+
			// "1| Ê R Ê R Ê R Ê R |"+
			// " +-----------------+"+
			// " Ê a b c d e f g h"+
			// "",
			// Ê Ê Ê Ê Ê Ê Ê Ê Ê"63w "+
			// " +-----------------+"+
			// "8| Ê Ê Ê Ê Ê Ê Ê r |"+
			// "7| Ê E Ê H Ê C Ê Ê |"+
			// "6| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// "5| Ê Ê D Ê M Ê D Ê |"+
			// "4| Ê Ê Ê C Ê H Ê Ê |"+
			// "3| Ê R Ê Ê Ê Ê Ê Ê |"+
			// "2| Ê Ê R Ê R Ê R Ê |"+
			// "1| Ê R Ê R Ê R Ê R |"+
			// " +-----------------+"+
			// " Ê a b c d e f g h"+
			// "",
			// Ê Ê Ê Ê Ê Ê Ê Ê Ê"63w "+
			// " +-----------------+"+
			// "8| Ê Ê Ê Ê Ê Ê Ê r |"+
			// "7| E Ê Ê H Ê C Ê Ê |"+
			// "6| Ê Ê Ê Ê Ê Ê Ê Ê |"+
			// "5| Ê Ê D Ê M Ê D Ê |"+
			// "4| Ê Ê Ê C Ê H Ê Ê |"+
			// "3| Ê R Ê Ê Ê Ê Ê Ê |"+
			// "2| Ê Ê R Ê R Ê R Ê |"+
			// "1| Ê R Ê R Ê R Ê R |"+
			// " +-----------------+"+
			// " Ê a b c d e f g h"+
			// "" Ê
			new Board(b1, b3, c2, d1, e2, f1, g2, h1, d4, f7, c5, g5, f4, d7,
					e5, a7, h8, xx, xx, xx, xx, xx, xx, xx, xx, xx, xx, xx, xx,
					xx, xx, xx),
			new Board(b3, xx, xx, xx, xx, xx, xx, xx, xx, xx, xx, xx, xx, xx,
					xx, d5, xx, xx, xx, xx, xx, xx, xx, xx, xx, xx, xx, xx, xx,
					xx, xx, c8),
			new Board(a1, a2, b1, c1, f1, g1, h1, h2, c2, e2, g3, xx, b3, xx,
					e1, d6, a8, a7, b8, c8, e8, f7, h7, b5, e7, xx, b6, g6, d7,
					xx, c7, e6),
			new Board(xx, xx, xx, xx, xx, xx, xx, xx, xx, xx, xx, xx, xx, xx,
					xx, e3, xx, xx, xx, xx, xx, xx, xx, xx, xx, xx, xx, xx, xx,
					xx, d3, xx),
			new Board(a3, c1, d1, e1, f1, g1, h2, xx, b4, xx, c2, xx, xx, xx,
					e4, d3, d2, e2, f2, f4, c3, xx, xx, xx, xx, xx, xx, xx, xx,
					xx, xx, b3) };

	long correctMoveCount[] = { 20229, 20203, 18569, 25293, 17386, 23695,
			22409, 31866, 17622, 10232, 14361, 79, 8596, 16477, 48665, 358, 19,
			150, 3598, 161, 2549, 141, 2030, 3045, 336488, 337916, 337922,
			337922, 338030, 338034, 338034, 315908 };

	@Test
	public void moveGen2() throws Exception {
		MoveTree tree = new MoveTree(boards[2], new BaseEvaluator());
		StepTree stepBuffer = new StepTree(boards[2], tree);
		boards[2].print(System.out);
		long time = System.currentTimeMillis();
		stepBuffer.moveTree.searchForMoves(stepBuffer, true);
		System.out.println("Time = " + (System.currentTimeMillis() - time));
		int numMovesGenerated = tree.getFirstNumber();
		assertEquals(correctMoveCount[2], numMovesGenerated);
	}

	@Test
	public void moveGen4() throws Exception {
		MoveTree tree = new MoveTree(boards[4], new BaseEvaluator());
		StepTree stepBuffer = new StepTree(boards[4], tree);
		boards[4].print(System.out);
		long time = System.currentTimeMillis();
		stepBuffer.moveTree.searchForMoves(stepBuffer, false);
		System.out.println("Time = " + (System.currentTimeMillis() - time));
		int numMovesGenerated = tree.getFirstNumber();
		assertEquals(correctMoveCount[4], numMovesGenerated);
	}

	@Test
	public void moveGen3() throws Exception {
		int index = 3;
		MoveTree tree = new MoveTree(boards[index], new BaseEvaluator());
		StepTree stepBuffer = new StepTree(boards[index], tree);
		boards[index].print(System.out);
		long time = System.currentTimeMillis();
		stepBuffer.moveTree.searchForMoves(stepBuffer, false);
		System.out.println("Time = " + (System.currentTimeMillis() - time));
		int numMovesGenerated = tree.getFirstNumber();
		assertEquals(correctMoveCount[index], numMovesGenerated);
	}

	@Test
	public void moveGen5() throws Exception {
		MoveTree tree = new MoveTree(boards[5], new BaseEvaluator());
		StepTree stepBuffer = new StepTree(boards[5], tree);
		boards[5].print(System.out);
		long time = System.currentTimeMillis();
		stepBuffer.moveTree.searchForMoves(stepBuffer, true);
		System.out.println("Time = " + (System.currentTimeMillis() - time));
		int numMovesGenerated = tree.getFirstNumber();
		assertEquals(correctMoveCount[5], numMovesGenerated);
	}

	@Test
	public void moveGen0() throws Exception {
		MoveTree tree = new MoveTree(boards[0], new BaseEvaluator());
		StepTree stepBuffer = new StepTree(boards[0], tree);
		boards[0].print(System.out);
		long time = System.currentTimeMillis();
		stepBuffer.moveTree.searchForMoves(stepBuffer, true);
		System.out.println("Time = " + (System.currentTimeMillis() - time));
		int numMovesGenerated = tree.getFirstNumber();
		assertEquals(correctMoveCount[0], numMovesGenerated);
	}

	@Test
	public void moveGen1() throws Exception {

		MoveTree tree = new MoveTree(boards[1], new BaseEvaluator());
		StepTree stepBuffer = new StepTree(boards[1], tree);

		boards[1].print(System.out);
		long time = System.currentTimeMillis();
		stepBuffer.moveTree.searchForMoves(stepBuffer, true);
		System.out.println("Time = " + (System.currentTimeMillis() - time));
		int numMovesGenerated = tree.getFirstNumber();
		assertEquals(correctMoveCount[1], numMovesGenerated);
	}

	@Test
	public void moveGen6() throws Exception {
		int index = 6;
		MoveTree tree = new MoveTree(boards[index], new BaseEvaluator());
		StepTree stepBuffer = new StepTree(boards[index], tree);
		boards[index].print(System.out);
		long time = System.currentTimeMillis();
		stepBuffer.moveTree.searchForMoves(stepBuffer, false);
		System.out.println("Time = " + (System.currentTimeMillis() - time));
		int numMovesGenerated = tree.getFirstNumber();
		assertEquals(correctMoveCount[index], numMovesGenerated);
	}

	@Test
	public void moveGen7() throws Exception {
		int index = 7;
		MoveTree tree = new MoveTree(boards[index], new BaseEvaluator());
		StepTree stepBuffer = new StepTree(boards[index], tree);
		boards[index].print(System.out);
		long time = System.currentTimeMillis();
		stepBuffer.moveTree.searchForMoves(stepBuffer, true);
		System.out.println("Time = " + (System.currentTimeMillis() - time));
		int numMovesGenerated = tree.getFirstNumber();
		assertEquals(correctMoveCount[index], numMovesGenerated);
	}

	@Test
	public void moveGen8() throws Exception {
		int index = 8;
		MoveTree tree = new MoveTree(boards[index], new BaseEvaluator());
		StepTree stepBuffer = new StepTree(boards[index], tree);
		boards[index].print(System.out);
		long time = System.currentTimeMillis();
		stepBuffer.moveTree.searchForMoves(stepBuffer, true);
		System.out.println("Time = " + (System.currentTimeMillis() - time));
		int numMovesGenerated = tree.getFirstNumber();
		assertEquals(correctMoveCount[index], numMovesGenerated);
	}

	@Test
	public void moveGen9() throws Exception {
		int index = 9;
		MoveTree tree = new MoveTree(boards[index], new BaseEvaluator());
		StepTree stepBuffer = new StepTree(boards[index], tree);
		boards[index].print(System.out);
		long time = System.currentTimeMillis();
		stepBuffer.moveTree.searchForMoves(stepBuffer, false);
		System.out.println("Time = " + (System.currentTimeMillis() - time));
		int numMovesGenerated = tree.getFirstNumber();
		assertEquals(correctMoveCount[index], numMovesGenerated);
	}

	@Test
	public void moveGen10() throws Exception {
		int index = 10;
		MoveTree tree = new MoveTree(boards[index], new BaseEvaluator());
		StepTree stepBuffer = new StepTree(boards[index], tree);
		boards[index].print(System.out);
		long time = System.currentTimeMillis();
		stepBuffer.moveTree.searchForMoves(stepBuffer, false);
		System.out.println("Time = " + (System.currentTimeMillis() - time));
		int numMovesGenerated = tree.getFirstNumber();
		assertEquals(correctMoveCount[index], numMovesGenerated);
	}

	@Test
	public void moveGen11() throws Exception {
		int index = 11;
		MoveTree tree = new MoveTree(boards[index], new BaseEvaluator());
		StepTree stepBuffer = new StepTree(boards[index], tree);
		boards[index].print(System.out);
		long time = System.currentTimeMillis();
		stepBuffer.moveTree.searchForMoves(stepBuffer, true);
		System.out.println("Time = " + (System.currentTimeMillis() - time));
		int numMovesGenerated = tree.getFirstNumber();
		assertEquals(correctMoveCount[index], numMovesGenerated);
	}

	@Test
	public void moveGen12() throws Exception {
		int index = 12;
		MoveTree tree = new MoveTree(boards[index], new BaseEvaluator());
		StepTree stepBuffer = new StepTree(boards[index], tree);
		boards[index].print(System.out);
		long time = System.currentTimeMillis();
		stepBuffer.moveTree.searchForMoves(stepBuffer, false);
		System.out.println("Time = " + (System.currentTimeMillis() - time));
		int numMovesGenerated = tree.getFirstNumber();
		assertEquals(correctMoveCount[index], numMovesGenerated);
	}

	@Test
	public void moveGen13() throws Exception {
		int index = 13;
		MoveTree tree = new MoveTree(boards[index], new BaseEvaluator());
		StepTree stepBuffer = new StepTree(boards[index], tree);
		boards[index].print(System.out);
		long time = System.currentTimeMillis();
		stepBuffer.moveTree.searchForMoves(stepBuffer, false);
		System.out.println("Time = " + (System.currentTimeMillis() - time));
		int numMovesGenerated = tree.getFirstNumber();
		assertEquals(correctMoveCount[index], numMovesGenerated);
	}

	@Test
	public void moveGen14() throws Exception {
		int index = 14;
		MoveTree tree = new MoveTree(boards[index], new BaseEvaluator());
		StepTree stepBuffer = new StepTree(boards[index], tree);
		boards[index].print(System.out);
		long time = System.currentTimeMillis();
		stepBuffer.moveTree.searchForMoves(stepBuffer, true);
		System.out.println("Time = " + (System.currentTimeMillis() - time));
		System.out.println("Ran search for steps " + stepBuffer.numTimes);
		int numMovesGenerated = tree.getFirstNumber();
		assertEquals(correctMoveCount[index], numMovesGenerated);
	}

	@Test
	public void moveGen15() throws Exception {
		int index = 15;
		MoveTree tree = new MoveTree(boards[index], new BaseEvaluator());
		StepTree stepBuffer = new StepTree(boards[index], tree);
		boards[index].print(System.out);
		long time = System.currentTimeMillis();
		stepBuffer.moveTree.searchForMoves(stepBuffer, true);
		System.out.println("Time = " + (System.currentTimeMillis() - time));
		int numMovesGenerated = tree.getFirstNumber();
		assertEquals(correctMoveCount[31], numMovesGenerated);
	}

	@After
	public void cleanup() throws Exception {
		System.gc();
		Thread.sleep(10);
	}
}
