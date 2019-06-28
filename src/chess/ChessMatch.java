package chess;

import java.util.ArrayList;
import java.util.List;

import boardGame.Board;
import boardGame.Piece;
import boardGame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {
// ChessMatch conter� todas as regras do jogo de xadrez
	
	private Board board;
	private int turn;
	private Color currentPlayer;
	private List<Piece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();
	
	// Construtor
	public ChessMatch() {
		board = new Board (8, 8); // Quem define a dimens�o de um tabuleiro de xadrez � a classe ChessMatch, e n�o o Board
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
	
	//M�todos
	
	// Downcasting da matriz de Piece para uma matriz de ChessPiece
	public ChessPiece[][] getPieces() {
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
		for (int i=0; i<board.getRows(); i++) {
			for (int j=0; j<board.getColumns(); j++) {
				mat[i][j] = (ChessPiece) board.piece(i,j); // DOWNCASTING
			}
		}
		//A camada Chess n�o deve acessar a classe Piece
		//que pertence a outra camada. E sim o ChessPiece, subclasse da camada Chess
		return mat;
	}
	
	// Cria uma matriz booleana com o valor "true" para os movimentos poss�veis da pe�a que est� na posi��o dada
	public boolean [][] possibleMoves(ChessPosition sourcePosition){
		Position position = sourcePosition.toPosition();
		validateSourcePosition(position);
		return board.piece(position).possibleMoves();
	}
	
	// Converte posi��o fornecida para posi��o de matriz, valida se posi��o e movimento s�o poss�veis, e executa o movimento
	// Retorna a pe�a que estava na posi��o destino (ou nulo)
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		validateSourcePosition(source); // Verifica se h� pe�a na posi��o inicial e se n�o est� "presa"
		validateTargetPosition(source, target); // Verifica se a pe�a escolhida pode ser movida para a posi��o destino
		Piece capturedPiece = makeMove(source, target);
		nextTurn(); // Troca o turno
		return (ChessPiece)capturedPiece;
	}
	
	// Executa o movimento no tabuleiro
	private Piece makeMove(Position source, Position target) {
		Piece p = board.removePiece(source);
		Piece capturedPiece = board.removePiece(target);
		board.placePiece(p, target);
		
		//Se houver pe�a na posi��o destino, a mesma � retirada da lista piecesOnTheBoard, e acrescentada na lista capturedPieces
		if (capturedPiece != null) { 
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}
		return (ChessPiece) capturedPiece;
	}
	
	// Verifica se � poss�vel mover a pe�a da posi��o inicial
	private void validateSourcePosition(Position position) {
		// Verifica se h� pe�a na posi��o
		if (!board.thereIsAPiece(position)) {
			throw new ChessException("There is no piece on source position");
		}
		// Verifica se a pe�a na posi��o escolhida pertence ao jogador do turno
		if (currentPlayer != ((ChessPiece)board.piece(position)).getColor()) {
			throw new ChessException("The chosen piece is not yours");
		}
		// Verifica se h� movimentos poss�veis (pe�a n�o est� "presa")
		if (!board.piece(position).isThereAnyPossibleMove()) {
			throw new ChessException("There is no possible moves for the chosen piece");
		}
	}
	
	// Verifica se � poss�vel mover a pe�a escolhida para a posi��o final
	private void validateTargetPosition(Position source, Position target) {
		// Se para a pe�a de origem a posi��o de destino n�o � um movimento poss�vel
		// ent�o, lan�ar excess�o de movimento n�o poss�vel
		if (!board.piece(source).possibleMove(target)) {
			throw new ChessException("The chosen piece can't move to target position");
		}
	}
	
	// Pr�ximo turno da partida, muda a cor do jogador atual
	private void nextTurn() {
		turn ++;
		currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
		// Express�o condicional tern�ria: ( condi��o ) ? valor_se_verdadeiro : valor_se_falso
	}
	
	// Coloca nova pe�a no tabuleiro, usado no initial Setup
	private void placeNewPiece (char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		// Cada pe�a colocada � tamb�m inserida na lista de pe�as no tabuleiro, piecesOnTheBoard
		piecesOnTheBoard.add(piece);
	}
	
	// Coloca as pe�as no tabuleiro
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
