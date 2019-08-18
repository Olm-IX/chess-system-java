package chess.pieces;

import boardGame.Board;
import boardGame.Position;
import chess.ChessPiece;
import chess.Color;
import chess.ChessMatch;

public final class Pawn extends ChessPiece {

	private ChessMatch chessMatch;
	
	public Pawn(Board board, Color color, ChessMatch chessMatch) {
		super(board, color);
		this.chessMatch = chessMatch;
	}
	
	@Override
	public String toString() {
		return "P";
	}

	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
		Position p = new Position(0, 0);

		// COR BRANCA
		if (getColor() == Color.WHITE) {
			// Pode mover uma posição vertical para frente por jogada
			p.setValues(position.getRow() - 1, position.getColumn());
			if (getBoard().positionExists(p) && (!getBoard().thereIsAPiece(p) || isThereOpponentPiece(p))) {
				mat[p.getRow()][p.getColumn()] = true;
				// Se estiver na posição inicial, pode mover duas posições verticais para frente na primeira jogada
				if (!isThereOpponentPiece(p) && getMoveCount() == 0) {
					p.setValues(position.getRow() - 2, position.getColumn());
					if (!getBoard().thereIsAPiece(p) || isThereOpponentPiece(p)) {
						mat[p.getRow()][p.getColumn()] = true;
					}
				}
			}
			// Se tiver peça inimiga na posição diagonal para frente, esquerda ou direita, pode mover
			p.setValues(position.getRow() - 1, position.getColumn() - 1);
			if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			p.setValues(position.getRow() - 1, position.getColumn() + 1);
			if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			// Verifica a jogada En Passant
			if (position.getRow() == 3) {
				Position left = new Position(position.getRow(), position.getColumn() - 1);
				if (getBoard().positionExists(left) && isThereOpponentPiece(left) && getBoard().piece(left) == chessMatch.getEnPassantVulnerable()) {
					mat[left.getRow() - 1][left.getColumn()] = true;
				}
				Position right = new Position(position.getRow(), position.getColumn() + 1);
				if (getBoard().positionExists(right) && isThereOpponentPiece(right) && getBoard().piece(right) == chessMatch.getEnPassantVulnerable()) {
					mat[right.getRow() - 1][right.getColumn()] = true;
				}
			}
		} else {
			// COR PRETA
			if (getColor() == Color.BLACK) {
				p.setValues(position.getRow() + 1, position.getColumn());
				if (getBoard().positionExists(p) && (!getBoard().thereIsAPiece(p) || isThereOpponentPiece(p))) {
					mat[p.getRow()][p.getColumn()] = true;
					if (!isThereOpponentPiece(p) && getMoveCount() == 0) {
						p.setValues(position.getRow() + 2, position.getColumn());
						if (!getBoard().thereIsAPiece(p) || isThereOpponentPiece(p)) {
							mat[p.getRow()][p.getColumn()] = true;
						}
					}
				}
				p.setValues(position.getRow() + 1, position.getColumn() - 1);
				if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
					mat[p.getRow()][p.getColumn()] = true;
				}
				p.setValues(position.getRow() + 1, position.getColumn() + 1);
				if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
					mat[p.getRow()][p.getColumn()] = true;
				}
			}
			// Verifica a jogada En Passant
			if (position.getRow() == 4) {
				Position left = new Position(position.getRow(), position.getColumn() - 1);
				if (getBoard().positionExists(left) && isThereOpponentPiece(left) && getBoard().piece(left) == chessMatch.getEnPassantVulnerable()) {
					mat[left.getRow() + 1][left.getColumn()] = true;
				}
				Position right = new Position(position.getRow(), position.getColumn() + 1);
				if (getBoard().positionExists(right) && isThereOpponentPiece(right) && getBoard().piece(right) == chessMatch.getEnPassantVulnerable()) {
					mat[right.getRow() + 1][right.getColumn()] = true;
				}
			}	

		}
		return mat;
	}
}
