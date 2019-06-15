package chess;

import boardGame.Board;
import boardGame.Piece;

/* 
 * Como � subclasse da classe abstrata Piece, a ChessPiece tamb�m deve ser abstrata
 * ou ent�o implementar os m�todos abstratos da classe m�e. Como os m�todos s� ser�o
 * implementados nas outras subclasses, essa classe ser� tamb�m abstrata
 */

public abstract class ChessPiece extends Piece {
	private Color color;

	public ChessPiece(Board board, Color color) {
		super(board);
		this.color = color;
	}

	public Color getColor() {
		return color;
	}

	
}
