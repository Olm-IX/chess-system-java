package chess;

import boardGame.Board;
import boardGame.Piece;

/* 
 * Como é subclasse da classe abstrata Piece, a ChessPiece também deve ser abstrata
 * ou então implementar os métodos abstratos da classe mãe. Como os métodos só serão
 * implementados nas outras subclasses, essa classe será também abstrata
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
