package chess.pieces;

import boardGame.Board;
import boardGame.Position;
import chess.ChessPiece;
import chess.Color;

public final class King extends ChessPiece {

	public King(Board board, Color color) {
		super(board, color);
	}

	@Override
	public String toString() {
		return "K";
	}

	// Cria matriz booleana com valor "true" nas posições em que é possível o movimento da peça
	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
		Position p = new Position(0, 0);

		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {

				if (i == 0 && j == 0) {
					continue; // Quando 'i' e 'j' forem '0' irá ignorar o código abaixo e voltará para bloco for
				}

				p.setValues(position.getRow() + i, position.getColumn() + j);

				 if (getBoard().positionExists(p) && (!getBoard().thereIsAPiece(p) || isThereOpponentPiece(p))) {
					mat[p.getRow()][p.getColumn()] = true;
				}
			}
		}
		return mat;
	}
}
