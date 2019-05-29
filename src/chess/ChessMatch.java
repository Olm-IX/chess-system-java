package chess;

import boardGame.Board;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {

	private Board board;
	
	//QUEM DEVE SABER A DIMENS�O DO TABULEIRO DE XADREZ � A CLASSE
	//CHESSMATCH, N�O O TABULEIRO!
	//O CHESSMATCH CONTER� TODAS AS REGRAS DO JOGO
	public ChessMatch() {
		board = new Board (8, 8);
		initialSetup();
	}
	
	//A CAMADA DE CHESS N�O DEVE ACESSAR A CLASSE DO TIPO PIECE,
	//QUE PERTENCE A OUTRA CAMADA! E SIM O CHESSPIECE, SUBCLASSE, DA CAMADA CHESS
	public ChessPiece[][] getPieces() {
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
		for (int i=0; i<board.getRows(); i++) {
			for (int j=0; j<board.getColumns(); j++) {
				mat[i][j] = (ChessPiece) board.piece(i,j); // DOWNCASTING
			}
		}
		return mat;
	}
	
	private void placeNewPiece (char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
	}
	
	//COLOCANDO AS PE�AS NO TABULEIRO
	private void initialSetup() {
		placeNewPiece('b', 6, new Rook (board, Color.WHITE));
		placeNewPiece('e', 8, new King (board, Color.BLACK));
		placeNewPiece('e', 1, new King (board, Color.WHITE));
	}
}
