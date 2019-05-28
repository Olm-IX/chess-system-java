package chess;

import boardGame.Board;
import boardGame.Position;
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
	
	//COLOCANDO AS PE�AS NO TABULEIRO
	private void initialSetup() {
		board.placePiece(new Rook(board, Color.WHITE), new Position(2, 1));
		board.placePiece(new King(board, Color.BLACK), new Position(0, 4));
		board.placePiece(new King(board, Color.WHITE), new Position(7, 4));
	}
}
