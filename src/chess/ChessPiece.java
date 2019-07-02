package chess;

import boardGame.Board;
import boardGame.Piece;
import boardGame.Position;

/* 
 * Como é subclasse da classe abstrata Piece, a ChessPiece também deve ser abstrata
 * ou então implementar os métodos abstratos da classe mãe. Como os métodos só serão
 * implementados nas outras subclasses, essa classe será também abstrata
 */

public abstract class ChessPiece extends Piece {
	
	private Color color;
	private int moveCount;

	// Construtor
	public ChessPiece(Board board, Color color) {
		super(board);
		this.color = color;
	}

	// Getters and Setters
	public Color getColor() {
		return color;
	}
	
	public ChessPosition getChessPosition() {
		return ChessPosition.fromPosition(position);
	}
	
	public int getMoveCount() {
		return moveCount;
	}
	
	// Métodos
	
	// Protected para ser apenas acessível para o mesmo pacote e subclasses
	// Métodos protected aparecem precedidos de uma a cerquilha no UML
	// Verifica se a a peça é da cor adversária (true)
	protected boolean isThereOpponentPiece(Position position) {
		ChessPiece p = (ChessPiece) getBoard().piece(position);
		return p != null & p.getColor() != color;
	}
	
	public void increaseMoveCount() {
		moveCount++;
	}
	
	public void decreaseMoveCount() {
		moveCount--;
	}

	
}
