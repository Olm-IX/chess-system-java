package chess;

import java.util.ArrayList;
import java.util.List;

import boardGame.Board;
import boardGame.Piece;
import boardGame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {
// ChessMatch conterá todas as regras do jogo de xadrez
	
	private Board board;
	private int turn;
	private Color currentPlayer;
	private List<Piece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();
	
	// Construtor
	public ChessMatch() {
		board = new Board (8, 8); // Quem define a dimensão de um tabuleiro de xadrez é a classe ChessMatch, e não o Board
		turn = 1;
		currentPlayer = Color.WHITE;
		initialSetup();
	}
	
	// Getters
	public int getTurn() {
		return turn;
	}
	
	public Color getCurrentPlayer() {
		return currentPlayer;
	}
	
	//Métodos
	
	// Downcasting da matriz de Piece para uma matriz de ChessPiece
	public ChessPiece[][] getPieces() {
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
		for (int i=0; i<board.getRows(); i++) {
			for (int j=0; j<board.getColumns(); j++) {
				mat[i][j] = (ChessPiece) board.piece(i,j); // DOWNCASTING
			}
		}
		//A camada Chess não deve acessar a classe Piece
		//que pertence a outra camada. E sim o ChessPiece, subclasse da camada Chess
		return mat;
	}
	
	// Cria uma matriz booleana com o valor "true" para os movimentos possíveis da peça que está na posição dada
	public boolean [][] possibleMoves(ChessPosition sourcePosition){
		Position position = sourcePosition.toPosition();
		validateSourcePosition(position);
		return board.piece(position).possibleMoves();
	}
	
	// Converte posição fornecida para posição de matriz, valida se posição e movimento são possíveis, e executa o movimento
	// Retorna a peça que estava na posição destino (ou nulo)
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		validateSourcePosition(source); // Verifica se há peça na posição inicial e se não está "presa"
		validateTargetPosition(source, target); // Verifica se a peça escolhida pode ser movida para a posição destino
		Piece capturedPiece = makeMove(source, target);
		nextTurn(); // Troca o turno
		return (ChessPiece)capturedPiece;
	}
	
	// Executa o movimento no tabuleiro
	private Piece makeMove(Position source, Position target) {
		Piece p = board.removePiece(source);
		Piece capturedPiece = board.removePiece(target);
		board.placePiece(p, target);
		
		//Se houver peça na posição destino, a mesma é retirada da lista piecesOnTheBoard, e acrescentada na lista capturedPieces
		if (capturedPiece != null) { 
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}
		return (ChessPiece) capturedPiece;
	}
	
	// Verifica se é possível mover a peça da posição inicial
	private void validateSourcePosition(Position position) {
		// Verifica se há peça na posição
		if (!board.thereIsAPiece(position)) {
			throw new ChessException("There is no piece on source position");
		}
		// Verifica se a peça na posição escolhida pertence ao jogador do turno
		if (currentPlayer != ((ChessPiece)board.piece(position)).getColor()) {
			throw new ChessException("The chosen piece is not yours");
		}
		// Verifica se há movimentos possíveis (peça não está "presa")
		if (!board.piece(position).isThereAnyPossibleMove()) {
			throw new ChessException("There is no possible moves for the chosen piece");
		}
	}
	
	// Verifica se é possível mover a peça escolhida para a posição final
	private void validateTargetPosition(Position source, Position target) {
		// Se para a peça de origem a posição de destino não é um movimento possível
		// então, lançar excessão de movimento não possível
		if (!board.piece(source).possibleMove(target)) {
			throw new ChessException("The chosen piece can't move to target position");
		}
	}
	
	// Próximo turno da partida, muda a cor do jogador atual
	private void nextTurn() {
		turn ++;
		currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
		// Expressão condicional ternária: ( condição ) ? valor_se_verdadeiro : valor_se_falso
	}
	
	// Coloca nova peça no tabuleiro, usado no initial Setup
	private void placeNewPiece (char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		// Cada peça colocada é também inserida na lista de peças no tabuleiro, piecesOnTheBoard
		piecesOnTheBoard.add(piece);
	}
	
	// Coloca as peças no tabuleiro
	private void initialSetup() {
		placeNewPiece('c', 1, new Rook(board, Color.WHITE));
        placeNewPiece('c', 2, new Rook(board, Color.WHITE));
        placeNewPiece('d', 2, new Rook(board, Color.WHITE));
        placeNewPiece('e', 2, new Rook(board, Color.WHITE));
        placeNewPiece('e', 1, new Rook(board, Color.WHITE));
        placeNewPiece('d', 1, new King(board, Color.WHITE));

        placeNewPiece('c', 7, new Rook(board, Color.BLACK));
        placeNewPiece('c', 8, new Rook(board, Color.BLACK));
        placeNewPiece('d', 7, new Rook(board, Color.BLACK));
        placeNewPiece('e', 7, new Rook(board, Color.BLACK));
        placeNewPiece('e', 8, new Rook(board, Color.BLACK));
        placeNewPiece('d', 8, new King(board, Color.BLACK));
	}
}
