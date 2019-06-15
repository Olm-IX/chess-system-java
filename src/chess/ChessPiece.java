package chess;

import boardGame.Board;
import boardGame.Piece;
import boardGame.Position;

/* 
 * Como � subclasse da classe abstrata Piece, a ChessPiece tamb�m deve ser abstrata
 * ou ent�o implementar os m�todos abstratos da classe m�e. Como os m�todos s� ser�o
 * implementados nas outras subclasses, essa classe ser� tamb�m abstrata
 */

public abstract class ChessPiece extends Piece {
	private Color color;

	// Construtor
	public ChessPiece(Board board, Color color) {
		super(board);
		this.color = color;
	}

	// Encapsulamento
	public Color getColor() {
		return color;
	}
	
	// M�todos
	
	// Protected para ser apenas acess�vel para o mesmo pacote e subclasses
	// Verifica se a a pe�a � da cor advers�ria (true)
	protected boolean isThereOpponentPiece(Position position) {
		ChessPiece p = (ChessPiece) getBoard().piece(position);
		return p != null & p.getColor() != color;
	}

	
}
